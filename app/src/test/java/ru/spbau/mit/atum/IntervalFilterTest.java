package ru.spbau.mit.atum;

import org.joda.time.base.AbstractDateTime;
import org.junit.Test;

import java.util.List;

import ru.spbau.mit.atum.model.IntervalFilter;
import ru.spbau.mit.atum.model.TimeFilter.ExclusionType;
import ru.spbau.mit.atum.model.Interval;

import static org.junit.Assert.assertEquals;
import static ru.spbau.mit.atum.TestUtilities.theFirstOfJan;
import static ru.spbau.mit.atum.model.TimeFilter.ExclusionType.COMMON;
import static ru.spbau.mit.atum.model.TimeFilter.ExclusionType.EXCLUSIONARY;

public class IntervalFilterTest {
    @Test
    public void testConstructor1() throws Exception {
        new IntervalFilter("description", theFirstOfJan(0, 0), theFirstOfJan(12, 0), COMMON);
        new IntervalFilter("description", theFirstOfJan(0, 0), theFirstOfJan(12, 0), EXCLUSIONARY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() throws Exception {
        new IntervalFilter("desc", theFirstOfJan(12, 0), theFirstOfJan(0, 0), COMMON);
    }

    /**
     * Вспомогательный метод, который тестирует методы доступа
     */
    private void testGettersOn(String description,
                               AbstractDateTime initialMoment,
                               AbstractDateTime finalMoment,
                               ExclusionType exclusionType) throws Exception {

        IntervalFilter filter = new IntervalFilter(description,
                                                   initialMoment,
                                                   finalMoment,
                                                   exclusionType);

        assertEquals(description, filter.getDescription());
        assertEquals(exclusionType, filter.exclusionType());
        assertEquals(initialMoment, filter.getInitialMoment());
        assertEquals(finalMoment, filter.getFinalMoment());
    }

    @Test
    public void testGetters() throws Exception {
        testGettersOn("desc", theFirstOfJan(0, 0), theFirstOfJan(23, 22), COMMON);
        testGettersOn("", theFirstOfJan(12, 0), theFirstOfJan(22, 23), EXCLUSIONARY);
    }

    private void testIntervalRepresentationOn(AbstractDateTime filterInitialMoment,
                                              AbstractDateTime filterFinalMoment,
                                              ExclusionType exclusionType,
                                              AbstractDateTime initialMoment,
                                              AbstractDateTime finalMoment,
                                              int begin,
                                              int end) throws Exception {
        IntervalFilter filter = new IntervalFilter("desc", filterInitialMoment,
                                                   filterFinalMoment, exclusionType);

        List<Interval> list = filter.intervalRepresentation(initialMoment, finalMoment);
        assertEquals(1, list.size());

        Interval interval = list.get(0);
        assertEquals(begin, interval.left());
        assertEquals(end, interval.right());
    }

    private void testIntervalRepresentationOnEmpty(AbstractDateTime filterInitialMoment,
                                                   AbstractDateTime filterFinalMoment,
                                                   ExclusionType exclusionType,
                                                   AbstractDateTime initialMoment,
                                                   AbstractDateTime finalMoment) throws Exception {
        IntervalFilter filter = new IntervalFilter("desc", filterInitialMoment,
                filterFinalMoment, exclusionType);

        List<Interval> list = filter.intervalRepresentation(initialMoment, finalMoment);
        assertEquals(0, list.size());
    }

    @Test
    public void testIntervalRepresentation() throws Exception {
        // Нет пересечения
        testIntervalRepresentationOnEmpty(theFirstOfJan(13, 0),
                                          theFirstOfJan(14, 0),
                                          COMMON,
                                          theFirstOfJan(1, 0),
                                          theFirstOfJan(13, 0));

        testIntervalRepresentationOnEmpty(theFirstOfJan(1, 0),
                                          theFirstOfJan(2, 0),
                                          EXCLUSIONARY,
                                          theFirstOfJan(3, 0),
                                          theFirstOfJan(10, 0));

        // Вложение
        testIntervalRepresentationOn(theFirstOfJan(2, 0),
                                     theFirstOfJan(3, 10),
                                     COMMON,
                                     theFirstOfJan(1, 0),
                                     theFirstOfJan(4, 0),
                                     60, 130);

        testIntervalRepresentationOn(theFirstOfJan(3, 0),
                                     theFirstOfJan(5, 1),
                                     EXCLUSIONARY,
                                     theFirstOfJan(3, 0),
                                     theFirstOfJan(12, 0),
                                     0, 121);

        // Частичное пересечение
        testIntervalRepresentationOn(theFirstOfJan(2, 0),
                                     theFirstOfJan(2, 20),
                                     COMMON,
                                     theFirstOfJan(1, 0),
                                     theFirstOfJan(2, 10),
                                     60, 70);

        testIntervalRepresentationOn(theFirstOfJan(2, 0),
                                     theFirstOfJan(3, 20),
                                     EXCLUSIONARY,
                                     theFirstOfJan(2, 55),
                                     theFirstOfJan(12, 0),
                                     0, 25);
    }
}