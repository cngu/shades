package com.cngu.shades.presenter;

import android.content.Intent;
import android.util.Log;

import com.cngu.shades.fragment.ShadesFragment;
import com.cngu.shades.helpers.FilterCommandFactory;
import com.cngu.shades.helpers.FilterCommandSender;
import com.cngu.shades.service.ScreenFilterService;

public class ShadesPresenter {
    private static final String TAG = "ShadesPresenter";
    private static final boolean DEBUG = true;

    private ShadesFragment view;
    private FilterCommandFactory filterCommandFactory;
    private FilterCommandSender filterCommandSender;

    public ShadesPresenter(ShadesFragment view, FilterCommandFactory filterCommandFactory,
                           FilterCommandSender filterCommandSender) {
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }
        if (filterCommandFactory == null) {
            throw new IllegalArgumentException("filterCommandFactory cannot be null");
        }
        if (filterCommandSender == null) {
            throw new IllegalArgumentException("filterCommandSender cannot be null");
        }

        this.view = view;
        this.filterCommandFactory = filterCommandFactory;
        this.filterCommandSender = filterCommandSender;

        this.view.registerPresenter(this);

        if (DEBUG) Log.i(TAG, "Registered View");
    }

    public void onShadesFabClicked() {
        Intent command = filterCommandFactory.createCommand(ScreenFilterService.COMMAND_ON);

        filterCommandSender.send(command);
    }
}
