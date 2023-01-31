package com.attornatus.attornatus.controller;

import com.attornatus.attornatus.dto.request.AddressAttachedPersonDTO;
import com.attornatus.attornatus.dto.request.CreatePersonDTO;
import com.attornatus.attornatus.dto.response.ResponseAddressAttachedPersonDTO;
import com.attornatus.attornatus.dto.response.ResponsePersonDTO;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    public static String toJson(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            return objectMapper.writerWithView(ResponsePersonDTO.class).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        mvc.perform(post("/api/person")
                        .content(asJsonString(createPersonDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
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
        List<AddressAttachedPersonDTO> addressesDTO = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000000", false)
        );
        CreatePersonDTO createPersonDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(addressesDTO).build();

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

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/person/50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responsePersonDTO.getId()))
                .andExpect(jsonPath("$.name").value(responsePersonDTO.getName()))
                .andExpect(jsonPath("$.birthDate").value(responsePersonDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.addresses").exists());
    }

    @Test
    public void shouldReturn404WhenGetPersonById() throws Exception {
        given(personService.getPersonById(50L)).willAnswer((invocation) -> Optional.empty());

        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/person/50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}