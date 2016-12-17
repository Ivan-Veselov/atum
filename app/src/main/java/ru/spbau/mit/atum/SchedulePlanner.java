package ru.spbau.mit.atum;

import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.ReadableDateTime;

import java.util.ArrayList;
import java.util.List;

public final class SchedulePlanner {

    private SchedulePlanner() {}

    public static void planSchedule(UserData preferences,
                                    @NonNull ReadableDateTime initialMoment,
                                    @NonNull ReadableDateTime finalMoment) {
        TimeLineTaskGroup timeLineTaskGroup = new TimeLineTaskGroup(preferences.getTasks(),
                                                                    preferences.getBlockers(),
                                                                    initialMoment, finalMoment);
        Log.i("my_tag", initialMoment.toString());
        Log.i("my_tag", finalMoment.toString());
        planSchedule(timeLineTaskGroup, initialMoment);
    }

    private static void planSchedule(TimeLineTaskGroup tasks,
                                     @NonNull ReadableDateTime initialMoment) {
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
