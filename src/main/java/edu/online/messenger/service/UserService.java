package edu.online.messenger.service;

import org.springframework.stereotype.Service;

public interface UserService {

    boolean existsById(Long id);
}

