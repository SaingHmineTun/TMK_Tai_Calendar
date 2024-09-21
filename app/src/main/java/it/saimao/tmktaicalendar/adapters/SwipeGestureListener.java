package it.saimao.tmktaicalendar.adapters;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private final OnSwipeListener listener;

    public interface OnSwipeListener {
        void onSwipeLeft();

        void onSwipeRight();
    }

    public SwipeGestureListener(OnSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();
        if (Math.abs(diffX) > Math.abs(diffY)) { // Check for horizontal swipe
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    listener.onSwipeRight();
                } else {
                    listener.onSwipeLeft();
                }
                return true;
            }
        }
        return false;
    }
}
