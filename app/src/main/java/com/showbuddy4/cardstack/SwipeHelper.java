package com.showbuddy4.cardstack;

import android.animation.Animator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Jainam on 07-01-2018.
 */

public class SwipeHelper implements View.OnTouchListener {

    private final SwipeStack mSwipeStack;
    private View mObservedView;

    private boolean mListenForTouchEvents;
    private float mDownX;
    private float mDownY;
    private float mInitialX;
    private float mInitialY;
    private int mPointerId;

    private float mRotateDegrees = SwipeStack.DEFAULT_SWIPE_ROTATION;
    private float mOpacityEnd = SwipeStack.DEFAULT_SWIPE_OPACITY;
    private int mAnimationDuration = SwipeStack.DEFAULT_ANIMATION_DURATION;


    public SwipeHelper(SwipeStack swipeStack) {
        mSwipeStack = swipeStack;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mListenForTouchEvents || !mSwipeStack.isEnabled()) {
                    return false;
                }

                v.getParent().requestDisallowInterceptTouchEvent(true);
                mSwipeStack.onSwipeStart();
                mPointerId = event.getPointerId(0);
                mDownX = event.getX(mPointerId);
                mDownY = event.getY(mPointerId);

                return true;

            case MotionEvent.ACTION_MOVE:
                int pointerIndex = event.findPointerIndex(mPointerId);
                if (pointerIndex < 0) return false;

                float dx = event.getX(pointerIndex) - mDownX;
                float dy = event.getY(pointerIndex) - mDownY;

                float newX = mObservedView.getX() + dx;
                float newY = mObservedView.getY() + dy;

                mObservedView.setX(newX);
                mObservedView.setY(newY);

                float dragDistanceX = newX - mInitialX;
                float swipeProgress = Math.min(Math.max(
                        dragDistanceX / mSwipeStack.getWidth(), -1), 1);

                float viewCenterVertical = mObservedView.getY() + (mObservedView.getHeight() / 2);
                float parentFirstThirdVertical = mSwipeStack.getHeight() / 3f;

                if (viewCenterVertical < parentFirstThirdVertical &&
                        mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_TOP) {
                    float dragDistanceY = newY - mInitialY;
                    float swipeProgressY = Math.min(Math.max(
                            dragDistanceY / mSwipeStack.getHeight(), -1), 1);
                    mSwipeStack.onSwipeProgress(swipeProgressY,2);
                }else{
                    mSwipeStack.onSwipeProgress(swipeProgress,0);
                }


                if (mRotateDegrees > 0) {
                    float rotation = mRotateDegrees * swipeProgress;
                    mObservedView.setRotation(rotation);
                }

                if (mOpacityEnd < 1f) {
                    float alpha = 1 - Math.min(Math.abs(swipeProgress * 2), 1);
                    mObservedView.setAlpha(alpha);
                }

                return true;

            case MotionEvent.ACTION_UP:
                v.getParent().requestDisallowInterceptTouchEvent(false);
                mSwipeStack.onSwipeEnd();
                checkViewPosition();

                return true;

        }

        return false;
    }

    private void checkViewPosition() {
        if (!mSwipeStack.isEnabled()) {
            resetViewPosition();
            return;
        }

        float viewCenterHorizontal = mObservedView.getX() + (mObservedView.getWidth() / 2);
        float viewCenterVertical = mObservedView.getY() + (mObservedView.getHeight() / 2);
        float parentFirstThird = mSwipeStack.getWidth() / 3f;
        float parentLastThird = parentFirstThird * 2;

        float parentFirstThirdVertical = mSwipeStack.getHeight() / 3f;

        if (viewCenterHorizontal < parentFirstThird &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_RIGHT) {
            swipeViewToLeft(mAnimationDuration / 2);
        } else if (viewCenterHorizontal > parentLastThird &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_LEFT) {
            swipeViewToRight(mAnimationDuration / 2);
        } else if (viewCenterVertical < parentFirstThirdVertical &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_TOP) {
            swipeViewToTop(mAnimationDuration / 2);
        } else {
            resetViewPosition();
        }
    }

    private void resetViewPosition() {
        mObservedView.animate()
                .x(mInitialX)
                .y(mInitialY)
                .rotation(0)
                .alpha(1)
                .setDuration(mAnimationDuration)
                .setInterpolator(new OvershootInterpolator(1.4f))
                .setListener(null);
    }

    private void swipeViewToLeft(int duration) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .x(-mSwipeStack.getWidth() + mObservedView.getX())
                .rotation(-mRotateDegrees)
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSwipeStack.onViewSwipedToLeft();
                    }
                });
    }

    private void swipeViewToRight(int duration) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .x(mSwipeStack.getWidth() + mObservedView.getX())
                .rotation(mRotateDegrees)
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSwipeStack.onViewSwipedToRight();
                    }
                });
    }

    private void swipeViewToTop(int duration) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .y(-mSwipeStack.getHeight() + mObservedView.getY())
                .rotation(mRotateDegrees)
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mSwipeStack.onViewSwipeToTop();
                    }
                });
    }

    public void registerObservedView(View view, float initialX, float initialY) {
        if (view == null) return;
        mObservedView = view;
        mObservedView.setOnTouchListener(this);
        mInitialX = initialX;
        mInitialY = initialY;
        mListenForTouchEvents = true;
    }

    public void unregisterObservedView() {
        if (mObservedView != null) {
            mObservedView.setOnTouchListener(null);
        }
        mObservedView = null;
        mListenForTouchEvents = false;
    }

    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    public void setRotation(float rotation) {
        mRotateDegrees = rotation;
    }

    public void setOpacityEnd(float alpha) {
        mOpacityEnd = alpha;
    }

    public void swipeViewToLeft() {
        swipeViewToLeft(mAnimationDuration);
    }

    public void swipeViewToRight() {
        swipeViewToRight(mAnimationDuration);
    }

    public void swipeViewToTop() {
        swipeViewToTop(mAnimationDuration);
    }

}
