package com.cupslicenew.core.view;

/**
 * Created by ekobudiarto on 9/7/15.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;

import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperImage;
import com.cupslicenew.core.util.DisplayUtil;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;


public class ViewCoreSticker extends View {

    private float mScaleSize;

    public static final float MAX_SCALE_SIZE = 4.0f;
    public static final float MIN_SCALE_SIZE = 0.1f;


    private float[] mOriginPoints;
    private float[] mPoints;
    private RectF mOriginContentRect;
    private RectF mContentRect;
    private RectF mViewRect;

    private float mLastPointX, mLastPointY;

    private Bitmap mBitmap, icon_rotate, icon_resize, icon_delete;
    private Bitmap mControllerBitmap, mDeleteBitmap;
    private Bitmap mReversalHorBitmap,mReversalVerBitmap;//水平反转和垂直反转bitmap
    private Matrix mMatrix;
    private Paint mPaint, mBorderPaint;
    private float mControllerWidth, mControllerHeight, mDeleteWidth, mDeleteHeight;
    private float mReversalHorWidth,mReversalHorHeight,mReversalVerWidth,mReversalVerHeight;
    private boolean mInController, mInMove;
    private boolean mInReversalHorizontal,mInReversalVertical;

    private boolean mDrawController = true;
    //private boolean mCanTouch;
    private float mStickerScaleSize = 1.0f;

    private OnStickerDeleteListener mOnStickerDeleteListener;



    int color;
    double opac;
    Bitmap tempBitmap;
    boolean isTouch = false;
    Context mContext;

    public ViewCoreSticker(Context context) {
        this(context, null);
        mContext = context;
    }

    public ViewCoreSticker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public ViewCoreSticker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4.0f);
        mPaint.setColor(Color.WHITE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

        mBorderPaint = new Paint(mPaint);
        mBorderPaint.setColor(Color.parseColor("#B2ffffff"));
        mBorderPaint.setShadowLayer(DisplayUtil.dip2px(getContext(), 2.0f), 0, 0, Color.parseColor("#33000000"));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_sticker_rotate);
        mControllerWidth = mControllerBitmap.getWidth();
        mControllerHeight = mControllerBitmap.getHeight();

        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_sticker_rotate);
        mDeleteWidth = mDeleteBitmap.getWidth();
        mDeleteHeight = mDeleteBitmap.getHeight();

        mReversalHorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_sticker_resize);
        mReversalHorWidth = mReversalHorBitmap.getWidth();
        mReversalHorHeight = mReversalHorBitmap.getHeight();

        mReversalVerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_sticker_resize);
        mReversalVerWidth = mReversalVerBitmap.getWidth();
        mReversalVerHeight = mReversalVerBitmap.getHeight();

        opac = 255;
        //color = 0xffffffff;

    }

    public void setIconRotate(Bitmap icon_rotate)
    {
        this.icon_rotate = icon_rotate;
    }
    public void setIconDelete(Bitmap icon_delete)
    {
        this.icon_delete = icon_delete;
    }
    public void setIconResize(Bitmap icon_resize)
    {
        this.icon_resize = icon_resize;
    }


    public void setWaterMark(@NonNull Bitmap bitmap) {
        mBitmap = bitmap;
        mStickerScaleSize = 1.0f;
        tempBitmap = bitmap;


        setFocusable(true);
        try {


            float px = mBitmap.getWidth();
            float py = mBitmap.getHeight();

            float cx = px / 2;
            float cy = py / 2;


            mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
            mOriginContentRect = new RectF(0, 0, px, py);
            //mOriginPoints = new float[]{0, 0, cx, 0, cx, cy, 0, cy, cx / 2, cy / 2};
            //mOriginContentRect = new RectF(0, 0, cx, cy);
            mPoints = new float[10];
            mContentRect = new RectF();

            mMatrix = new Matrix();
            //float transtLeft = ((float)DisplayUtil.getDisplayWidthPixels(getContext()) - mBitmap.getWidth()) / 2;
            //float transtTop = ((float)DisplayUtil.getDisplayWidthPixels(getContext()) - mBitmap.getHeight()) / 2;

            float transtLeft = ((float)DisplayUtil.getDisplayWidthPixels(getContext()) - cx) / 2;
            float transtTop = ((float)DisplayUtil.getDisplayWidthPixels(getContext()) - cy) / 2;

            mMatrix.postScale(0.5f,0.5f);
            mMatrix.postTranslate(transtLeft, transtTop);
            //mMatrix.postScale(0.5f,0.5f);


        } catch (Exception e) {
            e.printStackTrace();
        }
        postInvalidate();

    }

    public Matrix getMarkMatrix() {
        return mMatrix;
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null || mMatrix == null) {
            return;
        }

        mMatrix.mapPoints(mPoints, mOriginPoints);

        mMatrix.mapRect(mContentRect, mOriginContentRect);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        if (mDrawController && isFocusable() && isTouch) {
            Path mPath = new Path();
            mPath.moveTo(mPoints[0], mPoints[1]);
            mPath.quadTo(mPoints[0], mPoints[1], mPoints[2], mPoints[3]);
            mPath.moveTo(mPoints[2], mPoints[3]);
            mPath.quadTo(mPoints[2], mPoints[3], mPoints[4], mPoints[5]);
            mPath.moveTo(mPoints[4], mPoints[5]);
            mPath.quadTo(mPoints[4], mPoints[5], mPoints[6], mPoints[7]);
            mPath.moveTo(mPoints[6], mPoints[7]);
            mPath.quadTo(mPoints[6], mPoints[7], mPoints[0], mPoints[1]);
            canvas.drawPath(mPath, mPaint);
            canvas.drawBitmap(mControllerBitmap, mPoints[4] - mControllerWidth / 2, mPoints[5] - mControllerHeight / 2, null);
            //canvas.drawBitmap(mDeleteBitmap, mPoints[0] - mDeleteWidth / 2, mPoints[1] - mDeleteHeight / 2, mBorderPaint);
            //canvas.drawBitmap(mReversalHorBitmap,mPoints[2] - mReversalHorWidth/2,mPoints[3] - mReversalVerHeight/2,null);
            canvas.drawBitmap(mReversalVerBitmap,mPoints[6] - mReversalVerWidth/2,mPoints[7] - mReversalVerHeight/2,null);

            //canvas.drawBitmap(mControllerBitmap,null,new Rect((int)(mPoints[4] - mControllerWidth / 2),(int)(mPoints[5] - mControllerHeight / 2),(int)mControllerWidth,(int)mControllerHeight),null);
            //canvas.drawBitmap(mReversalHorBitmap,null,new Rect((int)(mPoints[2] - mReversalHorWidth / 2),(int)(mPoints[3] - mReversalHorHeight / 2),(int)mReversalHorWidth,(int)mReversalHorHeight),null);
            //canvas.drawBitmap(mReversalVerBitmap,null,new Rect((int)(mPoints[6] - mReversalVerWidth / 2),(int)(mPoints[7] - mReversalVerHeight / 2),(int)mReversalVerWidth,(int)mReversalVerHeight),null);
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mDrawController = false;
        draw(canvas);
        mDrawController = true;
        canvas.save();
        return bitmap;
    }

    public void setShowDrawController(boolean show) {
        mDrawController = show;
    }


    private boolean isInController(float x, float y) {
        int position = 4;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mControllerWidth / 2,
                ry - mControllerHeight / 2,
                rx + mControllerWidth / 2,
                ry + mControllerHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        //   position += 2;
        //}
        return false;

    }

    private boolean isInDelete(float x, float y) {
        int position = 0;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mDeleteWidth / 2,
                ry - mDeleteHeight / 2,
                rx + mDeleteWidth / 2,
                ry + mDeleteHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        //   position += 2;
        //}
        return false;

    }
    //判断点击区域是否在水平反转按钮区域内
    private boolean isInReversalHorizontal(float x,float y){
        int position = 2;
        float rx = mPoints[position];
        float ry = mPoints[position+1];

        RectF rectF = new RectF(rx - mReversalHorWidth/2,ry-mReversalHorHeight/2,rx+mReversalHorWidth/2,ry+mReversalHorHeight/2);
        if (rectF.contains(x,y))
            return true;

        return false;

    }
    //判断点击区域是否在垂直反转按钮区域内
    private boolean isInReversalVertical(float x,float y){
        int position = 6;
        float rx = mPoints[position];
        float ry = mPoints[position+1];

        RectF rectF = new RectF(rx - mReversalVerWidth/2,ry - mReversalVerHeight/2,rx + mReversalVerWidth/2,ry+mReversalVerHeight/2);
        if (rectF.contains(x,y))
            return true;
        return false;
    }

    private boolean mInDelete = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isFocusable()) {
            return super.dispatchTouchEvent(event);
        }
        if (mViewRect == null) {
            mViewRect = new RectF(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInController(x, y)) {
                    mInController = true;
                    mLastPointY = y;
                    mLastPointX = x;
                    isTouch = true;
                    invalidate();
                    break;
                }

                if(isInReversalVertical(x, y)){
                    mInReversalVertical = true;
                    isTouch = true;
                    invalidate();
                    break;
                }

                if (mContentRect.contains(x, y)) {
                    mLastPointY = y;
                    mLastPointX = x;
                    mInMove = true;
                    isTouch = true;
                    invalidate();
                }
                if (!mContentRect.contains(x, y)) {
                    mInMove = false;
                    isTouch = false;
                    mInReversalVertical = false;
                    mInController = false;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mInController = false;
                mInMove = false;
                mInReversalVertical = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                mLastPointX = 0;
                mLastPointY = 0;
                mInController = false;
                mInMove = false;
                mInDelete = false;
                mInReversalHorizontal = false;
                mInReversalVertical = false;
                isTouch = false;
                break;
            case MotionEvent.ACTION_MOVE:
                isTouch = true;
                if (mInController) {

                    mMatrix.postRotate(rotation(event), mPoints[8], mPoints[9]);
                    invalidate();
                    mLastPointX = x;
                    mLastPointY = y;
                    break;

                }

                if(mInReversalVertical)
                {
                    float nowLenght = caculateLength(mPoints[2], mPoints[3]);
                    float touchLenght = caculateLength(event.getX(), event.getY());
                    if ((float) Math.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {
                        float scale = touchLenght / nowLenght;
                        float nowsc = mStickerScaleSize * scale;
                        if (nowsc >= MIN_SCALE_SIZE && nowsc <= MAX_SCALE_SIZE) {
                            mMatrix.postScale(scale, scale, mPoints[8], mPoints[9]);
                            mStickerScaleSize = nowsc;
                        }
                    }

                    invalidate();
                    mLastPointX = x;
                    mLastPointY = y;
                    break;
                }

                if (mInMove == true) { //拖动的操作
                    float cX = x - mLastPointX;
                    float cY = y - mLastPointY;
                    mInController = false;
                    //Log.i("MATRIX_OK", "ma_jiaodu:" + a(cX, cY));

                    if ((float) Math.sqrt(cX * cX + cY * cY) > 2.0f  && canStickerMove(cX, cY)) {
                        //Log.i("MATRIX_OK", "is true to move");
                        mMatrix.postTranslate(cX, cY);
                        postInvalidate();
                        mLastPointX = x;
                        mLastPointY = y;
                    }
                    break;
                }


                return true;

        }
        return true;
    }

    private void doDeleteSticker() {
        setWaterMark(null);
        if (mOnStickerDeleteListener != null) {
            mOnStickerDeleteListener.onDelete();
        }
    }

    //图片水平反转
    private void doReversalHorizontal(){
        float[] floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
        Matrix tmpMatrix = new Matrix();
        tmpMatrix.setValues(floats);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), tmpMatrix, true);
        invalidate();
        mInReversalHorizontal = false;
    }
    //图片垂直反转
    private void doReversalVertical(){
        float[] floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
        Matrix tmpMatrix = new Matrix();
        tmpMatrix.setValues(floats);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), tmpMatrix, true);
        invalidate();
        mInReversalVertical = false;
    }


    private boolean canStickerMove(float cx, float cy) {
        float px = cx + mPoints[8];
        float py = cy + mPoints[9];
        if (mViewRect.contains(px, py)) {
            return true;
        } else {
            return false;
        }
    }


    private float caculateLength(float x, float y) {
        float ex = x - mPoints[8];
        float ey = y - mPoints[9];
        return (float) Math.sqrt(ex*ex + ey*ey);
    }


    private float rotation(MotionEvent event) {
        float  originDegree = calculateDegree(mLastPointX, mLastPointY);
        float nowDegree = calculateDegree(event.getX(), event.getY());
        return nowDegree - originDegree;
    }

    private float calculateDegree(float x, float y) {
        double delta_x = x - mPoints[8];
        double delta_y = y - mPoints[9];
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public interface OnStickerDeleteListener {
        public void onDelete();
    }

    public void setOnStickerDeleteListener(OnStickerDeleteListener listener) {
        mOnStickerDeleteListener = listener;
    }



    public void setColor(int color)
    {
        Bitmap r = tempBitmap;
        mBitmap = HelperImage.doColorOverlay(color, r);
        tempBitmap = mBitmap;
        invalidate();
    }


    public void setOpacity(final int percentage)
    {
        new AsyncTask<Void, Integer, String>()
        {
            Bitmap converted;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                Paint p = new Paint();
                p.setFlags(Paint.FILTER_BITMAP_FLAG);
                p.setAlpha(HelperImage.range(percentage, 100, 255));
                Bitmap cs = Bitmap.createBitmap(tempBitmap.getWidth(), tempBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(cs);
                canvas.drawBitmap(tempBitmap, new Matrix(), p);
                converted = cs;
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(converted != null)
                {
                    mBitmap = converted;
                    invalidate();
                }
            }
        }.execute();

    }
}
