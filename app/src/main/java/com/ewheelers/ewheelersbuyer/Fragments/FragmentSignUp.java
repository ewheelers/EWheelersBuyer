package com.ewheelers.ewheelersbuyer.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.kinda.alert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.ewheelers.ewheelersbuyer.Dialogs.ShowAlerts.showfailedDialog;


public class FragmentSignUp extends Fragment implements View.OnClickListener, TextWatcher {
    AppCompatButton sign_up;
    EditText fullname, username, email, password, confirmpassword;
    CheckBox agree, newsupdate;
    InputMethodManager imm;
    TextView termsconditions;
    KAlertDialog pDialog;
    TextView textView_strength;

    public FragmentSignUp() {
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
        View v = inflater.inflate(R.layout.fragment_fragment_sign_up, container, false);
        sign_up = v.findViewById(R.id.signup);
        fullname = v.findViewById(R.id.fullname);
        username = v.findViewById(R.id.username);
        email = v.findViewById(R.id.email);
        password = v.findViewById(R.id.password);
        confirmpassword = v.findViewById(R.id.confirm_password);
        newsupdate = v.findViewById(R.id.news);
        agree = v.findViewById(R.id.agree_conditions);
        termsconditions = v.findViewById(R.id.terms);
        textView_strength = v.findViewById(R.id.strength);

        password.addTextChangedListener(this);

        termsconditions.setOnClickListener(this);
        sign_up.setOnClickListener(this);

        pDialog = new KAlertDialog(getActivity(), KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#9C3C34"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        try {
            imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String full_name = fullname.getText().toString().trim();
                    String user_name = username.getText().toString().trim();
                    String emailid = email.getText().toString().trim();
                    String passwords = password.getText().toString().trim();
                    String confirmPassword = confirmpassword.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (full_name.isEmpty() || user_name.isEmpty() || emailid.isEmpty() || passwords.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(getActivity(), "Leaved Empty Field", Toast.LENGTH_SHORT).show();
                        agree.setChecked(false);
                    } else if (!emailid.matches(emailPattern)) {
                        email.setError("Enter valid Email Id");
                        agree.setChecked(false);
                    } else if (!isValidPassword(passwords.trim())) {
                        agree.setChecked(false);
                        password.setError("Password should have atleast 6 letters with a combination of small & Capital letter, special chars (@#$%^&+=) and numbers");
                    } else if (!passwords.matches(confirmPassword)) {
                        agree.setChecked(false);
                        confirmpassword.setError("Confirm password is not match with given Password");
                    } else {
                        agree.setChecked(true);
                        sign_up.setVisibility(View.VISIBLE);
                    }
                } else {
                    sign_up.setVisibility(View.GONE);
                }
            }
        });

        return v;
    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.terms:
                String url = "http://www.ewheelers.in/cms/view/3/1";
                showPrivacyPolicies(url);
                break;
            case R.id.signup:
                try {
                    imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                signUpBuyer(v);
                break;
        }
    }

    private void signUpBuyer(final View v) {
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Apis.register;
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int getStatus = Integer.parseInt(jsonObject.getString("status"));
                            if (getStatus != 0) {
                                pDialog.dismiss();
                                sign_up.setVisibility(View.GONE);
                                int status = jsonObject.getInt("status");
                                String smsg = jsonObject.getString("msg");
                                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.success_layout, viewGroup, false);
                                TextView textView = dialogView.findViewById(R.id.message);
                                Button button = dialogView.findViewById(R.id.buttonOk);
                                textView.setText(smsg);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setView(dialogView);
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                        fullname.setText(null);
                                        username.setText(null);
                                        email.setText(null);
                                        password.setText(null);
                                        confirmpassword.setText(null);
                                        newsupdate.setChecked(false);
                                        agree.setChecked(false);

                                      /*  Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();*/
                                    }
                                });

                                //showSuccessDialog(getActivity(), v, smsg);
                            } else {
                                pDialog.dismiss();
                                sign_up.setVisibility(View.VISIBLE);
                                String smsg = jsonObject.getString("msg");
                                showfailedDialog(getActivity(), v, smsg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        sign_up.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {

                String full_name = fullname.getText().toString().trim();
                String user_name = username.getText().toString().trim();
                String emailid = email.getText().toString().trim();
                String passwords = password.getText().toString().trim();
                String confirmPassword = confirmpassword.getText().toString().trim();
                String schec = String.valueOf(agree.isChecked());
                String snews = String.valueOf(newsupdate.isChecked());

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("user_name", full_name);
                data3.put("user_username", user_name);
                data3.put("user_email", emailid);
                data3.put("user_password", passwords);
                data3.put("password1", confirmPassword);
                data3.put("agree", schec);
                data3.put("user_newsletter_signup", String.valueOf(0));

                return data3;

            }
        };

        queue.add(strRequest);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(s.length()==1){
            password.setError("Password should have atleast 6 letters with a combination of small & Capital letter, special chars (@#$%^&+=) and numbers");
        }

        if(s.length()<4){
            textView_strength.setVisibility(View.GONE);

        }else {
            if(isValidPassword(s.toString())) {
                textView_strength.setVisibility(View.VISIBLE);
            }else {
                password.setError("Password should have atleast 6 letters with a combination of small & Capital letter, special chars (@#$%^&+=) and numbers");
            }

            if (s.length() == 4) {
                textView_strength.setTextColor(Color.DKGRAY);
                textView_strength.setText("WEAK Password");
            } else if (s.length() < 6) {
                textView_strength.setTextColor(Color.RED);
                textView_strength.setText("EASY Password");
            } else if (s.length() < 8) {
                textView_strength.setTextColor(Color.BLUE);
                textView_strength.setText("MEDIUM Password");
            } else if (s.length() < 10) {
                textView_strength.setTextColor(Color.GREEN);
                textView_strength.setText("STRONG Password");
            }else {
                textView_strength.setTextColor(Color.GREEN);
                textView_strength.setText("STRONGEST Password");
            }

        }

        if(s.length()==15){
            textView_strength.setTextColor(Color.BLACK);
            textView_strength.setText("Password Max Length Reached");
        }




    }
}
