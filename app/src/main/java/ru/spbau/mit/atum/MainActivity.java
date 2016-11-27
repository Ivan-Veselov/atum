package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserPreferences userPreferences = new UserPreferences();
    private List<UserDefinedTask> tasks;
    private List<UserDefinedTimeBlocker> blockers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeekFilter weekFilter1 = new WeekFilter(10 * 60, 90, new WeekMask(1, 2, 4), false);
        WeekFilter weekFilter2 = new WeekFilter(10 * 60, 120, new WeekMask(1, 3, 5), false);

        WeekFilter weekFilter3 = new WeekFilter(10 * 60, 90, new WeekMask(0, 6), false);
        WeekFilter weekFilter4 = new WeekFilter(10 * 60, 120, new WeekMask(0, 1), false);

        List<TimeFilter> taskFilters = new ArrayList<>();
        taskFilters.add(weekFilter1);
        taskFilters.add(weekFilter2);

        List<TimeFilter> blockerFilters = new ArrayList<>();
        blockerFilters.add(weekFilter3);
        blockerFilters.add(weekFilter4);

        tasks = userPreferences.getTaskList();
        blockers = userPreferences.getBlockerList();

        for (int i = 1; i <= 50; i++) {
            tasks.add(new UserDefinedTask("task #" + i, "", taskFilters, 5 * i));
        }

        for (int i = 1; i <= 20; i++) {
            blockers.add(new UserDefinedTimeBlocker("blocker #" + i, "", blockerFilters));
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

    public void onSchedulePlannerClick(View view) {
        SchedulePlanner.planSchedule(userPreferences, new DateTime(), new DateTime().plus(100L * 365 * 60 * 60 * 1000));

        Toast.makeText(getApplicationContext(), "SCHEDULE PLANNED", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            tasks = (ArrayList<UserDefinedTask>)data.getSerializableExtra("filter holders");
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            blockers = (ArrayList<UserDefinedTimeBlocker>)data.getSerializableExtra("filter holders");
        }
    }
}
