package edu.online.messenger.service;

import edu.online.messenger.model.dto.UserDto;


public interface UserService {

    UserDto getUserById(Long id);
}
