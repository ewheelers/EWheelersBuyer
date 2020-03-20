package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class BuyerGuideActivity extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_guide);
        webView = findViewById(R.id.webview);//https://ewheelers.in/blog
        webView.loadUrl("https://ewheelers.in/blog");

    }
}
