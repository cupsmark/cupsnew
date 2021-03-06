package com.cupslicenew.external.metalev;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/**
 * Created by ekobudiarto on 7/19/16.
 */
public class Image {

    private int resId;
    private static final int UI_MODE_ROTATE = 1, UI_MODE_ANISOTROPIC_SCALE = 2;

    private int mUIMode = UI_MODE_ROTATE;

    private Drawable drawable;

    private boolean firstLoad;

    private int width, height, displayWidth, displayHeight;

    private float centerX, centerY, scaleX, scaleY, angle;

    private float minX, maxX, minY, maxY;

    private static final float SCREEN_MARGIN = 100;

    public Image(int resId, Resources res) {
        this.resId = resId;
        this.firstLoad = true;
        getMetrics(res);
    }

    private void getMetrics(Resources res) {
        DisplayMetrics metrics = res.getDisplayMetrics();
        // The DisplayMetrics don't seem to always be updated on screen rotate, so we hard code a portrait
        // screen orientation for the non-rotated screen here...
        // this.displayWidth = metrics.widthPixels;
        // this.displayHeight = metrics.heightPixels;
        this.displayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
                metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
        this.displayHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
                metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);
    }

    /** Called by activity's onResume() method to load the images */
    public void load(Resources res) {
        getMetrics(res);
        this.drawable = res.getDrawable(resId);
        this.width = drawable.getIntrinsicWidth();
        this.height = drawable.getIntrinsicHeight();
        float cx, cy, sx, sy;
        if (firstLoad) {
            cx = SCREEN_MARGIN + (float) (Math.random() * (displayWidth - 2 * SCREEN_MARGIN));
            cy = SCREEN_MARGIN + (float) (Math.random() * (displayHeight - 2 * SCREEN_MARGIN));
            float sc = (float) (Math.max(displayWidth, displayHeight) / (float) Math.max(width, height) * Math.random() * 0.3 + 0.2);
            sx = sy = sc;
            firstLoad = false;
        } else {
            // Reuse position and scale information if it is available
            // FIXME this doesn't actually work because the whole activity is torn down and re-created on rotate
            cx = this.centerX;
            cy = this.centerY;
            sx = this.scaleX;
            sy = this.scaleY;
            // Make sure the image is not off the screen after a screen rotation
            if (this.maxX < SCREEN_MARGIN)
                cx = SCREEN_MARGIN;
            else if (this.minX > displayWidth - SCREEN_MARGIN)
                cx = displayWidth - SCREEN_MARGIN;
            if (this.maxY > SCREEN_MARGIN)
                cy = SCREEN_MARGIN;
            else if (this.minY > displayHeight - SCREEN_MARGIN)
                cy = displayHeight - SCREEN_MARGIN;
        }
        setPos(cx, cy, sx, sy, 0.0f);
    }

    /** Called by activity's onPause() method to free memory used for loading the images */
    public void unload() {
        this.drawable = null;
    }

    /** Set the position and scale of an image in screen coordinates */
    public boolean setPos(MultiTouchController.PositionAndScale newImgPosAndScale) {
        return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale
                .getScaleX() : newImgPosAndScale.getScale(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale.getScaleY()
                : newImgPosAndScale.getScale(), newImgPosAndScale.getAngle());
        // FIXME: anisotropic scaling jumps when axis-snapping
        // FIXME: affine-ize
        // return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), newImgPosAndScale.getScaleAnisotropicX(),
        // newImgPosAndScale.getScaleAnisotropicY(), 0.0f);
    }

    /** Set the position and scale of an image in screen coordinates */
    private boolean setPos(float centerX, float centerY, float scaleX, float scaleY, float angle) {
        float ws = (width / 2) * scaleX, hs = (height / 2) * scaleY;
        float newMinX = centerX - ws, newMinY = centerY - hs, newMaxX = centerX + ws, newMaxY = centerY + hs;
        if (newMinX > displayWidth - SCREEN_MARGIN || newMaxX < SCREEN_MARGIN || newMinY > displayHeight - SCREEN_MARGIN
                || newMaxY < SCREEN_MARGIN)
            return false;
        this.centerX = centerX;
        this.centerY = centerY;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.angle = angle;
        this.minX = newMinX;
        this.minY = newMinY;
        this.maxX = newMaxX;
        this.maxY = newMaxY;
        return true;
    }

    /** Return whether or not the given screen coords are inside this image */
    public boolean containsPoint(float scrnX, float scrnY) {
        // FIXME: need to correctly account for image rotation
        return (scrnX >= minX && scrnX <= maxX && scrnY >= minY && scrnY <= maxY);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        float dx = (maxX + minX) / 2;
        float dy = (maxY + minY) / 2;
        drawable.setBounds((int) minX, (int) minY, (int) maxX, (int) maxY);
        canvas.translate(dx, dy);
        canvas.rotate(angle * 180.0f / (float) Math.PI);
        canvas.translate(-dx, -dy);
        drawable.draw(canvas);
        canvas.restore();
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getAngle() {
        return angle;
    }

    // FIXME: these need to be updated for rotation
    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }
}
