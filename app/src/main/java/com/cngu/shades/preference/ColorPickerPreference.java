package com.cngu.shades.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import com.cngu.shades.R;
import com.cngu.shades.adapter.ColorAdapter;
import com.cngu.shades.widget.ColorPicker;

public class ColorPickerPreference extends Preference {

    // Changes to DEFAULT_VALUE should be reflected in preferences.xml
    public static final int DEFAULT_VALUE = 0xFF000000;

    private ColorPicker mColorPicker;
    private ColorAdapter mColorAdapter;
    private int mSelectedColor;

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mColorAdapter = new ColorAdapter(context);

        setLayoutResource(R.layout.preference_color_picker);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mSelectedColor = getPersistedInt(DEFAULT_VALUE);
        } else {
            mSelectedColor = (Integer) defaultValue;
            persistInt(mSelectedColor);
        }
    }

    @Override
    protected void onBindView(final View view) {
        super.onBindView(view);

        mColorPicker = (ColorPicker) view.findViewById(R.id.color_picker);
        initLayout();
    }

    private void initLayout() {
        int selectedColorPosition = mColorAdapter.getPosition(mSelectedColor);
        if (selectedColorPosition == -1) {
            throw new IllegalStateException("default color for ColorPickerPreference is not defined");
        }
        mColorAdapter.setSelectedPosition(selectedColorPosition);

        mColorPicker.setAdapter(mColorAdapter);

        mColorPicker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mColorAdapter.setSelectedPosition(position);
                mColorAdapter.notifyDataSetChanged();

                mSelectedColor = (Integer) mColorAdapter.getItem(position);

                persistInt(mSelectedColor);
            }
        });
    }
}
