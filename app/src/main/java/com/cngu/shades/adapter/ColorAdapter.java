package com.cngu.shades.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cngu.shades.R;

public class ColorAdapter extends BaseAdapter {

    private static int[] COLORS;
    private int mSelectedPosition;

    private Context mContext;

    public ColorAdapter(@NonNull Context context) {
        super();

        mContext = context;
        mSelectedPosition = -1;

        initColorsList(context.getResources());
    }

    private void initColorsList(Resources resources) {
        COLORS = new int[] {
                resources.getColor(android.R.color.black),
                resources.getColor(R.color.red_500),
                resources.getColor(R.color.amber_500),
                resources.getColor(R.color.indigo_500),
                resources.getColor(R.color.green_500),
                resources.getColor(R.color.blue_grey_500),
                resources.getColor(R.color.pink_500),
                resources.getColor(R.color.deep_orange_500),
                resources.getColor(R.color.deep_purple_500),
                resources.getColor(R.color.teal_500)
        };
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    @Override
    public int getCount() {
        return COLORS.length;
    }

    @Override
    public Object getItem(int position) {
        return COLORS[position];
    }

    public int getPosition(int colorItem) {
        for (int i = 0; i < COLORS.length; i++) {
            if (colorItem == COLORS[i]) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Not used.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView selectableColorView;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            selectableColorView = (ImageView) inflater.inflate(R.layout.widget_selectable_color, parent, false);
        } else {
            selectableColorView = (ImageView) convertView;
        }

        // Color the background circle
        LayerDrawable background = (LayerDrawable) selectableColorView.getBackground();
        GradientDrawable backgroundCircle = (GradientDrawable) background.getDrawable(0);
        backgroundCircle.setColor(COLORS[position]);

        // If currently selected, show a check mark icon
        if (position == mSelectedPosition) {
            selectableColorView.setImageResource(R.drawable.ic_done);
            selectableColorView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        } else {
            selectableColorView.setImageDrawable(null);
        }

        return selectableColorView;
    }
}
