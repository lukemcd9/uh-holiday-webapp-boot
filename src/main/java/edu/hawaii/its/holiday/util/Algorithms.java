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

    public static LocalDate newYearsDay(int year, boolean observed) {
        LocalDate localDate = Dates.firstDateOfYear(year);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate martinLutherKingJrDay(int year, boolean observed) {
        LocalDate localDate = mondayOccurence(year, Month.JANUARY, 3);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate presidentsDay(int year, boolean observed) {
        LocalDate localDate = mondayOccurence(year, Month.FEBRUARY, 3);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate princeKuhioDay(int year, boolean observed) {
        LocalDate localDate = Dates.newLocalDate(year, Month.MARCH, 26);
        return observed ? observedDay(localDate) : localDate;
    }

    // TODO here: Add Good Friday Holiday.
    public static LocalDate goodFriday(int year, boolean observed) {
        LocalDate localDate = easterDay(year).minusDays(2);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate easterDay(int year) {
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


    public static LocalDate memorialDay(int year, boolean observed) {
        LocalDate localDate = Dates.lastDateOfMonth(Month.MAY, year);
        while (localDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            localDate = localDate.minusDays(1);
        }
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate kingKamehamehaDay(int year, boolean observed) {
        LocalDate localDate = Dates.newLocalDate(year, Month.JUNE, 11);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate independenceDay(int year, boolean observed) {
        LocalDate localDate = Dates.newLocalDate(year, Month.JULY, 4);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate statehoodDay(int year, boolean observed) {
        LocalDate localDate = occurence(year, Month.AUGUST, DayOfWeek.FRIDAY, 3);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate laborDay(int year, boolean observed) {
        LocalDate localDate = mondayOccurence(year, Month.SEPTEMBER, 1);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate discoverersDay(int year, boolean observed) {
        LocalDate localDate = mondayOccurence(year, Month.OCTOBER, 2);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate electionDay(int year, boolean observed) {
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

        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate veteransDay(int year, boolean observed) {
        LocalDate localDate = Dates.newLocalDate(year, Month.NOVEMBER, 11);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate thanksgivingDay(int year, boolean observed) {
        LocalDate localDate = occurence(year, Month.NOVEMBER, DayOfWeek.THURSDAY, 4);
        return observed ? observedDay(localDate) : localDate;
    }

    public static LocalDate christmasDay(int year, boolean observed) {
        LocalDate localDate = Dates.newLocalDate(year, Month.DECEMBER, 25);
        return observed ? observedDay(localDate) : localDate;
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

    public static LocalDate dateByDescription(String description, int year, boolean observed) {
        LocalDate localDate;
        switch (description) {
            case "New Year's Day":
                localDate = newYearsDay(year, observed);
                break;
            case "Martin Luther King Jr. Day":
                localDate = martinLutherKingJrDay(year, observed);
                break;
            case "Presidents' Day":
                localDate = presidentsDay(year, observed);
                break;
            case "Prince Kuhio Day":
                localDate = princeKuhioDay(year, observed);
                break;
            case "Good Friday":
                localDate = goodFriday(year, observed);
                break;
            case "Memorial Day":
                localDate = memorialDay(year, observed);
                break;
            case "King Kamehameha I Day":
                localDate = kingKamehamehaDay(year, observed);
                break;
            case "Independence Day":
                localDate = independenceDay(year, observed);
                break;
            case "Statehood Day":
                localDate = statehoodDay(year, observed);
                break;
            case "Labor Day":
                localDate = laborDay(year, observed);
                break;
            case "Veterans' Day":
                localDate = veteransDay(year, observed);
                break;
            case "Thanksgiving":
                localDate = thanksgivingDay(year, observed);
                break;
            case "Christmas":
                localDate = christmasDay(year, observed);
                break;
            case "General Election Day":
                localDate = electionDay(year, observed);
                break;
            default:
                localDate = null;
                break;
        }
        return localDate;
    }

}
