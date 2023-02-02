package com.attornatus.attornatus.controller;

import com.attornatus.attornatus.dto.request.CreateAddressDTO;
import com.attornatus.attornatus.dto.response.ResponseAddressDTO;
import com.attornatus.attornatus.dto.response.ResponsePersonDTO;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.service.AddressService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Address Controller", description = "Create, Update, Read and Delete")
@RestController
@RequestMapping(path = "/api/address", produces = {"application/json"})
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping
    @Operation(summary = "Post address",
            description = "Create a address and save to database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseAddressDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ResponseAddressDTO> postPerson(@Valid @RequestBody CreateAddressDTO addressDTO) throws BusinessRuleException {
        return new ResponseEntity<>(addressService.createAddress(addressDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all address from a person",
            description = "Get a list of all address from a specific person.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Got", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePersonDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(path = "/{id}", produces = {"application/json"})
    public ResponseEntity<List<ResponseAddressDTO>> getPerson(@PathVariable Long id) {
        return ResponseEntity.of(Optional.ofNullable(addressService.getAllAddressFromPersonId(id)));
    }
}