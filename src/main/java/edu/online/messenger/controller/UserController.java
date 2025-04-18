package edu.online.messenger.controller;


import edu.online.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")

public class UserController {

    private UserService userService;

    @GetMapping("/existence/login/{login}")
    public ResponseEntity<Boolean> checkUserExistenceByLogin(@PathVariable String login) {
        boolean exists = userService.userExistsByLogin(login);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}
