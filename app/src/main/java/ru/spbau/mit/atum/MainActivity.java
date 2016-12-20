package ru.spbau.mit.atum;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class MainActivity extends UserDataEditorActivity {
    private static final int PERMISSIONS_REQUEST_CALENDAR = 0;

    private CalendarExporter calendarExporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] { android.Manifest.permission.WRITE_CALENDAR,
                                                  android.Manifest.permission.READ_CALENDAR
                                                },
                                   PERMISSIONS_REQUEST_CALENDAR);
            }
        } else {
            calendarExporter = new CalendarExporter(getApplicationContext());
        }

        loadUserData();
    }

    public void onTaskListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        startActivity(intent);
    }

    public void onBlockerListClick(View view) {
        Intent intent = new Intent(this, TimeBlockerListActivity.class);
        startActivity(intent);
    }

    public void onSchedulePlannerClick(View view) {
        SchedulePlanner.planSchedule(UserSynchronisableData.getInstance(), new DateTime(), new DateTime().plusYears(5));
        saveUserData();

        Toast.makeText(getApplicationContext(), "SCHEDULE PLANNED", Toast.LENGTH_SHORT).show();
    }

    public void onViewScheduleClick(View view) {
        Intent intent = new Intent(this, SchedulerViewerActivity.class);
        intent.putParcelableArrayListExtra("all tasks", (ArrayList<? extends Parcelable>)
                UserSynchronisableData.getInstance().getTasks());
        startActivity(intent);
    }

    public void onExportToGoogleCalendarClick(View view) {
        calendarExporter.addTasks(UserSynchronisableData.getInstance().getTasks());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CALENDAR) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                calendarExporter = new CalendarExporter(getApplicationContext());
            }
        }
    }
}
