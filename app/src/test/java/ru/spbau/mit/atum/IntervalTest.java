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

    private void assertInterval(int left, int right, Interval interval) throws Exception {
        assertEquals(left, interval.left());
        assertEquals(right, interval.right());
    }

    private void assertIntervalList(int[] lefts, int[] rights,
                                    List<Interval> list) throws Exception {
        if (lefts.length != rights.length) {
            throw new TestUtilities.InvalidTestException();
        }

        assertEquals(lefts.length, list.size());

        for (int i = 0; i < list.size(); ++i) {
            assertInterval(lefts[i], rights[i], list.get(i));
        }
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
        assertInterval(resultIntervalLeftEnd, resultIntervalRightEnd, interval);
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

    private void assertEndPointList(int[] coordinates, boolean[] rightFlags,
                                    List<Interval.EndPoint> list) throws Exception {
        if (coordinates.length != rightFlags.length) {
            throw new TestUtilities.InvalidTestException();
        }

        assertEquals(coordinates.length, list.size());

        for (int i = 0; i < list.size(); ++i) {
            assertEndPoint(coordinates[i], rightFlags[i], list.get(i));
        }
    }

    @Test
    public void testEndPoints1() throws Exception {
        List<Interval.EndPoint> list = Interval.endPoints(new ArrayList<Interval>());
        assertEndPointList(new int[] {}, new boolean[] {}, list);
    }

    @Test
    public void testEndPoints2() throws Exception {
        List<Interval.EndPoint> list = Interval.endPoints(Arrays.asList(new Interval(-10, 10),
                                                                        new Interval(-8, -6),
                                                                        new Interval(-7, 20)));

        Collections.sort(list);
        assertEndPointList(new int[] {-10, -8, -7, -6, 10, 20},
                           new boolean[] {false, false, false, true, true, true},
                           list);
        }

    @Test
    public void testEndPoints3() throws Exception {
        // Тест на дубликаты

        List<Interval.EndPoint> list = Interval.endPoints(Arrays.asList(new Interval(-1, 5),
                                                                        new Interval(-1, 5)));

        Collections.sort(list);
        assertEndPointList(new int[] {-1, -1, 5, 5},
                           new boolean [] {false, false, true, true},
                           list);
    }

    @Test
    public void testEndPoints4() throws Exception {
        // Сортировка

        List<Interval.EndPoint> list = Interval.endPoints(Arrays.asList(new Interval(-1, 5),
                new Interval(5, 10)));

        Collections.sort(list);
        assertEndPointList(new int[] {-1, 5, 5, 10},
                           new boolean[] {false, false, true, true}, list);
    }

    @Test
    public void testNormalize() throws Exception {
        List<Interval> list = Interval.normalize(Arrays.asList(new Interval(0, 3),
                                                               new Interval(2, 5),
                                                               new Interval(9, 10),
                                                               new Interval(8, 10)));

        assertIntervalList(new int[] {0, 8},
                           new int[] {5, 10},
                           list);
    }
}