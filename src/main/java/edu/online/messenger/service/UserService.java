package edu.online.messenger.service;

import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.dto.UserDto;

import java.util.List;

public interface UserService {

    boolean existsById(Long id);

    boolean existsByLogin(String login);

    UserDto getUserDtoByLogin(String login);

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    List<AddressDto> getAddressesByUserId(Long userId);
}





