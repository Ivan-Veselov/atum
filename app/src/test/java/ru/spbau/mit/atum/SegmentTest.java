package ru.spbau.mit.atum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SegmentTest {
    /**
     * Проверяет работоспособность конструктора. Последний вызов должен выбросить исключение.
     *
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() throws Exception {
        new Segment(1, 2);
        new Segment(0, 0);

        // Тут должно вылететь исключение
        new Segment(2, 1);
    }

    /**
     * Проверяет работоспособность методов доступа к полям.
     *
     * @throws Exception
     */
    @Test
    public void testGetters() throws Exception {
        Segment segment = new Segment(-1, 2);

        assertEquals(-1, segment.begin());
        assertEquals(2, segment.end());
    }
}