package com.cngu.shades.manager;

import android.view.WindowManager;

import com.cngu.shades.view.WindowView;

/**
 * Convenience class that wraps {@link WindowManager} and operates on properly-initialized
 * {@link WindowView}s rather than arbitrary {@link android.view.View}s.
 */
public class WindowViewManager {

    private WindowManager mWindowManager;

    public WindowViewManager(WindowManager windowManager) {
        mWindowManager = windowManager;
    }

    /**
     * Creates and opens a new Window to display {@code view}.
     * @param view the view to render in the new Window.
     * @param wlp the {@link android.view.WindowManager.LayoutParams} to use when laying out the window.
     */
    public void openWindow(WindowView view, WindowManager.LayoutParams wlp) {
        mWindowManager.addView(view, wlp);
    }

    /**
     * Triggers a Window undergo a screen measurement and layout pass with the provided
     * {@link android.view.WindowManager.LayoutParams}.
     *
     * @param view the Window containing this view will have its LayoutParams set to {@code wlp}.
     * @param wlp the new LayoutParams to set on the Window.
     */
    public void reLayoutWindow(WindowView view, WindowManager.LayoutParams wlp) {
        mWindowManager.updateViewLayout(view, wlp);
    }

    /**
     * Closes the Window that is currently displaying {@code view}.
     * @param view the Window containing this view will be closed.
     */
    public void closeWindow(WindowView view) {
        mWindowManager.removeView(view);
    }
}
