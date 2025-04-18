package edu.online.messenger.service;

import edu.online.messenger.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;

    public boolean userExistsByLogin(String login) {

        return userRepository.existsByLogin(login);
    }

}
