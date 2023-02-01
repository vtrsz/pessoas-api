package com.attornatus.attornatus.controller;

import com.attornatus.attornatus.dto.request.CreateAddressDTO;
import com.attornatus.attornatus.dto.response.ResponseAddressDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}