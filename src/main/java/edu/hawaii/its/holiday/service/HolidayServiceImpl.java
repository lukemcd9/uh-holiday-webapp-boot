package edu.hawaii.its.holiday.service;

import edu.hawaii.its.holiday.repository.DesignationRepository;
import edu.hawaii.its.holiday.repository.HolidayRepository;
import edu.hawaii.its.holiday.repository.TypeRepository;
import edu.hawaii.its.holiday.repository.UserRoleRepository;
import edu.hawaii.its.holiday.type.Designation;
import edu.hawaii.its.holiday.type.Holiday;
import edu.hawaii.its.holiday.type.Type;
import edu.hawaii.its.holiday.type.UserRole;
import edu.hawaii.its.holiday.util.Algorithms;
import edu.hawaii.its.holiday.util.Dates;
import edu.hawaii.its.holiday.util.HolidayBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @CacheEvict(value = "holidays", allEntries = true)
    public void evictHolidaysCache() {
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
    public List<Holiday> findHolidaysByRange(String beginDate, String endDate, boolean include) {
        LocalDate start = Dates.toLocalDate(beginDate, "yyyy-MM-dd");
        LocalDate end =  Dates.toLocalDate(endDate, "yyyy-MM-dd");
        if (!include) {
            start = Dates.fromOffset(start, 1);
            end = Dates.fromOffset(end, -1);
        }
        return holidayRepository.findAllByOfficialDateBetween(start, end);
    }

    @Override
    public Holiday findClosestHolidayByDate(String date, boolean forward) {
        List<Holiday> holidays = holidayRepository.findAllByOrderByObservedDateDesc();
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
        List<Holiday> holidays = holidayRepository.findAllByOrderByObservedDateDesc();
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
        while(!found) {
            for (int j = 0; j < holidays.get(closestIndex).getHolidayTypes().size(); j++) {
                if (holidays.get(closestIndex).getHolidayTypes().get(j).toLowerCase().equals(type)) {
                    found = true;
                }
            }
            if(!found) {
                closestIndex = forward ? closestIndex-1 : closestIndex+1;
            }
        }
        holidays.get(closestIndex + 1).setClosest(false);
        holidays.get(closestIndex).setClosest(true);
        return holidays.get(closestIndex);
    }

    @Override
    public List<Holiday> generateHolidaysByYear(Integer year) {
        List<Holiday> currentHolidays = new ArrayList<>(findHolidaysByYear(LocalDate.now().getYear()));
        List<Holiday> newHolidays = new ArrayList<>();

        if (findHolidays().stream().anyMatch(holiday -> holiday.getOfficialYear().intValue() == year.intValue())) {
            return currentHolidays;
        }

        if (year % 2 == 0) {
            newHolidays.add(new HolidayBuilder(Algorithms.observedElectionDay(year), Algorithms.observedElectionDay(year))
                    .description("General Election Day")
                    .make());
        }

        Holiday goodFriday = currentHolidays.parallelStream().filter(holiday -> holiday.getDescription().equalsIgnoreCase("Good Friday")).findFirst().get();
        newHolidays.add(new HolidayBuilder(Algorithms.observedGoodFriday(year), Algorithms.observedGoodFriday(year))
                .description(goodFriday.getDescription())
                .types(new ArrayList<>(goodFriday.getTypes()))
                .make());

        currentHolidays.stream().filter(holiday -> !holiday.getDescription().equalsIgnoreCase("Good Friday")).forEach(holiday -> newHolidays.add(new HolidayBuilder(holiday.getOfficialDate().plusYears(year - holiday.getOfficialYear()), Algorithms.observedDayByDescription(holiday.getDescription(), year))
                .description(holiday.getDescription()).types(new ArrayList<>(holiday.getTypes())).make()));
        holidayRepository.saveAll(newHolidays);
        holidayRepository.flush();
        return newHolidays;
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
