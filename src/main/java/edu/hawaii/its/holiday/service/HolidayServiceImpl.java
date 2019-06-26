package edu.hawaii.its.holiday.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

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

import static java.lang.Math.abs;

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
    public List<Holiday> findHolidaysByType(List<Holiday> holidays, String type) {
        return holidays.stream().filter(holiday ->
                holiday.getTypes().stream().anyMatch(types ->
                        types.getDescription().equalsIgnoreCase(type))).collect(Collectors.toList());
    }
    
    @Override
    public List<Holiday> findHolidaysByMonth(Integer month, Integer year) {
        Month realMonth = Month.of(month);
        LocalDate start = Dates.firstDateOfMonth(realMonth, year);
        LocalDate end = Dates.lastDateOfMonth(realMonth, year);
        return holidayRepository.findAllByOfficialDateBetween(start, end);
    }

    @Override
    public List<Holiday> findHolidaysByRange(String beginDate, String endDate, Boolean include) {
        LocalDate start = Dates.toLocalDate(beginDate, "yyyy-MM-dd");
        LocalDate end =  Dates.toLocalDate(endDate, "yyyy-MM-dd");
        if (!include) {
            start = Dates.fromOffset(start, 1);
            end = Dates.fromOffset(end, -1);
        }
        return holidayRepository.findAllByOfficialDateBetween(start, end);
    }

    @Override
    public List<Holiday> findClosestHolidayByDate(String date, Boolean forward) {
        List<Holiday> holidays = holidayRepository.findAllByOrderByObservedDateDesc();
        LocalDate curDate = Dates.toLocalDate(date, "yyyy-MM-dd");
        int closestIndex = 0;
        long daysBetween;
        long min = 9999;
        for (int i = 0; i < holidays.size(); i++) {
            daysBetween = Dates.compareDates(curDate, holidays.get(i).getObservedDate());
            if (!forward) {
                daysBetween = abs(daysBetween);
            }
            if (daysBetween >= 0) {
                if (daysBetween < min) {
                    min = daysBetween;
                    holidays.get(closestIndex).setClosest(false);
                    holidays.get(i).setClosest(true);
                    closestIndex = i;
                }
            }
        }
        LocalDate holiday = holidays.get(closestIndex).getOfficialDate();
        return holidayRepository.findAllByOfficialDateBetween(holiday, holiday);
    }

    @Override
    public Holiday findClosestHoliday() {
        List<Holiday> holidays = holidayRepository.findAllByOrderByObservedDateDesc();
        LocalDate curDate = Dates.newLocalDate();
        int closestIndex = 0;
        long daysBetween;
        long min = 9999;
        for (int i = 0; i < holidays.size(); i++) {
            daysBetween = Dates.compareDates(curDate, holidays.get(i).getObservedDate());
            if (daysBetween >= 0) {
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
