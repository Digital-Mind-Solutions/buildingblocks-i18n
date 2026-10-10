# buildingblocks-i18n — release 4.1.0

Library for **DB-backed internationalization**: store `(namespace, code, locale) → content`, resolve with an ordered locale preference list in **one query**, optionally expose Spring `MessageSource` and a REST admin API.

| | |
|--|--|
| Artifact | `org.digitalmind.buildingblocks.i18n:i18n-core` |
| Module | `i18n-core` |
| Branch | `spring-4.1.0` |
| Version | `4.1.0` |
| Stack | Spring Boot **4.0.3** (library plugins) |
| Licence | **DMSAL v1.0** — [`dmsal-license` `@v1.0`](https://github.com/Digital-Mind-Solutions/dmsal-license/blob/v1.0/v1.0/LICENSE.md); no `LICENSE.md` in-repo; `includeBuild('../dmsal-license')` → `META-INF/LICENSE.md` in the JAR |
| Doc | [`releases/README-4.1.0.md`](README-4.1.0.md) |
| Prior line | [`README-4.0.0.md`](README-4.0.0.md) — `(code, locale)` / esign-evo (different contract) |

This document is the **full contract** for version **4.1.0**. Overview / index: [`../README.md`](../README.md). The consumer owns Liquibase execution and the `CacheManager`.

## Differences from 4.0.0

`4.0.0` on `spring-4.0.0` (esign-evo) is a **different** contract. Do not treat 4.1.0 as a drop-in rename of that line.

| | **4.0.0** (`spring-4.0.0`) | **4.1.0** (`spring-4.1.0`) |
|--|---------------------------|----------------------------|
| Logical key | `(code, locale)` | `(namespace, code, locale)` |
| Locale resolve | Exact / fallback as in 4.0.0 service | Ordered preference list in **one** SQL (`List<?>` / `preferenceList`) |
| Locale form | As documented for 4.0.0 | Canonical lowercase, `_` → `-` |
| MessageSource bean | **`messageSource`** (when module enabled) | Opt-in **`i18nMessageSource`** (`message-source.enabled`, not `@Primary`) |
| REST | Opt-in admin / search | Opt-in admin / resolve with ordered `locales` |
| Schema ownership | Consumer (see 4.0.0 doc) | Consumer runs Liquibase; jar ships `db/changelog/i18n/` |
| Agent / migration docs | Not on that line | [`../ai/`](../ai/) + host-migration skill |

Full prior-line contract: [`README-4.0.0.md`](README-4.0.0.md).

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
  README.md                  # overview index
  releases/                  # per-version full contracts
  i18n-core/                 # library + Liquibase + unit tests
  ai/
    rules/                   # stable contracts (Markdown)
    skills/                  # migration playbook (Markdown)
```

Agent-oriented notes: [`../ai/README.md`](../ai/README.md).

### Main packages (`i18n-core`)

| Package / path | Role |
|----------------|------|
| `...i18n.entity` | JPA `I18n` |
| `...i18n.repository` | JPA + custom ordered-locale query (`EntityManager` ctor injection) |
| `...i18n.service` | `I18nService` / `I18nServiceImpl` (cache, normalize, miss) |
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

**Principle:** the jar ships changelogs; the **consumer** owns DataSource, master changelog, and execution.

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

### ChangeSet `0001`

- Creates `i18n` **only if the table does not exist**.
- If the table exists → `MARK_RAN` (no create, **no drop**).
- Old/incompatible schema: drop or migrate **manually** (consumer-owned changeSet), then align data.

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

Admin create/get/update/delete + resolve by `namespace` + `code` + ordered `locales` (`List<String>` query params → `translate(..., List<?>)`).

## `I18nService` API

### Translate

| Method | Behaviour |
|--------|-----------|
| `translate(namespace, code, List<?> locales)` | Preferred list API. Elements: `String`, `Locale`, or `CharSequence`. Accepts `List<String>` / `List<Locale>` directly. Normalize + distinct (first wins) → one SQL, first match. |
| `translate(namespace, code, String locale)` | Preference: that locale, then `default-locale` from config. |
| `translate(namespace, code, Locale locale)` | Preference chain via `I18nLocaleUtil.preferenceList` (tag → language → default). |
| `translate(code, String locale)` | Same as above with namespace `default`. |
| `translate(code, Locale locale)` | Same as above with namespace `default`. |

```java
// List<String> — works with List<?>
I18n row = i18nService.translate("default", "greeting.welcome", List.of("ro-ro", "ro", "en"));

// single locale
I18n row2 = i18nService.translate("default", "greeting.welcome", "ro-RO");
I18n row3 = i18nService.translate("default", "greeting.welcome", Locale.forLanguageTag("ro-RO"));
```

- Miss → synthetic row: `id = 0`, `content = code` (**not** cached). Callers that need “no translation” must check `id == 0` (or equivalent), not only null.
- Empty preference list after normalize → uses config `default-locale` if set.

### Exact / CRUD

| Method | Notes |
|--------|--------|
| `findByNamespaceAndCodeAndLocale(..., String\|Locale)` | Exact match (normalized locale); may return null |
| `deleteByNamespaceAndCodeAndLocale(..., String\|Locale)` | Evicts cache |
| `getOne(id)` | Or `I18nNotFoundException` |
| `save` / `deleteById` / `clearCache` | Evict all `i18n-cache` |

## Locale utilities (`I18nLocaleUtil`)

| Method | Role |
|--------|------|
| `normalize(String\|Locale)` | Canonical lowercase, `_` → `-` |
| `normalizeOrdered(List<String>)` | Normalize, drop blank, **distinct preserving first occurrence** |
| `normalizeOrderedLocales(List<Locale>)` | Same for `Locale` |
| `normalizeOrderedObjects(List<?>)` | Same for mixed `String` / `Locale` / `CharSequence`; unsupported type → `IllegalArgumentException` |
| `preferenceList(Locale, defaultLocale)` | tag → language → default |

## Cache

Library contract = cache name `i18n-cache` only. Consumer provides `CacheManager`.

| | |
|--|--|
| Cached | `I18nServiceImpl.translateForStringLocales` (**impl-only**, not on `I18nService`) |
| Public path | All public `translate` overloads → normalize → cached method via `@Lazy I18nServiceImpl` self |
| Not cached | Miss (`id == 0`) |
| Evict all | `save`, deletes, `clearCache` |

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

## Storage format (one format for both APIs)

**DB `content` = plain display text** (what the user should see), plus intentional placeholders like `{0}` when needed.

| API | Behaviour |
|-----|-----------|
| `I18nService.translate(...).getContent()` | Raw DB string |
| `i18nMessageSource` | `I18nMessageFormatUtil.toMessageFormatPattern` then `MessageFormat` |

Do **not** store `don''t` / `'{'` in the DB for normal rows.

Nested MessageFormat arguments (e.g. `choice` containing `{1}`) use **`I18nBraceMatcher`** (brace depth).

### Examples

| Intended UI | Store in DB | `getContent()` | MessageSource |
|-------------|-------------|----------------|---------------|
| `don't` | `don't` | `don't` | `don't` |
| `Hello Ada` (arg) | `Hello {0}` | `Hello {0}` | `Hello Ada` |
| `Use {TEST} mode` | `Use {TEST} mode` | `Use {TEST} mode` | `Use {TEST} mode` |
| `Don't use {0}` | `Don't use {0}` | `Don't use {0}` | `Don't use TEST` (+ arg) |

### Placeholders

- Real args: `{0}`, `{1}`, `{0,number}`, `{0,choice,...}` (nested braces via depth matching).
- Accidental `{ABC}`: store plain; adapter treats as literals.
- Legacy MessageFormat-escaped rows (`don''t`): unescape to plain before dual-path use.

## Tests

Unit tests (no database):

- `I18nLocaleUtilTest` — `normalizeOrderedObjects` (null/empty, distinct order, mixed `Locale`/`String`/`CharSequence`, unsupported type)
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

Playbook: [`../ai/skills/i18n-host-migration/README.md`](../ai/skills/i18n-host-migration/README.md).
