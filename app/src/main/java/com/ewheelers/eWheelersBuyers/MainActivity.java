package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


     /*   webView = findViewById(R.id.webView1);
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/index.html");*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String tokenvalue = new SessionStorage().getStrings(MainActivity.this, SessionStorage.tokenvalue);
                if (tokenvalue == null||tokenvalue.equals("Invalid Token")) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    overridePendingTransition(R.anim.slide_from_bottom,R.anim.slide_up_in);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(MainActivity.this, NavAppBarActivity.class);
                    overridePendingTransition(R.anim.slide_from_bottom,R.anim.slide_up_in);
                    startActivity(i);
                    finish();
                }
            }

        }, 2*1000); // wait for
    }

  /*  private class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }*/
}
