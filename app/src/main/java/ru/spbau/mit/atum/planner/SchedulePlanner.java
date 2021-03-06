package ru.spbau.mit.atum.planner;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.spbau.mit.atum.model.Interval;
import ru.spbau.mit.atum.model.UserData;
import ru.spbau.mit.atum.model.UserDefinedTask;

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
        PlanGeneralTasksWithPrioritiesScheduleAlgorithm(tasks, initialMoment, resultIntervals);
        PlanAdditionalTasksScheduleAlgorithm(tasks);
    }

    private static void PlanAdditionalTasksScheduleAlgorithm(TimeLineTaskGroup tasks) {
        for (TimeLineTask task: tasks.getTaskList()) {
            if (task.getType() == UserDefinedTask.Type.QUICKIE) {
                DateTime minTime = null;
                for (TimeLineTask taskFirst: tasks.getTaskList()) {
                    CharSequence address = null;
                    if (taskFirst.getHolder().getPlace() != null) {
                        address = task.getHolder().getPlace().getAddress();
                    }
                    if (taskFirst.getHolder().getScheduledTime() != null &&
                            task.getHolder().getPlace().getAddress().equals(address)) {
                        DateTime taskFirstEnd = ((DateTime)taskFirst.getHolder().getScheduledTime())
                                .plusMinutes((int) taskFirst.getHolder().getDuration().getStandardMinutes());
                        if (minTime == null || taskFirstEnd.isBefore(minTime)) {
                            minTime = taskFirstEnd;
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
                    Interval interval = new Interval(task.getTimeIntervals().get(0).left(),
                            task.getTimeIntervals().get(0).left() + task.getDuration());
                    resultIntervals.add(interval);
                    task.getHolder().setScheduledTime(initialMoment.toDateTime().plusMinutes(interval.left()));
                }
            }
        }

    }

    private static void PlanGeneralTasksWithPrioritiesScheduleAlgorithm(TimeLineTaskGroup tasks,
                                                          @NonNull ReadableDateTime initialMoment,
                                                          List<Interval> resultIntervals) {
        Comparator<TimeLineTask> comp = new Comparator<TimeLineTask>() {
            @Override
            public int compare(TimeLineTask o1, TimeLineTask o2) {
                return ((Integer)o2.getHolder().getPriority()).compareTo(o1.getHolder().getPriority());
            }
        };
        Collections.sort(tasks.getTaskList(), comp);

        PlanGeneralTasksScheduleAlgorithm(tasks, initialMoment, resultIntervals);

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
