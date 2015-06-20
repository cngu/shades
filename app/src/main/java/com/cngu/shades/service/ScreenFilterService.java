package com.cngu.shades.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ScreenFilterService extends Service {


    private static final String TAG = "ScreenFilterService";
    private static final boolean DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();

        if (DEBUG) Log.i(TAG, "onCreate");

        // TODO: Create WindowView and Presenter
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.i(TAG, String.format("onStartCommand(%s, %d, %d", intent, flags, startId));

        // TODO: Forward command to Presenter. Presenter could implement State pattern too.

        // Restart with the same intent if the hosting process is killed,
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Prevent binding.
        return null;
    }
}
