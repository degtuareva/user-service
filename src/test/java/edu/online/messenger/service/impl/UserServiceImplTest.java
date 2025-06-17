package edu.online.messenger.service.impl;

import edu.online.messenger.mapper.AddressMapper;
import edu.online.messenger.model.dto.AddressDto;
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
    private UserServiceImpl userService;

    @Test
    void existsByIdShouldReturnTrueWhenUserExists() {
        User user = UserTestBuilder.builder().withId(15L).build().buildUser();

        when(userRepository.existsById(user.getId())).thenReturn(true);

        boolean result = userService.existsById(user.getId());

        assertTrue(result);
        verify(userRepository, times(1)).existsById(user.getId());
    }

    @Test
    void existsByIdShouldReturnFalseWhenUserDoesNotExist() {
        Long userId = 500L;

        when(userRepository.existsById(userId)).thenReturn(false);

        boolean result = userService.existsById(userId);

        assertFalse(result);
        verify(userRepository, times(1)).existsById(userId);
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
}