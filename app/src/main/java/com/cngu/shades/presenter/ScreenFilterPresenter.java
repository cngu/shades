package com.cngu.shades.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.cngu.shades.helpers.FilterCommandParser;
import com.cngu.shades.manager.WindowViewManager;
import com.cngu.shades.service.ScreenFilterService;
import com.cngu.shades.view.ScreenFilterView;

public class ScreenFilterPresenter {
    private static final String TAG = "ScreenFilterPresenter";
    private static final boolean DEBUG = true;

    private ScreenFilterView view;
    private WindowViewManager windowViewManager;
    private FilterCommandParser filterCommandParser;

    private State state;
    private State onState;
    private State offState;
    private State pauseState;
    private State resumeState;

    public ScreenFilterPresenter(ScreenFilterView view, WindowViewManager windowViewManager,
                                 FilterCommandParser filterCommandParser) {
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }
        if (windowViewManager == null) {
            throw new IllegalArgumentException("windowViewManager cannot be null");
        }
        if (filterCommandParser == null) {
            throw new IllegalArgumentException("filterCommandParser cannot be null");
        }

        this.view = view;
        this.windowViewManager = windowViewManager;
        this.filterCommandParser = filterCommandParser;

        view.registerPresenter(this);

        initializeStates();
    }

    private void initializeStates() {
        onState = new OnState();
        offState = new OffState();
        pauseState = new PauseState();
        resumeState = new ResumeState();

        // TODO: What should the default state be?
        state = offState;
    }

    public void onScreenFilterCommand(Intent command) {
        int commandFlag = filterCommandParser.parseCommandFlag(command);

        if (DEBUG) {
            Log.i(TAG, String.format("Handling command: %d in current state: %s", commandFlag, state));
        }

        state.onScreenFilterCommand(commandFlag);
    }

    private void moveToState(State newState) {
        if (newState == null) {
            throw new IllegalArgumentException("newState cannot be null");
        }

        if (DEBUG) Log.i(TAG, String.format("Transitioning state from %s to %s", state, newState));

        state = newState;
    }

    private abstract class State {

        protected abstract void onScreenFilterCommand(int commandFlag);

        @Override
        public String toString() {
            return getClass().getSimpleName();
        }
    }

    private class OnState extends State {
        @Override
        protected void onScreenFilterCommand(int commandFlag) {
            if (commandFlag == ScreenFilterService.COMMAND_ON) {
                windowViewManager.closeWindow(view);
                moveToState(offState);
            }
        }
    }

    private class OffState extends State {
        @Override
        protected void onScreenFilterCommand(int commandFlag) {
            if (commandFlag == ScreenFilterService.COMMAND_ON) {
                view.setFilterDimLevel(50);
                view.setFilterRgbColor(Color.RED);
                windowViewManager.openWindow(view);
                moveToState(onState);
            }
        }
    }

    private class PauseState extends State {
        @Override
        protected void onScreenFilterCommand(int commandFlag) {

        }
    }

    private class ResumeState extends State {
        @Override
        protected void onScreenFilterCommand(int commandFlag) {

        }
    }
}
