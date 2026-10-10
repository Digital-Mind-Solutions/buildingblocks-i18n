# buildingblocks-i18n — release 4.0.0

Library for **DB-backed internationalization**: store `(code, locale) → content`, resolve via `I18nService.translate`, optionally expose Spring `MessageSource` and a REST admin API.

| | |
|--|--|
| Artifact | `org.digitalmind.buildingblocks.i18n:i18n-core` |
| Module | `i18n-core` |
| Branch | `spring-4.0.0` |
| Version | `4.0.0` (Gradle root `version`) |
| Tip | `esign-evo` (`f16268c`) — Spring Boot **4.0.3**, Java **25** |
| Licence | **DMSAL** — `dmsal_license_version` in [`../gradle.properties`](../gradle.properties); Git tag `v{ver}` + `v{ver}/LICENSE.md`; plugin `org.digitalmind.dmsal-license:{ver}` → `META-INF/LICENSE.md` |
| Doc | [`releases/README-4.0.0.md`](README-4.0.0.md) |

This document is the **full contract** for version **4.0.0** as shipped on this branch. Overview / index: [`../README.md`](../README.md).

## Purpose

| Need | Use |
|------|-----|
| In-process lookup | `I18nService.translate(...)` |
| Spring `MessageSource` | Bean name **`messageSource`** (when module `enabled`) |
| HTTP admin CRUD / search | Opt-in REST (`api.enabled`) |

## Layout

```text
buildingblocks-i18n/
  README.md                  # overview index
  releases/                  # per-version full contracts
  i18n-core/                 # library
```

### Main packages (`i18n-core`)

| Package / path | Role |
|----------------|------|
| `...i18n.entity` | JPA `I18n` |
| `...i18n.repository` | Spring Data `I18nRepository` |
| `...i18n.service` | `I18nService` / `I18nServiceImpl` |
| `...i18n.component` | `I18nMessageSource` (`messageSource`) |
| `...i18n.api` | REST (`I18nController`) |
| `...i18n.dto` | `I18nSearchOperator` |
| `...i18n.config` | `I18nConfig`, `I18nCoreModuleConfig`, OpenAPI |

## Data model

Table `i18n`:

| Column | Notes |
|--------|--------|
| `id` | Surrogate PK |
| `code` | Stable key |
| `locale` | Locale string as stored / queried (no separate normalize util in this line) |
| `content` | Translation text |
| audit | from `ContextVersionableAuditModel` (`jpautils`) |

- Unique: `(code, locale)` → `i18n_ux1`
- **No `namespace` column** on this line

Schema ownership: **consumer** (this jar does not ship Liquibase changelogs on `esign-evo`).

## Consumer configuration

```yaml
application.modules.common.i18n:
  enabled: true
  default-locale: en
  api:
    enabled: false
    docket:
      base-path: /api/i18n
```

Also required:

1. JPA: `I18nCoreModuleConfig.ENTITY_PACKAGE` + `REPOSITORY_PACKAGE` on the persistence unit that owns `i18n`.
2. Import / component-scan `I18nCoreModuleConfig.CONFIG_PACKAGE`.
3. Cache named exactly **`i18n-cache`**.

### MessageSource

Enabled with the module (`enabled: true`). Bean name: **`messageSource`**.

- Resolves via `translate(code, locale.getLanguage())` then `MessageFormat` on `content`.

### REST API (optional)

```yaml
application.modules.common.i18n.api.enabled: true
```

Base path from `api.docket.base-path`. Create / get / list (search operator) / update / delete.

## `I18nService` API

### Translate

| Method | Behaviour |
|--------|-----------|
| `translate(code, Locale locale)` | Lookup `locale.toString()`; if miss, `locale.getLanguage()`; if still miss → synthetic `id = 0`, `content = code`. Cached. |
| `translate(code, String locale)` | Lookup exact `locale`; if miss, config `defaultLocale`; if still miss → synthetic. Cached. |

```java
I18n row = i18nService.translate("greeting.welcome", Locale.forLanguageTag("ro-RO"));
I18n row2 = i18nService.translate("greeting.welcome", "ro_RO");
```

Callers that need “no translation” must check `id == 0` (or equivalent), not only null.

### Exact / search / CRUD

| Method | Notes |
|--------|--------|
| `findByCodeAndLocale(code, locale)` | Exact; may return null |
| `findByCodeAndLocale(code, operator, locale, pageable)` | `EQUALS` / `START_WITH` / `CONTAINS` + locale `startingWith` |
| `getOne(id)` | By id |
| `save` / `deleteById` / `deleteByCodeAndLocale` | Persist / remove |
| `clearCache` | Evict all `i18n-cache` |

## Cache

| | |
|--|--|
| Name | `i18n-cache` (`I18nCoreModuleConfig.CACHE_NAME`) |
| Cached | both `translate` overloads |
| Evict | `clearCache` (all entries) |
| Provider | consumer `CacheManager` |

## Publish / consume

1. Publish `i18n-core` `4.0.0` to your Maven repository.
2. Consumer: dependency + JPA packages + `i18n-cache` + `enabled: true`.
3. Optionally enable `api`.

## Related line

Newer namespace / ordered-locale / jar Liquibase work lives on **`spring-4.1.0`**, not on this tip.
