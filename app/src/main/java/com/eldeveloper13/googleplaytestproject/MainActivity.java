package com.eldeveloper13.googleplaytestproject;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.query.Query;

import butterknife.ButterKnife;
import butterknife.OnClick;

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
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGMSVersion();
        setupGoogleApiClient();
    }

    @OnClick(R.id.async_load_file)
    void onAsyncLoadFileClicked() {
        loadFile("");
    }

    private void loadFile(String filename) {
        Query query = new Query.Builder()
                .build();
        Drive.DriveApi.query(mGoogleApiClient, query)
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                        if (metadataBufferResult.getMetadataBuffer() != null) {
                            MetadataBuffer metadataBuffer = metadataBufferResult.getMetadataBuffer();
                            try{
                                for (Metadata metadata : metadataBuffer) {
                                    Log.d(MainActivity.this.getLocalClassName(), metadata.toString());
                                }
                            } finally {
                                metadataBuffer.release();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error getting file ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void setupGoogleApiClient() {
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
