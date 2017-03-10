package com.example.vaio.timestone.model;

import java.util.Calendar;

/**
 * Created by vaio on 08/03/2017.
 */

public class CurrentTime {
    public static Calendar calendar = null;

    public static int getDay() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public static int getYear() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
}
