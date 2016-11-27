package ru.spbau.mit.atum;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Объект этого класса содержит предпочтения пользователя о том, как он хочет выполнять задания.
 * Предпочтения задают список задач и список блокировщиков.
 */
public class UserPreferences {
    private List<UserDefinedTask> taskList;

    private List<UserDefinedTimeBlocker> blockerList;

    /**
     * Создает новые предпочтения с пустыми списками.
     */
    public UserPreferences() {
        taskList = new ArrayList<>();
        blockerList = new ArrayList<>();
    }

    /**
     * @return список задач.
     */
    public @NotNull List<UserDefinedTask> getTaskList() {
        return taskList;
    }

    /**
     * @return список блокировщиков.
     */
    public @NotNull List<UserDefinedTimeBlocker> getBlockerList() {
        return blockerList;
    }
}
