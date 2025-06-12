package com.elderlycare.mapper;

import com.elderlycare.model.Elder;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;


@Mapper
public interface ElderMapper {
    @Select("SELECT * FROM elder WHERE archived = 0")
    List<Elder> findAll();

    @Update("update elder set archived = #{archived} where elder_id = #{elderId}")
    int updateArchived(@Param("elderId") Long elderId, @Param("archived") Boolean archived);


    @Select("SELECT * FROM elder WHERE elder_id = #{id}")
    Elder findById(Long id);

    @Insert("""
            INSERT INTO elder
              (name, gender, id_card, age, contact, address,
               allergies, medical_history, emergency_contact, institution_id)
            VALUES
              (#{name}, #{gender}, #{idCard}, #{age}, #{contact}, #{address},
               #{allergies}, #{medicalHistory}, #{emergencyContact}, #{institutionId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "elderId")
    void insert(Elder elder);

    /**
     * 根据一组 elderId 批量查询 Elder
     */
    @Select({
            "<script>",
            "  SELECT",
            "    elder_id, name, gender, id_card as idCard, age, contact, address,",
            "    medical_history as medicalHistory, allergies, emergency_contact as emergencyContact,",
            "    institution_id as institutionId",
            "  FROM elder",
            "  WHERE elder_id IN",
            "    <foreach item='id' collection='ids' open='(' separator=',' close=')'>",
            "      #{id}",
            "    </foreach>",
            "</script>"
    })
    List<Elder> selectByIds(@Param("ids") Collection<Long> ids);

    @Delete("DELETE FROM elder WHERE elder_id = #{elderId}")
    int deleteById(@Param("elderId") Long elderId);

    @Select("SELECT * FROM elder WHERE id_card = #{idCard}")
    Elder getElderByIdCard(String idCard);

    @Update("""
            UPDATE elder
            SET name = #{name}, contact = #{contact}, address = #{address},
                emergency_contact = #{emergencyContact}
            WHERE id_card = #{idCard}
            """)
    void updateElderInfo(String idCard, String name, String contact, String address, String emergencyContact);
}
