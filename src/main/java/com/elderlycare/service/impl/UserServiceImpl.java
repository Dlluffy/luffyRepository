package com.elderlycare.service.impl;

import com.elderlycare.mapper.UserMapper;
import com.elderlycare.model.User;
import com.elderlycare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public String getPasswordByUser(String username) {
        return userMapper.getPasswordByUser(username);
    }

    @Override
    public User getUserByName(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public boolean registerUser(String username, String password,String role) {
        return userMapper.registerUser(username, password, role);
    }
}
