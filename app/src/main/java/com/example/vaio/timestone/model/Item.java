package com.example.vaio.timestone.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;

/**
 * Created by vaio on 10/03/2017.
 */

public class Item implements Parcelable {

    String e_type;
    String e_info;
    String e_date;
    String e_day;
    String e_month;
    String e_year;
    int e_weight;
    String url;

    public Item() {
    }

    public Item(String e_type, String e_info, String e_date, String e_day, String e_month, String e_year, int e_weight, String url) {
        this.e_type = e_type;
        this.e_info = e_info;
        this.e_date = e_date;
        this.e_day = e_day;
        this.e_month = e_month;
        this.e_year = e_year;
        this.e_weight = e_weight;
        this.url = url;
    }

    protected Item(Parcel in) {
        e_type = in.readString();
        e_info = in.readString();
        e_date = in.readString();
        e_day = in.readString();
        e_month = in.readString();
        e_year = in.readString();
        e_weight = in.readInt();
        url = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getE_type() {
        return e_type;
    }

    public void setE_type(String e_type) {
        this.e_type = e_type;
    }

    public String getE_info() {
        return e_info;
    }

    public void setE_info(String e_info) {
        this.e_info = e_info;
    }

    public String getE_date() {
        return e_date;
    }

    public void setE_date(String e_date) {
        this.e_date = e_date;
    }

    public String getE_day() {
        return e_day;
    }

    public void setE_day(String e_day) {
        this.e_day = e_day;
    }

    public String getE_month() {
        return e_month;
    }

    public void setE_month(String e_month) {
        this.e_month = e_month;
    }

    public String getE_year() {
        return e_year;
    }

    public void setE_year(String e_year) {
        this.e_year = e_year;
    }

    public int getE_weight() {
        return e_weight;
    }

    public void setE_weight(int e_weight) {
        this.e_weight = e_weight;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(e_type);
        dest.writeString(e_info);
        dest.writeString(e_date);
        dest.writeString(e_day);
        dest.writeString(e_month);
        dest.writeString(e_year);
        dest.writeInt(e_weight);
        dest.writeString(url);
    }
}