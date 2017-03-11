package com.example.vaio.timestone.sync_task;

import android.os.AsyncTask;
import android.os.Environment;

import com.example.vaio.timestone.database.DBhelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by vaio on 11/03/2017.
 */

public class CopyingDataAsyncTask extends AsyncTask<Void, Void, Void> {
    public static final String PATH_SRC = Environment.getDataDirectory() + "/data/com.example.vaio.timestone/databases/" + DBhelper.DB_NAME;
    public static final String PATH_DES = Environment.getExternalStorageDirectory() + "/Database/" + DBhelper.DB_NAME;

    @Override
    protected Void doInBackground(Void... params) {
        File file = new File(PATH_SRC);
        if (!file.exists()) {
            return null;
        }
        try {
            File fileOutPut = new File(PATH_DES);
            if (!file.exists()) {
                fileOutPut.mkdirs();
            }
            FileInputStream inputStream = new FileInputStream(file);
            FileOutputStream outputStream = new FileOutputStream(fileOutPut);
            byte b[] = new byte[1024];
            int count = inputStream.read(b);
            while (count != -1) {
                outputStream.write(b, 0, count);
                count = inputStream.read(b);
            }
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
