package com.example.vaio.timestone.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.vaio.timestone.R;
import com.example.vaio.timestone.database.Database;
import com.example.vaio.timestone.model.GlobalData;
import com.example.vaio.timestone.model.Item;
import java.util.ArrayList;
/**
 * Created by vaio on 11/03/2017.
 */

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = "SplashActivity";
    public static final int RESULT_CODE = 0;
    private ArrayList<Item> arrItem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            dataLoadingAsyncTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private AsyncTask dataLoadingAsyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            Database database = new Database(SplashActivity.this);
            arrItem = database.getData(); // lấy dữ liệu từ database
            GlobalData globalData = (GlobalData) getApplication(); // ánh xạ dữ liệu toàn cục
            globalData.setArrItem(arrItem); // gán dữ liệu từ database cho dữ liệu toàn cục
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivityForResult(intent, RESULT_CODE); // Chuyển sang main activity ngay sau khi đã chuẩn bị xong dữ liệu
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE) {
            // thoát ứng dụng
            finish();
        }
    }

}
