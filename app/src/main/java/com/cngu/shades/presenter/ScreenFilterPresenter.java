package com.cngu.shades.presenter;

import android.content.Intent;

import com.cngu.shades.manager.WindowViewManager;
import com.cngu.shades.view.ScreenFilterView;

public class ScreenFilterPresenter {

    private ScreenFilterView view;
    private WindowViewManager windowViewManager;

    public ScreenFilterPresenter(ScreenFilterView v, WindowViewManager wvm) {
        view = v;
        view.registerPresenter(this);

        windowViewManager = wvm;
    }

    public void onFilterCommand(Intent command) {

    }

}
