package ru.spbau.mit.atum;

import android.support.annotation.NonNull;

import org.joda.time.ReadableDateTime;

import java.util.ArrayList;
import java.util.List;

public class TimeLineTaskGroup {
    private final ReadableDateTime initialMoment;

    private final ReadableDateTime finalMoment;

    private List<TimeLineTask> taskList = new ArrayList<>();

    public TimeLineTaskGroup(List<UserDefinedTask> taskList,
                             List<UserDefinedTimeBlocker> blockerList,
                             @NonNull ReadableDateTime initialMoment,
                             @NonNull ReadableDateTime finalMoment) {
        List<Interval> blockerIntervalList = new ArrayList<>();
        for (AbstractFiltersHolder blocker: blockerList) {
            blockerIntervalList.addAll(blocker.intervalRepresentation(initialMoment, finalMoment));
        }

        blockerIntervalList = Interval.normalize(blockerIntervalList);

        for (UserDefinedTask task: taskList) {
            this.taskList.add(new TimeLineTask(task, initialMoment, finalMoment, blockerIntervalList));
        }

        this.initialMoment = initialMoment;
        this.finalMoment = finalMoment;
    }

    public List<TimeLineTask> getTaskList() {
        return taskList;
    }

}
