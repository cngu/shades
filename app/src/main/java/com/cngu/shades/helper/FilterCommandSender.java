package com.cngu.shades.helper;

import android.content.Context;
import android.content.Intent;

/**
 * Helper class to send commands to {@link com.cngu.shades.service.ScreenFilterService}.
 *
 * <p>Use {@link FilterCommandFactory} to create valid commands.
 */
/*
 * This class is used primarily for dependency injection.
 */
public class FilterCommandSender {

    private Context mContext;

    public FilterCommandSender(Context context) {
        mContext = context;
    }

    public void send(Intent command) {
        mContext.startService(command);
    }
}
