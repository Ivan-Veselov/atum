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

        tasks = userPreferences.getTaskList();
        blockers = userPreferences.getBlockerList();
    }

    private final int TASK_CODE = 0;
    private final int BLOCKER_CODE = 1;

    public void onTaskListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter holders", (Serializable)tasks);
        intent.putExtra("filter holder type", TASK_CODE);
        startActivityForResult(intent, TASK_CODE);
    }

    public void onBlockerListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter holders", (Serializable)blockers);
        intent.putExtra("filter holder type", BLOCKER_CODE);
        startActivityForResult(intent, BLOCKER_CODE);
    }

    public void onSchedulePlannerClick(View view) {
        SchedulePlanner.planSchedule(userPreferences, new DateTime(), new DateTime().plus(365L * 24 * 60 * 60 * 1000));
        Toast.makeText(getApplicationContext(), "SCHEDULE PLANNED", Toast.LENGTH_SHORT).show();
    }

    public void onViewScheduleClick(View view) {
        Intent intent = new Intent(this, SchedulerViewerActivity.class);
        intent.putExtra("all tasks", (Serializable)tasks);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TASK_CODE && resultCode == RESULT_OK) {
            tasks = (ArrayList<UserDefinedTask>)data.getSerializableExtra("filter holders");
        }
        if (requestCode == BLOCKER_CODE && resultCode == RESULT_OK) {
            blockers = (ArrayList<UserDefinedTimeBlocker>)data.getSerializableExtra("filter holders");
        }
    }
}
