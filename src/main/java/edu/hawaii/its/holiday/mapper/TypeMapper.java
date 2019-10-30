package edu.hawaii.its.holiday.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import edu.hawaii.its.holiday.type.Type;

@Mapper
public interface TypeMapper {

    @Select("SELECT * FROM type WHERE id = #{id}")
    Type findById(@Param("id") Integer id);

    @Select("SELECT * FROM type")
    List<Type> findAll();
}
