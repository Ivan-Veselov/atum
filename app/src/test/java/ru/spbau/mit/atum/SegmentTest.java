package ru.spbau.mit.atum;

import org.junit.Test;

import static org.junit.Assert.*;

public class SegmentTest {
    @Test
    public void testConstructor1() throws Exception {
        Segment segment1 = new Segment(1, 2);
        Segment segment2 = new Segment(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() throws Exception {
        Segment segment = new Segment(2, 1);
    }

    @Test
    public void testGetters() throws Exception {
        Segment segment = new Segment(-1, 2);

        assertEquals(-1, segment.begin());
        assertEquals(2, segment.end());
    }
}