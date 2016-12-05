package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity {

    private UserSynchronisableData userSynchronisableData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            userSynchronisableData = new UserSynchronisableData(this, "atum");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private final int TASK_CODE = 0;
    private final int BLOCKER_CODE = 1;

    public void onTaskListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter holders", (Serializable) userSynchronisableData.getTasks());
        intent.putExtra("filter holder type", TASK_CODE);
        startActivityForResult(intent, TASK_CODE);
    }

    public void onBlockerListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter holders", (Serializable) userSynchronisableData.getBlockers());
        intent.putExtra("filter holder type", BLOCKER_CODE);
        startActivityForResult(intent, BLOCKER_CODE);
    }

    public void onSchedulePlannerClick(View view) {
        SchedulePlanner.planSchedule(userSynchronisableData, new DateTime(), new DateTime().plusYears(5));
        Toast.makeText(getApplicationContext(), "SCHEDULE PLANNED", Toast.LENGTH_SHORT).show();
    }

    public void onViewScheduleClick(View view) {
        Intent intent = new Intent(this, SchedulerViewerActivity.class);
        intent.putExtra("all tasks", (Serializable) userSynchronisableData.getTasks());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TASK_CODE && resultCode == RESULT_OK) {
            userSynchronisableData.setTasks((ArrayList<UserDefinedTask>)data.getSerializableExtra("filter holders"));
        }
        if (requestCode == BLOCKER_CODE && resultCode == RESULT_OK) {
            userSynchronisableData.setBlockers((ArrayList<UserDefinedTimeBlocker>)data.getSerializableExtra("filter holders"));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            userSynchronisableData.saveData(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
