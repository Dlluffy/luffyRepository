package com.elderlycare.service.impl;

import com.elderlycare.mapper.CareTaskRecordMapper;
import com.elderlycare.model.CareTaskRecord;
import com.elderlycare.service.CareTaskService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CareTaskServiceImpl implements CareTaskService {
    private final CareTaskRecordMapper mapper;
    public CareTaskServiceImpl(CareTaskRecordMapper mapper) { this.mapper = mapper; }

    @Override
    public List<CareTaskRecord> listByPlan(Long planId) {
        return mapper.selectByPlanId(planId);
    }

    @Override
    public void markDone(CareTaskRecord rec) {
        rec.setExecutedAt(new Date());  // 用 java.util.Date
        mapper.insert(rec);
    }
}
