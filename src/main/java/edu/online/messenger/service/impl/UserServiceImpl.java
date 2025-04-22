package edu.online.messenger.service.impl;

import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }
}
