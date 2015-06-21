package com.cngu.shades.helpers;

import android.content.Context;
import android.content.Intent;

/**
 * Helper class to send commands to {@link com.cngu.shades.service.ScreenFilterService}.
 *
 * <p>Use {@link FilterCommandFactory} to create valid commands.
 */
public class FilterCommandSender {

    private Context context;

    public FilterCommandSender(Context context) {
        this.context = context;
    }

    public void send(Intent command) {
        context.startService(command);
    }
}
