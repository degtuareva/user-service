package edu.online.messenger.controller;

import edu.online.messenger.config.AbstractIntegrationTest;
import edu.online.messenger.model.entity.Address;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.repository.AddressRepository;
import edu.online.messenger.util.AddressTestBuilder;
import edu.online.messenger.util.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private final Address testAddress = AddressTestBuilder.builder().withId(10L).build().buildAddress();

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

    @Test
    void existsByIdShouldReturnTrueWhenUserExists() throws Exception {
        mockMvc.perform(get("/api/users/existence/id/{userId}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsByIdShouldReturnFalseWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/existence/id/{userId}", 333L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void deleteAddressShouldReturnNoContentWhenAddressExists() throws Exception {
        assertThat(addressRepository.existsById(testAddress.getId())).isTrue();

        mockMvc.perform(delete("/api/users/address/{id}", testAddress.getId()))
                .andExpect(status().isNoContent());

        assertThat(addressRepository.existsById(testAddress.getId())).isFalse();
    }

    @Test
    void deleteAddressByIdShouldReturnNoContentWhenAddressDoesNotExist() throws Exception {
        assertThat(addressRepository.existsById(333L)).isFalse();

        mockMvc.perform(delete("/api/users/address/{id}", 333L))
                .andExpect(status().isNoContent());
    }

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