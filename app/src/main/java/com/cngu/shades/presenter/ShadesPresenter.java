package com.cngu.shades.presenter;

import android.content.Intent;
import android.util.Log;

import com.cngu.shades.fragment.ShadesFragment;
import com.cngu.shades.helper.FilterCommandFactory;
import com.cngu.shades.helper.FilterCommandSender;
import com.cngu.shades.service.ScreenFilterService;

public class ShadesPresenter {
    private static final String TAG = "ShadesPresenter";
    private static final boolean DEBUG = true;

    private ShadesFragment mView;
    private FilterCommandFactory mFilterCommandFactory;
    private FilterCommandSender mFilterCommandSender;

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

        mView = view;
        mFilterCommandFactory = filterCommandFactory;
        mFilterCommandSender = filterCommandSender;

        mView.registerPresenter(this);

        if (DEBUG) Log.i(TAG, "Registered View");
    }

    public void onShadesFabClicked() {
        Intent command = mFilterCommandFactory.createCommand(ScreenFilterService.COMMAND_ON);

        mFilterCommandSender.send(command);
    }
}
