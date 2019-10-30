package edu.hawaii.its.holiday.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import edu.hawaii.its.holiday.type.Holiday;
import edu.hawaii.its.holiday.type.Type;

@Mapper
public interface HolidayMapper {

    @Select("SELECT * FROM holiday WHERE id = #{id}")
    @Results({
            @Result(property = "types", javaType = List.class, column = "id", many = @Many(select = "findHolidayTypes")),
            @Result(property = "id", column = "id")
    })
    Holiday findById(@Param("id") Integer id);

    @Select("SELECT * FROM holiday h ORDER BY h.observed_date DESC")
    @Results({
            @Result(property = "types", javaType = List.class, column = "id", many = @Many(select = "findHolidayTypes")),
            @Result(property = "id", column = "id")
    })
    List<Holiday> findAllByOrderByObservedDateDesc();

    @Select("SELECT * FROM holiday h WHERE h.official_date BETWEEN #{start, jdbcType=DATE} AND #{end, jdbcType=DATE}")
    @Results({
            @Result(property = "types", javaType = List.class, column = "id", many = @Many(select = "findHolidayTypes"))
    })
    List<Holiday> findAllByOfficialDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Select("SELECT DISTINCT h.description FROM holiday h ORDER BY h.description")
    List<String> findAllDistinctDescription();

    @Select("SELECT * FROM holiday_type INNER JOIN type ON holiday_type.type_id = type.id AND holiday_type.holiday_id = #{holiday_id}")
    List<Type> findHolidayTypes(@Param("holiday_id") Integer id);

}
