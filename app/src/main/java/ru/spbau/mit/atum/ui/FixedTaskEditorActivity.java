package ru.spbau.mit.atum.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.joda.time.DateTime;
import ru.spbau.mit.atum.model.IntervalFilter;
import ru.spbau.mit.atum.R;
import ru.spbau.mit.atum.model.UserDefinedTask;

import static ru.spbau.mit.atum.ui.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER;
import static ru.spbau.mit.atum.ui.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER_POSITION;
import static ru.spbau.mit.atum.model.PlacesUtils.setBuilderPositionNearPlace;
import static ru.spbau.mit.atum.model.UserDefinedTask.DEFAULT_REST_DURATION;

public class FixedTaskEditorActivity extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 0;

    private final int START_DATE = 1;
    private final int START_TIME = 2;
    private final int END_DATE = 3;
    private final int END_TIME = 4;

    private final DateTime currentDateTime = new DateTime();

    private DateTime start = currentDateTime;
    private DateTime end = currentDateTime;

    private TextView tvStartDate;
    private TextView tvStartTime;
    private TextView tvEndDate;
    private TextView tvEndTime;
    private EditText name;
    private EditText description;
    private EditText restDuration;
    private TextView placeTextView;

    private Place chosenPlace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_task_editor);

        tvStartDate = (TextView) findViewById(R.id.fixed_task_start_date);
        tvStartTime = (TextView) findViewById(R.id.fixed_task_start_time);
        tvEndDate = (TextView) findViewById(R.id.fixed_task_end_date);
        tvEndTime = (TextView) findViewById(R.id.fixed_task_end_time);
        name = (EditText) findViewById(R.id.fixed_task_name);
        description = (EditText) findViewById(R.id.fixed_task_description);
        restDuration = (EditText) findViewById(R.id.fixed_task_rest_duration);
        placeTextView = (TextView) findViewById(R.id.fixed_task_location_text_view);

        restDuration.setHint(((Integer)DEFAULT_REST_DURATION).toString());

        UserDefinedTask taskToEdit = getIntent().getParcelableExtra(EXTRA_FILTER_HOLDER);
        if (taskToEdit != null) {

            name.setText(taskToEdit.getName());
            description.setText(taskToEdit.getDescription());

            restDuration.setText(((Long) taskToEdit.getRestDuration().getStandardMinutes()).toString());

            IntervalFilter filter = (IntervalFilter) taskToEdit.getFilterSet().getFilterList().get(0);
            start = (DateTime)filter.getInitialMoment();
            end = (DateTime)filter.getFinalMoment();

            if (taskToEdit.getPlace() != null) {
                chosenPlace = taskToEdit.getPlace();
                placeTextView.setText(chosenPlace.getAddress());
            }

        }

        tvStartDate.setText(start.getDayOfMonth() + "/" + start.getMonthOfYear() + "/" + start.getYear());
        tvStartTime.setText(start.getHourOfDay() + " hours " + start.getMinuteOfHour() + " minutes");

        tvEndDate.setText(end.getDayOfMonth() + "/" + end.getMonthOfYear() + "/" + end.getYear());
        tvEndTime.setText(end.getHourOfDay() + " hours " + end.getMinuteOfHour() + " minutes");

    }

    public void onStartIntervalDateClick(View view) {
        showDialog(START_DATE);
    }

    public void onStartIntervalTimeClick(View view) {
        showDialog(START_TIME);
    }

    public void onEndIntervalDateClick(View view) {
        showDialog(END_DATE);
    }

    public void onEndIntervalTimeClick(View view) {
        showDialog(END_TIME);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == START_DATE) {
            return new DatePickerDialog(this, callBackStartIntervalDate,
                    start.getYear(), start.getMonthOfYear() - 1, start.getDayOfMonth());
        }
        if (id == START_TIME) {
            return new TimePickerDialog(this, callBackStartIntevalTime,
                    start.getHourOfDay(), start.getMinuteOfHour(), true);
        }
        if (id == END_DATE) {
            return new DatePickerDialog(this, callBackEndIntervalDate,
                    end.getYear(), end.getMonthOfYear() - 1, end.getDayOfMonth());
        }
        if (id == END_TIME) {
            return new TimePickerDialog(this, callBackEndIntevalTime,
                    end.getHourOfDay(), end.getMinuteOfHour(), true);
        }
        return super.onCreateDialog(id);
    }

    private final DatePickerDialog.OnDateSetListener callBackStartIntervalDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            start = start.withDate(year, monthOfYear + 1, dayOfMonth);
            tvStartDate.setText(start.getDayOfMonth() + "/" + start.getMonthOfYear() + "/" + start.getYear());
        }
    };

    private final DatePickerDialog.OnDateSetListener callBackEndIntervalDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            end = end.withDate(year, monthOfYear + 1, dayOfMonth);
            tvEndDate.setText(end.getDayOfMonth() + "/" + end.getMonthOfYear() + "/" + end.getYear());
        }
    };

    private final TimePickerDialog.OnTimeSetListener callBackStartIntevalTime = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            start = start.withTime(hourOfDay, minute, 0, 0);
            tvStartTime.setText(start.getHourOfDay() + " hours " + start.getMinuteOfHour() + " minutes");
        }
    };

    private final TimePickerDialog.OnTimeSetListener callBackEndIntevalTime = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            end = end.withTime(hourOfDay, minute, 0, 0);
            tvEndTime.setText(end.getHourOfDay() + " hours " + end.getMinuteOfHour() + " minutes");
        }
    };

    public void onButtonApplyClick(View view) {
        Intent intent = new Intent();

        if (end.compareTo(start) < 0) {
            Toast.makeText(getApplicationContext(), "Invalid time interval", Toast.LENGTH_SHORT).show();
            return;
        }

        UserDefinedTask fixedTask = UserDefinedTask.newFixedTask(name.getText().toString(),
                description.getText().toString(), start, end, chosenPlace);

        if (!restDuration.getText().toString().isEmpty()) {
            try {
                fixedTask.setRestDuration(Integer.parseInt(restDuration.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Incorrect rest duration", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            fixedTask.setRestDuration(DEFAULT_REST_DURATION);
        }

        intent.putExtra(EXTRA_FILTER_HOLDER, fixedTask);

        int position = getIntent().getIntExtra(EXTRA_FILTER_HOLDER_POSITION, -1);
        if (position >= 0) {
            intent.putExtra(EXTRA_FILTER_HOLDER_POSITION, position);
        }

        if (getParent() == null) {
            setResult(RESULT_OK, intent);
        } else {
            getParent().setResult(RESULT_OK, intent);
        }
        finish();
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
    public void onBackPressed() {
        if (getParent() == null) {
            setResult(RESULT_CANCELED, null);
        } else {
            getParent().setResult(RESULT_CANCELED, null);
        }
        finish();
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
