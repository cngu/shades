package com.cngu.shades.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

import com.cngu.shades.R;

public class DimSeekBarPreference extends Preference {
    private static final int DEFAULT_VALUE = 50;

    public DimSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.preference_dim_seekbar);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);


    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }


}
