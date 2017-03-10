package com.example.vaio.timestone.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.async.LoadingDataAsyncTask;
import com.example.vaio.timestone.database.MyDatabase;
import com.example.vaio.timestone.fragment.ContentMainFragment;
import com.example.vaio.timestone.fragment.QuizFragment;
import com.example.vaio.timestone.model.GlobalData;
import com.example.vaio.timestone.model.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ITEM = "item";
    public static final int WHAT_COMPLETE_LOADING_DATA = 1;
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private ArrayList arrItem = new ArrayList();   // arr Main data
    private ContentMainFragment contentMainFragment;
    private ProgressDialog progressDialog;
//    private Handler handlerOnCompleteLoadingData = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == WHAT_COMPLETE_LOADING_DATA) {
//                arrItem = (ArrayList) msg.obj;
//                progressDialog.hide();
//            }
//        }
//    };

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

    }

    private void getData() {
        GlobalData data = (GlobalData) getApplication();
        arrItem.addAll(data.getArrItem());
        Toast.makeText(this, arrItem.size() + "data", Toast.LENGTH_SHORT).show();
        contentMainFragment.notifyData();
    }


    private void initComponent() throws Exception {
        contentMainFragment = new ContentMainFragment(arrItem);
        replaceContentMainLayout(contentMainFragment);
    }

    public void replaceContentMainLayout(Fragment fragment) throws Exception {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.content_main_layout, fragment);
        fragmentTransaction.commit();
    }

    public void initToolbar(String title) throws Exception {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initDrawerLayout() throws Exception {

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                coordinatorLayout.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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