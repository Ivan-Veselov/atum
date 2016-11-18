package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private List<String> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        final ArrayList<UserDefinedTask> userDefinedTaskList =
                (ArrayList<UserDefinedTask>)getIntent().getSerializableExtra("tasks");

        for (UserDefinedTask task: userDefinedTaskList) {
            taskList.add(task.getName());
        }

        ListView lvTaskList = (ListView)findViewById(R.id.task_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);

        lvTaskList.setAdapter(adapter);

        lvTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                UserDefinedTask task = userDefinedTaskList.get(position);
                Toast.makeText(getApplicationContext(), "duration is " + task.getDuration(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("tasks", (Serializable)taskList);

        setResult(RESULT_OK, intent);
        finish();
    }
}
