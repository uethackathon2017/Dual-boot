package com.example.vaio.timestone.sync_task;

import android.os.AsyncTask;

import com.example.vaio.timestone.model.Item;

import java.util.ArrayList;

/**
 * Created by vaio on 11/03/2017.
 */

public class OnNumberPickerSelectedAsyncTask extends AsyncTask<Void, Void, ArrayList<Item>> {
    private ArrayList<Item> arrItemTmp;
    private ArrayList<Item> arrItem = new ArrayList<>();
    private int centurySelected;
    private int yearSelected;
    private int monthSelected;

    public OnNumberPickerSelectedAsyncTask(ArrayList<Item> arrItemTmp, int centurySelected, int yearSelected, int monthSelected) {
        this.arrItemTmp = arrItemTmp;
        this.centurySelected = centurySelected;
        this.yearSelected = yearSelected;
        this.monthSelected = monthSelected;
    }


    protected ArrayList<Item> doInBackground(Void... params) {
        try {
            arrItem.clear(); // xóa dữ liệu để chuẩn bị dữ liệu cho nội dung mới
            for (int i = 0; i < arrItemTmp.size(); i++) {
                Item item = arrItemTmp.get(i);
                int year = Integer.parseInt(item.getE_year().toString().trim()); // lấy ra năm
                int century;
                if (year % 100 == 0) {
                    century = year / 100; // tính ra thế kỉ từ năm
                } else {
                    century = year / 100 + 1; //// tính ra thế kỉ từ năm
                }

                int month = Integer.parseInt(item.getE_month().toString().trim()); // lấy ra tháng
                if (century == centurySelected) {
                    if (year == yearSelected || yearSelected == 0) {
                        if (month == monthSelected || monthSelected == 0) {
                            arrItem.add(arrItemTmp.get(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrItem;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> arrItem) {
        super.onPostExecute(arrItem);
        if (onSuccessfulLoadingData != null) {
            onSuccessfulLoadingData.onSuccess(arrItem);
        }
    }

    public void setOnSuccessfulLoadingData(OnSuccessfulLoadingData onSuccessfulLoadingData) {
        this.onSuccessfulLoadingData = onSuccessfulLoadingData;
    }

    private OnSuccessfulLoadingData onSuccessfulLoadingData;

    public interface OnSuccessfulLoadingData {
        void onSuccess(ArrayList<Item> arrData);
    }
}
