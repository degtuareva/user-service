package edu.online.messenger.controller;

import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

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
        return userService.getUserDtoByLogin(login);
        }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
