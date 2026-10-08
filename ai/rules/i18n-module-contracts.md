# Rule: `buildingblocks-i18n` module contracts

## Persistence

- Table `i18n`: unique `(namespace, code, locale)`; index `(namespace, locale)`.
- Default `namespace`: `default`.
- Locale: canonical lowercase, `_` → `-` (`en_US` → `en-us`).
- Liquibase in jar: `classpath:db/changelog/i18n/db.changelog-master.xml`.
- `0001`: create if missing; `MARK_RAN` if table exists; **no drop**. Consumer drops/migrates old schemas manually.

## Runtime service

- Primary: `I18nService.translate(namespace, code, List<String> locales)` — one query, first match by list order.
- Locale normalize: `I18nLocaleUtil`.
- Cache name: `i18n-cache` (consumer must define it). Miss (`id == 0`) not cached. Writes / `clearCache` evict all.
- Overloads use `@Lazy` self-proxy so `@Cacheable` on the List method still runs.
- `getOne(id)` → `findById` or `I18nNotFoundException`.

## Repository

- Exact: `findByNamespaceAndCodeAndLocale`, `deleteByNamespaceAndCodeAndLocale`.
- Ordered locales: `findFirstByNamespaceAndCodeAndLocales` (custom impl, `EntityManager` constructor injection).

## MessageSource (optional)

- Bean: `i18nMessageSource` (not global `messageSource`).
- Flag: `application.modules.common.i18n.message-source.enabled` (default false).
- `@ConditionalOnBean(I18nService)` + that property.
- Consumer wires the bean explicitly.
- Content policy: see `i18n-messageformat-escaping.md`.

## REST API (optional, default off)

- Flag: `application.modules.common.i18n.api.enabled` (default false).
- `@ConditionalOnBean(I18nService)` + that property.
- Admin CRUD + resolve — not required for in-process translate.

## Utils

- `I18nLocaleUtil` — normalize / preference list.
- `I18nMessageFormatUtil` — plain text → MessageFormat pattern.
- `I18nBraceMatcher` — brace depth for nested `{` `}` (e.g. choice).
