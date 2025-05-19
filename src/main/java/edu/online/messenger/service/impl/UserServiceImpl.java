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
        log.debug("Проверка существования пользователя по id: {}", id);
        boolean exists = userRepository.existsById(id);
        log.debug("Результат проверки существования пользователя с id {}: {}", id, exists);
        return exists;
    }

    @Override
    public boolean existsByLogin(String login) {
        log.debug("Проверка существования пользователя по логину: {}", login);
        boolean exists = userRepository.existsByLogin(login);
        log.debug("Результат проверки существования пользователя с логином '{}': {}", login, exists);
        return exists;
    }

    @Override
    public UserDto getUserByLogin(String login) {
        log.info("Получение пользователя по логину: {}", login);
        UserDto userDto = userRepository.findByLogin(login)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Пользователь с логином '{}' не найден", login);
                    return new UserNotFoundException(login);
                });
        log.info("Пользователь с логином найден: {}", userDto);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Получение пользователя по id: {}", id);
        UserDto userDto = userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", id);
                    return new UserNotFoundException(id);
                });
        log.info("Пользователь с id найден: {}", userDto);
        return userDto;
    }

    @Override
    public List<AddressDto> getAddressListByUserId(Long userId) {
        log.info("Получение адресов для пользователя id: {}", userId);
        List<Address> addresses = addressRepository.findByUserId(userId);
        List<AddressDto> addressDto = addresses.stream()
                .map(addressMapper::toDto)
                .toList();
        log.info("Найдено {} адресов для пользователя с id {}", addressDto.size(), userId);
        return addressDto;
    }

    @Override
    public PageContentDto<UserDto> findAll(PageParamDto pageParamDto, AddressFilterDto addressFilterDto) {
        log.info("Поиск всех пользователей с фильтрами: {} и пагинацией: {}", addressFilterDto, pageParamDto);
        Pageable pageable = PageRequest.of(pageParamDto.pageNumber() - 1, pageParamDto.pageSize());
        Specification<Address> spec = AddressSpecification.findAll(addressFilterDto);
        Page<Address> addresses = addressRepository.findAll(spec, pageable);
        PageContentDto<UserDto> pageContentDto = convertToPageContentDto(addresses);
        log.info("Найдено {} пользователей, соответствующих критериям", pageContentDto.content().size());
        return pageContentDto;
    }

    @Override
    @Transactional
    public UserDto save(UserInfoDto userInfoDto) {
        log.info("Попытка сохранить нового пользователя: {}", userInfoDto);
        User createdUser = userRepository.save(userMapper.toUser(userInfoDto));
        UserDto userDto = userMapper.toDto(createdUser);
        log.info("Пользователь успешно сохранён: {}", userDto);
        return userDto;
    }

    @Override
    @Transactional
    public AddressDto addAddressByUserId(AddressCreateDto addressCreateDto) {
        Long userId = addressCreateDto.getUserId();
        log.info("Добавление адреса для userId {}: {}", userId, addressCreateDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", userId);
                    return new UserNotFoundException(userId);
                });
        Address address = addressMapper.toEntity(addressCreateDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        AddressDto addressDto = addressMapper.toDto(savedAddress);
        log.info("Адрес успешно добавлен: {}", addressDto);
        return addressDto;
    }

    @Override
    @Transactional
    public void deleteAddressById(Long id) {
        log.info("Попытка удалить адрес с id: {}", id);
        addressRepository.findById(id).ifPresentOrElse(
                address -> {
                    addressRepository.delete(address);
                    log.info("Адрес с id {} успешно удалён", id);
                },
                () -> log.warn("Адрес с id {} не найден для удаления", id)
        );
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        log.info("Попытка удалить пользователя с id: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("Пользователь с id {} успешно удалён", id);
        } else {
            log.warn("Пользователь с id {} не найден для удаления", id);
        }
    }

    private PageContentDto<UserDto> convertToPageContentDto(Page<Address> page) {
        log.debug("Преобразование Page<Address> в PageContentDto<UserDto>");
        Set<Long> userIds = page.getContent()
                .stream()
                .map(Address::getUser)
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());
        log.debug("Извлечены id пользователей из адресов: {}", userIds);
        List<UserDto> userDtoList = userRepository.findAllById(userIds)
                .stream()
                .map(userMapper::toDto)
                .toList();
        PageDto pageDto = new PageDto(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements());
        log.debug("Создан PageDto: {}", pageDto);
        return new PageContentDto<>(pageDto, userDtoList);
    }
}