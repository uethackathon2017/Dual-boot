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

public class DAOdb {
    public static final String DB_NAME = "timestone.sqlite";
    public static final String PATH = Environment.getDataDirectory() + "/data/com.example.vaio.timestone/databases" + DB_NAME;
    private  Context context;

    public SQLiteDatabase database;
    private DBhelper dbHelper;

    public DAOdb(Context context) {
        this.context = context;
        copyDatabase(context);
//        dbHelper = new DBhelper(context);
//        database = dbHelper.getWritableDatabase();

    }
    public void openDatabase() {
        database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
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
    public void closeDatabase() {
        database.close();
    }

    public void insertData(ArrayList<Item> items){
        openDatabase();
        String sql = "INSERT INTO "+ DBhelper.TB_NAME +" VALUES (?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = database.compileStatement(sql);
        database.beginTransaction();
        for (Item item : items) {
            statement.clearBindings();
            statement.bindString(2, item.getE_type());
            statement.bindString(3, item.getE_info());
            statement.bindLong(4, item.getE_date());
            statement.bindString(5, item.getE_day());
            statement.bindString(6, item.getE_month());
            statement.bindString(7, item.getE_year());
            statement.bindLong(8, item.getE_weight());
            statement.bindString(9, item.getUrl());
            statement.execute();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        closeDatabase();
    }

    public ArrayList<Item> getData() {
        openDatabase();
        ArrayList<Item> arrItem = new ArrayList<>();
        Cursor cursor = database.query(DBhelper.TB_NAME, null, null, null, null, null, null);
        int typeIndex = cursor.getColumnIndex(DBhelper.TYPE);
        int infoIndex = cursor.getColumnIndex(DBhelper.INFO);
        int dateIndex = cursor.getColumnIndex(DBhelper.DATE);
        int dayIndex = cursor.getColumnIndex(DBhelper.DAY);
        int monthIndex = cursor.getColumnIndex(DBhelper.MONTH);
        int yearIndex = cursor.getColumnIndex(DBhelper.YEAR);
        int weightIndex = cursor.getColumnIndex(DBhelper.WEIGHT);
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

    public ArrayList<Item> getDataWithDate(long fromDate, long toDate) {
        openDatabase();
        ArrayList<Item> arrItem = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBhelper.TB_NAME + " WHERE " + DBhelper.DATE + " >" + fromDate + " AND " + DBhelper.DATE + " < " + toDate + " ORDER BY " + DBhelper.YEAR;
        Cursor cursor = database.rawQuery(selectQuery, null);
//        Cursor cursor = database.query(DBhelper.TB_NAME, null, DBhelper.DATE + ">? AND " + DBhelper.DATE + "<?", new String[]{String.valueOf(fromDate), String.valueOf(toDate)}, null, null, null);
        int typeIndex = cursor.getColumnIndex(DBhelper.TYPE);
        int infoIndex = cursor.getColumnIndex(DBhelper.INFO);
        int dateIndex = cursor.getColumnIndex(DBhelper.DATE);
        int dayIndex = cursor.getColumnIndex(DBhelper.DAY);
        int monthIndex = cursor.getColumnIndex(DBhelper.MONTH);
        int yearIndex = cursor.getColumnIndex(DBhelper.YEAR);
        int weightIndex = cursor.getColumnIndex(DBhelper.WEIGHT);
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

    public void deleteData(){
        database.delete(DBhelper.TB_NAME, null, null);
    }
}
