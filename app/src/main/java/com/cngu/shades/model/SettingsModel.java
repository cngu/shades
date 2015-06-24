package com.cngu.shades.model;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.cngu.shades.R;
import com.cngu.shades.preference.ColorPickerPreference;
import com.cngu.shades.preference.DimSeekBarPreference;

public class SettingsModel implements SharedPreferences.OnSharedPreferenceChangeListener {
    private OnSettingsChangedListener mSettingsChangedListener;

    private String mDimPrefKey;
    private String mColorPrefKey;

    public SettingsModel(Resources resources) {
        if (resources == null) {
            throw new IllegalArgumentException("resources cannot be null");
        }

        mDimPrefKey = resources.getString(R.string.pref_key_shades_dim_level);
        mColorPrefKey = resources.getString(R.string.pref_key_shades_color);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (mSettingsChangedListener == null) {
            return;
        }

        if (key.equals(mDimPrefKey))
        {
            int dimLevel = sharedPreferences.getInt(key, DimSeekBarPreference.DEFAULT_VALUE);
            mSettingsChangedListener.onDimLevelChanged(dimLevel);
        }
        else if (key.equals(mColorPrefKey))
        {
            int color = sharedPreferences.getInt(key, ColorPickerPreference.DEFAULT_VALUE);
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
