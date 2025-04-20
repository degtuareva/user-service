package edu.online.messenger.controller;

import edu.online.messenger.converter.UserConverter;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    @GetMapping("/login/{login}")
    public UserDto getUserByLogin(@PathVariable String login) {
        return userConverter.converter(userService.getUserByLogin(login));
    }
}
