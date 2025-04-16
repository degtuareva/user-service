package edu.online.messenger.service;

import edu.online.messenger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean userExistsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

}
