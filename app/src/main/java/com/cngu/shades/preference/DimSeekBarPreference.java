package com.cngu.shades.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.cngu.shades.R;

public class DimSeekBarPreference extends Preference {

    public DimSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutResource(R.layout.preference_dim_seekbar);
    }

}
