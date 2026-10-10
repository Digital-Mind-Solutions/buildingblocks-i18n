package org.digitalmind.buildingblocks.core.i18n.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.i18n.config.I18nConfig;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.exception.I18nInitializeException;
import org.digitalmind.buildingblocks.core.i18n.exception.I18nNotFoundException;
import org.digitalmind.buildingblocks.core.i18n.service.I18nStoreService;
import org.digitalmind.buildingblocks.core.i18n.util.I18nLocaleUtil;
import org.digitalmind.buildingblocks.core.i18n.util.I18nStoreFsPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.ENABLED;
import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.STORE_IMPLEMENTATION;
import static org.digitalmind.buildingblocks.core.i18n.entity.I18n.DEFAULT_NAMESPACE;

/**
 * Filesystem {@link I18nStoreService}.
 * Layout: {@code {rootFolder}/{locale}/{namespace}/{code…}}.
 */
@Service
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@ConditionalOnProperty(name = STORE_IMPLEMENTATION, havingValue = "FS")
@Slf4j
public class I18nStoreServiceFSImpl implements I18nStoreService {

    private final Path root;

    @Autowired
    public I18nStoreServiceFSImpl(I18nConfig i18nConfig) {
        String rootFolder = i18nConfig.getFs() != null ? i18nConfig.getFs().getRootFolder() : null;
        if (rootFolder == null || rootFolder.isBlank()) {
            throw new I18nInitializeException(
                    "I18n store implementation=FS requires application.modules.common.i18n.fs.rootFolder"
            );
        }
        Path configured = Paths.get(rootFolder.trim()).toAbsolutePath().normalize();
        if (!Files.isDirectory(configured)) {
            throw new I18nInitializeException(
                    "I18n FS rootFolder is missing or not a directory: " + configured
            );
        }
        this.root = configured;
        log.info("I18n store FS root={}", this.root);
    }

    /** Test / package-visible constructor. */
    I18nStoreServiceFSImpl(Path root) {
        this.root = root.toAbsolutePath().normalize();
    }

    @Override
    public I18n getById(Long id) {
        return listAll().stream()
                .filter(row -> Objects.equals(row.getId(), id))
                .findFirst()
                .orElseThrow(() -> new I18nNotFoundException("I18n not found for id=" + id));
    }

    @Override
    public I18n findByNamespaceAndCodeAndLocale(String namespace, String code, String locale) {
        Path file = I18nStoreFsPaths.contentFile(root, locale, namespace, code);
        if (!Files.isRegularFile(file)) {
            return null;
        }
        return readFile(namespace, code, locale, file);
    }

    @Override
    public I18n findFirstByNamespaceAndCodeAndLocales(String namespace, String code, List<?> locales) {
        if (namespace == null || code == null || locales == null) {
            return null;
        }
        List<String> ordered = I18nLocaleUtil.normalizeOrderedObjects(locales);
        for (String locale : ordered) {
            I18n hit = findByNamespaceAndCodeAndLocale(namespace, code, locale);
            if (hit != null) {
                return hit;
            }
        }
        return null;
    }

    @Override
    public long deleteByNamespaceAndCodeAndLocale(String namespace, String code, String locale) {
        Path file = I18nStoreFsPaths.contentFile(root, locale, namespace, code);
        try {
            return Files.deleteIfExists(file) ? 1L : 0L;
        } catch (IOException e) {
            throw new I18nInitializeException("Failed to delete i18n file: " + file, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        I18n existing = getById(id);
        deleteByNamespaceAndCodeAndLocale(existing.getNamespace(), existing.getCode(), existing.getLocale());
    }

    @Override
    public I18n save(I18n i18n) {
        if (i18n == null) {
            throw new I18nInitializeException("I18n entity is required");
        }
        String namespace = (i18n.getNamespace() == null || i18n.getNamespace().isBlank())
                ? DEFAULT_NAMESPACE
                : i18n.getNamespace();
        String code = i18n.getCode();
        String locale = i18n.getLocale();
        if (i18n.getContent() == null) {
            throw new I18nInitializeException("I18n content is required");
        }
        Path file = I18nStoreFsPaths.contentFile(root, locale, namespace, code);
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, i18n.getContent(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new I18nInitializeException("Failed to write i18n file: " + file, e);
        }
        i18n.setNamespace(I18nStoreFsPaths.normalizeNamespace(namespace));
        i18n.setCode(I18nStoreFsPaths.normalizeCode(code));
        i18n.setLocale(I18nStoreFsPaths.normalizeLocale(locale));
        i18n.setId(I18nStoreFsPaths.stableId(i18n.getNamespace(), i18n.getCode(), i18n.getLocale()));
        return i18n;
    }

    private I18n readFile(String namespace, String code, String locale, Path file) {
        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            String ns = I18nStoreFsPaths.normalizeNamespace(namespace);
            String cd = I18nStoreFsPaths.normalizeCode(code);
            String loc = I18nStoreFsPaths.normalizeLocale(locale);
            return I18n.builder()
                    .id(I18nStoreFsPaths.stableId(ns, cd, loc))
                    .namespace(ns)
                    .code(cd)
                    .locale(loc)
                    .content(content)
                    .build();
        } catch (IOException e) {
            throw new I18nInitializeException("Failed to read i18n file: " + file, e);
        }
    }

    private List<I18n> listAll() {
        List<I18n> rows = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(Files::isRegularFile).forEach(file -> {
                Path relative = root.relativize(file);
                I18nStoreFsPaths.ParsedPath parsed = I18nStoreFsPaths.parseRelative(relative);
                if (parsed == null) {
                    return; // skip README.md / other non locale/namespace/code files
                }
                try {
                    rows.add(readFile(parsed.namespace(), parsed.code(), parsed.locale(), file));
                } catch (I18nInitializeException | IllegalArgumentException ex) {
                    log.debug("Skipping non-i18n FS entry {}: {}", relative, ex.getMessage());
                }
            });
        } catch (IOException e) {
            throw new I18nInitializeException("Failed to walk i18n FS root: " + root, e);
        }
        return rows;
    }
}
