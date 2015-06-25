package com.cngu.shades.presenter;

import android.content.Intent;

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

        initializeShadesFabIcon();
    }

    private void initializeShadesFabIcon() {
        
    }

    public void onShadesFabClicked() {
        Intent command = mFilterCommandFactory.createCommand(ScreenFilterService.COMMAND_ON);

        mFilterCommandSender.send(command);
    }

    @Override
    public void onShadesDimLevelChanged(int dimLevel) {

    }

    @Override
    public void onShadesColorChanged(int color) {

    }
}
