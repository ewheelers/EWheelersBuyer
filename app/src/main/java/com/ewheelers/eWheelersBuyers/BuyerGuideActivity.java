package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class BuyerGuideActivity extends AppCompatActivity {
    WebView mWebview;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_guide);
        mWebview = findViewById(R.id.webview);
        //mWebview.loadUrl("https://ewheelers.in/blog");
        //Toast.makeText(this, "Loading ... Please wait", Toast.LENGTH_LONG).show();
        String getText = getIntent().getStringExtra("opens");
        String geturlas = getIntent().getStringExtra("openurl");
        String bottomurl = getIntent().getStringExtra("openurlbottom");
        String topurl = getIntent().getStringExtra("openurltop");
        String banurl = getIntent().getStringExtra("bannerurl");
        if(banurl!=null)
        {
            url = banurl;
        }
        if (topurl != null)
        {
            url = topurl;
        }
        if (bottomurl != null)
        {
            url = bottomurl;
        }

        if (geturlas != null)
        {
            url = geturlas;
        }

        if (getText != null)
        {
            if (getText.equals("openshort")) {
                url = "https://ewheelers.in/about-us";
            } else if (getText.equals("howworks")) {
                url = "https://ewheelers.in/service-process";
            } else if (getText.equals("terms")) {
                url = "https://ewheelers.in/terms-conditions";
            } else if (getText.equals("support")) {
                url = "https://ewheelers.in/ev-store-near-you";
            } else {
                url = "https://ewheelers.in/blog";
            }
        }

        mWebview.loadUrl(url);
        mWebview.canGoBack();
        mWebview.canGoForward();
        mWebview.getSettings().setJavaScriptEnabled(true);

     /*   mWebview = new WebView(this);
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity;
        activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        mWebview.loadUrl(url);
        setContentView(mWebview);
*/

    }
}
