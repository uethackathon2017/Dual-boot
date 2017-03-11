package com.example.vaio.timestone.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.database.Database;
import com.example.vaio.timestone.model.GlobalData;
import com.example.vaio.timestone.model.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.vaio.timestone.activity.MainActivity.ITEM;

/**
 * Created by vaio on 11/03/2017.
 */

public class SplashActivity extends AppCompatActivity {
    public static final int RESULT_CODE = 0;
    public static final int WHAT_DATA = 0;
    private ArrayList<Item> arrItem;
    public static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
//            getdata();
            dataLoadingAsyncTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getdata() throws Exception {
        // Load dữ liệu online hoặc offline vào mảng arrItem
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child(ITEM).keepSynced(true); // Lưu dữ liệu khi sử dụng offline
        reference.child(ITEM).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Item item = dataSnapshot.getValue(Item.class);
                arrItem.add(item); // add item lấy được vào mảng
                Log.e(TAG, arrItem.size() + "");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(ITEM).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GlobalData globalData = (GlobalData) getApplication();
                globalData.setArrItem(arrItem);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivityForResult(intent, RESULT_CODE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private AsyncTask dataLoadingAsyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            Database database = new Database(SplashActivity.this);
            arrItem = database.getData();
            GlobalData globalData = (GlobalData) getApplication();
            globalData.setArrItem(arrItem);
//            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//            DatabaseReference reference = firebaseDatabase.getReference();
//
//            for (int i = 0; i < arrItem.size(); i++) {
//                reference.child("item").child(arrItem.get(i).getE_id() + "").setValue(arrItem.get(i));
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Log.e(TAG,i+"");
//            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivityForResult(intent, RESULT_CODE);
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
