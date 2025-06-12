package com.elderlycare.mapper;

import com.elderlycare.model.HealthRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface HealthRecordMapper {
    // 新增：按 elder_id 查所有
    @Select({
            "select record_id, elder_id, blood_pressure, blood_sugar, record_date, medication",
            "from health_record",
            "where elder_id = #{elderId,jdbcType=BIGINT}",
            "order by record_date desc"
    })
    @Results({
            @Result(column = "record_id", property = "recordId", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "elder_id", property = "elderId", jdbcType = JdbcType.BIGINT),
            @Result(column = "blood_pressure", property = "bloodPressure", jdbcType = JdbcType.VARCHAR),
            @Result(column = "blood_sugar", property = "bloodSugar", jdbcType = JdbcType.DECIMAL),
            @Result(column = "record_date", property = "recordDate", jdbcType = JdbcType.DATE),
            @Result(column = "medication", property = "medication", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<HealthRecord> selectByElderId(Long elderId);

    @Delete("delete from health_record where record_id = #{recordId,jdbcType=BIGINT}")
    int deleteByPrimaryKey(Long recordId);

    @Insert({
            "insert into health_record (elder_id, blood_pressure, blood_sugar, record_date, medication)",
            "values (#{elderId,jdbcType=BIGINT}, #{bloodPressure,jdbcType=VARCHAR},",
            "        #{bloodSugar,jdbcType=DECIMAL}, #{recordDate,jdbcType=DATE},",
            "        #{medication,jdbcType=LONGVARCHAR})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int insert(HealthRecord record);

    @Select("select record_id, elder_id, blood_pressure, blood_sugar, record_date, medication "
            + "from health_record where record_id = #{recordId,jdbcType=BIGINT}")
    @Results({
            @Result(column = "record_id", property = "recordId", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "elder_id", property = "elderId", jdbcType = JdbcType.BIGINT),
            @Result(column = "blood_pressure", property = "bloodPressure", jdbcType = JdbcType.VARCHAR),
            @Result(column = "blood_sugar", property = "bloodSugar", jdbcType = JdbcType.DECIMAL),
            @Result(column = "record_date", property = "recordDate", jdbcType = JdbcType.DATE),
            @Result(column = "medication", property = "medication", jdbcType = JdbcType.LONGVARCHAR)
    })
    HealthRecord selectByPrimaryKey(Long recordId);

    @Select("select record_id, elder_id, blood_pressure, blood_sugar, record_date, medication from health_record")
    @Results({
            @Result(column = "record_id", property = "recordId", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "elder_id", property = "elderId", jdbcType = JdbcType.BIGINT),
            @Result(column = "blood_pressure", property = "bloodPressure", jdbcType = JdbcType.VARCHAR),
            @Result(column = "blood_sugar", property = "bloodSugar", jdbcType = JdbcType.DECIMAL),
            @Result(column = "record_date", property = "recordDate", jdbcType = JdbcType.DATE),
            @Result(column = "medication", property = "medication", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<HealthRecord> selectAll();

    @Update({
            "update health_record",
            "set elder_id      = #{elderId,jdbcType=BIGINT},",
            "    blood_pressure= #{bloodPressure,jdbcType=VARCHAR},",
            "    blood_sugar   = #{bloodSugar,jdbcType=DECIMAL},",
            "    record_date   = #{recordDate,jdbcType=DATE},",
            "    medication    = #{medication,jdbcType=LONGVARCHAR}",
            "where record_id   = #{recordId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(HealthRecord record);


    @Select("select record_id, elder_id, blood_pressure, blood_sugar, record_date, medication "
            + "from health_record where elder_id = #{elderId,jdbcType=BIGINT} order by record_date desc limit 1")
    HealthRecord getNewestRecord(Long elderId);

    @Select("select record_id, elder_id, blood_pressure, blood_sugar, record_date, medication "
            + "from health_record where elder_id = #{elderId,jdbcType=BIGINT} order by record_date desc limit #{limit,jdbcType=INTEGER} offset #{offset,jdbcType=INTEGER}")
    List<HealthRecord> findByElderIdPaged(@Param("elderId") Long elderId,
                                          @Param("limit") int limit,
                                          @Param("offset") int offset);

    @Select("select count(*) from health_record where elder_id = #{elderId,jdbcType=BIGINT}")
    int countByElderId(@Param("elderId") Long elderId);
}
