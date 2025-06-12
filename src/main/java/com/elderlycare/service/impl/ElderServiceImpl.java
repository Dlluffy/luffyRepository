package com.elderlycare.service.impl;

import com.elderlycare.mapper.ElderMapper;
import com.elderlycare.model.Elder;
import com.elderlycare.service.ElderService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class ElderServiceImpl implements ElderService {
    private final ElderMapper elderMapper;

    public ElderServiceImpl(ElderMapper elderMapper) {
        this.elderMapper = elderMapper;
    }

    @Override
    public List<Elder> list() {
        return elderMapper.findAll();
    }

    @Override
    public Elder get(Long id) {
        return elderMapper.findById(id);
    }

    @Override
    public void save(Elder elder) {
        elderMapper.insert(elder);
    }

    @Override
    public List<Elder> findByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return elderMapper.selectByIds(ids);
    }

    @Override
    public void archive(Long id) {
        elderMapper.updateArchived(id, true);
    }

    @Override
    public void delete(Long id) {
        elderMapper.deleteById(id);
    }

    @Override
    public Elder getElderByIdCard(String idCard) {
        return elderMapper.getElderByIdCard(idCard);
    }

    @Override
    public void updateElderInfo(String idCard, String name, String contact, String address, String emergencyContact) {
        elderMapper.updateElderInfo(idCard, name, contact, address, emergencyContact);
    }
}

