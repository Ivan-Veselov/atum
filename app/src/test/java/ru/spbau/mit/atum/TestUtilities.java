package ru.spbau.mit.atum;

import org.joda.time.DateTime;
import org.joda.time.base.AbstractDateTime;

/**
 * Вспомогательные метода для тестирования.
 */
public final class TestUtilities {
    /**
     * Класс является хранилищем статических методов.
     */
    private TestUtilities() {
    }

    /**
     * Генерирует объект класса Calendar, который задает некоторое время в январе 2000 года.
     *
     * @param day день месяца (1-индексация).
     * @param hour час.
     * @param minute минута.
     * @return объект класса Calendar.
     */
    public static AbstractDateTime theNthOfJan(int day, int hour, int minute) {
        return new DateTime(2000, 1, day, hour, minute, 0);
    }

    /**
     * Генерирует объект класса Calendar, который задает некоторое время 1 января 2000 года
     * (суббота).
     *
     * @param hour час.
     * @param minute минута.
     * @return объект класса Calendar.
     */
    public static AbstractDateTime theFirstOfJan(int hour, int minute) {
        return theNthOfJan(1, hour, minute);
    }

    public static class InvalidTestException extends Exception {
    }
}
