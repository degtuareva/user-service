package edu.online.messenger.service;

import edu.online.messenger.model.entity.User;

public interface UserService {

    public User getUserByLogin(String login);
}

