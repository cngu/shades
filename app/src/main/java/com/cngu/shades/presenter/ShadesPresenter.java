package com.cngu.shades.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.cngu.shades.R;
import com.cngu.shades.fragment.ShadesFragment;
import com.cngu.shades.helper.FilterCommandFactory;
import com.cngu.shades.helper.FilterCommandSender;
import com.cngu.shades.model.SettingsModel;
import com.cngu.shades.service.ScreenFilterService;

public class ShadesPresenter implements SettingsModel.OnSettingsChangedListener {
    private ShadesFragment mView;
    private SettingsModel mSettingsModel;
    private FilterCommandFactory mFilterCommandFactory;
    private FilterCommandSender mFilterCommandSender;

    public ShadesPresenter(@NonNull ShadesFragment view,
                           @NonNull SettingsModel settingsModel,
                           @NonNull FilterCommandFactory filterCommandFactory,
                           @NonNull FilterCommandSender filterCommandSender) {
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
        Intent command;
        if (mSettingsModel.getShadesPowerState() && !mSettingsModel.getShadesPauseState()) {
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
