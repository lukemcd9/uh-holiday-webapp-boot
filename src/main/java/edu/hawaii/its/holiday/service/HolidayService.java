package edu.hawaii.its.holiday.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import com.sun.org.apache.xpath.internal.operations.Bool;
import edu.hawaii.its.holiday.type.Designation;
import edu.hawaii.its.holiday.type.Holiday;
import edu.hawaii.its.holiday.type.Type;
import edu.hawaii.its.holiday.type.UserRole;

public interface HolidayService {

    List<Designation> findDesignations();

    Designation findDesignation(Integer id);

    Holiday findHoliday(Integer id);

    List<Holiday> findHolidays();

    List<Holiday> findHolidaysByYear(Integer year);

    List<Holiday> findHolidaysByMonth(Integer month, Integer year);

    List<Holiday> findHolidaysByRange(String beginDate, String endDate, Boolean include);

    Holiday findClosestHoliday();

    List<Holiday> findClosestHolidayByDate(String date, Boolean forward);

    List<UserRole> findUserRoles();

    List<Holiday> findHolidaysByType(String type);

    Type findType(Integer id);

    List<Type> findTypes();

    List<String> findAllDescriptions();

}
