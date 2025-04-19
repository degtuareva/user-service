package edu.online.messenger.service;

import edu.online.messenger.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
}
