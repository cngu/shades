package com.cngu.shades.view;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cngu.shades.R;
import com.cngu.shades.presenter.ScreenFilterPresenter;

public class ScreenFilterView extends WindowView {
    private static final String TAG = "ScreenFilterView";
    private static final boolean DEBUG = true;

    private ScreenFilterPresenter presenter;
    private ImageView screenFilter;

    public ScreenFilterView(Context context) {
        super(context);
    }

    @Override
    protected int getContentLayoutResId() {
        return R.layout.view_screen_filter;
    }

    @Override
    protected void onFinishInflate() {
        screenFilter = (ImageView) findViewById(R.id.screen_filter_imageview);

        super.onFinishInflate();
    }

    @Override
    public WindowManager.LayoutParams getWindowLayoutParams() {
        return null;
    }

    public void setOpacity(int opacity) {

    }

    public void setColor(int color) {

    }

    public void registerPresenter(ScreenFilterPresenter presenter) {
        if (presenter == null) {
            throw new IllegalArgumentException("presenter cannot be null");
        }
        this.presenter = presenter;

        if (DEBUG) Log.i(TAG, "Registered Presenter");
    }
}
