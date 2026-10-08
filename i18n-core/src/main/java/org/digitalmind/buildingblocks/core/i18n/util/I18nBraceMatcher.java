package org.digitalmind.buildingblocks.core.i18n.util;

/**
 * Counts and matches curly braces by nesting depth.
 * <p>
 * Used when scanning MessageFormat-like segments so nested choice patterns
 * (for example an outer argument that contains an inner <code>{1}</code>)
 * are not cut at the first closing brace.
 */
public final class I18nBraceMatcher {

    private I18nBraceMatcher() {
    }

    /**
     * Finds the index of the closing brace that matches the opening brace at {@code openIndex}.
     *
     * @param text      full text
     * @param openIndex index of the opening <code>'{'</code> that starts the group
     * @return index of the matching <code>'}'</code>, or {@code -1} if unbalanced or invalid
     */
    public static int findMatchingClosingBrace(String text, int openIndex) {
        if (text == null || openIndex < 0 || openIndex >= text.length() || text.charAt(openIndex) != '{') {
            return -1;
        }
        int depth = 0;
        for (int i = openIndex; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
                if (depth < 0) {
                    return -1;
                }
            }
        }
        return -1;
    }

    /**
     * @return {@code true} if every opening brace is matched in order
     * (depth never goes negative and ends at zero)
     */
    public static boolean isBalanced(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }
        int depth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth < 0) {
                    return false;
                }
            }
        }
        return depth == 0;
    }

    /**
     * Net brace depth of the whole string (opens minus closes).
     * Returns {@code -1} if a closing brace appears while depth is already zero.
     */
    public static int netDepth(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        int depth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth < 0) {
                    return -1;
                }
            }
        }
        return depth;
    }

}
