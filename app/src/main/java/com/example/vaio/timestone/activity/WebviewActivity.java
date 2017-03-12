package com.example.vaio.timestone.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.fragment.ContentMainFragment;

public class WebviewActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
    private WebView webView;
    private ContentLoadingProgressBar progressBar;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.webview); // ánh xạ
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingProgressBar); // thanh bar loading web
        progressBar.show(); // show progress bar bắt đầu loading
        if (!MainActivity.isNetWorkAvailable(WebviewActivity.this)) {
            Toast.makeText(WebviewActivity.this, "No internet connection !", Toast.LENGTH_SHORT);
            progressBar.hide(); // ẩn progress bar khi không có kết nối internet
        }
        link = getIntent().getExtras().getString(ContentMainFragment.LINK);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.hide(); // ẩn progress bar khi loading thành công

            }
        });
        webView.loadUrl(link); // load dữ liệu vào webview từ link

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview_activity, menu);
        MenuItem itemShare = menu.findItem(R.id.action_share);
        itemShare.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(Intent.createChooser(share, "Bạn muốn chia sẻ trên..."));
                break;

        }
        return false;
    }
}
