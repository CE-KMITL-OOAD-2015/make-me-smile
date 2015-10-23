package com.toocomplicated.mademesmile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.toocomplicated.mademesmile.Constant.CONTENT;
import static com.toocomplicated.mademesmile.Constant.TABLE_NAME;
import static com.toocomplicated.mademesmile.Constant.TIME;
import static com.toocomplicated.mademesmile.Constant._ID;
/**
 * Created by SUPPORTERROR on 13/10/2558.
 */
public class ShareHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "story.db";
    private static final int DATABASE_VERSION = 1;

    public ShareHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TIME + " INTEGER, " + CONTENT + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
