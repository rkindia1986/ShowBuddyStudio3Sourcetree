package com.showbuddy4.cardstack;

import android.animation.Animator;

/**
 * Created by Jainam on 07-01-2018.
 */

public class AnimationUtils {

    public static abstract class AnimationEndListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {
            // Do nothing
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // Do nothing
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            // Do nothing
        }
    }
}
