package com.cupslicenew.core.helper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.model.MHistory;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.view.ViewDialogConfirm;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ekobudiarto on 5/23/16.
 */
public class HelperGlobal {

    public static ArrayList<String> listLocalImage = new ArrayList<String>();
    public static String ADS_ID = "a153340335c49cc";
    public static String FULLSCREEN_ADS_ID = "ccbf3acd50074d55";
    public static String POPUP_RATE_ID = "c778hy9h76367";

    public interface ImagePusher{

        void onImageResult(Bitmap src, int featureType);
    }

    public interface EditorBehavior{

        void setActiveFeature(String feature);
        void setBitmapFeature(Bitmap asset);
        void setOptionBlend(Map<String, String> param);
        void processFeature();
        void updateFeature();
        void onEditApply();
        void onEditCancel();
        void onUndo();
        void onSave();
        void pushImage(Bitmap result);
    }

    public interface FragmentInterface{
        void onNavigate(BaseFragment fragmentSrc, Map<String, String> parameter);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public interface onLoadMoreListener{
        void onLoadMore();
    }

    public static String GU(int obj)
    {
        String res = "";
        res = HelperNative.getLink(obj);
        return res;
    }

    public static String getJSON(String urlData)
    {
        String result = "";
        try{
            URL url = new URL(urlData);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(20 * 1000);
            urlConnection.setReadTimeout(20 * 1000);

            // Check the connection status
            if(urlConnection.getResponseCode() == 200)
            {
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
                urlConnection.disconnect();
            }
            else
            {
               result = "";
            }
        }catch (MalformedURLException e){
            //e.printStackTrace();
            result = "";
        }catch(IOException e){
            //e.printStackTrace();
            result = "";
        }finally {

        }
        // Return the data from specified url
        return result;
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static String getDeviceID(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getNetworkProvider(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperatorName();
    }

    public static String getCountryID(Context c) {
        String result = "";
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            result = tm.getSimCountryIso();
        } else {
            result = tm.getNetworkCountryIso();
        }
        return result.toUpperCase(Locale.US);
    }

    public static void saveInternalBitmap(String filename, Context context, Bitmap bmp) {

        FileOutputStream fos;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public final static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getDeviceVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getAppVersionName(Context context) {
        String result = "";
        try {
            result = "v" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            result = "";
        }
        return result;
    }

    public static int getAppVersionCode(Context context) {
        int result = 0;
        try {
            result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            result = 0;
        }
        return result;
    }

    public static boolean checkIfFileIsImage(String path) {
        boolean result = false;
        String files = path.substring(path.lastIndexOf(".") + 1).toUpperCase(Locale.US);
        String[] ext = new String[]{"JPG", "PNG", "BMP", "JPEG"};
        if (Arrays.asList(ext).contains(files)) {
            result = true;
        }
        return result;
    }

    public static Bitmap getExifBitmap(String file) {
        Bitmap result = null;
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(file, opts);
        ExifInterface exif;
        try {
            exif = new ExifInterface(file);
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

            Matrix matrix = new Matrix();
            if(bm != null){
                matrix.postRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
                result = rotatedBitmap;
            }
            else
            {
                result = null;
            }
        } catch (IOException e) {
            result = null;
            //e.printStackTrace();
        }
        return result;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static byte[] convertBitmapToByteArray(Context context, Bitmap bitmap) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer);
        return buffer.toByteArray();
    }


    /*public static String postJSON(String url,String[] field, String[] value)
    {
        InputStream is = null;
        String result = "";

        // HTTP
        try {
            HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> paramPost = new ArrayList<NameValuePair>();
            for(int i = 0;i < field.length;i++)
            {
                paramPost.add(new BasicNameValuePair(field[i],value[i]));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(paramPost));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e) {
            return "0";
        }

        // Read response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch(Exception e) {
            return "0";
        }


        return result;
    }*/


    public static void openApplicationInPlayStore(Context context, String packages) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packages)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packages)));
        }
    }

    public static void openAppURL(Context context, String url) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    public static File createImageFile(){
        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "JPG_" + timeStamp + ".jpeg");


        return mediaFile;
    }

    public static String createImageFileCameraDefault() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        String mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return mCurrentPhotoPath;
    }

    public static ArrayList<String> getAllShownImagesPath(Context context) {
        ArrayList<String> list_album = new ArrayList<String>();
        String[] PROJECTION_BUCKET = {
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA,
                "count(" + MediaStore.Images.ImageColumns.DATA + ") as COUNTS"
        };


        String BUCKET_GROUP_BY =
                "1) GROUP BY 1,(2";
        String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

        // Get the base URI for the People table in the Contacts content provider.
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = context.getContentResolver().query(
                images, PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);


        if (cur.moveToFirst()) {
            String bucket;
            String date;
            String data;
            String nums;
            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);
            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);
            int numColumn = cur.getColumnIndex("COUNTS");

            int i = 0;
            do {
                // Get the field values
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                data = cur.getString(dataColumn);
                nums = cur.getString(numColumn);

                File f = new File(data);
                String path_image = Uri.fromFile(f).toString();
                String realpath = data.substring(0, data.lastIndexOf("/"));
                list_album.add(bucket + "-cup-" + date + "-cup-" + Uri.decode(path_image) + "-cup-" + Uri.decode(realpath) + "-cup-" + nums);
                // Do something with the values.
                i++;

            } while (cur.moveToNext());
        }
        return list_album;
    }


    public static void getImageFromDirectoryDetailGallery(String folder_name) {

        File directory = new File(folder_name);


        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                if (checkIfFileIsImage(file.getAbsolutePath().toString())) {
                    listLocalImage.add(Uri.decode(Uri.fromFile(file).toString()));
                }
            }
        }
    }

    public static String getLastHistory(Context context)
    {
        String files = "";
        HelperDB db = new HelperDB(context);
        List<MHistory> arr = db.getLastHistory();
        for(MHistory h : arr)
        {
            files = h.get_history_files();
        }
        return files;
    }

    public static Bitmap getPrivateBitmap(String filename, Context context) {
        Bitmap result = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(filename);
            result = BitmapFactory.decodeStream(fis);
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Bitmap getAssetBitmap(String filename, Context context) {
        Bitmap bmp = null;
        AssetManager assets = context.getAssets();
        InputStream inputStream;
        try {
            inputStream = assets.open("files/" + filename);
            bmp = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            bmp = null;
            e.printStackTrace();
        }
        return bmp;
    }

    public final static String getPrivatePath(String filename, Context context) {
        String result = "";
        result = "file:///" + context.getFilesDir().getAbsolutePath() + "/" + filename;
        return result;
    }

    public final static String getAssetsPath(String filename) {
        return "file:///android_asset/" + filename;
    }

    public static void sendAnalytic(Tracker tracker, String screen) {
        if (tracker != null) {
            tracker.setScreenName(screen);
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public static void DeleteInternalFile(String filename, Context mContext)
    {
        File dir = mContext.getFilesDir();
        File file = new File(dir, filename);
        if(file.exists())
        {
            file.delete();
        }
    }

    public static void ExitApplication(final BaseActivity activity)
    {
        final ViewDialogConfirm dialog = new ViewDialogConfirm(activity);
        dialog.setCancelable(true);
        dialog.show();

        dialog.setTextMessage(activity.getResources().getString(R.string.message_exit));
        dialog.getButtonOK().setRegular();
        dialog.getButtonOK().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperDB helper = new HelperDB(activity);
                helper.resetHistory();
                helper.close();
                dialog.dismiss();
                activity.finish();
            }
        });
        dialog.getButtonCancel().setRegular();
        dialog.getButtonCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void SetFirstHistory(Context context, String filename) {
        HelperDB history_conn = new HelperDB(context);
        int sum = history_conn.getHistoryCount();
        int newid = sum + 1;
        String newfile = filename;
        MHistory hist = new MHistory();
        hist.set_history_id(newid);
        hist.set_history_file(newfile);
        history_conn.addHistory(hist);
    }

    public static int getScreenSize(Context context, String param) {
        int size = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (android.os.Build.VERSION.SDK_INT > 12) {
            Point sizePoint = new Point();
            display.getSize(sizePoint);
            if(param.equals("w"))
            {
                size = sizePoint.x;
            }
            else
            {
                size = sizePoint.y;
            }

        } else {
            if(param.equals("w"))
            {
                size = display.getWidth();
            }
            else
            {
                size = display.getHeight();
            }
        }

        return size;
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HelperGlobal.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HelperGlobal.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static String getLastHistoryFile(Context context)
    {
        String files = "";
        HelperDB db = new HelperDB(context);
        List<MHistory> history = db.getLastHistory();
        for(MHistory h : history)
        {
            files = h.get_history_files();
        }
        return files;
    }

    public static final int getColor(Context context, int id)
    {
        final int version = Build.VERSION.SDK_INT;
        if(version >= 23)
        {
            return ContextCompat.getColor(context,id);
        }
        else
        {
            return context.getResources().getColor(id);
        }
    }

    public static String doSave(BaseActivity activity, Bitmap bitmap, String dir, String filename)
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
}
