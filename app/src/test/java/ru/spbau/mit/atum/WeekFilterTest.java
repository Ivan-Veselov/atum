package ru.spbau.mit.atum;

import org.junit.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.spbau.mit.atum.WeekFilter.DaysOfWeek;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import static ru.spbau.mit.atum.TestUtilities.theNthOfJan;
import static ru.spbau.mit.atum.TimeFilter.ExclusionType.COMMON;
import static ru.spbau.mit.atum.TimeFilter.ExclusionType.EXCLUSIONARY;
import static ru.spbau.mit.atum.WeekFilter.DaysOfWeek.FRIDAY;
import static ru.spbau.mit.atum.WeekFilter.DaysOfWeek.MONDAY;
import static ru.spbau.mit.atum.WeekFilter.DaysOfWeek.SATURDAY;
import static ru.spbau.mit.atum.WeekFilter.DaysOfWeek.SUNDAY;
import static ru.spbau.mit.atum.WeekFilter.DaysOfWeek.THURSDAY;
import static ru.spbau.mit.atum.WeekFilter.DaysOfWeek.TUESDAY;
import static ru.spbau.mit.atum.WeekFilter.DaysOfWeek.WEDNESDAY;

public class WeekFilterTest {
    @Test
    public void testConstructor1() throws Exception {
        new WeekFilter("desc", 60, 10, EnumSet.range(MONDAY, SUNDAY), COMMON);
        new WeekFilter("desc", 60 * 23, 300, EnumSet.range(THURSDAY, SATURDAY), COMMON);
        new WeekFilter("desc", 60 * 20, 101, EnumSet.allOf(DaysOfWeek.class), EXCLUSIONARY);
        new WeekFilter("desc", 65, 60 * 24, EnumSet.of(MONDAY), COMMON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() throws Exception {
        new WeekFilter("desc", 0, 60 * 25, EnumSet.of(MONDAY), COMMON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3() throws Exception {
        new WeekFilter("desc", 0, -25, EnumSet.of(MONDAY), COMMON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor4() throws Exception {
        new WeekFilter("desc", -1, 10, EnumSet.of(MONDAY), COMMON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor5() throws Exception {
        new WeekFilter("desc", 24 * 60, 10, EnumSet.of(MONDAY), COMMON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor6() throws Exception {
        new WeekFilter("desc", 30 * 60, 10, EnumSet.of(MONDAY), COMMON);
    }

    @Test
    public void testGetters() throws Exception {
        WeekFilter filter = new WeekFilter("desc", 10, 111,
                EnumSet.of(MONDAY, WEDNESDAY, THURSDAY, SUNDAY), EXCLUSIONARY);

        assertEquals("desc", filter.getDescription());
        assertEquals(10, filter.getFirstMinute());
        assertEquals(111, filter.getDuration());
        assertEquals(EXCLUSIONARY, filter.exclusionType());

        EnumSet<DaysOfWeek> mask = filter.getWeekMask();
        assertEquals(true, mask.contains(MONDAY));
        assertEquals(false, mask.contains(TUESDAY));
        assertEquals(true, mask.contains(WEDNESDAY));
        assertEquals(true, mask.contains(THURSDAY));
        assertEquals(false, mask.contains(FRIDAY));
        assertEquals(false, mask.contains(SATURDAY));
        assertEquals(true, mask.contains(SUNDAY));
    }

    private int[] intervalsToArray(List<Interval> list) {
        int[] array = new int[list.size() * 2];
        for (int i = 0; i < list.size(); i++) {
            array[2 * i] = list.get(i).left();
            array[2 * i + 1] = list.get(i).right();
        }

        return array;
    }

    @Test
    public void testIntervalRepresentation() throws Exception {
        int duration = 65;
        WeekFilter filter = new WeekFilter("desc", 12 * 60, duration,
                EnumSet.of(TUESDAY, SATURDAY), COMMON);

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