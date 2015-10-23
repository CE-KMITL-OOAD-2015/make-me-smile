package com.toocomplicated.mademesmile;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by SUPPORTERROR on 13/10/2558.
 */
public interface Constant extends BaseColumns {
    public static final String TABLE_NAME = "notes";
    public static final String TIME = "time";
    public static final String CONTENT = "content";
    public static final String AUTHORITY = "com.toocomplicated.mademesmile";
    public static final Uri URI_STORY = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
}
