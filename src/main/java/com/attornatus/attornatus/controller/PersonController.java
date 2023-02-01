package com.attornatus.attornatus.controller;

import com.attornatus.attornatus.dto.request.CreatePersonDTO;
import com.attornatus.attornatus.dto.response.ResponsePersonDTO;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.exception.MultipleMainAddressException;
import com.attornatus.attornatus.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Person Controller", description = "Create, Update, Read and Delete")
@RestController
@RequestMapping(path = "/api/person", produces = {"application/json"})
public class PersonController {
    @Autowired
    private PersonService personService;

    @PostMapping
    @Operation(summary = "Post person",
            description = "Create a person and save to database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePersonDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ResponsePersonDTO> postPerson(@Valid @RequestBody CreatePersonDTO personDTO) throws MultipleMainAddressException, BusinessRuleException {
        return new ResponseEntity<>(personService.createPerson(personDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get person",
            description = "Get a person information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully Got", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponsePersonDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(path = "/{id}", produces = {"application/json"})
    public ResponseEntity<ResponsePersonDTO> getPerson(@PathVariable final Long id) {
        return ResponseEntity.of(personService.getPersonById(id));
    }

    @Operation(summary = "Get all people",
            description = "Get a list of people information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully Got", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePersonDTO.class))),
    })
    @GetMapping(produces = {"application/json"})
    public ResponseEntity<List<ResponsePersonDTO>> getPerson() {
        return ResponseEntity.of(Optional.ofNullable(personService.getAllPeople()));
    }


    @Operation(summary = "Update person",
            description = "Update a person and save to database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully Updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePersonDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = "application/json"))
    })
    @PutMapping(path = "/{id}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ResponsePersonDTO> putPerson(@PathVariable final Long id, @Valid @RequestBody CreatePersonDTO createPersonDTO) throws MultipleMainAddressException, BusinessRuleException {
        return ResponseEntity.of(personService.updatePersonById(createPersonDTO, id));
    }
}