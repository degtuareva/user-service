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
        log.info("Получен запрос на проверку существования пользователя по id: {}", userId);
        boolean exists = userService.existsById(userId);
        log.info("Результат проверки существования пользователя с id {}: {}", userId, exists);
        return exists;
    }

    @GetMapping("/existence/login/{login}")
    @ResponseStatus(HttpStatus.OK)
    public boolean existsByLogin(@PathVariable String login) {
        log.info("Получен запрос на проверку существования пользователя по логину: {}", login);
        boolean exists = userService.existsByLogin(login);
        log.info("Результат проверки существования пользователя с логином'{}': {}", login, exists);
        return exists;
    }

    @GetMapping("/login/{login}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserByLogin(@PathVariable String login) {
        log.info("Получен запрос на получение пользователя по логину: {}", login);
        UserDto userDto = userService.getUserByLogin(login);
        log.info("Возвращены данные пользователя по логину  '{}': {}", login, userDto);
        return userDto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Получен запрос на получение пользователя по id: {}", id);
        UserDto userDto = userService.getUserById(id);
        log.info("Возвращены данные пользователя по id {}: {}", id, userDto);
        return userDto;
    }

    @GetMapping("/address/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDto> getAddressListByUserId(@PathVariable Long userId) {
        log.info("Получен запрос на получение списка адресов пользователя с id: {}", userId);
        List<AddressDto> addresses = userService.getAddressListByUserId(userId);
        log.info("Возвращен список {} адресов для пользователя с id {}", addresses.size(), userId);
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
        log.info("Получен запрос на поиск всех пользователей с фильтрами: country={}, postalCode={}, city={}, street={}, house={}, housing={}, apartment={}, pageNumber={}, pageSize={}",
                country, postalCode, city, street, house, housing, apartment, pageNumber, pageSize);

        PageContentDto<UserDto> result = userService.findAll(
                new PageParamDto(pageNumber, pageSize),
                new AddressFilterDto(country, postalCode, city, street, house, housing, apartment));

        log.info("Возвращено содержимое страницы с {} пользователями", result.content().size());
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Valid @RequestBody UserInfoDto userInfoDto) {
        log.info("Получен запрос на сохранение пользователя: {}", userInfoDto);
        UserDto savedUser = userService.save(userInfoDto);
        log.info("Пользователь успешно сохранён: {}", savedUser);
        return savedUser;
    }

    @PostMapping("/address")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDto addAddressByUserId(@Valid @RequestBody AddressCreateDto addressCreateDto) {
        log.info("Получен запрос на добавление адреса: {}", addressCreateDto);
        AddressDto savedAddress = userService.addAddressByUserId(addressCreateDto);
        log.info("Адрес успешно добавлен: {}", savedAddress);
        return savedAddress;
    }

    @DeleteMapping("/address/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddressById(@PathVariable Long id) {
        log.info("Получен запрос на удаление адреса с id: {}", id);
        userService.deleteAddressById(id);
        log.info("Адрес с id {} успешно удалён", id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя с id: {}", id);
        userService.deleteUserById(id);
        log.info("Пользователь с id {} успешно удалён", id);
    }
}