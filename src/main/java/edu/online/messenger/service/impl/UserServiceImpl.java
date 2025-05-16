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
    @Transactional
    public boolean existsById(Long id) {
        log.debug("Checking existence of user by id: {}", id);
        boolean exists = userRepository.existsById(id);
        log.debug("User with id {} exists: {}", id, exists);
        return exists;
    }

    @Override
    @Transactional
    public boolean existsByLogin(String login) {
        log.debug("Checking existence of user by login: {}", login);
        boolean exists = userRepository.existsByLogin(login);
        log.debug("User with login '{}' exists: {}", login, exists);
        return exists;
    }

    @Override
    @Transactional
    public UserDto getUserByLogin(String login) {
        log.info("Fetching user by login: {}", login);
        try {
            User user = userRepository.findByLogin(login)
                    .orElseThrow(() -> new UserNotFoundException(login));
            UserDto userDto = userMapper.toDto(user);
            log.info("User found: {}", userDto);
            return userDto;
        } catch (UserNotFoundException e) {
            log.error("User not found with login: {}", login, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
            UserDto userDto = userMapper.toDto(user);
            log.info("User found: {}", userDto);
            return userDto;
        } catch (UserNotFoundException e) {
            log.error("User not found with id: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<AddressDto> getAddressListByUserId(Long userId) {
        log.info("Fetching addresses for user id: {}", userId);
        List<Address> addresses = addressRepository.findByUserId(userId);
        List<AddressDto> addressDto = addresses.stream()
                .map(addressMapper::toDto)
                .toList();
        log.info("Found {} addresses for user id {}", addressDto.size(), userId);
        return addressDto;
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
    public UserDto save(UserInfoDto userInfoDto) {
        log.info("Attempting to save new user: {}", userInfoDto);
        try {
            User user = userMapper.toUser(userInfoDto);
            User savedUser = userRepository.save(user);
            UserDto userDto = userMapper.toDto(savedUser);
            log.info("User successfully saved: {}", userDto);
            return userDto;
        } catch (Exception e) {
            log.error("Error occurred while saving user: {}", userInfoDto, e);
            throw e;
        }
    }

    //    @Override
//    @Transactional
//    public UserDto save(UserInfoDto userInfoDto) {
//        User createdUser = userRepository.save(userMapper.toUser(userInfoDto));
//        return userMapper.toDto(createdUser);
//    }
    @Override
    @Transactional
    public AddressDto addAddressByUserId(AddressCreateDto addressCreateDto) {
        log.info("Adding address for userId {}: {}", addressCreateDto.getUserId(), addressCreateDto);
        try {
            Address address = addressMapper.toEntity(addressCreateDto);
            Address savedAddress = addressRepository.save(address);
            AddressDto addressDto = addressMapper.toDto(savedAddress);
            log.info("Address successfully added: {}", addressDto);
            return addressDto;
        } catch (Exception e) {
            log.error("Error occurred while adding address: {}", addressCreateDto, e);
            throw e;
        }
    }
//    @Override
//    @Transactional
//    public AddressDto addAddressByUserId(AddressCreateDto addressCreateDto) {
//        Long userId = addressCreateDto.getUserId();
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException(userId));
//        Address address = addressMapper.toEntity(addressCreateDto);
//        address.setUser(user);
//        Address savedAddress = addressRepository.save(address);
//        return addressMapper.toDto(savedAddress);
//    }

    //    @Override
//    @Transactional
//    public void deleteAddressById(Long id) {
//        addressRepository.findById(id).ifPresent(addressRepository::delete);
//    }
    @Override
    @Transactional
    public void deleteAddressById(Long id) {
        log.info("Attempting to delete address with id: {}", id);
        try {
            addressRepository.deleteById(id);
            log.info("Address with id {} successfully deleted", id);
        } catch (Exception e) {
            log.error("Error occurred while deleting address with id: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        log.info("Attempting to delete user with id: {}", id);
        try {
            userRepository.deleteById(id);
            log.info("User with id {} successfully deleted", id);
        } catch (Exception e) {
            log.error("Error occurred while deleting user with id: {}", id, e);
            throw e;
        }
    }

    //    @Override
//    @Transactional
//    public void deleteUserById(Long id) {
//        userRepository.deleteById(id);
//    }

//    private PageContentDto<UserDto> convertToPageContentDto(Page<Address> page) {
//        Set<Long> userIds = page.getContent()
//                .stream()
//                .map(Address::getUser)
//                .map(BaseEntity::getId)
//                .collect(Collectors.toSet());
//        List<UserDto> userDtoList = userRepository.findAllById(userIds)
//                .stream()
//                .map(userMapper::toDto)
//                .toList();
//        PageDto pageDto = new PageDto(
//                page.getNumber() + 1,
//                page.getSize(),
//                page.getTotalPages(),
//                page.getTotalElements());
//        return new PageContentDto<>(pageDto, userDtoList);
//    }
}