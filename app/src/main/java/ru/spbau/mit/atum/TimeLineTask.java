package ru.spbau.mit.atum;

import android.support.annotation.NonNull;

import org.joda.time.ReadableDateTime;

import java.util.ArrayList;
import java.util.List;

public class TimeLineTask {
    private List<Interval> timeIntervals = new ArrayList<>();
    private UserDefinedTask holder;

    public TimeLineTask(UserDefinedTask holder,
                        @NonNull ReadableDateTime initialMoment,
                        @NonNull ReadableDateTime finalMoment,
                        List<Interval> blockerIntervalList) {
        List<Interval> intervals = Interval.difference(holder.intervalRepresentation(initialMoment, finalMoment),
                                            blockerIntervalList);
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

    public UserDefinedTask.Type getType() {
        return holder.getType();
    }

    public int getDuration() {
        return holder.getDuration() + holder.getRestDuration();
    }
}
