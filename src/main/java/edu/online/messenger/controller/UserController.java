package edu.online.messenger.controller;

import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /*
     * GET:http://localhost/api/users/1
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable ("id") Long id) {
        return  userService.getUserById(id);
    }
}
