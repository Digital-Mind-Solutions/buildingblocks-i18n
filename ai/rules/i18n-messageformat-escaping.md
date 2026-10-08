# Rule: single storage format for i18n `content`

## Policy

**Database `content` = plain display text** (what the user should see).

| Path | Behaviour |
|------|-----------|
| `I18nService.translate(...).getContent()` | Unchanged DB text |
| `i18nMessageSource` | `I18nMessageFormatUtil.toMessageFormatPattern` then `MessageFormat` |

Do **not** store MessageFormat-escaped forms (`don''t`, `'{'`) for normal rows.

Nested arguments (e.g. `{0,choice,0#none|1#{1}}`) rely on `I18nBraceMatcher.findMatchingClosingBrace` (depth), not the first `}`.

## Examples

| Store | Both paths |
|-------|------------|
| `don't` | OK |
| `Hello {0}` | OK (`{0}` remains a real argument for MessageSource) |
| `Use {TEST} mode` | OK (non-argument braces escaped only inside MessageSource) |
| `Don't use {0}` | OK |
| Nested `choice` with inner `{1}` | OK (depth matching) |

## Do not

- Pre-double apostrophes in DB (`don''t`) — breaks plain UI via `getContent()`.
- Maintain two storage policies (“escaped for MessageSource / raw for service”). There is one: plain text.
- Assume the first `}` closes a MessageFormat argument when nesting is possible — use brace depth.
