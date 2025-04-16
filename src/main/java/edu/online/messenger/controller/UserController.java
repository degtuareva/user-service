package edu.online.messenger.controller;

import edu.online.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("existence/id/{userId}")
    public boolean exists(@PathVariable Long userId) {
        return userService.exists(userId);
    }
}
