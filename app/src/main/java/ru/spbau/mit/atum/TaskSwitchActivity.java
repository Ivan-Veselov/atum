package ru.spbau.mit.atum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TaskSwitchActivity extends AppCompatActivity {

    private int FIXED_TASK_CODE = 0;

    private int GENERAL_TASK_CODE = 1;

    private int ADDITIONAL_TASK_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_switch);
    }

    public void onGeneralTaskButtonClick(View view) {
        Intent intent = new Intent(this, TaskEditorActivity.class);
        if (getIntent().getExtras() != null) intent.putExtras(getIntent().getExtras());

        startActivityForResult(intent, GENERAL_TASK_CODE);
    }

    public void onFixedTaskButtonClick(View view) {
        Intent intent = new Intent(this, FixedTaskEditorActivity.class);
        if (getIntent().getExtras() != null) intent.putExtras(getIntent().getExtras());

        startActivityForResult(intent, FIXED_TASK_CODE);
    }

    public void onAdditionalTaskButtonClick(View view) {
        Intent intent = new Intent(this, AdditionalTaskEditorActivity.class);
        if (getIntent().getExtras() != null) intent.putExtras(getIntent().getExtras());

        startActivityForResult(intent, ADDITIONAL_TASK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}