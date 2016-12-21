package ru.spbau.mit.atum;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.location.places.Place;

import org.joda.time.ReadableDateTime;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Класс, представляющий задание, которое определил пользователь. Задание определяют фильры,
 * задающие промежутки времени, в которые это задание можно выполнить, и продолжительность
 * выполнения задания. Также к самому заданию еще можно добавить запланированное время его
 * выполнения.
 */
public class UserDefinedTask extends AbstractFiltersHolder {
    private final Type type;

    private final int duration;

    private int restDuration = 0;

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
     * TODO: заменить на статический метод
     * Создает новое задание общего типа, без каких либо особенных свойств.
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
        this(name, description, filterList, Type.GENERAL, duration, place);
    }

    /**
     * Создает новое фиксированное задание. Такое задание фиксировано во времени и время его
     * выполнения не может быть никуда подвинуто.
     *
     * @param name имя задания.
     * @param description описание задания.
     * @param initialMoment время начала выполнения задания.
     * @param finalMoment время окончание выполнения задания.
     * @param place место, в котором нужно выполнять задание.
     * @return новое задание фиксированного типа.
     */
    public static UserDefinedTask newFixedTask(@NonNull String name, @NonNull String description,
                                               @NonNull ReadableDateTime initialMoment,
                                               @NonNull ReadableDateTime finalMoment,
                                               @Nullable Place place) {
        return new UserDefinedTask(name,
                                   description,
                                   Collections.singletonList(
                                        (TimeFilter) new IntervalFilter(
                                            "",
                                            initialMoment,
                                            finalMoment,
                                            TimeFilter.ExclusionType.COMMON)),
                                   Type.FIXED,
                                   TimeIntervalUtils.convertToPointRelative(initialMoment,
                                                                            finalMoment),
                                   place);
    }

    /**
     * Создает новое быстрое задание, которое обязательно привязано к некоторому месту и имеет
     * нулевую продолжительность выполнения.
     *
     * @param name имя задания.
     * @param description описание задания.
     * @param place место, в котором нужно выполнять задание.
     * @return новое быстрое задание.
     */
    public static UserDefinedTask newQuickieTask(@NonNull String name, @NonNull String description,
                                                 @NonNull Place place) {
        return new UserDefinedTask(name,
                                   description,
                                   Collections.singletonList(
                                        (TimeFilter) WeekFilter.newWeekFilterFromMinutesInterval(
                                            "", 0, 0,
                                            EnumSet.allOf(WeekFilter.DaysOfWeek.class),
                                            TimeFilter.ExclusionType.COMMON)),
                                   Type.QUICKIE,
                                   0,
                                   place);
    }

    /**
     * Возвращает тип задания.
     */
    public Type getType() {
        return type;
    }

    public void setRestDuration(int restDuration) {
        this.restDuration = restDuration;
    }

    public int getRestDuration() {
        return restDuration;
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

        out.writeSerializable(type);
        out.writeInt(duration);
        out.writeParcelable((Parcelable) place, flags);
        out.writeSerializable((Serializable) scheduledTime);
    }

    protected UserDefinedTask(Parcel in) {
        super(in);

        type = (Type) in.readSerializable();
        duration = in.readInt();
        place = in.readParcelable(getClass().getClassLoader());
        scheduledTime = (ReadableDateTime) in.readSerializable();
    }

    private UserDefinedTask(@NonNull String name, @NonNull String description,
                            @NonNull List<TimeFilter> filterList,
                            @NonNull Type type,
                            int duration, @Nullable Place place) {
        super(name, description, filterList);

        if (duration < 0) {
            throw new IllegalArgumentException("Duration of task is non positive.");
        }

        this.type = type;
        this.duration = duration;
        this.place = place;
    }

    public enum Type { GENERAL, FIXED, QUICKIE }
}
