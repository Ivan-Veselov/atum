package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Activity, в которой пользователь редактирует или создает объект типа UserDefinedTask.
 */
public class TaskEditorActivity extends AbstractFiltersHolderEditorActivity {
    private EditText durationField;

    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_task_editor);

        nameField = (EditText) findViewById(R.id.task_editor_name_field);
        descriptionField = (EditText) findViewById(R.id.task_editor_description_field);
        durationField = (EditText) findViewById(R.id.task_editor_duration_field);
        timeFilterListView = (ListView) findViewById(R.id.task_editor_filter_list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserDefinedTask taskToEdit =
                (UserDefinedTask) getIntent().getSerializableExtra(EXTRA_FILTER_HOLDER);

        if (savedInstanceState == null && taskToEdit != null) {
            durationField.setText(Integer.toString(taskToEdit.getDuration()));
        }
    }

    public void onClickApplyButton(View view) {
        int duration = Integer.parseInt(durationField.getText().toString());
        if (duration == 0) {
            Toast.makeText(this, "Duration must not be 0", Toast.LENGTH_LONG).show();
            return;
        }

        UserDefinedTask resultingTask =
                new UserDefinedTask(nameField.getText().toString(),
                        descriptionField.getText().toString(),
                        timeFilters,
                        duration);

        Intent result = new Intent();
        result.putExtra(EXTRA_FILTER_HOLDER, resultingTask);
        setResult(RESULT_OK, result);
        finish();
    }
}
