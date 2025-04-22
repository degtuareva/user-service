package edu.online.messenger.service;

import edu.online.messenger.model.entity.User;

public interface UserService {

    boolean existsById(Long id);
    boolean existsByLogin(String login);
    User getUserByLogin(String login);
}

