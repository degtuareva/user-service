package edu.online.messenger.service;

import edu.online.messenger.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    public boolean exists(Long id) {
        return repository.existsById(id);
    }
}
