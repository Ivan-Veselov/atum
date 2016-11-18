package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNewTaskClick(View view) {
        Intent intent = new Intent(this, TaskEditorActivity.class);
        startActivity(intent);
    }

    public void onTaskListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);

        List<UserDefinedTask> tasks = new ArrayList<>();
        WeekFilter weekFilter1 = new WeekFilter(10 * 60, 90, new WeekMask(1, 2, 4), false);
        WeekFilter weekFilter2 = new WeekFilter(10 * 60, 120, new WeekMask(1, 3, 5), false);
        List<TimeFilter> filters = new ArrayList<>();
        filters.add(weekFilter1);
        filters.add(weekFilter2);

        UserDefinedTask task1 = new UserDefinedTask("first", "", filters, 50);
        UserDefinedTask task2 = new UserDefinedTask("second", "", filters, 60);

        tasks.add(task1);
        tasks.add(task2);

        intent.putExtra("tasks", (Serializable)tasks);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            List<UserDefinedTask> tasks = (ArrayList<UserDefinedTask>)data.getSerializableExtra("tasks");
        }
    }
}
