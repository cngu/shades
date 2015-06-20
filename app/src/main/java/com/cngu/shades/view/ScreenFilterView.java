package com.cngu.shades.view;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import com.cngu.shades.presenter.ScreenFilterPresenter;

public class ScreenFilterView extends WindowView {
    private static final String TAG = "ScreenFilterView";
    private static final boolean DEBUG = true;

    private ScreenFilterPresenter presenter;

    public ScreenFilterView(Context context) {
        super(context);
    }

    @Override
    protected int getContentLayoutResId() {
        // TODO: Return a <merge> layout
        return 0;
    }

    @Override
    protected void onFinishInflate() {
        // TODO: Find filter by id

        super.onFinishInflate();
    }

    @Override
    public WindowManager.LayoutParams getWindowLayoutParams() {
        return null;
    }

    public void registerPresenter(ScreenFilterPresenter presenter) {
        if (presenter == null) {
            throw new IllegalArgumentException("presenter cannot be null");
        }
        this.presenter = presenter;

        if (DEBUG) Log.i(TAG, "Registered Presenter");
    }
}
