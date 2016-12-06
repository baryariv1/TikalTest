package com.example.baryariv.tikaltest.utils;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by baryariv on 05/12/2016.
 *
 * This class use to create the movies table in Sqlite DB.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String MOVIES_TABLE_NAME = "movies";
    public static final String MOVIES_COLUMN_TITLE = "title";
    public static final String MOVIES_COLUMN_ID = "_id";
    public static final String MOVIES_COLUMN_POSTER_PATH = "image";
    public static final String MOVIES_COLUMN_DATE = "date";
    public static final String MOVIES_COLUMN_OVERVIEW = "overview";
    public static final String MOVIES_COLUMN_SCORE = "score";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table movies " +
                        "(_id text primary key, title text,image text,date text, overview text, score text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS movies");
        onCreate(db);
    }
}
