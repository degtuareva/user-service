package edu.online.messenger.controller;

import edu.online.messenger.config.AbstractIntegrationTest;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.repository.AddressRepository;
import edu.online.messenger.util.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/sql/setup.sql")
public class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

    private final User testUser = UserTestBuilder.builder().withId(5L).build().buildUser();

    @Test
    void getUserByIdShouldReturnUserWhenUserExists() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.login").value("testLogin"));
    }

    @Test
    void getUserByIdShouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 666L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAddressListByUserIdShouldReturnAddressesWhenUserHasAddresses() throws Exception {
        mockMvc.perform(get("/api/users/address/{userId}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].street").value("testStreet"))
                .andExpect(jsonPath("$[0].city").value("testCity"))
                .andExpect(jsonPath("$[0].house").value(1))
                .andExpect(jsonPath("$[0].postalCode").value("testPostalCode"))
                .andExpect(jsonPath("$[0].country").value("testCountry"))
                .andExpect(jsonPath("$[0].housing").value("testHousing"))
                .andExpect(jsonPath("$[0].apartment").value(1));
    }

    @Test
    void getAddressListByUserIdShouldReturnEmptyListWhenUserNoAddresses() throws Exception {
        addressRepository.deleteAll();
        mockMvc.perform(get("/api/users/address/{userId}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}