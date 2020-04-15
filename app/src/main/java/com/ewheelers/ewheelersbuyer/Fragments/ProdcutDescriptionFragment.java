package com.ewheelers.ewheelersbuyer.Fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.ewheelers.ewheelersbuyer.ProductDescriptionActivity;
import com.ewheelers.ewheelersbuyer.R;


public class ProdcutDescriptionFragment extends Fragment {
    String description;
    TextView html_reponse;
    private Spanned htmlAsSpanned;
    WebView webView;
    public ProdcutDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_prodcut_description, container, false);
        webView = root.findViewById(R.id.web_view);
        ProductDescriptionActivity activity = (ProductDescriptionActivity) getActivity();
        String getData = activity.sendData();
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript

        webView.setWebViewClient(new WebViewClient() {
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

       /* html_reponse = root.findViewById(R.id.html_reponsetxt);
        ProductDescriptionActivity activity = (ProductDescriptionActivity) getActivity();
        String getData = activity.sendData();
        htmlAsSpanned = Html.fromHtml(getData);
        html_reponse.setText(htmlAsSpanned);*/

        webView.loadData(getData, "text/html", "UTF-8");

        return root;

    }

}
