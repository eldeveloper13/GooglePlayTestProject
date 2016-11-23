package com.eldeveloper13.googleplaytestproject;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

public class MainActivity extends AppCompatActivity {

    GoogleApiClient mGoogleApiClient;

    GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGMSVersion();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, mOnConnectionFailedListener)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .build();
    }

    private void checkGMSVersion() {
        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, result, RequestCode.GMS_ERROR_REQUEST);
            errorDialog.show();
        }
    }

    static class RequestCode {
        private static final int GMS_ERROR_REQUEST = 100;
    }
}
