package com.cngu.shades.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cngu.shades.helper.FilterCommandFactory;
import com.cngu.shades.helper.FilterCommandSender;
import com.cngu.shades.model.SettingsModel;
import com.cngu.shades.service.ScreenFilterService;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    private static final boolean DEBUG = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG) Log.i(TAG, "Boot broadcast received!");

        FilterCommandSender commandSender = new FilterCommandSender(context);
        FilterCommandFactory commandFactory = new FilterCommandFactory(context);
        Intent onCommand = commandFactory.createCommand(ScreenFilterService.COMMAND_ON);
        Intent pauseCommand = commandFactory.createCommand(ScreenFilterService.COMMAND_PAUSE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SettingsModel settingsModel = new SettingsModel(context.getResources(), sharedPreferences);

        boolean poweredOnBeforeReboot = settingsModel.getShadesPowerState();
        boolean pausedBeforeReboot = settingsModel.getShadesPauseState();

        // Handle "Always open on startup" flag
        boolean alwaysOpenOnBoot = settingsModel.getOpenOnBootFlag();
        if (alwaysOpenOnBoot) {
            if (DEBUG) Log.i(TAG, "\"Always open on startup\" flag was set; starting now.");

            commandSender.send(onCommand);
            return;
        }

        // Handle "Keep running after reboot" flag
        boolean resumeAfterReboot = settingsModel.getResumeAfterRebootFlag();
        if (resumeAfterReboot) {
            if (DEBUG) Log.i(TAG, "\"Keep running after reboot\" flag was set.");

            if (poweredOnBeforeReboot) {
                if (DEBUG) Log.i(TAG, "Shades was on before reboot; resuming state.");

                commandSender.send(pausedBeforeReboot ? pauseCommand : onCommand);
            } else {
                if (DEBUG) Log.i(TAG, "Shades was off before reboot; no state to resume from.");
            }
            return;
        }

        // Allow ScreenFilterService to sync its state and any shared preferences to "off" mode
        Intent offCommand = commandFactory.createCommand(ScreenFilterService.COMMAND_OFF);
        commandSender.send(offCommand);
    }
}
