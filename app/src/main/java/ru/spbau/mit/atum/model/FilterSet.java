package ru.spbau.mit.atum.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.joda.time.ReadableDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.spbau.mit.atum.model.Interval;
import ru.spbau.mit.atum.model.TimeFilter;

/**
 * Класс, описывающий набор фильтров.
 * Весь набор фильтров описывает некоторое подмножество временной прямой. Объединение обычных
 * фильтров дает некоторое подмножество прямой. Если из этого объединения вычесть объединение
 * исключающих фильтров, то получится подмножество временной прямой, которое задает набор фильтров.
 */
public final class FilterSet implements Parcelable {
    private final List<TimeFilter> filterList;

    public static final Parcelable.Creator<FilterSet> CREATOR
            = new Parcelable.Creator<FilterSet>() {
        public FilterSet createFromParcel(Parcel in) {
            return new FilterSet(in);
        }

        public FilterSet[] newArray(int size) {
            return new FilterSet[size];
        }
    };

    /**
     * Конструктор набора фильтров.
     *
     * @param filterList набор фильтров.
     */
    public FilterSet(@NonNull List<TimeFilter> filterList) {
        this.filterList = Collections.unmodifiableList(filterList);
    }

    /**
     * @return неизменяемый набор фильтров.
     */
    public @NonNull List<TimeFilter> getFilterList() {
        return filterList;
    }

    /**
     * Конвертирует подмножество временной прямой, которое задает набор фильтров, в набор
     * непересекающихся интервалов на прямой. Интервалы целочисленные, единицей дискретизации
     * является минута.
     * Точкой отсчета считается первый аргумент, все интервалы до нее обрезаются или отбрасываются.
     * Второй аргумент задает правую границу, все интервалы после нее обрезаются или отбрасываются.
     * Таким образом бесконечное число интервалов получится не может. Интервалы расположены в
     * отсортированном порядке.
     *
     * @param initialMoment точка отсчета и нижняя граница времени.
     * @param finalMoment верхняя граница времени.
     * @return набор непересекающихся интервалов, представляющий подмножество временной прямой,
     *         которое задает набор фильтров, в отсортированном порядке.
     */
    public @NonNull List<Interval> intervalRepresentation(@NonNull ReadableDateTime initialMoment,
                                                          @NonNull ReadableDateTime finalMoment) {
        List<Interval> commonFiltersRepresentation = new ArrayList<>();
        List<Interval> exclusiveFiltersRepresentation = new ArrayList<>();

        for (TimeFilter filter : filterList) {
            List<Interval> filterRepresentation = filter.intervalRepresentation(initialMoment,
                    finalMoment);
            if (filter.isExclusive()) {
                exclusiveFiltersRepresentation.addAll(filterRepresentation);
            } else {
                commonFiltersRepresentation.addAll(filterRepresentation);
            }
        }

        return Interval.difference(commonFiltersRepresentation, exclusiveFiltersRepresentation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(filterList);
    }

    protected FilterSet(Parcel in) {
        filterList = new ArrayList<>();
        in.readList(filterList, getClass().getClassLoader());
    }
}
