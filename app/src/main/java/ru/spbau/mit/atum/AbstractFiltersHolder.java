package ru.spbau.mit.atum;

import android.support.annotation.NonNull;

import org.joda.time.ReadableDateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Абстрактный класс, описывающий объекты, которые состоят из набора фильтров. Типичный пример
 * такого объекта - задача.
 * Весь набор фильтров описывает некоторое подмножество временной прямой. Объединение обычных
 * фильтров дает некоторое подмножество прямой. Если из этого объединения вычесть объединение
 * исключающих фильтров, то получится подмножество временной прямой, которое задает набор фильтров.
 */
public abstract class AbstractFiltersHolder implements Serializable {
    private final String name;

    private final String description;

    private final List<TimeFilter> filterList;

    /**
     * Конструктор абстрактного объекта, содержащего набор фильтров.
     *
     * @param name имя объекта.
     * @param description описание объекта.
     * @param filterList набор фильтров.
     */
    public AbstractFiltersHolder(@NonNull String name, @NonNull String description,
                                 @NonNull List<TimeFilter> filterList) {
        this.name = name;
        this.description = description;
        this.filterList = Collections.unmodifiableList(filterList);
    }

    /**
     * @return имя объекта.
     */
    public @NonNull String getName() {
        return name;
    }

    /**
     * @return описание объекта.
     */
    public @NonNull String getDescription() {
        return description;
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
}
