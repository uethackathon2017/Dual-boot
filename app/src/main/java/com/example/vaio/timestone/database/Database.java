package com.example.vaio.timestone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.vaio.timestone.model.Item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.vaio.timestone.fragment.ContentMainFragment.TAG;

/**
 * Created by sonbn on 3/11/17.
 */

public class Database {
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

    public static final String DB_NAME = "timestone.sqlite";
    public static final String PATH = Environment.getDataDirectory() + "/data/com.example.vaio.timestone/databases/" + DB_NAME;
    private Context context;

    public SQLiteDatabase database;

    public Database(Context context) {
        this.context = context;
        copyDatabase(context);
    }

    private void copyDatabase(Context context) {
        try {
            File file = new File(PATH);
            if (file.exists()) {
                return;
            }
            File parentFile = file.getParentFile();
            parentFile.mkdirs();

            byte[] b = new byte[1024];

            FileOutputStream outputStream = new FileOutputStream(file);
            InputStream inputStream = context.getAssets().open(DB_NAME);
            int count = inputStream.read(b);
            while (count != -1) {
                outputStream.write(b, 0, count);
                count = inputStream.read(b);
            }
            inputStream.close();
            outputStream.close();
            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * close any database object
     */
    public void openDatabase() {
        database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
    }

    public void closeDatabase() {
        database.close();
    }

    public void updateWeight(int id, int weight) {
        try {
            openDatabase();
            String sql = "Update " + TB_NAME + " SET " + WEIGHT + " = " + weight + " WHERE " + ID + " = " + id;
            database.execSQL(sql);
            closeDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Item> getData() {
        //lấy toàn bộ dữ liệu trong database
        try {
            openDatabase();
            ArrayList<Item> arrItem = new ArrayList<>();
            String sql = "SELECT * FROM " + TB_NAME + " ORDER BY " + WEIGHT + " DESC";
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                String type = cursor.getString(cursor.getColumnIndex(TYPE));
                String info = cursor.getString(cursor.getColumnIndex(INFO));
                long date = cursor.getLong(cursor.getColumnIndex(DATE));
                String day = cursor.getString(cursor.getColumnIndex(DAY));
                String month = cursor.getString(cursor.getColumnIndex(MONTH));
                String year = cursor.getString(cursor.getColumnIndex(YEAR));
                int weight = cursor.getInt(cursor.getColumnIndex(WEIGHT));
                Item item = new Item(id, type, info, date, day, month, year, weight, "");
                arrItem.add(item);
                cursor.moveToNext();

            }
            closeDatabase();
            return arrItem;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Item> getDataFromID(String stringID) {
        //lấy toàn bộ dữ liệu trong database
        String[] s = stringID.split("/");
        try {
            openDatabase();
            ArrayList<Item> arrItem = new ArrayList<>();

            for (int i = 1; i < s.length; i++) {

                String sql = "SELECT * FROM " + TB_NAME + " WHERE " + ID + " = " + s[i];
                Log.e(TAG,sql);
                Cursor cursor = database.rawQuery(sql, null);
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                String type = cursor.getString(cursor.getColumnIndex(TYPE));
                String info = cursor.getString(cursor.getColumnIndex(INFO));
                long date = cursor.getLong(cursor.getColumnIndex(DATE));
                String day = cursor.getString(cursor.getColumnIndex(DAY));
                String month = cursor.getString(cursor.getColumnIndex(MONTH));
                String year = cursor.getString(cursor.getColumnIndex(YEAR));
                int weight = cursor.getInt(cursor.getColumnIndex(WEIGHT));
                Item item = new Item(id, type, info, date, day, month, year, weight, "");
                arrItem.add(item);
            }
            closeDatabase();
            return arrItem;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
