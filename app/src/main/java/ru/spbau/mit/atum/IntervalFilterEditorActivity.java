package ru.spbau.mit.atum;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;

public class IntervalFilterEditorActivity extends AppCompatActivity {
    private int START_DATE = 1;
    private int START_TIME = 2;
    private int END_DATE = 3;
    private int END_TIME = 4;


    private int startYear = 2016;
    private int startMonth = 1;
    private int startDay = 1;
    private int endYear = 2016;
    private int endMonth = 1;
    private int endDay = 1;

    private int startHour = 0;
    private int startMinute = 0;
    private int endHour = 0;
    private int endMinute = 0;

    private TextView tvStartDate;
    private TextView tvStartTime;
    private TextView tvEndDate;
    private TextView tvEndTime;

    private CheckBox isExclusionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_filter_editor);

        isExclusionary = (CheckBox) findViewById(R.id.isExclusionary);
        tvStartDate = (TextView) findViewById(R.id.interval_start_date);
        tvStartTime = (TextView) findViewById(R.id.interval_start_time);
        tvEndDate = (TextView) findViewById(R.id.interval_end_date);
        tvEndTime = (TextView) findViewById(R.id.interval_end_time);
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
            DatePickerDialog tpd = new DatePickerDialog(this, CallBackStartIntervalDate,
                    startYear, startMonth, startDay);
            return tpd;
        }
        if (id == START_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, CallBackStartIntevalTime, startHour, startMinute, true);
            return tpd;
        }
        if (id == END_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, CallBackEndIntervalDate,
                    startYear, startMonth, startDay);
            return tpd;
        }
        if (id == END_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, CallBackEndIntevalTime, startHour, startMinute, true);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog.OnDateSetListener CallBackStartIntervalDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
            tvStartDate.setText(startDay + "/" + startMonth + "/" + startYear);
        }
    };

    private DatePickerDialog.OnDateSetListener CallBackEndIntervalDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            endYear = year;
            endMonth = monthOfYear;
            endDay = dayOfMonth;
            tvEndDate.setText(endDay + "/" + endMonth + "/" + endYear);
        }
    };

    private TimePickerDialog.OnTimeSetListener CallBackStartIntevalTime = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startHour = hourOfDay;
            startMinute = minute;
            tvStartTime.setText(startHour + " hours " + startMinute + " minutes");
        }
    };

    private TimePickerDialog.OnTimeSetListener CallBackEndIntevalTime = new TimePickerDialog.OnTimeSetListener() {
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

        TimeFilter timeFilter = new IntervalFilter(startTime, endTime, isExclusionary.isChecked());

        intent.putExtra("filter", timeFilter);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        finish();
    }
}
