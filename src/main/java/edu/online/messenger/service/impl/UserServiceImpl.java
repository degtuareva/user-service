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
        log.debug("existsById - входящий параметр id: {}", id);
        boolean result = userRepository.existsById(id);
        log.debug("existsById - результат: {}", result);
        log.info("Результат existsById для id {}: {}", id, result);
        return result;
    }

    @Override
    public boolean existsByLogin(String login) {
        log.info("Проверка существования пользователя по логину: {}", login);
        log.debug("existsByLogin - входящий параметр login: {}", login);
        boolean result = userRepository.existsByLogin(login);
        log.debug("existsByLogin - результат: {}", result);
        log.info("Результат existsByLogin для логина '{}': {}", login, result);
        return result;
    }

    @Override
    public UserDto getUserByLogin(String login) {
        log.info("Получение пользователя по логину: {}", login);
        log.debug("getUserByLogin - входящий параметр login: {}", login);
        UserDto userDto = userMapper.toDto(userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login)));
        log.debug("getUserByLogin - найден пользователь: {}", userDto);
        log.info("Пользователь найден: {}", userDto);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Получение пользователя по id: {}", id);
        log.debug("getUserById - входящий параметр id: {}", id);
        UserDto userDto = userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
        log.debug("getUserById - найден пользователь: {}", userDto);
        log.info("Пользователь найден: {}", userDto);
        return userDto;
    }

    @Override
    public List<AddressDto> getAddressListByUserId(Long userId) {
        log.info("Получение адресов пользователя с id: {}", userId);
        log.debug("getAddressListByUserId - входящий параметр userId: {}", userId);
        List<AddressDto> addresses = addressRepository.findByUserId(userId)
                .stream()
                .map(addressMapper::toDto)
                .toList();
        log.debug("getAddressListByUserId - найдено адресов: {}", addresses.size());
        log.info("Найдено адресов: {}", addresses.size());
        return addresses;
    }

    @Override
    public PageContentDto<UserDto> findAll(PageParamDto pageParamDto, AddressFilterDto addressFilterDto) {
        log.info("Поиск пользователей с фильтрами: {}, страница {}, размер {}", addressFilterDto, pageParamDto.pageNumber(), pageParamDto.pageSize());
        log.debug("findAll - входящие параметры: pageParamDto={}, addressFilterDto={}", pageParamDto, addressFilterDto);
        Pageable pageable = PageRequest.of(pageParamDto.pageNumber() - 1, pageParamDto.pageSize());
        Specification<Address> spec = AddressSpecification.findAll(addressFilterDto);
        Page<Address> addresses = addressRepository.findAll(spec, pageable);
        PageContentDto<UserDto> result = convertToPageContentDto(addresses);
        log.debug("findAll - найдено пользователей на странице: {}", result.content().size());
        log.info("Найдено пользователей на странице: {}", result.content().size());
        return result;
    }

    @Override
    @Transactional
    public UserDto save(UserInfoDto userInfoDto) {
        log.info("Сохранение пользователя: {}", userInfoDto);
        log.debug("save - входящий параметр userInfoDto: {}", userInfoDto);
        User createdUser = userRepository.save(userMapper.toUser(userInfoDto));
        UserDto userDto = userMapper.toDto(createdUser);
        log.debug("save - сохранён пользователь: {}", userDto);
        log.info("Пользователь сохранён: {}", userDto);
        return userDto;
    }

    @Override
    @Transactional
    public AddressDto addAddressByUserId(AddressCreateDto addressCreateDto) {
        log.info("Добавление адреса: {}", addressCreateDto);
        log.debug("addAddressByUserId - входящий параметр addressCreateDto: {}", addressCreateDto);
        Long userId = addressCreateDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Address address = addressMapper.toEntity(addressCreateDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        AddressDto addressDto = addressMapper.toDto(savedAddress);
        log.debug("addAddressByUserId - добавлен адрес: {}", addressDto);
        log.info("Адрес добавлен: {}", addressDto);
        return addressDto;
    }

    @Override
    @Transactional
    public void deleteAddressById(Long id) {
        log.info("Удаление адреса с id: {}", id);
        log.debug("deleteAddressById - входящий параметр id: {}", id);
        addressRepository.findById(id).ifPresent(addressRepository::delete);
        log.debug("deleteAddressById - адрес с id {} удалён (если существовал)", id);
        log.info("Адрес с id {} удалён (если существовал)", id);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        log.info("Удаление пользователя с id: {}", id);
        log.debug("deleteUserById - входящий параметр id: {}", id);
        userRepository.deleteById(id);
        log.debug("deleteUserById - пользователь с id {} удалён", id);
        log.info("Пользователь с id {} удалён", id);
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
}