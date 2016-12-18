package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Activity, в которой пользователь редактирует или создает объект типа UserDefinedTask.
 */
public class TaskEditorActivity extends AbstractFiltersHolderEditorActivity {
    private EditText durationField;

    private static final int PLACE_PICKER_REQUEST = 10;

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

    @Override
    protected @Nullable AbstractFiltersHolder buildFiltersHolder() {
        String durationString = durationField.getText().toString();
        if (durationString.isEmpty()) {
            Toast.makeText(this, "Duration must not be empty", Toast.LENGTH_LONG).show();
            return null;
        }

        int duration = Integer.parseInt(durationString);
        if (duration == 0) {
            Toast.makeText(this, "Duration must not be 0", Toast.LENGTH_LONG).show();
            return null;
        }

        return new UserDefinedTask(nameField.getText().toString(),
                                   descriptionField.getText().toString(),
                                   timeFilters,
                                   duration);
    }

    public void onClickLocationButton(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException |
                 GooglePlayServicesRepairableException e) {
            throw new RuntimeException(e);
        }
    }
}
