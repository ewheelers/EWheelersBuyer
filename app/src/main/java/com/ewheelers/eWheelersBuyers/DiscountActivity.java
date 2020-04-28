package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

public class DiscountActivity extends AppCompatActivity {
WebView webView;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_discount);
        webView = findViewById(R.id.webview_discount);
        imageView = findViewById(R.id.close_icon);
        webView.loadUrl("file:///android_asset/discount_banner.html");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

    }
}
