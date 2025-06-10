package edu.online.messenger.controller;

import edu.online.messenger.model.dto.AddressCreateDto;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.dto.UserInfoDto;
import edu.online.messenger.model.dto.page.PageContentDto;
import edu.online.messenger.model.dto.page.PageParamDto;
import edu.online.messenger.model.entity.dto.AddressFilterDto;
import edu.online.messenger.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
public class UserController {

    private final UserService userService;

    @GetMapping("existence/id/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean existsById(@PathVariable Long userId) {
        return userService.existsById(userId);
    }

    @GetMapping("/existence/login/{login}")
    @ResponseStatus(HttpStatus.OK)
    public boolean existsByLogin(@PathVariable String login) {
        return userService.existsByLogin(login);
    }

    @GetMapping("/login/{login}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserByLogin(@PathVariable String login) {
        return userService.getUserByLogin(login);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/address/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDto> getAddressListByUserId(@PathVariable Long userId) {
        return userService.getAddressListByUserId(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageContentDto<UserDto> findAll(@Valid
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
        return userService.findAll(
                new PageParamDto(pageNumber, pageSize),
                new AddressFilterDto(country, postalCode, city, street, house, housing, apartment));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Valid @RequestBody UserInfoDto userInfoDto) {
        return userService.save(userInfoDto);
    }

    @PostMapping("/address")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDto addAddressByUserId(@Valid @RequestBody AddressCreateDto addressCreateDto) {
        return userService.addAddressByUserId(addressCreateDto);
    }

    @DeleteMapping("/address/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddressById(@PathVariable Long id) {
        userService.deleteAddressById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}