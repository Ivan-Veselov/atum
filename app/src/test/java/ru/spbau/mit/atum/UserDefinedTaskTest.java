package ru.spbau.mit.atum;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import ru.spbau.mit.atum.model.IntervalFilter;
import ru.spbau.mit.atum.model.TimeFilter;
import ru.spbau.mit.atum.model.UserDefinedTask;

import static ru.spbau.mit.atum.TestUtilities.assertIntervalListEquals;
import static ru.spbau.mit.atum.TestUtilities.theFirstOfJan;
import static ru.spbau.mit.atum.model.TimeFilter.ExclusionType.COMMON;
import static ru.spbau.mit.atum.model.TimeFilter.ExclusionType.EXCLUSIONARY;

public class UserDefinedTaskTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor1() throws Exception {
        UserDefinedTask.newGeneralTask("Name", "Description", new ArrayList<TimeFilter>(), 0, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() throws Exception {
        UserDefinedTask.newGeneralTask("Name", "Description", new ArrayList<TimeFilter>(), -10, null, 0);
    }

    @Test
    public void testIntervalRepresentation() throws Exception {
        UserDefinedTask task = UserDefinedTask.newGeneralTask("Name", "Description",
                Arrays.asList(new TimeFilter[] {
                        new IntervalFilter("desc", theFirstOfJan(1, 0),
                                           theFirstOfJan(3, 0), COMMON),
                        new IntervalFilter("desc", theFirstOfJan(2, 0),
                                           theFirstOfJan(2, 30), EXCLUSIONARY)
                }), 1, null, 0);

        assertIntervalListEquals(new int[] {0, 60},
                                 new int[] {30, 90},
                                 task.intervalRepresentation(theFirstOfJan(1, 30),
                                                             theFirstOfJan(12, 0)));
    }
}