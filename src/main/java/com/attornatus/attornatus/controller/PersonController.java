package com.attornatus.attornatus.controller;

import com.attornatus.attornatus.dto.request.CreatePersonDTO;
import com.attornatus.attornatus.dto.response.ResponsePersonDTO;
import com.attornatus.attornatus.entity.Person;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.exception.MultipleMainAddressException;
import com.attornatus.attornatus.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Person Controller", description = "Create, Update, Read and Delete")
@RestController
@RequestMapping(path = "/api/person", produces = {"application/json"}, consumes = {"application/json"})
public class PersonController {
    @Autowired
    private PersonService personService;

    @PostMapping
    @Operation(summary = "Post person",
            description = "Create a person and save to database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePersonDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ResponsePersonDTO> postPerson(@Valid @RequestBody CreatePersonDTO personDTO) throws MultipleMainAddressException, BusinessRuleException {
        Person person = personDTO.toEntity();

        Person createdPerson = personService.createPerson(person);

        ResponsePersonDTO responsePersonDTO = createdPerson.toDto();

        return new ResponseEntity<>(responsePersonDTO, HttpStatus.CREATED);
    }
}