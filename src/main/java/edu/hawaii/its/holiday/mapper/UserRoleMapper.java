package edu.hawaii.its.holiday.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import edu.hawaii.its.holiday.type.UserRole;

@Mapper
public interface UserRoleMapper {

    @Select("SELECT * FROM role WHERE id = #{id}")
    UserRole findById(@Param("id") Integer id);

    @Select("SELECT * FROM role")
    List<UserRole> findAll();

}
