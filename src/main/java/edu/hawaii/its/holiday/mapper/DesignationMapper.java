package edu.hawaii.its.holiday.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import edu.hawaii.its.holiday.type.Designation;

@Mapper
public interface DesignationMapper {

    @Select("SELECT * FROM designation WHERE id = #{id}")
    Designation findById(@Param("id") Integer id);

    @Select("SELECT * FROM designation")
    List<Designation> findAll();

}
