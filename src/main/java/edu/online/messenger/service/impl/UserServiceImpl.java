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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    @Override
    public boolean existsById(Long id) {
        log.info("Проверка существования пользователя по id: {}", id);
        boolean exists = userRepository.existsById(id);
        log.info("Результат existsById для id {}: {}", id, exists);
        return exists;
    }

    @Override
    public boolean existsByLogin(String login) {
        log.info("Проверка существования пользователя по логину: {}", login);
        boolean exists = userRepository.existsByLogin(login);
        log.info("Результат existsByLogin для логина '{}': {}", login, exists);
        return exists;
    }

    @Override
    public UserDto getUserByLogin(String login) {
        log.info("Получение пользователя по логину: {}", login);
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> {
                    UserNotFoundException ex = new UserNotFoundException(login);
                    log.info("Ошибка: пользователь с логином {} не найден", login, ex);
                    throw ex;
                });
        UserDto userDto = userMapper.toDto(user);
        log.info("Пользователь найден: {}", userDto);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Получение пользователя по id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    UserNotFoundException ex = new UserNotFoundException(id);
                    log.info("Ошибка: пользователь с id {} не найден", id, ex);
                    throw ex;
                });
        UserDto userDto = userMapper.toDto(user);
        log.info("Пользователь найден: {}", userDto);
        return userDto;
    }

    @Override
    public List<AddressDto> getAddressListByUserId(Long userId) {
        log.info("Получение адресов пользователя с id: {}", userId);
        List<Address> addresses = addressRepository.findByUserId(userId);
        List<AddressDto> addressDtos = addresses.stream()
                .map(addressMapper::toDto)
                .toList();
        log.info("Найдено адресов: {}", addressDtos.size());
        return addressDtos;
    }

    @Override
    public PageContentDto<UserDto> findAll(PageParamDto pageParamDto, AddressFilterDto addressFilterDto) {
        log.info("Поиск пользователей с фильтрами: {}, страница {}, размер {}", addressFilterDto, pageParamDto.pageNumber(), pageParamDto.pageSize());
        Pageable pageable = PageRequest.of(pageParamDto.pageNumber() - 1, pageParamDto.pageSize());
        Specification<Address> spec = AddressSpecification.findAll(addressFilterDto);
        Page<Address> addresses = addressRepository.findAll(spec, pageable);
        if (isFilterEmpty(addressFilterDto)) {
            Page<User> users = userRepository.findAll(pageable);
            PageContentDto<UserDto> result = convertUserPageToDto(users);
            log.info("Найдено пользователей (без фильтра) на странице: {}", result.content().size());
            return result;
        }
        PageContentDto<UserDto> result = convertToPageContentDto(addresses);
        log.info("Найдено пользователей с фильтром на странице: {}", result.content().size());
        return result;
    }

    @Override
    @Transactional
    public UserDto save(UserInfoDto userInfoDto) {
        log.info("Сохранение пользователя: {}", userInfoDto);
        User createdUser = userRepository.save(userMapper.toUser(userInfoDto));
        UserDto userDto = userMapper.toDto(createdUser);
        log.info("Пользователь сохранён: {}", userDto);
        return userDto;
    }

    @Override
    @Transactional
    public AddressDto addAddressByUserId(AddressCreateDto addressCreateDto) {
        log.info("Добавление адреса: {}", addressCreateDto);
        Long userId = addressCreateDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    UserNotFoundException ex = new UserNotFoundException(userId);
                    log.info("Ошибка: пользователь с id {} не найден", userId, ex);
                    throw ex;
                });
        Address address = addressMapper.toEntity(addressCreateDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        AddressDto addressDto = addressMapper.toDto(savedAddress);
        log.info("Адрес добавлен: {}", addressDto);
        return addressDto;
    }

    @Override
    @Transactional
    public void deleteAddressById(Long id) {
        log.info("Удаление адреса с id: {}", id);
        addressRepository.findById(id).ifPresent(addressRepository::delete);
        log.info("Адрес с id {} удалён (если существовал)", id);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        log.info("Удаление пользователя с id: {}", id);
        userRepository.deleteById(id);
        log.info("Пользователь с id {} удалён", id);
    }

    private boolean isFilterEmpty(AddressFilterDto addressFilterDto) {
        return Arrays.stream(addressFilterDto.getClass().getRecordComponents())
                .map(recordComponent -> {
                    try {
                        return recordComponent.getAccessor().invoke(addressFilterDto);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Ошибка доступа к полю " + recordComponent.getName(), e);
                    }
                })
                .allMatch(Objects::isNull);
    }

    private PageContentDto<UserDto> convertToPageContentDto(Page<Address> page) {
        Set<Long> userIds = page.getContent()
                .stream()
                .map(Address::getUser)
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());
        List<UserDto> userDtoList = userRepository.findAllById(userIds)
                .stream()
                .map(userMapper::toDto)
                .toList();
        PageDto pageDto = new PageDto(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements());
        return new PageContentDto<>(pageDto, userDtoList);
    }

    private PageContentDto<UserDto> convertUserPageToDto(Page<User> page) {
        List<UserDto> userDtoList = page.getContent()
                .stream()
                .map(userMapper::toDto)
                .toList();
        PageDto pageDto = new PageDto(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements());
        return new PageContentDto<>(pageDto, userDtoList);
    }
}
