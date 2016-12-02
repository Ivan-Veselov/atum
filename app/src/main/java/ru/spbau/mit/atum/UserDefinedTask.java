package ru.spbau.mit.atum;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.ReadableDateTime;

import java.util.List;

/**
 * Класс, представляющий задание, которое определил пользователь. Задание определяют фильры,
 * задающие промежутки времени, в которые это задание можно выполнить, и продолжительность
 * выполнения задания. Также к самому заданию еще можно добавить запланированное время его
 * выполнения.
 */
public class UserDefinedTask extends AbstractFiltersHolder {
    private final int duration;

    private ReadableDateTime scheduledTime = null;

    /**
     * Создает новое задание.
     *
     * @param name имя задания.
     * @param description описание задания.
     * @param filterList временные фильтры задания, которые задают время, в которое это задание
     *                   можно выполнять.
     * @param duration продолжительность выполнения задания.
     */
    public UserDefinedTask(@NonNull String name, @NonNull String description,
                           @NonNull List<TimeFilter> filterList, int duration) {
        super(name, description, filterList);

        if (duration <= 0) {
            throw new IllegalArgumentException("Duration of task is non positive.");
        }

        this.duration = duration;
    }

    /**
     * @return продолжительность выполнения задания.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @return запланированное время начала выполнения задания.
     */
    public @Nullable ReadableDateTime getScheduledTime() {
        return scheduledTime;
    }

    /**
     * Задает запланированное время начала выполнения задания.
     *
     * @param scheduledTime новое время начала выполнения задания.
     */
    public void setScheduledTime(@Nullable ReadableDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
