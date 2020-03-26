package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {
    ArrayList<String> countrieslist = new ArrayList<>();
    ArrayList<String> statelist = new ArrayList<>();
    Spinner countries,states;
    String tokenValue,countryString,stateString;
    Button update;
    EditText userprodservice,userprofileinfo,usercompany,usercity,userphonenumber,username;
    static EditText userdob;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        countries = findViewById(R.id.countries);
        states = findViewById(R.id.states);
        update = findViewById(R.id.upadate_btn);

        userdob = findViewById(R.id.userdob_update);
        userprodservice = findViewById(R.id.userprodservices_update);
        userprofileinfo = findViewById(R.id.userprofileinfo_update);
        usercompany = findViewById(R.id.usercompany_update);
        usercity = findViewById(R.id.usercity_update);
        userphonenumber = findViewById(R.id.userphone_update);
        username = findViewById(R.id.username_update);

        scrollView = findViewById(R.id.updatelayout);

        userdob.setOnClickListener(this);
        update.setOnClickListener(this);

        tokenValue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        getCountries();
        countries.setOnItemSelectedListener(this);

    }

    private void getCountries() {
        String url_link = Apis.getCountries;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("countries");
                    for(int i=0;i<jsonArrayCountries.length();i++) {
                        JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                        String countryid = jsonObjectcountry.getString("id");
                        String countryname = jsonObjectcountry.getString("name");
                        countrieslist.add(countryid+" - "+countryname);
                    }

                    countries.setAdapter(new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, countrieslist));


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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        countryString = countries.getSelectedItem().toString();
        getStatesNames(splitString(countryString));
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

     public String splitString(String str)
    {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i=0; i<str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if(Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }

        return String.valueOf(num);
    }

    private void getStatesNames(String splitString) {
        statelist.clear();
        String url_link = Apis.getStates+splitString;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("states");

                    for(int i=0;i<jsonArrayCountries.length();i++) {
                        JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                        String countryid = jsonObjectcountry.getString("id");
                        JSONObject countryname = jsonObjectcountry.getJSONObject("name");
                        String stateid = countryname.getString("state_id");
                        String state_code = countryname.getString("state_code");
                        String state_name = countryname.getString("state_name");

                        statelist.add(stateid+" - "+state_name);
                    }

                    states.setAdapter(new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, statelist));
                    states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            stateString = states.getSelectedItem().toString();
                            Toast.makeText(UpdateProfileActivity.this, splitString(stateString), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

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


    public void updateProfile(View v){
        String Login_url = Apis.UpdateProfileInfo;

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(getStatus.equals("1")) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                                Toast.makeText(UpdateProfileActivity.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {


                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("user_country_id", splitString(countryString));
                data3.put("user_state_id", splitString(stateString));
                data3.put("user_name", username.getText().toString());
                data3.put("user_phone", userphonenumber.getText().toString());
                data3.put("user_city_id", usercity.getText().toString());
                data3.put("user_company", usercompany.getText().toString());
                data3.put("user_profile_info", userprofileinfo.getText().toString());
                data3.put("user_products_services", userprodservice.getText().toString());
                data3.put("user_dob", userdob.getText().toString());

                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userdob_update:
                showTruitonDatePickerDialog(v);
                break;
            case R.id.upadate_btn:
                String userName = username.getText().toString();
                String mobile = userphonenumber.getText().toString();
                String city = usercity.getText().toString();
                String company = usercompany.getText().toString();
                String info = userprofileinfo.getText().toString();
                String servcie = userprodservice.getText().toString();
                String dob = userdob.getText().toString();
                if(userName.isEmpty()||mobile.isEmpty()||city.isEmpty()||company.isEmpty()||info.isEmpty()||servcie.isEmpty()||dob.isEmpty()){
                   /* Snackbar mySnackbar = Snackbar.make(v.findViewById(R.id.updatelayout), "Fill all details", Snackbar.LENGTH_SHORT).setTextColor(Color.YELLOW);
                    mySnackbar.show();*/
                   Toast.makeText(this, "Fill all details", Toast.LENGTH_SHORT).show();
                }else {
                    updateProfile(v);
                }
                break;
        }
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            userdob.setText(year + "-" + (month + 1) + "-" + day);
        }
    }
}
