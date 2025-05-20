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
import org.springframework.web.bind.annotation.*;

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
        log.info("Проверка существования пользователя по id: {}", userId);
        return userService.existsById(userId);
    }

    @GetMapping("/existence/login/{login}")
    @ResponseStatus(HttpStatus.OK)
    public boolean existsByLogin(@PathVariable String login) {
        log.info("Проверка существования пользователя по логину: {}", login);
        return userService.existsByLogin(login);
    }

    @GetMapping("/login/{login}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserByLogin(@PathVariable String login) {
        log.info("Получение пользователя по логину: {}", login);
        return userService.getUserByLogin(login);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Получение пользователя по id: {}", id);
        return userService.getUserById(id);
    }

    @GetMapping("/address/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDto> getAddressListByUserId(@PathVariable Long userId) {
        log.info("Получение адресов пользователя с id: {}", userId);
        return userService.getAddressListByUserId(userId);
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
        log.info("Поиск пользователей с фильтрами country={}, postalCode={}, city={}, street={}, house={}, housing={}, apartment={}, pageNumber={}, pageSize={}",
                country, postalCode, city, street, house, housing, apartment, pageNumber, pageSize);
        return userService.findAll(
                new PageParamDto(pageNumber, pageSize),
                new AddressFilterDto(country, postalCode, city, street, house, housing, apartment));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Valid @RequestBody UserInfoDto userInfoDto) {
        log.info("Сохранение пользователя: {}", userInfoDto);
        return userService.save(userInfoDto);
    }

    @PostMapping("/address")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDto addAddressByUserId(@Valid @RequestBody AddressCreateDto addressCreateDto) {
        log.info("Добавление адреса: {}", addressCreateDto);
        return userService.addAddressByUserId(addressCreateDto);
    }

    @DeleteMapping("/address/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddressById(@PathVariable Long id) {
        log.info("Удаление адреса с id: {}", id);
        userService.deleteAddressById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        log.info("Удаление пользователя с id: {}", id);
        userService.deleteUserById(id);
    }
}

