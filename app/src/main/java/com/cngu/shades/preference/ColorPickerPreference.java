package com.cngu.shades.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.cngu.shades.R;

public class ColorPickerPreference extends DialogPreference {

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.preference_color_picker);
    }
}
