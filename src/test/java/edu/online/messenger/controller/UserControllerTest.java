package edu.online.messenger.controller;

import edu.online.messenger.constant.RoleName;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private User testUser;

    private Address testAddress;

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.4-alpine")
            .withDatabaseName("edu_online_messenger_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setup() {
        addressRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setLogin("testLogin");
        testUser.setPassword("testPassword");
        testUser.setRole(RoleName.USER);
        testUser = userRepository.save(testUser);

        testAddress = new Address();
        testAddress.setUser(testUser);
        testAddress.setCountry("testCountry");
        testAddress.setCity("testCity");
        testAddress.setStreet("testStreet");
        testAddress.setPostalCode("testPostalCode");
        testAddress.setHouse(1);
        testAddress.setHousing("testHousing");
        testAddress.setApartment(1);
        testAddress = addressRepository.save(testAddress);
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() throws Exception {
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