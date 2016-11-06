package ru.spbau.mit.atum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntervalTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() throws Exception {
        new Interval(1, 2);
        new Interval(0, 0);

        // Тут должно вылететь исключение
        new Interval(2, 1);
    }

    @Test
    public void testGetters() throws Exception {
        Interval interval = new Interval(-1, 2);

        assertEquals(-1, interval.begin());
        assertEquals(2, interval.end());
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertEquals(false, (new Interval(0, 1).isEmpty()));
        assertEquals(true, (new Interval(0, 0).isEmpty()));
    }

    /**
     * Вспомогательный метод, который тестирует пересечени двух интервалов.
     */
    private void testIntersectWithOnImpl(int firstIntervalBegin,
                                         int firstIntervalEnd,
                                         int secondIntervalBegin,
                                         int secondIntervalEnd,
                                         int resultIntervalBegin,
                                         int resultIntervalEnd) throws Exception {
        Interval interval;
        interval = new Interval(firstIntervalBegin, firstIntervalEnd);
        interval.intersectWith(new Interval(secondIntervalBegin, secondIntervalEnd));
        assertEquals(resultIntervalBegin, interval.begin());
        assertEquals(resultIntervalEnd, interval.end());
    }

    /**
     * Вспомогательный метод, который тестирует пересечение двух интервалов, вызывая метод сначала
     * от одного, а затем от другого.
     */
    private void testIntersectWithOn(int firstIntervalBegin,
                                     int firstIntervalEnd,
                                     int secondIntervalBegin,
                                     int secondIntervalEnd,
                                     int resultIntervalBegin,
                                     int resultIntervalEnd) throws Exception {
        testIntersectWithOnImpl(firstIntervalBegin, firstIntervalEnd,
                                secondIntervalBegin, secondIntervalEnd,
                                resultIntervalBegin, resultIntervalEnd);

        testIntersectWithOnImpl(secondIntervalBegin, secondIntervalEnd,
                                firstIntervalBegin, firstIntervalEnd,
                                resultIntervalBegin, resultIntervalEnd);
    }

    @Test
    public void testIntersectWith() throws Exception {
        // Нет пересечения
        testIntersectWithOn(0, 1, 2, 3,
                            2, 2);

        // Вложение интервалов
        testIntersectWithOn(0, 3, 1, 2,
                            1, 2);

        // Частичное пересечени
        testIntersectWithOn(0, 2, 1, 3,
                            1, 2);
    }
}