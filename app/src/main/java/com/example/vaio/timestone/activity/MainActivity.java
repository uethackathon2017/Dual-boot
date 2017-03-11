package com.example.vaio.timestone.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.database.DAOdb;
import com.example.vaio.timestone.fragment.ContentMainFragment;
import com.example.vaio.timestone.fragment.QuizFragment;
import com.example.vaio.timestone.model.GlobalData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ITEM = "item";
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private ArrayList arrItem = new ArrayList();   // arr Main data
    private ContentMainFragment contentMainFragment;
    private DAOdb daOdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            initToolbar("CC / YYYY / MM ");
            initDrawerLayout();
            initComponent();
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        daOdb = new DAOdb(this);
        daOdb.deleteData();
        daOdb.insertData(arrItem);

        daOdb.getDataWithDate(10101, 991231);
    }

    public static boolean isNetWorkAvailable(Context context) {
        // kiểm tra trạng thái network
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void getData() {
        // Lấy về dữ liệu sau khi đã load
        GlobalData data = (GlobalData) getApplication();
        arrItem.addAll(data.getArrItem());
        contentMainFragment.notifyData();
    }


    private void initComponent() throws Exception {
        contentMainFragment = new ContentMainFragment(arrItem);
        replaceContentMainLayout(contentMainFragment);
    }

    public void replaceContentMainLayout(Fragment fragment) throws Exception {
        // Thay đổi nội dung màn hình chính
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.content_main_layout, fragment);
        fragmentTransaction.commit();
    }

    public void initToolbar(String title) throws Exception {
        // Khởi tạo thanh Action Bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initDrawerLayout() throws Exception {
        // khởi tạo drawer layout
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                // Dịch chuyển layout chính theo drawerlayout
                coordinatorLayout.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState(); //  đồng bộ trạng thái nút Home và drawerlayout

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // đóng drawerlayout khi nhấn back
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // Hiển thị dialog xác nhận thoát ứng dụng
            AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setResult(SplashActivity.RESULT_CODE);
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setMessage("Bạn có muốn thoát không ?");
            builder.create().show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // set onlick các phần tử trong navigation
        try {
            int id = item.getItemId();
            switch (id) {
                case R.id.nav_home:
                    replaceContentMainLayout(new ContentMainFragment(arrItem));
                    break;
                case R.id.nav_quiz:
                    replaceContentMainLayout(new QuizFragment(arrItem));
                    break;
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}