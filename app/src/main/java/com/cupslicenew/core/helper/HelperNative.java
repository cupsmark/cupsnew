package com.cupslicenew.core.helper;

/**
 * Created by ekobudiarto on 9/18/15.
 */
public class HelperNative {

    static {
        System.loadLibrary("cupslice-library");
    }

    public static native String getLink(int i);
}
