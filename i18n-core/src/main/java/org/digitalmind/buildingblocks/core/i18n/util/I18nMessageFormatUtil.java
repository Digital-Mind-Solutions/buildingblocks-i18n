package org.digitalmind.buildingblocks.core.i18n.util;

/**
 * Converts <strong>plain display text</strong> (as stored in DB / returned by {@code I18nService})
 * into a {@link java.text.MessageFormat} pattern.
 * <p>
 * Storage policy: keep human-readable strings in {@code content} (e.g. {@code don't}).
 * {@code I18nMessageSource} applies this conversion so MessageSource and {@code translate().getContent()}
 * share one storage format.
 * <p>
 * Nested braces use {@link I18nBraceMatcher} so patterns like
 * {@code {0,choice,0#none|1#{1}}} are not truncated at the first {@code }}.
 */
public final class I18nMessageFormatUtil {

    private I18nMessageFormatUtil() {
    }

    /**
     * @param plainText display text (may contain {@code '} and intentional {@code {0}} placeholders)
     * @return MessageFormat pattern safe for {@code new MessageFormat(pattern, locale)}
     */
    public static String toMessageFormatPattern(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText == null ? "" : plainText;
        }
        StringBuilder out = new StringBuilder(plainText.length() + 16);
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            if (c == '\'') {
                out.append("''");
                continue;
            }
            if (c == '{') {
                int closing = I18nBraceMatcher.findMatchingClosingBrace(plainText, i);
                if (closing > i + 1) {
                    String inside = plainText.substring(i + 1, closing);
                    if (isMessageFormatArgument(inside)) {
                        out.append(plainText, i, closing + 1);
                        i = closing;
                        continue;
                    }
                }
                out.append("'{'");
                continue;
            }
            if (c == '}') {
                out.append("'}'");
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }

    /**
     * MessageFormat argument body: {@code 0}, {@code 0,number}, {@code 1,date,short},
     * {@code 0,choice,...} (may contain nested {@code {}}).
     */
    static boolean isMessageFormatArgument(String inside) {
        if (inside == null || inside.isEmpty()) {
            return false;
        }
        int index = 0;
        if (!Character.isDigit(inside.charAt(index))) {
            return false;
        }
        while (index < inside.length() && Character.isDigit(inside.charAt(index))) {
            index++;
        }
        if (index == inside.length()) {
            return true;
        }
        return inside.charAt(index) == ',';
    }

}
