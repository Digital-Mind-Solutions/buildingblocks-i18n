# Rule: `buildingblocks-i18n` module contracts

## Persistence

- Table `i18n`: unique `(namespace, code, locale)` → `i18n_ux1`; index `(namespace, locale)` → `i18n_ix1`.
- Default `namespace`: `default`.
- Locale: canonical lowercase, `_` → `-` (`en_US` → `en-us`).
- Liquibase in jar: `classpath:db/changelog/i18n/db.changelog-master.xml`.
- `0001`: create if missing; `MARK_RAN` if table exists; **no drop**. Consumer migrates old schemas manually.

## Runtime service (`I18nService`)

### Translate (public)

| Signature | Notes |
|-----------|--------|
| `translate(namespace, code, List<?> locales)` | Preferred. Elements `String` / `Locale` / `CharSequence`. `List<String>` and `List<Locale>` pass directly. |
| `translate(namespace, code, String locale)` | Locale + config `default-locale` preference. |
| `translate(namespace, code, Locale locale)` | `preferenceList` (tag → language → default). |
| `translate(code, String\|Locale)` | Namespace `default`. |

### Translate (implementation only)

- `I18nServiceImpl.translateForStringLocales(namespace, code, List<String>)` — **@Cacheable**; not on the interface.
- Public overloads reach it via `@Lazy I18nServiceImpl` self after normalize.

### Other

- Exact: `findByNamespaceAndCodeAndLocale` / `deleteByNamespaceAndCodeAndLocale` with `String` or `Locale`.
- Miss: synthetic `id == 0`, `content == code` — **not** cached. Callers check miss via `id`, not only null.
- Cache name: `i18n-cache` (consumer must define it). Writes / `clearCache` evict all.
- `getById(id)` → `findById` or `I18nNotFoundException`.

## Repository

- Exact: `findByNamespaceAndCodeAndLocale`, `deleteByNamespaceAndCodeAndLocale` (`String` locale at repo).
- Ordered locales: `findFirstByNamespaceAndCodeAndLocales` (custom impl, `EntityManager` constructor injection).

## Locale util

- `normalize` / `normalizeOrdered` / `normalizeOrderedLocales` / `normalizeOrderedObjects` / `preferenceList`.
- Ordered helpers: drop blank, **distinct preserving first occurrence** (`LinkedHashSet`).
- `normalizeOrderedObjects`: unsupported element type → `IllegalArgumentException`.

## MessageSource (optional)

- Bean: `i18nMessageSource` (not global `messageSource`).
- Flag: `application.modules.common.i18n.message-source.enabled` (default false).
- `@ConditionalOnBean(I18nService)` + that property.
- Consumer wires the bean explicitly.
- Content policy: see `i18n-messageformat-escaping.md`.

## REST API (optional, default off)

- Flag: `application.modules.common.i18n.api.enabled` (default false).
- `@ConditionalOnBean(I18nService)` + that property.
- Resolve endpoint: `namespace` + `code` + `List<String> locales` → `translate(..., List<?>)`.

## MessageFormat utils

- `I18nMessageFormatUtil` — plain text → MessageFormat pattern.
- `I18nBraceMatcher` — brace depth for nested `{` `}` (e.g. choice).
