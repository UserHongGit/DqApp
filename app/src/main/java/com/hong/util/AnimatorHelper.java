package com.hong.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class AnimatorHelper {
    public static void zoomIn(View target) {
        zoom(target, 0.0f);
    }

    public static void zoomOut(View target) {
        zoom(target, 1.0f);
    }

    public static void zoom(View target, float value) {
        getZoomAnimatorSet(target, value).start();
    }

    public static AnimatorSet getZoomAnimatorSet(View target, float value) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(target, View.SCALE_X, new float[]{value});
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(target, View.SCALE_Y, new float[]{value});
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{animatorX, animatorY});
        return animatorSet;
    }
}
