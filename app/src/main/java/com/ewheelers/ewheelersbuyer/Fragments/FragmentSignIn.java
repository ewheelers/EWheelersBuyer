package com.ewheelers.ewheelersbuyer.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.ewheelersbuyer.BottomedNavigationActivity;
import com.ewheelers.ewheelersbuyer.NavAppBarActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.kinda.alert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.ewheelers.ewheelersbuyer.Dialogs.ShowAlerts.showfailedDialog;


public class FragmentSignIn extends Fragment implements View.OnClickListener {
    private AppCompatButton sign_in;
    private EditText username, password;
    InputMethodManager imm;
    TextView textViewterms;
    KAlertDialog pDialog;
    ImageButton imageButtonmail, imageButtoncall;

    public FragmentSignIn() {
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
        View v = inflater.inflate(R.layout.fragment_fragment_sign_in, container, false);

        username = v.findViewById(R.id.username);
        password = v.findViewById(R.id.password);
        sign_in = v.findViewById(R.id.signin);
        textViewterms = v.findViewById(R.id.terms);
        imageButtonmail = v.findViewById(R.id.infomailid);
        imageButtoncall = v.findViewById(R.id.helpline_call);
        imageButtonmail.setOnClickListener(this);
        imageButtoncall.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        textViewterms.setOnClickListener(this);
      /*  try {
            imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        pDialog = new KAlertDialog(getActivity(), KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);


        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin:
                try {
                    imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Leaved Empty Field", Toast.LENGTH_SHORT).show();
                } else {
                    signInBuyer(v);
                }
                break;
            case R.id.terms:
                String url = "http://www.ewheelers.in/cms/view/3/1";
                showPrivacyPolicies(url);
                break;
            case R.id.infomailid:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@ewheelers.in"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Query");
                //email.putExtra(Intent.EXTRA_TEXT, "");

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;
            case R.id.helpline_call:
                if (isPermissionGranted()) {
                    call_action();
                }
                break;
        }
    }

    public void call_action() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "9010500076"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        startActivity(callIntent);
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
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


    private void signInBuyer(final View v) {
        pDialog.show();
        sign_in.setBackgroundColor(Color.GRAY);
        sign_in.setEnabled(false);
        String Login_url = Apis.login;

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int getStatus = jsonObject.getInt("status");
                            String message = jsonObject.getString("msg");

                            if (getStatus == 1) {
                                pDialog.dismiss();
                                JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                                String token = jsonObjectdata.getString("token");
                                String userName = jsonObjectdata.getString("user_name");
                                String userId = jsonObjectdata.getString("user_id");
                                SessionStorage.saveString(getActivity(), SessionStorage.tokenvalue, token);
                                Intent intent = new Intent(getActivity(), NavAppBarActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            } else {
                                pDialog.dismiss();
                                sign_in.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bg));
                                sign_in.setEnabled(true);
                                showfailedDialog(getActivity(), v, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                sign_in.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bg));
                sign_in.setEnabled(true);
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getParams() {
                String susername = username.getText().toString();
                String spass = password.getText().toString();

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("username", susername);
                data3.put("password", spass);
                data3.put("userType", String.valueOf(1));

                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(strRequest);
    }

}
