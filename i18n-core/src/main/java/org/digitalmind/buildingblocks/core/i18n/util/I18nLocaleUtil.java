package org.digitalmind.buildingblocks.core.i18n.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public final class I18nLocaleUtil {

    private I18nLocaleUtil() {
    }

    /**
     * Canonical form: lowercase, {@code _} → {@code -}, trim.
     * Examples: {@code en_US} → {@code en-us}, {@code RO} → {@code ro}.
     */
    public static String normalize(String locale) {
        if (locale == null) {
            return null;
        }
        String value = locale.trim().toLowerCase(Locale.ROOT).replace('_', '-');
        return value.isEmpty() ? null : value;
    }

    public static String normalize(Locale locale) {
        if (locale == null) {
            return null;
        }
        return normalize(locale.toLanguageTag());
    }

    /**
     * Preserves order, drops null/blank, normalizes, de-duplicates.
     */
    public static List<String> normalizeOrdered(List<String> locales) {
        if (locales == null || locales.isEmpty()) {
            return List.of();
        }
        LinkedHashSet<String> ordered = new LinkedHashSet<>();
        for (String locale : locales) {
            String normalized = normalize(locale);
            if (normalized != null) {
                ordered.add(normalized);
            }
        }
        return new ArrayList<>(ordered);
    }

    /**
     * Preference chain: full tag → language → optional defaultLocale.
     */
    public static List<String> preferenceList(Locale locale, String defaultLocale) {
        List<String> locales = new ArrayList<>(3);
        if (locale != null) {
            String tag = normalize(locale);
            if (tag != null) {
                locales.add(tag);
            }
            String language = normalize(locale.getLanguage());
            if (language != null) {
                locales.add(language);
            }
        }
        String fallback = normalize(defaultLocale);
        if (fallback != null) {
            locales.add(fallback);
        }
        return normalizeOrdered(locales);
    }

}
