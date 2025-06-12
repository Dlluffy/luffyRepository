package com.elderlycare.service;

import com.elderlycare.model.User;

public interface UserService {
    String getPasswordByUser(String username);

    User getUserByName(String username);

    boolean registerUser(String username, String password,String role);
}
