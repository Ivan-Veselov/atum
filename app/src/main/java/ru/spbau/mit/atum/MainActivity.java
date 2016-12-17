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

    public void onTaskListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        startActivity(intent);
    }

    public void onBlockerListClick(View view) {
        Intent intent = new Intent(this, TimeBlockerListActivity.class);
        startActivity(intent);
    }

    public void onSchedulePlannerClick(View view) {
        SchedulePlanner.planSchedule(UserSynchronisableData.getInstance(), new DateTime(), new DateTime().plusYears(5));
        Toast.makeText(getApplicationContext(), "SCHEDULE PLANNED", Toast.LENGTH_SHORT).show();
    }

    public void onViewScheduleClick(View view) {
        Intent intent = new Intent(this, SchedulerViewerActivity.class);
        intent.putExtra("all tasks", (Serializable) UserSynchronisableData.getInstance().getTasks());
        startActivity(intent);
    }
}
