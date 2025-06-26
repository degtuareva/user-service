package edu.online.messenger.controller;

import edu.online.messenger.config.AbstractIntegrationTest;
import edu.online.messenger.model.entity.Address;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.repository.AddressRepository;
import edu.online.messenger.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Sql(scripts = "/setup.sql")
public class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private User testUser;

    private Address testAddress;

    @BeforeEach
    void setup() {
        testUser = userRepository.findById(5L).orElseThrow();
        testAddress = addressRepository.findById(10L).orElseThrow();
    }

    @Test
    void getUserByIdShouldReturnUserWhenUserExists() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.login").value(testUser.getLogin()));
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
                .andExpect(jsonPath("$[0].street").value(testAddress.getStreet()))
                .andExpect(jsonPath("$[0].city").value(testAddress.getCity()))
                .andExpect(jsonPath("$[0].house").value(testAddress.getHouse()))
                .andExpect(jsonPath("$[0].postalCode").value(testAddress.getPostalCode()))
                .andExpect(jsonPath("$[0].country").value(testAddress.getCountry()))
                .andExpect(jsonPath("$[0].housing").value(testAddress.getHousing()))
                .andExpect(jsonPath("$[0].apartment").value(testAddress.getApartment()));
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