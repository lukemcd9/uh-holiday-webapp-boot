package edu.hawaii.its.holiday.service;

import java.time.Month;
import java.util.List;

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

    List<Holiday> findHolidaysByMonth(String month, Integer year);

    Holiday findClosestHoliday();

    Boolean isClosestHoliday(Holiday holiday);

    Month convertMonth(String month);

    List<UserRole> findUserRoles();

    Type findType(Integer id);

    List<Type> findTypes();

    List<String> findAllDescriptions();

}
