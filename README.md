# buildingblocks-i18n

DB-backed internationalization: `(namespace, code, locale) → content`, ordered-locale resolve in **one query**, optional Spring `MessageSource` and REST admin API.

| | |
|--|--|
| Artifact | `org.digitalmind.buildingblocks.i18n:i18n-core` |
| Module | `i18n-core` |
| Current line | **4.1.0** (`spring-4.1.0`) |
| Full contracts | [`releases/`](releases/) |
| Licence | **DMSAL v1.0** (source-available; inspection only unless Articles 5, 6, or 9) — [`LICENSE`](LICENSE.md) |

Consumer owns Liquibase execution and the `CacheManager`. Agent notes: [`ai/README.md`](ai/README.md).

## Licence

Licensed under the **Digital Mind Source-Available License (DMSAL) v1.0**. Public Source Code is for inspection only; Use requires Articles 5, 6, or 9 of the licence.

Full text: [`LICENSE`](LICENSE.md).

## Overview

| Need | Use |
|------|-----|
| In-process lookup | `I18nService.translate(...)` |
| Spring `MessageSource` | Opt-in bean `i18nMessageSource` |
| HTTP admin CRUD / resolve | Opt-in REST (`api.enabled`, default **false**) |

```text
buildingblocks-i18n/
  README.md                  # this index
  releases/README-<version>.md
  i18n-core/
  ai/
```

Table `i18n`: unique `(namespace, code, locale)`; locale canonical lowercase `_` → `-`. Miss → synthetic row `id = 0`, `content = code` (not cached).

## Interfaces

### `I18nService`

| Method | Role |
|--------|------|
| `translate(namespace, code, List<?> locales)` | Ordered preference; elements `String` / `Locale` / `CharSequence` → one SQL, first match |
| `translate(namespace, code, String locale)` | That locale, then config `default-locale` |
| `translate(namespace, code, Locale locale)` | `preferenceList`: tag → language → default |
| `translate(code, String\|Locale locale)` | Namespace `default` |
| `findByNamespaceAndCodeAndLocale(..., String\|Locale)` | Exact match; may be null |
| `deleteByNamespaceAndCodeAndLocale(..., String\|Locale)` | Evicts cache |
| `getOne(id)` / `save` / `deleteById` / `clearCache` | CRUD + cache eviction |

Related: `I18nLocaleUtil` (normalize / ordered / preference), `I18nMessageFormatUtil` + `I18nBraceMatcher` (MessageSource path), Liquibase `classpath:db/changelog/i18n/db.changelog-master.xml`.

## Usage

```yaml
application.modules.common.i18n:
  enabled: true
  default-locale: en
  api:
    enabled: false
  message-source:
    enabled: false
```

Also: JPA packages from `I18nCoreModuleConfig`, component-scan `CONFIG_PACKAGE`, cache name exactly **`i18n-cache`**.

```java
I18n row = i18nService.translate("default", "greeting.welcome", List.of("ro-ro", "ro", "en"));
I18n row2 = i18nService.translate("default", "greeting.welcome", "ro-RO");
```

```xml
<dependency>
  <groupId>org.digitalmind.buildingblocks.i18n</groupId>
  <artifactId>i18n-core</artifactId>
  <version>4.1.0</version>
</dependency>
```

Storage: DB `content` = plain display text (`don't`, `Hello {0}`); MessageSource adapts via `I18nMessageFormatUtil`. Details per release doc.

Host migration playbook: [`ai/skills/i18n-host-migration/README.md`](ai/skills/i18n-host-migration/README.md).

## Versions

| Version | Branch | Notes | Full README |
|---------|--------|-------|-------------|
| **4.1.0** | `spring-4.1.0` | Current line: `(namespace, code, locale)`, ordered-locale resolve, bean `i18nMessageSource` | [`releases/README-4.1.0.md`](releases/README-4.1.0.md) |
| **4.0.0** | `spring-4.0.0` | Prior line (esign-evo): `(code, locale)`, bean `messageSource` | [`releases/README-4.0.0.md`](releases/README-4.0.0.md) |

**4.1.0 vs 4.0.0:** different data key and APIs — see [Differences from 4.0.0](releases/README-4.1.0.md#differences-from-400) in the 4.1.0 contract.
