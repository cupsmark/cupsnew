package com.cupslicenew.core.helper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by ekobudiarto on 5/23/16.
 */
public class HelperImage {



    public Bitmap doHighlight(Bitmap src) {
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth() + 96, src.getHeight() + 96, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOut);
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        Paint ptBlur = new Paint();
        ptBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
        int[] offsetXY = new int[2];
        Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);
        Paint ptAlphaColor = new Paint();
        ptAlphaColor.setColor(0xFFFFFFFF);
        canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
        bmAlpha.recycle();
        canvas.drawBitmap(src, 0, 0, null);
        return bmOut;
    }

    public Bitmap doInvert(Bitmap src) {
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int A, R, G, B;
        int pixelColor;
        int height = src.getHeight();
        int width = src.getWidth();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                pixelColor = src.getPixel(x, y);
                A = Color.alpha(pixelColor);
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public Bitmap doGamma(Bitmap src) {
        double red = 0.6;
        double green = 0.6;
        double blue = 0.6;

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int width = src.getWidth();
        int height = src.getHeight();
        int A, R, G, B;
        int pixel;
        final int    MAX_SIZE = 256;
        final double MAX_VALUE_DBL = 255.0;
        final int    MAX_VALUE_INT = 255;
        final double REVERSE = 1.0;

        int[] gammaR = new int[MAX_SIZE];
        int[] gammaG = new int[MAX_SIZE];
        int[] gammaB = new int[MAX_SIZE];

        for(int i = 0; i < MAX_SIZE; ++i) {
            gammaR[i] = (int)Math.min(MAX_VALUE_INT,
                    (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
            gammaG[i] = (int)Math.min(MAX_VALUE_INT,
                    (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
            gammaB[i] = (int)Math.min(MAX_VALUE_INT,
                    (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
        }

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = gammaR[Color.red(pixel)];
                G = gammaG[Color.green(pixel)];
                B = gammaB[Color.blue(pixel)];
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public Bitmap doGreyscale(Bitmap src) {

        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int A, R, G, B;
        int pixel;
        int width = src.getWidth();
        int height = src.getHeight();

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public Bitmap doColorFilter(Bitmap src) {

        double red = 50;
        double green = 50;
        double blue = 50;

        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        int A, R, G, B;
        int pixel;

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel);
                R = (int)(Color.red(pixel) * red);
                G = (int)(Color.green(pixel) * green);
                B = (int)(Color.blue(pixel) * blue);
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public Bitmap rotate(Bitmap src, float degree) {

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public Bitmap doRoundedCorner(Bitmap src, float round) {

        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(0, 0, 0, 0);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        canvas.drawRoundRect(rectF, round, round, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);
        return result;
    }

    public static Bitmap doWatermark(Bitmap src, String watermark, Point location, int size) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Paint paint = new Paint();
        //paint.setColor(color);
        paint.setTextSize(size);
        paint.setAntiAlias(true);
        canvas.drawText(watermark, location.x, location.y, paint);

        return result;
    }

    public Bitmap doFlip(Bitmap src, int type) {

        // type 1 = flip vertical
        // type 2 = flip horizontal
        Matrix matrix = new Matrix();
        if(type == 1) {
            matrix.preScale(1.0f, -1.0f);
        }
        else if(type == 2) {
            matrix.preScale(-1.0f, 1.0f);
        } else {
            return null;
        }
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public Bitmap doFilterFlea(Bitmap src) {

        int width = src.getWidth();
        int height = src.getHeight();
        int COLOR_MIN = 0x00;
        int COLOR_MAX = 0xFF;
        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);
        Random random = new Random();
        int index = 0;
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                index = y * width + x;
                int randColor = Color.rgb(random.nextInt(COLOR_MAX),
                        random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX));
                pixels[index] |= randColor;
            }
        }
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    public Bitmap doFilterBlack(Bitmap src) {

        int COLOR_MIN = 0x00;
        int COLOR_MAX = 0xFF;
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);
        // random object
        Random random = new Random();

        int R, G, B, index = 0, thresHold = 0;
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                index = y * width + x;
                R = Color.red(pixels[index]);
                G = Color.green(pixels[index]);
                B = Color.blue(pixels[index]);
                thresHold = random.nextInt(COLOR_MAX);
                if(R < thresHold && G < thresHold && B < thresHold) {
                    pixels[index] = Color.rgb(COLOR_MIN, COLOR_MIN, COLOR_MIN);
                }
            }
        }
        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    public Bitmap doFilterSnow(Bitmap src) {

        int COLOR_MIN = 0x00;
        int COLOR_MAX = 0xFF;
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);
        Random random = new Random();
        int R, G, B, index = 0, thresHold = 50;
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                index = y * width + x;
                R = Color.red(pixels[index]);
                G = Color.green(pixels[index]);
                B = Color.blue(pixels[index]);
                thresHold = random.nextInt(COLOR_MAX);
                if(R > thresHold && G > thresHold && B > thresHold) {
                    pixels[index] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX);
                }
            }
        }
        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    public Bitmap doShading(Bitmap src) {

        int shadingColor = 0xff00ff00;//this.shadingColor;
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        int index = 0;
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                index = y * width + x;
                pixels[index] &= shadingColor;
            }
        }
        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    public Bitmap doReflection(Bitmap src) {

        final int reflectionGap = 4;
        int width = src.getWidth();
        int height = src.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(src, 0, height/2, width, height/2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(src, 0, 0, null);
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, src.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
                Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
        return bitmapWithReflection;
    }

    public Bitmap doMerge(Bitmap src, Bitmap dst) {
        Bitmap cs = null;
        Bitmap c = src;
        Bitmap s = dst;
        int width, height = 0;
        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth();
            height = c.getHeight();
        } else {
            width = c.getWidth();
            height = c.getHeight();
        }

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(c, new Matrix(), null);
        comboImage.drawBitmap(s, null, new Rect(0, 0, width, height), paint);
        return cs;
    }

    private Bitmap doAlpha(float alpha, Bitmap src)
    {
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        paint.setAlpha((int) alpha);
        Bitmap cs = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(src, new Matrix(), paint);
        return cs;
    }

    public static String doSave(Activity activity, Bitmap bitmap, String dir, String filename)
    {
        String path = "";
        try
        {
            File myDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + dir + File.separator);
            if(!myDir.exists())
            {
                myDir.mkdirs();
            }

            final File file = new File(myDir,filename);
            if(file.exists()) file.delete();
            final FileOutputStream _out = new FileOutputStream(file);

            SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("rate", "rated");
            editor.commit();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, _out);
            _out.flush();
            _out.close();
            path = file.getAbsolutePath().toString();
        }
        catch(Exception e)
        {
            path = "";
            e.printStackTrace();
        }
        return path;
    }

    public static void doPushToGallery(Activity activity, File file)
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.DISPLAY_NAME, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static Bitmap doColorOverlay(int color,Bitmap source)
    {
        if (color == 0) {
            color = 0xffffffff;
        }

        Bitmap maskBitmap = source;
        final int width = maskBitmap.getWidth();
        final int height = maskBitmap.getHeight();

        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawBitmap(maskBitmap, 0, 0, null);

        Paint maskedPaint = new Paint();
        maskedPaint.setColor(color);
        maskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        canvas.drawRect(0, 0, width, height, maskedPaint);

        return outBitmap;
    }

    public static float range(final int percentage, final float start, final float end) {
        return (end - start) * percentage / 100.0f + start;
    }
    public static int range(final int percentage, final int start, final int end) {
        return (end - start) * percentage / 100 + start;
    }

    public static Bitmap doResizeFromBitmap(Bitmap bm, int maxWidth, int maxHeight, boolean increase){

        int bmpWidth = bm.getWidth();
        int bmpHeight = bm.getHeight();

        int newWidth = 0;
        int newHeight = 0;

        if(bmpWidth < maxWidth || bmpHeight < maxHeight)
        {
            if(increase){
                if(bmpWidth > bmpHeight)
                {
                    double ratio = ((double) maxWidth) / bmpWidth;
                    newWidth = (int) (ratio * bmpWidth);
                    newHeight = (int) (ratio * bmpHeight);
                }
                else
                {
                    double ratio = ((double) maxHeight) / bmpHeight;
                    newWidth = (int) (ratio * bmpWidth);
                    newHeight = (int) (ratio * bmpHeight);
                }
            }
            else {
                newWidth = bmpWidth;
                newHeight = bmpHeight;
            }


        }
        else
        {
            if(bmpWidth > bmpHeight)
            {
                double ratio = ((double) maxWidth) / bmpWidth;
                newWidth = (int) (ratio * bmpWidth);
                newHeight = (int) (ratio * bmpHeight);
            }
            else
            {
                double ratio = ((double) maxHeight) / bmpHeight;
                newWidth = (int) (ratio * bmpWidth);
                newHeight = (int) (ratio * bmpHeight);
            }
        }


        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return resizedBitmap;
    }

    public static Bitmap doResizeFromPath(String url, int maxWidth, int maxHeight, boolean increase){

        Bitmap bm = BitmapFactory.decodeFile(url);
        int bmpWidth = bm.getWidth();
        int bmpHeight = bm.getHeight();

        int newWidth = 0;
        int newHeight = 0;

        if(bmpWidth < maxWidth || bmpHeight < maxHeight)
        {
            if(increase){
                if(bmpWidth > bmpHeight)
                {
                    double ratio = ((double) maxWidth) / bmpWidth;
                    newWidth = (int) (ratio * bmpWidth);
                    newHeight = (int) (ratio * bmpHeight);
                }
                else
                {
                    double ratio = ((double) maxHeight) / bmpHeight;
                    newWidth = (int) (ratio * bmpWidth);
                    newHeight = (int) (ratio * bmpHeight);
                }
            }
            else {
                newWidth = bmpWidth;
                newHeight = bmpHeight;
            }


        }
        else
        {
            if(bmpWidth > bmpHeight)
            {
                double ratio = ((double) maxWidth) / bmpWidth;
                newWidth = (int) (ratio * bmpWidth);
                newHeight = (int) (ratio * bmpHeight);
            }
            else
            {
                double ratio = ((double) maxHeight) / bmpHeight;
                newWidth = (int) (ratio * bmpWidth);
                newHeight = (int) (ratio * bmpHeight);
            }
        }


        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return resizedBitmap;
    }
}

