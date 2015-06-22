package com.cngu.shades.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;

import com.cngu.shades.helpers.FilterCommandParser;
import com.cngu.shades.manager.WindowViewManager;
import com.cngu.shades.presenter.ScreenFilterPresenter;
import com.cngu.shades.view.ScreenFilterView;

public class ScreenFilterService extends Service {
    public static final int VALID_COMMAND_START = 0;
    public static final int COMMAND_ON = 0;
    public static final int COMMAND_OFF = 1;
    public static final int COMMAND_PAUSE = 2;
    public static final int COMMAND_RESUME = 4;
    public static final int VALID_COMMAND_END = 4;

    public static final String BUNDLE_KEY_COMMAND = "cngu.bundle.key.COMMAND";

    private static final String TAG = "ScreenFilterService";
    private static final boolean DEBUG = true;

    private ScreenFilterPresenter mPresenter;

    private SharedPreferences.OnSharedPreferenceChangeListener mSharedPrefListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            int changedValue = sharedPreferences.getInt(key, -1);
            Log.d(TAG, "Pref " + key + " changed to " + changedValue);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        if (DEBUG) Log.i(TAG, "onCreate");

        // Wire View and Presenter
        Context context = this;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        ScreenFilterView view = new ScreenFilterView(context);
        WindowViewManager windowViewManager = new WindowViewManager(windowManager);
        FilterCommandParser filterCommandParser = new FilterCommandParser();

        mPresenter = new ScreenFilterPresenter(view, windowViewManager, filterCommandParser);
    }

    SharedPreferences mSharedPreferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.i(TAG, String.format("onStartCommand(%s, %d, %d", intent, flags, startId));

        mSharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mSharedPrefListener);

        mPresenter.onScreenFilterCommand(intent);

        // Do not attempt to restart if the hosting process is killed by Android
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Prevent binding.
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (DEBUG) Log.i(TAG, "onDestroy");

        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mSharedPrefListener);
    }
}
