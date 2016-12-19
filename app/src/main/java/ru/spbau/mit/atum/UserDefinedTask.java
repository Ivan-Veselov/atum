package ru.spbau.mit.atum;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.location.places.Place;

import org.joda.time.ReadableDateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий задание, которое определил пользователь. Задание определяют фильры,
 * задающие промежутки времени, в которые это задание можно выполнить, и продолжительность
 * выполнения задания. Также к самому заданию еще можно добавить запланированное время его
 * выполнения.
 */
public class UserDefinedTask extends AbstractFiltersHolder {
    private final int duration;

    private final Place place;

    private ReadableDateTime scheduledTime = null;

    public static final Parcelable.Creator<UserDefinedTask> CREATOR
            = new Parcelable.Creator<UserDefinedTask>() {
        public UserDefinedTask createFromParcel(Parcel in) {
            return new UserDefinedTask(in);
        }

        public UserDefinedTask[] newArray(int size) {
            return new UserDefinedTask[size];
        }
    };

    /**
     * Создает новое задание.
     *
     * @param name имя задания.
     * @param description описание задания.
     * @param filterList временные фильтры задания, которые задают время, в которое это задание
     *                   можно выполнять.
     * @param duration продолжительность выполнения задания.
     * @param place место, в котором нужно выполнять задание.
     */
    public UserDefinedTask(@NonNull String name, @NonNull String description,
                           @NonNull List<TimeFilter> filterList,
                           int duration, @Nullable Place place) {
        super(name, description, filterList);

        if (duration <= 0) {
            throw new IllegalArgumentException("Duration of task is non positive.");
        }

        this.duration = duration;
        this.place = place;
    }

    /**
     * @return продолжительность выполнения задания.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Место, в котором нужно выполнять задание.
     */
    public Place getPlace() {
        return place;
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

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);

        out.writeInt(duration);
        out.writeParcelable((Parcelable) place, flags);
        out.writeSerializable((Serializable) scheduledTime);
    }

    protected UserDefinedTask(Parcel in) {
        super(in);

        duration = in.readInt();
        place = in.readParcelable(getClass().getClassLoader());
        scheduledTime = (ReadableDateTime) in.readSerializable();
    }
}
