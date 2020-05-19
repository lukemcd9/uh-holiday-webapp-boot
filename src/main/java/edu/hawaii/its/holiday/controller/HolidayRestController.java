package edu.hawaii.its.holiday.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.hawaii.its.holiday.service.HolidayService;
import edu.hawaii.its.holiday.type.Holiday;
import edu.hawaii.its.holiday.type.Type;
import edu.hawaii.its.holiday.util.Dates;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class HolidayRestController {

    private static final Logger logger = LoggerFactory.getLogger(HolidayRestController.class);
    @Autowired
    private HolidayService holidayService;

    @GetMapping(value = "/api/holidays")
    public ResponseEntity<List<Holiday>> holidays() {
        logger.debug("Entered REST holidays...");
        List<Holiday> holidays = holidayService.findHolidays();
        holidayService.findClosestHolidayByDate(Dates.newLocalDate().toString(), true);
        return ResponseEntity
                .ok()
                .body(holidays);
    }

    @GetMapping(value = "/api/holidays/{id}")
    public ResponseEntity<Holiday> holiday(@PathVariable Integer id) {
        logger.debug("Entered REST holiday(" + id + ") ...");
        Holiday holiday = holidayService.findHoliday(id);
        return ResponseEntity
                .ok()
                .body(holiday);
    }

    @GetMapping(value = "/api/types")
    public ResponseEntity<List<Type>> types() {
        logger.debug("Entered REST types...");
        List<Type> types = holidayService.findTypes();
        return ResponseEntity
                .ok()
                .body(types);
    }

    @GetMapping(value = "/api/holidays/year/{year}")
    public ResponseEntity<List<Holiday>> holidaysByYear(
            @PathVariable Integer year,
            @RequestParam(value = "type", defaultValue = "uh") String type) {
        logger.debug("Entered REST holidays/year... year: " + year);
        List<Holiday> holidays = holidayService.findHolidaysByYear(year);
        holidays = holidayService.findHolidaysByType(holidays, type);
        return ResponseEntity
                .ok()
                .body(holidays);
    }

    @GetMapping(value = "/api/holidays/month/{month}")
    public ResponseEntity<List<Holiday>> holidaysByMonth(
            @PathVariable Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "type", defaultValue = "uh") String type) {
        Integer newYear = year != null ? year : Dates.currentYear();
        List<Holiday> holidays = holidayService.findHolidaysByMonth(month, newYear);
        holidays = holidayService.findHolidaysByType(holidays, type);
        return ResponseEntity
                .ok()
                .body(holidays);
    }

    @GetMapping(value = "api/holidays/range")
    public ResponseEntity<List<Holiday>> holidaysByRange(
            @RequestParam("begin-date") String beginDate,
            @RequestParam("end-date") String endDate,
            @RequestParam(value = "inclusive", defaultValue = "true", required = false) boolean include,
            @RequestParam(value = "type", defaultValue = "uh") String type) {
        List<Holiday> holidays = holidayService.findHolidaysByRange(beginDate, endDate, include);
        holidays = holidayService.findHolidaysByType(holidays, type);
        return ResponseEntity
                .ok()
                .body(holidays);
    }

    @GetMapping(value = "api/holidays/exists")
    public ResponseEntity<List<Holiday>> holidaysByExists(
            @RequestParam("date") String date,
            @RequestParam(value = "type", defaultValue = "uh") String type) {
        List<Holiday> holidays = holidayService.findHolidaysByRange(date, date, true);
        holidays = holidayService.findHolidaysByType(holidays, type);
        return ResponseEntity
                .ok()
                .body(holidays);
    }

    @GetMapping(value = "api/holidays/closest")
    public ResponseEntity<Holiday> holidaysByClosest(
            @RequestParam("date") String date,
            @RequestParam(value = "search-forward", defaultValue = "true", required = false) boolean forward,
            @RequestParam(value = "type", defaultValue = "uh") String type) {
        Holiday holiday = holidayService.findClosestHolidayByDate(date, forward, type);
        return ResponseEntity
                .ok()
                .body(holiday);
    }

    @GetMapping(value = "rest/inYear")
    public ResponseEntity<List<Holiday>> holidaysByYearParam(
            @RequestParam("year") Integer year,
            @RequestParam(name = "type", defaultValue = "uh") String type,
            @RequestParam(name = "isObserved", defaultValue = "false") Boolean isObserved) {
        List<Holiday> holidays = holidayService.findHolidaysByYear(year);
        holidays = holidayService.findHolidaysByType(holidays, type);
        return ResponseEntity
                .ok()
                .body(holidays);
    }

    @RequestMapping(value = "/api/holidaygrid/get",
            params = { "page", "size" },
            method = RequestMethod.GET,
            produces = "application/json")
    public Page<Holiday> findPaginated(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {
        logger.debug("Entered REST holidays grid...");
        return holidayService.findPaginatedHdays(page, size);
    }
}
