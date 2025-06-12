package com.elderlycare.mapper;

import com.elderlycare.model.ElderPhoto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ElderPhotoMapper {

    @Select("SELECT * FROM elder_photo WHERE elder_id = #{elderId}")
    List<ElderPhoto> findByElderId(Long elderId);

    int insertPhoto(ElderPhoto photo);

    int deletePhotoById(Long photoId);

    ElderPhoto findById(Long photoId);
}
