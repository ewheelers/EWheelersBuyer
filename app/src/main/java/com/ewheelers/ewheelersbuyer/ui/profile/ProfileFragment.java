package com.ewheelers.ewheelersbuyer.ui.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.style.IconMarginSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.ServiceProvidersAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.ServiceProvidersClass;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.ShowServiceProvidersActivity;
import com.ewheelers.ewheelersbuyer.UpdateProfileActivity;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel mViewModel;
    private TextView user_Name, user_Email, Name, Mobile, Email, Address,dob, PrivacyPolicy, Edit, Bank, Reffer, Faq;
    private ImageView imageView;
    String tokenValue;
    CollapsingToolbarLayout collapsingToolbar;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);
        tokenValue = new SessionStorage().getStrings(getActivity(), SessionStorage.tokenvalue);

        collapsingToolbar = (CollapsingToolbarLayout) root.findViewById(R.id.toolbar_layout);
        user_Name = root.findViewById(R.id.userName);
        user_Email = root.findViewById(R.id.userEmail);
        Name = root.findViewById(R.id.name);
        Mobile = root.findViewById(R.id.mobileNo);
        Email = root.findViewById(R.id.emailId);
        Address = root.findViewById(R.id.address);
        PrivacyPolicy = root.findViewById(R.id.privacyPolicytext);
        Edit = root.findViewById(R.id.editProfile);
        Bank = root.findViewById(R.id.gotobank);
        Reffer = root.findViewById(R.id.referral);
        Faq = root.findViewById(R.id.faq);
        imageView = root.findViewById(R.id.profileImage);
        dob = root.findViewById(R.id.dobDate);
        PrivacyPolicy.setOnClickListener(this);
        Edit.setOnClickListener(this);
        getProfile();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // textView.setText(s);
            }
        });
    }

    private void getProfile() {
        String url_link = Apis.profileInfo;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");
                    JSONObject jsonObjectProfileInfo = jsonObjectData.getJSONObject("personalInfo");
                    String user_name = jsonObjectProfileInfo.getString("user_name");
                    String user_phone = jsonObjectProfileInfo.getString("user_phone");
                    String user_profile_info = jsonObjectProfileInfo.getString("user_profile_info");
                    String user_regdate = jsonObjectProfileInfo.getString("user_regdate");
                    String credential_username = jsonObjectProfileInfo.getString("credential_username");
                    String credential_email = jsonObjectProfileInfo.getString("credential_email");
                    String user_dob = jsonObjectProfileInfo.getString("user_dob");
                    String user_address1 = jsonObjectProfileInfo.getString("user_address1");
                    String user_address2 = jsonObjectProfileInfo.getString("user_address2");
                    String user_city = jsonObjectProfileInfo.getString("user_city");
                    String user_referral_code = jsonObjectProfileInfo.getString("user_referral_code");
                    String user_order_tracking_url = jsonObjectProfileInfo.getString("user_order_tracking_url");
                    String user_img_updated_on = jsonObjectProfileInfo.getString("user_img_updated_on");
                    String country_name = jsonObjectProfileInfo.getString("country_name");
                    String state_name = jsonObjectProfileInfo.getString("state_name");
                    String userImage = jsonObjectProfileInfo.getString("userImage");

                   // private TextView PrivacyPolicy, Edit, Bank, Faq;
                    collapsingToolbar.setTitle(user_name);
                    user_Name.setText(user_name);
                    user_Email.setText(credential_email);
                    Name.setText(credential_username);
                    Email.setText(credential_email);
                    Address.setText(user_address1+" - "+user_address2+" - "+user_city+" - "+state_name+" - "+country_name);
                    Reffer.setText("Refer to friends\n Referral code: "+user_referral_code);
                    if(user_phone.isEmpty()){
                        Mobile.setText("xx-xxxxxxxxxx(update with edit option)");
                    }else {
                        Mobile.setText(user_phone);
                    }

                    if(user_dob.isEmpty()){
                        dob.setText("0000-00-00 (update with edit option)");
                    }else {
                        dob.setText(user_dob);
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.privacyPolicytext:
                String url = "http://www.ewheelers.in/cms/view/3/1";
                showPrivacyPolicies(url);
                break;
            case R.id.editProfile:
                Intent i = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(i);
                break;
        }
    }
    private void showPrivacyPolicies(String url) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        WebView webView = new WebView(getActivity());
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        dialog.setView(webView);
        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
