package org.digitalmind.buildingblocks.core.i18n.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class I18nStoreFsProperties {

    /**
     * Absolute or process-relative root.
     * Layout: {@code {rootFolder}/{locale}/{namespace}/{code…}}.
     */
    private String rootFolder;
}
