package com.cupslicenew.core.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.core.model.MEffectCategory;

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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
}
