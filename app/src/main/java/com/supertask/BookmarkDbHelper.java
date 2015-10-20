package com.supertask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by pratik on 19/10/15.
 */
public class BookmarkDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SuperTask.db";

    private static final String TABLE_NAME = "bookmark";
    private static final String COLUMN_NAME_BOOKMARK_ID = "id";
    private static final String COLUMN_NAME_SHIRT = "shirt";
    private static final String COLUMN_NAME_PANT = "pant";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_TABLE_BOOKMARK =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_BOOKMARK_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_SHIRT + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_PANT + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_TABLE_BOOKMARK =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SQL_SELECT_ALL_TABLE_BOOKMARK =
            "SELECT * FROM " + TABLE_NAME;
    private static final String SQL_SELECT_GIVEN_SHIRT_PANT_TABLE_BOOKMARK =
            "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_SHIRT + "=?" + " AND " + COLUMN_NAME_PANT + "=?";


    public BookmarkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_BOOKMARK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_BOOKMARK);
        onCreate(sqLiteDatabase);
    }

    public Boolean insertBookmark(Bookmark bookmark) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_SHIRT, bookmark.getShirt());
        contentValues.put(COLUMN_NAME_PANT, bookmark.getPant());
        Long rowId = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (rowId > 0)
            return true;
        return false;
    }

    public Integer deleteBookmark(Bookmark bookmark) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(
                TABLE_NAME,
                COLUMN_NAME_SHIRT + " = ? AND " + COLUMN_NAME_PANT + " = ?",
                new String[]{
                        bookmark.getShirt(), bookmark.getPant()
                });
    }

    public ArrayList<Bookmark> getAllBookmark() {
        ArrayList<Bookmark> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SQL_SELECT_ALL_TABLE_BOOKMARK, null);
        if (cursor == null || cursor.getCount() == 0)
            return list;
        if (cursor.moveToFirst()) {
            do {
                String shirt = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SHIRT));
                String pant = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PANT));
                Bookmark bookmark = new Bookmark(shirt, pant);
                list.add(bookmark);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * @param bookmark
     * @return returns id of the first row where it finds the bookmark, 0 otherwise
     */
    public Integer getIdOfBookmark(Bookmark bookmark) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                SQL_SELECT_GIVEN_SHIRT_PANT_TABLE_BOOKMARK,
                new String[]{
                        bookmark.getShirt(), bookmark.getPant()
                }
        );
        if (cursor.getCount() == 0)
            return 0;
        return cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_BOOKMARK_ID));
    }
}
