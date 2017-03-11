package com.example.vaio.timestone.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.example.vaio.timestone.model.Item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by vaio on 10/03/2017.
 */

public class MyDatabase {
    public static final String ID = "id";
    public static final String TYPE = "e_type";
    public static final String INFO = "e_info";
    public static final String DATE = "e_date";
    public static final String DAY = "e_day";
    public static final String MONTH = "e_month";
    public static final String YEAR = "e_year";
    public static final String WEIGHT = "e_weight";
    public static final String URL = "url";
    public static final String BIRTHDAY = "BIRTHDAY";
    public static final String DEATH = "DEATH";
    public static final String EVENT = "EVENT";

    public static final String DB_NAME = "timestone.sqlite";
    public static final String TB_NAME = "data";
    public static final String PATH = Environment.getDataDirectory() + "/data/com.example.vaio.timestone/databases" + DB_NAME;
    private Context context;
    private SQLiteDatabase database;

    public MyDatabase(Context context) {
        this.context = context;
        copyDatabase(context);
    }

    public void openDatabase() {
        database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
    }

    public void closeDatabase() {
        database.close();
    }

    private void copyDatabase(Context context) {
        File file = new File(PATH);
        if (file.exists()) {
            return;
        }
        File parentFile = file.getParentFile();
        parentFile.mkdirs();

        byte[] b = new byte[1024];
        try {
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

    public void insertData(ArrayList<Item> arrItem) {
        openDatabase();
        for (int i = 0; i < arrItem.size(); i++) {
            Item item = arrItem.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(TYPE, item.getE_type());
            contentValues.put(INFO, item.getE_info());
            contentValues.put(DATE, item.getE_date());
            contentValues.put(DAY, item.getE_day());
            contentValues.put(MONTH, item.getE_month());
            contentValues.put(YEAR, item.getE_year());
            contentValues.put(WEIGHT, item.getE_weight());
            database.insert(TB_NAME, null, contentValues);
        }
        closeDatabase();
    }

    public void insertItem(Item item) {
        openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE, item.getE_type());
        contentValues.put(INFO, item.getE_info());
        contentValues.put(DATE, item.getE_date());
        contentValues.put(DAY, item.getE_day());
        contentValues.put(MONTH, item.getE_month());
        contentValues.put(YEAR, item.getE_year());
        contentValues.put(WEIGHT, item.getE_weight());
        database.insert(TB_NAME, null, contentValues);
        closeDatabase();
    }

    public ArrayList<Item> getData() {
        openDatabase();
        ArrayList<Item> arrItem = new ArrayList<>();
        Cursor cursor = database.query(TB_NAME, null, null, null, null, null, null);
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
        }

        closeDatabase();
        return arrItem;
    }

//    public ArrayList<Item> getDataFromCentury(int century) {
//        int yearStart = (century - 1) * 100;
//        int yearEnd = century * 100;
//        Cursor cursor = database.rawQuery("SELECT * FROM " + TB_NAME + " WHERE " + YEAR + " BETWEEN " + yearStart + " AND " + yearEnd, null);
//        ArrayList<Item> arrItem = new ArrayList<>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String type = cursor.getString(cursor.getColumnIndex(TYPE));
//            String info = cursor.getString(cursor.getColumnIndex(INFO));
//            String date = cursor.getString(cursor.getColumnIndex(DATE));
//            String day = cursor.getString(cursor.getColumnIndex(DAY));
//            String month = cursor.getString(cursor.getColumnIndex(MONTH));
//            String year = cursor.getString(cursor.getColumnIndex(YEAR));
//            int weight = cursor.getInt(cursor.getColumnIndex(WEIGHT));
//            String url = cursor.getString(cursor.getColumnIndex(URL));
//            Item item = new Item(type, info, date, day, month, year, weight, url);
//            arrItem.add(item);
//            cursor.moveToNext();
//        }
//        return arrItem;
//    }

}
