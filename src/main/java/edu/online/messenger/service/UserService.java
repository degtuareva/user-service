package edu.online.messenger.service;

import edu.online.messenger.exception.UserNotFoundException;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }
}
