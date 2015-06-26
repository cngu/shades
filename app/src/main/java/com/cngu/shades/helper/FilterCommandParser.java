package com.cngu.shades.helper;

import android.content.Intent;
import android.graphics.Point;

import com.cngu.shades.service.ScreenFilterService;

/**
 * Helper class that encapsulates the logic to parse an {@link Intent} that was created by
 * {@link FilterCommandFactory} and sent to {@link ScreenFilterService}.
 */
public class FilterCommandParser {

    /**
     * Retrieves the command in an intent sent to {@link ScreenFilterService}.
     *
     * @param intent that was constructed by {@link FilterCommandFactory}.
     * @return one of {@link ScreenFilterService#COMMAND_OFF}, {@link ScreenFilterService#COMMAND_ON},
     *         {@link ScreenFilterService#COMMAND_PAUSE}, or -1 if {@code intent} doesn't contain a
     *         valid command.
     */
    public int parseCommandFlag(Intent intent) {
        int errorCode = -1;

        if (intent == null) {
            return errorCode;
        }

        int commandFlag = intent.getIntExtra(ScreenFilterService.BUNDLE_KEY_COMMAND, errorCode);
        return commandFlag;
    }
}
