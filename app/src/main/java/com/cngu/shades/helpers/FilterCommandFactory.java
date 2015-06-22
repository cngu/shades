package com.cngu.shades.helpers;

import android.content.Context;
import android.content.Intent;

import com.cngu.shades.service.ScreenFilterService;

/**
 * Factory class to construct a valid {@link Intent} commands that can be sent to
 * {@link com.cngu.shades.service.ScreenFilterService}.
 *
 * <p>Use {@link FilterCommandSender} to execute the constructed commands.
 */
public class FilterCommandFactory {

    private Context mContext;

    public FilterCommandFactory(Context context) {
        mContext = context;
    }

    /**
     *
     * @param screenFilterServiceCommand one of {@link ScreenFilterService#COMMAND_OFF},
     *        {@link ScreenFilterService#COMMAND_ON}, {@link ScreenFilterService#COMMAND_PAUSE},
     *        {@link ScreenFilterService#COMMAND_RESUME}.
     * @return an Intent containing a command that can be sent to {@link ScreenFilterService} via
     *         {@link FilterCommandSender#send(Intent)}; null if
     *         {@code screenFilterServiceCommand} is invalid.
     */
    public Intent createCommand(int screenFilterServiceCommand) {
        Intent command;

        if (screenFilterServiceCommand < ScreenFilterService.VALID_COMMAND_START ||
            screenFilterServiceCommand > ScreenFilterService.VALID_COMMAND_END) {
            command = null;
        } else {
            command = new Intent(mContext, ScreenFilterService.class);
            command.putExtra(ScreenFilterService.BUNDLE_KEY_COMMAND, screenFilterServiceCommand);
        }

        return command;
    }
}
