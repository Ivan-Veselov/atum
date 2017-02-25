package ru.spbau.mit.atum.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.util.EnumSet;

import ru.spbau.mit.atum.R;
import ru.spbau.mit.atum.model.TimeFilter;
import ru.spbau.mit.atum.model.WeekFilter;

public class WeekFilterEditorActivity extends AppCompatActivity {
    private final int FIRST_MINUTE = 1;
    private final int DURATION = 2;

    private DateTime first = new DateTime().withTime(0, 0, 0, 0);
    private DateTime after = new DateTime().withTime(0, 0, 0, 0);

    private CheckBox isExclusionary;

    private TextView tvFirstMinute;
    private TextView tvDuration;
    private EditText description;

    private final int DAYS_IN_WEEK = 7;
    private final CheckBox [] checkBoxList = new CheckBox[DAYS_IN_WEEK];

    private WeekFilter previousFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_filter_editor);

        checkBoxList[0] = (CheckBox) findViewById(R.id.checkBoxMonday);
        checkBoxList[1] = (CheckBox) findViewById(R.id.checkBoxTuesday);
        checkBoxList[2] = (CheckBox) findViewById(R.id.checkBoxWednesday);
        checkBoxList[3] = (CheckBox) findViewById(R.id.checkBoxThursday);
        checkBoxList[4] = (CheckBox) findViewById(R.id.checkBoxFriday);
        checkBoxList[5] = (CheckBox) findViewById(R.id.checkBoxSaturday);
        checkBoxList[6] = (CheckBox) findViewById(R.id.checkBoxSunday);

        description = (EditText) findViewById(R.id.week_filter_name);
        isExclusionary = (CheckBox) findViewById(R.id.isExclusionary);

        previousFilter = getIntent().getParcelableExtra(FilterEditorActivity.EXTRA_FILTER);
        if (previousFilter != null) {
            isExclusionary.setChecked(previousFilter.isExclusive());
            description.setText(previousFilter.getDescription());

            first = new DateTime().withTime(0, 0, 0, 0).plusMinutes(previousFilter.getFirstMinute());
            after = new DateTime().withTime(0, 0, 0, 0).plusMinutes(previousFilter.getMinuteAfterLast());

            for (int i = 0; i < 7; i++) {
                checkBoxList[i].setChecked(
                        previousFilter.getWeekMask().contains(WeekFilter.DaysOfWeek.values()[i]));
            }
        }

        tvFirstMinute = (TextView) findViewById(R.id.first_minute);
        tvFirstMinute.setText(first.toString("HH:mm"));

        tvDuration = (TextView) findViewById(R.id.duration);
        tvDuration.setText(after.toString("HH:mm"));
    }

    public void onFirstMinuteClick(View view) {
        showDialog(FIRST_MINUTE);
    }

    public void onDurationClick(View view) {
        showDialog(DURATION);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == FIRST_MINUTE) {
            return new TimePickerDialog(this, CallBackForFirstMinute, first.getHourOfDay(), first.getMinuteOfHour(), true);
        }
        if (id == DURATION) {
            return new TimePickerDialog(this, CallBackForDuration, after.getHourOfDay(), after.getMinuteOfHour(), true);
        }
        return super.onCreateDialog(id);
    }

    private final TimePickerDialog.OnTimeSetListener CallBackForFirstMinute = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            first = first.withTime(hourOfDay, minute, 0, 0);
            tvFirstMinute.setText(first.toString("HH:mm"));
        }
    };

    private final TimePickerDialog.OnTimeSetListener CallBackForDuration = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            after = after.withTime(hourOfDay, minute, 0, 0);
            tvDuration.setText(after.toString("HH:mm"));
        }
    };

    public void onButtonOKClick(View view) {
        Intent intent = new Intent();
        EnumSet<WeekFilter.DaysOfWeek> mask = EnumSet.noneOf(WeekFilter.DaysOfWeek.class);
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            if (checkBoxList[i].isChecked()) {
                mask.add(WeekFilter.DaysOfWeek.values()[i]);
            }
        }

        TimeFilter.ExclusionType exclusionType;
        if (isExclusionary.isChecked()) {
            exclusionType = TimeFilter.ExclusionType.EXCLUSIONARY;
        } else {
            exclusionType = TimeFilter.ExclusionType.COMMON;
        }

        TimeFilter timeFilter = WeekFilter.newWeekFilterFromMinutesInterval(description.getText().toString(),
                first.getMinuteOfDay(), after.getMinuteOfDay(),
                mask, exclusionType);

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
