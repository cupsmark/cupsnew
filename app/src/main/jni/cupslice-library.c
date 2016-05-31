#include <jni.h>
#include <android/log.h>


JNIEXPORT jstring JNICALL Java_com_cupslicenew_core_helper_HelperNative_getLink(JNIEnv * env, jobject obj, jint i)
{
    if(i == 159180)
    {
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/upload/");
    }
    else if(i == 159181)
    {
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/index.php/api/v2/list-recommended-apps?");
    }
    else if(i == 159182)
    {
        //return (*env)->NewStringUTF(env,"http://osc.cupslice.com/index.php/acat/gl_cat?");
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/index.php/api/v2/list-product?");
    }
    else if(i == 159183)
    {
        return (*env)->NewStringUTF(env,"http://osc.cupslice.com/index.php/acat/gl_reg?");
    }
    else if(i == 159184)
    {
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/index.php/api/v2/list-category?");
    }
    else if(i == 159185)
    {
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/index.php/api/v2/list-product-detail?");
    }
    else if(i == 159186)
    {
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/index.php/api/v2/product-download");
    }
    else if(i == 159187)
    {
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/index.php/api/v2/list-message?");
    }
    else if(i == 159188)
    {
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/index.php/api/v2/list-settings-follow?");
    }
    else if(i == 159189)
    {
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/index.php/api/v2/list-htu?");
    }
    else if(i == 159190)
    {
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/index.php/api/v2/about?");
    }
    else if(i == 159191)
    {
        return (*env)->NewStringUTF(env,"http://api.cupslice.com/index.php/api/v2/list-banner?");
    }
    else
    {
        return (*env)->NewStringUTF(env,"no-data");
    }

}

JNIEXPORT void JNICALL Java_com_gpuimage_GPUImageNativeLibrary_YUVtoRBGA(JNIEnv * env, jobject obj, jbyteArray yuv420sp, jint width, jint height, jintArray rgbOut)
{
    int             sz;
    int             i;
    int             j;
    int             Y;
    int             Cr = 0;
    int             Cb = 0;
    int             pixPtr = 0;
    int             jDiv2 = 0;
    int             R = 0;
    int             G = 0;
    int             B = 0;
    int             cOff;
    int w = width;
    int h = height;
    sz = w * h;

    jint *rgbData = (jint*) ((*env)->GetPrimitiveArrayCritical(env, rgbOut, 0));
    jbyte* yuv = (jbyte*) (*env)->GetPrimitiveArrayCritical(env, yuv420sp, 0);

    for(j = 0; j < h; j++) {
             pixPtr = j * w;
             jDiv2 = j >> 1;
             for(i = 0; i < w; i++) {
                     Y = yuv[pixPtr];
                     if(Y < 0) Y += 255;
                     if((i & 0x1) != 1) {
                             cOff = sz + jDiv2 * w + (i >> 1) * 2;
                             Cb = yuv[cOff];
                             if(Cb < 0) Cb += 127; else Cb -= 128;
                             Cr = yuv[cOff + 1];
                             if(Cr < 0) Cr += 127; else Cr -= 128;
                     }
                     R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
                     if(R < 0) R = 0; else if(R > 255) R = 255;
                     G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1) + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
                     if(G < 0) G = 0; else if(G > 255) G = 255;
                     B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
                     if(B < 0) B = 0; else if(B > 255) B = 255;
                     rgbData[pixPtr++] = 0xff000000 + (R << 16) + (G << 8) + B;
             }
    }

    (*env)->ReleasePrimitiveArrayCritical(env, rgbOut, rgbData, 0);
    (*env)->ReleasePrimitiveArrayCritical(env, yuv420sp, yuv, 0);
}

JNIEXPORT void JNICALL Java_com_gpuimage_GPUImageNativeLibrary_YUVtoARBG(JNIEnv * env, jobject obj, jbyteArray yuv420sp, jint width, jint height, jintArray rgbOut)
{
    int             sz;
    int             i;
    int             j;
    int             Y;
    int             Cr = 0;
    int             Cb = 0;
    int             pixPtr = 0;
    int             jDiv2 = 0;
    int             R = 0;
    int             G = 0;
    int             B = 0;
    int             cOff;
    int w = width;
    int h = height;
    sz = w * h;

    jint *rgbData = (jint*) ((*env)->GetPrimitiveArrayCritical(env, rgbOut, 0));
    jbyte* yuv = (jbyte*) (*env)->GetPrimitiveArrayCritical(env, yuv420sp, 0);

    for(j = 0; j < h; j++) {
             pixPtr = j * w;
             jDiv2 = j >> 1;
             for(i = 0; i < w; i++) {
                     Y = yuv[pixPtr];
                     if(Y < 0) Y += 255;
                     if((i & 0x1) != 1) {
                             cOff = sz + jDiv2 * w + (i >> 1) * 2;
                             Cb = yuv[cOff];
                             if(Cb < 0) Cb += 127; else Cb -= 128;
                             Cr = yuv[cOff + 1];
                             if(Cr < 0) Cr += 127; else Cr -= 128;
                     }
                     R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
                     if(R < 0) R = 0; else if(R > 255) R = 255;
                     G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1) + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
                     if(G < 0) G = 0; else if(G > 255) G = 255;
                     B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
                     if(B < 0) B = 0; else if(B > 255) B = 255;
                     rgbData[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
             }
    }

    (*env)->ReleasePrimitiveArrayCritical(env, rgbOut, rgbData, 0);
    (*env)->ReleasePrimitiveArrayCritical(env, yuv420sp, yuv, 0);
}
