package ru.spbau.mit.atum;

import android.support.annotation.NonNull;

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

    /**
     * Меняет список задач на переданный в качестве аргумента.
     *
     * @param taskList новый список задач.
     */
    public void setTaskList(@NonNull List<UserDefinedTask> taskList) {
        this.taskList = taskList;
    }

    /**
     * Меняет список блокировщиков на переданный в качестве аргумента.
     *
     * @param blockerList новый список блокировщиков.
     */
    public void setBlockerList(@NonNull List<UserDefinedTimeBlocker> blockerList) {
        this.blockerList = blockerList;
    }
}
