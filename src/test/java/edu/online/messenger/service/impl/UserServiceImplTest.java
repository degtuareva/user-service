package edu.online.messenger.service.impl;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

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
}