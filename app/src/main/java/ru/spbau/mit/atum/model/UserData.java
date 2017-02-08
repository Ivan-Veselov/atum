package ru.spbau.mit.atum.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.atum.model.UserDefinedTask;
import ru.spbau.mit.atum.model.UserDefinedTimeBlocker;

/**
 * Объект этого класса содержит данные пользователя о том, как он хочет выполнять задания.
 * Данные представляют собой список задач и список блокировщиков.
 */
public class UserData {
    protected List<UserDefinedTask> tasks;

    protected List<UserDefinedTimeBlocker> blockers;

    /**
     * Создает новый пустой набор данных.
     */
    public UserData() {
        tasks = new ArrayList<>();
        blockers = new ArrayList<>();
    }

    /**
     * @return список задач.
     */
    public @NonNull
    List<UserDefinedTask> getTasks() {
        return tasks;
    }

    /**
     * @return список блокировщиков.
     */
    public @NonNull List<UserDefinedTimeBlocker> getBlockers() {
        return blockers;
    }

    /**
     * Меняет список задач на переданный в качестве аргумента.
     *
     * @param tasks новый список задач.
     */
    public void setTasks(@NonNull List<UserDefinedTask> tasks) {
        this.tasks = tasks;
    }

    /**
     * Меняет список блокировщиков на переданный в качестве аргумента.
     *
     * @param blockers новый список блокировщиков.
     */
    public void setBlockers(@NonNull List<UserDefinedTimeBlocker> blockers) {
        this.blockers = blockers;
    }
}
