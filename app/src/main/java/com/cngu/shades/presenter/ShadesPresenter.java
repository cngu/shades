package com.cngu.shades.presenter;

import android.content.Intent;
import android.util.Log;

import com.cngu.shades.fragment.ShadesFragment;
import com.cngu.shades.helpers.FilterCommandFactory;
import com.cngu.shades.service.ScreenFilterService;

public class ShadesPresenter {
    private static final String TAG = "ShadesPresenter";
    private static final boolean DEBUG = true;

    private ShadesFragment view;
    private FilterCommandFactory filterCommandFactory;

    public ShadesPresenter(ShadesFragment view, FilterCommandFactory filterCommandFactory) {
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }
        if (filterCommandFactory == null) {
            throw new IllegalArgumentException("filterCommandFactory cannot be null");
        }

        this.view = view;
        this.filterCommandFactory = filterCommandFactory;

        this.view.registerPresenter(this);

        if (DEBUG) Log.i(TAG, "Registered View");
    }

    public void onShadesFabClicked() {
        Intent command = filterCommandFactory.createCommand(ScreenFilterService.COMMAND_ON);

        // TODO: Refactor this out into a FilterCommandSender.send(Intent command)
        view.startService(command);
    }
}
