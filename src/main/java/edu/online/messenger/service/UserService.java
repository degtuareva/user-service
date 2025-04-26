package edu.online.messenger.service;

import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.entity.User;

public interface UserService {

    UserDto getUserById(Long id);
}
