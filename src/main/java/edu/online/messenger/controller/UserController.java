package edu.online.messenger.controller;

import edu.online.messenger.model.dto.AddressCreateDto;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.dto.UserInfoDto;
import edu.online.messenger.model.dto.page.PageContentDto;
import edu.online.messenger.model.dto.page.PageParamDto;
import edu.online.messenger.model.entity.dto.AddressFilterDto;
import edu.online.messenger.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("existence/id/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean existsById(@PathVariable Long userId) {
        log.info("Received request to check existence of user by id: {}", userId);
        boolean exists = userService.existsById(userId);
        log.info("Existence check result for user id {}: {}", userId, exists);
        return exists;
    }

    @GetMapping("/existence/login/{login}")
    @ResponseStatus(HttpStatus.OK)
    public boolean existsByLogin(@PathVariable String login) {
        log.info("Received request to check existence of user by login: {}", login);
        boolean exists = userService.existsByLogin(login);
        log.info("Existence check result for user login '{}': {}", login, exists);
        return exists;
    }

    @GetMapping("/login/{login}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserByLogin(@PathVariable String login) {
        log.info("Received request to get user by login: {}", login);
        UserDto userDto = userService.getUserByLogin(login);
        log.info("Returning user data for login '{}': {}", login, userDto);
        return userDto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Received request to get user by id: {}", id);
        UserDto userDto = userService.getUserById(id);
        log.info("Returning user data for id {}: {}", id, userDto);
        return userDto;
    }

    @GetMapping("/address/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDto> getAddressListByUserId(@PathVariable Long userId) {
        log.info("Received request to get address list for user id: {}", userId);
        List<AddressDto> addresses = userService.getAddressListByUserId(userId);
        log.info("Returning {} addresses for user id {}", addresses.size(), userId);
        return addresses;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageContentDto<UserDto> findAll(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "postalCode", required = false) String postalCode,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "street", required = false) String street,
            @RequestParam(value = "house", required = false) String house,
            @RequestParam(value = "housing", required = false) String housing,
            @RequestParam(value = "apartment", required = false) String apartment
    ) {
        log.info("Received request to find all users with filters: country={}, postalCode={}, city={}, street={}, house={}, housing={}, apartment={}, pageNumber={}, pageSize={}",
                country, postalCode, city, street, house, housing, apartment, pageNumber, pageSize);

        PageContentDto<UserDto> result = userService.findAll(
                new PageParamDto(pageNumber, pageSize),
                new AddressFilterDto(country, postalCode, city, street, house, housing, apartment));

        log.info("Returning page content with {} users", result.content().size());
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Valid @RequestBody UserInfoDto userInfoDto) {
        log.info("Received request to save user: {}", userInfoDto);
        UserDto savedUser = userService.save(userInfoDto);
        log.info("User saved successfully: {}", savedUser);
        return savedUser;
    }


    @PostMapping("/address")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDto addAddressByUserId(@Valid @RequestBody AddressCreateDto addressCreateDto) {
        log.info("Received request to add address: {}", addressCreateDto);
        AddressDto savedAddress = userService.addAddressByUserId(addressCreateDto);
        log.info("Address added successfully: {}", savedAddress);
        return savedAddress;
    }

    @DeleteMapping("/address/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddressById(@PathVariable Long id) {
        log.info("Received request to delete address with id: {}", id);
        userService.deleteAddressById(id);
        log.info("Address with id {} deleted successfully", id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        log.info("Received request to delete user with id: {}", id);
        userService.deleteUserById(id);
        log.info("User with id {} deleted successfully", id);
    }
}