package edu.online.messenger.service.impl;

import edu.online.messenger.mapper.AddressMapper;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.entity.Address;
import edu.online.messenger.repository.AddressRepository;
import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.util.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
    private AddressMapper addressMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private final UserTestBuilder testBuilder = UserTestBuilder.builder().build();

    @Test
    void existsByIdShouldReturnTrueWhenUserExists() {
        Long userId = 15L;

        when(userRepository.existsById(userId)).thenReturn(true);

        boolean result = userServiceImpl.existsById(userId);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    void existsByIdShouldReturnFalseWhenUserDoesNotExist() {
        Long userId = 500L;

        when(userRepository.existsById(userId)).thenReturn(false);

        boolean result = userServiceImpl.existsById(userId);

        assertFalse(result);
        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    void getAddressListByUserIdShouldReturnAddressDtoListWhenAddressExist() {
        Long userId = 5L;

        List<Address> addresses = testBuilder.buildAddressList();
        List<AddressDto> expectedDto = testBuilder.buildAddressDtoList();

        when(addressRepository.findByUserId(userId)).thenReturn(addresses);
        when(addressMapper.toDto(any(Address.class))).thenAnswer(invocation -> {
            Address address = invocation.getArgument(0);
            return expectedDto.stream()
                    .filter(addressDto -> addressDto.getId().equals(address.getId()))
                    .findFirst()
                    .orElseThrow();
        });

        List<AddressDto> addressDtoList = userServiceImpl.getAddressListByUserId(userId);

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

        List<AddressDto> addressDtoList = userServiceImpl.getAddressListByUserId(userId);

        assertTrue(addressDtoList.isEmpty());
        verify(addressRepository, times(1)).findByUserId(userId);
        verify(addressMapper, never()).toDto(any());
    }
}