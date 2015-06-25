package com.cngu.shades.presenter;

import android.content.Intent;

import com.cngu.shades.R;
import com.cngu.shades.fragment.ShadesFragment;
import com.cngu.shades.helper.FilterCommandFactory;
import com.cngu.shades.helper.FilterCommandSender;
import com.cngu.shades.model.SettingsModel;
import com.cngu.shades.service.ScreenFilterService;

public class ShadesPresenter implements SettingsModel.OnSettingsChangedListener {
    private static final String TAG = "ShadesPresenter";
    private static final boolean DEBUG = true;

    private ShadesFragment mView;
    private SettingsModel mSettingsModel;
    private FilterCommandFactory mFilterCommandFactory;
    private FilterCommandSender mFilterCommandSender;

    private boolean mSendingCommand = false;

    public ShadesPresenter(ShadesFragment view, SettingsModel settingsModel,
                           FilterCommandFactory filterCommandFactory,
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
        mSettingsModel = settingsModel;
        mFilterCommandFactory = filterCommandFactory;
        mFilterCommandSender = filterCommandSender;
    }

    public void onStart() {
        boolean poweredOn = mSettingsModel.getShadesPowerState();
        boolean paused = mSettingsModel.getShadesPauseState();
        setShadesFabIcon(poweredOn, paused);
    }

    private void setShadesFabIcon(boolean poweredOn, boolean pauseState) {
        int iconResId;
        if (!poweredOn || pauseState) {
            iconResId = R.drawable.ic_shades_on;
        } else {
            iconResId = R.drawable.ic_shades_off;
        }

        mView.setShadesFabIcon(iconResId);
    }

    public void onShadesFabClicked() {
        if (mSendingCommand) {
            return;
        }

        mSendingCommand = true;

        Intent command;
        if (mSettingsModel.getShadesPowerState()) {
            command = mFilterCommandFactory.createCommand(ScreenFilterService.COMMAND_OFF);
        } else {
            command = mFilterCommandFactory.createCommand(ScreenFilterService.COMMAND_ON);
        }

        mFilterCommandSender.send(command);
    }

    //region OnSettingsChangedListener
    @Override
    public void onShadesPowerStateChanged(boolean powerState) {
        setShadesFabIcon(powerState, mSettingsModel.getShadesPauseState());

        mSendingCommand = false;
    }

    @Override
    public void onShadesPauseStateChanged(boolean pauseState) {
        setShadesFabIcon(mSettingsModel.getShadesPowerState(), pauseState);
    }

    @Override
    public void onShadesDimLevelChanged(int dimLevel) {/* do nothing */}

    @Override
    public void onShadesColorChanged(int color) {/* do nothing */}
    //endregion
}
