package edu.online.messenger.controller;

import edu.online.messenger.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Sql(scripts = "/sql/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUserByLoginShouldReturnUserWhenUserExists() throws Exception {
        mockMvc.perform(get("/api/users/login/testLogin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testLogin"));
    }

    @Test
    void getUserByLoginShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/login/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addAddressByUserIdShouldCreateAddressWhenValidRequest() throws Exception {
        String addressJson = "{\"userId\":5,\"country\":\"Russia\",\"city\":\"Moscow\",\"street\":\"Lenina\",\"postalCode\":\"123456\",\"house\":1,\"housing\":\"A\",\"apartment\":10}";
        mockMvc.perform(post("/api/users/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city").value("Moscow"));
    }

    @Test
    void addAddressByUserIdShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        String addressJson = "{\"userId\":999,\"country\":\"Russia\",\"city\":\"Moscow\",\"street\":\"Lenina\",\"postalCode\":\"123456\",\"house\":1,\"housing\":\"A\",\"apartment\":10}";
        mockMvc.perform(post("/api/users/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isNotFound());
    }
}