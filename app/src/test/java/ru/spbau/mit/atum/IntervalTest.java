package ru.spbau.mit.atum;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import ru.spbau.mit.atum.Interval.EndPoint.Type;

import static org.junit.Assert.assertEquals;
import static ru.spbau.mit.atum.TestUtilities.assertIntervalEquals;
import static ru.spbau.mit.atum.TestUtilities.assertIntervalListEquals;

import static ru.spbau.mit.atum.Interval.EndPoint.Type.LEFT;
import static ru.spbau.mit.atum.Interval.EndPoint.Type.RIGHT;

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
        assertEquals(LEFT, left.getType());

        Interval.EndPoint right = interval.rightEndPoint();
        assertEquals(-10, right.getCoordinate());
        assertEquals(RIGHT, right.getType());
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
        assertIntervalEquals(resultIntervalLeftEnd, resultIntervalRightEnd, interval);
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
                                Type type,
                                Interval.EndPoint endPoint) throws Exception{
        assertEquals(coordinate, endPoint.getCoordinate());
        assertEquals(type, endPoint.getType());
    }

    private void assertEndPointList(int[] coordinates, Type[] types,
                                    List<Interval.EndPoint> list) throws Exception {
        if (coordinates.length != types.length) {
            throw new TestUtilities.InvalidTestException();
        }

        assertEquals(coordinates.length, list.size());

        for (int i = 0; i < list.size(); ++i) {
            assertEndPoint(coordinates[i], types[i], list.get(i));
        }
    }

    @Test
    public void testEndPoints1() throws Exception {
        List<Interval.EndPoint> list = Interval.endPoints(new ArrayList<Interval>());
        assertEndPointList(new int[] {}, new Type[] {}, list);
    }

    @Test
    public void testEndPoints2() throws Exception {
        List<Interval.EndPoint> list = Interval.endPoints(Arrays.asList(new Interval(-10, 10),
                                                                        new Interval(-8, -6),
                                                                        new Interval(-7, 20)));

        Collections.sort(list);
        assertEndPointList(new int[] {-10, -8, -7, -6, 10, 20},
                           new Type[] {LEFT, LEFT, LEFT, RIGHT, RIGHT, RIGHT},
                           list);
        }

    @Test
    public void testEndPoints3() throws Exception {
        // Тест на дубликаты

        List<Interval.EndPoint> list = Interval.endPoints(Arrays.asList(new Interval(-1, 5),
                                                                        new Interval(-1, 5)));

        Collections.sort(list);
        assertEndPointList(new int[] {-1, -1, 5, 5},
                           new Type[] {LEFT, LEFT, RIGHT, RIGHT},
                           list);
    }

    @Test
    public void testEndPoints4() throws Exception {
        // Сортировка

        List<Interval.EndPoint> list = Interval.endPoints(Arrays.asList(new Interval(-1, 5),
                new Interval(5, 10)));

        Collections.sort(list);
        assertEndPointList(new int[] {-1, 5, 5, 10},
                           new Type[] {LEFT, LEFT, RIGHT, RIGHT}, list);
    }

    @Test
    public void testNormalize() throws Exception {
        List<Interval> list = Interval.normalize(Arrays.asList(new Interval(0, 3),
                                                               new Interval(2, 5),
                                                               new Interval(9, 10),
                                                               new Interval(8, 10)));

        assertIntervalListEquals(new int[] {0, 8},
                                 new int[] {5, 10},
                                 list);
    }

    @Test
    public void testDifference1() throws Exception {
        List<Interval> list = Interval.difference(Arrays.asList(new Interval(5, 10)),
                                                  Arrays.asList(new Interval(0, 1),
                                                                new Interval(2, 3),
                                                                new Interval(4, 5)));

        assertIntervalListEquals(new int[] {5},
                                 new int[] {10},
                                 list);
    }

    @Test
    public void testDifference2() throws Exception {
        List<Interval> list = Interval.difference(Arrays.asList(new Interval(5, 10)),
                                                  Arrays.asList(new Interval(3, 7)));

        assertIntervalListEquals(new int[] {7},
                                 new int[] {10},
                                 list);
    }

    @Test
    public void testDifference3() throws Exception {
        List<Interval> list = Interval.difference(Arrays.asList(new Interval(5, 10)),
                                                  Arrays.asList(new Interval(6, 7),
                                                                new Interval(8, 9)));

        assertIntervalListEquals(new int[] {5, 7, 9},
                                 new int[] {6, 8, 10},
                                 list);
    }

    @Test
    public void testDifference4() throws Exception {
        List<Interval> list = Interval.difference(Arrays.asList(new Interval(5, 10)),
                                                  Arrays.asList(new Interval(8, 13)));

        assertIntervalListEquals(new int[] {5},
                                 new int[] {8},
                                 list);
    }

    @Test
    public void testDifference5() throws Exception {
        List<Interval> list = Interval.difference(Arrays.asList(new Interval(5, 10)),
                                                  Arrays.asList(new Interval(3, 10)));

        assertIntervalListEquals(new int[] {},
                                 new int[] {},
                                 list);
    }

    @Test
    public void testDifference6() throws Exception {
        List<Interval> list = Interval.difference(Arrays.asList(new Interval(5, 10)),
                                                  Arrays.asList(new Interval(12, 13),
                                                                new Interval(14, 15)));

        assertIntervalListEquals(new int[] {5},
                                 new int[] {10},
                                 list);
    }

    @Test
    public void testDifference7() throws Exception {
        List<Interval> list = Interval.difference(Arrays.asList(new Interval(2, 6),
                                                                new Interval(7, 10)),
                                                  Arrays.asList(new Interval(0, 2),
                                                                new Interval(3, 4),
                                                                new Interval(5, 8)));

        assertIntervalListEquals(new int[] {2, 4, 8},
                                 new int[] {3, 5, 10},
                                 list);
    }
}