package com.elderlycare.service.impl;

import com.elderlycare.mapper.CarePlanMapper;
import com.elderlycare.model.CarePlan;
import com.elderlycare.service.CarePlanService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CarePlanServiceImpl implements CarePlanService {
    private final CarePlanMapper mapper;

    public CarePlanServiceImpl(CarePlanMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<CarePlan> listAll() {
        return mapper.selectAll();
    }

    @Override
    public void save(CarePlan cp) {
        cp.setCreatedAt(new Date());  // 用 java.util.Date
        cp.setStatus("启用");
        mapper.insert(cp);
    }

    @Override
    public CarePlan getById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(CarePlan cp) {
        mapper.updateByPrimaryKey(cp);
    }

    @Override
    public void delete(Long id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void archive(Long id) {
        mapper.updateArchived(id, true);
    }

    @Override
    public CarePlan getPlanByElderId(Long elderId) {
        return mapper.getPlanByElderId(elderId);
    }
}
