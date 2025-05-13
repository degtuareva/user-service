package edu.online.messenger.service.impl;

import edu.online.messenger.exception.UserNotFoundException;
import edu.online.messenger.mapper.AddressMapper;
import edu.online.messenger.mapper.UserMapper;
import edu.online.messenger.model.dto.AddressCreateDto;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.dto.PageContentDto;
import edu.online.messenger.model.dto.PageDto;
import edu.online.messenger.model.dto.PageParamDto;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.dto.UserInfoDto;
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
    public UserDto getUserByLogin(String login) {
        return userMapper.toDto(userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login)));
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public List<AddressDto> getAddressListByUserId(Long userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    public PageContentDto<UserDto> findAll(PageParamDto pageParamDto, AddressFilterDto addressFilterDto) {
        Pageable pageable = PageRequest.of(pageParamDto.pageNumber() - 1, pageParamDto.pageSize());
        Specification<Address> spec = AddressSpecification.findAll(addressFilterDto);
        Page<Address> addresses = addressRepository.findAll(spec, pageable);
        return convertToPageContentDto(addresses);
    }

    @Override
    @Transactional
    public UserDto save(UserInfoDto userInfoDto) {
        User createdUser = userRepository.save(userMapper.toUser(userInfoDto));
        return userMapper.toDto(createdUser);
    }

    @Override
    @Transactional
    public AddressDto addAddressByUserId(AddressCreateDto addressCreateDto) {
        Long userId = addressCreateDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Address address = addressMapper.toEntity(addressCreateDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    @Override
    @Transactional
    public void deleteAddressById(Long id) {
        addressRepository.findById(id).ifPresent(addressRepository::delete);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
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