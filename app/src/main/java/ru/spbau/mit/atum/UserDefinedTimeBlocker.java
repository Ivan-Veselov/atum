package ru.spbau.mit.atum;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Объект этого класса представляет собой набор фильтров, которые задают некоторое подмножество
 * временной прямой. Это подмножество означает моменты времени, в которые пользователь не хочет
 * выполнять задания.
 */
public class UserDefinedTimeBlocker extends AbstractFiltersHolder {
    /**
     * @param name имя объекта.
     * @param description описание объекта.
     * @param filterList набор фильтров.
     */
    public UserDefinedTimeBlocker(@NotNull String name,
                                  @NotNull String description,
                                  @NotNull List<TimeFilter> filterList) {
        super(name, description, filterList);
    }
}
