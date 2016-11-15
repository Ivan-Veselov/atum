package ru.spbau.mit.atum;

import org.jetbrains.annotations.NotNull;
import org.joda.time.ReadableDateTime;

import java.util.ArrayList;
import java.util.List;

public class TimeLineTask {
    private List<Interval> timeIntervals = new ArrayList<>();
    private UserDefinedTask holder;

    public TimeLineTask(UserDefinedTask holder,
                        @NotNull ReadableDateTime initialMoment,
                        @NotNull ReadableDateTime finalMoment,
                        List<Interval> blockerIntarvalList) {
        List<Interval> intervals = Interval.difference(holder.intervalRepresentation(initialMoment, finalMoment),
                                            blockerIntarvalList);
        for (Interval interval: intervals) {
            if (interval.right() - interval.left() >= holder.getDuration()) {
                timeIntervals.add(interval);
            }
        }
        this.holder = holder;
    }

    public List<Interval> getTimeIntervals() {
        return timeIntervals;
    }

    public UserDefinedTask getHolder() {
        return holder;
    }
}
