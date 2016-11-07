package ru.spbau.mit.atum;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IntervalFilterTest {
    private Calendar theFirstOfJan(int hour, int minute) {
        return new GregorianCalendar(2000, 0, 1, hour, minute, 0);
    }

    @Test
    public void testConstructor1() throws Exception {
        new IntervalFilter(theFirstOfJan(0, 0), theFirstOfJan(12, 0), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() throws Exception {
        new IntervalFilter(theFirstOfJan(12, 0), theFirstOfJan(0, 0), false);
    }

    /**
     * Вспомогательный метод, который тестирует методы доступа
     */
    private void testGettersOn(Calendar initialMoment,
                               Calendar finalMoment, boolean exclusiveFlag) throws Exception {

        IntervalFilter filter = new IntervalFilter(initialMoment, finalMoment, exclusiveFlag);

        assertEquals(exclusiveFlag, filter.isExclusive());
        assertEquals(initialMoment, filter.getInitialMoment());
        assertEquals(finalMoment, filter.getFinalMoment());
    }

    @Test
    public void testGetters() throws Exception {
        testGettersOn(theFirstOfJan(0, 0), theFirstOfJan(23, 22), false);
        testGettersOn(theFirstOfJan(12, 0), theFirstOfJan(22, 23), true);
    }

    private void testIntervalRepresentationOn(Calendar filterInitialMoment,
                                              Calendar filterFinalMoment,
                                              boolean exclusiveFlag,
                                              Calendar initialMoment,
                                              Calendar finalMoment,
                                              int begin,
                                              int end) throws Exception {
        IntervalFilter filter = new IntervalFilter(filterInitialMoment,
                                                   filterFinalMoment, exclusiveFlag);

        List<DualInterval> list = filter.intervalRepresentation(initialMoment, finalMoment);
        assertEquals(1, list.size());

        DualInterval interval = list.get(0);
        assertEquals(begin, interval.begin());
        assertEquals(end, interval.end());
        assertEquals(exclusiveFlag, interval.isExclusive());
    }

    private void testIntervalRepresentationOnEmpty(Calendar filterInitialMoment,
                                                   Calendar filterFinalMoment,
                                                   boolean exclusiveFlag,
                                                   Calendar initialMoment,
                                                   Calendar finalMoment) throws Exception {
        IntervalFilter filter = new IntervalFilter(filterInitialMoment,
                filterFinalMoment, exclusiveFlag);

        List<DualInterval> list = filter.intervalRepresentation(initialMoment, finalMoment);
        assertEquals(0, list.size());
    }

    @Test
    public void testIntervalRepresentation() throws Exception {
        // Нет пересечения
        testIntervalRepresentationOnEmpty(theFirstOfJan(13, 0),
                                          theFirstOfJan(14, 0),
                                          false,
                                          theFirstOfJan(1, 0),
                                          theFirstOfJan(13, 0));

        testIntervalRepresentationOnEmpty(theFirstOfJan(1, 0),
                                          theFirstOfJan(2, 0),
                                          true,
                                          theFirstOfJan(3, 0),
                                          theFirstOfJan(10, 0));

        // Вложение
        testIntervalRepresentationOn(theFirstOfJan(2, 0),
                                     theFirstOfJan(3, 10),
                                     false,
                                     theFirstOfJan(1, 0),
                                     theFirstOfJan(4, 0),
                                     60, 130);

        testIntervalRepresentationOn(theFirstOfJan(3, 0),
                                     theFirstOfJan(5, 1),
                                     true,
                                     theFirstOfJan(3, 0),
                                     theFirstOfJan(12, 0),
                                     0, 121);

        // Частичное пересечение
        testIntervalRepresentationOn(theFirstOfJan(2, 0),
                                     theFirstOfJan(2, 20),
                                     false,
                                     theFirstOfJan(1, 0),
                                     theFirstOfJan(2, 10),
                                     60, 70);

        testIntervalRepresentationOn(theFirstOfJan(2, 0),
                                     theFirstOfJan(3, 20),
                                     true,
                                     theFirstOfJan(2, 55),
                                     theFirstOfJan(12, 0),
                                     0, 25);
    }
}