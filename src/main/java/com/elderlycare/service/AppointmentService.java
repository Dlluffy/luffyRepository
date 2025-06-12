package com.elderlycare.service;

import com.elderlycare.model.Appointment;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;

public interface AppointmentService {
    PageInfo<Appointment> listAll(int pageNum, int pageSize);
    void save(Appointment appt);
    void cancel(Long appointmentId);
    void createBilling(Long appointmentId);
    void createBilling(Long appointmentId, BigDecimal fee);
    void pay(Long appointmentId);
}
