package edu.hawaii.its.holiday.util;

import edu.hawaii.its.holiday.type.Holiday;
import edu.hawaii.its.holiday.type.Type;

import java.time.LocalDate;
import java.util.List;

public class HolidayBuilder {

    private Holiday holiday;
    private HolidayBuilder builder;

    public HolidayBuilder(LocalDate officialDate, LocalDate observedDate) {
        holiday = new Holiday(officialDate, observedDate);
        builder = this;
        holiday.setVersion(0);
    }

    public HolidayBuilder description(String description) {
        holiday.setDescription(description);
        return builder;
    }

    public HolidayBuilder types(List<Type> types) {
        holiday.setTypes(types);
        return builder;
    }

    public Holiday make() {
        return holiday;
    }
}
