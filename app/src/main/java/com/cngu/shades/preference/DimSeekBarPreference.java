package com.cngu.shades.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

import com.cngu.shades.R;

public class DimSeekBarPreference extends Preference {
    public static final int DEFAULT_VALUE = 50;

    private SeekBar mDimLevelSeekBar;
    private int mDimLevel;

    public DimSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.preference_dim_seekbar);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mDimLevel = getPersistedInt(DEFAULT_VALUE);
        } else {
            mDimLevel = (Integer) defaultValue;
            persistInt(mDimLevel);
        }
    }

    @Override
    protected void onBindView(@NonNull View view) {
        super.onBindView(view);

        mDimLevelSeekBar = (SeekBar) view.findViewById(R.id.dim_level_seekbar);
        initLayout();
    }

    private void initLayout() {
        mDimLevelSeekBar.setProgress(mDimLevel);

        mDimLevelSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDimLevel = progress;
                persistInt(mDimLevel);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
