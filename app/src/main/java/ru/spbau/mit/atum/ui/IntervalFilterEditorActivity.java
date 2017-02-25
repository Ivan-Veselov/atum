package ru.spbau.mit.atum.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.DateTime;

import ru.spbau.mit.atum.R;
import ru.spbau.mit.atum.model.IntervalFilter;
import ru.spbau.mit.atum.model.TimeFilter;

public class IntervalFilterEditorActivity extends AppCompatActivity {
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
    private EditText description;

    private CheckBox isExclusionary;

    private IntervalFilter previousFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_filter_editor);

        isExclusionary = (CheckBox) findViewById(R.id.isExclusionary);
        tvStartDate = (TextView) findViewById(R.id.interval_start_date);
        tvStartTime = (TextView) findViewById(R.id.interval_start_time);
        tvEndDate = (TextView) findViewById(R.id.interval_end_date);
        tvEndTime = (TextView) findViewById(R.id.interval_end_time);
        description = (EditText) findViewById(R.id.interval_filter_name);

        previousFilter = getIntent().getParcelableExtra(FilterEditorActivity.EXTRA_FILTER);
        if (previousFilter != null) {
            isExclusionary.setChecked(previousFilter.isExclusive());
            description.setText(previousFilter.getDescription());

            start = previousFilter.getInitialMoment().toDateTime();
            end = previousFilter.getFinalMoment().toDateTime();
        }

        tvStartDate.setText(start.toString("dd.MM.yyyy"));
        tvStartTime.setText(start.toString("HH:mm"));

        tvEndDate.setText(end.toString("dd.MM.yyyy"));
        tvEndTime.setText(end.toString("HH:mm"));

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
                    start.getYear(), end.getMonthOfYear() - 1, end.getDayOfMonth());
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
            tvStartDate.setText(start.toString("dd.MM.yyyy"));
        }
    };

    private final DatePickerDialog.OnDateSetListener callBackEndIntervalDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            end = end.withDate(year, monthOfYear + 1, dayOfMonth);
            tvEndDate.setText(end.toString("dd.MM.yyyy"));
        }
    };

    private final TimePickerDialog.OnTimeSetListener callBackStartIntevalTime = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            start = start.withTime(hourOfDay, minute, 0, 0);
            tvStartTime.setText(start.toString("HH:mm"));
        }
    };

    private TimePickerDialog.OnTimeSetListener callBackEndIntevalTime = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            end = end.withTime(hourOfDay, minute, 0, 0);
            tvEndTime.setText(end.toString("HH:mm"));
        }
    };

    public void onButtonOKClick(View view) {
        final Intent intent = new Intent();

        if (end.compareTo(start) < 0) {
            Toast.makeText(getApplicationContext(), "Invalid time interval", Toast.LENGTH_SHORT).show();
            return;
        }

        TimeFilter.ExclusionType exclusionType;
        if (isExclusionary.isChecked()) {
            exclusionType = TimeFilter.ExclusionType.EXCLUSIONARY;
        } else {
            exclusionType = TimeFilter.ExclusionType.COMMON;
        }

        TimeFilter timeFilter = new IntervalFilter(description.getText().toString(),
                    start, end, exclusionType);

        if (previousFilter == null) {
            intent.putExtra("filter", timeFilter);
        } else {
            intent.putExtra(FilterEditorActivity.EXTRA_FILTER, timeFilter);
            intent.putExtra(FilterEditorActivity.EXTRA_FILTER_POSITION,
                    getIntent().getIntExtra(FilterEditorActivity.EXTRA_FILTER_POSITION, -1));
        }

        if (getParent() == null) {
            setResult(RESULT_OK, intent);
        } else {
            getParent().setResult(RESULT_OK, intent);
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        if (getParent() == null) {
            setResult(RESULT_CANCELED);
        } else {
            getParent().setResult(RESULT_CANCELED);
        }
        finish();
    }
}
