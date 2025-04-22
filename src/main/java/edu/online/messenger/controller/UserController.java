package edu.online.messenger.controller;

import edu.online.messenger.mapper.UserMapper;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("existence/id/{userId}")
    public boolean existsById(@PathVariable Long userId) {
        return userService.existsById(userId);
    }

    @GetMapping("/existence/login/{login}")
    public boolean existsByLogin(@PathVariable String login) {
        return userService.existsByLogin(login);
    @GetMapping("/login/{login}")
    public UserDto getUserByLogin(@PathVariable String login) {
        return userMapper.mapper(userService.getUserByLogin(login));
    }
}
