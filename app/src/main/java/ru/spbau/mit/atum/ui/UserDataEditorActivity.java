package ru.spbau.mit.atum.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import ru.spbau.mit.atum.model.UserSynchronisableData;

public abstract class UserDataEditorActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;

    private ConnectionReason reason;

    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                              .addApi(Drive.API)
                                              .addScope(Drive.SCOPE_FILE)
                                              .addScope(Drive.SCOPE_APPFOLDER)
                                              .addConnectionCallbacks(this)
                                              .addOnConnectionFailedListener(this)
                                              .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESOLVE_CONNECTION_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Connection to Google API failed.",
                        Toast.LENGTH_LONG).show();
            }

            mGoogleApiClient.connect();
        }
    }

    protected void loadUserData() {
        reason = ConnectionReason.LOAD;

        disableActions();
        mGoogleApiClient.connect();
    }

    protected void saveUserData() {
        reason = ConnectionReason.SAVE;

        disableActions();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        switch (reason) {
            case LOAD:
                UserSynchronisableData.getInstance().loadData(this, mGoogleApiClient,
                    new UserSynchronisableData.Callback() {
                        @Override
                        public void call() {
                            mGoogleApiClient.disconnect();
                            enableActions();
                        }
                    }
                );
                break;

            case SAVE:
                UserSynchronisableData.getInstance().saveData(this, mGoogleApiClient,
                    new UserSynchronisableData.Callback() {
                        @Override
                        public void call() {
                            mGoogleApiClient.disconnect();
                            enableActions();
                        }
                    }
                );
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                Toast.makeText(this, "Unable to resolve connection with Google API",
                        Toast.LENGTH_LONG).show();
                mGoogleApiClient.connect();
            }
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),
                                                                  this, 0);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mGoogleApiClient.connect();
                }
            });

            dialog.show();
        }
    }

    private void disableActions() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                             WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void enableActions() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private enum ConnectionReason { LOAD, SAVE }
}
