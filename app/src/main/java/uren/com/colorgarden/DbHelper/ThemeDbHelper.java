package uren.com.colorgarden.DbHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import uren.com.colorgarden.model.bean.ThemeBean;

import static uren.com.colorgarden.Constants.NumericConstants.DATABASE_VERSION;
import static uren.com.colorgarden.Constants.StringConstants.DATABASE_NAME;

public class ThemeDbHelper extends SQLiteOpenHelper {

    public ThemeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_THEME_TABLE = "CREATE TABLE " + ThemeDbConstants.TABLE_NAME_THEME + " (" +
                ThemeDbConstants.COLUMN_CATEGORY + " INT NOT NULL, " +
                ThemeDbConstants.COLUMN_NAME + " STRING, " +
                ThemeDbConstants.COLUMN_IS_LOCKED + " STRING, " +
                ThemeDbConstants.COLUMN_PATH + " STRING, " +
                ThemeDbConstants.COLUMN_PRODUCT_ID + " STRING " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_THEME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ThemeDbConstants.TABLE_NAME_THEME );
        onCreate(sqLiteDatabase);
    }

    public void addAllDataToDB(List<ThemeBean.Theme> themeList) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        for(ThemeBean.Theme theme: themeList){
            ContentValues values = new ContentValues();
            values.put(ThemeDbConstants.COLUMN_CATEGORY, theme.getCategoryId());
            values.put(ThemeDbConstants.COLUMN_NAME, theme.getName());
            values.put(ThemeDbConstants.COLUMN_IS_LOCKED, theme.getIsLocked());
            values.put(ThemeDbConstants.COLUMN_PATH, theme.getPath());
            values.put(ThemeDbConstants.COLUMN_PRODUCT_ID, theme.getProductId());
            sqLiteDatabase.insert(ThemeDbConstants.TABLE_NAME_THEME, null, values);
        }
        sqLiteDatabase.close();
    }

    public ThemeBean.Theme getThemeItem(int categoryId) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ThemeDbConstants.TABLE_NAME_THEME,
                new String[]{ThemeDbConstants.COLUMN_CATEGORY,
                        ThemeDbConstants.COLUMN_NAME,
                        ThemeDbConstants.COLUMN_IS_LOCKED,
                        ThemeDbConstants.COLUMN_PATH,
                        ThemeDbConstants.COLUMN_PRODUCT_ID},
                ThemeDbConstants.COLUMN_CATEGORY + " =?",
                new String[]{String.valueOf(categoryId)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        ThemeBean.Theme theme = new ThemeBean.Theme(
                cursor.getInt(0), cursor.getString(1),
                0,cursor.getString(3), cursor.getString(2),
                cursor.getString(4));

        return theme;
    }

    public List<ThemeBean.Theme> getAllThemeItems() {
        List<ThemeBean.Theme> themeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ThemeDbConstants.TABLE_NAME_THEME + " ORDER BY " +
                ThemeDbConstants.COLUMN_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                ThemeBean.Theme theme = new ThemeBean.Theme();

                theme.setCategoryId(cursor.getInt(0));
                theme.setName(cursor.getString(1));
                theme.setPath(cursor.getString(3));
                theme.setStatus(0);
                theme.setIsLocked(cursor.getString(2));
                theme.setProductId(cursor.getString(4));

                themeList.add(theme);
            }
            while (cursor.moveToNext());
        }
        return themeList;
    }

    public int updateThemeItem(String lockedType, int categoryId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ThemeDbConstants.COLUMN_IS_LOCKED, lockedType);

        return sqLiteDatabase.update(ThemeDbConstants.TABLE_NAME_THEME , values,
                ThemeDbConstants.COLUMN_CATEGORY + " =?",
                new String[]{String.valueOf(categoryId)});
    }

    public Boolean isAnyItem() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(
                "SELECT * FROM " + ThemeDbConstants.TABLE_NAME_THEME
                , null);
        Boolean c = mCursor.moveToFirst();
        return mCursor.moveToFirst();
    }
}
