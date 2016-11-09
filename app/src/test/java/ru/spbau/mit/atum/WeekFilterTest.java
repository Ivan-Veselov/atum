package ru.spbau.mit.atum;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static ru.spbau.mit.atum.TestUtilities.theNthOfJan;

public class WeekFilterTest {
    @Test
    public void testConstructor1() throws Exception {
        new WeekFilter(60, 10, new WeekMask(0, 1, 2), false);
        new WeekFilter(60 * 23, 300, new WeekMask(3, 4, 5), false);
        new WeekFilter(60 * 20, 101, new WeekMask(0, 1, 2, 3, 4, 5, 6), true);
        new WeekFilter(65, 60 * 24, new WeekMask(0), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() throws Exception {
        new WeekFilter(0, 60 * 25, new WeekMask(0), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3() throws Exception {
        new WeekFilter(0, -25, new WeekMask(0), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor4() throws Exception {
        new WeekFilter(-1, 10, new WeekMask(0), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor5() throws Exception {
        new WeekFilter(24 * 60, 10, new WeekMask(0), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor6() throws Exception {
        new WeekFilter(30 * 60, 10, new WeekMask(0), false);
    }

    @Test
    public void testGetters() throws Exception {
        WeekFilter filter = new WeekFilter(10, 111, new WeekMask(0, 2, 3, 6), true);

        assertEquals(10, filter.getFirstMinute());
        assertEquals(111, filter.getDuration());
        assertEquals(true, filter.isExclusive());

        WeekMask mask = filter.getWeekMask();
        assertEquals(true, mask.isSet(0));
        assertEquals(false, mask.isSet(1));
        assertEquals(true, mask.isSet(2));
        assertEquals(true, mask.isSet(3));
        assertEquals(false, mask.isSet(4));
        assertEquals(false, mask.isSet(5));
        assertEquals(true, mask.isSet(6));
    }

    private int[] intervalsToArray(List<Interval> list) {
        int[] array = new int[list.size() * 2];
        for (int i = 0; i < list.size(); i++) {
            array[2 * i] = list.get(i).begin();
            array[2 * i + 1] = list.get(i).end();
        }

        return array;
    }

    @Test
    public void testIntervalRepresentation() throws Exception {
        int duration = 65;
        WeekFilter filter = new WeekFilter(12 * 60, duration, new WeekMask(1, 5), false);

        // вт(4, 13:00-13:05)/сб(8, 12:00-13:05)/вт(11, 12:00-13:05)/сб(15, 12:00-12:31)
        List<Interval> list = filter.intervalRepresentation(theNthOfJan(4, 13, 0),
                                                            theNthOfJan(15, 12, 31));

        int the8 = (int) TimeUnit.DAYS.toMinutes(4) - 60;
        int the11 = (int) TimeUnit.DAYS.toMinutes(7) - 60;
        int the15 = (int) TimeUnit.DAYS.toMinutes(11) - 60;
        assertArrayEquals(new int[] {0, 5,
                                     the8, the8 + duration,
                                     the11, the11 + duration,
                                     the15, the15 + 31},
                          intervalsToArray(list));
    }
}