package org.digitalmind.buildingblocks.core.i18n.util;

import org.digitalmind.buildingblocks.core.i18n.exception.I18nInitializeException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * FS layout: {@code {root}/{locale}/{namespace}/{code…}} (last segment = file).
 * <p>
 * {@code namespace} is a single path segment; {@code code} may contain {@code /}
 * (nested folders under the namespace). Locale is the first folder under root.
 */
public final class I18nStoreFsPaths {

    private I18nStoreFsPaths() {
    }

    public static String normalizeLocale(String locale) {
        String normalized = I18nLocaleUtil.normalize(locale);
        if (normalized == null || normalized.isBlank()) {
            throw new I18nInitializeException("I18n locale is required");
        }
        if (normalized.contains("/") || normalized.contains("\\") || normalized.contains("..")) {
            throw new I18nInitializeException("I18n locale must be a single path segment: " + locale);
        }
        return normalized;
    }

    public static String normalizeNamespace(String namespace) {
        String value = requireNonBlank(namespace, "namespace").trim();
        rejectTraversal(value);
        // single segment (no slash) — keeps reverse-parse unambiguous with multi-segment code
        if (value.contains("/") || value.contains("\\")) {
            throw new I18nInitializeException(
                    "I18n FS namespace must be a single path segment (got: " + namespace + ")"
            );
        }
        return value;
    }

    public static String normalizeCode(String code) {
        String value = requireNonBlank(code, "code").trim().replace('\\', '/');
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        rejectTraversal(value);
        if (value.isEmpty()) {
            throw new I18nInitializeException("I18n code is invalid: " + code);
        }
        return value;
    }

    public static Path contentFile(Path root, String locale, String namespace, String code) {
        Path file = root.resolve(normalizeLocale(locale)).resolve(normalizeNamespace(namespace));
        for (String segment : normalizeCode(code).split("/")) {
            if (!segment.isEmpty()) {
                file = file.resolve(segment);
            }
        }
        return ensureUnderRoot(root, file.normalize());
    }

    public static Path ensureUnderRoot(Path root, Path candidate) {
        Path normalizedRoot = root.toAbsolutePath().normalize();
        Path normalized = candidate.toAbsolutePath().normalize();
        if (!normalized.startsWith(normalizedRoot)) {
            throw new I18nInitializeException("Resolved path escapes i18n root: " + candidate);
        }
        return normalized;
    }

    public static long stableId(String namespace, String code, String locale) {
        String key = normalizeNamespace(namespace) + "\0"
                + normalizeCode(code) + "\0"
                + normalizeLocale(locale);
        long bits = UUID.nameUUIDFromBytes(key.getBytes(StandardCharsets.UTF_8)).getMostSignificantBits();
        return bits == Long.MIN_VALUE ? 1L : Math.abs(bits);
    }

    /**
     * Parse relative path under root: {@code locale / namespace / code…}.
     * Returns {@code null} for non-entry files (e.g. {@code README.md} at root).
     */
    public static ParsedPath parseRelative(Path relative) {
        if (relative == null || relative.getNameCount() < 3) {
            return null;
        }
        String locale = relative.getName(0).toString().toLowerCase(Locale.ROOT);
        String namespace = relative.getName(1).toString();
        List<String> codeSegments = new ArrayList<>();
        for (int i = 2; i < relative.getNameCount(); i++) {
            codeSegments.add(relative.getName(i).toString());
        }
        return new ParsedPath(locale, namespace, String.join("/", codeSegments));
    }

    public static void rejectTraversal(String relative) {
        for (String segment : relative.split("/")) {
            if ("..".equals(segment) || ".".equals(segment)) {
                throw new I18nInitializeException("Path traversal is not allowed: " + relative);
            }
        }
    }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new I18nInitializeException("I18n " + field + " is required");
        }
        return value;
    }

    public record ParsedPath(String locale, String namespace, String code) {
    }
}
