package com.cngu.shades.helpers;

import android.content.Intent;
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
     *         {@link ScreenFilterService#COMMAND_PAUSE}, {@link ScreenFilterService#COMMAND_RESUME}.
     */
    public int getCommand(Intent intent) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
