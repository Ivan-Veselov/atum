package ru.spbau.mit.atum;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
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
        PlanScheduleAlgorithm(timeLineTaskGroup, initialMoment);
    }

    private static void PlanScheduleAlgorithm(TimeLineTaskGroup tasks,
                                             @NonNull ReadableDateTime initialMoment) {

        List<Interval> resultIntervals = new ArrayList<>();

        clearAllSchedule(tasks);

        PlanFixedTasksScheduleAlgorithm(tasks, initialMoment, resultIntervals);
        PlanGeneralTasksScheduleAlgorithm(tasks, initialMoment, resultIntervals);
        PlanAdditionalTasksScheduleAlgorithm(tasks);
    }

    private static void PlanAdditionalTasksScheduleAlgorithm(TimeLineTaskGroup tasks) {
        for (TimeLineTask task: tasks.getTaskList()) {
            if (task.getType() == UserDefinedTask.Type.QUICKIE) {
                DateTime minTime = null;
                for (TimeLineTask taskFirst: tasks.getTaskList()) {
                    if (taskFirst.getHolder().getScheduledTime() != null &&
                            task.getHolder().getPlace().equals(taskFirst.getHolder().getPlace())) {
                        if (minTime == null || taskFirst.getHolder().getScheduledTime().isBefore(minTime)) {
                            minTime = (DateTime)taskFirst.getHolder().getScheduledTime();
                        }
                    }
                }
                task.getHolder().setScheduledTime(minTime);
            }
        }

    }

    private static void PlanFixedTasksScheduleAlgorithm(TimeLineTaskGroup tasks,
                                                           @NonNull ReadableDateTime initialMoment,
                                                        List<Interval> resultIntervals) {
        for (TimeLineTask task: tasks.getTaskList()) {
            if (task.getType() == UserDefinedTask.Type.FIXED) {
                if (task.getTimeIntervals().size() > 0) {
                    Interval interval = task.getTimeIntervals().get(0);
                    resultIntervals.add(interval);
                    task.getHolder().setScheduledTime(initialMoment.toDateTime().plusMinutes(interval.left()));
                }
            }
        }

    }

    private static void PlanGeneralTasksScheduleAlgorithm(TimeLineTaskGroup tasks,
                                                    @NonNull ReadableDateTime initialMoment,
                                                    List<Interval> resultIntervals) {
        for (TimeLineTask task: tasks.getTaskList()) {
            if (task.getType() == UserDefinedTask.Type.GENERAL) {
                List<Interval> possibleIntervals = Interval.difference(task.getTimeIntervals(), resultIntervals);
                Interval interval = Interval.getMaxInterval(possibleIntervals);
                if (interval.length() >= task.getDuration()) {
                    resultIntervals.add(interval.withDuration(task.getDuration()));
                    task.getHolder().setScheduledTime(initialMoment.toDateTime().plusMinutes(interval.left()));
                }
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

    private static void clearAllSchedule(TimeLineTaskGroup tasks) {
        for (TimeLineTask task: tasks.getTaskList()) {
            task.getHolder().setScheduledTime(null);
        }
    }
}
