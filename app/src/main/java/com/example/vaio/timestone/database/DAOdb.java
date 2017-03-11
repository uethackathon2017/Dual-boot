package com.example.vaio.timestone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.vaio.timestone.model.Item;

import java.util.ArrayList;

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

    public void insertData(ArrayList<Item> items){
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
    }

    public ArrayList<Item> getData() {
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
        return arrItem;
    }

    public ArrayList<Item> getDataWithDate(long fromDate, long toDate) {
        ArrayList<Item> arrItem = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBhelper.TB_NAME + " WHERE " + DBhelper.DATE + " BETWEEN " + fromDate + " AND " + toDate;
        Cursor cursor = database.rawQuery(selectQuery, null);
        //Cursor cursor = database.query(DBhelper.TB_NAME, null, DBhelper.DATE + ">? AND " + DBhelper.DATE + "<?", new String[]{fromDate, toDate}, null, null, null);
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
        return arrItem;
    }

    public void deleteData(){
        database.delete(DBhelper.TB_NAME, null, null);
    }
}
