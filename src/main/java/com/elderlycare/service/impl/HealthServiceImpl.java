package com.elderlycare.service.impl;

import com.elderlycare.mapper.HealthRecordMapper;
import com.elderlycare.model.HealthRecord;
import com.elderlycare.service.HealthService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthServiceImpl implements HealthService {

    private final HealthRecordMapper healthRecordMapper;

    public HealthServiceImpl(HealthRecordMapper healthRecordMapper) {
        this.healthRecordMapper = healthRecordMapper;
    }

    @Override
    public List<HealthRecord> findByElderId(Long elderId) {
        // 对应新增的 mapper 方法
        return healthRecordMapper.selectByElderId(elderId);
    }

    @Override
    public void save(HealthRecord record) {
        healthRecordMapper.insert(record);
    }

    @Override
    public HealthRecord getById(Long id) {
        return healthRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(HealthRecord record) {
        // 调用 updateByPrimaryKey
        healthRecordMapper.updateByPrimaryKey(record);
    }

    @Override
    public void delete(Long id) {
        // 调用 deleteByPrimaryKey
        healthRecordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<HealthRecord> findByElderIdPaged(Long elderId, int limit, int offset) {
        return healthRecordMapper.findByElderIdPaged(elderId, limit, offset);
    }

    @Override
    public int countByElderId(Long elderId) {
        return healthRecordMapper.countByElderId(elderId);
    }
}
