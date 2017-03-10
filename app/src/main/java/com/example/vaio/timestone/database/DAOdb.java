package com.example.vaio.timestone.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sonbn on 3/11/17.
 */

public class DAOdb {
    public SQLiteDatabase database;
    private DBhelper dbHelper;

    public DAOdb(Context context) {
        dbHelper = new DBhelper(context);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * close any database object
     */
    public void close() {
        dbHelper.close();
    }
}
