package edu.online.messenger.service.impl;

import edu.online.messenger.exception.UserNotFoundException;
import edu.online.messenger.mapper.AddressMapper;
import edu.online.messenger.mapper.UserMapper;
import edu.online.messenger.model.dto.AddressCreateDto;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.dto.UserInfoDto;
import edu.online.messenger.model.entity.Address;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.repository.AddressRepository;
import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

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
    public UserDto saveUser(UserInfoDto userInfoDto) {
        User createdUser = userRepository.save(userMapper.toUser(userInfoDto));
        return userMapper.toDto(createdUser);
    }

    @Override
    @Transactional
    public AddressDto addAddressToUser(AddressCreateDto addressCreateDto) {
        Long userId = (long) addressCreateDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Address address = addressMapper.toEntity(addressCreateDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    @Override
    public List<AddressDto> getAddressesByUserId(Long userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAddressById(Long id) {
        addressRepository.findById(id).ifPresent(addressRepository::delete);
    }
}