package edu.hawaii.its.holiday.util;

import edu.hawaii.its.holiday.type.HolidayAdjuster;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public final class Algorithms {

    // Private constructor; prevent instantiation.
    private Algorithms() {
        // Empty.
    }

    public static LocalDate observedNewYearsDay(int year) {
        LocalDate localDate = Dates.firstDateOfYear(year);
        return observedDay(localDate);
    }

    public static LocalDate observedMartinLutherKingJrDay(int year) {
        LocalDate localDate = mondayOccurence(year, Month.JANUARY, 3);
        return observedDay(localDate);
    }

    public static LocalDate observedPresidentsDay(int year) {
        LocalDate localDate = mondayOccurence(year, Month.FEBRUARY, 3);
        return observedDay(localDate);
    }

    public static LocalDate observedPrinceKuhioDay(int year) {
        LocalDate localDate = Dates.newLocalDate(year, Month.MARCH, 26);
        return observedDay(localDate);
    }

    // TODO here: Add Good Friday Holiday.
    public static LocalDate observedGoodFriday(int year) {
        LocalDate localDate = officialEasterDay(year).minusDays(2);
        return observedDay(localDate);
    }

    public static LocalDate officialEasterDay(int year) {
        //https://stackoverflow.com/questions/26022233/calculate-the-date-of-easter-sunday
        int a = year % 19,
                b = year / 100,
                c = year % 100,
                d = b / 4,
                e = b % 4,
                g = (8 * b + 13) / 25,
                h = (19 * a + b - d - g + 15) % 30,
                j = c / 4,
                k = c % 4,
                m = (a + 11 * h) / 319,
                r = (2 * e + 2 * j - k - h + m + 32) % 7,
                month = (h - m + r + 90) / 25,
                day = (h - m + r + month + 19) % 32;

        LocalDate localDate = Dates.newLocalDate(year, Month.of(month), day);
        return localDate;
    }


    public static LocalDate observedMemorialDay(int year) {
        LocalDate localDate = Dates.lastDateOfMonth(Month.MAY, year);
        while (localDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            localDate = localDate.minusDays(1);
        }
        return localDate;
    }

    public static LocalDate observedKingKamehamehaDay(int year) {
        LocalDate localDate = Dates.newLocalDate(year, Month.JUNE, 11);
        return observedDay(localDate);
    }

    public static LocalDate observedIndependenceDay(int year) {
        LocalDate localDate = Dates.newLocalDate(year, Month.JULY, 4);
        return observedDay(localDate);
    }

    public static LocalDate observedStatehoodDay(int year) {
        LocalDate localDate = occurence(year, Month.AUGUST, DayOfWeek.FRIDAY, 3);
        return observedDay(localDate);
    }

    public static LocalDate observedLaborDay(int year) {
        LocalDate localDate = mondayOccurence(year, Month.SEPTEMBER, 1);
        return observedDay(localDate);
    }

    public static LocalDate observedDiscoverersDay(int year) {
        LocalDate localDate = mondayOccurence(year, Month.OCTOBER, 2);
        return observedDay(localDate);
    }

    public static LocalDate observedElectionDay(int year) {
        if (year % 2 != 0) {
            throw new IllegalArgumentException("Year must be even.");
        }

        LocalDate localDate = Dates.firstDateOfMonth(Month.NOVEMBER, year);
        boolean firstMondayFound = false;
        while (!firstMondayFound) {
            if (localDate.getDayOfWeek() == DayOfWeek.MONDAY) {
                firstMondayFound = true;
            }
            localDate = localDate.plusDays(1);
        }

        return observedDay(localDate);
    }

    public static LocalDate observedVeteransDay(int year) {
        LocalDate localDate = Dates.newLocalDate(year, Month.NOVEMBER, 11);
        return observedDay(localDate);
    }

    public static LocalDate observedThanksgivingDay(int year) {
        LocalDate localDate = occurence(year, Month.NOVEMBER, DayOfWeek.THURSDAY, 4);
        return observedDay(localDate);
    }

    public static LocalDate observedChristmasDay(int year) {
        LocalDate localDate = Dates.newLocalDate(year, Month.DECEMBER, 25);
        return observedDay(localDate);
    }

    private static LocalDate observedDay(LocalDate localDate) {
        return localDate.with(new HolidayAdjuster());
    }

    private static LocalDate mondayOccurence(int year, Month month, int occurence) {
        return occurence(year, month, DayOfWeek.MONDAY, occurence);
    }

    public static LocalDate occurence(int year, Month month, DayOfWeek dayOfWeek, int occurence) {
        int counter = 0;
        LocalDate localDate = Dates.firstOfMonth(month, year);
        while (counter < occurence) {
            if (localDate.getDayOfWeek() == dayOfWeek) {
                counter++;
                if (counter == occurence) {
                    break; // <-- Note. 
                }
            }
            localDate = localDate.plusDays(1);
        }
        return localDate;
    }

}
