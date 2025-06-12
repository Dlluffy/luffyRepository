package com.elderlycare.mapper;

import com.elderlycare.model.Appointment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AppointmentMapper {

    @Select("SELECT appointment_id AS appointmentId, elder_id AS elderId,"
            + " appointment_time AS appointmentTime, status, created_at AS createdAt,"
            + " updated_at AS updatedAt, user_id AS userId, reason, service_type AS serviceType,"
            + " bill_id AS billId"
            + " FROM appointment ORDER BY created_at DESC")
    List<Appointment> findAll();

    @Select("SELECT * FROM appointment WHERE appointment_id = #{id}")
    Appointment findById(Long id);

    @Insert("""
            INSERT INTO appointment
              (elder_id, appointment_time, status, user_id, reason, service_type, bill_id)
            VALUES
              (#{elderId}, #{appointmentTime}, #{status}, #{userId}, #{reason}, #{serviceType}, #{billId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "appointmentId")
    void insert(Appointment appt);

    @Update("UPDATE appointment SET status = #{status}, updated_at = NOW() WHERE appointment_id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    @Update("""
              UPDATE appointment
                 SET bill_id = #{billId},
                     status  = #{status},
                     updated_at = NOW()
               WHERE appointment_id = #{id}
            """)
    void updateBillAndStatus(@Param("id") Long id, @Param("billId") Long billId, @Param("status") String status);

    @Select("SELECT * FROM appointment WHERE elder_id = #{elderId}")
    List<Appointment> selectByElderId(Long elderId);

    // 如果需要更新其他字段，可再加一个 update 方法
}
