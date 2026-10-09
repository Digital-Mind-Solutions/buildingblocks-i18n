# buildingblocks-i18n

DB-backed internationalization: `(code, locale) → content`, optional Spring `MessageSource` and REST admin API.

| | |
|--|--|
| Artifact | `org.digitalmind.buildingblocks.i18n:i18n-core` |
| Module | `i18n-core` |
| Current line | **4.0.0** (`spring-4.0.0`, tip `esign-evo`) |
| Full contracts | [`releases/`](releases/) |
| Licence | **DMSAL v1.0** (source-available; inspection only unless Articles 5, 6, or 9) — [`LICENSE`](LICENSE) |

Consumer owns schema/migrations and the `CacheManager`.

## Licence

Licensed under the **Digital Mind Source-Available License (DMSAL) v1.0**. Public Source Code is for inspection only; Use requires Articles 5, 6, or 9 of the licence.

Full text: [`LICENSE`](LICENSE).

## Overview

| Need | Use |
|------|-----|
| In-process lookup | `I18nService.translate(code, Locale\|String)` |
| Spring `MessageSource` | Bean `messageSource` when module enabled |
| HTTP admin CRUD / search | Opt-in REST (`api.enabled`) |

```text
buildingblocks-i18n/
  README.md                  # this index
  releases/README-<version>.md
  i18n-core/
```

Table `i18n`: unique `(code, locale)`. Miss → synthetic row `id = 0`, `content = code`.

## Interfaces

### `I18nService`

| Method | Role |
|--------|------|
| `translate(code, Locale locale)` | Exact `locale.toString()`, then language tag; miss → synthetic |
| `translate(code, String locale)` | Exact locale, then config `defaultLocale`; miss → synthetic |
| `findByCodeAndLocale(code, locale)` | Exact match; may be null |
| `findByCodeAndLocale(code, operator, locale, pageable)` | Paged search (`EQUALS` / `START_WITH` / `CONTAINS`) |
| `getOne(id)` / `save` / `deleteById` / `deleteByCodeAndLocale` | CRUD |
| `clearCache` | Evict all `i18n-cache` |

Cache name: **`i18n-cache`**. Config prefix: `application.modules.common.i18n`.

## Usage

```yaml
application.modules.common.i18n:
  enabled: true
  default-locale: en
  api:
    enabled: false
    docket:
      base-path: /api/i18n
```

Also: JPA packages from `I18nCoreModuleConfig` (`ENTITY_PACKAGE`, `REPOSITORY_PACKAGE`), component-scan `CONFIG_PACKAGE`, cache name **`i18n-cache`**.

```java
I18n row = i18nService.translate("greeting.welcome", Locale.forLanguageTag("ro-RO"));
I18n row2 = i18nService.translate("greeting.welcome", "ro_RO");
```

```xml
<dependency>
  <groupId>org.digitalmind.buildingblocks.i18n</groupId>
  <artifactId>i18n-core</artifactId>
  <version>4.0.0</version>
</dependency>
```

## Versions

| Version | Branch | Notes | Full README |
|---------|--------|-------|-------------|
| **4.0.0** | `spring-4.0.0` | Current line (`esign-evo`); `(code, locale)` model | [`releases/README-4.0.0.md`](releases/README-4.0.0.md) |
