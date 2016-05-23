package com.cupslicenew.core.util;

import android.view.MotionEvent;

public class EclairMotionEvent extends WrapMotionEvent {

	MotionEvent event;
    protected EclairMotionEvent(MotionEvent event) {
            super(event);
            this.event = event;
    }

    public float getX(int pointerIndex) {
            return event.getX(pointerIndex);
    }

    public float getY(int pointerIndex) {
            return event.getY(pointerIndex);
    }

    public int getPointerCount() {
            return event.getPointerCount();
    }

    public int getPointerId(int pointerIndex) {
            return event.getPointerId(pointerIndex);
    }
}
