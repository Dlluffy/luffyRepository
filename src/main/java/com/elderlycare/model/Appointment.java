package com.elderlycare.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class Appointment {
    private Long appointmentId;
    private Long elderId;
    private LocalDateTime  appointmentTime;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private Long userId;
    private String reason;
    private String serviceType;
    private Long billId;

    // --- getters / setters ---
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Long getElderId() { return elderId; }
    public void setElderId(Long elderId) { this.elderId = elderId; }

    public LocalDateTime  getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
}
