package ru.spbau.mit.atum;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;

import static ru.spbau.mit.atum.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER;
import static ru.spbau.mit.atum.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER_POSITION;
import static ru.spbau.mit.atum.UserDefinedTask.DEFAULT_REST_DURATION;

public class FixedTaskEditorActivity extends AppCompatActivity {

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
    private EditText name;
    private EditText description;
    private EditText restDuration;

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

        restDuration.setHint(((Integer)DEFAULT_REST_DURATION).toString());

        UserDefinedTask taskToEdit = getIntent().getParcelableExtra(EXTRA_FILTER_HOLDER);
        if (taskToEdit != null) {

            name.setText(taskToEdit.getName());
            description.setText(taskToEdit.getDescription());

            restDuration.setText(((Integer)taskToEdit.getRestDuration()).toString());

            IntervalFilter filter = (IntervalFilter) taskToEdit.getFilterList().get(0);
            DateTime start = (DateTime)filter.getInitialMoment();
            DateTime end = (DateTime)filter.getFinalMoment();

            startYear = start.getYear();
            startMonth = start.getMonthOfYear();
            startDay = start.getDayOfMonth();
            startHour = start.getHourOfDay();
            startMinute = start.getMinuteOfHour();

            endYear = end.getYear();
            endMonth = end.getMonthOfYear();
            endDay = end.getDayOfMonth();
            endHour = end.getHourOfDay();
            endMinute = end.getMinuteOfHour();


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
            DatePickerDialog tpd = new DatePickerDialog(this, callBackStartIntervalDate,
                    startYear, startMonth - 1, startDay);
            return tpd;
        }
        if (id == START_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, callBackStartIntevalTime, startHour, startMinute, true);
            return tpd;
        }
        if (id == END_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, callBackEndIntervalDate,
                    endYear, endMonth - 1, endDay);
            return tpd;
        }
        if (id == END_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, callBackEndIntevalTime, endHour, endMinute, true);
            return tpd;
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

    public void onButtonApplyClick(View view) {
        Intent intent = new Intent();

        ReadableDateTime startTime = new DateTime(startYear, startMonth,
                startDay, startHour, startMinute);
        ReadableDateTime endTime = new DateTime(endYear, endMonth,
                endDay, endHour, endMinute);

        if (endTime.compareTo(startTime) < 0) {
            Toast.makeText(getApplicationContext(), "Invalid time interval", Toast.LENGTH_SHORT).show();
            return;
        }

        UserDefinedTask fixedTask = UserDefinedTask.newFixedTask(name.getText().toString(),
                description.getText().toString(), startTime, endTime, null);

        if (!restDuration.getText().toString().isEmpty()) {
            fixedTask.setRestDuration(Integer.parseInt(restDuration.getText().toString()));
        } else {
            fixedTask.setRestDuration(DEFAULT_REST_DURATION);
        }

        intent.putExtra(EXTRA_FILTER_HOLDER, fixedTask);

        int position = getIntent().getIntExtra(EXTRA_FILTER_HOLDER_POSITION, -1);
        if (position >= 0) {
            intent.putExtra(EXTRA_FILTER_HOLDER_POSITION, position);
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        finish();
    }
}
