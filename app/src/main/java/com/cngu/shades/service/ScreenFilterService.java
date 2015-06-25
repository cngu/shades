package com.cngu.shades.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;

import com.cngu.shades.R;
import com.cngu.shades.helper.FilterCommandFactory;
import com.cngu.shades.helper.FilterCommandParser;
import com.cngu.shades.manager.ScreenManager;
import com.cngu.shades.manager.WindowViewManager;
import com.cngu.shades.model.SettingsModel;
import com.cngu.shades.presenter.ScreenFilterPresenter;
import com.cngu.shades.receiver.OrientationChangeReceiver;
import com.cngu.shades.view.ScreenFilterView;

public class ScreenFilterService extends Service implements ServiceLifeCycleController {
    public static final int VALID_COMMAND_START = 0;
    public static final int COMMAND_ON = 0;
    public static final int COMMAND_OFF = 1;
    public static final int COMMAND_PAUSE = 2;
    public static final int COMMAND_RESUME = 3;
    public static final int VALID_COMMAND_END = 3;

    public static final String BUNDLE_KEY_COMMAND = "cngu.bundle.key.COMMAND";

    private static final String TAG = "ScreenFilterService";
    private static final boolean DEBUG = true;

    private ScreenFilterPresenter mPresenter;
    private SettingsModel mSettingsModel;
    private SharedPreferences mSharedPreferences;
    private OrientationChangeReceiver mOrientationReceiver;

    private boolean mRunningInForeground = false;

    @Override
    public void onCreate() {
        super.onCreate();

        if (DEBUG) Log.i(TAG, "onCreate");

        // Initialize helpers and managers
        Context context = this;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        ScreenFilterView view = new ScreenFilterView(context);
        WindowViewManager wvm = new WindowViewManager(windowManager);
        ScreenManager sm = new ScreenManager(this, windowManager);
        FilterCommandParser fcp = new FilterCommandParser();

        // Wire MVP classes
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSettingsModel = new SettingsModel(context.getResources(), mSharedPreferences);

        mPresenter = new ScreenFilterPresenter(view, mSettingsModel, this, wvm, sm, fcp);

        // Make Presenter listen to settings changes and orientation changes
        mSettingsModel.openSettingsChangeListener();
        mSettingsModel.setOnSettingsChangedListener(mPresenter);

        registerOrientationReceiver(mPresenter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.i(TAG, String.format("onStartCommand(%s, %d, %d", intent, flags, startId));

        if (!mRunningInForeground) {
            runOnForegroundWithNotification();
        }

        mPresenter.onScreenFilterCommand(intent);

        // Do not attempt to restart if the hosting process is killed by Android
        return START_NOT_STICKY;
    }

    private void runOnForegroundWithNotification() {
        FilterCommandFactory commandFactory = new FilterCommandFactory(this);

        // TODO: Properly construct this notification
        startForeground(1, new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("TITLE").setContentText("CONTENT").setSubText("SUB").build());

        mRunningInForeground = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Prevent binding.
        return null;
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.i(TAG, "onDestroy");

        mSettingsModel.closeSettingsChangeListener();
        unregisterOrientationReceiver();

        super.onDestroy();
    }

    @Override
    public void stop() {
        if (DEBUG) Log.i(TAG, "Received stop request");
        stopSelf();
    }

    private void registerOrientationReceiver(OrientationChangeReceiver.OnOrientationChangeListener listener) {
        if (mOrientationReceiver != null) {
            return;
        }

        IntentFilter orientationIntentFilter = new IntentFilter();
        orientationIntentFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);

        mOrientationReceiver = new OrientationChangeReceiver(this, listener);
        registerReceiver(mOrientationReceiver, orientationIntentFilter);
    }

    private void unregisterOrientationReceiver() {
        unregisterReceiver(mOrientationReceiver);
        mOrientationReceiver = null;
    }
}
