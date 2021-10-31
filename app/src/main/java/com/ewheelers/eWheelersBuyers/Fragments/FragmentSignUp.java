package com.ewheelers.eWheelersBuyers.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.LoginActivity;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.kinda.alert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.ewheelers.eWheelersBuyers.Dialogs.ShowAlerts.showfailedDialog;


public class FragmentSignUp extends Fragment implements View.OnClickListener, TextWatcher {
    AppCompatButton sign_up;
    EditText fullname, lastname, username, email, password, confirmpassword, phone;
    CheckBox agree, newsupdate;
    InputMethodManager imm;
    TextView termsconditions;
    KAlertDialog pDialog;
    TextView textView_strength;
    RadioGroup radioGroupbtn;
    RadioButton radioOtpbtn;
    String otpSelect;

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
        radioGroupbtn = v.findViewById(R.id.radioGroup);
        sign_up = v.findViewById(R.id.signup);
        fullname = v.findViewById(R.id.fullname);
        lastname = v.findViewById(R.id.lastname);
        phone = v.findViewById(R.id.phone);
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

      /*  try {
            imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        radioGroupbtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedId = radioGroupbtn.getCheckedRadioButtonId();
                radioOtpbtn = v.findViewById(checkedId);
                if (checkedId == R.id.radioPhone) {
                    otpSelect = "2";
                } else if (checkedId == R.id.radioEmail) {
                    otpSelect = "1";
                }
                //Toast.makeText(getActivity(),radioOtpbtn.getText(),Toast.LENGTH_SHORT).show();
            }
        });
        agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String full_name = fullname.getText().toString().trim();
                    String last_name = lastname.getText().toString().trim();
                    String phone_no = phone.getText().toString().trim();
                    String user_name = username.getText().toString().trim();
                    String emailid = email.getText().toString().trim();
                    String passwords = password.getText().toString().trim();
                    String confirmPassword = confirmpassword.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (full_name.isEmpty() || last_name.isEmpty() || phone_no.isEmpty() || user_name.isEmpty() || emailid.isEmpty() || passwords.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(getActivity(), "Leaved Empty Field", Toast.LENGTH_SHORT).show();
                        agree.setChecked(false);
                    } else if (phone_no.length() < 10) {
                        phone.setError("Enter correct mobile number");
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
                    } else if (radioGroupbtn.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getActivity(), "Select Phone or Email to receive OTP", Toast.LENGTH_SHORT).show();
                        agree.setChecked(false);
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
                String url = "http://ewheelers.in/cms/view/2";
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
                            if (response.startsWith("{")) {
                                JSONObject jsonObject = new JSONObject(response);
                                int getStatus = Integer.parseInt(jsonObject.getString("status"));
                                if (getStatus != 0) {
                                    pDialog.dismiss();
                                    sign_up.setVisibility(View.GONE);
                                    int status = jsonObject.getInt("status");
                                    String smsg = jsonObject.getString("msg");
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    String usedId = jsonObject1.getString("user_id");
                                    ViewGroup viewGroup = v.findViewById(android.R.id.content);
                                    View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.success_layout, viewGroup, false);
                                    TextView textView = dialogView.findViewById(R.id.message);
                                    TextView otpMsg = dialogView.findViewById(R.id.otpmsg);
                                    EditText otpstr1 = dialogView.findViewById(R.id.otpstr1);
                                    EditText otpstr2 = dialogView.findViewById(R.id.otpstr2);
                                    EditText otpstr3 = dialogView.findViewById(R.id.otpstr3);
                                    EditText otpstr4 = dialogView.findViewById(R.id.otpstr4);
                                    TextView countDown = dialogView.findViewById(R.id.countOtpSec);
                                    Button button = dialogView.findViewById(R.id.buttonOk);
                                    textView.setText(smsg);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setView(dialogView);
                                    final AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                    alertDialog.setCancelable(false);
                                    new CountDownTimer(30000, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                            countDown.setText("Please wait for " + millisUntilFinished / 1000 + " seconds to Resend-OTP");
                                        }

                                        public void onFinish() {
                                            countDown.setText("Resend-OTP");
                                            countDown.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    resendOTP(v,countDown,usedId);
                                                }
                                            });
                                        }

                                    }.start();
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
                                    if (otpSelect.equals("2")) {
                                        otpMsg.setText("OTP sent to the Phone - Verify the OTP by entering 4 digit number");
                                    } else if (otpSelect.equals("1")) {
                                        otpMsg.setText("OTP sent to the Email - Verify the OTP by entering 4 digit number");
                                    }

                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (otpstr1.getText().toString().isEmpty() || otpstr2.getText().toString().isEmpty() || otpstr3.getText().toString().isEmpty() || otpstr4.getText().toString().isEmpty()) {
                                                showfailedDialog(getActivity(), v, "Not entered 4 digit OTP code");

                                            } else {
                                                otpValidation(alertDialog,v,usedId, otpstr1.getText().toString(), otpstr2.getText().toString(), otpstr3.getText().toString(), otpstr4.getText().toString(), otpSelect);
                                            }
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
                            } else {
                                pDialog.dismiss();
                                sign_up.setVisibility(View.VISIBLE);
                                showfailedDialog(getActivity(), v, response);
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
            protected Map<String, String> getParams() {

                String full_name = fullname.getText().toString().trim();
                String last_name = lastname.getText().toString().trim();
                String phone_number = phone.getText().toString().trim();
                String user_name = username.getText().toString().trim();
                String emailid = email.getText().toString().trim();
                String passwords = password.getText().toString().trim();
                String confirmPassword = confirmpassword.getText().toString().trim();
                String schec = String.valueOf(agree.isChecked());
                String snews = String.valueOf(newsupdate.isChecked());

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("user_name", full_name);
                data3.put("user_last_name", last_name);
                data3.put("user_username", user_name);
                data3.put("user_email", emailid);
                data3.put("user_phone", phone_number);
                data3.put("user_password", passwords);
                data3.put("password1", confirmPassword);
                data3.put("user_dial_code", "+91");
                data3.put("user_country_iso", "in");
                data3.put("sendOtpOn", otpSelect);
                data3.put("agree", schec);
                data3.put("user_newsletter_signup", String.valueOf(0));

                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(strRequest);
    }

    private void resendOTP(View v, TextView countid, String usedId) {
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Apis.resendotp + usedId;
        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.startsWith("{")) {
                                JSONObject jsonObject2 = new JSONObject(response);
                                int getStatus = Integer.parseInt(jsonObject2.getString("status"));
                                if (getStatus != 0) {
                                    pDialog.dismiss();
                                    String smsg = jsonObject2.getString("msg");
                                    new CountDownTimer(30000, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                            countid.setText("Please wait for " + millisUntilFinished / 1000 + " seconds to Resend-OTP");
                                        }

                                        public void onFinish() {
                                            countid.setText("Resend-OTP");
                                            countid.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    resendOTP(v, countid, usedId);
                                                }
                                            });
                                        }

                                    }.start();
                                }else {
                                    pDialog.dismiss();
                                    String smsg = jsonObject2.getString("msg");
                                    showfailedDialog(getActivity(), v, smsg);
                                }
                            } else {
                                pDialog.dismiss();
                                //sign_up.setVisibility(View.VISIBLE);
                                showfailedDialog(getActivity(), v, response);
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
                        //sign_up.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override

            protected Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(strRequest);
    }

    private void otpValidation(AlertDialog alertDialog, View v, String userId, String otpstr1, String otpstr2, String otpstr3, String otpstr4, String otpSelect) {
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Apis.validateOtp;
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.startsWith("{")) {
                                JSONObject jsonObject2 = new JSONObject(response);
                                int getStatus = Integer.parseInt(jsonObject2.getString("status"));
                                if (getStatus != 0) {
                                    pDialog.dismiss();
                                    String smsg = jsonObject2.getString("msg");
                                    Toast.makeText(getActivity(), smsg, Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    fullname.setText(null);
                                    lastname.setText(null);
                                    phone.setText(null);
                                    username.setText(null);
                                    email.setText(null);
                                    password.setText(null);
                                    confirmpassword.setText(null);
                                    radioGroupbtn.clearCheck();
                                    newsupdate.setChecked(false);
                                    agree.setChecked(false);
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }else {
                                    pDialog.dismiss();
                                    String smsg = jsonObject2.getString("msg");
                                    showfailedDialog(getActivity(), v, smsg);
                                }
                            } else {
                                pDialog.dismiss();
                                //sign_up.setVisibility(View.VISIBLE);
                                showfailedDialog(getActivity(), v, response);
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
                        //sign_up.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override

            protected Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("uo_otp",(otpstr1+otpstr2+otpstr3+otpstr4));
                data3.put("user_id", String.valueOf(userId));
                data3.put("sendOtpOn", String.valueOf(otpSelect));
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
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

        if (s.length() == 1) {
            password.setError("Password should have atleast 8 letters with a combination of small & Capital letter, special chars (@#$%^&+=) and numbers");
        }

        if (s.length() < 8) {
            textView_strength.setVisibility(View.GONE);

        } else {
            if (isValidPassword(s.toString())) {
                textView_strength.setVisibility(View.VISIBLE);
            } else {
                password.setError("Password should have atleast 8 letters with a combination of small & Capital letter, special chars (@#$%^&+=) and numbers");
            }

//            if (s.length() == 4) {
//                textView_strength.setTextColor(Color.DKGRAY);
//                textView_strength.setText("WEAK Password");
//            }
            if (s.length() < 8) {
                textView_strength.setTextColor(Color.RED);
                textView_strength.setText("EASY Password");
            } else if (s.length() > 8 && s.length() < 10) {
                textView_strength.setTextColor(Color.BLUE);
                textView_strength.setText("MEDIUM Password");
            } else if (s.length() > 10 && s.length() < 12) {
                textView_strength.setTextColor(Color.GREEN);
                textView_strength.setText("STRONG Password");
            } else {
                textView_strength.setTextColor(Color.GREEN);
                textView_strength.setText("STRONGEST Password");
            }

        }

        if (s.length() == 15) {
            textView_strength.setTextColor(Color.BLACK);
            textView_strength.setText("Password Max Length Reached");
        }


    }
}
