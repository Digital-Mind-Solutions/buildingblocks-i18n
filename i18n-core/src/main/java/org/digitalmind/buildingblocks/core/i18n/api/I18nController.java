package org.digitalmind.buildingblocks.core.i18n.api;


import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig;
import org.digitalmind.buildingblocks.core.i18n.dto.I18nSearchOperator;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@ConditionalOnProperty(name = I18nCoreModuleConfig.API_ENABLED, havingValue = "true")
@RequestMapping("${" + I18nCoreModuleConfig.PREFIX + ".api.docket.base-path}")
@Api(value = "I18n", description = "This resource is exposing the services for internationalization support", tags = {"I18n"})
public class I18nController {
    private final I18nService i18nService;

    @Autowired
    public I18nController(I18nService i18nService) {
        this.i18nService = i18nService;
    }

    //CREATE I18n
    @ApiOperation(
            value = "Create translation",
            notes = "This API is used for creating a new translation entry.",
            response = I18n.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Error encountered when processing request")
    })
    @PostMapping(path = "/", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<I18n> createI18n(
            @ApiParam(name = "i18n", value = "The translation", required = true, allowMultiple = false) @Valid @RequestBody I18n i18n) {

        I18n result = i18nService.save(i18n);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    //GET I18n
    @ApiOperation(
            value = "Retrieve translation",
            notes = "This API is used for retrieving translation.",
            response = I18n.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })
    @GetMapping(path = "/{identifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<I18n> retrieveI18n(
            @ApiParam(name = "identifier", value = "The identifier used to identify a I18n.", required = true) @PathVariable(value = "identifier", required = true) Long identifier
    ) {
        I18n i18n = i18nService.getOne(identifier);
        return ResponseEntity.ok(i18n);
    }

    //LIST I18n
    @ApiOperation(
            value = "Retrieve translation list",
            notes = "This API is used for retrieving translation lists.",
            response = Page.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })
    @GetMapping(path = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<I18n>> listI18n(
            @ApiParam(name = "code", value = "The code", required = true, allowMultiple = false) @Valid @RequestParam String code,
            @ApiParam(name = "operator", value = "The operator", required = true, allowMultiple = false, allowableValues = "EQUALS,CONTAINS,START_WITH") @Valid @RequestParam I18nSearchOperator operator,
            @ApiParam(name = "locale", value = "The locale", required = true, allowMultiple = false) @Valid @RequestParam String locale,
            @ApiParam(name = "pageable", value = "Pageable parameters.", required = false) Pageable pageable
    ) {
        Page<I18n> result = i18nService.findByCodeAndLocale(code, operator, locale, pageable);
        return ResponseEntity.ok(result);
    }

    //UPDATE BY ID
    @ApiOperation(
            value = "Update translation info",
            notes = "This API is used for updating a translation.",
            response = Void.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 404, message = "Process does not exists"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })

    @PutMapping(path = "/{identifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateI18n(
            @ApiParam(name = "identifier", value = "The identifier used to identify a translation.", required = true) @PathVariable(value = "identifier", required = true) Long identifier,
            @ApiParam(name = "18n", value = "The translation details", required = true) @Valid @RequestBody I18n i18n
    ) {
        I18n result = i18nService.getOne(identifier);
        result.setLocale(i18n.getLocale());
        result.setContent(i18n.getContent());
        result.setCode(i18n.getCode());
        i18nService.save(result);
        return ResponseEntity.ok().build();
    }

    //DELETE BY ID
    @ApiOperation(
            value = "Delete translation info",
            notes = "This API is used for deleting a translation.",
            response = Void.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 404, message = "Process does not exists"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })

    @DeleteMapping(path = "/{identifier}")
    public ResponseEntity<Void> deleteI18n(
            @ApiParam(name = "identifier", value = "The identifier used to identify a translation.", required = true) @PathVariable(value = "identifier", required = true) Long identifier
    ) {
        I18n i18n = i18nService.getOne(identifier);
        i18nService.deleteById(identifier);
        return ResponseEntity.ok().build();
    }

}
