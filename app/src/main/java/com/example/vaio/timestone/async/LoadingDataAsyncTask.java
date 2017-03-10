package com.example.vaio.timestone.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.vaio.timestone.activity.MainActivity;
import com.example.vaio.timestone.model.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.vaio.timestone.activity.MainActivity.ITEM;

/**
 * Created by vaio on 11/03/2017.
 */

public class LoadingDataAsyncTask extends AsyncTask<Void, Void, ArrayList<Item>> implements ChildEventListener {
    private ArrayList arrItem = new ArrayList();
    private Handler handlerOnCompleteLoadingData;

    public LoadingDataAsyncTask(Handler handlerOnCompleteLoadingData) {
        this.handlerOnCompleteLoadingData = handlerOnCompleteLoadingData;
    }

    @Override
    protected ArrayList<Item> doInBackground(Void... params) {
        ArrayList<Item> arrItem = new ArrayList<>();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child(ITEM).keepSynced(true);
        reference.child(ITEM).addChildEventListener(this);
        return arrItem;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> items) {
        Message message = new Message();
        message.what = MainActivity.WHAT_COMPLETE_LOADING_DATA;
        message.obj = arrItem;
        handlerOnCompleteLoadingData.sendMessage(message);
        super.onPostExecute(items);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Item item = dataSnapshot.getValue(Item.class);
        arrItem.add(item);
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
}
