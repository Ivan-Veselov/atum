package ru.spbau.mit.atum;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Абстрактный класс, описывающий объекты, которые состоят из набора фильтров. Типичный пример
 * такого объекта - задача.
 * Весь набор фильтров описывает некоторое подмножество временной прямой. Объединение обычных
 * фильтров дает некоторое подмножество прямой. Если из этого объединения вычесть объединение
 * исключающих фильтров, то получится подмножество временной прямой, которое задает набор фильтров.
 */
public abstract class AbstractFiltersHolder {
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
    public AbstractFiltersHolder(@NotNull String name, @NotNull String description,
                                 @NotNull List<TimeFilter> filterList) {
        this.name = name;
        this.description = description;
        this.filterList = Collections.unmodifiableList(filterList);
    }

    /**
     * @return имя объекта.
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * @return описание объекта.
     */
    public @NotNull String getDescription() {
        return description;
    }

    /**
     * @return неизменяемый набор фильтров.
     */
    public @NotNull List<TimeFilter> getFilterList() {
        return filterList;
    }
}
