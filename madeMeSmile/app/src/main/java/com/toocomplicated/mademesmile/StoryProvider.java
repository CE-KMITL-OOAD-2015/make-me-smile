package com.toocomplicated.mademesmile;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static com.toocomplicated.mademesmile.Constant.AUTHORITY;
import static com.toocomplicated.mademesmile.Constant.CONTENT;
import static com.toocomplicated.mademesmile.Constant.TABLE_NAME;
import static com.toocomplicated.mademesmile.Constant.TIME;
import static com.toocomplicated.mademesmile.Constant._ID;
import static com.toocomplicated.mademesmile.Constant.URI_STORY;

/**
 * Created by Win8.1 on 14/10/2558.
 */
public class StoryProvider extends ContentProvider {
    private static final int STORY = 1;
    private static final int STORY_WITH_ID = 2;

    private ShareHelper helper;
    private UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "notes", STORY);
        uriMatcher.addURI(AUTHORITY, "notes/#", STORY_WITH_ID);

        helper = new ShareHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        String whereClause = "";

        if(uriMatcher.match(uri) == STORY_WITH_ID){
            long id = Long.parseLong(uri.getPathSegments().get(1));
            whereClause = " WHERE _id = " + id + " ";
        }

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id, datetime(time/1000, 'unixepoch', 'localtime') as time, content FROM " + TABLE_NAME + whereClause + " ORDER_BY " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();

        if(uriMatcher.match(uri) != STORY){
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        long id = db.insertOrThrow(TABLE_NAME, null, values);

        Uri newUri = ContentUris.withAppendedId(URI_STORY, id);
        getContext().getContentResolver().notifyChange(newUri, null);
        return  newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)){
            case STORY:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case STORY_WITH_ID:
                long id = Long.parseLong(uri.getPathSegments().get(1));
                count = db.delete(TABLE_NAME, appendRowId(selection, id), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)){
            case STORY:
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case STORY_WITH_ID:
                long id = Long.parseLong(uri.getPathSegments().get(1));
                count = db.update(TABLE_NAME, values, appendRowId(selection, id), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    private String appendRowId(String selection, long id){
        return _ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
    }

    private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.toocomplicated.mademesmile";
    private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.toocomplicated.mademesmile";

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case STORY:
                return CONTENT_TYPE;
            case STORY_WITH_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
}
