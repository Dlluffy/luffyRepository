package com.elderlycare.service;

import com.elderlycare.model.Elder;

import java.util.Collection;
import java.util.List;

public interface ElderService {
    List<Elder> list();

    Elder get(Long id);

    void save(Elder elder);

    List<Elder> findByIds(Collection<Long> ids);

    void archive(Long id);

    void delete(Long id);

    Elder getElderByIdCard(String idCard);

    void updateElderInfo(String idCard, String name, String contact, String address, String emergencyContact);

}
