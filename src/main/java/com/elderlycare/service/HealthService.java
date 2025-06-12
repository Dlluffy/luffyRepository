package com.elderlycare.service;

import com.elderlycare.model.HealthRecord;

import java.util.List;

public interface HealthService {
    /**
     * 根据老人 ID 查询所有健康记录（按日期倒序）
     */
    List<HealthRecord> findByElderId(Long elderId);

    /**
     * 保存一条新的健康记录
     */
    void save(HealthRecord record);

    /**
     * （可选）根据记录 ID 查询
     */
    HealthRecord getById(Long id);

    /**
     * （可选）更新一条记录
     */
    void update(HealthRecord record);

    /**
     * （可选）删除一条记录
     */
    void delete(Long id);

    List<HealthRecord> findByElderIdPaged(Long elderId,
                                          int limit,
                                          int offset);

    int countByElderId(Long elderId);

}
