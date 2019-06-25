package edu.hawaii.its.holiday.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;

import edu.hawaii.its.holiday.util.Dates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/api/holidays/year/{year}")
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByYear(@PathVariable Integer year) {
        logger.debug("Entered REST holidays/year... year: " + year);
        List<Holiday> holidays = holidayService.findHolidaysByYear(year);
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }


    @GetMapping(value = "/api/holidays/year/{year}/month/{month}")
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByMonth(
            @PathVariable Integer year,
            @PathVariable String month) {
        List<Holiday> holidays = holidayService.findHolidaysByMonth(month, year);
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }

    @GetMapping(value = "api/holidays/range", params = { "begin-date", "end-date", "include-start-and-end"})
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByRange (
            @RequestParam("begin-date") String beginDate,
            @RequestParam("end-date") String endDate,
            @RequestParam(value = "include-start-and-end", defaultValue = "true", required = false) Boolean include) {
            List<Holiday> holidays = holidayService.findHolidaysByRange(beginDate, endDate, include);
            JsonData<List<Holiday>> data = new JsonData<>(holidays);
            return ResponseEntity
                    .ok()
                    .body(data);
    }

    @GetMapping(value = "api/holidays/exists", params = {"date"})
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByExists (
            @RequestParam("date") String date) {
            List<Holiday> holidays = holidayService.findHolidaysByRange(date, date, true);
            JsonData<List<Holiday>> data = new JsonData<>(holidays);
            return ResponseEntity
                    .ok()
                    .body(data);
    }

    @GetMapping(value = "api/holidays/closest", params = {"date", "search-forward"})
    public ResponseEntity<JsonData<List<Holiday>>> holidaysByClosest (
            @RequestParam("date") String date,
            @RequestParam(value = "search-forward", defaultValue = "true", required = false) Boolean forward) {
            List<Holiday> holidays = holidayService.findClosestHolidayByDate(date, forward);
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
}
