package com.elderlycare.service;

import com.elderlycare.model.Billing;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BillingService {
    /** 分页查询所有账单 */
    PageInfo<Billing> listAll(int pageNum, int pageSize);

    /** 根据主键查询 */
    Billing get(Long billId);

    /** 新增账单（由预约支付时调用） */
    void save(Billing billing);

    /** 更新整条账单（如标记已支付／设置 paidAt） */
    void update(Billing billing);

    /** 删除账单 */
    void delete(Long billId);

    /** 查询某位老人所有账单 */
    List<Billing> listByElderId(Long elderId);
}
