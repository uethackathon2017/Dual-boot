package com.example.vaio.timestone.model;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by vaio on 11/03/2017.
 */
    // dữ liệu toàn cục
public class GlobalData extends Application {
    private ArrayList<Item> arrItem;

    public ArrayList<Item> getArrItem() {
        return arrItem;
    }

    public void setArrItem(ArrayList<Item> arrItem) {
        this.arrItem = arrItem;
    }
}
