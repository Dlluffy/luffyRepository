package com.elderlycare.mapper;

import com.elderlycare.model.VisitAppointment;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface VisitAppointmentMapper {

    @Insert("INSERT INTO visit_appointment (user_id, elder_id, visit_date, visit_time, reason, status, created_at, updated_at) " +
            "VALUES (#{userId}, #{elderId}, #{visitDate}, #{visitTime}, #{reason}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "appointmentId")
    int insert(VisitAppointment visitAppointment);

    @Select("SELECT * FROM visit_appointment WHERE appointment_id = #{id}")
    VisitAppointment selectById(Long id);

    @Select("SELECT * FROM visit_appointment WHERE user_id = #{userId}")
    List<VisitAppointment> selectByUserId(Long userId);

    @Select("SELECT * FROM visit_appointment WHERE elder_id = #{elderId}")
    List<VisitAppointment> selectByElderId(Long elderId);

    @Update("UPDATE visit_appointment SET status = #{status}, updated_at = NOW() WHERE appointment_id = #{appointmentId}")
    int updateStatus(@Param("appointmentId") Long appointmentId, @Param("status") String status);

    @Delete("DELETE FROM visit_appointment WHERE appointment_id = #{id}")
    int deleteById(Long id);
}
