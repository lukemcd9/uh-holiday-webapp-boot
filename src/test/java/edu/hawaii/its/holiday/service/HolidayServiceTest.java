package edu.hawaii.its.holiday.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hawaii.its.holiday.configuration.SpringBootWebApplication;
import edu.hawaii.its.holiday.type.*;
import edu.hawaii.its.holiday.util.Dates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import static edu.hawaii.its.holiday.util.Algorithms.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpringBootWebApplication.class })
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class HolidayServiceTest {

    @Autowired
    private HolidayService holidayService;

    @Test
    public void findHolidays() {
        List<Holiday> holidays = holidayService.findHolidays();
        int size = holidays.size();
        assertTrue(size > 0);
        int index = size - 1;

        Holiday h0 = holidays.get(index);
        assertNotNull(h0);
        Holiday h1 = holidayService.findHoliday(h0.getId());
        assertEquals(h0.getId(), h1.getId());
        assertEquals(h0, h1);

        // Check that the caching is working.
        Holiday h2 = holidayService.findHolidays().get(index);
        Holiday h3 = holidayService.findHolidays().get(index);
        assertEquals(h2, h3);
        assertSame(h2, h3);

        assertThat(h2.getId(), equalTo(1001));
        assertThat(h2.getDescription(), equalTo("New Year's Day"));

        // Make sure they all have Types
        for (Holiday h : holidays) {
            assertThat(h.toString(), h.getTypes().size(), not(equalTo(0)));
        }
    }

    @Test
    public void findTypeById() {
        Type t0 = holidayService.findType(1);
        Type t1 = holidayService.findType(1);
        assertThat(t0.getId(), equalTo(1));
        assertThat(t1.getId(), equalTo(1));
        assertEquals(t0, t1);
        assertSame(t0, t1); // Check if caching is working.
    }

    @Test
    public void findTypes() {
        List<Type> types = holidayService.findTypes();

        Type ht = types.get(0);
        assertThat(ht.getId(), equalTo(1));
        assertThat(ht.getVersion(), equalTo(1));
        assertThat(ht.getDescription(), equalTo("Bank"));

        ht = types.get(1);
        assertThat(ht.getId(), equalTo(2));
        assertThat(ht.getVersion(), equalTo(1));
        assertThat(ht.getDescription(), equalTo("Federal"));

        ht = types.get(2);
        assertThat(ht.getId(), equalTo(3));
        assertThat(ht.getVersion(), equalTo(1));
        assertThat(ht.getDescription(), equalTo("UH"));

        ht = types.get(3);
        assertThat(ht.getId(), equalTo(4));
        assertThat(ht.getVersion(), equalTo(1));
        assertThat(ht.getDescription(), equalTo("State"));
    }

    @Test
    public void findUserRoles() {
        List<UserRole> userRoles = holidayService.findUserRoles();
        assertTrue(userRoles.size() >= 2);
        assertEquals(1, userRoles.get(0).getId().intValue());
        assertEquals(2, userRoles.get(1).getId().intValue());
        assertEquals("ROLE_ADMIN", userRoles.get(0).getAuthority());
        assertEquals("ROLE_USER", userRoles.get(1).getAuthority());
    }

    @Test
    public void dateFormatting() throws Exception {
        final String DATE_FORMAT = Dates.DATE_FORMAT;

        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone("HST"));

        String toParse = "December 20, 2014, Saturday";
        LocalDate obsDate = Dates.toLocalDate(df.parse(toParse));
        assertNotNull(obsDate);

        LocalDate localDate = Dates.newLocalDate(2014, Month.DECEMBER, 20);
        LocalDate offDate = localDate.plusDays(200);

        Holiday holiday = new Holiday();
        holiday.setObservedDate(obsDate);
        holiday.setOfficialDate(offDate);

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(holiday);
        assertThat(result, containsString(toParse));
    }

    @Test
    public void findHolidayById() {
        Holiday h1 = holidayService.findHoliday(1);

        assertEquals("New Year's Day", h1.getDescription());

        Holiday h2 = holidayService.findHoliday(2);
        assertEquals("Martin Luther King Jr. Day", h2.getDescription());

        Holiday h4 = holidayService.findHoliday(4);
        assertEquals("Prince Kuhio Day", h4.getDescription());

        assertEquals(3, h1.getTypes().size());
        assertEquals(3, h2.getTypes().size());
        assertEquals(2, h4.getTypes().size());

        List<Type> types = h1.getTypes();
        assertThat(types.get(0).getId(), equalTo(1));
        assertThat(types.get(1).getId(), equalTo(2));
        assertThat(types.get(2).getId(), equalTo(3));

        types = h2.getTypes();
        assertThat(types.get(0).getId(), equalTo(1));
        assertThat(types.get(1).getId(), equalTo(2));
        assertThat(types.get(2).getId(), equalTo(3));

        types = h4.getTypes();
        assertThat(types.get(0).getId(), equalTo(3));
        assertThat(types.get(1).getId(), equalTo(4));

        List<String> holidayTypes = h4.getHolidayTypes();
        assertThat(holidayTypes.size(), equalTo(2));
        assertThat(holidayTypes.get(0), equalTo("UH"));
        assertThat(holidayTypes.get(1), equalTo("State"));
    }

    @Test
    public void findHolidaysByYear() {
        assertThat(holidayService.findHolidaysByYear(2005).size(), equalTo(0));
        assertThat(holidayService.findHolidaysByYear(2006).size(), equalTo(14));
        assertThat(holidayService.findHolidaysByYear(2007).size(), equalTo(13));
        assertThat(holidayService.findHolidaysByYear(2008).size(), equalTo(14));
        assertThat(holidayService.findHolidaysByYear(2009).size(), equalTo(13));
        assertThat(holidayService.findHolidaysByYear(2010).size(), equalTo(14));
        assertThat(holidayService.findHolidaysByYear(2011).size(), equalTo(14));
        assertThat(holidayService.findHolidaysByYear(2012).size(), equalTo(15));
        assertThat(holidayService.findHolidaysByYear(2013).size(), equalTo(14));
        assertThat(holidayService.findHolidaysByYear(2014).size(), equalTo(15));
        assertThat(holidayService.findHolidaysByYear(2015).size(), equalTo(14));
        assertThat(holidayService.findHolidaysByYear(2016).size(), equalTo(14));
        assertThat(holidayService.findHolidaysByYear(2017).size(), equalTo(13));
        assertThat(holidayService.findHolidaysByYear(2018).size(), equalTo(14));
        assertThat(holidayService.findHolidaysByYear(2019).size(), equalTo(13));
        assertThat(holidayService.findHolidaysByYear(2020).size(), equalTo(14));
        assertThat(holidayService.findHolidaysByYear(2021).size(), equalTo(13));
        assertThat(holidayService.findHolidaysByYear(2022).size(), equalTo(14));
        assertThat(holidayService.findHolidaysByYear(2023).size(), equalTo(0));
    }

    @Test
    public void findHolidaysByMonth() {
        assertThat(holidayService.findHolidaysByMonth(01,2019).size(), equalTo(2));
        assertThat(holidayService.findHolidaysByMonth(02, 2019).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByMonth(03, 2019).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByMonth(04,2019).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByMonth(05,2019).size(),equalTo(1));
        assertThat(holidayService.findHolidaysByMonth(06,2019).size(),equalTo(1));
        assertThat(holidayService.findHolidaysByMonth(07,2019).size(),equalTo(1));
        assertThat(holidayService.findHolidaysByMonth(8,2019).size(),equalTo(1));
        assertThat(holidayService.findHolidaysByMonth(9,2019).size(),equalTo(1));
        assertThat(holidayService.findHolidaysByMonth(10,2019).size(),equalTo(0));
        assertThat(holidayService.findHolidaysByMonth(11,2019).size(),equalTo(2));
        assertThat(holidayService.findHolidaysByMonth(12,2019).size(),equalTo(1));
    }

    @Test
    public void findHolidaysByRange() {
        assertThat(holidayService.findHolidaysByRange("2019-01-01","2019-01-31", true).size(), equalTo(2));
        assertThat(holidayService.findHolidaysByRange("2019-02-01","2019-02-28", true).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByRange("2019-03-01","2019-03-31", true).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByRange("2019-04-01","2019-04-30", true).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByRange("2019-05-01","2019-05-31", true).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByRange("2019-06-01","2019-06-30", true).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByRange("2019-07-01","2019-07-31", true).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByRange("2019-08-01","2019-08-31", true).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByRange("2019-09-01","2019-09-30", true).size(), equalTo(1));
        assertThat(holidayService.findHolidaysByRange("2019-10-01","2019-10-31", true).size(), equalTo(0));
        assertThat(holidayService.findHolidaysByRange("2019-11-01","2019-11-30", true).size(), equalTo(2));
        assertThat(holidayService.findHolidaysByRange("2019-12-01","2019-12-31", true).size(), equalTo(1));
    }

    @Test
    public void findClosestHolidaysByDate() {
        Holiday holiday = holidayService.findClosestHolidayByDate("2019-01-01", true, "uh");
        assertThat(holiday.getDescription(), equalTo("New Year's Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-01-02", true, "uh");
        assertThat(holiday.getDescription(), equalTo("Martin Luther King Jr. Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-01-02", false, "uh");
        assertThat(holiday.getDescription(), equalTo("New Year's Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-05-27", true, "uh");
        assertThat(holiday.getDescription(), equalTo("Memorial Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-05-28", true, "uh");
        assertThat(holiday.getDescription(), equalTo("King Kamehameha I Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-05-28", false, "uh");
        assertThat(holiday.getDescription(), equalTo("Memorial Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-12-25", true, "uh");
        assertThat(holiday.getDescription(), equalTo("Christmas"));

        holiday = holidayService.findClosestHolidayByDate("2019-12-26", true, "uh");
        assertThat(holiday.getDescription(), equalTo("New Year's Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-12-26", false, "uh");
        assertThat(holiday.getDescription(), equalTo("Christmas"));

        /* Tested on June 26, 2019 (Dates.newLocalDate()) */
        holiday = holidayService.findClosestHolidayByDate(Dates.newLocalDate().toString(), true, "uh");
        assertThat(holiday.getDescription(), equalTo("Independence Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-06-26", true, "state");
        assertThat(holiday.getDescription(), equalTo("Statehood Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-06-26", false, "bank");
        assertThat(holiday.getDescription(), equalTo("Memorial Day"));
    }

    @Test
    public void findClosestHolidaysByDate2() {
        Holiday holiday = holidayService.findClosestHolidayByDate("2019-01-01", true);
        assertThat(holiday.getDescription(), equalTo("New Year's Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-01-02", true);
        assertThat(holiday.getDescription(), equalTo("Martin Luther King Jr. Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-01-02", false);
        assertThat(holiday.getDescription(), equalTo("New Year's Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-05-27", true);
        assertThat(holiday.getDescription(), equalTo("Memorial Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-05-28", true);
        assertThat(holiday.getDescription(), equalTo("King Kamehameha I Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-05-28", false);
        assertThat(holiday.getDescription(), equalTo("Memorial Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-12-25", true);
        assertThat(holiday.getDescription(), equalTo("Christmas"));

        holiday = holidayService.findClosestHolidayByDate("2019-12-26", true);
        assertThat(holiday.getDescription(), equalTo("New Year's Day"));

        holiday = holidayService.findClosestHolidayByDate("2019-12-26", false);
        assertThat(holiday.getDescription(), equalTo("Christmas"));

        /* Tested on June 26, 2019 (Dates.newLocalDate()) */
        holiday = holidayService.findClosestHolidayByDate(Dates.newLocalDate().toString(), true);
        assertThat(holiday.getDescription(), equalTo("Independence Day"));
    }

    @Test
    public void findDesignation() {
        Designation d1a = holidayService.findDesignation(1);
        assertThat(d1a.getName(), equalTo("New Year's Day"));

        Designation d1b = holidayService.findDesignation(d1a.getId());
        assertThat(d1b.getName(), equalTo("New Year's Day"));

        assertThat(d1a, equalTo(d1b));
        assertThat(d1a.hashCode(), equalTo(d1b.hashCode()));
    }

    @Test
    public void findDesignations() {
        assertThat(holidayService.findDesignations().size(), equalTo(15));
    }

    @Test
    public void holidayAdjustment() {
        LocalDate ld0 = Dates.firstDateOfYear(2011);
        LocalDate ld1 = ld0.with(new HolidayAdjuster());
        assertThat(ld0, not(equalTo(ld1)));
        assertThat(ld0.getDayOfWeek(), equalTo(DayOfWeek.SATURDAY));
        assertThat(ld1.getDayOfWeek(), equalTo(DayOfWeek.FRIDAY));

        LocalDate ld2 = Dates.newLocalDate(2011, Month.DECEMBER, 25);
        LocalDate ld3 = ld2.with(new HolidayAdjuster());
        assertThat(ld2, not(equalTo(ld3)));
        assertThat(ld2.getDayOfWeek(), equalTo(DayOfWeek.SUNDAY));
        assertThat(ld3.getDayOfWeek(), equalTo(DayOfWeek.MONDAY));

        // 2010-12-31
        LocalDate ld4 = Dates.newLocalDate(2010, Month.DECEMBER, 31);
        LocalDate ld5 = ld4.with(new HolidayAdjuster());
        assertThat(ld4, equalTo(ld5));
        assertThat(ld4.getDayOfWeek(), equalTo(DayOfWeek.FRIDAY));
        assertThat(ld5.getDayOfWeek(), equalTo(DayOfWeek.FRIDAY));
    }

    @Test
    public void testNames() {
        List<Designation> designations = holidayService.findDesignations();
        Set<String> names = new HashSet<>();
        for (Designation d : designations) {
            names.add(d.getName());
        }

        List<Holiday> holidays = holidayService.findHolidays();
        for (Holiday h : holidays) {
            assertTrue(h.toString(), names.contains(h.getDescription()));
        }
    }

    @Test
    public void observeredDayOfWeek() {
        LocalDate date;
        int[] counts = new int[15];

        List<Holiday> holidays = holidayService.findHolidays();
        for (Holiday h : holidays) {
            int year = h.getOfficialDate().getYear();
            switch (h.getDescription()) {
                case "New Year's Day":
                    date = observedNewYearsDay(h.getOfficialYear());
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[0] += 1;
                    break;

                case "Martin Luther King Jr. Day":
                    date = observedMartinLutherKingJrDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[1] += 1;
                    break;

                case "Presidents' Day":
                    date = observedPresidentsDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[2] += 1;
                    break;

                case "Prince Kuhio Day":
                    date = observedPrinceKuhioDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[3] += 1;
                    break;

                case "Good Friday":
                    // TBD
                    counts[4] += 1;
                    break;

                case "Memorial Day":
                    date = observedMemorialDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[5] += 1;
                    break;

                case "King Kamehameha I Day":
                    date = observedKingKamehamehaDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[6] += 1;
                    break;

                case "Independence Day":
                    date = observedIndependenceDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[7] += 1;
                    break;

                case "Statehood Day":
                    date = observedStatehoodDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[8] += 1;
                    break;

                case "Labor Day":
                    date = observedLaborDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[9] += 1;
                    break;

                case "Discoverers' Day":
                    date = observedDiscoverersDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[10] += 1;
                    break;

                case "General Election Day":
                    date = observedElectionDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[11] += 1;
                    break;

                case "Veterans' Day":
                    date = observedVeteransDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[12] += 1;
                    break;

                case "Thanksgiving":
                    date = observedThanksgivingDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[13] += 1;
                    break;

                case "Christmas":
                    date = observedChristmasDay(year);
                    assertThat(date, equalTo(h.getObservedDate()));
                    counts[14] += 1;
                    break;

                default:
                    fail("Should not get here. h: " + h);
                    break;
            }
        }

        assertThat(counts[0], greaterThanOrEqualTo(17));
        assertThat(counts[1], greaterThanOrEqualTo(17));
        assertThat(counts[2], greaterThanOrEqualTo(17));
        assertThat(counts[3], greaterThanOrEqualTo(17));
        assertThat(counts[4], greaterThanOrEqualTo(17));
        assertThat(counts[5], greaterThanOrEqualTo(17));
        assertThat(counts[6], greaterThanOrEqualTo(17));
        assertThat(counts[7], greaterThanOrEqualTo(17));
        assertThat(counts[8], greaterThanOrEqualTo(17));
        assertThat(counts[9], greaterThanOrEqualTo(17));
        assertThat(counts[10], greaterThanOrEqualTo(5)); // Discoverers' Day
        assertThat(counts[11], greaterThanOrEqualTo(9)); // General Election Day
        assertThat(counts[12], greaterThanOrEqualTo(17));
        assertThat(counts[13], greaterThanOrEqualTo(17));
        assertThat(counts[14], greaterThanOrEqualTo(17));
    }

    @Test
    public void findAllDescriptions() {
        List<String> descriptions = holidayService.findAllDescriptions();

        assertThat(descriptions.get(0), equalTo("Christmas"));
        assertThat(descriptions.get(1), equalTo("Discoverers' Day")); // Not a State holiday!
        assertThat(descriptions.get(2), equalTo("General Election Day"));
        assertThat(descriptions.get(3), equalTo("Good Friday"));
        assertThat(descriptions.get(4), equalTo("Independence Day"));
        assertThat(descriptions.get(5), equalTo("King Kamehameha I Day"));
        assertThat(descriptions.get(6), equalTo("Labor Day"));
        assertThat(descriptions.get(7), equalTo("Martin Luther King Jr. Day"));
        assertThat(descriptions.get(8), equalTo("Memorial Day"));
        assertThat(descriptions.get(9), equalTo("New Year's Day"));
        assertThat(descriptions.get(10), equalTo("Presidents' Day"));
        assertThat(descriptions.get(11), equalTo("Prince Kuhio Day"));
        assertThat(descriptions.get(12), equalTo("Statehood Day"));
        assertThat(descriptions.get(13), equalTo("Thanksgiving"));
        assertThat(descriptions.get(14), equalTo("Veterans' Day"));

        assertThat(descriptions.size(), equalTo(15));
    }

    @Test
    public void testOccurence() {
        int year = 2019;
        LocalDate ld = occurence(year, Month.NOVEMBER, DayOfWeek.THURSDAY, 4);
        assertThat(ld, equalTo(Dates.newLocalDate(year, Month.NOVEMBER, 28)));

        ld = occurence(year, Month.APRIL, DayOfWeek.MONDAY, 1);
        assertThat(ld, equalTo(Dates.newLocalDate(year, Month.APRIL, 1)));

        ld = occurence(year, Month.APRIL, DayOfWeek.TUESDAY, 1);
        assertThat(ld, equalTo(Dates.newLocalDate(year, Month.APRIL, 2)));

        ld = occurence(year, Month.APRIL, DayOfWeek.SUNDAY, 1);
        assertThat(ld, equalTo(Dates.newLocalDate(year, Month.APRIL, 7)));

        ld = occurence(year, Month.APRIL, DayOfWeek.MONDAY, 5);
        assertThat(ld, equalTo(Dates.newLocalDate(year, Month.APRIL, 29)));

        // Note:
        ld = occurence(year, Month.APRIL, DayOfWeek.MONDAY, 6);
        assertThat(ld, equalTo(Dates.newLocalDate(year, Month.MAY, 6)));

        // Note these weird cases, too:
        ld = occurence(year, Month.APRIL, DayOfWeek.MONDAY, 0);
        assertThat(ld, equalTo(Dates.newLocalDate(year, Month.APRIL, 1)));
        ld = occurence(year, Month.APRIL, DayOfWeek.MONDAY, -1);
        assertThat(ld, equalTo(Dates.newLocalDate(year, Month.APRIL, 1)));
    }

    @Test
    public void testGenerateHolidays() {
        int year = 2019;
        List<Holiday> generatedHolidays = holidayService.generateHolidaysByYear(year);

        assertEquals(generatedHolidays.get(0).getOfficialYear().intValue(), 2019);

        Integer otherYear = 2023;
        assertNotEquals(generatedHolidays.get(0).getOfficialYear(), otherYear);
    }
}
