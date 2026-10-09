package org.digitalmind.buildingblocks.core.i18n.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class I18nLocaleUtilTest {

    @Test
    void normalizeOrderedObjects_nullAndEmpty() {
        assertTrue(I18nLocaleUtil.normalizeOrderedObjects(null).isEmpty());
        assertTrue(I18nLocaleUtil.normalizeOrderedObjects(List.of()).isEmpty());
    }

    @Test
    void normalizeOrderedObjects_stringsCanonicalAndDistinctPreservingFirst() {
        List<String> result = I18nLocaleUtil.normalizeOrderedObjects(
                Arrays.asList("ro_RO", "RO", "ro-ro", "en", "  ", null, "en_US", "en")
        );
        assertEquals(List.of("ro-ro", "ro", "en", "en-us"), result);
    }

    @Test
    void normalizeOrderedObjects_localesAndMixed() {
        List<String> result = I18nLocaleUtil.normalizeOrderedObjects(
                Arrays.asList(
                        Locale.forLanguageTag("ro-RO"),
                        "ro",
                        Locale.ENGLISH,
                        new StringBuilder("en_GB")
                )
        );
        assertEquals(List.of("ro-ro", "ro", "en", "en-gb"), result);
    }

    @Test
    void normalizeOrderedObjects_skipsNullAndBlank() {
        List<Object> input = new ArrayList<>();
        input.add("   ");
        input.add(null);
        input.add("");
        assertTrue(I18nLocaleUtil.normalizeOrderedObjects(input).isEmpty());
    }

    @Test
    void normalizeOrderedObjects_rejectsUnsupportedType() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> I18nLocaleUtil.normalizeOrderedObjects(List.of("ro", 42))
        );
        assertTrue(ex.getMessage().contains("Integer"));
    }
}
