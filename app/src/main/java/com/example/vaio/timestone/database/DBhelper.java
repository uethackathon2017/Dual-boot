package com.example.vaio.timestone.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sonbn on 3/11/17.
 */

public class DBhelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "timestone.sqlite";
    public static final String TB_NAME = "data";
    public static final int DB_VERSION = 1;

    public static final String COMMA_SEP = ",";
    public static final String TEXT_TYPE = " TEXT";
    public static final String NUMERIC = " INTEGER";

    public static final String ID = "id";
    public static final String TYPE = "e_type";
    public static final String INFO = "e_info";
    public static final String DATE = "e_date";
    public static final String DAY = "e_day";
    public static final String MONTH = "e_month";
    public static final String YEAR = "e_year";
    public static final String WEIGHT = "e_weight";
    public static final String URL = "url";

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TB_NAME;
    private static final String CREATE_TABLE = "CREATE TABLE " + TB_NAME + " ( " +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TYPE + TEXT_TYPE + COMMA_SEP +
            INFO + TEXT_TYPE + COMMA_SEP +
            DATE + TEXT_TYPE + COMMA_SEP +
            DAY + TEXT_TYPE + COMMA_SEP +
            MONTH + TEXT_TYPE + COMMA_SEP +
            YEAR + TEXT_TYPE + COMMA_SEP +
            WEIGHT + NUMERIC + COMMA_SEP +
            URL + TEXT_TYPE +
            " )";

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }
}
