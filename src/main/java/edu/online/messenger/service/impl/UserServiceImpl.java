package edu.online.messenger.service.impl;

import edu.online.messenger.exception.UserNotFoundException;
import edu.online.messenger.mapper.UserMapper;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.entity.Address;
import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }

    @Override
    public UserDto getUserDtoByLogin(String login) {
        return userMapper.toDto(repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login)));
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }

    public List<AddressDto> getAddressesByUserId(Long userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream()
                .map(userConverter::addressToDto)
                .collect(Collectors.toList());
    }
}
