package com.example.vaio.timestone.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.database.Database;
import com.example.vaio.timestone.fragment.ContentMainFragment;
import com.example.vaio.timestone.fragment.QuizFragment;
import com.example.vaio.timestone.fragment.WatchLaterFragment;
import com.example.vaio.timestone.model.GlobalData;
import com.example.vaio.timestone.model.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String STRING_ID = "string";
    public static final String ITEM = "item";
    private static final String TAG = "MainActivity";
    private static final java.lang.String EMAIL = "vietcoscc@gmail.com";
    private Toolbar toolbar;
    private TextView tvTitle; // titile trên toolbar
    private ImageView ivSearch; // image view search trên toolbar
    private ArrayList arrItem = new ArrayList();   // arr Main data
    private ContentMainFragment contentMainFragment; // fragment chính
    private QuizFragment quizFragment; // fragment câu đố
    private String stringIDData = "/";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            getData();
            initToolbar("CC / YYYY / MM ");
            initDrawerLayout();
            initComponent();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }


    private void initComponent() throws Exception {

        quizFragment = new QuizFragment(arrItem);
        contentMainFragment = new ContentMainFragment(arrItem);
        replaceContentMainLayout(contentMainFragment);
        sharedPreferences = getSharedPreferences(QuizFragment.SHARE_PRE, Context.MODE_PRIVATE);
        stringIDData = sharedPreferences.getString(STRING_ID, "/");
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
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle.setText(title);
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
                coordinatorLayout.setTranslationX(slideOffset * drawerView.getWidth()); // set tọa độ dịch chuyển
                drawer.bringChildToFront(drawerView); // set hiển thị drawer
                drawer.requestLayout(); // request thực thi
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // set onlick các phần tử trong navigation
        try {
            int id = item.getItemId();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            switch (id) {
                case R.id.nav_home:
                    // chọn fragment chính
                    tvTitle.setVisibility(View.VISIBLE);
                    replaceContentMainLayout(contentMainFragment);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                case R.id.nav_quiz:
                    // chọn fragment quiz
                    tvTitle.setVisibility(View.INVISIBLE);
                    replaceContentMainLayout(quizFragment);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                case R.id.nav_watch_later:
                    // chọn fragment quiz
                    Database database = new Database(this);
                    stringIDData = sharedPreferences.getString(STRING_ID, "");
                    Log.e(TAG, stringIDData);
                    ArrayList<Item> arrItem = database.getDataFromID(stringIDData);
                    if (arrItem != null) {
                        tvTitle.setVisibility(View.INVISIBLE);
                        replaceContentMainLayout(new WatchLaterFragment(arrItem));
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        Toast.makeText(this, "Không có sự kiện xem sau", Toast.LENGTH_SHORT).show();
                    }
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                case R.id.nav_share:
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, "_Time_stone_app_");
                    startActivity(Intent.createChooser(share, "Bạn muốn chia sẻ trên..."));
                    break;
                case R.id.nav_feed_back:
                    // lựa chọn phản hồi của người dùng
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                            Uri.parse("mailto:" + Uri.encode(EMAIL)));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(emailIntent, "Send email via..."));
                    break;
                case R.id.nav_exit:
                    finish();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}