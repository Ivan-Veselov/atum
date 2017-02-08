package ru.spbau.mit.atum;

import org.joda.time.DateTime;
import org.joda.time.base.AbstractDateTime;

import java.util.List;

import ru.spbau.mit.atum.model.Interval;

import static org.junit.Assert.assertEquals;

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
        int year = 2000;
        return new DateTime(year, 1, day, hour, minute, 0);
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

    /**
     * Проверяет состояние класса Interval.
     *
     * @param left левая граница интервала.
     * @param right правая граница интервала.
     * @param interval состояние класса Interval.
     * @throws Exception любое исключение, которое может вылететь во время тестирования.
     */
    public static void assertIntervalEquals(int left, int right, Interval interval) throws Exception {
        assertEquals(left, interval.left());
        assertEquals(right, interval.right());
    }

    /**
     * Проверяет список состояний класса Interval.
     *
     * @param lefts список левых границ интервалов.
     * @param rights список правых границ интервалов.
     * @param list список состояний класса Interval.
     * @throws Exception любое исключение, которое может вылететь во время тестирования.
     */
    public static void assertIntervalListEquals(int[] lefts, int[] rights,
                                         List<Interval> list) throws Exception {
        if (lefts.length != rights.length) {
            throw new TestUtilities.InvalidTestException();
        }

        assertEquals(lefts.length, list.size());

        for (int i = 0; i < list.size(); ++i) {
            assertIntervalEquals(lefts[i], rights[i], list.get(i));
        }
    }

    /**
     * Исключение, которое означает, что при работе теста произошла ошибка, связанная со структурой
     * теста, а не с тестируемым объектом.
     */
    public static class InvalidTestException extends Exception {
        InvalidTestException() {
        }
    }
}
