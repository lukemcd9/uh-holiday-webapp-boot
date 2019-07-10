package edu.hawaii.its.holiday.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static edu.hawaii.its.holiday.util.Algorithms.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class AlgorithmsTest {

    @Test
    public void testCalculateGoodFriday() {
        int year = 2019;
        assertThat(officialEasterDay(year).getDayOfWeek(),
                equalTo(DayOfWeek.SUNDAY));
        assertThat(officialEasterDay(year).getDayOfMonth(),
                equalTo(21));
        assertThat(observedGoodFriday(year).getDayOfWeek(),
                equalTo(DayOfWeek.FRIDAY));
        assertThat(observedGoodFriday(year).getDayOfMonth(),
                equalTo(19));
    }

    @Test
    public void testCalculateElectionDay() {
        int year = 2020;
        assertThat(observedElectionDay(year),
                equalTo(Dates.newLocalDate(year, Month.NOVEMBER, 3)));

        try {
            // Not an election year.
            // Note the exception thrown.
            observedElectionDay(2019);
            fail("Should not reach here.");
        } catch (Exception ex) {
            assertThat(ex, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void testConstructorIsPrivate() throws Exception {
        Constructor<Algorithms> constructor = Algorithms.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testStreamFiltering() {
        List<String> testing = new ArrayList<>();
        testing.add("Test");
        testing.add("Apple");
        testing.add("Banana");
        testing.add("Orange");
        testing.add("Apricot");
        testing = testing.stream().filter(list -> list.startsWith("A")).collect(Collectors.toList());
        assertEquals(2, testing.size());
    }

}
