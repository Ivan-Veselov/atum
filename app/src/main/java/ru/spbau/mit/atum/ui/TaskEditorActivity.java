package ru.spbau.mit.atum.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import ru.spbau.mit.atum.model.AbstractFiltersHolder;
import ru.spbau.mit.atum.R;
import ru.spbau.mit.atum.model.UserDefinedTask;

import static ru.spbau.mit.atum.model.PlacesUtils.setBuilderPositionNearPlace;
import static ru.spbau.mit.atum.model.UserDefinedTask.DEFAULT_REST_DURATION;


/**
 * Activity, в которой пользователь редактирует или создает объект типа UserDefinedTask.
 */
public class TaskEditorActivity extends AbstractFiltersHolderEditorActivity {
    private static final String STATE_CHOSEN_PLACE = "CHOSEN_PLACE";

    private static final String STATE_CHOSEN_PRIORITY = "CHOSEN_PRIORITY";

    private EditText durationField;

    private TextView placeTextView;

    private static final int PLACE_PICKER_REQUEST = 10;

    private EditText restDuration;

    private Place chosenPlace = null;

    private int chosenPriority = 1;

    private SeekBar seekBar;

    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_task_editor);

        View header = getLayoutInflater().inflate(R.layout.activity_task_editor_header, null);

        nameField = (EditText) header.findViewById(R.id.task_editor_name_field);
        descriptionField = (EditText) header.findViewById(R.id.task_editor_description_field);
        durationField = (EditText) header.findViewById(R.id.task_editor_duration_field);
        placeTextView = (TextView) header.findViewById(R.id.task_editor_location_text);
        restDuration = (EditText) header.findViewById(R.id.task_editor_rest_duration);
        seekBar = (SeekBar) header.findViewById(R.id.seek_bar);

        restDuration.setHint(((Integer)DEFAULT_REST_DURATION).toString());

        timeFilterListView = (ListView) findViewById(R.id.task_editor_filter_list);
        timeFilterListView.addHeaderView(header);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserDefinedTask taskToEdit = getIntent().getParcelableExtra(EXTRA_FILTER_HOLDER);

        if (savedInstanceState != null) {
            chosenPlace = savedInstanceState.getParcelable(STATE_CHOSEN_PLACE);
            chosenPriority = savedInstanceState.getInt(STATE_CHOSEN_PRIORITY);
        } else if (taskToEdit != null) {
            durationField.setText(Long.toString(taskToEdit.getDuration().getStandardMinutes()));
            restDuration.setText(((Long) taskToEdit.getRestDuration().getStandardMinutes()).toString());

            chosenPlace = taskToEdit.getPlace();
            chosenPriority = taskToEdit.getPriority();

            if (chosenPlace != null) {
                placeTextView.setText(chosenPlace.getAddress());
            }
        }

        seekBar.setProgress(chosenPriority - 1);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STATE_CHOSEN_PLACE, (Parcelable) chosenPlace);
        outState.putInt(STATE_CHOSEN_PRIORITY, chosenPriority);
    }

    @Override
    protected @Nullable
    AbstractFiltersHolder buildFiltersHolder() {
        String durationString = durationField.getText().toString();
        if (durationString.isEmpty()) {
            Toast.makeText(this, "Duration must not be empty", Toast.LENGTH_LONG).show();
            return null;
        }

        int duration;
        try {
            duration = Integer.parseInt(durationString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Incorrect duration", Toast.LENGTH_LONG).show();
            return null;
        }

        if (duration == 0) {
            Toast.makeText(this, "Duration must not be 0", Toast.LENGTH_LONG).show();
            return null;
        }

        UserDefinedTask task = UserDefinedTask.newGeneralTask(nameField.getText().toString(),
                descriptionField.getText().toString(),
                timeFilters,
                duration,
                chosenPlace,
                seekBar.getProgress() + 1);

        if (!restDuration.getText().toString().isEmpty()) {
            try {
                task.setRestDuration(Integer.parseInt(restDuration.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Incorrect rest duration", Toast.LENGTH_LONG).show();
                return null;
            }
        } else {
            task.setRestDuration(DEFAULT_REST_DURATION);
        }

        return task;
    }

    public void onClickLocationButton(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        if (chosenPlace != null) {
            setBuilderPositionNearPlace(builder, chosenPlace);
        }

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException |
                 GooglePlayServicesRepairableException e) {
            throw new RuntimeException(e);
        }
    }

    public void onClearLocationButtonClick(View view) {
        chosenPlace = null;
        placeTextView.setText(R.string.location);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    chosenPlace = PlacePicker.getPlace(this, data);
                    placeTextView.setText(chosenPlace.getAddress());
                }

                break;

            default:
                break;
        }
    }
}
