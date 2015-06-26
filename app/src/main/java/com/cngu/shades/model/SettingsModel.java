package com.cngu.shades.model;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cngu.shades.R;
import com.cngu.shades.preference.ColorPickerPreference;
import com.cngu.shades.preference.DimSeekBarPreference;

/**
 * This class provides access to get and set Shades settings, and also listen to settings changes.
 *
 * <p>In order to listen to settings changes, invoke
 * {@link SettingsModel#setOnSettingsChangedListener(OnSettingsChangedListener)} and
 * {@link SettingsModel#openSettingsChangeListener()}.
 *
 * <p><b>You must call {@link SettingsModel#closeSettingsChangeListener()} when you are done
 * listening to changes.</b>
 *
 * <p>To begin listening again, invoke {@link SettingsModel#openSettingsChangeListener()}.
 */
public class SettingsModel implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "SettingsModel";
    private static final boolean DEBUG = true;

    private SharedPreferences mSharedPreferences;
    private OnSettingsChangedListener mSettingsChangedListener;

    private String mPowerStatePrefKey;
    private String mPauseStatePrefKey;
    private String mDimPrefKey;
    private String mColorPrefKey;
    private String mOpenOnBootPrefKey;
    private String mKeepRunningAfterRebootPrefKey;

    public SettingsModel(@NonNull Resources resources, @NonNull SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;

        mPowerStatePrefKey = resources.getString(R.string.pref_key_shades_power_state);
        mPauseStatePrefKey = resources.getString(R.string.pref_key_shades_pause_state);
        mDimPrefKey = resources.getString(R.string.pref_key_shades_dim_level);
        mColorPrefKey = resources.getString(R.string.pref_key_shades_color);
        mOpenOnBootPrefKey = resources.getString(R.string.pref_key_always_open_on_startup);
        mKeepRunningAfterRebootPrefKey = resources.getString(R.string.pref_key_keep_running_after_reboot);
    }

    public boolean getShadesPowerState() {
        return mSharedPreferences.getBoolean(mPowerStatePrefKey, false);
    }

    public void setShadesPowerState(boolean state) {
        mSharedPreferences.edit().putBoolean(mPowerStatePrefKey, state).apply();
    }

    public boolean getShadesPauseState() {
        return mSharedPreferences.getBoolean(mPauseStatePrefKey, false);
    }

    public void setShadesPauseState(boolean state) {
        mSharedPreferences.edit().putBoolean(mPauseStatePrefKey, state).apply();
    }

    public int getShadesDimLevel() {
        return mSharedPreferences.getInt(mDimPrefKey, DimSeekBarPreference.DEFAULT_VALUE);
    }

    public int getShadesColor() {
        return mSharedPreferences.getInt(mColorPrefKey, ColorPickerPreference.DEFAULT_VALUE);
    }

    public boolean getOpenOnBootFlag() {
        return mSharedPreferences.getBoolean(mOpenOnBootPrefKey, false);
    }

    public boolean getKeepRunningAfterRebootFlag() {
        return mSharedPreferences.getBoolean(mKeepRunningAfterRebootPrefKey, false);
    }

    public void setOnSettingsChangedListener(OnSettingsChangedListener listener) {
        mSettingsChangedListener = listener;
    }

    public void openSettingsChangeListener() {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        if (DEBUG) Log.d(TAG, "Opened Settings change listener");
    }

    public void closeSettingsChangeListener() {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

        if (DEBUG) Log.d(TAG, "Closed Settings change listener");
    }

    //region OnSharedPreferenceChangeListener
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (mSettingsChangedListener == null) {
            return;
        }

        if (key.equals(mPowerStatePrefKey))
        {
            boolean powerState = getShadesPowerState();
            mSettingsChangedListener.onShadesPowerStateChanged(powerState);
        }
        else if (key.equals(mPauseStatePrefKey))
        {
            boolean pauseState = getShadesPauseState();
            mSettingsChangedListener.onShadesPauseStateChanged(pauseState);
        }
        else if (key.equals(mDimPrefKey))
        {
            int dimLevel = getShadesDimLevel();
            mSettingsChangedListener.onShadesDimLevelChanged(dimLevel);
        }
        else if (key.equals(mColorPrefKey))
        {
            int color = getShadesColor();
            mSettingsChangedListener.onShadesColorChanged(color);
        }
    }
    //endregion

    public interface OnSettingsChangedListener {
        void onShadesPowerStateChanged(boolean powerState);
        void onShadesPauseStateChanged(boolean pauseState);
        void onShadesDimLevelChanged(int dimLevel);
        void onShadesColorChanged(int color);
    }
}
