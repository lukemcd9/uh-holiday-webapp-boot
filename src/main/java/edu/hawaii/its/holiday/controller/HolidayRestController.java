package edu.hawaii.its.holiday.controller;

import java.time.Month;
import java.util.Date;
import java.util.List;

import edu.hawaii.its.holiday.util.Dates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.hawaii.its.holiday.service.HolidayService;
import edu.hawaii.its.holiday.type.Holiday;
import edu.hawaii.its.holiday.type.Type;

@RestController
public class HolidayRestController {

    private static final Logger logger = LoggerFactory.getLogger(HolidayRestController.class);

    @Autowired
    private HolidayService holidayService;

    @GetMapping(value = "/api/holidays")
    public ResponseEntity<JsonData<List<Holiday>>> holidays() {
        logger.debug("Entered REST holidays...");
        List<Holiday> holidays = holidayService.findHolidays();
        holidayService.findClosestHoliday();
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }

    @GetMapping(value = "/api/holidays/{id}")
    public ResponseEntity<JsonData<Holiday>> holiday(@PathVariable Integer id) {
        logger.debug("Entered REST holiday(" + id + ") ...");
        Holiday holiday = holidayService.findHoliday(id);
        holidayService.findClosestHoliday();
        JsonData<Holiday> data = new JsonData<>(holiday);
        return ResponseEntity
                .ok()
                .body(data);
    }

    @GetMapping(value = "/api/types")
    public ResponseEntity<JsonData<List<Type>>> types() {
        logger.debug("Entered REST types...");
        List<Type> types = holidayService.findTypes();
        JsonData<List<Type>> data = new JsonData<>(types);
        return ResponseEntity
                .ok()
                .body(data);
    }

    @GetMapping(value = "/api/holidays/year/{year}/")
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByYear(@PathVariable Integer year) {
        logger.debug("Entered REST holidays/year... year: " + year);
        List<Holiday> holidays = holidayService.findHolidaysByYear(year);
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }

    @GetMapping(value = "rest/inYear", params = { "year" })
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByYearParam(
            @RequestParam("year") Integer year,
            @RequestParam(name = "type", defaultValue = "uh") String type,
            @RequestParam(name = "isObserved", defaultValue = "false") Boolean isObserved) {
        List<Holiday> holidays = holidayService.findHolidaysByYear(year);
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }

    @GetMapping(value = "rest/exists", params = {})
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByExistsParam(
            @RequestParam("date") Date date,
            @RequestParam(name = "type", defaultValue = "uh") String type,
            @RequestParam(name = "isObserved", defaultValue = "false") Boolean isObserved) {
        List<Holiday> holidays = holidayService.findHolidays();
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }

    @GetMapping(value = "rest/closest", params = {})
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByClosestParam(
            @RequestParam("date") Date date,
            @RequestParam(name = "type", defaultValue = "uh") String type,
            @RequestParam(name = "isObserved", defaultValue = "false") Boolean isObserved,
            @RequestParam(name = "searchForward", defaultValue = "true") Boolean searchForward) {
        List<Holiday> holidays = holidayService.findHolidays();
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }

    @GetMapping(value = "rest/inMonth", params = { "month" })
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByMonthParam(
            @RequestParam("month") String month,
            @RequestParam(name = "year", defaultValue = "2019") Integer year,
            @RequestParam(name = "type", defaultValue = "uh") String type,
            @RequestParam(name = "isObserved", defaultValue = "false") Boolean isObserved) {
        logger.info("Entered month value: " + month);
        logger.info("Entered year value: " + year);
        List<Holiday> holidays = holidayService.findHolidaysByMonth(month, year);
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }

    @GetMapping(value = "rest/inRange", params = {})
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByRangeParam(
            @RequestParam("beginDate") Date beginDate,
            @RequestParam("endDate") Date endDate,
            @RequestParam(name = "type", defaultValue = "uh") String type,
            @RequestParam(name = "isObserved", defaultValue = "false") Boolean isObserved,
            @RequestParam(name = "includeStartAndEnd", defaultValue = "false") Boolean includeStartAndEnd) {
        List<Holiday> holidays = holidayService.findHolidays();
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }


}
