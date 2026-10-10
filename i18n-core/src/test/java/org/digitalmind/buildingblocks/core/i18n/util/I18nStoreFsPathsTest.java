package org.digitalmind.buildingblocks.core.i18n.util;

import org.digitalmind.buildingblocks.core.i18n.exception.I18nInitializeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class I18nStoreFsPathsTest {

    @TempDir
    Path tempDir;

    @Test
    void contentFile_localeFirstThenNamespaceThenCode() {
        Path file = I18nStoreFsPaths.contentFile(tempDir, "en_US", "template", "esign/sms/welcome");
        assertEquals(
                tempDir.resolve("en-us/template/esign/sms/welcome").normalize(),
                file
        );
    }

    @Test
    void parseRelative_roundTrip() {
        Path relative = Path.of("ro", "template", "esign", "sms", "welcome");
        I18nStoreFsPaths.ParsedPath parsed = I18nStoreFsPaths.parseRelative(relative);
        assertEquals("ro", parsed.locale());
        assertEquals("template", parsed.namespace());
        assertEquals("esign/sms/welcome", parsed.code());
    }

    @Test
    void parseRelative_skipsShallowFiles() {
        assertNull(I18nStoreFsPaths.parseRelative(Path.of("README.md")));
    }

    @Test
    void multiSegmentNamespace_rejected() {
        assertThrows(
                I18nInitializeException.class,
                () -> I18nStoreFsPaths.normalizeNamespace("a/b")
        );
    }

    @Test
    void contentFile_rejectsTraversalInCode() {
        assertThrows(
                I18nInitializeException.class,
                () -> I18nStoreFsPaths.contentFile(tempDir, "en", "default", "../secret")
        );
    }

    @Test
    void contentFile_rejectsTraversalInLocale() {
        assertThrows(
                I18nInitializeException.class,
                () -> I18nStoreFsPaths.contentFile(tempDir, "../en", "default", "hello")
        );
    }

    @Test
    void stableId_isStableAcrossCalls() {
        long a = I18nStoreFsPaths.stableId("template", "esign/sms/welcome", "en");
        long b = I18nStoreFsPaths.stableId("template", "esign/sms/welcome", "en");
        long c = I18nStoreFsPaths.stableId("template", "esign/sms/welcome", "ro");
        assertEquals(a, b);
        assertTrue(a != c);
        assertTrue(a > 0);
    }
}

