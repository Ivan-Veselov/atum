package ru.spbau.mit.atum;

import org.joda.time.ReadableDateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ru.spbau.mit.atum.model.TimeFilter;
import ru.spbau.mit.atum.model.UserData;
import ru.spbau.mit.atum.model.UserDefinedTask;
import ru.spbau.mit.atum.model.WeekFilter;
import ru.spbau.mit.atum.planner.SchedulePlanner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.spbau.mit.atum.TestUtilities.theNthOfJan;
import static ru.spbau.mit.atum.model.TimeFilter.ExclusionType.COMMON;
import static ru.spbau.mit.atum.model.WeekFilter.DaysOfWeek.FRIDAY;
import static ru.spbau.mit.atum.model.WeekFilter.DaysOfWeek.SATURDAY;
import static ru.spbau.mit.atum.model.WeekFilter.DaysOfWeek.THURSDAY;
import static ru.spbau.mit.atum.model.WeekFilter.DaysOfWeek.TUESDAY;
import static ru.spbau.mit.atum.model.WeekFilter.DaysOfWeek.WEDNESDAY;

public class SchedulePlannerTest {
    @Test
    public void planScheduleTest() throws Exception {
        UserData userSynchronisableData = new UserData();
        List<UserDefinedTask> tasks = userSynchronisableData.getTasks();
        WeekFilter weekFilter1 = new WeekFilter("", 10 * 60, 90, EnumSet.of(TUESDAY, WEDNESDAY, FRIDAY), COMMON);
        WeekFilter weekFilter2 = new WeekFilter("", 10 * 60, 120, EnumSet.of(TUESDAY, THURSDAY, SATURDAY), COMMON);
        List<TimeFilter> filters = new ArrayList<>();
        filters.add(weekFilter1);
        filters.add(weekFilter2);

        UserDefinedTask task1 = UserDefinedTask.newGeneralTask("first", "", filters, 50, null, 0);
        UserDefinedTask task2 = UserDefinedTask.newGeneralTask("second", "", filters, 60, null, 0);

        tasks.add(task1);
        tasks.add(task2);

        ReadableDateTime initialMoment = theNthOfJan(1, 0, 0);
        ReadableDateTime finalMoment = theNthOfJan(10, 0, 0);

        SchedulePlanner.planSchedule(userSynchronisableData, initialMoment, finalMoment);
        assertNotNull(task1.getScheduledTime());
        assertNotNull(task2.getScheduledTime());

        assertTrue(task1.getScheduledTime().getDayOfWeek() != 1 && task1.getScheduledTime().getDayOfWeek() != 7);
        assertTrue(task2.getScheduledTime().getDayOfWeek() != 1 && task2.getScheduledTime().getDayOfWeek() != 7);

        assertTrue(task1.getScheduledTime().isAfter(initialMoment));
        assertTrue(task2.getScheduledTime().isAfter(initialMoment));

        assertTrue(task1.getScheduledTime().isBefore(finalMoment));
        assertTrue(task2.getScheduledTime().isBefore(finalMoment));

        assertTrue(task1.getScheduledTime().toDateTime().plusMinutes((int) task1.getDuration().getStandardMinutes()).
                isBefore(task2.getScheduledTime()) ||
                task2.getScheduledTime().toDateTime().plusMinutes((int) task2.getDuration().getStandardMinutes()).
                        isBefore(task1.getScheduledTime())
        );
    }
    
}