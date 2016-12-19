package ru.spbau.mit.atum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Самопальный запускатель TimeBlockerEditorActivity для тестирования.
 */
public class TimeBlockerEditorActivityStarter extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, TimeBlockerEditorActivity.class);

        intent.putExtra(TimeBlockerEditorActivity.EXTRA_FILTER_HOLDER,
                new UserDefinedTimeBlocker("BlockerName",
                        "BlockerDescription",
                        new ArrayList<>(Arrays.asList(new TimeFilter[] {
                                new IntervalFilter("Some Filter",
                                        new DateTime(2000, 1, 1, 0, 10, 0),
                                        new DateTime(2000, 1, 1, 0, 55, 0),
                                        TimeFilter.ExclusionType.COMMON),
                                new IntervalFilter("Another Filter",
                                        new DateTime(2000, 1, 1, 12, 0, 0),
                                        new DateTime(2000, 1, 1, 17, 0, 0),
                                        TimeFilter.ExclusionType.COMMON)
                        }))));

        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                UserDefinedTimeBlocker task = data.getParcelableExtra(TaskEditorActivity.EXTRA_FILTER_HOLDER);

                Toast.makeText(this, task.getName() + " " + task.getFilterList().size(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
