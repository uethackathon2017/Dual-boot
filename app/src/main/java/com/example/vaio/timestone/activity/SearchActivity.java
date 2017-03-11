package com.example.vaio.timestone.activity;

import android.content.Intent;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.adapter.EventRecyclerViewAdapter;
import com.example.vaio.timestone.model.GlobalData;
import com.example.vaio.timestone.model.Item;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String LINK = "link";
    private SearchView searchView;
    private ArrayList<Item> arrItem = new ArrayList();
    private ArrayList<Item> arrItemTmp = new ArrayList();
    private RecyclerView recyclerView; // hiển thị dữ liệu
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;
    private ContentLoadingProgressBar contentLoadingProgressBar; // hiển thị trong khi loading data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();
        initToolbar();
        initComponent();
    }

    private void initData() {
        GlobalData globalData = (GlobalData) getApplication();
        arrItem.addAll(globalData.getArrItem());
        arrItemTmp.addAll(globalData.getArrItem());
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(arrItem); //
        recyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerViewAdapter.setOnItemClick(new EventRecyclerViewAdapter.OnItemClick() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, WebviewActivity.class); // chuyển sang activity nội dung của sự kiện
                intent.putExtra(LINK, "http://www.google.com/search?btnI=I'm+Feeling+Lucky&q=" + arrItem.get(position).getE_info().trim()); //
                // Đường link tới nội dung
                startActivity(intent);
            }
        });
        contentLoadingProgressBar = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingProgressBar);
        contentLoadingProgressBar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu); // inflate menu
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) itemSearch.getActionView(); // lấy action search cho search view
        itemSearch.collapseActionView(); //
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
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}
