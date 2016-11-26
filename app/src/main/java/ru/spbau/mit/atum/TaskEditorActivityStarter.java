package ru.spbau.mit.atum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;

public class TaskEditorActivityStarter extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, TaskEditorActivity.class);

        intent.putExtra(TaskEditorActivity.EXTRA_TASK,
                        new UserDefinedTask("TaskName",
                                            "TaskDescription",
                                            new ArrayList<>(Arrays.asList(new TimeFilter[] {
                                                    new IntervalFilter(new DateTime(2000, 1, 1, 0, 10, 0),
                                                                       new DateTime(2000, 1, 1, 0, 55, 0),
                                                                       false),
                                                    new IntervalFilter(new DateTime(2000, 1, 1, 12, 0, 0),
                                                                       new DateTime(2000, 1, 1, 17, 0, 0),
                                                                       false)
                                            })),
                                            42));
        startActivity(intent);
    }
}
