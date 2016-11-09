package ru.spbau.mit.atum;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IntervalTest {
    @Test
    public void testConstructor1() throws Exception {
        new Interval(1, 2);
        new Interval(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() throws Exception {
        new Interval(2, 1);
    }

    @Test
    public void testGetters() throws Exception {
        Interval interval = new Interval(-1, 2);

        assertEquals(-1, interval.left());
        assertEquals(2, interval.right());
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertEquals(false, (new Interval(0, 1).isEmpty()));
        assertEquals(true, (new Interval(0, 0).isEmpty()));
    }

    @Test
    public void testEndPointsGetters() throws Exception {
        Interval interval = new Interval(-42, -10);

        Interval.EndPoint left = interval.leftEndPoint();
        assertEquals(-42, left.getCoordinate());
        assertEquals(false, left.isRight());

        Interval.EndPoint right = interval.rightEndPoint();
        assertEquals(-10, right.getCoordinate());
        assertEquals(true, right.isRight());
    }

    /**
     * Вспомогательный метод, который тестирует пересечени двух интервалов.
     */
    private void testIntersectionOnImpl(int firstIntervalLeftEnd,
                                        int firstIntervalRightEnd,
                                        int secondIntervalLeftEnd,
                                        int secondIntervalRightEnd,
                                        int resultIntervalLeftEnd,
                                        int resultIntervalRightEnd) throws Exception {
        Interval interval;
        interval = new Interval(firstIntervalLeftEnd, firstIntervalRightEnd);
        interval = interval.intersection(new Interval(secondIntervalLeftEnd, secondIntervalRightEnd));
        assertEquals(resultIntervalLeftEnd, interval.left());
        assertEquals(resultIntervalRightEnd, interval.right());
    }

    /**
     * Вспомогательный метод, который тестирует пересечение двух интервалов, вызывая метод сначала
     * от одного, а затем от другого.
     */
    private void testIntersectionOn(int firstIntervalLeftEnd,
                                    int firstIntervalRightEnd,
                                    int secondIntervalLeftEnd,
                                    int secondIntervalRightEnd,
                                    int resultIntervalLeftEnd,
                                    int resultIntervalRightEnd) throws Exception {
        testIntersectionOnImpl(firstIntervalLeftEnd, firstIntervalRightEnd,
                               secondIntervalLeftEnd, secondIntervalRightEnd,
                               resultIntervalLeftEnd, resultIntervalRightEnd);

        testIntersectionOnImpl(secondIntervalLeftEnd, secondIntervalRightEnd,
                               firstIntervalLeftEnd, firstIntervalRightEnd,
                               resultIntervalLeftEnd, resultIntervalRightEnd);
    }

    @Test
    public void testIntersection() throws Exception {
        // Нет пересечения
        testIntersectionOn(0, 1, 2, 3,
                           2, 2);

        // Вложение интервалов
        testIntersectionOn(0, 3, 1, 2,
                           1, 2);

        // Частичное пересечени
        testIntersectionOn(0, 2, 1, 3,
                           1, 2);
    }

    private void assertEndPoint(int coordinate,
                                boolean rightFlag,
                                Interval.EndPoint endPoint) throws Exception{
        assertEquals(coordinate, endPoint.getCoordinate());
        assertEquals(rightFlag, endPoint.isRight());
    }

    @Test
    public void testEndPoints() throws Exception {
        List<Interval.EndPoint> list = Interval.endPoints(Arrays.asList(new Interval(-10, 10),
                                                                        new Interval(-8, -6),
                                                                        new Interval(-7, 20)));

        Collections.sort(list);
        assertEquals(6, list.size());

        assertEndPoint(-10, false, list.get(0));
        assertEndPoint(-8, false, list.get(1));
        assertEndPoint(-7, false, list.get(2));
        assertEndPoint(-6, true, list.get(3));
        assertEndPoint(10, true, list.get(4));
        assertEndPoint(20, true, list.get(5));
    }
}