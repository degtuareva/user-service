package edu.online.messenger.service;

import edu.online.messenger.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}

