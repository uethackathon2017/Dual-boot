package com.example.vaio.timestone.sync_task;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vaio.timestone.database.Database;
import com.example.vaio.timestone.model.Item;

import java.util.ArrayList;

/**
 * Created by vaio on 11/03/2017.
 */

public class RefreshDataAsyncTask extends AsyncTask<Void, Void, ArrayList<Item>> {
    private Context context;

    public RefreshDataAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Item> doInBackground(Void... params) {
        Database database = new Database(context);
        ArrayList<Item> arrItemTmp = new ArrayList<>();
        arrItemTmp.addAll(database.getData());
        return arrItemTmp;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> arrayList) {
        super.onPostExecute(arrayList);
        if (onComplete != null) {
            onComplete.onComplete(arrayList);
        }
    }

    public void setOnComplete(OnComplete onComplete) {
        this.onComplete = onComplete;
    }

    private OnComplete onComplete;

    public interface OnComplete {
        void onComplete(ArrayList<Item> arrItem);
    }
}
