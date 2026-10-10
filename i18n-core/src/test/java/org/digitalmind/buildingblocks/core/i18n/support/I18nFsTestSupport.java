package org.digitalmind.buildingblocks.core.i18n.support;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Resolves classpath {@code /test-samples/i18n} to an absolute filesystem root for FS store tests.
 */
public final class I18nFsTestSupport {

    public static final String SAMPLES_CLASSPATH = "/test-samples/i18n";

    private I18nFsTestSupport() {
    }

    public static Path samplesRoot() {
        URL url = I18nFsTestSupport.class.getResource(SAMPLES_CLASSPATH);
        Objects.requireNonNull(url, "Missing classpath resource " + SAMPLES_CLASSPATH);
        try {
            return Paths.get(url.toURI()).toAbsolutePath().normalize();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Cannot resolve " + SAMPLES_CLASSPATH, e);
        }
    }
}
