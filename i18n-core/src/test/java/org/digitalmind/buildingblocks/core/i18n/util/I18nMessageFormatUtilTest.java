package org.digitalmind.buildingblocks.core.i18n.util;

import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class I18nMessageFormatUtilTest {

    @Test
    void toMessageFormatPattern_doublesApostropheInDont() {
        String pattern = I18nMessageFormatUtil.toMessageFormatPattern("don't");
        assertEquals("don''t", pattern);
        assertEquals("don't", format(pattern));
    }

    @Test
    void toMessageFormatPattern_preservesIndexedPlaceholder() {
        String pattern = I18nMessageFormatUtil.toMessageFormatPattern("Hello {0}");
        assertEquals("Hello {0}", pattern);
        assertEquals("Hello Ada", format(pattern, "Ada"));
    }

    @Test
    void toMessageFormatPattern_treatsNonArgumentBracesAsLiteral() {
        String pattern = I18nMessageFormatUtil.toMessageFormatPattern("Use {TEST} mode");
        assertEquals("Use '{'TEST'}' mode", pattern);
        assertEquals("Use {TEST} mode", format(pattern));
    }

    @Test
    void toMessageFormatPattern_apostrophePlusPlaceholder() {
        String plain = "Don't use {0}";
        String pattern = I18nMessageFormatUtil.toMessageFormatPattern(plain);
        assertEquals("Don''t use {0}", pattern);
        assertEquals("Don't use TEST", format(pattern, "TEST"));
    }

    @Test
    void toMessageFormatPattern_preservesNumberStyleArgument() {
        String pattern = I18nMessageFormatUtil.toMessageFormatPattern("Amount {0,number}");
        assertEquals("Amount {0,number}", pattern);
    }

    @Test
    void toMessageFormatPattern_preservesNestedChoiceArgument() {
        String plain = "Items: {0,choice,0#none|1#one|1<{1}}";
        String pattern = I18nMessageFormatUtil.toMessageFormatPattern(plain);
        assertEquals(plain, pattern);
        assertEquals(
                "Items: one",
                format(pattern, 1, "ignored")
        );
    }

    @Test
    void toMessageFormatPattern_nullAndEmpty() {
        assertEquals("", I18nMessageFormatUtil.toMessageFormatPattern(null));
        assertEquals("", I18nMessageFormatUtil.toMessageFormatPattern(""));
    }

    @Test
    void isMessageFormatArgument_acceptsIndexAndTypedForms() {
        assertTrue(I18nMessageFormatUtil.isMessageFormatArgument("0"));
        assertTrue(I18nMessageFormatUtil.isMessageFormatArgument("12"));
        assertTrue(I18nMessageFormatUtil.isMessageFormatArgument("0,number"));
        assertTrue(I18nMessageFormatUtil.isMessageFormatArgument("1,date,short"));
        assertFalse(I18nMessageFormatUtil.isMessageFormatArgument("TEST"));
        assertFalse(I18nMessageFormatUtil.isMessageFormatArgument("50%"));
        assertFalse(I18nMessageFormatUtil.isMessageFormatArgument(""));
        assertFalse(I18nMessageFormatUtil.isMessageFormatArgument(null));
    }

    private static String format(String pattern, Object... args) {
        return new MessageFormat(pattern, Locale.ROOT).format(args);
    }

}
