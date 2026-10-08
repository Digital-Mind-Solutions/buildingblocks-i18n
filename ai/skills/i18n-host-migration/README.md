# Skill: Consumer migration onto `buildingblocks-i18n`

## Purpose

Integrate or upgrade a **consuming application** to `i18n-core`: schema, Liquibase include, cache, optional MessageSource/API, plain-text content.

## When to use

- Adding/upgrading `org.digitalmind.buildingblocks.i18n:i18n-core`
- Existing `i18n` table without `namespace` (or wrong schema)
- Enabling MessageSource or REST API
- Seeding or migrating translation strings

## Read first

- [`../../rules/i18n-module-contracts.md`](../../rules/i18n-module-contracts.md)
- [`../../rules/i18n-messageformat-escaping.md`](../../rules/i18n-messageformat-escaping.md)
- Root [`../../../README.md`](../../../README.md)

## Steps

### 1. Dependency

```text
implementation 'org.digitalmind.buildingblocks.i18n:i18n-core:<version>'
```

### 2. Liquibase (consumer executes)

In the consumer master changelog:

```yaml
- include:
    file: classpath:db/changelog/i18n/db.changelog-master.xml
```

- `0001` creates `i18n` only if missing; does **not** drop.
- Old incompatible schema: drop/migrate **manually** (or consumer changeSet), then align data.

### 3. Configuration

```yaml
application.modules.common.i18n:
  enabled: true
  default-locale: en
  api:
    enabled: false
  message-source:
    enabled: false
```

### 4. JPA + scan

- Entity + repository packages from `I18nCoreModuleConfig` on the correct persistence unit.
- Config package scanned / imported so `I18nCoreModuleConfig` loads.

### 5. Cache

Define Spring cache **`i18n-cache`** (example: Caffeine in-memory — see root README).

### 6. Optional MessageSource

1. `message-source.enabled: true`
2. Bean `i18nMessageSource`
3. Wire in the consumer (any `MessageSource` user)

Do not rename the library bean to `messageSource` inside the module.

### 7. Data checklist

| Field | Action |
|-------|--------|
| `namespace` | Set (`default` if unknown) |
| `code` | Keep stable business key |
| `locale` | Normalize: lowercase, `_` → `-` |
| `content` | Plain display text (e.g. `don't`). Intentional args: `{0}`. Do not store `don''t`. |

```java
i18nService.translate("default", "greeting.welcome", List.of("ro-ro", "ro", "en"));
```

MessageSource key: `default:greeting.welcome`.

### 8. Verify

- [ ] Table has `namespace` + unique `(namespace, code, locale)`
- [ ] `translate` returns first matching locale from the list
- [ ] Optional: second translate hits cache
- [ ] If MessageSource on: `don't` and `{TEST}` / `{0}` render correctly
- [ ] REST API off unless intentionally enabled
- [ ] Optional: `./gradlew :i18n-core:test` on the library (no DB)

## Do not

- Fork module Liquibase into the consumer tree; include from the jar classpath.
- Implement locale fallback with multiple finds; use one ordered list.
- Assume REST API is required for runtime translate.
- Store MessageFormat-escaped `content` for dual-path (service + MessageSource) use.
