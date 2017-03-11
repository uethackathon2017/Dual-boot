package com.example.vaio.timestone.activity;

import android.content.Intent;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingProgressBar);
        progressBar.show();
        link = getIntent().getExtras().getString(ContentMainFragment.LINK);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.hide();
            }
        });
        webView.loadUrl(link);

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
