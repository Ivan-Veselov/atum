package ru.spbau.mit.atum;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Класс, представляющий задание, которое определил пользователь. Задание определяют фильры,
 * задающие промежутки времени, в которые это задание можно выполнить, и продолжительность
 * выполнения задания. Также к самому заданию еще можно добавить запланированное время его
 * выполнения.
 */
public class UserDefinedTask extends AbstractFiltersHolder {
    private static final String NON_POS_DURATION_MSG = "Duration of task is non positive.";

    private final int duration;

    /**
     * Создает новое задание.
     *
     * @param name имя задания.
     * @param description описание задания.
     * @param filterList временные фильтры задания, которые задают время, в которое это задание
     *                   можно выполнять.
     * @param duration продолжительность выполнения задания.
     */
    public UserDefinedTask(@NotNull String name, @NotNull String description,
                           @NotNull List<TimeFilter> filterList, int duration) {
        super(name, description, filterList);

        if (duration <= 0) {
            throw new IllegalArgumentException(NON_POS_DURATION_MSG);
        }

        this.duration = duration;
    }

    /**
     * @return продолжительность выполнения задания.
     */
    public int getDuration() {
        return duration;
    }
}
