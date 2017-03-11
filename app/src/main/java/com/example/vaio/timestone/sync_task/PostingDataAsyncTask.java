package com.example.vaio.timestone.sync_task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vaio.timestone.database.MyDatabase;
import com.example.vaio.timestone.model.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by vaio on 11/03/2017.
 */

public class PostingDataAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context context;

    public PostingDataAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            InputStream inputStream = context.getResources().getAssets().open("ultimate.json");
            byte b[] = new byte[1024];
            int count = inputStream.read(b);
            StringBuilder stringBuilder = new StringBuilder();
            while (count != -1) {
                String s = new String(b);
                stringBuilder.append(s);
                count = inputStream.read(b);
            }
            inputStream.close();
            Log.e("TAG", stringBuilder.toString());
            ArrayList<Item> arrItem = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String type = jsonObject.getString(MyDatabase.TYPE);
                String indo = jsonObject.getString(MyDatabase.INFO);
                long date = jsonObject.getLong(MyDatabase.DATE);
                String day = jsonObject.getString(MyDatabase.DAY);
                String month = jsonObject.getString(MyDatabase.MONTH);
                String year = jsonObject.getString(MyDatabase.YEAR);
                int weight = jsonObject.getInt(MyDatabase.WEIGHT);
                String url = jsonObject.getString(MyDatabase.URL);
                Log.e("TAG", date + ":" + type);
                Item item = new Item(type, indo, date, day, month, year, weight, url);
                arrItem.add(item);
                reference.child("item").push().setValue(item);
                Thread.sleep(10);
            }
            Log.e("TAG", arrItem.size() + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

