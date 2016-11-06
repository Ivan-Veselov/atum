package ru.spbau.mit.atum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DualIntervalTest {
    /**
     * Проверяет работоспособность методов доступа к полям.
     */
    @Test
    public void testGetters() throws Exception {
        DualInterval normalInterval = new DualInterval(1, 2, false);
        DualInterval exclusiveInterval = new DualInterval(1, 2, true);

        assertEquals(false, normalInterval.isExclusive());
        assertEquals(true, exclusiveInterval.isExclusive());
    }
}