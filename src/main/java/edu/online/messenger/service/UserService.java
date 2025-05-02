package edu.online.messenger.service;

import edu.online.messenger.model.dto.AddressCreateDto;
import edu.online.messenger.model.dto.UserDto;

public interface UserService {

    boolean existsById(Long id);

    boolean existsByLogin(String login);

    UserDto getUserDtoByLogin(String login);

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    void removeAddressFromUser(AddressCreateDto addressCreateDto);
}

