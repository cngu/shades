package com.cngu.shades.presenter;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.cngu.shades.helper.FilterCommandParser;
import com.cngu.shades.manager.ScreenManager;
import com.cngu.shades.manager.WindowViewManager;
import com.cngu.shades.model.SettingsModel;
import com.cngu.shades.receiver.OrientationChangeReceiver;
import com.cngu.shades.service.ScreenFilterService;
import com.cngu.shades.service.ServiceLifeCycleController;
import com.cngu.shades.view.ScreenFilterView;

public class ScreenFilterPresenter implements OrientationChangeReceiver.OnOrientationChangeListener,
                                              SettingsModel.OnSettingsChangedListener {
    private static final String TAG = "ScreenFilterPresenter";
    private static final boolean DEBUG = true;

    private ScreenFilterView mView;
    private SettingsModel mSettingsModel;
    private ServiceLifeCycleController mServiceController;
    private WindowViewManager mWindowViewManager;
    private ScreenManager mScreenManager;
    private FilterCommandParser mFilterCommandParser;

    private boolean mScreenFilterOpen = false;

    private final State mOnState = new OnState();
    private final State mOffState = new OffState();
    private final State mPauseState = new PauseState();
    private State mCurrentState = mOffState;

    public ScreenFilterPresenter(ScreenFilterView view, SettingsModel model,
                                 ServiceLifeCycleController serviceController,
                                 WindowViewManager windowViewManager, ScreenManager screenManager,
                                 FilterCommandParser filterCommandParser) {
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }
        if (model == null) {
            throw new IllegalArgumentException("model cannot be null");
        }
        if (serviceController == null) {
            throw new IllegalArgumentException("serviceController cannot be null");
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
        mSettingsModel = model;
        mServiceController = serviceController;
        mWindowViewManager = windowViewManager;
        mScreenManager = screenManager;
        mFilterCommandParser = filterCommandParser;
    }

    public void onScreenFilterCommand(Intent command) {
        int commandFlag = mFilterCommandParser.parseCommandFlag(command);

        if (DEBUG) Log.i(TAG, String.format("Handling command: %d in current state: %s",
                commandFlag, mCurrentState));

        mCurrentState.onScreenFilterCommand(commandFlag);
    }

    //region OnSettingsChangedListener
    @Override
    public void onShadesPowerStateChanged(boolean powerState) {/* do nothing */}

    @Override
    public void onShadesDimLevelChanged(int dimLevel) {
        mView.setFilterDimLevel(dimLevel);
    }

    @Override
    public void onShadesColorChanged(int color) {
        mView.setFilterRgbColor(color);
    }
    //endregion

    //region OnOrientationChangeListener
    public void onPortraitOrientation() {
        reLayoutScreenFilter();
    }

    public void onLandscapeOrientation() {
        reLayoutScreenFilter();
    }
    //endregion

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
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSLUCENT);

        wlp.gravity = Gravity.TOP | Gravity.START;

        return wlp;
    }

    private void openScreenFilter() {
        if (!mScreenFilterOpen) {
            mWindowViewManager.openWindow(mView, createFilterLayoutParams());
            mScreenFilterOpen = true;

            mSettingsModel.setShadesPowerState(true);
        }
    }

    private void reLayoutScreenFilter() {
        if (mScreenFilterOpen) {
            mWindowViewManager.reLayoutWindow(mView, createFilterLayoutParams());
        }
    }

    private void closeScreenFilter() {
        if (mScreenFilterOpen) {
            mWindowViewManager.closeWindow(mView);
            mScreenFilterOpen = false;

            mSettingsModel.setShadesPowerState(false);
        }
    }

    private void moveToState(State newState) {
        if (newState == null) {
            throw new IllegalArgumentException("newState cannot be null");
        }
        if (DEBUG) Log.i(TAG, String.format("Transitioning state from %s to %s", mCurrentState, newState));

        mCurrentState = newState;

        if (mCurrentState == mOffState) {
            mServiceController.stop();
        }
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
                closeScreenFilter();

                moveToState(mOffState);
            }
        }
    }

    private class OffState extends State {
        @Override
        protected void onScreenFilterCommand(int commandFlag) {
            if (commandFlag == ScreenFilterService.COMMAND_ON) {
                mView.setFilterDimLevel(mSettingsModel.getShadesDimLevel());
                mView.setFilterRgbColor(mSettingsModel.getShadesColor());

                openScreenFilter();

                moveToState(mOnState);
            }
        }
    }

    private class PauseState extends State {
        @Override
        protected void onScreenFilterCommand(int commandFlag) {

        }
    }
}
