package com.elderlycare.service.impl;

import com.elderlycare.mapper.BillingMapper;
import com.elderlycare.model.Billing;
import com.elderlycare.service.BillingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BillingServiceImpl implements BillingService {

    private final BillingMapper billingMapper;

    public BillingServiceImpl(BillingMapper billingMapper) {
        this.billingMapper = billingMapper;
    }

    @Override
    public PageInfo<Billing> listAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Billing> all = billingMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public Billing get(Long billId) {
        return billingMapper.selectByPrimaryKey(billId);
    }

    @Override
    public void save(Billing billing) {
        // 默认 status、paidAt 可在上层（AppointmentService）设置
        billingMapper.insert(billing);
    }

    @Override
    public void update(Billing billing) {
        billingMapper.updateByPrimaryKey(billing);
    }

    @Override
    public void delete(Long billId) {
        billingMapper.deleteByPrimaryKey(billId);
    }

    @Override
    public List<Billing> listByElderId(Long elderId) {
        // 假如 mapper 中已经有 selectByElderId 方法
        return billingMapper.selectByElderId(elderId);
    }
}
