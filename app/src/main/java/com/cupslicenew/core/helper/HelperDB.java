package com.cupslicenew.core.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.core.model.MAds;
import com.cupslicenew.core.model.MEffect;
import com.cupslicenew.core.model.MEffectCategory;
import com.cupslicenew.core.model.MFrame;
import com.cupslicenew.core.model.MFrameCategory;
import com.cupslicenew.core.model.MHistory;
import com.cupslicenew.core.model.MSet;
import com.cupslicenew.core.model.MSticker;
import com.cupslicenew.core.model.MStickerCategory;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by developer on 5/24/16.
 */
public class HelperDB extends SQLiteOpenHelper {

    Context mContext;

    public HelperDB(Context context) {
        super(context, HelperColumn.DATABASE_NAME, null, HelperColumn.DATABASE_VERSION);
        mContext = context;
        Fabric.with(mContext, new Crashlytics());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createEffectCategory(db);
        createEffect(db);
        createStickerCategory(db);
        createSticker(db);
        createFrameCategory(db);
        createFrame(db);
        createAds(db);
        createSet(db);
        createHistory(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion <= 11)
        {
            onCreate(db);
        }
        else
        {
            createEffectCategory(db);
            createEffect(db);
            createFrameCategory(db);
            createFrame(db);
            createAds(db);
            createSet(db);
            createHistory(db);
            if(oldVersion > 11 && newVersion <= 17)
            {
                alterStickerCategoryDate(db);
            }
        }
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    //******* INSERT **********//
    public void addCategoryEffect(MEffectCategory cat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_ID, cat.getEffectCategoryID());
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_NAME, cat.getEffectCategoryName());
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_FILE, cat.getEffectCategoryFile());
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_DESC, cat.getEffectCategoryDesc());
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_STATUS, cat.getEffectCategoryStatus());
        db.insert(HelperColumn.TABLE_EFFECT_CATEGORY, null, values);
        db.close();
    }

    public int updateEffectCategory(MEffectCategory cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_NAME, cat.getEffectCategoryName());
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_FILE, cat.getEffectCategoryFile());
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_DESC, cat.getEffectCategoryDesc());
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_STATUS, cat.getEffectCategoryStatus());
        return db.update(HelperColumn.TABLE_EFFECT_CATEGORY, values, HelperColumn.COLUMN_EFFECT_CATEGORY_ID + " = ?",
                new String[] { String.valueOf(cat.getEffectCategoryID()) });
    }

    public MEffectCategory getCategoryEffectByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(HelperColumn.TABLE_EFFECT_CATEGORY, new String[]{HelperColumn.COLUMN_EFFECT_CATEGORY_ID,
                        HelperColumn.COLUMN_EFFECT_CATEGORY_NAME, HelperColumn.COLUMN_EFFECT_CATEGORY_FILE, HelperColumn.COLUMN_EFFECT_CATEGORY_DESC}, HelperColumn.COLUMN_EFFECT_CATEGORY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MEffectCategory efcat = new MEffectCategory();
        efcat.setEffectCategoryID(Integer.parseInt(cursor.getString(0)));
        efcat.setEffectCategoryName(cursor.getString(1));
        efcat.setEffectCategoryFile(cursor.getString(2));
        efcat.setEffectCategoryDesc(cursor.getString(3));
        efcat.setEffectCategoryStatus(cursor.getString(4));

        return efcat;
    }

    public List<MEffectCategory> getAllEffectCategory() {
        List<MEffectCategory> catList = new ArrayList<MEffectCategory>();
        String selectQuery = "SELECT  * FROM " + HelperColumn.TABLE_EFFECT_CATEGORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MEffectCategory cat = new MEffectCategory();
                cat.setEffectCategoryID(Integer.parseInt(cursor.getString(0)));
                cat.setEffectCategoryName(cursor.getString(1));
                cat.setEffectCategoryFile(cursor.getString(2));
                cat.setEffectCategoryDesc(cursor.getString(3));
                cat.setEffectCategoryStatus(cursor.getString(4));
                catList.add(cat);
            } while (cursor.moveToNext());
        }
        return catList;
    }

    public void deleteEffectCategory(MEffectCategory cat) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HelperColumn.TABLE_EFFECT_CATEGORY, HelperColumn.COLUMN_EFFECT_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(cat.getEffectCategoryID())});
        db.close();
    }

    public void resetEffectCategory()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperColumn.TABLE_EFFECT_CATEGORY + " WHERE " + HelperColumn.COLUMN_EFFECT_CATEGORY_STATUS + " = 'f'");
        db.close();
    }

    public int getEffectCategoryCount() {
        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_EFFECT_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do{
                count++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return count;
    }

    public boolean checkEffectCategoryByID(int id)
    {

        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_EFFECT_CATEGORY + " WHERE " + HelperColumn.COLUMN_EFFECT_CATEGORY_ID + " = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    //EFFECT
    public void addEffect(MEffect effect) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_EFFECT_ID, effect.getEffectID());
        values.put(HelperColumn.COLUMN_EFFECT_NAME, effect.getEffectName());
        values.put(HelperColumn.COLUMN_EFFECT_FILE, effect.getEffectFile());
        values.put(HelperColumn.COLUMN_EFFECT_DESC, effect.getEffectDesc());
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_ID, effect.getEffectCategoryID());
        values.put(HelperColumn.COLUMN_EFFECT_STATUS, effect.getEffectStatus());
        db.insert(HelperColumn.TABLE_EFFECT, null, values);
        db.close();
    }

    public int updateEffect(MEffect effect) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_EFFECT_ID, effect.getEffectID());
        values.put(HelperColumn.COLUMN_EFFECT_NAME, effect.getEffectName());
        values.put(HelperColumn.COLUMN_EFFECT_FILE, effect.getEffectFile());
        values.put(HelperColumn.COLUMN_EFFECT_DESC, effect.getEffectDesc());
        values.put(HelperColumn.COLUMN_EFFECT_CATEGORY_ID, effect.getEffectCategoryID());
        values.put(HelperColumn.COLUMN_EFFECT_STATUS, effect.getEffectStatus());
        return db.update(HelperColumn.TABLE_EFFECT, values, HelperColumn.COLUMN_EFFECT_ID + " = ?",
                new String[] { String.valueOf(effect.getEffectID()) });
    }

    public List<MEffect> getEffectByCategory(int id) {
        List<MEffect> effectList = new ArrayList<MEffect>();
        String selectQuery = "SELECT  * FROM " + HelperColumn.TABLE_EFFECT + " WHERE " + HelperColumn.COLUMN_EFFECT_CATEGORY_ID+" = "+id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MEffect effect = new MEffect();
                effect.setEffectID(Integer.parseInt(cursor.getString(0)));
                effect.setEffectName(cursor.getString(1));
                effect.setEffectFile(cursor.getString(2));
                effect.setEffectDesc(cursor.getString(3));
                effect.setEffectCategoryID(Integer.parseInt(cursor.getString(4)));
                effect.setEffectStatus(cursor.getString(5));
                effectList.add(effect);
            } while (cursor.moveToNext());
        }
        return effectList;
    }

    public void deleteEffect(MEffect effect) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HelperColumn.TABLE_EFFECT, HelperColumn.COLUMN_EFFECT_ID + " = ?",
                new String[]{String.valueOf(effect.getEffectID())});
        db.close();
    }

    public void resetEffect()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperColumn.TABLE_EFFECT + " WHERE " + HelperColumn.COLUMN_EFFECT_STATUS + " = 'f'");
        db.close();
    }

    public MEffect getEffectByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(HelperColumn.TABLE_EFFECT, new String[] { HelperColumn.COLUMN_EFFECT_ID,
                        HelperColumn.COLUMN_EFFECT_NAME, HelperColumn.COLUMN_EFFECT_FILE, HelperColumn.COLUMN_EFFECT_DESC, HelperColumn.COLUMN_EFFECT_CATEGORY_ID, HelperColumn.COLUMN_EFFECT_STATUS}, HelperColumn.COLUMN_EFFECT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MEffect effect = new MEffect();
        effect.setEffectID(Integer.parseInt(cursor.getString(0)));
        effect.setEffectName(cursor.getString(1));
        effect.setEffectFile(cursor.getString(2));
        effect.setEffectDesc(cursor.getString(3));
        effect.setEffectCategoryID(Integer.parseInt(cursor.getString(4)));
        effect.setEffectStatus(cursor.getString(5));
        return effect;
    }

    public int getEffectCount() {
        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_EFFECT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do{
                count++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return count;
    }

    public boolean checkEffectByID(int id)
    {

        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_EFFECT + " WHERE " + HelperColumn.COLUMN_EFFECT_ID + " = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //STICKER CATEGORY
    public void addStickerCategory(MStickerCategory cat)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_ID, cat.getStickerCategoryID());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_NAME, cat.getStickerCategoryName());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_DESC, cat.getStickerCategoryDesc());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_URI, cat.getStickerCategoryUri());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_STATUS, cat.getStickerCategoryStatus());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_DATE, cat.getStickerCategoryDate());
        db.insert(HelperColumn.TABLE_CATEGORY_BADGE, null, values);
        db.close();
    }

    public int updateStickerCategory(MStickerCategory cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_NAME, cat.getStickerCategoryName());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_DESC, cat.getStickerCategoryDesc());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_URI, cat.getStickerCategoryUri());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_STATUS, cat.getStickerCategoryDate());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_DATE, cat.getStickerCategoryDate());
        return db.update(HelperColumn.TABLE_CATEGORY_BADGE, values, HelperColumn.COLUMN_CATEGORY_BADGE_ID + " = ?",
                new String[] { String.valueOf(cat.getStickerCategoryID()) });
    }

    public MStickerCategory getStickerCategoryByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(HelperColumn.TABLE_CATEGORY_BADGE, new String[]{HelperColumn.COLUMN_CATEGORY_BADGE_ID,
                        HelperColumn.COLUMN_CATEGORY_BADGE_NAME, HelperColumn.COLUMN_CATEGORY_BADGE_DESC, HelperColumn.COLUMN_CATEGORY_BADGE_URI, HelperColumn.COLUMN_CATEGORY_BADGE_STATUS, HelperColumn.COLUMN_CATEGORY_BADGE_DATE}, HelperColumn.COLUMN_CATEGORY_BADGE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        MStickerCategory cat = new MStickerCategory();
        cat.setStickerCategoryID(Integer.parseInt(cursor.getString(0)));
        cat.setStickerCategoryName(cursor.getString(1));
        cat.setStickerCategoryDesc(cursor.getString(2));
        cat.setStickerCategoryUri(cursor.getString(3));
        cat.setStickerCategoryStatus(cursor.getString(4));
        cat.setStickerCategoryDate(cursor.getString(5));
        return cat;
    }

    public List<MStickerCategory> getAllStickerCategory() {
        List<MStickerCategory> catBadgeList = new ArrayList<MStickerCategory>();
        String selectQuery = "SELECT  * FROM " + HelperColumn.TABLE_CATEGORY_BADGE + " ORDER BY "+ HelperColumn.COLUMN_CATEGORY_BADGE_DATE +" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MStickerCategory cat = new MStickerCategory();
                cat.setStickerCategoryID(Integer.parseInt(cursor.getString(0)));
                cat.setStickerCategoryName(cursor.getString(1));
                cat.setStickerCategoryDesc(cursor.getString(2));
                cat.setStickerCategoryUri(cursor.getString(3));
                cat.setStickerCategoryStatus(cursor.getString(4));
                cat.setStickerCategoryDate(cursor.getString(5));
                catBadgeList.add(cat);
            } while (cursor.moveToNext());
        }
        return catBadgeList;
    }

    public void deleteStickerCategory(MStickerCategory cat) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HelperColumn.TABLE_CATEGORY_BADGE, HelperColumn.COLUMN_CATEGORY_BADGE_ID + " = ?",
                new String[]{String.valueOf(cat.getStickerCategoryID())});
        db.close();
    }

    public void resetStickerCategory()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperColumn.TABLE_CATEGORY_BADGE + " WHERE " + HelperColumn.COLUMN_CATEGORY_BADGE_STATUS + " = 'f'");
        db.close();
    }

    public int getStickerCategoryCount() {
        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_CATEGORY_BADGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do{
                count++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return count;
    }

    public boolean checkStickerCategoryByID(int id)
    {

        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_CATEGORY_BADGE + " WHERE " + HelperColumn.COLUMN_CATEGORY_BADGE_ID + " = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void updateStickerCategoryDateNull() {
        String selectQuery = "UPDATE " + HelperColumn.TABLE_CATEGORY_BADGE+" SET "+HelperColumn.COLUMN_CATEGORY_BADGE_DATE+"='2015-12-01 01:00:00' WHERE "+HelperColumn.COLUMN_CATEGORY_BADGE_DATE+" IS null";
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery(selectQuery, null);
    }

    public void updateStickerCategoryFile(int id, String file) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_URI, file);
        db.update(HelperColumn.TABLE_CATEGORY_BADGE, values, HelperColumn.COLUMN_CATEGORY_BADGE_ID + " = ?",new String[] { String.valueOf(id) });
        db.close();
    }


    //STICKER
    public void addSticker(MSticker sticker)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_BADGE_ID, sticker.getStickerID());
        values.put(HelperColumn.COLUMN_BADGE_NAME, sticker.getStickerName());
        values.put(HelperColumn.COLUMN_BADGE_URI, sticker.getStickerUri());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_ID, sticker.getStickerCategoryID());
        values.put(HelperColumn.COLUMN_BADGE_STATUS, sticker.getStickerStatus());
        values.put(HelperColumn.COLUMN_BADGE_COLOR, sticker.getStickerColor());
        db.insert(HelperColumn.TABLE_BADGE, null, values);
        db.close();
    }

    public int updateSticker(MSticker sticker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_BADGE_NAME, sticker.getStickerName());
        values.put(HelperColumn.COLUMN_BADGE_URI, sticker.getStickerUri());
        values.put(HelperColumn.COLUMN_CATEGORY_BADGE_ID, sticker.getStickerCategoryID());
        values.put(HelperColumn.COLUMN_BADGE_STATUS, sticker.getStickerStatus());
        values.put(HelperColumn.COLUMN_BADGE_COLOR, sticker.getStickerColor());
        return db.update(HelperColumn.TABLE_BADGE, values, HelperColumn.COLUMN_BADGE_ID + " = ?",
                new String[] { String.valueOf(sticker.getStickerID()) });
    }

    public List<MSticker> getStickerByCategory(int id) {
        List<MSticker> stickerList = new ArrayList<MSticker>();
        String selectQuery = "SELECT  * FROM " + HelperColumn.TABLE_BADGE + " WHERE "+ HelperColumn.COLUMN_CATEGORY_BADGE_ID+" = "+id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MSticker sticker = new MSticker();
                sticker.setStickerID(Integer.parseInt(cursor.getString(0)));
                sticker.setStickerName(cursor.getString(1));
                sticker.setStickerUri(cursor.getString(2));
                sticker.setStickerCategoryID(Integer.parseInt(cursor.getString(3)));
                sticker.setStickerStatus(cursor.getString(4));
                sticker.setStickerColor(cursor.getString(5));
                stickerList.add(sticker);
            } while (cursor.moveToNext());
        }
        return stickerList;
    }

    public MSticker getStickerByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(HelperColumn.TABLE_BADGE, new String[]{HelperColumn.COLUMN_BADGE_ID,
                        HelperColumn.COLUMN_BADGE_NAME, HelperColumn.COLUMN_BADGE_URI, HelperColumn.COLUMN_CATEGORY_BADGE_ID, HelperColumn.COLUMN_BADGE_COLOR, HelperColumn.COLUMN_BADGE_STATUS}, HelperColumn.COLUMN_BADGE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MSticker sticker = new MSticker();
        sticker.setStickerID(Integer.parseInt(cursor.getString(0)));
        sticker.setStickerName(cursor.getString(1));
        sticker.setStickerUri(cursor.getString(2));
        sticker.setStickerCategoryID(Integer.parseInt(cursor.getString(3)));
        sticker.setStickerColor(cursor.getString(4));
        sticker.setStickerStatus(cursor.getString(5));

        return sticker;
    }

    public void deleteSticker(MSticker sticker) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HelperColumn.TABLE_BADGE, HelperColumn.COLUMN_BADGE_ID + " = ?",
                new String[]{String.valueOf(sticker.getStickerID())});
        db.close();
    }

    public void resetSticker()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperColumn.TABLE_BADGE + " WHERE " + HelperColumn.COLUMN_BADGE_STATUS + " = 'f'");
        db.close();
    }

    public int getStickerCount() {
        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_BADGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do{
                count++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return count;
    }

    public boolean checkStickerByID(int id)
    {

        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_BADGE + " WHERE " + HelperColumn.COLUMN_BADGE_ID + " = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    //FRAME CATEGORY
    public void addFrameCategory(MFrameCategory cat)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_ID, cat.getFrameCategoryID());
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_NAME, cat.getFrameCategoryName());
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_DESC, cat.getFrameCategoryDesc());
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_URI, cat.getFrameCategoryUri());
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_STATUS, cat.getFrameCategoryStatus());
        db.insert(HelperColumn.TABLE_FRAME_CATEGORY, null, values);
        db.close();
    }

    public int updateFrameCategory(MFrameCategory catFrame) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_NAME, catFrame.getFrameCategoryName());
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_DESC, catFrame.getFrameCategoryDesc());
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_URI, catFrame.getFrameCategoryUri());
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_STATUS, catFrame.getFrameCategoryStatus());

        return db.update(HelperColumn.TABLE_FRAME_CATEGORY, values, HelperColumn.COLUMN_FRAME_CATEGORY_ID + " = ?",
                new String[] { String.valueOf(catFrame.getFrameCategoryID()) });
    }

    public List<MFrameCategory> getAllFrameCategory() {
        List<MFrameCategory> catFrameList = new ArrayList<MFrameCategory>();
        String selectQuery = "SELECT  * FROM " + HelperColumn.TABLE_FRAME_CATEGORY + " ORDER BY " + HelperColumn.COLUMN_FRAME_CATEGORY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MFrameCategory catFrame = new MFrameCategory();
                catFrame.setFrameCategoryID(Integer.parseInt(cursor.getString(0)));
                catFrame.setFrameCategoryName(cursor.getString(1));
                catFrame.setFrameCategoryDesc(cursor.getString(2));
                catFrame.setFrameCategoryUri(cursor.getString(3));
                catFrame.setFrameCategoryStatus(cursor.getString(4));
                catFrameList.add(catFrame);
            } while (cursor.moveToNext());
        }
        return catFrameList;
    }

    public MFrameCategory getFrameCategoryByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(HelperColumn.TABLE_FRAME_CATEGORY, new String[] { HelperColumn.COLUMN_FRAME_CATEGORY_ID,
                        HelperColumn.COLUMN_FRAME_CATEGORY_NAME, HelperColumn.COLUMN_FRAME_CATEGORY_DESC, HelperColumn.COLUMN_FRAME_CATEGORY_URI, HelperColumn.COLUMN_FRAME_CATEGORY_STATUS}, HelperColumn.COLUMN_FRAME_CATEGORY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        MFrameCategory cat = new MFrameCategory();
        cat.setFrameCategoryID(Integer.parseInt(cursor.getString(0)));
        cat.setFrameCategoryName(cursor.getString(1));
        cat.setFrameCategoryDesc(cursor.getString(2));
        cat.setFrameCategoryUri(cursor.getString(3));
        cat.setFrameCategoryStatus(cursor.getString(4));
        return cat;
    }

    public void deleteFrameCategory(MFrameCategory catFrame) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HelperColumn.TABLE_FRAME_CATEGORY, HelperColumn.COLUMN_FRAME_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(catFrame.getFrameCategoryID())});
        db.close();
    }

    public void resetFrameCategory()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperColumn.TABLE_FRAME_CATEGORY + " WHERE " + HelperColumn.COLUMN_FRAME_CATEGORY_STATUS + " = 'f'");
        db.close();
    }

    public int getFrameCategoryCount() {
        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_FRAME_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do{
                count++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return count;
    }

    public boolean checkFrameCategoryByID(int id)
    {

        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_FRAME_CATEGORY + " WHERE " + HelperColumn.COLUMN_FRAME_CATEGORY_ID + " = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    //FRAME
    public void addFrame(MFrame frame)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_FRAME_ID, frame.getFrameID());
        values.put(HelperColumn.COLUMN_FRAME_NAME, frame.getFrameName());
        values.put(HelperColumn.COLUMN_FRAME_DESC, frame.getFrameDesc());
        values.put(HelperColumn.COLUMN_FRAME_URI, frame.getFrameUri());
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_ID, frame.getFrameCategoryID());
        values.put(HelperColumn.COLUMN_FRAME_STATUS, frame.getFrameStatus());
        values.put(HelperColumn.COLUMN_FRAME_COLOR, frame.getFrameColor());
        db.insert(HelperColumn.TABLE_FRAME, null, values);
        db.close();
    }

    public int updateFrame(MFrame frame) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_FRAME_NAME, frame.getFrameName());
        values.put(HelperColumn.COLUMN_FRAME_DESC, frame.getFrameDesc());
        values.put(HelperColumn.COLUMN_FRAME_URI, frame.getFrameUri());
        values.put(HelperColumn.COLUMN_FRAME_CATEGORY_ID, frame.getFrameCategoryID());
        values.put(HelperColumn.COLUMN_FRAME_STATUS, frame.getFrameStatus());
        values.put(HelperColumn.COLUMN_FRAME_COLOR, frame.getFrameColor());
        return db.update(HelperColumn.TABLE_FRAME, values, HelperColumn.COLUMN_FRAME_ID + " = ?",
                new String[] { String.valueOf(frame.getFrameID()) });
    }

    public List<MFrame> getFrameByCategory(int id) {
        List<MFrame> frameList = new ArrayList<MFrame>();
        String selectQuery = "SELECT  * FROM " + HelperColumn.TABLE_FRAME+" WHERE "+ HelperColumn.COLUMN_FRAME_CATEGORY_ID+" = "+id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MFrame frame = new MFrame();
                frame.setFrameID(Integer.parseInt(cursor.getString(0)));
                frame.setFrameName(cursor.getString(1));
                frame.setFrameDesc(cursor.getString(2));
                frame.setFrameUri(cursor.getString(3));
                frame.setFrameCategoryID(Integer.parseInt(cursor.getString(4)));
                frame.setFrameStatus(cursor.getString(5));
                frame.setFrameColor(cursor.getString(6));
                frameList.add(frame);
            } while (cursor.moveToNext());
        }
        return frameList;
    }

    public MFrame getFrameByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(HelperColumn.TABLE_FRAME, new String[] { HelperColumn.COLUMN_FRAME_ID,
                        HelperColumn.COLUMN_FRAME_NAME, HelperColumn.COLUMN_FRAME_DESC, HelperColumn.COLUMN_FRAME_URI, HelperColumn.COLUMN_FRAME_CATEGORY_ID, HelperColumn.COLUMN_FRAME_STATUS, HelperColumn.COLUMN_FRAME_COLOR}, HelperColumn.COLUMN_FRAME_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null,null);
        if (cursor != null)
            cursor.moveToFirst();

        MFrame frame = new MFrame();
        frame.setFrameID(Integer.parseInt(cursor.getString(0)));
        frame.setFrameName(cursor.getString(1));
        frame.setFrameDesc(cursor.getString(2));
        frame.setFrameUri(cursor.getString(3));
        frame.setFrameCategoryID(Integer.parseInt(cursor.getString(4)));
        frame.setFrameStatus(cursor.getString(5));
        frame.setFrameColor(cursor.getString(6));
        return frame;
    }

    public void deleteFrame(MFrame frame) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HelperColumn.TABLE_FRAME, HelperColumn.COLUMN_FRAME_ID + " = ?",
                new String[]{String.valueOf(frame.getFrameID())});
        db.close();
    }

    public void resetFrame()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperColumn.TABLE_FRAME + " WHERE " + HelperColumn.COLUMN_FRAME_STATUS + " = 'f'");
        db.close();
    }

    public int getFrameCount() {
        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_FRAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do{
                count++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return count;
    }

    public boolean checkFrameByID(int id)
    {

        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_FRAME_CATEGORY + " WHERE " + HelperColumn.COLUMN_FRAME_ID + " = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    //ADS
    public void addAds(MAds ads)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_ADS_ID, ads.getAdsID());
        values.put(HelperColumn.COLUMN_ADS_CATEGORY, ads.getAdsCategory());
        values.put(HelperColumn.COLUMN_ADS_STATE, ads.getAdsState());
        values.put(HelperColumn.COLUMN_ADS_NEXT_TIME, ads.getAdsNextTime());
        values.put(HelperColumn.COLUMN_ADS_OVERLAY, ads.getAdsOverlay());
        db.insert(HelperColumn.TABLE_ADS, null, values);
        db.close();
    }

    public int updateAds(MAds ads) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_ADS_ID, ads.getAdsID());
        values.put(HelperColumn.COLUMN_ADS_CATEGORY, ads.getAdsCategory());
        values.put(HelperColumn.COLUMN_ADS_STATE, ads.getAdsState());
        values.put(HelperColumn.COLUMN_ADS_NEXT_TIME, ads.getAdsNextTime());
        values.put(HelperColumn.COLUMN_ADS_OVERLAY, ads.getAdsOverlay());

        return db.update(HelperColumn.TABLE_ADS, values, HelperColumn.COLUMN_ADS_ID + " = ?",
                new String[] { String.valueOf(ads.getAdsID()) });
    }

    public void update_ads_status(String status,String uid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_ADS_STATE, status);
        db.update(HelperColumn.TABLE_ADS, values, HelperColumn.COLUMN_ADS_ID + " = ?", new String[]{String.valueOf(uid)});
        db.close();
    }

    public void update_ads_time(String time,String uid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_ADS_NEXT_TIME, time);
        db.update(HelperColumn.TABLE_ADS, values, HelperColumn.COLUMN_ADS_ID + " = ?", new String[]{String.valueOf(uid)});
        db.close();
    }

    public void update_ads_overlay(String status,String uid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_ADS_OVERLAY, status);
        db.update(HelperColumn.TABLE_ADS, values, HelperColumn.COLUMN_ADS_ID + " = ?", new String[]{String.valueOf(uid)});
        db.close();
    }

    public List<MAds> getAllAds() {
        List<MAds> adsList = new ArrayList<MAds>();
        String selectQuery = "SELECT  * FROM " + HelperColumn.TABLE_ADS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MAds ads = new MAds();
                ads.setAdsID(cursor.getString(0));
                ads.setAdsCategory(cursor.getString(1));
                ads.setAdsState(cursor.getString(2));
                ads.setAdsNextTime(cursor.getString(3));
                ads.setAdsOverlay(cursor.getString(4));
                adsList.add(ads);
            } while (cursor.moveToNext());
        }
        return adsList;
    }

    public MAds getAdsById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(HelperColumn.TABLE_ADS, new String[]{HelperColumn.COLUMN_ADS_ID,
                        HelperColumn.COLUMN_ADS_CATEGORY, HelperColumn.COLUMN_ADS_STATE, HelperColumn.COLUMN_ADS_NEXT_TIME, HelperColumn.COLUMN_ADS_OVERLAY}, HelperColumn.COLUMN_ADS_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        MAds ads;
        if (cursor.moveToFirst())
        {
            ads = new MAds();
            ads.setAdsID(cursor.getString(0));
            ads.setAdsCategory(cursor.getString(1));
            ads.setAdsState(cursor.getString(2));
            ads.setAdsNextTime(cursor.getString(3));
            ads.setAdsOverlay(cursor.getString(4));
        }
        else
        {
            ads = new MAds();
        }
        return ads;
    }

    public void deleteAds(MAds ads) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HelperColumn.TABLE_ADS, HelperColumn.COLUMN_ADS_ID + " = ?",
                new String[] { String.valueOf(ads.getAdsID()) });
        db.close();
    }

    public void resetAds()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperColumn.TABLE_ADS);
        db.close();
    }

    public int getAdsCount() {
        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_ADS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do{
                count++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return count;
    }


    //HISTORY
    public void addHistory(MHistory hist)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_HISTORY_ID, hist.get_history_id());
        values.put(HelperColumn.COLUMN_HISTORY_FILES, hist.get_history_files());
        db.insert(HelperColumn.TABLE_HISTORY, null, values);
        db.close();
    }

    public List<MHistory> getAllHistory() {
        List<MHistory> histList = new ArrayList<MHistory>();
        String selectQuery = "SELECT  * FROM " + HelperColumn.TABLE_HISTORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MHistory history = new MHistory();
                history.set_history_id(Integer.parseInt(cursor.getString(0)));
                history.set_history_file(cursor.getString(1));
                histList.add(history);
            } while (cursor.moveToNext());
        }
        return histList;
    }

    public List<MHistory> getLastHistory() {
        List<MHistory> histList = new ArrayList<MHistory>();
        String selectQuery = "SELECT  * FROM " + HelperColumn.TABLE_HISTORY + " ORDER BY "+ HelperColumn.COLUMN_HISTORY_ID+" DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MHistory history = new MHistory();
                history.set_history_id(Integer.parseInt(cursor.getString(0)));
                history.set_history_file(cursor.getString(1));
                histList.add(history);
            } while (cursor.moveToNext());
        }
        return histList;
    }

    public MHistory getHistoryByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(HelperColumn.TABLE_HISTORY, new String[] { HelperColumn.COLUMN_HISTORY_ID,
                        HelperColumn.COLUMN_HISTORY_FILES,}, HelperColumn.COLUMN_HISTORY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null,null);
        if (cursor != null)
            cursor.moveToFirst();

        MHistory history = new MHistory();
        history.set_history_id(Integer.parseInt(cursor.getString(0)));
        history.set_history_file(cursor.getString(1));
        return history;
    }

    public void deleteHistory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperColumn.TABLE_HISTORY + " WHERE " + HelperColumn.COLUMN_HISTORY_ID + " = " + id + "");
        db.close();
    }

    public void resetHistory()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperColumn.TABLE_HISTORY);
        db.close();
    }

    public int getHistoryCount() {

        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do{
                count++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return count;
    }



    //SET
    public void addSet(MSet set)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_SET_ID, set.getID());
        values.put(HelperColumn.COLUMN_SET_NAME, set.getName());
        values.put(HelperColumn.COLUMN_SET_VALUE, set.getValue());
        db.insert(HelperColumn.TABLE_SET, null, values);
        db.close();
    }

    public int updateSet(MSet set) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HelperColumn.COLUMN_SET_ID, set.getID());
        values.put(HelperColumn.COLUMN_SET_NAME, set.getName());
        values.put(HelperColumn.COLUMN_SET_VALUE, set.getValue());
        return db.update(HelperColumn.TABLE_SET, values, HelperColumn.COLUMN_SET_ID + " = ?",
                new String[] { String.valueOf(set.getID()) });
    }

    public MSet getSetByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(HelperColumn.TABLE_SET, new String[]{HelperColumn.COLUMN_SET_ID,
                        HelperColumn.COLUMN_SET_NAME, HelperColumn.COLUMN_SET_VALUE}, HelperColumn.COLUMN_SET_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        MSet set = new MSet();
        set.setID(Integer.parseInt(cursor.getString(0)));
        set.setName(cursor.getString(1));
        set.setValue(cursor.getString(2));
        return set;
    }

    public void deleteSet(MSet set) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HelperColumn.TABLE_SET, HelperColumn.COLUMN_SET_ID + " = ?",
                new String[]{String.valueOf(set.getID())});
        db.close();
    }

    public void resetSet()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperColumn.TABLE_SET);
        db.close();
    }

    public int getSetCount() {
        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_SET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do{
                count++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return count;
    }

    public boolean check_set_byId(int id)
    {

        String countQuery = "SELECT  * FROM " + HelperColumn.TABLE_SET + " WHERE " + HelperColumn.COLUMN_SET_ID + " = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public List<String> getTempora() {
        List<String> adsList = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM tempora_" + HelperColumn.TABLE_CATEGORY_BADGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                adsList.add(cursor.getString(0)+"--"+cursor.getString(1)+"--"+cursor.getString(2)+"--"+cursor.getString(3)+"--"+cursor.getString(4));
            } while (cursor.moveToNext());
        }
        return adsList;
    }




    //***** CREATE TABLE *****//
    private void createEffectCategory(SQLiteDatabase db)
    {
        String CREATE_TABLE_EFFECT_CATEGORY = "CREATE TABLE " + HelperColumn.TABLE_EFFECT_CATEGORY + " (" + HelperColumn.COLUMN_EFFECT_CATEGORY_ID +
                " INTEGER PRIMARY KEY , " + HelperColumn.COLUMN_EFFECT_CATEGORY_NAME + " TEXT," +
                HelperColumn.COLUMN_EFFECT_CATEGORY_FILE + " TEXT," + HelperColumn.COLUMN_EFFECT_CATEGORY_DESC + " TEXT, "+
                HelperColumn.COLUMN_EFFECT_CATEGORY_STATUS+" TEXT" + ")";
        db.execSQL("DROP TABLE IF EXISTS " + HelperColumn.TABLE_EFFECT_CATEGORY);
        db.execSQL(CREATE_TABLE_EFFECT_CATEGORY);
    }

    private void createEffect(SQLiteDatabase db)
    {
        String CREATE_TABLE_EFFECT = "CREATE TABLE " + HelperColumn.TABLE_EFFECT + " (" + HelperColumn.COLUMN_EFFECT_ID +
                " INTEGER PRIMARY KEY , " + HelperColumn.COLUMN_EFFECT_NAME + " TEXT," +
                HelperColumn.COLUMN_EFFECT_FILE + " TEXT," + HelperColumn.COLUMN_EFFECT_DESC + " TEXT," +
                HelperColumn.COLUMN_EFFECT_CATEGORY_ID + " INTEGER , "+ HelperColumn.COLUMN_EFFECT_STATUS+" TEXT," + HelperColumn.COLUMN_EFFECT_FUNC+" TEXT)";
        db.execSQL("DROP TABLE IF EXISTS " + HelperColumn.TABLE_EFFECT);
        db.execSQL(CREATE_TABLE_EFFECT);
    }

    private void createStickerCategory(SQLiteDatabase db)
    {
        String CREATE_TABLE_BADGE_CATEGORY = "CREATE TABLE " + HelperColumn.TABLE_CATEGORY_BADGE + " (" + HelperColumn.COLUMN_CATEGORY_BADGE_ID +
                " INTEGER PRIMARY KEY , " + HelperColumn.COLUMN_CATEGORY_BADGE_NAME + " TEXT," +
                HelperColumn.COLUMN_CATEGORY_BADGE_DESC + " TEXT," + HelperColumn.COLUMN_CATEGORY_BADGE_URI + " TEXT, "+
                HelperColumn.COLUMN_CATEGORY_BADGE_STATUS+" TEXT" + ", "+HelperColumn.COLUMN_CATEGORY_BADGE_DATE+" DATETIME )";
        db.execSQL("DROP TABLE IF EXISTS " + HelperColumn.TABLE_CATEGORY_BADGE);
        db.execSQL(CREATE_TABLE_BADGE_CATEGORY);
    }

    private void createSticker(SQLiteDatabase db)
    {
        String CREATE_TABLE_BADGE = "CREATE TABLE " + HelperColumn.TABLE_BADGE + " (" + HelperColumn.COLUMN_BADGE_ID +
                " INTEGER PRIMARY KEY , " + HelperColumn.COLUMN_BADGE_NAME + " TEXT," +
                HelperColumn.COLUMN_BADGE_URI+ " TEXT," + HelperColumn.COLUMN_CATEGORY_BADGE_ID + " INTEGER, "+
                HelperColumn.COLUMN_BADGE_STATUS+" TEXT,"+ HelperColumn.COLUMN_BADGE_COLOR+" TEXT)";
        db.execSQL("DROP TABLE IF EXISTS " + HelperColumn.TABLE_BADGE);
        db.execSQL(CREATE_TABLE_BADGE);
    }

    private void createFrameCategory(SQLiteDatabase db)
    {
        String CREATE_TABLE_FRAME_CATEGORY = "CREATE TABLE " + HelperColumn.TABLE_FRAME_CATEGORY + " (" + HelperColumn.COLUMN_FRAME_CATEGORY_ID +
                " INTEGER PRIMARY KEY , " + HelperColumn.COLUMN_FRAME_CATEGORY_NAME + " TEXT," +
                HelperColumn.COLUMN_FRAME_CATEGORY_DESC + " TEXT," + HelperColumn.COLUMN_FRAME_CATEGORY_URI + " TEXT, "+
                HelperColumn.COLUMN_FRAME_CATEGORY_STATUS+" TEXT" + ")";
        db.execSQL("DROP TABLE IF EXISTS " + HelperColumn.TABLE_FRAME_CATEGORY);
        db.execSQL(CREATE_TABLE_FRAME_CATEGORY);
    }

    private void createFrame(SQLiteDatabase db)
    {
        String CREATE_TABLE_FRAME = "CREATE TABLE " + HelperColumn.TABLE_FRAME + " (" + HelperColumn.COLUMN_FRAME_ID +
                " INTEGER PRIMARY KEY , " + HelperColumn.COLUMN_FRAME_NAME + " TEXT," +
                HelperColumn.COLUMN_FRAME_DESC + " TEXT," + HelperColumn.COLUMN_FRAME_URI + " TEXT," + HelperColumn.COLUMN_FRAME_CATEGORY_ID + " INTEGER , "+
                HelperColumn.COLUMN_FRAME_STATUS+" TEXT " +","+ HelperColumn.COLUMN_FRAME_COLOR+" TEXT)";
        db.execSQL("DROP TABLE IF EXISTS " + HelperColumn.TABLE_FRAME);
        db.execSQL(CREATE_TABLE_FRAME);
    }

    private void createAds(SQLiteDatabase db)
    {
        String CREATE_TABLE_ADS = "CREATE TABLE " + HelperColumn.TABLE_ADS + " (" + HelperColumn.COLUMN_ADS_ID +
                " TEXT PRIMARY KEY , " + HelperColumn.COLUMN_ADS_CATEGORY + " TEXT," +
                HelperColumn.COLUMN_ADS_STATE + " TEXT, "+ HelperColumn.COLUMN_ADS_NEXT_TIME+" TEXT, "+ HelperColumn.COLUMN_ADS_OVERLAY+" TEXT )";
        db.execSQL("DROP TABLE IF EXISTS " + HelperColumn.TABLE_ADS);
        db.execSQL(CREATE_TABLE_ADS);
    }

    private void createSet(SQLiteDatabase db)
    {
        String cREATE_TABLE_SET = "CREATE TABLE " + HelperColumn.TABLE_SET + " (" + HelperColumn.COLUMN_SET_ID +
                " INTEGER PRIMARY KEY , " + HelperColumn.COLUMN_SET_NAME + " TEXT," +
                HelperColumn.COLUMN_SET_VALUE + " TEXT)";
        db.execSQL("DROP TABLE IF EXISTS " + HelperColumn.TABLE_SET);
        db.execSQL(cREATE_TABLE_SET);
    }

    private void createHistory(SQLiteDatabase db)
    {
        String cREATE_TABLE_HISTORY = "CREATE TABLE " + HelperColumn.TABLE_HISTORY + " (" + HelperColumn.COLUMN_HISTORY_ID +
                " INTEGER PRIMARY KEY , " + HelperColumn.COLUMN_HISTORY_FILES + " TEXT)";
        db.execSQL("DROP TABLE IF EXISTS " + HelperColumn.TABLE_HISTORY);
        db.execSQL(cREATE_TABLE_HISTORY);
    }




    //FOR ALTER TABLE
    private void alterStickerCategoryDate(SQLiteDatabase db)
    {
        db.execSQL("ALTER TABLE "+HelperColumn.TABLE_CATEGORY_BADGE+" ADD COLUMN "+HelperColumn.COLUMN_CATEGORY_BADGE_DATE+" DATETIME DEFAULT null");
    }
}
