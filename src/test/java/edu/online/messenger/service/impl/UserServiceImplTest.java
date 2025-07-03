package edu.online.messenger.service.impl;

import edu.online.messenger.exception.UserNotFoundException;
import edu.online.messenger.mapper.AddressMapper;
import edu.online.messenger.mapper.UserMapper;
import edu.online.messenger.model.dto.AddressCreateDto;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.entity.Address;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.repository.AddressRepository;
import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.util.AddressTestBuilder;
import edu.online.messenger.util.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void existsByIdShouldReturnTrueWhenUserExists() {
        User user = UserTestBuilder.builder().build().buildUser();

        when(userRepository.existsById(user.getId())).thenReturn(true);

        boolean result = userService.existsById(user.getId());

        assertTrue(result);
        verify(userRepository, times(1)).existsById(user.getId());
    }

    @Test
    void existsByIdShouldReturnFalseWhenUserDoesNotExist() {
        Long id = 15L;

        when(userRepository.existsById(id)).thenReturn(false);

        boolean result = userService.existsById(id);

        assertFalse(result);
        verify(userRepository, times(1)).existsById(id);
    }

    @Test
    void deleteAddressByIdShouldInvokeRepositoryWhenAddressExists() {
        Address address = AddressTestBuilder.builder().build().buildAddress();

        userService.deleteAddressById(address.getId());

        verify(addressRepository, times(1)).deleteById(address.getId());
    }

    @Test
    void deleteAddressByIdShouldThrowExceptionWhenAddressNotFound() {
        Long id = 30L;

        doThrow(EmptyResultDataAccessException.class).when(addressRepository).deleteById(id);

        assertThrows(EmptyResultDataAccessException.class, () -> userService.deleteAddressById(id));
        verify(addressRepository, times(1)).deleteById(id);
    }

    @Test
    void existsByLoginShouldReturnTrueWhenUserExists() {
        String login = "test";

        when(userRepository.existsByLogin(login)).thenReturn(true);

        boolean result = userService.existsByLogin(login);

        assertTrue(result);
        verify(userRepository, times(1)).existsByLogin(login);
    }

    @Test
    void existsByLoginShouldReturnFalseWhenUserDoesNotExist() {
        String login = "test";

        when(userRepository.existsByLogin(login)).thenReturn(false);

        boolean result = userService.existsByLogin(login);

        assertFalse(result);
        verify(userRepository, times(1)).existsByLogin(login);
    }

    @Test
    void deleteUserShouldInvokeRepositoryWhenUserExists() {
        Long id = 3L;

        userService.deleteUserById(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteUserShouldThrowExceptionWhenUserDoesNotExist() {
        Long id = 666L;

        doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(id);

        assertThrows(EmptyResultDataAccessException.class, () -> userService.deleteUserById(id));
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void getUserByIdShouldReturnUserDtoWhenUserExists() {
        Long userId = 5L;
        User user = UserTestBuilder.builder().withId(userId).build().buildUser();
        UserDto userDto = UserTestBuilder.builder().withId(userId).build().buildUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserDoesNotExist() {
        Long userId = 666L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void getAddressListByUserIdShouldReturnAddressDtoListWhenAddressExist() {
        Long userId = 5L;

        List<Address> addresses = List.of(
                AddressTestBuilder.builder().withId(1L).build().buildAddress(),
                AddressTestBuilder.builder().withId(2L).build().buildAddress()
        );

        List<AddressDto> expectedDto = List.of(
                AddressTestBuilder.builder().withId(1L).build().buildAddressDto(),
                AddressTestBuilder.builder().withId(2L).build().buildAddressDto()
        );

        when(addressRepository.findByUserId(userId)).thenReturn(addresses);
        when(addressMapper.toDto(any(Address.class))).thenAnswer(invocation -> {
            Address address = invocation.getArgument(0);
            return expectedDto.stream()
                    .filter((AddressDto dto) -> dto.getId().equals(address.getId()))
                    .findFirst()
                    .orElseThrow();
        });

        List<AddressDto> addressDtoList = userService.getAddressListByUserId(userId);

        assertEquals(expectedDto.size(), addressDtoList.size());
        assertEquals(expectedDto.get(0), addressDtoList.get(0));
        assertEquals(expectedDto.get(1), addressDtoList.get(1));

        verify(addressRepository, times(1)).findByUserId(userId);
        verify(addressMapper, times(addresses.size())).toDto(any(Address.class));
    }

    @Test
    void getAddressListByUserIdShouldReturnEmptyListWhenAddressDoesNotExist() {
        Long userId = 77L;

        when(addressRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<AddressDto> addressDtoList = userService.getAddressListByUserId(userId);

        assertTrue(addressDtoList.isEmpty());

        verify(addressRepository, times(1)).findByUserId(userId);
        verify(addressMapper, never()).toDto(any());
    }

    @Test
    void getUserByLoginShouldReturnUserDtoWhenUserExists() {
        String login = "testLogin";
        User user = UserTestBuilder.builder().build().buildUser();
        UserDto userDto = UserTestBuilder.builder().build().buildUserDto();

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserByLogin(login);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        verify(userRepository, times(1)).findByLogin(login);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void getUserByLoginShouldThrowExceptionWhenUserDoesNotExist() {
        String login = "notExist";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByLogin(login));
        verify(userRepository, times(1)).findByLogin(login);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void addAddressByUserIdShouldAddAddressWhenUserExists() {
        Long userId = 5L;
        AddressCreateDto addressCreateDto = new AddressCreateDto();
        addressCreateDto.setUserId(userId);

        User user = UserTestBuilder.builder().withId(userId).build().buildUser();
        Address address = AddressTestBuilder.builder().build().buildAddress();
        Address savedAddress = AddressTestBuilder.builder().withId(123L).build().buildAddress();
        AddressDto addressDto = AddressTestBuilder.builder().withId(123L).build().buildAddressDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressMapper.toEntity(addressCreateDto)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(savedAddress);
        when(addressMapper.toDto(savedAddress)).thenReturn(addressDto);

        AddressDto result = userService.addAddressByUserId(addressCreateDto);

        assertNotNull(result);
        assertEquals(addressDto.getId(), result.getId());
        verify(userRepository, times(1)).findById(userId);
        verify(addressMapper, times(1)).toEntity(addressCreateDto);
        verify(addressRepository, times(1)).save(address);
        verify(addressMapper, times(1)).toDto(savedAddress);
    }

    @Test
    void addAddressByUserIdShouldThrowExceptionWhenUserDoesNotExist() {
        Long userId = 999L;
        AddressCreateDto addressCreateDto = new AddressCreateDto();
        addressCreateDto.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.addAddressByUserId(addressCreateDto));
        verify(userRepository, times(1)).findById(userId);
        verify(addressMapper, never()).toEntity(any());
        verify(addressRepository, never()).save(any());
        verify(addressMapper, never()).toDto(any());
    }
}