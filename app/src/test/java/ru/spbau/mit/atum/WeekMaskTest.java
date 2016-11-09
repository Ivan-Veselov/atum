package ru.spbau.mit.atum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeekMaskTest {
    @Test
    public void testConstructor1() throws Exception {
        new WeekMask();
        new WeekMask(0, 1);
        new WeekMask(1, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() throws Exception {
        new WeekMask(7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3() throws Exception {
        new WeekMask(-1);
    }

    @Test
    public void testIsSet1() throws Exception {
        WeekMask mask = new WeekMask(1, 3, 5);

        assertEquals(false, mask.isSet(0));
        assertEquals(true, mask.isSet(1));
        assertEquals(false, mask.isSet(2));
        assertEquals(true, mask.isSet(3));
        assertEquals(false, mask.isSet(4));
        assertEquals(true, mask.isSet(5));
        assertEquals(false, mask.isSet(6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSet2() throws Exception {
        WeekMask mask = new WeekMask(1, 3, 5);
        mask.isSet(7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSet3() throws Exception {
        WeekMask mask = new WeekMask(1, 3, 5);
        mask.isSet(-1);
    }
}