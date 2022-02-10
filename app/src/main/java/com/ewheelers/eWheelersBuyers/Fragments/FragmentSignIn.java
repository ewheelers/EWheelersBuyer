package com.ewheelers.eWheelersBuyers.Fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.BroadcastReceiver.SmsReceiver;
import com.ewheelers.eWheelersBuyers.Interface.SmsListener;
import com.ewheelers.eWheelersBuyers.LoginActivity;
import com.ewheelers.eWheelersBuyers.NavAppBarActivity;
import com.ewheelers.eWheelersBuyers.Notifications.SharedPrefManager;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SessionStorage;
import com.ewheelers.eWheelersBuyers.Utilities.Constants;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.kinda.alert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.ewheelers.eWheelersBuyers.Dialogs.ShowAlerts.showfailedDialog;


public class FragmentSignIn extends Fragment implements View.OnClickListener {
    private AppCompatButton sign_in;
    private EditText username, password,otpPhone;
    InputMethodManager imm;
    TextView textViewterms,otpLoginTV,passwordLoginTV;
    KAlertDialog pDialog;
    ImageButton imageButtonmail, imageButtoncall;
    String msg;
    String fcmtoken;
    LinearLayout otp_login_ui;
    ScrollView password_login;
    AppCompatButton sendOTP;
    private int first = -100, second = -100, third = -100, fourth = -100;
    EditText et1, et2, et3, et4;
    SharedPreferences getPref;
    String oTP =null;
    AlertDialog dismissDailog;
    View listenerView;


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
        getPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


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

       /* FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("instancsmsg", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        fcmtoken = task.getResult().getToken();

                        // Log and toast
                        msg = getString(R.string.msg_token_fmt, fcmtoken);
                        Log.d("tokenFCM", msg);

                        //Toast.makeText(NavAppBarActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
*/
        passwordLoginTV = v.findViewById(R.id.passwordLoginTV);
        otpLoginTV = v.findViewById(R.id.otpLoginTV);
        password_login = v.findViewById(R.id.password_login);
        otp_login_ui = v.findViewById(R.id.otp_login_ui);

        otpPhone = v.findViewById(R.id.otpPhone);
        sendOTP = v.findViewById(R.id.sendOtp);
        passwordLoginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordLoginTV.setTextColor(getResources().getColor(R.color.colorPrimary));
                otpLoginTV.setTextColor(getResources().getColor(R.color.profileEditTextColor));
                password_login.setVisibility(View.VISIBLE);
                otp_login_ui.setVisibility(View.GONE);
            }
        });
        otpLoginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpLoginTV.setTextColor(getResources().getColor(R.color.colorPrimary));
                passwordLoginTV.setTextColor(getResources().getColor(R.color.profileEditTextColor));
                otp_login_ui.setVisibility(View.VISIBLE);
                password_login.setVisibility(View.GONE);
                otpPhone.setText("");
            }
        });
        sendOTP.setOnClickListener(this);

        return v;
    }

    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    private BroadcastReceiver smsBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("smsBroadcastReceiver", "onReceive");

            Bundle data  = intent.getExtras();
            Boolean b;
            Object[] pdus = (Object[]) data.get("pdus");
            for (int i=0; i<pdus.length;i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String sender = smsMessage.getDisplayOriginatingAddress();
                String messageBody = smsMessage.getMessageBody();
                b = messageBody.contains("eWheelers.in");
                String messageText = messageBody.replaceAll("[^0-9]", "");   // contains otp
                if (b == true) {
                    Log.d("isFrom...", "OTPSignin");
                    if ( messageText.length() == 4) {
                        et1.setText(String.valueOf(messageText.charAt(0)));
                        et2.setText(String.valueOf(messageText.charAt(1)));
                        et3.setText(String.valueOf(messageText.charAt(2)));
                        et4.setText(String.valueOf(messageText.charAt(3)));
                        hideProgressBarDialog(messageText);
                    }
                }
            }

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(smsBroadcastReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(smsBroadcastReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();

    }


    private void hideProgressBarDialog(String otppp) {
        try{
            if(dismissDailog != null){
                dismissDailog.dismiss();
                String firetoken = SharedPrefManager.getInstance(getActivity()).getDeviceToken();
                if (firetoken != null) {

                    signInBuyer(listenerView, firetoken,true,otppp,dismissDailog);
                } else {
                    dismissDailog.dismiss();
                    Toast.makeText(getActivity(), "Device Id Not Found !!! Please Try Later", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){

        }
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


                    String firetoken = SharedPrefManager.getInstance(getActivity()).getDeviceToken();

                    if (firetoken != null) {
                        signInBuyer(v, firetoken,false,"",null);
                    } else {
                        Toast.makeText(getActivity(), "Device Id Not Found !!! Please Try Later", Toast.LENGTH_SHORT).show();
                    }
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
            case R.id.sendOtp:
                if(otpPhone.getText().toString() != null && otpPhone.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Leaved Empty Field", Toast.LENGTH_SHORT).show();
                }else if(otpPhone.getText().toString().trim().length() < 10){
                    Toast.makeText(getActivity(), "Phone Number Not Valid", Toast.LENGTH_SHORT).show();
                }else{
                    singWithLoginOTP(v,false);
                }

        }
    }

    public void call_action() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "8886656700"));
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


    private void signInBuyer(final View v, String firetoken,boolean isFromOtpLogin,String optCode,AlertDialog alertDialog) {
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
                                //pDialog.dismiss();
                                JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                                String token = jsonObjectdata.getString("token");
                                String userName = jsonObjectdata.getString("user_name");
                                String userId = jsonObjectdata.getString("user_id");


                                sendRegistrationToServer(token, firetoken,alertDialog);
                                SessionStorage.saveString(getActivity(), SessionStorage.user_id, userId);
                                SessionStorage.saveString(getActivity(), SessionStorage.tokenvalue, token);
                                /*Intent intent = new Intent(getActivity(), NavAppBarActivity.class);
                                startActivity(intent);
                                getActivity().finish();*/

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
                String susername = "",spass="";
                Map<String, String> data3 = new HashMap<String, String>();
                if(isFromOtpLogin){
                    susername = otpPhone.getText().toString().trim();
                    spass = optCode;
                    data3.put("username", susername);
                    data3.put("password", spass);
                    data3.put("userType", String.valueOf(1));
                    data3.put("user_dial_code", "+91");
                }else{
                    susername = username.getText().toString().trim();
                    spass = password.getText().toString().trim();
                    data3.put("username", susername);
                    data3.put("password", spass);
                    data3.put("userType", String.valueOf(1));

                }
                 return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(strRequest);
    }

    private void singWithLoginOTP(final View v,boolean isFromResendOtp) {
        pDialog.show();
        sign_in.setBackgroundColor(Color.GRAY);
        sign_in.setEnabled(false);
        String Login_url = Apis.sendloginOtp;

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Constants.isFromOTPLogin = true;
                            Constants.isFromSignUp  = false;
                            JSONObject jsonObject = new JSONObject(response);
                            int getStatus = jsonObject.getInt("status");
                            String message = jsonObject.getString("msg");

                            if (getStatus == 1) {
                                if(!isFromResendOtp) {
                                    otpDialog(v, message);
                                }else{
                                    try{
                                        first = -100;
                                        second = -100;
                                        third = -100;
                                        fourth = -100;
                                        et1.setText("");
                                        et2.setText("");
                                        et3.setText("");
                                        et4.setText("");
                                        et1.requestFocus();
                                    }catch (Exception e){

                                    }

                                }
                                //pDialog.dismiss();
                                /*sendRegistrationToServer(token, firetoken);
                                SessionStorage.saveString(getActivity(), SessionStorage.user_id, userId);
                                SessionStorage.saveString(getActivity(), SessionStorage.tokenvalue, token);*/
                                /*Intent intent = new Intent(getActivity(), NavAppBarActivity.class);
                                startActivity(intent);
                                getActivity().finish();*/

                            } else {
                                pDialog.dismiss();
                                Log.d("error...",message);
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
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
                /* sign_in.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bg));
                sign_in.setEnabled(true);
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());*/
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("username", otpPhone.getText().toString().trim());
                data3.put("user_dial_code", "91");
                data3.put("user_country_iso", "in");
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        Log.d("Request...",strRequest.getBodyContentType());
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(strRequest);
    }

    private void sendRegistrationToServer(String token, String msg,AlertDialog alertDialog) {
        // TODO: Implement this method to send token to your app server.
        String Login_url = Apis.setuppushnotification;

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                if(alertDialog != null ){
                                    alertDialog.dismiss();
                                }
                                pDialog.dismiss();
                                Intent intent = new Intent(getActivity(), NavAppBarActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                //Toast.makeText(getActivity(), "fcmtoken: " + message, Toast.LENGTH_SHORT).show();
                                //sendNotification(message);
                            } else {
                                Toast.makeText(getActivity(), "FCM Token: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", token);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("deviceToken", msg);

                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(strRequest);
    }

    private void otpDialog(View v,String msg){
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.success_layout, viewGroup, false);
        TextView textView = dialogView.findViewById(R.id.message);
        TextView otpMsg = dialogView.findViewById(R.id.otpmsg);
        et1 = dialogView.findViewById(R.id.otpstr1);
        et2 = dialogView.findViewById(R.id.otpstr2);
        et3 = dialogView.findViewById(R.id.otpstr3);
        et4 = dialogView.findViewById(R.id.otpstr4);
        TextView countDown = dialogView.findViewById(R.id.countOtpSec);
        Button button = dialogView.findViewById(R.id.buttonOk);
        textView.setText(msg);
        first = -100;
        second = -100;
        third = -100;
        fourth = -100;
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et1.requestFocus();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
         AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        dismissDailog = alertDialog;
        listenerView = v;
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText("Please wait for " + millisUntilFinished / 1000 + " seconds to Resend-OTP");
            }

            public void onFinish() {
                countDown.setText("Resend-OTP");
                countDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        resendOTP(v,countDown,usedId);
                        singWithLoginOTP(v,true);
                    }
                });
            }

        }.start();
/*
        View.OnKeyListener key=new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(!((EditText) v).toString().isEmpty())
                    v.focusSearch(View.FOCUS_RIGHT).requestFocus();

                return false;
            }
        };
        otpstr1.setOnKeyListener(key);
        otpstr2.setOnKeyListener(key);
        otpstr3.setOnKeyListener(key);
*/
        et1.addTextChangedListener(new GenericTextWatcher(et1));
        et2.addTextChangedListener(new GenericTextWatcher(et2));
        et3.addTextChangedListener(new GenericTextWatcher(et3));
        et4.addTextChangedListener(new GenericTextWatcher(et4));

        otpMsg.setText("OTP sent to the Phone - Verify the OTP by entering 4 digit number");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first != -100 && second != -100 && third != -100 && fourth != -100){


                    String firetoken = SharedPrefManager.getInstance(getActivity()).getDeviceToken();
                    if (firetoken != null) {
                        String resultingOtp = String.valueOf(first) + String.valueOf(second) + String.valueOf(third) + String.valueOf(fourth);
                        signInBuyer(v, firetoken,true,resultingOtp,alertDialog);
                    } else {
                        alertDialog.dismiss();
                        Toast.makeText(getActivity(), "Device Id Not Found !!! Please Try Later", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    showfailedDialog(getActivity(), v, "Not entered 4 digit OTP code");
                }


                                      /*  Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();*/
            }
        });

    }
    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.otpstr1:
                    if (text.length() == 1) {
                        Log.d("first", text);
                        first = Integer.parseInt(text);
                        et2.requestFocus();
                    } else if (text.equals("") || text.length() == 0) {
                        first = -100;
                    }
                    break;
                case R.id.otpstr2:
                    if (text.length() == 1) {
                        Log.d("second", text);
                        second = Integer.parseInt(text);
                        et3.requestFocus();
                    } else if (text.equals("") || text.length() == 0) {
                        second = -100;
                    }
                    break;
                case R.id.otpstr3:
                    if (text.length() == 1) {
                        Log.d("third", text);
                        third = Integer.parseInt(text);
                        et4.requestFocus();
                    } else if (text.equals("") || text.length() == 0) {
                        third = -100;
                    }
                    break;
                case R.id.otpstr4:
                    if (text.length() == 1) {
                        Log.d("fourth", text);
                        fourth = Integer.parseInt(text);
                    } else if (text.equals("") || text.length() == 0) {
                        fourth = -100;
                        hideSoftKeyboard();
                    }
                    break;

            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
    public void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
