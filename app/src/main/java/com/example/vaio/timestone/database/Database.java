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
//        dbHelper = new DBhelper(context);
//        database = dbHelper.getWritableDatabase();

    }

    private void copyDatabase(Context context) {
        try {
            File file = new File(PATH);
            Log.e(TAG, file.getAbsolutePath());
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

//    public void insertData(ArrayList<Item> items) {
//        openDatabase();
//        String sql = "INSERT INTO " + DBhelper.TB_NAME + " VALUES (?,?,?,?,?,?,?,?,?);";
//        SQLiteStatement statement = database.compileStatement(sql);
//        database.beginTransaction();
//
//        for (Item item : items) {
//            statement.clearBindings();
//            statement.bindString(2, item.getE_type());
//            statement.bindString(3, item.getE_info());
//            statement.bindLong(4, item.getE_date());
//            statement.bindString(5, item.getE_day());
//            statement.bindString(6, item.getE_month());
//            statement.bindString(7, item.getE_year());
//            statement.bindLong(8, item.getE_weight());
//            statement.bindString(9, item.getUrl());
//            statement.execute();
//        }
//        database.setTransactionSuccessful();
//        database.endTransaction();
//        closeDatabase();
//    }

    public ArrayList<Item> getData() {
        openDatabase();
        ArrayList<Item> arrItem = new ArrayList<>();
        Cursor cursor = database.query("data", null, null, null, null, null, null);
        int typeIndex = cursor.getColumnIndex(TYPE);
        int infoIndex = cursor.getColumnIndex(INFO);
        int dateIndex = cursor.getColumnIndex(DATE);
        int dayIndex = cursor.getColumnIndex(DAY);
        int monthIndex = cursor.getColumnIndex(MONTH);
        int yearIndex = cursor.getColumnIndex(YEAR);
        int weightIndex = cursor.getColumnIndex(WEIGHT);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String type = cursor.getString(typeIndex);
            String info = cursor.getString(infoIndex);
            long date = cursor.getLong(dateIndex);
            String day = cursor.getString(dayIndex);
            String month = cursor.getString(monthIndex);
            String year = cursor.getString(yearIndex);
            int weight = cursor.getInt(weightIndex);
            Item item = new Item(type, info, date, day, month, year, weight, "");
            arrItem.add(item);
            cursor.moveToNext();
            Log.e(TAG, arrItem.size() + "");

        }
        closeDatabase();
        return arrItem;
    }

    public ArrayList<Item> getDataWithDate(long fromDate, long toDate) {
        openDatabase();
        ArrayList<Item> arrItem = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TB_NAME + " WHERE " + DATE + " >" + fromDate + " AND " + DATE + " < " + toDate + " ORDER BY " + YEAR;
        Cursor cursor = database.rawQuery(selectQuery, null);
//        Cursor cursor = database.query(DBhelper.TB_NAME, null, DBhelper.DATE + ">? AND " + DBhelper.DATE + "<?", new String[]{String.valueOf(fromDate), String.valueOf(toDate)}, null, null, null);
        int typeIndex = cursor.getColumnIndex(TYPE);
        int infoIndex = cursor.getColumnIndex(INFO);
        int dateIndex = cursor.getColumnIndex(DATE);
        int dayIndex = cursor.getColumnIndex(DAY);
        int monthIndex = cursor.getColumnIndex(MONTH);
        int yearIndex = cursor.getColumnIndex(YEAR);
        int weightIndex = cursor.getColumnIndex(WEIGHT);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String type = cursor.getString(typeIndex);
            String info = cursor.getString(infoIndex);
            long date = cursor.getLong(dateIndex);
            String day = cursor.getString(dayIndex);
            String month = cursor.getString(monthIndex);
            String year = cursor.getString(yearIndex);
            int weight = cursor.getInt(weightIndex);
            Item item = new Item(type, info, date, day, month, year, weight, "");
            arrItem.add(item);
            cursor.moveToNext();
            Log.e("DATE ", String.valueOf(item.getE_date()));
        }
        closeDatabase();
        return arrItem;
    }

    public void deleteData() {
        database.delete(TB_NAME, null, null);
    }
}
