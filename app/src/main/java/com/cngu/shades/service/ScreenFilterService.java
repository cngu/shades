package com.cngu.shades.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.cngu.shades.helpers.FilterCommandParser;
import com.cngu.shades.manager.ScreenManager;
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

            try {
                int changedValue = sharedPreferences.getInt(key, -1);
                Log.d(TAG, "Pref " + key + " changed to " + changedValue);
            } catch (ClassCastException cce) {
                boolean changedValue = sharedPreferences.getBoolean(key, false);
                Log.d(TAG, "Pref " + key + " changed to " + changedValue);
            }
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
        ScreenManager screenManager = new ScreenManager(this, windowManager);
        FilterCommandParser commandParser = new FilterCommandParser();

        mPresenter = new ScreenFilterPresenter(view, windowViewManager, screenManager, commandParser);
    }

    // TODO: Wrap this in a SettingsManager
    SharedPreferences mSharedPreferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.i(TAG, String.format("onStartCommand(%s, %d, %d", intent, flags, startId));

        mSharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mSharedPrefListener);

        mPresenter.onScreenFilterCommand(intent);

        // TODO: Clean up. Probably move into Presenter and register if ON, and unregister if OFF.
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        this.registerReceiver(mBroadcastReceiver, filter);

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





    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent myIntent) {

            if ( myIntent.getAction().equals( Intent.ACTION_CONFIGURATION_CHANGED ) ) {

                Log.d(TAG, "received->" + Intent.ACTION_CONFIGURATION_CHANGED);


                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    // it's Landscape
                    Log.d(TAG, "LANDSCAPE");
                }
                else {
                    Log.d(TAG, "PORTRAIT");
                }

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                DisplayMetrics dm = new DisplayMetrics();
                display.getRealMetrics(dm);

                int finalHeight = dm.heightPixels;

                Log.d(TAG, "Screen Filter height: " + finalHeight);
            }
        }
    };
}
