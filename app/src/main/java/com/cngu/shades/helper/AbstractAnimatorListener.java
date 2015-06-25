package com.cngu.shades.helper;

import android.animation.Animator;

public abstract class AbstractAnimatorListener implements Animator.AnimatorListener {
    @Override
    public void onAnimationStart(Animator animator) {}

    @Override
    public void onAnimationEnd(Animator animator) {}

    @Override
    public void onAnimationCancel(Animator animator) {}

    @Override
    public void onAnimationRepeat(Animator animator) {}
}
