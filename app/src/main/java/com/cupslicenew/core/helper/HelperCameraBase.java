package com.cupslicenew.core.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

/**
 * Created by ekobudiarto on 9/14/15.
 */
public class HelperCameraBase implements HelperCamera.CameraHelperImpl {

    private final Context mContext;

    public HelperCameraBase(final Context context) {
        mContext = context;
    }

    @Override
    public int getNumberOfCameras() {
        return hasCameraSupport() ? 1 : 0;
    }

    @Override
    public Camera openCamera(final int id) {
        return Camera.open();
    }

    @Override
    public Camera openDefaultCamera() {
        return Camera.open();
    }

    @Override
    public boolean hasCamera(final int facing) {
        if (facing == CameraInfo.CAMERA_FACING_BACK) {
            return hasCameraSupport();
        }
        return false;
    }

    @Override
    public Camera openCameraFacing(final int facing) {
        if (facing == CameraInfo.CAMERA_FACING_BACK) {
            return Camera.open();
        }
        return null;
    }

    @Override
    public void getCameraInfo(final int cameraId, final HelperCamera.CameraInfo2 cameraInfo) {
        cameraInfo.facing = CameraInfo.CAMERA_FACING_BACK;
        cameraInfo.orientation = 90;
    }

    private boolean hasCameraSupport() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
