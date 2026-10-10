# i18n FS test samples

Root used by FS store tests: classpath `/test-samples/i18n`
(resolved to an absolute path as `application.modules.common.i18n.fs.rootFolder`).

## Layout

```text
{root}/{locale}/{namespace}/{code…}
```

| Path | namespace | code | locale | content | Notes |
|------|----------|------|--------|---------|--------|
| `en/default/hello` | `default` | `hello` | `en` | Hello | |
| `ro/default/hello` | `default` | `hello` | `ro` | Salut | |
| `en/default/fallback-only-en` | `default` | `fallback-only-en` | `en` | Fallback EN only | no `ro`/`de` → preference falls back to `en` |
| `en/default/overwrite-demo` | `default` | `overwrite-demo` | `en` | overwrite-v1 | IT may overwrite then restore |
| `en/template/esign/sms/welcome` | `template` | `esign/sms/welcome` | `en` | Welcome to eSign SMS | |
| `ro/template/esign/sms/welcome` | `template` | `esign/sms/welcome` | `ro` | Bun venit la eSign SMS | |
| `en/template/esign/sms/status` | `template` | `esign/sms/status` | `en` | SMS EN | also `ro`, `de` |
| `ro/template/esign/sms/status` | `template` | `esign/sms/status` | `ro` | SMS RO | |
| `de/template/esign/sms/status` | `template` | `esign/sms/status` | `de` | SMS DE | |
| `en/app/errors/not-found` | `app` | `errors/not-found` | `en` | Error EN | nested code under other namespace |
| `ro/app/errors/not-found` | `app` | `errors/not-found` | `ro` | Eroare RO | |
| `en-us/template/esign/email/welcome` | `template` | `esign/email/welcome` | `en-us` | Welcome email (en-US) | no plain `en` |

Bootstrap (see `application-i18n-fs.properties` + IT):

```properties
application.modules.common.i18n.enabled=true
application.modules.common.i18n.implementation=FS
application.modules.common.i18n.fs.rootFolder=<absolute path to this folder>
application.modules.common.i18n.default-locale=en
```
