package ru.spbau.mit.atum;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
                          implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private UserSynchronisableData userSynchronisableData;

    private GoogleApiClient mGoogleApiClient;

    private boolean loadOnConnection = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                              .addApi(Drive.API)
                                              .addScope(Drive.SCOPE_FILE)
                                              .addScope(Drive.SCOPE_APPFOLDER)
                                              .addConnectionCallbacks(this)
                                              .addOnConnectionFailedListener(this)
                                              .build();

        userSynchronisableData = new UserSynchronisableData("atum");
    }

    private final int TASK_CODE = 0;
    private final int BLOCKER_CODE = 1;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 2;

    public void onTaskListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter holders", (Serializable) userSynchronisableData.getTasks());
        intent.putExtra("filter holder type", TASK_CODE);
        startActivityForResult(intent, TASK_CODE);
    }

    public void onBlockerListClick(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter holders", (Serializable) userSynchronisableData.getBlockers());
        intent.putExtra("filter holder type", BLOCKER_CODE);
        startActivityForResult(intent, BLOCKER_CODE);
    }

    public void onSchedulePlannerClick(View view) {
        SchedulePlanner.planSchedule(userSynchronisableData, new DateTime(), new DateTime().plus(365L * 24 * 60 * 60 * 1000));
        Toast.makeText(getApplicationContext(), "SCHEDULE PLANNED", Toast.LENGTH_SHORT).show();
    }

    public void onViewScheduleClick(View view) {
        Intent intent = new Intent(this, SchedulerViewerActivity.class);
        intent.putExtra("all tasks", (Serializable) userSynchronisableData.getTasks());
        startActivity(intent);
    }

    public void onLoadClick(View view) {
        loadOnConnection = true;
        mGoogleApiClient.connect();
    }

    public void onSaveClick(View view) {
        loadOnConnection = false;
        mGoogleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TASK_CODE && resultCode == RESULT_OK) {
            userSynchronisableData.setTasks((ArrayList<UserDefinedTask>)data.getSerializableExtra("filter holders"));
        }
        if (requestCode == BLOCKER_CODE && resultCode == RESULT_OK) {
            userSynchronisableData.setBlockers((ArrayList<UserDefinedTimeBlocker>)data.getSerializableExtra("filter holders"));
        }
        if (requestCode == RESOLVE_CONNECTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                Toast.makeText(this, "Connection to Google API failed. Check your internet connection.",
                                     Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Connected to Google API", Toast.LENGTH_LONG).show();

        if (loadOnConnection) {
            userSynchronisableData.loadData(this, mGoogleApiClient);
        } else {
            userSynchronisableData.saveData(this, mGoogleApiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                Toast.makeText(this, "Unable to resolve connection with Google API",
                               Toast.LENGTH_LONG).show();
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }
}
