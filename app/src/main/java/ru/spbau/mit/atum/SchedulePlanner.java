package ru.spbau.mit.atum;

import org.jetbrains.annotations.NotNull;
import org.joda.time.ReadableDateTime;

import java.util.ArrayList;
import java.util.List;

public class SchedulePlanner {
    public static void planSchedule(UserPreferences preferences,
                                    @NotNull ReadableDateTime initialMoment,
                                    @NotNull ReadableDateTime finalMoment) {
        TimeLineTaskGroup timeLineTaskGroup = new TimeLineTaskGroup(preferences.getTaskList(),
                                                                    preferences.getBlockerList(),
                                                                    initialMoment, finalMoment);
        planSchedule(timeLineTaskGroup, initialMoment);
    }

    private static void planSchedule(TimeLineTaskGroup tasks,
                                     @NotNull ReadableDateTime initialMoment) {
        List<Interval> resultIntervals = new ArrayList<>();
        for (TimeLineTask task: tasks.getTaskList()) {
            for (Interval interval: task.getTimeIntervals()) {
                if (!interval.isIntersectionWithListOfIntervals(resultIntervals)) {
                    resultIntervals.add(interval);
                    task.getHolder().setScheduledTime(initialMoment.toDateTime().plusMinutes(interval.left()));
                    break;
                }
            }
        }
    }
}
