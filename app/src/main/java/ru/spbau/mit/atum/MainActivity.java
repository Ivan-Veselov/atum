package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.io.Serializable;

public class MainActivity extends UserDataEditorActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadUserData();
    }

    private final int TASK_CODE = 0;
    private final int BLOCKER_CODE = 1;

    public void onTaskListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter holder type", TASK_CODE);
        startActivity(intent);
    }

    public void onBlockerListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter holder type", BLOCKER_CODE);
        startActivity(intent);
    }

    public void onSchedulePlannerClick(View view) {
        SchedulePlanner.planSchedule(UserSynchronisableData.getInstance(), new DateTime(), new DateTime().plus(365L * 24 * 60 * 60 * 1000));
        Toast.makeText(getApplicationContext(), "SCHEDULE PLANNED", Toast.LENGTH_SHORT).show();
    }

    public void onViewScheduleClick(View view) {
        Intent intent = new Intent(this, SchedulerViewerActivity.class);
        intent.putExtra("all tasks", (Serializable) UserSynchronisableData.getInstance().getTasks());
        startActivity(intent);
    }
}
