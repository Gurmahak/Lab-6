package com.example.lab_6;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class RedditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddit);
        WebView webview =(WebView)findViewById(R.id.webview);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String Url = "https://www.reddit.com"+url;

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(Url);
    }
}
