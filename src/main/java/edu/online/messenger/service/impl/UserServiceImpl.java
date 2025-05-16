package edu.online.messenger.service.impl;

import edu.online.messenger.exception.UserNotFoundException;
import edu.online.messenger.mapper.AddressMapper;
import edu.online.messenger.mapper.UserMapper;
import edu.online.messenger.model.dto.AddressCreateDto;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.dto.UserInfoDto;
import edu.online.messenger.model.dto.page.PageContentDto;
import edu.online.messenger.model.dto.page.PageDto;
import edu.online.messenger.model.dto.page.PageParamDto;
import edu.online.messenger.model.entity.Address;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.model.entity.dto.AddressFilterDto;
import edu.online.messenger.model.entity.parent.BaseEntity;
import edu.online.messenger.repository.AddressRepository;
import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.service.UserService;
import edu.online.messenger.specification.AddressSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    @Override
    public boolean existsById(Long id) {
        log.debug("Checking existence of user by id: {}", id);
        boolean exists = userRepository.existsById(id);
        log.debug("Existence result for user id {}: {}", id, exists);
        return exists;
    }

    @Override
    public boolean existsByLogin(String login) {
        log.debug("Checking existence of user by login: {}", login);
        boolean exists = userRepository.existsByLogin(login);
        log.debug("Existence result for user login '{}': {}", login, exists);
        return exists;
    }

    @Override
    public UserDto getUserByLogin(String login) {
        log.info("Fetching user by login: {}", login);
        UserDto userDto = userRepository.findByLogin(login)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.error("User not found with login: {}", login);
                    return new UserNotFoundException(login);
                });
        log.info("User found: {}", userDto);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        UserDto userDto = userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new UserNotFoundException(id);
                });
        log.info("User found: {}", userDto);
        return userDto;
    }
    @Override
    public List<AddressDto> getAddressListByUserId(Long userId) {
        log.info("Fetching addresses for user id: {}", userId);
        List<Address> addresses = addressRepository.findByUserId(userId);
        List<AddressDto> addressDtos = addresses.stream()
                .map(addressMapper::toDto)
                .toList();
        log.info("Found {} addresses for user id {}", addressDtos.size(), userId);
        return addressDtos;
    }

    @Override
    public PageContentDto<UserDto> findAll(PageParamDto pageParamDto, AddressFilterDto addressFilterDto) {
        log.info("Finding all users with filters: {} and pagination: {}", addressFilterDto, pageParamDto);
        Pageable pageable = PageRequest.of(pageParamDto.pageNumber() - 1, pageParamDto.pageSize());
        Specification<Address> spec = AddressSpecification.findAll(addressFilterDto);
        Page<Address> addresses = addressRepository.findAll(spec, pageable);
        PageContentDto<UserDto> pageContentDto = convertToPageContentDto(addresses);
        log.info("Found {} users matching criteria", pageContentDto.content().size());
        return pageContentDto;
    }

    @Override
    @Transactional
    public UserDto save(UserInfoDto userInfoDto) {
        log.info("Attempting to save new user: {}", userInfoDto);
        User createdUser = userRepository.save(userMapper.toUser(userInfoDto));
        UserDto userDto = userMapper.toDto(createdUser);
        log.info("User successfully saved: {}", userDto);
        return userDto;
    }

    @Override
    @Transactional
    public AddressDto addAddressByUserId(AddressCreateDto addressCreateDto) {
        Long userId = addressCreateDto.getUserId();
        log.info("Adding address for userId {}: {}", userId, addressCreateDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new UserNotFoundException(userId);
                });
        Address address = addressMapper.toEntity(addressCreateDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        AddressDto addressDto = addressMapper.toDto(savedAddress);
        log.info("Address successfully added: {}", addressDto);
        return addressDto;
    }

    @Override
    @Transactional
    public void deleteAddressById(Long id) {
        log.info("Attempting to delete address with id: {}", id);
        addressRepository.findById(id).ifPresentOrElse(
                address -> {
                    addressRepository.delete(address);
                    log.info("Address with id {} successfully deleted", id);
                },
                () -> log.warn("Address with id {} not found for deletion", id)
        );
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        log.info("Attempting to delete user with id: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User with id {} successfully deleted", id);
        } else {
            log.warn("User with id {} not found for deletion", id);
        }
    }

    private PageContentDto<UserDto> convertToPageContentDto(Page<Address> page) {
        log.debug("Converting Page<Address> to PageContentDto<UserDto>");
        Set<Long> userIds = page.getContent()
                .stream()
                .map(Address::getUser)
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());
        log.debug("Extracted user IDs from addresses: {}", userIds);
        List<UserDto> userDtoList = userRepository.findAllById(userIds)
                .stream()
                .map(userMapper::toDto)
                .toList();
        PageDto pageDto = new PageDto(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements());
        log.debug("PageDto created: {}", pageDto);
        return new PageContentDto<>(pageDto, userDtoList);
    }
}
