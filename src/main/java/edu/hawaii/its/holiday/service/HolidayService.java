package edu.hawaii.its.holiday.service;

import java.util.List;

import edu.hawaii.its.holiday.type.Holiday;
import edu.hawaii.its.holiday.type.Type;
import edu.hawaii.its.holiday.type.UserRole;

public interface HolidayService {

    public Holiday findHoliday(Integer id);

    public List<Holiday> findHolidays();

    public List<Holiday> findHolidays(Integer year);

    public List<UserRole> findUserRoles();

    public Type findType(Integer id);

    public List<Type> findTypes();

}
