package com.elderlycare.service.impl;

import com.elderlycare.mapper.RelativeElderMapper;
import com.elderlycare.mapper.UserMapper;
import com.elderlycare.model.RelativeElder;
import com.elderlycare.service.RelativeElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelativeElderServiceImpl implements RelativeElderService {

    @Autowired
    private RelativeElderMapper relativeElderMapper;

    @Autowired
    UserMapper userMapper;
    @Override
    public boolean linkUserToElder(Long userId, Long elderId, String relation) {
        return relativeElderMapper.insert(userId, elderId, relation);
    }

    @Override
    public RelativeElder getRelativeElderByUserId(Integer userId) {
        return userMapper.getRelativeElderByUserId(userId);
    }
}
