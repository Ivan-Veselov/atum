package ru.spbau.mit.atum.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;

import ru.spbau.mit.atum.model.IntervalFilter;
import ru.spbau.mit.atum.model.TimeFilter;
import ru.spbau.mit.atum.model.UserDefinedTask;

/**
 * Самопальный запускатель TaskEditorActivity для тестирования.
 */
public class TaskEditorActivityStarter extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, TaskEditorActivity.class);

        intent.putExtra(TaskEditorActivity.EXTRA_FILTER_HOLDER,
                        UserDefinedTask.newGeneralTask("TaskName",
                                            "TaskDescription",
                                            new ArrayList<>(Arrays.asList(new TimeFilter[] {
                                                    new IntervalFilter("First filter",
                                                                       new DateTime(2000, 1, 1, 0, 10, 0),
                                                                       new DateTime(2000, 1, 1, 0, 55, 0),
                                                                       TimeFilter.ExclusionType.COMMON),
                                                    new IntervalFilter("Second Filter",
                                                                       new DateTime(2000, 1, 1, 12, 0, 0),
                                                                       new DateTime(2000, 1, 1, 17, 0, 0),
                                                                       TimeFilter.ExclusionType.COMMON)
                                            })),
                                            42, null, 0));

        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                UserDefinedTask task = data.getParcelableExtra(TaskEditorActivity.EXTRA_FILTER_HOLDER);

                Toast.makeText(this, task.getName() + " " +
                        task.getFilterSet().getFilterList().size(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
