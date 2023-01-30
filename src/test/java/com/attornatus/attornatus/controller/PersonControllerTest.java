package com.attornatus.attornatus.controller;

import com.attornatus.attornatus.dto.request.AddressAttachedPersonDTO;
import com.attornatus.attornatus.dto.request.CreatePersonDTO;
import com.attornatus.attornatus.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    PersonService personService;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldReturn201WhenCreateNewPerson() throws Exception {
        List<AddressAttachedPersonDTO> addresses = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000000", false)
        );

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/person")
                        .content(asJsonString(new CreatePersonDTO("John Doe", LocalDate.parse("01/01/2000", dateFormatter), addresses)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void shouldReturn400WhenCreateNewPerson() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/api/person")
                        .content(asJsonString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists());
    }

    @Test
    public void shouldReturn422WhenCreateNewPerson() throws Exception {
        List<AddressAttachedPersonDTO> addresses = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000000", true)
        );

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/person")
                        .content(asJsonString(new CreatePersonDTO("John Doe", LocalDate.parse("01/01/2000", dateFormatter), addresses)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists());
    }
}