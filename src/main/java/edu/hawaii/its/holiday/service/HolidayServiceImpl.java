package edu.hawaii.its.holiday.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hawaii.its.holiday.mapper.DesignationMapper;
import edu.hawaii.its.holiday.mapper.HolidayMapper;
import edu.hawaii.its.holiday.mapper.TypeMapper;
import edu.hawaii.its.holiday.mapper.UserRoleMapper;
import edu.hawaii.its.holiday.type.Designation;
import edu.hawaii.its.holiday.type.Holiday;
import edu.hawaii.its.holiday.type.Type;
import edu.hawaii.its.holiday.type.UserRole;
import edu.hawaii.its.holiday.util.Dates;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolidayMapper holidayMapper;

    @Autowired
    private DesignationMapper designationMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private UserRoleMapper userRoleRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "holidaysById", key = "#id")
    public Holiday findHoliday(Integer id) {
        return holidayMapper.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "holidays")
    public List<Holiday> findHolidays() {
        return holidayMapper.findAllByOrderByObservedDateDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRole> findUserRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "holidayTypesById", key = "#id")
    public Type findType(Integer id) {
        return typeMapper.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "holidayTypes")
    public List<Type> findTypes() {
        return typeMapper.findAll();
    }

    @Override
    public List<Holiday> findHolidaysByYear(Integer year) {
        LocalDate start = Dates.firstDateOfYear(year);
        LocalDate end = Dates.lastDateOfMonth(Month.DECEMBER, year);
        return holidayMapper.findAllByOfficialDateBetween(start, end);
    }

    @Override
    public List<Holiday> findHolidaysByType(List<Holiday> holidays, String type) {
        return holidays.stream().filter(holiday -> holiday.getTypes().stream()
                .anyMatch(types -> types.getDescription().equalsIgnoreCase(type)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Holiday> findHolidaysByMonth(Integer month, Integer year) {
        Month realMonth = Month.of(month);
        LocalDate start = Dates.firstDateOfMonth(realMonth, year);
        LocalDate end = Dates.lastDateOfMonth(realMonth, year);
        return holidayMapper.findAllByOfficialDateBetween(start, end);
    }

    @Override
    public List<Holiday> findHolidaysByRange(String beginDate, String endDate, boolean include) {
        LocalDate start = Dates.toLocalDate(beginDate, "yyyy-MM-dd");
        LocalDate end = Dates.toLocalDate(endDate, "yyyy-MM-dd");
        if (!include) {
            start = Dates.fromOffset(start, 1);
            end = Dates.fromOffset(end, -1);
        }
        return holidayMapper.findAllByOfficialDateBetween(start, end);
    }

    @Override
    public Holiday findClosestHolidayByDate(String date, boolean forward) {
        List<Holiday> holidays = holidayMapper.findAllByOrderByObservedDateDesc();
        LocalDate curDate = Dates.toLocalDate(date, "yyyy-MM-dd");

        int closestIndex;
        long daysBetween;
        int i = 0;

        do {
            daysBetween = Dates.compareDates(curDate, holidays.get(i).getObservedDate());
            i++;
        } while (daysBetween > -1);

        closestIndex = forward ? i - 2 : i - 1;

        holidays.get(closestIndex + 1).setClosest(false);
        holidays.get(closestIndex).setClosest(true);

        return holidays.get(closestIndex);
    }

    @Override
    public Holiday findClosestHolidayByDate(String date, boolean forward, String type) {
        List<Holiday> holidays = holidayMapper.findAllByOrderByObservedDateDesc();
        LocalDate curDate = Dates.toLocalDate(date, "yyyy-MM-dd");

        int closestIndex;
        long daysBetween;
        int i = 0;
        boolean found = false;

        do {
            daysBetween = Dates.compareDates(curDate, holidays.get(i).getObservedDate());
            i++;
        } while (daysBetween > -1);

        closestIndex = forward ? i - 2 : i - 1;

        while (!found) {
            for (int j = 0; j < holidays.get(closestIndex).getHolidayTypes().size(); j++) {
                if (holidays.get(closestIndex).getHolidayTypes().get(j).equalsIgnoreCase(type)) {
                    found = true;
                }
            }

            if (!found) {
                closestIndex = forward ? closestIndex - 1 : closestIndex + 1;
            }
        }

        holidays.get(closestIndex + 1).setClosest(false);
        holidays.get(closestIndex).setClosest(true);

        return holidays.get(closestIndex);
    }

    @Caching(evict = {
            @CacheEvict(value = "holidays", allEntries = true),
            @CacheEvict(value = "holidaysById", allEntries = true) })
    public void evictHolidaysCache() {
        // Empty.
    }

    @Override
    public List<String> findAllDescriptions() {
        return holidayMapper.findAllDistinctDescription();
    }

    @Override
    public List<Designation> findDesignations() {
        return designationMapper.findAll();
    }

    @Override
    public Designation findDesignation(Integer id) {
        return designationMapper.findById(id);
    }

    @Override
    public List<Type> findHolidayTypes(Holiday holiday) {
        return holidayMapper.findHolidayTypes(holiday.getId());
    }
}
