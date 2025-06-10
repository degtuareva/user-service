package messenger.service.impl.impl;

import edu.online.messenger.mapper.AddressMapper;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.entity.Address;
import edu.online.messenger.repository.AddressRepository;
import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.service.impl.impl.UserServiceImpl;
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
        Address address1 = new Address();
        address1.setId(1L);
        Address address2 = new Address();
        address2.setId(2L);
        List<Address> addressList = List.of(address1, address2);
        AddressDto addressDto1 = new AddressDto();
        addressDto1.setId(1L);
        AddressDto addressDto2 = new AddressDto();
        addressDto2.setId(2L);
        when(addressRepository.findByUserId(userId)).thenReturn(addressList);
        when(addressMapper.toDto(address1)).thenReturn(addressDto1);
        when(addressMapper.toDto(address2)).thenReturn(addressDto2);
        List<AddressDto> addressDtoList = userServiceImpl.getAddressListByUserId(userId);
        assertEquals(2, addressDtoList.size());
        assertEquals(addressDto1, addressDtoList.get(0));
        assertEquals(addressDto2, addressDtoList.get(1));
        verify(addressRepository, times(1)).findByUserId(userId);
        verify(addressMapper, times(1)).toDto(address1);
        verify(addressMapper, times(1)).toDto(address2);
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