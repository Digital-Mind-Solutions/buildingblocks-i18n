package org.digitalmind.buildingblocks.core.i18n.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class I18nBraceMatcherTest {

    @Test
    void findMatchingClosingBrace_simple() {
        String text = "Hello {0}!";
        int open = text.indexOf('{');
        assertEquals(text.indexOf('}'), I18nBraceMatcher.findMatchingClosingBrace(text, open));
    }

    @Test
    void findMatchingClosingBrace_nested() {
        //               012345678901234567890123456789012
        String text = "x{0,choice,0#a|1#{1}}y";
        int open = text.indexOf('{');
        int close = I18nBraceMatcher.findMatchingClosingBrace(text, open);
        assertEquals(text.lastIndexOf('}'), close);
        assertEquals("0,choice,0#a|1#{1}", text.substring(open + 1, close));
    }

    @Test
    void findMatchingClosingBrace_deepNesting() {
        String text = "{{{{}{}}}{}{}}";
        assertEquals(text.length() - 1, I18nBraceMatcher.findMatchingClosingBrace(text, 0));
        assertTrue(I18nBraceMatcher.isBalanced(text));
        assertEquals(0, I18nBraceMatcher.netDepth(text));
    }

    @Test
    void findMatchingClosingBrace_unbalanced() {
        assertEquals(-1, I18nBraceMatcher.findMatchingClosingBrace("{a{b}", 0));
        assertEquals(-1, I18nBraceMatcher.findMatchingClosingBrace("nope", 0));
        assertEquals(-1, I18nBraceMatcher.findMatchingClosingBrace(null, 0));
    }

    @Test
    void isBalanced_and_netDepth() {
        assertTrue(I18nBraceMatcher.isBalanced(""));
        assertTrue(I18nBraceMatcher.isBalanced("{a{b}c}"));
        assertFalse(I18nBraceMatcher.isBalanced("{a{b}"));
        assertFalse(I18nBraceMatcher.isBalanced("a}b"));
        assertEquals(0, I18nBraceMatcher.netDepth("{}{}"));
        assertEquals(1, I18nBraceMatcher.netDepth("{{}"));
        assertEquals(-1, I18nBraceMatcher.netDepth("}{"));
    }

}
