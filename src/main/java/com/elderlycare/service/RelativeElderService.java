package com.elderlycare.service;

import com.elderlycare.model.RelativeElder;

public interface RelativeElderService {
    boolean linkUserToElder(Long userId, Long elderId, String relation);

    RelativeElder getRelativeElderByUserId(Integer userId);
}
