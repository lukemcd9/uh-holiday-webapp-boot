package edu.hawaii.its.holiday.service;

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

    List<UserRole> findUserRoles();

    Type findType(Integer id);

    List<Type> findTypes();

    List<String> findAllDescriptions();

}
