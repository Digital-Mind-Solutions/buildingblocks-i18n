package org.digitalmind.buildingblocks.core.i18n.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@ConditionalOnBean(I18nService.class)
@ConditionalOnProperty(name = I18nCoreModuleConfig.API_ENABLED, havingValue = "true", matchIfMissing = false)
@RequestMapping("${" + I18nCoreModuleConfig.PREFIX + ".api.docket.base-path}")
@Tag(name = "I18n", description = "This resource is exposing the services for internationalization support")
public class I18nController {
    private final I18nService i18nService;

    @Autowired
    public I18nController(I18nService i18nService) {
        this.i18nService = i18nService;
    }

    //CREATE I18n
    @Operation(summary = "Create translation", description = "This API is used for creating a new translation entry.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Operation success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Error encountered when processing request")
    })
    @PostMapping(path = "/", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<I18n> createI18n(
            @Parameter(description = "The translation", required = true) @Valid @RequestBody I18n i18n) {

        I18n result = i18nService.save(i18n);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    //GET I18n
    @Operation(summary = "Retrieve translation", description = "This API is used for retrieving translation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })
    @GetMapping(path = "/{identifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<I18n> retrieveI18n(
            @Parameter(description = "The identifier used to identify a I18n.", required = true) @PathVariable(value = "identifier", required = true) Long identifier
    ) {
        I18n i18n = i18nService.getOne(identifier);
        return ResponseEntity.ok(i18n);
    }

    //RESOLVE I18n (ordered locale preference)
    @Operation(summary = "Resolve translation", description = "Resolves a translation by namespace, code and ordered locale preference list (first match wins).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })
    @GetMapping(path = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<I18n> resolveI18n(
            @Parameter(description = "The namespace", required = true) @Valid @RequestParam String namespace,
            @Parameter(description = "The code", required = true) @Valid @RequestParam String code,
            @Parameter(description = "Ordered locale preference list", required = true) @Valid @RequestParam List<String> locales
    ) {
        return ResponseEntity.ok(i18nService.translate(namespace, code, locales));
    }

    //UPDATE BY ID
    @Operation(summary = "Update translation info", description = "This API is used for updating a translation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "404", description = "Process does not exists"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })
    @PutMapping(path = "/{identifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateI18n(
            @Parameter(description = "The identifier used to identify a translation.", required = true) @PathVariable(value = "identifier", required = true) Long identifier,
            @Parameter(description = "The translation details", required = true) @Valid @RequestBody I18n i18n
    ) {
        I18n result = i18nService.getOne(identifier);
        result.setNamespace(i18n.getNamespace());
        result.setLocale(i18n.getLocale());
        result.setContent(i18n.getContent());
        result.setCode(i18n.getCode());
        i18nService.save(result);
        return ResponseEntity.ok().build();
    }

    //DELETE BY ID
    @Operation(summary = "Delete translation info", description = "This API is used for deleting a translation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "404", description = "Process does not exists"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })
    @DeleteMapping(path = "/{identifier}")
    public ResponseEntity<Void> deleteI18n(
            @Parameter(description = "The identifier used to identify a translation.", required = true) @PathVariable(value = "identifier", required = true) Long identifier
    ) {
        I18n i18n = i18nService.getOne(identifier);
        i18nService.deleteById(identifier);
        return ResponseEntity.ok().build();
    }

}
