package com.attornatus.attornatus.controller;

import com.attornatus.attornatus.dto.request.CreateAddressDTO;
import com.attornatus.attornatus.dto.response.ResponseAddressDTO;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AddressController.class)
public class AddressControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AddressService addressService;

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String asJsonNodeString(byte[] bytes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.readTree(bytes).toString();
    }

    public static String asJsonNodeString(final Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.valueToTree(obj).toString();
    }

    @Test
    public void shouldReturn201WhenCreateNewAddress() throws Exception {
        CreateAddressDTO createAddressDTO = CreateAddressDTO.builder().street("Rua da Igreja")
                .number("12")
                .city("São Paulo")
                .state("SP")
                .cep("00000000")
                .main(true)
                .personId(1L).build();

        ResponseAddressDTO responseAddressDTO = ResponseAddressDTO.builder().id(1L)
                .street("Rua da Igreja")
                .number("12")
                .city("São Paulo")
                .state("SP")
                .cep("00000000")
                .main(true)
                .personId(1L).build();

        given(addressService.createAddress(any(CreateAddressDTO.class))).willAnswer((invocation) -> responseAddressDTO);

        mvc.perform(post("/api/address")
                        .content(asJsonString(createAddressDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseAddressDTO.getId()))
                .andExpect(jsonPath("$.street").value(responseAddressDTO.getStreet()))
                .andExpect(jsonPath("$.number").value(responseAddressDTO.getNumber()))
                .andExpect(jsonPath("$.city").value(responseAddressDTO.getCity()))
                .andExpect(jsonPath("$.state").value(responseAddressDTO.getState()))
                .andExpect(jsonPath("$.cep").value(responseAddressDTO.getCep()))
                .andExpect(jsonPath("$.main").value(responseAddressDTO.getMain()))
                .andExpect(jsonPath("$.personId").value(responseAddressDTO.getPersonId()));
    }

    @Test
    public void shouldReturn400WhenCreateNewAddress() throws Exception {
        mvc.perform(post("/api/address")
                        .content(asJsonString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void shouldReturn422WhenCreateNewAddress() throws Exception {
        CreateAddressDTO createAddressDTO = CreateAddressDTO.builder().street("Rua da Igreja")
                .number("12")
                .city("São Paulo")
                .state("SP")
                .cep("00000000")
                .main(true)
                .personId(0L).build();

        given(addressService.createAddress(any(CreateAddressDTO.class))).willThrow(new BusinessRuleException("a person with this personId is not found"));

        mvc.perform(post("/api/address")
                        .content(asJsonString(createAddressDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void shouldReturn200WhenGetAllAddressFromAPerson() throws Exception {
        ResponseAddressDTO secondResponseAddressDTO = ResponseAddressDTO.builder().id(1L)
                .street("Rua da Igreja")
                .number("12")
                .city("São Paulo")
                .state("SP")
                .cep("00000000")
                .main(true)
                .personId(1L).build();

        ResponseAddressDTO firstResponseAddressDTO = ResponseAddressDTO.builder().id(2L)
                .street("Rua da Flor")
                .number("344")
                .city("Recife")
                .state("PE")
                .cep("11111111")
                .main(false)
                .personId(1L).build();

        List<ResponseAddressDTO> responseAddresses = Arrays.asList(firstResponseAddressDTO, secondResponseAddressDTO);

        given(addressService.getAllAddressFromPersonId(1L)).willAnswer((invocation) -> responseAddresses);

        MvcResult response = mvc.perform(get("/api/address/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(asJsonNodeString(response.getResponse().getContentAsByteArray()), asJsonNodeString(responseAddresses));
    }

    @Test
    public void shouldReturn404WhenGetAllAddressFromAPerson() throws Exception {
        given(addressService.getAllAddressFromPersonId(0L)).willAnswer((invocation) -> null);

        mvc.perform(get("/api/address/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
