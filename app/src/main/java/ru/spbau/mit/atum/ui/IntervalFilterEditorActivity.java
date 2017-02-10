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
import org.joda.time.ReadableDateTime;

import ru.spbau.mit.atum.model.IntervalFilter;
import ru.spbau.mit.atum.R;
import ru.spbau.mit.atum.model.TimeFilter;

public class IntervalFilterEditorActivity extends AppCompatActivity {
    private final int START_DATE = 1;
    private final int START_TIME = 2;
    private final int END_DATE = 3;
    private final int END_TIME = 4;

    private final DateTime currentDateTime = new DateTime();

    private int startYear = currentDateTime.getYear();
    private int startMonth = currentDateTime.getMonthOfYear();
    private int startDay = currentDateTime.getDayOfMonth();
    private int endYear = currentDateTime.getYear();
    private int endMonth = currentDateTime.getMonthOfYear();
    private int endDay = currentDateTime.getDayOfMonth();

    private int startHour = currentDateTime.getHourOfDay();
    private int startMinute = currentDateTime.getMinuteOfHour();
    private int endHour = currentDateTime.getHourOfDay();
    private int endMinute = currentDateTime.getMinuteOfHour();

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

            startYear = previousFilter.getInitialMoment().getYear();
            startMonth = previousFilter.getInitialMoment().getMonthOfYear();
            startDay = previousFilter.getInitialMoment().getDayOfMonth();
            startHour = previousFilter.getInitialMoment().getHourOfDay();
            startMinute = previousFilter.getInitialMoment().getMinuteOfHour();

            endYear = previousFilter.getFinalMoment().getYear();
            endMonth = previousFilter.getFinalMoment().getMonthOfYear();
            endDay = previousFilter.getFinalMoment().getDayOfMonth();
            endHour = previousFilter.getFinalMoment().getHourOfDay();
            endMinute = previousFilter.getFinalMoment().getMinuteOfHour();
        }
        tvStartDate.setText(startDay + "/" + startMonth + "/" + startYear);
        tvStartTime.setText(startHour + " hours " + startMinute + " minutes");

        tvEndDate.setText(endDay + "/" + endMonth + "/" + endYear);
        tvEndTime.setText(endHour + " hours " + endMinute + " minutes");

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
                    startYear, startMonth - 1, startDay);
        }
        if (id == START_TIME) {
            return new TimePickerDialog(this, callBackStartIntevalTime, startHour, startMinute, true);
        }
        if (id == END_DATE) {
            return new DatePickerDialog(this, callBackEndIntervalDate,
                    endYear, endMonth - 1, endDay);
        }
        if (id == END_TIME) {
            return new TimePickerDialog(this, callBackEndIntevalTime, endHour, endMinute, true);
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog.OnDateSetListener callBackStartIntervalDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            startYear = year;
            startMonth = monthOfYear + 1;
            startDay = dayOfMonth;
            tvStartDate.setText(startDay + "/" + startMonth + "/" + startYear);
        }
    };

    private DatePickerDialog.OnDateSetListener callBackEndIntervalDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            endYear = year;
            endMonth = monthOfYear + 1;
            endDay = dayOfMonth;
            tvEndDate.setText(endDay + "/" + endMonth + "/" + endYear);
        }
    };

    private TimePickerDialog.OnTimeSetListener callBackStartIntevalTime = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startHour = hourOfDay;
            startMinute = minute;
            tvStartTime.setText(startHour + " hours " + startMinute + " minutes");
        }
    };

    private TimePickerDialog.OnTimeSetListener callBackEndIntevalTime = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endHour = hourOfDay;
            endMinute = minute;
            tvEndTime.setText(endHour + " hours " + endMinute + " minutes");
        }
    };

    public void onButtonOKClick(View view) {
        Intent intent = new Intent();

        ReadableDateTime startTime = new DateTime(startYear, startMonth,
                startDay, startHour, startMinute);
        ReadableDateTime endTime = new DateTime(endYear, endMonth,
                endDay, endHour, endMinute);

        if (endTime.compareTo(startTime) < 0) {
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
                    startTime, endTime, exclusionType);

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
