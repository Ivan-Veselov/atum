package ru.spbau.mit.atum;

import java.util.List;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import org.joda.time.ReadableDateTime;

/**
 * Фильтр, который задает непрерывный интервал времени.
 */
public class IntervalFilter extends TimeFilter {
    private ReadableDateTime filterInitialMoment;

    private ReadableDateTime filterFinalMoment;

    /**
     * Создает новый фильтр.
     *
     * @param filterInitialMoment начальный момент интервала, который задает фильтр.
     * @param filterFinalMoment конечный момент интервала, который задает фильтр.
     * @param exclusiveFlag если true, то фильтр будет исключающим.
     */
    public IntervalFilter(@NotNull ReadableDateTime filterInitialMoment,
                          @NotNull ReadableDateTime filterFinalMoment, boolean exclusiveFlag) {
        super(exclusiveFlag);

        if (!checkOrderOfMoments(filterInitialMoment, filterFinalMoment)) {
            throw new IllegalArgumentException(FINAL_LESS_THAN_INIT_MSG);
        }

        this.filterInitialMoment = filterInitialMoment;
        this.filterFinalMoment = filterFinalMoment;
    }

    /**
     * @return начальный момент интервала, который задает фильтр.
     */
    public @NotNull ReadableDateTime getInitialMoment() {
        return filterInitialMoment;
    }

    /**
     * @return конечный момент интервала, который задает фильтр.
     */
    public @NotNull ReadableDateTime getFinalMoment() {
        return filterFinalMoment;
    }

    /**
     * @param initialMoment точка отсчета и нижняя граница времени.
     * @param globalInterval целочисленный интервал всего доступного времени.
     * @return набор непересекающихся интервалов, представляющий временные промежутки, которые
     *         задает фильтр, в отсортированном порядке.
     */
    @Override
    protected @NotNull List<Interval> intervalRepresentationImpl(
                                                @NotNull ReadableDateTime initialMoment,
                                                @NotNull Interval globalInterval) {
        Interval filterInterval = new Interval(
                                        convertToPointRelative(initialMoment, filterInitialMoment),
                                        convertToPointRelative(initialMoment, filterFinalMoment));

        filterInterval = filterInterval.intersection(globalInterval);

        List<Interval> intervalList = new ArrayList<>();

        if (!filterInterval.isEmpty()) {
            intervalList.add(filterInterval);
        }

        return intervalList;
    }
}
