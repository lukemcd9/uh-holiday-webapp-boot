package edu.hawaii.its.holiday.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.hawaii.its.holiday.type.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

    Holiday findById(Integer id);

    List<Holiday> findAllByOrderByObservedDateDesc();

    List<Holiday> findAllByOfficialDateBetween(LocalDate start, LocalDate end);

}
