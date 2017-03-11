package com.example.vaio.timestone.sync_task;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.vaio.timestone.database.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by vaio on 11/03/2017.
 */

public class CopyingDataAsyncTask extends AsyncTask<Void, Void, Void> {
    public static final String PATH_SRC = Environment.getDataDirectory() + "/data/com.example.vaio.timestone/databases/" + Database.DB_NAME;
    public static final String PATH_DES = Environment.getExternalStorageDirectory() + "/Database/" + Database.DB_NAME;
    private static final String TAG = "CopyingDataAsyncTask";

    @Override
    protected Void doInBackground(Void... params) {
        try {
            File file = new File(PATH_SRC);
            File fileOutPut = new File(PATH_DES);
            if (!fileOutPut.exists()) {
                File fileParent = fileOutPut.getParentFile();
                fileParent.mkdirs();
            }
            FileInputStream inputStream = new FileInputStream(file);
            FileOutputStream outputStream = new FileOutputStream(fileOutPut);
            byte b[] = new byte[1024];
            int count = inputStream.read(b);
            while (count != -1) {
                outputStream.write(b, 0, count);
                count = inputStream.read(b);
                Log.e(TAG, count + "");
            }
            Log.e(TAG, PATH_DES);
            inputStream.close();
            outputStream.close();
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
