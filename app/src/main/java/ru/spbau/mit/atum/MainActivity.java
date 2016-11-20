package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<AbstractFiltersHolder> tasks = new ArrayList<>();
    private List<AbstractFiltersHolder> blockers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeekFilter weekFilter1 = new WeekFilter(10 * 60, 90, new WeekMask(1, 2, 4), false);
        WeekFilter weekFilter2 = new WeekFilter(10 * 60, 120, new WeekMask(1, 3, 5), false);
        List<TimeFilter> filters = new ArrayList<>();
        filters.add(weekFilter1);
        filters.add(weekFilter2);

        for (int i = 1; i <= 50; i++) {
            tasks.add(new UserDefinedTask("task #" + i, "", filters, 5 * i));
        }

        for (int i = 1; i <= 20; i++) {
            blockers.add(new UserDefinedTimeBlocker("blocker #" + i, "", filters));
        }

    }

    private final int TASK_CODE = 0;
    private final int BLOCKER_CODE = 1;

    public void onTaskListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter holders", (Serializable)tasks);
        startActivityForResult(intent, TASK_CODE);
    }

    public void onBlockerListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter holders", (Serializable)blockers);
        startActivityForResult(intent, BLOCKER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            tasks = (ArrayList<AbstractFiltersHolder>)data.getSerializableExtra("filter holders");
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            blockers = (ArrayList<AbstractFiltersHolder>)data.getSerializableExtra("filter holders");
        }
    }
}
