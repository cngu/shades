package com.cngu.shades.preference;

import android.content.Context;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;

import com.cngu.shades.R;
import com.cngu.shades.adapter.ColorAdapter;
import com.cngu.shades.widget.ColorPicker;

public class ColorPickerPreference extends Preference {
    private static final String TAG = "ColorPickerPreference";
    private static final boolean DEBUG = true;

    private ColorPicker mColorPicker;
    private ColorAdapter mColorAdapter;

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mColorAdapter = new ColorAdapter(context);

        setLayoutResource(R.layout.preference_color_picker);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        mColorPicker = (ColorPicker) view.findViewById(R.id.color_picker);
        initLayout();
    }

    private void initLayout() {
        mColorPicker.setAdapter(mColorAdapter);

        mColorPicker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO: This should ignore clicks on already-selected-items

                mColorAdapter.setSelectedPosition(position);
                mColorAdapter.notifyDataSetChanged();

                int selectedColor = (Integer) mColorAdapter.getItem(position);
                if (DEBUG) Log.d(TAG, "Selected color: " + Integer.toHexString(selectedColor) + " at index: " + mColorAdapter.getPosition(selectedColor));
                // TODO: persist selected color
            }
        });
    }
}
