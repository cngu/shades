package com.cngu.shades.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.cngu.shades.helpers.FilterCommandParser;
import com.cngu.shades.manager.ScreenManager;
import com.cngu.shades.manager.WindowViewManager;
import com.cngu.shades.service.ScreenFilterService;
import com.cngu.shades.view.ScreenFilterView;

public class ScreenFilterPresenter {
    private static final String TAG = "ScreenFilterPresenter";
    private static final boolean DEBUG = true;

    private ScreenFilterView mView;
    private WindowViewManager mWindowViewManager;
    private ScreenManager mScreenManager;
    private FilterCommandParser mFilterCommandParser;

    private State mState;
    private State mOnState;
    private State mOffState;
    private State mPauseState;
    private State mResumeState;

    public ScreenFilterPresenter(ScreenFilterView view, WindowViewManager windowViewManager,
                                 ScreenManager screenManager, FilterCommandParser filterCommandParser) {
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }
        if (windowViewManager == null) {
            throw new IllegalArgumentException("windowViewManager cannot be null");
        }
        if (screenManager == null) {
            throw new IllegalArgumentException("screenManager cannot be null");
        }
        if (filterCommandParser == null) {
            throw new IllegalArgumentException("filterCommandParser cannot be null");
        }

        mView = view;
        mWindowViewManager = windowViewManager;
        mScreenManager = screenManager;
        mFilterCommandParser = filterCommandParser;

        mView.registerPresenter(this);

        initializeStates();
    }

    private void initializeStates() {
        mOnState = new OnState();
        mOffState = new OffState();
        mPauseState = new PauseState();
        mResumeState = new ResumeState();

        // TODO: What should the default state be?
        mState = mOffState;
    }

    public void onScreenFilterCommand(Intent command) {
        int commandFlag = mFilterCommandParser.parseCommandFlag(command);

        if (DEBUG) {
            Log.i(TAG, String.format("Handling command: %d in current state: %s", commandFlag, mState));
        }

        mState.onScreenFilterCommand(commandFlag);
    }

    private WindowManager.LayoutParams createFilterLayoutParams() {
        WindowManager.LayoutParams wlp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                mScreenManager.getScreenHeight(),
                0,
                -mScreenManager.getStatusBarHeightPx(),
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                PixelFormat.TRANSLUCENT);

        wlp.gravity = Gravity.TOP | Gravity.LEFT;

        return wlp;
    }

    private void moveToState(State newState) {
        if (newState == null) {
            throw new IllegalArgumentException("newState cannot be null");
        }

        if (DEBUG) Log.i(TAG, String.format("Transitioning state from %s to %s", mState, newState));

        mState = newState;
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
                mWindowViewManager.closeWindow(mView);
                moveToState(mOffState);
            }
        }
    }

    private class OffState extends State {
        @Override
        protected void onScreenFilterCommand(int commandFlag) {
            if (commandFlag == ScreenFilterService.COMMAND_ON) {
                mView.setFilterDimLevel(100);
                mView.setFilterRgbColor(Color.BLACK);
                mWindowViewManager.openWindow(mView, createFilterLayoutParams());
                moveToState(mOnState);
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
