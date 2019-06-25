package edu.hawaii.its.holiday.controller;

import java.util.List;
import java.util.stream.Collectors;

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
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }

    @GetMapping(value = "/api/holidays/{id}")
    public ResponseEntity<JsonData<Holiday>> holiday(@PathVariable Integer id) {
        logger.debug("Entered REST holiday(" + id + ") ...");
        Holiday holiday = holidayService.findHoliday(id);
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
        List<Holiday> holidays = holidayService.findHolidaysByYear(year).stream().filter(holiday ->
                holiday.getTypes().stream().anyMatch(types ->
                        types.getDescription().equalsIgnoreCase(type))).collect(Collectors.toList());
        JsonData<List<Holiday>> data = new JsonData<>(holidays);
        return ResponseEntity
                .ok()
                .body(data);
    }

}
