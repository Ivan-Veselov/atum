package ru.spbau.mit.atum;

import android.support.annotation.NonNull;

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
    //    Log.i("my_tag", initialMoment.toString());
    //    Log.i("my_tag", finalMoment.toString());
        PlanScheduleAlgorithmWithFixedTasks(timeLineTaskGroup, initialMoment);
    }

    private static void PlanScheduleAlgorithmWithFixedTasks(TimeLineTaskGroup tasks,
                                                           @NonNull ReadableDateTime initialMoment) {
        List<Interval> resultIntervals = new ArrayList<>();
        for (TimeLineTask task: tasks.getTaskList()) {
            if (task.getType() == UserDefinedTask.Type.FIXED) {
                if (task.getTimeIntervals().size() > 0) {
                    Interval interval = task.getTimeIntervals().get(0);
                    resultIntervals.add(interval);
                    task.getHolder().setScheduledTime(initialMoment.toDateTime().plusMinutes(interval.left()));
                }
            }
        }
        SimplePlanScheduleAlgorithm(tasks, initialMoment, resultIntervals);
    }

    private static void SimplePlanScheduleAlgorithm(TimeLineTaskGroup tasks,
                                                    @NonNull ReadableDateTime initialMoment,
                                                    List<Interval> resultIntervals) {
        for (TimeLineTask task: tasks.getTaskList()) {
            List<Interval> possibleIntervals = Interval.difference(task.getTimeIntervals(), resultIntervals);
            Interval interval = Interval.getMaxInterval(possibleIntervals);
            if (interval.length() >= task.getDuration()) {
                resultIntervals.add(interval.withDuration(task.getDuration()));
                task.getHolder().setScheduledTime(initialMoment.toDateTime().plusMinutes(interval.left()));
            }
        }

    }

    private static void SuperSimplePlanScheduleAlgorithm(TimeLineTaskGroup tasks,
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
