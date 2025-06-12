package com.elderlycare.service.impl;

import com.elderlycare.mapper.AppointmentMapper;
import com.elderlycare.model.Appointment;
import com.elderlycare.model.Billing;
import com.elderlycare.service.AppointmentService;
import com.elderlycare.service.BillingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper apptMapper;
    private final BillingService billingService;

    public AppointmentServiceImpl(AppointmentMapper apptMapper,
                                  BillingService billingService) {
        this.apptMapper = apptMapper;
        this.billingService = billingService;
    }

    @Override
    public PageInfo<Appointment> listAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(apptMapper.findAll());
    }

    @Override
    public void save(Appointment appt) {
        appt.setStatus("无需支付");
        appt.setBillId(null);
        apptMapper.insert(appt);
    }

    @Override
    public void cancel(Long appointmentId) {
        apptMapper.updateStatus(appointmentId, "已取消");
    }

    @Override
    public void createBilling(Long appointmentId) {
        // 查预约
        Appointment appt = apptMapper.findById(appointmentId);

        // 生成账单
        Billing bill = new Billing();
        bill.setElderId(appt.getElderId());
        bill.setItemName(appt.getServiceType());
        bill.setBillingMonth(YearMonth.now().toString());
        bill.setAmount(calculateFee(appt.getServiceType()));
        bill.setStatus("未支付");
        billingService.save(bill);

        // 更新 appointment
        apptMapper.updateBillAndStatus(appointmentId, bill.getBillId(), "未支付");
    }

    @Override
    public void createBilling(Long appointmentId, BigDecimal fee) {
        Appointment appt = apptMapper.findById(appointmentId);

        Billing bill = new Billing();
        bill.setElderId(appt.getElderId());
        bill.setItemName(appt.getServiceType());
        bill.setBillingMonth(YearMonth.now().toString());
        bill.setAmount(fee);         // 直接用前端输入
        bill.setStatus("未支付");
        billingService.save(bill);

        apptMapper.updateBillAndStatus(
                appointmentId,
                bill.getBillId(),
                "未支付");
    }

    @Override
    @Transactional
    public void pay(Long appointmentId) {
        // 查预约
        Appointment appt = apptMapper.findById(appointmentId);

        // 生成账单
        Billing bill = new Billing();
        bill.setElderId(appt.getElderId());
        bill.setItemName(appt.getServiceType());
        bill.setBillingMonth(YearMonth.now().toString());
        bill.setAmount(calculateFee(appt.getServiceType()));
        bill.setStatus("已支付");
        billingService.save(bill);

        // 更新 appointment
        apptMapper.updateBillAndStatus(appointmentId, bill.getBillId(), "已支付");
    }

    private BigDecimal calculateFee(String serviceType) {
        return switch (serviceType) {
            case "上门护理" -> BigDecimal.valueOf(150);
            case "康复训练" -> BigDecimal.valueOf(200);
            default -> BigDecimal.valueOf(100);
        };
    }
}
