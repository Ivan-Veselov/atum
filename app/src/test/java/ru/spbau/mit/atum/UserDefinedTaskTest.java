package ru.spbau.mit.atum;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static ru.spbau.mit.atum.TestUtilities.assertIntervalListEquals;
import static ru.spbau.mit.atum.TestUtilities.theFirstOfJan;
import static ru.spbau.mit.atum.TestUtilities.theNthOfJan;

public class UserDefinedTaskTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor1() throws Exception {
        new UserDefinedTask("Name", "Description", new ArrayList<TimeFilter>(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() throws Exception {
        new UserDefinedTask("Name", "Description", new ArrayList<TimeFilter>(), -10);
    }

    @Test
    public void testIntervalRepresentation() throws Exception {
        UserDefinedTask task = new UserDefinedTask("Name", "Description",
                Arrays.asList(new TimeFilter[] {
                        new IntervalFilter(theFirstOfJan(1, 0), theFirstOfJan(3, 0), false),
                        new IntervalFilter(theFirstOfJan(2, 0), theFirstOfJan(2, 30), true)
                }), 1);

        assertIntervalListEquals(new int[] {0, 60},
                                 new int[] {30, 90},
                                 task.intervalRepresentation(theFirstOfJan(1, 30),
                                                             theFirstOfJan(12, 0)));
    }
}