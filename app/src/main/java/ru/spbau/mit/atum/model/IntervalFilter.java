package ru.spbau.mit.atum.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.joda.time.ReadableDateTime;

import ru.spbau.mit.atum.R;

/**
 * Фильтр, который задает непрерывный интервал времени.
 */
public class IntervalFilter extends TimeFilter {
    private final ReadableDateTime filterInitialMoment;

    private final ReadableDateTime filterFinalMoment;

    public static final Parcelable.Creator<IntervalFilter> CREATOR
            = new Parcelable.Creator<IntervalFilter>() {
        public IntervalFilter createFromParcel(Parcel in) {
            return new IntervalFilter(in);
        }

        public IntervalFilter[] newArray(int size) {
            return new IntervalFilter[size];
        }
    };

    /**
     * Создает новый фильтр.
     *
     * @param description описание фильтра.
     * @param filterInitialMoment начальный момент интервала, который задает фильтр.
     * @param filterFinalMoment конечный момент интервала, который задает фильтр.
     * @param exclusionType тип фильтра (исключающий или нет).
     */
    public IntervalFilter(@NonNull String description,
                          @NonNull ReadableDateTime filterInitialMoment,
                          @NonNull ReadableDateTime filterFinalMoment,
                          @NonNull ExclusionType exclusionType) {
        super(description, exclusionType);

        if (!TimeIntervalUtils.checkOrderOfMoments(filterInitialMoment, filterFinalMoment)) {
            throw new IllegalArgumentException(
              "In arguments of IntervalFilter constructor: final moment is less than initial.");
        }

        this.filterInitialMoment = filterInitialMoment;
        this.filterFinalMoment = filterFinalMoment;
    }

    /**
     * Возвращает описание типа фильтра в заданном контексте.
     *
     * @param context контекст приложения.
     * @return описание типа фильтра.
     */
    public @NonNull String getTypeDescription(@NonNull Context context) {
        return context.getString(R.string.interval_filter);
    }

    /**
     * @return начальный момент интервала, который задает фильтр.
     */
    public @NonNull ReadableDateTime getInitialMoment() {
        return filterInitialMoment;
    }

    /**
     * @return конечный момент интервала, который задает фильтр.
     */
    public @NonNull ReadableDateTime getFinalMoment() {
        return filterFinalMoment;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);

        out.writeSerializable((Serializable) filterInitialMoment);
        out.writeSerializable((Serializable) filterFinalMoment);
    }

    /**
     * @param initialMoment точка отсчета и нижняя граница времени.
     * @param globalInterval целочисленный интервал всего доступного времени.
     * @return набор непересекающихся интервалов, представляющий временные промежутки, которые
     *         задает фильтр, в отсортированном порядке.
     */
    @Override
    protected @NonNull List<Interval> intervalRepresentationImpl(
                                                @NonNull ReadableDateTime initialMoment,
                                                @NonNull Interval globalInterval) {
        Interval filterInterval = new Interval(
                    TimeIntervalUtils.convertToPointRelative(initialMoment, filterInitialMoment),
                    TimeIntervalUtils.convertToPointRelative(initialMoment, filterFinalMoment));

        filterInterval = filterInterval.intersection(globalInterval);

        List<Interval> intervalList = new ArrayList<>();

        if (!filterInterval.isEmpty()) {
            intervalList.add(filterInterval);
        }

        return intervalList;
    }

    private IntervalFilter(Parcel in) {
        super(in);

        filterInitialMoment = (ReadableDateTime) in.readSerializable();
        filterFinalMoment = (ReadableDateTime) in.readSerializable();
    }
}
