package edu.hawaii.its.holiday.util;

import static edu.hawaii.its.holiday.util.Algorithms.observedElectionDay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.Month;

import org.junit.Test;

public class AlgorithmsTest {

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
}
