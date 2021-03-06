package com.example.vaio.timestone.model;

/**
 * Created by vaio on 10/03/2017.
 */

public class Item {
    int e_id; // id
    String e_type; // loại
    String e_info;// nội dung của ngày
    long e_date; // ngày tháng
    String e_day; // ngày
    String e_month; // tháng
    String e_year; // năm
    int e_weight; // số lượt xem - mức độ ưu tiên
    String url; // đường đẫn tới trang full nội dung

    public Item() {


    }

    public Item(int e_id, String e_type, String e_info, long e_date, String e_day, String e_month, String e_year, int e_weight, String url) {
        this.e_id = e_id;
        this.e_type = e_type;
        this.e_info = e_info;
        this.e_date = e_date;
        this.e_day = e_day;
        this.e_month = e_month;
        this.e_year = e_year;
        this.e_weight = e_weight;
        this.url = url;
    }

    public int getE_id() {
        return e_id;
    }

    public void setE_id(int e_id) {
        this.e_id = e_id;
    }

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

    public long getE_date() {
        return e_date;
    }

    public void setE_date(long e_date) {
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
}