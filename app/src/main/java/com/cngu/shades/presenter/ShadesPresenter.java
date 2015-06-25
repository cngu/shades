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

        boolean powerState = mSettingsModel.getShadesPowerState();
        setShadesFabIcon(powerState);
    }

    private void setShadesFabIcon(boolean powerState) {
        int iconResId = powerState ? R.drawable.ic_brightness : R.drawable.ic_shades;
        mView.setShadesFabIcon(iconResId);
    }

    public void onShadesFabClicked() {
        if (mSendingCommand) {
            return;
        }

        mSendingCommand = true;

        Intent command = mFilterCommandFactory.createCommand(ScreenFilterService.COMMAND_ON);
        mFilterCommandSender.send(command);
    }

    //region OnSettingsChangedListener
    @Override
    public void onShadesPowerStateChanged(boolean powerState) {
        setShadesFabIcon(powerState);

        mSendingCommand = false;
    }

    @Override
    public void onShadesDimLevelChanged(int dimLevel) {/* do nothing */}

    @Override
    public void onShadesColorChanged(int color) {/* do nothing */}
    //endregion
}
