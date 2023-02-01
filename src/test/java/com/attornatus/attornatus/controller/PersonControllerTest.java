package com.attornatus.attornatus.controller;

import com.attornatus.attornatus.dto.request.AddressAttachedPersonDTO;
import com.attornatus.attornatus.dto.request.CreatePersonDTO;
import com.attornatus.attornatus.dto.response.ResponseAddressAttachedPersonDTO;
import com.attornatus.attornatus.dto.response.ResponsePersonDTO;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.service.PersonService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonService personService;

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

    public static JsonNode asJsonNode(byte[] bytes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.readTree(bytes);
    }

    @Test
    public void shouldReturn201WhenCreateNewPerson() throws Exception {
        List<AddressAttachedPersonDTO> addressesDTO = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000000", false)
        );
        CreatePersonDTO createPersonDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(addressesDTO).build();

        List<ResponseAddressAttachedPersonDTO> responseAddresses = Arrays.asList(
                new ResponseAddressAttachedPersonDTO(1L, "Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new ResponseAddressAttachedPersonDTO(2L, "Rua da Lagoa", "10", "Recife", "PE", "00000000", false)
        );
        ResponsePersonDTO responsePersonDTO = new ResponsePersonDTO(1L, "John Doe", LocalDate.parse("2000-01-01"), responseAddresses);

        given(personService.createPerson(any(CreatePersonDTO.class))).willAnswer((invocation)-> responsePersonDTO);

        MvcResult response = mvc.perform(post("/api/person")
                        .content(asJsonString(createPersonDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responsePersonDTO.getId()))
                .andExpect(jsonPath("$.name").value(responsePersonDTO.getName()))
                .andExpect(jsonPath("$.birthDate").value(responsePersonDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.addresses").exists())
                .andReturn();

        assertEquals(asJsonNode(response.getResponse().getContentAsByteArray()).get("addresses").toString(), asJsonNodeString(responsePersonDTO.getAddresses()));
    }

    @Test
    public void shouldReturn400WhenCreateNewPerson() throws Exception {
        mvc.perform(post("/api/person")
                        .content(asJsonString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void shouldReturn422WhenCreateNewPerson() throws Exception {
        CreatePersonDTO createPersonDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(List.of()).build();

        given(personService.createPerson(any(CreatePersonDTO.class))).willThrow(new BusinessRuleException("a person must have at least one address"));

        mvc.perform(post("/api/person")
                        .content(asJsonString(createPersonDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void shouldReturn200WhenGetPersonById() throws Exception {
        List<ResponseAddressAttachedPersonDTO> responseAddresses = List.of(
                new ResponseAddressAttachedPersonDTO(1L, "Rua da Escola", "1502", "São Paulo", "SP", "00000000", true)
        );
        ResponsePersonDTO responsePersonDTO = ResponsePersonDTO.builder()
                .id(50L).name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(responseAddresses).build();

        given(personService.getPersonById(50L)).willAnswer((invocation) -> Optional.of(responsePersonDTO));

        ObjectMapper mapper = new ObjectMapper();

        MvcResult response = mvc.perform(get("/api/person/50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responsePersonDTO.getId()))
                .andExpect(jsonPath("$.name").value(responsePersonDTO.getName()))
                .andExpect(jsonPath("$.birthDate").value(responsePersonDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.addresses").exists())
                .andReturn();

        assertEquals(asJsonNode(response.getResponse().getContentAsByteArray()).get("addresses").toString(), asJsonNodeString(responsePersonDTO.getAddresses()));
    }

    @Test
    public void shouldReturn404WhenGetPersonById() throws Exception {
        given(personService.getPersonById(50L)).willAnswer((invocation) -> Optional.empty());

        mvc.perform(get("/api/person/50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200WhenGetAllPeople() throws Exception {
        List<ResponseAddressAttachedPersonDTO> firstResponseAddressDTO = List.of(
                new ResponseAddressAttachedPersonDTO(1L, "Rua da Escola", "1502", "São Paulo", "SP", "00000000", true)
        );
        ResponsePersonDTO firstResponsePersonDTO = ResponsePersonDTO.builder()
                .id(1L).name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(firstResponseAddressDTO).build();

        List<ResponseAddressAttachedPersonDTO> secondResponseAddressDTO = List.of(
                new ResponseAddressAttachedPersonDTO(2L, "Rua da Igreja", "12", "Recife", "PE", "00000000", true)
        );
        ResponsePersonDTO secondResponsePersonDTO = ResponsePersonDTO.builder()
                .id(2L).name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(secondResponseAddressDTO).build();

        List<ResponsePersonDTO> responsePersonDTOS = Arrays.asList(firstResponsePersonDTO, secondResponsePersonDTO);

        given(personService.getAllPeople()).willAnswer((invocation) -> responsePersonDTOS);

        MvcResult response = mvc.perform(get("/api/person")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(asJsonNodeString(response.getResponse().getContentAsByteArray()), asJsonNodeString(responsePersonDTOS));
    }

    @Test
    public void shouldReturn200WhenUpdatePersonById() throws Exception {
        List<AddressAttachedPersonDTO> addressesDTO = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000000", false)
        );
        CreatePersonDTO createPersonDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(addressesDTO).build();

        List<ResponseAddressAttachedPersonDTO> responseAddresses = Arrays.asList(
                new ResponseAddressAttachedPersonDTO(1L, "Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new ResponseAddressAttachedPersonDTO(2L, "Rua da Lagoa", "10", "Recife", "PE", "00000000", false)
        );
        ResponsePersonDTO responsePersonDTO = new ResponsePersonDTO(1L, "John Doe", LocalDate.parse("2000-01-01"), responseAddresses);

        given(personService.updatePersonById(any(CreatePersonDTO.class), eq(1L))).willAnswer((invocation)-> Optional.of(responsePersonDTO));

        MvcResult response = mvc.perform(put("/api/person/1")
                        .content(asJsonString(createPersonDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responsePersonDTO.getId()))
                .andExpect(jsonPath("$.name").value(responsePersonDTO.getName()))
                .andExpect(jsonPath("$.birthDate").value(responsePersonDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.addresses").exists())
                .andReturn();

        assertEquals(asJsonNode(response.getResponse().getContentAsByteArray()).get("addresses").toString(), asJsonNodeString(responsePersonDTO.getAddresses()));
    }

    @Test
    public void shouldReturn400WhenUpdatePersonById() throws Exception {
        mvc.perform(put("/api/person/1")
                        .content(asJsonString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void shouldReturn404WhenUpdatePersonById() throws Exception {
        List<AddressAttachedPersonDTO> addressesDTO = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000000", false)
        );
        CreatePersonDTO createPersonDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(addressesDTO).build();

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/person/1")
                        .content(asJsonString(createPersonDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn422WhenUpdatePersonById() throws Exception {
        CreatePersonDTO createPersonDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(List.of()).build();

        given(personService.updatePersonById(any(CreatePersonDTO.class), eq(1L))).willThrow(new BusinessRuleException("a person must have at least one address"));

        mvc.perform(put("/api/person/1")
                        .content(asJsonString(createPersonDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").exists());
    }
}