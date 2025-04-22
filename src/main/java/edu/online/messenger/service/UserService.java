package edu.online.messenger.service;

public interface UserService {

    boolean existsById(Long id);
    boolean existsByLogin(String login);
}

