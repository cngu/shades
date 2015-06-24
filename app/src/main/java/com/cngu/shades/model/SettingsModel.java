package com.cngu.shades.model;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.cngu.shades.R;
import com.cngu.shades.preference.ColorPickerPreference;
import com.cngu.shades.preference.DimSeekBarPreference;

public class SettingsModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences mSharedPreferences;
    private OnSettingsChangedListener mSettingsChangedListener;

    private String mDimPrefKey;
    private String mColorPrefKey;

    public SettingsModel(Resources resources, SharedPreferences sharedPreferences) {
        if (resources == null) {
            throw new IllegalArgumentException("resources cannot be null");
        }
        if (sharedPreferences == null) {
            throw new IllegalArgumentException("sharedPreferences cannot be null");
        }

        mSharedPreferences = sharedPreferences;

        mDimPrefKey = resources.getString(R.string.pref_key_shades_dim_level);
        mColorPrefKey = resources.getString(R.string.pref_key_shades_color);
    }

    public int getShadesDimLevel() {
        return mSharedPreferences.getInt(mDimPrefKey, DimSeekBarPreference.DEFAULT_VALUE);
    }

    public int getShadesColor() {
        return mSharedPreferences.getInt(mColorPrefKey, ColorPickerPreference.DEFAULT_VALUE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (mSettingsChangedListener == null) {
            return;
        }

        if (key.equals(mDimPrefKey))
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

    public void setOnSettingsChangedListener(OnSettingsChangedListener listener) {
        mSettingsChangedListener = listener;
    }

    public interface OnSettingsChangedListener {
        void onShadesDimLevelChanged(int dimLevel);
        void onShadesColorChanged(int color);
    }
}
