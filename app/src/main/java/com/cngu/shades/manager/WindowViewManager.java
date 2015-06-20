package com.cngu.shades.manager;

import android.view.WindowManager;

import com.cngu.shades.view.WindowView;

/**
 * Convenience class that wraps {@link WindowManager} and operates on properly-initialized
 * {@link WindowView}s rather than arbitrary {@link android.view.View}s.
 */
public class WindowViewManager {

    private WindowManager windowManager;

    public WindowViewManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    /**
     * Creates and opens a new Window to display {@code view}.
     * @param view the view to render in the new Window.
     */
    public void openWindow(WindowView view) {
        windowManager.addView(view, view.getWindowLayoutParams());
    }

    /**
     * Closes the Window that is currently displaying {@code view}.
     * @param view the Window containing this view will be closed.
     */
    public void closeWindow(WindowView view) {
        windowManager.removeView(view);
    }
}
