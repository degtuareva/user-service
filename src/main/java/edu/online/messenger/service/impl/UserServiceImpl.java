package edu.online.messenger.service.impl;

import edu.online.messenger.exception.UserNotFoundException;
import edu.online.messenger.mapper.UserMapper;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.repository.AddressRepository;
import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressRepository addressRepository;

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public UserDto getUserDtoByLogin(String login) {
        return userMapper.toDto(userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login)));
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAddressById(Long id) {
        addressRepository.findById(id).ifPresent(addressRepository::delete);
    }
}