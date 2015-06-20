package com.cngu.shades.helpers;

import android.content.Intent;

import com.cngu.shades.service.ScreenFilterService;

/**
 * Factory class to construct a valid {@link Intent} that can be sent to
 * {@link com.cngu.shades.service.ScreenFilterService}.
 */
public class FilterCommandFactory {

    /**
     *
     * @param screenFilterServiceCommand one of {@link ScreenFilterService#COMMAND_OFF},
     *        {@link ScreenFilterService#COMMAND_ON}, {@link ScreenFilterService#COMMAND_PAUSE},
     *        {@link ScreenFilterService#COMMAND_RESUME}.
     * @return an Intent containing a command that can be sent to {@link ScreenFilterService} via
     *         {@link android.content.Context#startService(Intent)}.
     */
    public Intent createCommand(int screenFilterServiceCommand) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
