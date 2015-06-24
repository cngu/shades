package com.cngu.shades.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * A {@link View} that performs the necessary configuration in order to be displayed in a Window.
 *
 * <p>See {@link com.cngu.shades.manager.WindowViewManager}.
 */
public abstract class WindowView extends FrameLayout {

    public WindowView(Context context) {
        super(context);

        configureForWindowDisplay(context);
    }

    private void configureForWindowDisplay(Context context) {
        // Set wrap content
        int wrapContent = FrameLayout.LayoutParams.WRAP_CONTENT;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(wrapContent, wrapContent);
        setLayoutParams(lp);

        // Inflate layout into our self because the WindowManager doesn't provide a default ViewGroup
        int layoutResId = getContentLayoutResId();
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(layoutResId, this, true);
    }

    /**
     * @return the resource id of the layout for the contents of this WindowView.
     */
    protected abstract int getContentLayoutResId();
}
