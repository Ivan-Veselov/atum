package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static ru.spbau.mit.atum.PlacesUtils.setBuilderPositionNearPlace;
import static ru.spbau.mit.atum.UserDefinedTask.DEFAULT_REST_DURATION;


/**
 * Activity, в которой пользователь редактирует или создает объект типа UserDefinedTask.
 */
public class TaskEditorActivity extends AbstractFiltersHolderEditorActivity {
    private static final String STATE_CHOSEN_PLACE = "CHOSEN_PLACE";

    private EditText durationField;

    private TextView placeTextView;

    private Place chosenPlace = null;

    private static final int PLACE_PICKER_REQUEST = 10;

    private EditText restDuration;

    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_task_editor);

        nameField = (EditText) findViewById(R.id.task_editor_name_field);
        descriptionField = (EditText) findViewById(R.id.task_editor_description_field);
        durationField = (EditText) findViewById(R.id.task_editor_duration_field);
        timeFilterListView = (ListView) findViewById(R.id.task_editor_filter_list);
        placeTextView = (TextView) findViewById(R.id.task_editor_location_text);
        restDuration = (EditText) findViewById(R.id.task_editor_rest_duration);

        restDuration.setHint(((Integer)DEFAULT_REST_DURATION).toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserDefinedTask taskToEdit = getIntent().getParcelableExtra(EXTRA_FILTER_HOLDER);

        if (savedInstanceState != null) {
            chosenPlace = savedInstanceState.getParcelable(STATE_CHOSEN_PLACE);
        } else if (taskToEdit != null) {
            durationField.setText(Integer.toString(taskToEdit.getDuration()));
            restDuration.setText(((Integer)taskToEdit.getRestDuration()).toString());

            chosenPlace = taskToEdit.getPlace();

            if (chosenPlace != null) {
                placeTextView.setText(chosenPlace.getAddress());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_CHOSEN_PLACE, (Parcelable) chosenPlace);
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

        UserDefinedTask task = new UserDefinedTask(nameField.getText().toString(),
                descriptionField.getText().toString(),
                timeFilters,
                duration,
                chosenPlace);

        if (!restDuration.getText().toString().isEmpty()) {
            task.setRestDuration(Integer.parseInt(restDuration.getText().toString()));
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
