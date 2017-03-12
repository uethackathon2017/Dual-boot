package com.example.vaio.timestone.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.adapter.EventRecyclerViewAdapter;
import com.example.vaio.timestone.database.Database;
import com.example.vaio.timestone.model.GlobalData;
import com.example.vaio.timestone.model.Item;
import com.example.vaio.timestone.async_task.RefreshDataAsyncTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String LINK = "link";
    private SearchView searchView;
    private ArrayList<Item> arrItem = new ArrayList();
    private ArrayList<Item> arrItemTmp = new ArrayList();
    private RecyclerView recyclerView;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private String stringIDData = "/";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        try {
            initData();
            initToolbar();
            initComponent();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initData() throws Exception {
        GlobalData globalData = (GlobalData) getApplication();
        arrItem.addAll(globalData.getArrItem());
        arrItemTmp.addAll(globalData.getArrItem());
    }

    private void initToolbar() throws Exception {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() throws Exception {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(arrItem);
        recyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerViewAdapter.setOnItemClick(new EventRecyclerViewAdapter.OnItemClick() {
            @Override
            public void onClick(View view, int position) {
                final Item item = arrItem.get(position);
                item.setE_weight(item.getE_weight() + 1);
                reference.child("item").child(item.getE_id() + "").child(Database.WEIGHT).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int weight = dataSnapshot.getValue(Integer.class);
                        Database database = new Database(SearchActivity.this);
                        database.updateWeight(item.getE_id(), weight);
                        RefreshDataAsyncTask refreshDataAsyncTask = new RefreshDataAsyncTask(SearchActivity.this);
                        refreshDataAsyncTask.setOnComplete(new RefreshDataAsyncTask.OnComplete() {
                            @Override
                            public void onComplete(ArrayList<Item> arrItem) {
                                arrItemTmp.clear();
                                arrItemTmp.addAll(arrItem);
                            }
                        });
                        refreshDataAsyncTask.execute();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                reference.child("item").child(item.getE_id() + "").child(Database.WEIGHT).setValue(item.getE_weight());

                Intent intent = new Intent(SearchActivity.this, WebviewActivity.class);
                intent.putExtra(LINK, "http://www.google.com/search?btnI=I'm+Feeling+Lucky&q=" + arrItem.get(position).getE_info().trim()); //
                // Đường link tới nội dung
                startActivity(intent);
            }
        });
        eventRecyclerViewAdapter.setOnItemLongClick(new EventRecyclerViewAdapter.OnItemLongClick() {
            @Override
            public void onClick(View view, int position) {
                showDialogContent(SearchActivity.this,arrItem.get(position).getE_info(),arrItem.get(position).getE_id());
            }
        });
        contentLoadingProgressBar = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingProgressBar);
        contentLoadingProgressBar.show();
    }
    public void showDialogContent(final Context context, final String content, final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Xem sau", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!stringIDData.contains("/" + id + "/")) {
                    stringIDData = stringIDData + id + "/";
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MainActivity.STRING_ID, stringIDData);
                editor.commit();
            }
        });
        builder.setMessage(content);
        builder.create().show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) itemSearch.getActionView();
        itemSearch.collapseActionView();
        searchView.setOnQueryTextListener(this);
        // tự động focus vào search view
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // query nội dung trên search view
        contentLoadingProgressBar.show(); //
        arrItem.clear();
        for (int i = 0; i < arrItemTmp.size(); i++) { // duyệt mảng xét các thuộc tính của phần tử
            Item item = arrItemTmp.get(i);
            String s = item.getE_day() + "/" + item.getE_month() + "/" + item.getE_year() + "/" + item.getE_info() + "/" + item.getE_type();
            if (s.toLowerCase().contains(query.toLowerCase())) { // kiểm tra nếu có chứa cụm từ query thì add vào mảng data để hiển thị
                arrItem.add(item);
            }
        }
        contentLoadingProgressBar.hide(); // ẩn progress bar khi đã chuẩn bị xong dữ liệu
        eventRecyclerViewAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
}
