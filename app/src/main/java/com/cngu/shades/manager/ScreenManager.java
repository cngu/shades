package com.cngu.shades.manager;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class ScreenManager {
    private static final String TAG = "ScreenManager";
    private static final boolean DEBUG = true;

    private static final int DEFAULT_NAV_BAR_HEIGHT_DP = 48;
    private static final int DEFAULT_STATUS_BAR_HEIGHT_DP = 25;

    private Context mContext;
    private WindowManager mWindowManager;

    private int mStatusBarHeight = -1;
    private int mNavigationBarHeight = -1;

    public ScreenManager(Context context, WindowManager windowManager) {
        mContext = context;
        mWindowManager = windowManager;
    }

    public int getScreenHeight() {
        Display display = mWindowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);

        return dm.heightPixels + getStatusBarHeightPx() + getNavigationBarHeightPx();
    }

    public int getStatusBarHeightPx() {
        if (mStatusBarHeight == -1) {
            Resources r = mContext.getResources();
            int statusBarHeightId = r.getIdentifier("status_bar_height", "dimen", "android");

            if (statusBarHeightId > 0) {
                mStatusBarHeight = r.getDimensionPixelSize(statusBarHeightId);
                if (DEBUG) Log.i(TAG, "Found Status Bar Height: " + mStatusBarHeight);
            } else {
                mStatusBarHeight = (int) dpToPx(DEFAULT_STATUS_BAR_HEIGHT_DP);
                if (DEBUG) Log.i(TAG, "Using default Status Bar Height: " + mStatusBarHeight);
            }
        }

        return mStatusBarHeight;
    }

    public int getNavigationBarHeightPx() {
        if (mNavigationBarHeight == -1) {
            Resources r = mContext.getResources();
            int navBarHeightId = r.getIdentifier("navigation_bar_height", "dimen", "android");

            if (navBarHeightId > 0) {
                mNavigationBarHeight = r.getDimensionPixelSize(navBarHeightId);
                if (DEBUG) Log.i(TAG, "Found Navigation Bar Height: " + mNavigationBarHeight);
            } else {
                mNavigationBarHeight = (int) dpToPx(DEFAULT_NAV_BAR_HEIGHT_DP);
                if (DEBUG) Log.i(TAG, "Using default Navigation Bar Height: " + mNavigationBarHeight);
            }
        }

        return mNavigationBarHeight;
    }

    private float dpToPx(float dp) {
        Resources r = mContext.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
