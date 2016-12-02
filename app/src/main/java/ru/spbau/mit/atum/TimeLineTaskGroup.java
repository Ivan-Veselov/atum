package ru.spbau.mit.atum;

import android.support.annotation.NonNull;

import org.joda.time.ReadableDateTime;

import java.util.ArrayList;
import java.util.List;

public class TimeLineTaskGroup {
    ReadableDateTime initial_moment;
    ReadableDateTime final_moment;
    private List<TimeLineTask> taskList = new ArrayList<>();

    public TimeLineTaskGroup(List<UserDefinedTask> taskList,
                             List<UserDefinedTimeBlocker> blockerList,
                             @NonNull ReadableDateTime initialMoment,
                             @NonNull ReadableDateTime finalMoment) {
        List<Interval> blockerIntervalList = new ArrayList<>();
        for (AbstractFiltersHolder blocker: blockerList) {
            blockerIntervalList.addAll(blocker.intervalRepresentation(initialMoment, finalMoment));
        }
        Interval.normalize(blockerIntervalList);

        for (UserDefinedTask task: taskList) {
            this.taskList.add(new TimeLineTask(task, initialMoment, finalMoment, blockerIntervalList));
        }
        this.initial_moment = initialMoment;
        this.final_moment = finalMoment;
    }

    public List<TimeLineTask> getTaskList() {
        return taskList;
    }

}
