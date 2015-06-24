package com.cngu.shades.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;

import com.cngu.shades.helper.FilterCommandParser;
import com.cngu.shades.manager.ScreenManager;
import com.cngu.shades.manager.WindowViewManager;
import com.cngu.shades.model.SettingsModel;
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
    private SettingsModel mSettingsModel;
    private SharedPreferences mSharedPreferences;
    private OrientationReceiver mOrientationReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        if (DEBUG) Log.i(TAG, "onCreate");

        // Wire View and Presenter
        Context context = this;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        ScreenFilterView view = new ScreenFilterView(context);
        WindowViewManager wvm = new WindowViewManager(windowManager);
        ScreenManager sm = new ScreenManager(this, windowManager);
        FilterCommandParser fcp = new FilterCommandParser();

        mSettingsModel = new SettingsModel(context.getResources());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mSettingsModel);

        mPresenter = new ScreenFilterPresenter(view, mSettingsModel, wvm, sm, fcp);

        registerOrientationReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.i(TAG, String.format("onStartCommand(%s, %d, %d", intent, flags, startId));

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
        if (DEBUG) Log.i(TAG, "onDestroy");

        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mSettingsModel);
        unregisterOrientationReceiver();

        super.onDestroy();
    }

    private void registerOrientationReceiver() {
        if (mOrientationReceiver != null) {
            return;
        }

        IntentFilter orientationIntentFilter = new IntentFilter();
        orientationIntentFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);

        mOrientationReceiver = new OrientationReceiver();
        registerReceiver(mOrientationReceiver, orientationIntentFilter);
    }

    private void unregisterOrientationReceiver() {
        unregisterReceiver(mOrientationReceiver);
        mOrientationReceiver = null;
    }

    private class OrientationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CONFIGURATION_CHANGED)) {
                Resources r = getResources();
                if(r.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    mPresenter.onPortraitOrientation();
                }
                else {
                    mPresenter.onLandscapeOrientation();
                }
            }
        }
    }
}
