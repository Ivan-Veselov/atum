package ru.spbau.mit.atum;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Фильтр, который задает непрерывный интервал времени.
 */
public class IntervalFilter extends TimeFilter {
    private Calendar filterInitialMoment;

    private Calendar filterFinalMoment;

    /**
     * Создает новый фильтр.
     *
     * @param filterInitialMoment начальный момент интервала, который задает фильтр.
     * @param filterFinalMoment конечный момент интервала, который задает фильтр.
     * @param exclusiveFlag если true, то фильтр будет исключающим.
     */
    public IntervalFilter(@NotNull Calendar filterInitialMoment,
                          @NotNull Calendar filterFinalMoment, boolean exclusiveFlag) {
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
    public @NotNull Calendar getInitialMoment() {
        return filterInitialMoment;
    }

    /**
     * @return конечный момент интервала, который задает фильтр.
     */
    public @NotNull Calendar getFinalMoment() {
        return filterFinalMoment;
    }

    /**
     * @param initialMoment точка отсчета и нижняя граница времени.
     * @param globalInterval целочисленный интервал всего доступного времени.
     * @return набор непересекающихся интервалов, представляющий временные промежутки, которые
     *         задает фильтр.
     */
    @Override
    protected @NotNull List<DualInterval> intervalRepresentationImpl(
                                                @NotNull Calendar initialMoment,
                                                @NotNull Interval globalInterval) {
        DualInterval filterInterval = new DualInterval(
                                        convertToPointRelative(initialMoment, filterInitialMoment),
                                        convertToPointRelative(initialMoment, filterFinalMoment),
                                        isExclusive());

        filterInterval.intersectWith(globalInterval);

        List<DualInterval> intervalList = new ArrayList<>();

        if (!filterInterval.isEmpty()) {
            intervalList.add(filterInterval);
        }

        return intervalList;
    }
}
