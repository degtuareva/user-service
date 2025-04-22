package edu.online.messenger.service.impl;

import edu.online.messenger.exception.UserNotFoundException;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserRepository userRepository;

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }
}
