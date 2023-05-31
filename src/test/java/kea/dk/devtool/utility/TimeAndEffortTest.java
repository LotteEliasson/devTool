package kea.dk.devtool.utility;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TimeAndEffortTest {

    //@Test
    public void testDaysBetween() {
        // Test data
        LocalDate startDate = LocalDate.of(2023, 3,20);
        LocalDate endDate = LocalDate.of(2023, 4, 1);

        //kør test
        int days = TimeAndEffort.daysBetween(startDate, endDate);

        // Assert the result
        assertEquals(12, days, "antal dage imellem burde være 12");

    }


    //@Test
    public void testWorkingDaysBetween() {
        // Test data
        LocalDate startDate = LocalDate.of(2023, 3, 20);
        LocalDate endDate = LocalDate.of(2023, 4, 1);

        //kør test
        int workingDays = TimeAndEffort.workingDaysBetween(startDate, endDate);

        // Assert the result
        assertEquals(10, workingDays, "antal arbejdsdage imellem burde være 10");
    }
}