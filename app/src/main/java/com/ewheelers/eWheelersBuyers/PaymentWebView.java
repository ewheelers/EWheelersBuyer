package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.view.View.GONE;

public class PaymentWebView extends AppCompatActivity {
    String orderpayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);
        WebView webView = findViewById(R.id.payweb);
        LinearLayout linearLayout = findViewById(R.id.process_layout);
        orderpayment = getIntent().getStringExtra("payurl");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                linearLayout.setVisibility(GONE);
            }
        },3000);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(PaymentWebView.this, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });
        webView.loadUrl(orderpayment);
    }



}
