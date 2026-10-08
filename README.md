# buildingblocks-i18n

Library for **DB-backed internationalization**: store `(namespace, code, locale) → content`, resolve with an ordered locale list in **one query**, optionally expose Spring `MessageSource` and a REST admin API.

| | |
|--|--|
| Artifact | `org.digitalmind.buildingblocks.i18n:i18n-core` |
| Module | `i18n-core` |
| Version | root Gradle `version` (currently `4.0.0`) |

This document is a **reusable contract** for any consuming application. It does not assume a specific product, DB name, or changelog tree beyond: the consumer owns Liquibase and the `CacheManager`.

## Purpose

| Need | Use |
|------|-----|
| In-process lookup | `I18nService.translate(...)` |
| Spring `MessageSource` | Opt-in bean `i18nMessageSource` |
| HTTP admin CRUD / resolve | Opt-in REST (`api.enabled`, default **false**) |

Not in scope: document/templating engines, or “one template file per language”.

## Layout

```text
buildingblocks-i18n/
  README.md
  i18n-core/                 # library + Liquibase + unit tests
  ai/
    rules/                   # stable contracts (Markdown)
    skills/                  # migration playbook (Markdown)
```

Agent-oriented notes: [`ai/README.md`](ai/README.md).

### Main packages (`i18n-core`)

| Package / path | Role |
|----------------|------|
| `...i18n.entity` | JPA `I18n` |
| `...i18n.repository` | JPA + custom ordered-locale query (`EntityManager` ctor injection) |
| `...i18n.service` | `I18nService` / impl (cache, normalize, miss handling) |
| `...i18n.component` | `i18nMessageSource` (opt-in) |
| `...i18n.api` | REST (opt-in) |
| `...i18n.util` | `I18nLocaleUtil`, `I18nMessageFormatUtil`, `I18nBraceMatcher` |
| `db/changelog/i18n/` | Liquibase master + `0001_create_i18n` |

## Data model

Table `i18n`:

| Column | Notes |
|--------|--------|
| `id` | Surrogate PK |
| `namespace` | Scope; default `default` |
| `code` | Stable key |
| `locale` | Canonical: lowercase, `_` → `-` (`en_US` → `en-us`) |
| `content` | **Plain display text** (see [Storage](#storage-format-one-format-for-both-apis)) |
| audit | `context_id`, `version`, `created_at` / `by`, `updated_at` / `by` |

- Unique: `(namespace, code, locale)` → `i18n_ux1`
- Index: `(namespace, locale)` → `i18n_ix1`

## Liquibase (consumer runs it)

**Principle:** the jar ships changelogs; the **consumer** owns DataSource, master changelog, and execution. This library does not open a migration JDBC connection.

### Resource

```text
classpath:db/changelog/i18n/db.changelog-master.xml
```

### Include from any consumer master

YAML:

```yaml
databaseChangeLog:
  # … consumer changeSets …

  - include:
      file: classpath:db/changelog/i18n/db.changelog-master.xml
```

XML:

```xml
<include file="classpath:db/changelog/i18n/db.changelog-master.xml"/>
```

Point the consumer Liquibase `change-log` at **its** master (the file that contains this `include`).

### ChangeSet `0001`

- Creates `i18n` **only if the table does not exist**.
- If the table exists → `MARK_RAN` (no create, **no drop**).
- Old/incompatible schema: drop or migrate **manually** (or with a consumer-owned changeSet), then align data.

## Consumer configuration

```yaml
application.modules.common.i18n:
  enabled: true
  default-locale: en
  api:
    enabled: false
  message-source:
    enabled: false
```

Also required:

1. JPA: `I18nCoreModuleConfig.ENTITY_PACKAGE` + `REPOSITORY_PACKAGE` on the persistence unit that owns `i18n`.
2. Import / component-scan `I18nCoreModuleConfig.CONFIG_PACKAGE`.
3. Cache named exactly **`i18n-cache`**.

### MessageSource (optional)

```yaml
application.modules.common.i18n.message-source.enabled: true
```

- Bean: **`i18nMessageSource`** (not global `messageSource`, not `@Primary`).
- Consumer wires it into its `MessageSource` users.
- Key: `namespace:code` (missing `:` → namespace `default`).
- Conditions: `@ConditionalOnBean(I18nService)` + property (default off).

### REST API (optional, default off)

```yaml
application.modules.common.i18n.api.enabled: true
```

Admin create/get/update/delete + resolve by `namespace` + `code` + ordered `locales`.  
Requires `I18nService` bean + `api.enabled=true`.

## Cache

Library contract = cache name `i18n-cache` only. Consumer provides `CacheManager`.

| | |
|--|--|
| Cached | `translate(namespace, code, List<String> locales)` |
| Not cached | Miss synthetic row (`id == 0`, `content = code`) |
| Evict all | `save`, deletes, `clearCache` |

Overloads (`Locale` / single locale string) delegate through a Spring proxy (`@Lazy` self) so caching still applies.

### In-memory example (Caffeine)

```text
org.springframework.boot:spring-boot-starter-cache
com.github.ben-manes.caffeine:caffeine
```

```yaml
spring:
  cache:
    type: caffeine
    cache-names: i18n-cache
    caffeine:
      spec: maximumSize=10000,expireAfterAccess=6h,expireAfterWrite=24h
```

Any other Spring `CacheManager` is fine if it exposes `i18n-cache`.

## Runtime translate

```java
I18n row = i18nService.translate(
        "default",
        "greeting.welcome",
        List.of("ro-ro", "ro", "en")
);
String text = row.getContent(); // plain text from DB
```

- One SQL: `namespace` + `code` + `locale IN (...)` ordered by list position.
- Locale normalize via `I18nLocaleUtil`.
- Miss → `id = 0`, `content = code`.
- By id: `getOne` → `findById` or `I18nNotFoundException` (HTTP 404 when API is on).

## Storage format (one format for both APIs)

**DB `content` = plain display text** (what the user should see), plus intentional placeholders like `{0}` when needed.

| API | Behaviour |
|-----|-----------|
| `I18nService.translate(...).getContent()` | Raw DB string |
| `i18nMessageSource` | `I18nMessageFormatUtil.toMessageFormatPattern` then `MessageFormat` |

Do **not** store `don''t` / `'{'` in the DB for normal rows.

Nested MessageFormat arguments (e.g. `choice` containing `{1}`) use **`I18nBraceMatcher`** (brace depth) so the pattern is not cut at the first `}`.

### Examples

| Intended UI | Store in DB | `getContent()` | MessageSource |
|-------------|-------------|----------------|---------------|
| `don't` | `don't` | `don't` | `don't` |
| `Hello Ada` (arg) | `Hello {0}` | `Hello {0}` | `Hello Ada` |
| `Use {TEST} mode` | `Use {TEST} mode` | `Use {TEST} mode` | `Use {TEST} mode` |
| `Don't use {0}` | `Don't use {0}` | `Don't use {0}` | `Don't use TEST` (+ arg) |

### Placeholders

- Real args: `{0}`, `{1}`, `{0,number}`, `{0,choice,...}` (nested braces supported via depth matching).
- Accidental `{ABC}`: store plain; adapter treats as literals.
- Legacy rows stored as MessageFormat patterns (`don''t`): unescape to plain before dual-path use.

## Tests

Unit tests (no database):

- `I18nMessageFormatUtilTest` — apostrophe, `{0}`, `{TEST}`, combined, nested `choice`
- `I18nBraceMatcherTest` — matching / balance / depth

```bash
./gradlew :i18n-core:test --tests "org.digitalmind.buildingblocks.core.i18n.util.*"
```

(Requires a JDK matching the project toolchain.)

## Publish / consume

1. Publish `i18n-core` to your Maven repository.
2. Consumer: dependency + Liquibase include + JPA packages + `i18n-cache` + `enabled: true`.
3. Optionally enable `message-source` / `api`.

Playbook: [`ai/skills/i18n-host-migration/README.md`](ai/skills/i18n-host-migration/README.md).
