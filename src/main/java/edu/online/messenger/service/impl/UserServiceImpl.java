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
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByLogin(String login) {
        log.info("Проверка существования пользователя по логину: {}", login);
        return userRepository.existsByLogin(login);
    }

    @Override
    public UserDto getUserByLogin(String login) {
        log.info("Поиск пользователя по логину: {}", login);
        return userMapper.toDto(userRepository.findByLogin(login)
                .orElseThrow(() -> {
                    log.error("Пользователь с логином {} не найден", login);
                    return new UserNotFoundException(login);
                }));
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Поиск пользователя по id: {}", id);
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", id);
                    return new UserNotFoundException(id);
                }));
    }

    @Override
    public List<AddressDto> getAddressListByUserId(Long userId) {
        log.info("Получение списка адресов для пользователя с id: {}", userId);
        List<Address> addresses = addressRepository.findByUserId(userId);
        log.debug("Найдено {} адресов для пользователя с id: {}", addresses.size(), userId);
        return addresses.stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    public PageContentDto<UserDto> findAll(PageParamDto pageParamDto, AddressFilterDto addressFilterDto) {
        log.info("Поиск пользователей с фильтрами: {}, страница {}, размер {}",
                addressFilterDto, pageParamDto.pageNumber(), pageParamDto.pageSize());
        Pageable pageable = PageRequest.of(pageParamDto.pageNumber() - 1, pageParamDto.pageSize());

        Specification<Address> spec = AddressSpecification.findAll(addressFilterDto);
        Page<Address> addresses = addressRepository.findAll(spec, pageable);
        if (isFilterEmpty(addressFilterDto)) {
            log.info("Фильтр пуст, возвращаем всех пользователей без фильтрации");
            Page<User> users = userRepository.findAll(pageable);
            PageContentDto<UserDto> pageContentDto = convertUserPageToDto(users);
            log.debug("Найдено пользователей: {}", pageContentDto.content().size());
            return pageContentDto;
        }
        PageContentDto<UserDto> pageContentDto = convertToPageContentDto(addresses);
        log.debug("Найдено {} пользователей по фильтру адресов", pageContentDto.content().size());
        return pageContentDto;
    }

    @Override
    @Transactional
    public UserDto save(UserInfoDto userInfoDto) {
        log.info("Создание пользователя : {}", userInfoDto);
        return userMapper.toDto(userRepository.save(userMapper.toUser(userInfoDto)));
    }

    @Override
    @Transactional
    public AddressDto addAddressByUserId(AddressCreateDto addressCreateDto) {
        Long userId = addressCreateDto.getUserId();
        log.info("Добавление адреса для пользователя с id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден при добавлении адреса", userId);
                    return new UserNotFoundException(userId);
                });
        Address address = addressMapper.toEntity(addressCreateDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        AddressDto addressDto = addressMapper.toDto(savedAddress);
        log.debug("Адрес успешно добавлен с id: {} для пользователя с id:{}", addressDto.getId(), userId);
        return addressDto;
    }

    @Override
    @Transactional
    public void deleteAddressById(Long id) {
        log.info("Удаление адреса с id: {}", id);
        addressRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        log.info("Удаление пользователя с id: {}", id);
        userRepository.deleteById(id);
    }

    private boolean isFilterEmpty(AddressFilterDto addressFilterDto) {
        log.debug("Проверка пустоты фильтра адресов: {}", addressFilterDto);
        boolean empty = Arrays.stream(addressFilterDto.getClass().getRecordComponents())
                .map(recordComponent -> {
                    try {
                        return recordComponent.getAccessor().invoke(addressFilterDto);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Ошибка доступа к полю {} фильтра адресов" + recordComponent.getName());
                    }
                })
                .allMatch(Objects::isNull);
        log.debug("Проверка фильтра на пустоту: {}, фильтр: {}", empty, addressFilterDto);
        return empty;
    }

    private PageContentDto<UserDto> convertToPageContentDto(Page<Address> page) {
        log.debug("Конвертация страницы адресов в DTO пользователей");
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
        log.debug("Сформирована страница с {} пользователями", userDtoList.size());
        return new PageContentDto<>(pageDto, userDtoList);
    }

    private PageContentDto<UserDto> convertUserPageToDto(Page<User> page) {
        log.debug("Конвертация страницы пользователей в DTO");
        List<UserDto> userDtoList = page.getContent()
                .stream()
                .map(userMapper::toDto)
                .toList();
        PageDto pageDto = new PageDto(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements());
        log.debug("Сформирована страница с {} пользователями", userDtoList.size());
        return new PageContentDto<>(pageDto, userDtoList);
    }
}