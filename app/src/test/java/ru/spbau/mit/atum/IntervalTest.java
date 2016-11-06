package ru.spbau.mit.atum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntervalTest {
    /**
     * Проверяет работоспособность конструктора. Последний вызов должен выбросить исключение.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() throws Exception {
        new Interval(1, 2);
        new Interval(0, 0);

        // Тут должно вылететь исключение
        new Interval(2, 1);
    }

    /**
     * Проверяет работоспособность методов доступа к полям.
     */
    @Test
    public void testGetters() throws Exception {
        Interval interval = new Interval(-1, 2);

        assertEquals(-1, interval.begin());
        assertEquals(2, interval.end());
    }
}