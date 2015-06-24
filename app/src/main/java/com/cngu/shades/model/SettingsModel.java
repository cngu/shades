package com.cngu.shades.model;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.cngu.shades.R;

public class SettingsModel implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Resources mResources;
    private OnSettingsChangedListener mSettingsChangedListener;


    public SettingsModel(Resources resources) {
        if (resources == null) {
            throw new IllegalArgumentException("resources cannot be null");
        }

        mResources = resources;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (mSettingsChangedListener == null) {
            return;
        }

        if (key.equals(mResources.getString(R.string.pref_key_shades_dim_level)))
        {
            int dimLevel = sharedPreferences.getInt(key, -1);
            mSettingsChangedListener.onDimLevelChanged(dimLevel);
        }
        else if (key.equals(mResources.getString(R.string.pref_key_shades_color)))
        {
            int color = sharedPreferences.getInt(key, -1);
            mSettingsChangedListener.onColorChanged(color);
        }
    }

    public void setOnSettingsChangedListener(OnSettingsChangedListener listener) {
        mSettingsChangedListener = listener;
    }

    public interface OnSettingsChangedListener {
        void onDimLevelChanged(int dimLevel);
        void onColorChanged(int color);
    }
}
