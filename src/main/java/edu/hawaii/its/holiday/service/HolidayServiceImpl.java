package edu.hawaii.its.holiday.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hawaii.its.holiday.repository.DesignationRepository;
import edu.hawaii.its.holiday.repository.HolidayRepository;
import edu.hawaii.its.holiday.repository.TypeRepository;
import edu.hawaii.its.holiday.repository.UserRoleRepository;
import edu.hawaii.its.holiday.type.Designation;
import edu.hawaii.its.holiday.type.Holiday;
import edu.hawaii.its.holiday.type.Type;
import edu.hawaii.its.holiday.type.UserRole;
import edu.hawaii.its.holiday.util.Dates;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "holidaysById", key = "#id")
    public Holiday findHoliday(Integer id) { return holidayRepository.findById(id).get(); }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "holidays")
    public List<Holiday> findHolidays() { return holidayRepository.findAllByOrderByObservedDateDesc(); }

    @Override
    @Transactional(readOnly = true)
    public List<UserRole> findUserRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "holidayTypesById", key = "#id")
    public Type findType(Integer id) {
        return typeRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "holidayTypes")
    public List<Type> findTypes() {
        return typeRepository.findAll();
    }

    @Override
    public List<Holiday> findHolidaysByYear(Integer year) {
        LocalDate start = Dates.firstDateOfYear(year);
        LocalDate end = Dates.lastDateOfMonth(Month.DECEMBER, year);
        return holidayRepository.findAllByOfficialDateBetween(start, end);
    }

    @Override
    public List<Holiday> findHolidaysByMonth(String month, Integer year) {
        Month realMonth = convertMonth(month);
        LocalDate start = Dates.firstDateOfMonth(realMonth, year);
        LocalDate end = Dates.lastDateOfMonth(realMonth, year);
        return holidayRepository.findAllByOfficialDateBetween(start, end);
    }

    @Override
    public Month convertMonth(String month) {
        Month retMonth = null;
        switch(month) {
            case "01":
                retMonth = Month.JANUARY;
                break;
            case "02":
                retMonth = Month.FEBRUARY;
                break;
            case "03":
                retMonth = Month.MARCH;
                break;
            case "04":
                retMonth = Month.APRIL;
                break;
            case "05":
                retMonth = Month.MAY;
                break;
            case "06":
                retMonth = Month.JUNE;
                break;
            case "07":
                retMonth = Month.JULY;
                break;
            case "08":
                retMonth = Month.AUGUST;
                break;
            case "09":
                retMonth = Month.SEPTEMBER;
                break;
            case "10":
                retMonth = Month.OCTOBER;
                break;
            case "11":
                retMonth = Month.NOVEMBER;
                break;
            case "12":
                retMonth = Month.DECEMBER;
                break;
        }
        return retMonth;
    }

    @Override
    public Holiday findClosestHoliday() {
        List<Holiday> holidays = holidayRepository.findAllByOrderByObservedDateDesc();
        int size = holidays.size();
        LocalDate curDate = Dates.newLocalDate();
        int closestIndex = 0;
        long daysBetween;
        long min = 9999;
        for (int i = 0; i < size; i++) {
            daysBetween = Dates.compareDates(curDate, holidays.get(i).getObservedDate());
            if (daysBetween > 0) {
                if (daysBetween < min) {
                    min = daysBetween;
                    holidays.get(closestIndex).setClosest(false);
                    holidays.get(i).setClosest(true);
                    closestIndex = i;
                }
            }
        }
        return holidays.get(closestIndex);
    }

    @Override
    public Boolean isClosestHoliday(Holiday h0) {
        Holiday h1 = findClosestHoliday();
        return h0 == h1;
    }

    @Override
    public List<String> findAllDescriptions() {
        return holidayRepository.findAllDistinctDescription();
    }

    @Override
    public List<Designation> findDesignations() {
        return designationRepository.findAll();
    }

    @Override
    public Designation findDesignation(Integer id) {
        return designationRepository.findById(id).get();
    }
}
