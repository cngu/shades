package com.cngu.shades.view;

import android.content.Context;
import android.view.WindowManager;

import com.cngu.shades.presenter.ScreenFilterPresenter;

public class ScreenFilterView extends WindowView {

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
    public void onFinishInflate() {
        // TODO: Find filter by id

        super.onFinishInflate();
    }

    @Override
    public WindowManager.LayoutParams getWindowLayoutParams() {
        return null;
    }

    public void registerPresenter(ScreenFilterPresenter presenter) {
        this.presenter = presenter;
    }
}
