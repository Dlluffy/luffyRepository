package com.elderlycare.mapper;

import com.elderlycare.model.RelativeElder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface RelativeElderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relative_elder
     *
     * @mbg.generated Wed May 07 09:58:34 HKT 2025
     */
    @Delete({
        "delete from relative_elder",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relative_elder
     *
     * @mbg.generated Wed May 07 09:58:34 HKT 2025
     */
    @Insert({
        "insert into relative_elder (user_id, ",
        "elder_id, relation)",
        "values (#{userId,jdbcType=BIGINT}, ",
        "#{elderId,jdbcType=BIGINT}, #{relation,jdbcType=VARCHAR})"
    })
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    Boolean insert(Long userId, Long elderId, String relation);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relative_elder
     *
     * @mbg.generated Wed May 07 09:58:34 HKT 2025
     */
    @Select({
        "select",
        "id, user_id, elder_id, relation",
        "from relative_elder",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="elder_id", property="elderId", jdbcType=JdbcType.BIGINT),
        @Result(column="relation", property="relation", jdbcType=JdbcType.VARCHAR)
    })
    RelativeElder selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relative_elder
     *
     * @mbg.generated Wed May 07 09:58:34 HKT 2025
     */
    @Select({
        "select",
        "id, user_id, elder_id, relation",
        "from relative_elder"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="elder_id", property="elderId", jdbcType=JdbcType.BIGINT),
        @Result(column="relation", property="relation", jdbcType=JdbcType.VARCHAR)
    })
    List<RelativeElder> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relative_elder
     *
     * @mbg.generated Wed May 07 09:58:34 HKT 2025
     */
    @Update({
        "update relative_elder",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "elder_id = #{elderId,jdbcType=BIGINT},",
          "relation = #{relation,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(RelativeElder record);

}
