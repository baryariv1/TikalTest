package com.example.baryariv.tikaltest.utils;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


/**
 * Created by baryariv on 05/12/2016.
 *
 * This class is the content provider - managed the access to Sqlite DB.
 */
public class MoviesContentProvider extends ContentProvider {

    private DBHelper database;

    private static final int MOVIES = 10;
    private static final int MOVIES_ID = 20;

    private static final String AUTHORITY = "com.example.baryariv.tikaltest.utils";

    private static final String BASE_PATH = "movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, MOVIES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIES_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(DBHelper.MOVIES_TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MOVIES:
                break;
            case MOVIES_ID:
                queryBuilder.appendWhere(DBHelper.MOVIES_COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case MOVIES:
                id = sqlDB.insertWithOnConflict(DBHelper.MOVIES_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case MOVIES:
                rowsDeleted = sqlDB.delete(DBHelper.MOVIES_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case MOVIES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            DBHelper.MOVIES_TABLE_NAME,
                            DBHelper.MOVIES_COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            DBHelper.MOVIES_TABLE_NAME,
                            DBHelper.MOVIES_COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case MOVIES:
                rowsUpdated = sqlDB.update(DBHelper.MOVIES_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case MOVIES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DBHelper.MOVIES_TABLE_NAME,
                            values,
                            DBHelper.MOVIES_COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DBHelper.MOVIES_TABLE_NAME,
                            values,
                            DBHelper.MOVIES_COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {DBHelper.MOVIES_COLUMN_OVERVIEW, DBHelper.MOVIES_COLUMN_POSTER_PATH,
                DBHelper.MOVIES_COLUMN_TITLE, DBHelper.MOVIES_COLUMN_ID, DBHelper.MOVIES_COLUMN_DATE, DBHelper.MOVIES_COLUMN_SCORE};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }

}
