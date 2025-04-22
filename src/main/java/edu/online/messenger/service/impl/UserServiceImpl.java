package edu.online.messenger.service.impl;

import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserRepository userRepository;

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    public boolean existsByLogin(String login) {

        return userRepository.existsByLogin(login);
    }
}
