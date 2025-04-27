package edu.online.messenger.service.impl;

import edu.online.messenger.converter.UserConverter;
import edu.online.messenger.exception.UserNotFoundException;
import edu.online.messenger.mapper.UserMapper;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }

    public UserDto getUserDtoByLogin(String login) {
        return userMapper.toDto(repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login)));
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userConverter.toDto(user);
    }
}
