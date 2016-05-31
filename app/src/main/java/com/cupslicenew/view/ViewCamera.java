package com.cupslicenew.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperDB;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperImage;

import static android.os.Build.*;

/**
 * Created by ekobudiarto on 7/9/15.
 */
public class ViewCamera extends SurfaceView implements SurfaceHolder.Callback {
    private static boolean DEBUGGING = true;
    private static final String LOG_TAG = "CameraPreviewSample";
    private static final String CAMERA_PARAM_ORIENTATION = "orientation";
    private static final String CAMERA_PARAM_LANDSCAPE = "landscape";
    private static final String CAMERA_PARAM_PORTRAIT = "portrait";
    protected Activity mActivity;
    private SurfaceHolder mHolder;
    protected Camera mCamera;
    protected List<Camera.Size> mPreviewSizeList;
    protected List<Camera.Size> mPictureSizeList;
    protected Camera.Size mPreviewSize;
    protected Camera.Size mPictureSize;
    private int mSurfaceChangedCallDepth = 0;
    private int mCameraId;
    private LayoutMode mLayoutMode;
    private int mCenterPosX = -1;
    private int mCenterPosY;

    PreviewReadyCallback mPreviewReadyCallback = null;
    boolean safeToTake = false;
    Uri uri;
    boolean onTouch = false;
    float touchX, touchY;
    Rect targetFocus;
    int autoColorFocusBorder = Color.GREEN;

    public static enum LayoutMode {
        FitToParent, // Scale to the size that no side is larger than the parent
        NoBlank // Scale to the size that no side is smaller than the parent
    };

    public interface PreviewReadyCallback {
        public void onPreviewReady();
    }

    /**
     * State flag: true when surface's layout size is set and surfaceChanged()
     * process has not been completed.
     */
    protected boolean mSurfaceConfiguring = false;

    public ViewCamera(Activity activity, LayoutMode mode) {
        super(activity); // Always necessary
        mActivity = activity;
        mLayoutMode = mode;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setWillNotDraw(false);
    }

    public void setCameraId(int cameraId)
    {
        this.mCameraId = cameraId;
    }

    public void open()
    {
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(mCameraId);
        } else {
            mCamera = Camera.open();
        }
        Camera.Parameters cameraParams = mCamera.getParameters();
        mPreviewSizeList = cameraParams.getSupportedPreviewSizes();
        mPictureSizeList = cameraParams.getSupportedPictureSizes();
        if (cameraParams.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            cameraParams.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceChangedCallDepth++;
        doSurfaceChanged(width, height);
        mSurfaceChangedCallDepth--;
    }

    private void doSurfaceChanged(int width, int height) {
        mCamera.stopPreview();

        Camera.Parameters cameraParams = mCamera.getParameters();
        boolean portrait = isPortrait();

        // The code in this if-statement is prevented from executed again when surfaceChanged is
        // called again due to the change of the layout size in this if-statement.
        if (!mSurfaceConfiguring) {
            Camera.Size previewSize = determinePreviewSize(portrait, width, height);
            Camera.Size pictureSize = determinePictureSize(previewSize);
            if (DEBUGGING) { Log.v(LOG_TAG, "Desired Preview Size - w: " + width + ", h: " + height); }
            mPreviewSize = previewSize;
            mPictureSize = pictureSize;
            mSurfaceConfiguring = adjustSurfaceLayoutSize(previewSize, portrait, width, height);
            // Continue executing this method if this method is called recursively.
            // Recursive call of surfaceChanged is very special case, which is a path from
            // the catch clause at the end of this method.
            // The later part of this method should be executed as well in the recursive
            // invocation of this method, because the layout change made in this recursive
            // call will not trigger another invocation of this method.
            if (mSurfaceConfiguring && (mSurfaceChangedCallDepth <= 1)) {
                return;
            }
        }

        configureCameraParameters(cameraParams, portrait);
        mSurfaceConfiguring = false;

        try {
            mCamera.startPreview();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Failed to start preview: " + e.getMessage());

            // Remove failed size
            mPreviewSizeList.remove(mPreviewSize);
            mPreviewSize = null;

            // Reconfigure
            if (mPreviewSizeList.size() > 0) { // prevent infinite loop
                surfaceChanged(null, 0, width, height);
            } else {
                Toast.makeText(mActivity, "Can't start preview", Toast.LENGTH_LONG).show();
                Log.w(LOG_TAG, "Gave up starting preview");
            }
        }

        if (null != mPreviewReadyCallback) {
            mPreviewReadyCallback.onPreviewReady();
        }
    }

    /**
     /* @param cameraParams
     * @param portrait
     * @param reqWidth must be the value of the parameter passed in surfaceChanged
     * @param reqHeight must be the value of the parameter passed in surfaceChanged
     * @return Camera.Size object that is an element of the list returned from Camera.Parameters.getSupportedPreviewSizes.
     */
    protected Camera.Size determinePreviewSize(boolean portrait, int reqWidth, int reqHeight) {
        // Meaning of width and height is switched for preview when portrait,
        // while it is the same as user's view for surface and metrics.
        // That is, width must always be larger than height for setPreviewSize.
        int reqPreviewWidth; // requested width in terms of camera hardware
        int reqPreviewHeight; // requested height in terms of camera hardware
        if (portrait) {
            reqPreviewWidth = reqHeight;
            reqPreviewHeight = reqWidth;
        } else {
            reqPreviewWidth = reqWidth;
            reqPreviewHeight = reqHeight;
        }

        if (DEBUGGING) {
            Log.v(LOG_TAG, "Listing all supported preview sizes");
            for (Camera.Size size : mPreviewSizeList) {
                Log.v(LOG_TAG, "  w: " + size.width + ", h: " + size.height);
            }
            Log.v(LOG_TAG, "Listing all supported picture sizes");
            for (Camera.Size size : mPictureSizeList) {
                Log.v(LOG_TAG, "  w: " + size.width + ", h: " + size.height);
            }
        }

        // Adjust surface size with the closest aspect-ratio
        float reqRatio = ((float) reqPreviewWidth) / reqPreviewHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : mPreviewSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }

    protected Camera.Size determinePictureSize(Camera.Size previewSize) {
        Camera.Size retSize = null;
        for (Camera.Size size : mPictureSizeList) {
            if (size.equals(previewSize)) {
                return size;
            }
        }

        if (DEBUGGING) { Log.v(LOG_TAG, "Same picture size not found."); }

        // if the preview size is not supported as a picture size
        float reqRatio = ((float) previewSize.width) / previewSize.height;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        for (Camera.Size size : mPictureSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }

    protected boolean adjustSurfaceLayoutSize(Camera.Size previewSize, boolean portrait,
                                              int availableWidth, int availableHeight) {
        float tmpLayoutHeight, tmpLayoutWidth;
        if (portrait) {
            tmpLayoutHeight = previewSize.width;
            tmpLayoutWidth = previewSize.height;
        } else {
            tmpLayoutHeight = previewSize.height;
            tmpLayoutWidth = previewSize.width;
        }

        float factH, factW, fact;
        factH = availableHeight / tmpLayoutHeight;
        factW = availableWidth / tmpLayoutWidth;
        if (mLayoutMode == LayoutMode.FitToParent) {
            // Select smaller factor, because the surface cannot be set to the size larger than display metrics.
            if (factH < factW) {
                fact = factH;
            } else {
                fact = factW;
            }
        } else {
            if (factH < factW) {
                fact = factW;
            } else {
                fact = factH;
            }
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.getLayoutParams();

        int layoutHeight = (int) (tmpLayoutHeight * fact);
        int layoutWidth = (int) (tmpLayoutWidth * fact);
        if (DEBUGGING) {
            Log.v(LOG_TAG, "Preview Layout Size - w: " + layoutWidth + ", h: " + layoutHeight);
            Log.v(LOG_TAG, "Scale factor: " + fact);
        }

        boolean layoutChanged;
        if ((layoutWidth != this.getWidth()) || (layoutHeight != this.getHeight())) {
            layoutParams.height = layoutHeight;
            layoutParams.width = layoutWidth;
            if (mCenterPosX >= 0) {
                layoutParams.topMargin = mCenterPosY - (layoutHeight / 2);
                layoutParams.leftMargin = mCenterPosX - (layoutWidth / 2);
            }
            this.setLayoutParams(layoutParams); // this will trigger another surfaceChanged invocation.
            layoutChanged = true;
        } else {
            layoutChanged = false;
        }

        return layoutChanged;
    }

    /**
     * @param x X coordinate of center position on the screen. Set to negative value to unset.
     * @param y Y coordinate of center position on the screen.
     */
    public void setCenterPosition(int x, int y) {
        mCenterPosX = x;
        mCenterPosY = y;
    }

    protected void configureCameraParameters(Camera.Parameters cameraParams, boolean portrait) {
        if (VERSION.SDK_INT < VERSION_CODES.FROYO) { // for 2.1 and before
            if (portrait) {
                cameraParams.set(CAMERA_PARAM_ORIENTATION, CAMERA_PARAM_PORTRAIT);
            } else {
                cameraParams.set(CAMERA_PARAM_ORIENTATION, CAMERA_PARAM_LANDSCAPE);
            }
        } else { // for 2.2 and later
            int angle;
            Display display = mActivity.getWindowManager().getDefaultDisplay();
            switch (display.getRotation()) {
                case Surface.ROTATION_0: // This is display orientation
                    angle = 90; // This is camera orientation
                    break;
                case Surface.ROTATION_90:
                    angle = 0;
                    break;
                case Surface.ROTATION_180:
                    angle = 270;
                    break;
                case Surface.ROTATION_270:
                    angle = 180;
                    break;
                default:
                    angle = 90;
                    break;
            }
            Log.v(LOG_TAG, "angle: " + angle);
            mCamera.setDisplayOrientation(angle);
        }

        cameraParams.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        cameraParams.setPictureSize(mPictureSize.width, mPictureSize.height);
        if (DEBUGGING) {
            Log.v(LOG_TAG, "Preview Actual Size - w: " + mPreviewSize.width + ", h: " + mPreviewSize.height);
            Log.v(LOG_TAG, "Picture Actual Size - w: " + mPictureSize.width + ", h: " + mPictureSize.height);
        }

        mCamera.setParameters(cameraParams);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    public void stop() {
        if (null == mCamera) {
            return;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public boolean isPortrait() {
        return (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    public void setOneShotPreviewCallback(Camera.PreviewCallback callback) {
        if (null == mCamera) {
            return;
        }
        mCamera.setOneShotPreviewCallback(callback);
    }

    public void setPreviewCallback(Camera.PreviewCallback callback) {
        if (null == mCamera) {
            return;
        }
        mCamera.setPreviewCallback(callback);
    }

    public Camera.Size getPreviewSize() {
        return mPreviewSize;
    }

    public void setOnPreviewReady(PreviewReadyCallback cb) {
        mPreviewReadyCallback = cb;
    }




    public int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public void setFlash(boolean isOn)
    {
        PackageManager pm = mActivity.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(mActivity,mActivity.getResources().getString(R.string.camera_no_feature),Toast.LENGTH_LONG).show();
        }
        else
        {
            if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            {
                Toast.makeText(mActivity,mActivity.getResources().getString(R.string.camera_no_flashlight),Toast.LENGTH_LONG).show();
            }
            else{
                try {
                    Camera.Parameters p = mCamera.getParameters();
                    if (isOn) {
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.camera_flashlight_on), Toast.LENGTH_LONG).show();
                    } else {
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.camera_flashlight_off), Toast.LENGTH_LONG).show();
                    }
                    mCamera.setParameters(p);
                    mCamera.startPreview();
                }catch (Exception ex)
                {
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.camera_error), Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    public void setURI(Uri uri)
    {
        this.uri = uri;
    }

    public void takePicture()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    mCamera.takePicture(shutter, callbackRAW ,callbackJPEG);
                }catch (Exception e)
                {
                    Toast.makeText(mActivity,"Something wrong with camera because "+e.getMessage().toString(),Toast.LENGTH_LONG).show();
                    mCamera.startPreview();
                }
            }
        }, 1000);
    }

    public boolean isFlashSupported()
    {
        Camera.Parameters parameters = mCamera.getParameters();
        return parameters.getFlashMode() == null ? false : true;
    }

    Camera.PictureCallback callbackRAW = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    Camera.PictureCallback callbackJPEG = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {

            new AsyncTask<Void, Integer, Boolean>(){

                ViewLoadingDialog dialog;
                String msg;
                String path;
                boolean isSuccess = false;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mCamera.stopPreview();
                    dialog = new ViewLoadingDialog(mActivity);
                    dialog.setCancelable(false);
                    dialog.show();
                }

                @Override
                protected Boolean doInBackground(Void... voids) {
                    File pictureFile = null;
                    try {
                        pictureFile = HelperGlobal.createImageFile();
                        if (pictureFile == null){
                            safeToTake = false;
                            msg = "";
                        }
                        else {
                            uri = Uri.fromFile(pictureFile);
                            path = pictureFile.getPath();
                        }

                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        //Bitmap source = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        BitmapFactory.Options bounds = new BitmapFactory.Options();
                        bounds.inJustDecodeBounds = true;
                        BitmapFactory.decodeByteArray(data, 0, data.length, bounds);

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        Bitmap source = BitmapFactory.decodeByteArray(data, 0, data.length, opts);

                        ExifInterface exif = new ExifInterface(pictureFile.getCanonicalPath());
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int rotate = 0;
                        int w, h;

                        switch (orientation)
                        {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                if(mCameraId == 0){
                                    rotate = 0;
                                }
                                else{
                                    rotate = 180;
                                }
                                w = source.getWidth();
                                h = source.getHeight();
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                if(mCameraId == 0){
                                    rotate = 270;
                                }
                                else{
                                    rotate = 90;
                                }
                                w = source.getWidth();
                                h = source.getHeight();
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotate = 180;
                                if(mCameraId == 0){
                                    rotate = 180;
                                }
                                else{
                                    rotate = 0;
                                }
                                w = source.getHeight();
                                h = source.getWidth();
                                break;
                            default:
                                if(mCameraId == 0){
                                    rotate = 90;
                                }
                                else{
                                    rotate = 270;
                                }

                                w = source.getHeight();
                                h = source.getWidth();
                                break;
                        }

                        Paint p = new Paint();
                        if(VERSION.SDK_INT < 11)
                        {
                            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
                        }
                        else
                        {
                            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
                        }
                        p.setFlags(Paint.FILTER_BITMAP_FLAG);

                        Matrix m = new Matrix();
                        m.setRotate((float) rotate, (float) (w / 2), (float) (h / 2));

                        Bitmap result = Bitmap.createBitmap(source, 0 , 0 , bounds.outWidth, bounds.outHeight, m, true);
                        result.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                        //mActivity.setOriginalPath(path);
                        HelperDB helper = new HelperDB(mActivity);
                        helper.resetHistory();
                        Bitmap finals = HelperImage.doResizeFromBitmap(result, 1024, 1024, true);
                        HelperGlobal.SetFirstHistory(mActivity, path);
                        isSuccess = true;
                    } catch (FileNotFoundException e) {
                        isSuccess = false;
                        msg = "Error : " + e.getMessage().toString();
                    } catch (IOException e) {
                        isSuccess = false;
                        msg = "Error : "+e.getMessage().toString();
                    }

                    return isSuccess;
                }

                @Override
                protected void onPostExecute(Boolean s) {
                    super.onPostExecute(s);
                    if(dialog != null && dialog.isShowing())
                    {
                        dialog.dismiss();
                    }
                    if(s != null && !s.equals("") && mActivity != null && isSuccess)
                    {
                        /*mActivity.setActivityFrom("cam");
                        mActivity.setMessenger(false);
                        mActivity.setOriginalPath(path);
                        mActivity.CommitPreferences();
                        Intent ix = new Intent(mActivity, ActivityEditor.class);
                        mActivity.startActivity(ix);
                        mActivity.finish();
                        if(mCamera != null)
                        {
                            mCamera.startPreview();
                        }*/

                    }
                    if(s == null || s.equals("") || mActivity == null)
                    {
                        Toast.makeText(mActivity, "Something wrong. Cause " + msg,Toast.LENGTH_LONG).show();
                    }

                }
            }.execute();
        }
    };

    Camera.ShutterCallback shutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(VERSION.SDK_INT >= 14)
                {
                    onTouch = true;
                    touchX = x;
                    touchY = y;
                    autoColorFocusBorder = Color.GREEN;
                    invalidate();
                }

                return true;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(onTouch)
        {
            Paint p = new Paint();
            p.setColor(autoColorFocusBorder);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(4.0f);
            canvas.drawRect(touchX - 90, touchY - 90, touchX + 90, touchY + 90, p);
            targetFocus = new Rect((int)touchX - 90, (int)touchY - 90, (int)touchX + 90, (int)touchY + 90);
            setAutoFocusArea(targetFocus);
        }
    }

    @TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setAutoFocusArea(Rect target)
    {
        Camera.Parameters para = mCamera.getParameters();
        if(para.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO))
        {
            try {
                final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
                Camera.Area focusArea = new Camera.Area(target, 1000);
                focusList.add(focusArea);
                para.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                para.setFocusAreas(focusList);
                para.setMeteringAreas(focusList);
                mCamera.setParameters(para);
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean b, Camera camera) {
                        autoColorFocusBorder = Color.WHITE;
                    }
                });
            }
            catch (Exception ex)
            {
                Toast.makeText(mActivity,"Something wrong with camera because " + ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}

