package ru.spbau.mit.atum;

import org.joda.time.ReadableDateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.spbau.mit.atum.TestUtilities.theNthOfJan;

public class SchedulePlannerTest {
    @Test
    public void planScheduleTest() throws Exception {
        UserPreferences userPreferences = new UserPreferences();
        List<UserDefinedTask> tasks = userPreferences.getTaskList();
        WeekFilter weekFilter1 = new WeekFilter(10 * 60, 90, new WeekMask(1, 2, 4), false);
        WeekFilter weekFilter2 = new WeekFilter(10 * 60, 120, new WeekMask(1, 3, 5), false);
        List<TimeFilter> filters = new ArrayList<>();
        filters.add(weekFilter1);
        filters.add(weekFilter2);

        UserDefinedTask task1 = new UserDefinedTask("first", "", filters, 50);
        UserDefinedTask task2 = new UserDefinedTask("second", "", filters, 60);

        tasks.add(task1);
        tasks.add(task2);

        ReadableDateTime initialMoment = theNthOfJan(1, 0, 0);
        ReadableDateTime finalMoment = theNthOfJan(10, 0, 0);

        SchedulePlanner.planSchedule(userPreferences, initialMoment, finalMoment);
        assertNotNull(task1.getScheduledTime());
        assertNotNull(task2.getScheduledTime());

        assertTrue(task1.getScheduledTime().getDayOfWeek() != 1 && task1.getScheduledTime().getDayOfWeek() != 7);
        assertTrue(task2.getScheduledTime().getDayOfWeek() != 1 && task2.getScheduledTime().getDayOfWeek() != 7);

        assertTrue(task1.getScheduledTime().isAfter(initialMoment));
        assertTrue(task2.getScheduledTime().isAfter(initialMoment));

        assertTrue(task1.getScheduledTime().isBefore(finalMoment));
        assertTrue(task2.getScheduledTime().isBefore(finalMoment));

        assertTrue(task1.getScheduledTime().toDateTime().plusMinutes(task1.getDuration()).
                isBefore(task2.getScheduledTime()) ||
                task2.getScheduledTime().toDateTime().plusMinutes(task2.getDuration()).
                        isBefore(task1.getScheduledTime())
        );
    }
    
}