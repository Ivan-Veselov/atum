package ru.spbau.mit.atum;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Вспомогательные метода для тестирования.
 */
public class TestUtilities {
    /**
     * Генерирует объект класса Calendar, который задает некоторое время в январе 2000 года.
     *
     * @param day день месяца (1-индексация).
     * @param hour час.
     * @param minute минута.
     * @return объект класса Calendar.
     */
    public static Calendar theNthOfJan(int day, int hour, int minute) {
        return new GregorianCalendar(2000, 0, day, hour, minute, 0);
    }

    /**
     * Генерирует объект класса Calendar, который задает некоторое время 1 января 2000 года
     * (суббота).
     *
     * @param hour час.
     * @param minute минута.
     * @return объект класса Calendar.
     */
    public static Calendar theFirstOfJan(int hour, int minute) {
        return theNthOfJan(1, hour, minute);
    }
}
