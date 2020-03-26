package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.ewheelers.ewheelersbuyer.Adapters.BillingAddressesAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.BillingAddress;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupBillingAddressActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    //6
    EditText address_label, name, address_line1, address_line2, postalcode, phone;
    Spinner country, state, city;
    Button savechanges, cancel;
    ArrayList<String> countrieslist = new ArrayList<>();
    ArrayList<String> statelist = new ArrayList<>();
    ArrayList<String> citieslist = new ArrayList<>();
    String tokenValue, countryString, stateString, cityString;
    RecyclerView recyclerView;
    BillingAddressesAdapter billingAddressesAdapter;
    List<BillingAddress> billingAddressList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_billing_address);
        // address_label, name, address_line1, address_line2, postalcode, phone
        recyclerView = findViewById(R.id.addresses_list);
        address_label = findViewById(R.id.office_address);
        name = findViewById(R.id.name);
        address_line1 = findViewById(R.id.addressline1);
        address_line2 = findViewById(R.id.addressline2);
        postalcode = findViewById(R.id.postalCode);
        phone = findViewById(R.id.phone);

        country = findViewById(R.id.country);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);

        savechanges = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);

        country.setOnItemSelectedListener(this);
        state.setOnItemSelectedListener(this);
        cancel.setOnClickListener(this);
        savechanges.setOnClickListener(this);

        tokenValue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        getCountries();

        getAddresses();

    }

    private void getAddresses() {
        String url_link = Apis.getaddresses;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = jsonObjectData.getJSONArray("addresses");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject address = jsonArray.getJSONObject(i);
                        String uaid = address.getString("ua_id");
                        String uauserid = address.getString("ua_user_id");
                        String uaidentifier = address.getString("ua_identifier");
                        String uaname = address.getString("ua_name");
                        String address1 = address.getString("ua_address1");
                        String address2 = address.getString("ua_address2");
                        String city = address.getString("ua_city");
                        String cityid = address.getString("ua_city_id");
                        String stateid = address.getString("ua_state_id");
                        String contryid = address.getString("ua_country_id");
                        String phone = address.getString("ua_phone");
                        String zip = address.getString("ua_zip");
                        String uadefault = address.getString("ua_is_default");
                        String deleted = address.getString("ua_deleted");
                        String statecode = address.getString("state_code");
                        String countrycode = address.getString("country_code");
                        String countryname = address.getString("country_name");
                        String statename = address.getString("state_name");
                        String cityname = address.getString("city_name");
                        String shippingAddress = address.getString("isShippingAddress");
                        BillingAddress billingAddress = new BillingAddress();
                        billingAddress.setUa_id(uaid);
                        billingAddress.setUa_identifier(uaidentifier);
                        billingAddress.setUa_name(uaname);
                        billingAddress.setUa_address1(address1);
                        billingAddress.setUa_address2(address2);
                        billingAddress.setCity_name(city);
                        billingAddress.setState_name(statename);
                        billingAddress.setCountry_name(countryname);
                        billingAddress.setUa_zip(zip);
                        billingAddress.setUa_phone(phone);
                        billingAddressList.add(billingAddress);

                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SetupBillingAddressActivity.this,RecyclerView.HORIZONTAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    billingAddressesAdapter = new BillingAddressesAdapter(SetupBillingAddressActivity.this,billingAddressList);
                    recyclerView.setAdapter(billingAddressesAdapter);
                    billingAddressesAdapter.notifyDataSetChanged();

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
        switch (v.getId()) {
            case R.id.cancel:

                break;
            case R.id.save:
                String addressLabel = address_label.getText().toString();
                String names = name.getText().toString();
                String addressLine1 = address_line1.getText().toString();
                String addressLine2 = address_line2.getText().toString();
                String postalCode = postalcode.getText().toString();
                String mobile = phone.getText().toString();
                if (addressLabel.isEmpty() || names.isEmpty() || addressLine1.isEmpty() || addressLine2.isEmpty() || postalCode.isEmpty() || mobile.isEmpty()) {
                    Toast.makeText(this, "Fill all details", Toast.LENGTH_SHORT).show();
                } else if (state.getSelectedItem() == null) {
                    Toast.makeText(this, "Select State", Toast.LENGTH_SHORT).show();
                } else if (city.getSelectedItem() == null) {
                    Toast.makeText(this, "Select city", Toast.LENGTH_SHORT).show();
                } else {
                    saveChanges();
                }
                break;
        }
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
                    for (int i = 0; i < jsonArrayCountries.length(); i++) {
                        JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                        String countryid = jsonObjectcountry.getString("id");
                        String countryname = jsonObjectcountry.getString("name");
                        countrieslist.add(countryid + " - " + countryname);
                    }

                    country.setAdapter(new ArrayAdapter<String>(SetupBillingAddressActivity.this, android.R.layout.simple_spinner_dropdown_item, countrieslist));


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
        countryString = country.getSelectedItem().toString();
        getStatesNames(splitString(countryString));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String splitString(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }
        return String.valueOf(num);
    }

    public String splitStringAlpha(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }
        return String.valueOf(alpha);
    }

    private void getStatesNames(String splitString) {
        statelist.clear();
        String url_link = Apis.getStates + splitString;
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

                    for (int i = 0; i < jsonArrayCountries.length(); i++) {
                        JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                        String countryid = jsonObjectcountry.getString("id");
                        JSONObject countryname = jsonObjectcountry.getJSONObject("name");
                        String stateid = countryname.getString("state_id");
                        String state_code = countryname.getString("state_code");
                        String state_name = countryname.getString("state_name");

                        statelist.add(stateid + " - " + state_name);
                    }

                    state.setAdapter(new ArrayAdapter<String>(SetupBillingAddressActivity.this, android.R.layout.simple_spinner_dropdown_item, statelist));
                    state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            stateString = state.getSelectedItem().toString();
                            //Toast.makeText(SetupBillingAddressActivity.this, splitString(stateString), Toast.LENGTH_SHORT).show();
                            getCityNames(splitString(countryString), splitString(stateString));

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

    private void getCityNames(String countryid, String stateid) {
        citieslist.clear();
        String url_link = Apis.getcities + countryid + "/" + stateid;
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

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("cities");

                    for (int i = 0; i < jsonArrayCountries.length(); i++) {
                        JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                        String cityid = jsonObjectcountry.getString("id");
                        String cityname = jsonObjectcountry.getString("name");

                        citieslist.add(cityid + " - " + cityname);

                    }

                    city.setAdapter(new ArrayAdapter<String>(SetupBillingAddressActivity.this, android.R.layout.simple_spinner_dropdown_item, citieslist));
                    city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cityString = city.getSelectedItem().toString();
                            // Toast.makeText(SetupBillingAddressActivity.this, splitString(cityString), Toast.LENGTH_SHORT).show();

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

    private void saveChanges() {
        String Login_url = Apis.setupAddress;

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                            } else {
                                Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
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
                String addressLabel = address_label.getText().toString();
                String names = name.getText().toString();
                String addressLine1 = address_line1.getText().toString();
                String addressLine2 = address_line2.getText().toString();
                String postalCode = postalcode.getText().toString();
                String mobile = phone.getText().toString();

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("ua_state_id", splitString(stateString));
                data3.put("ua_identifier", addressLabel);
                data3.put("ua_name", names);
                data3.put("ua_address1", addressLine1);
                data3.put("ua_address2", addressLine2);
                data3.put("ua_country_id", splitString(countryString));
                data3.put("ua_city", splitStringAlpha(cityString));
                data3.put("ua_zip", postalCode);
                data3.put("ua_phone", mobile);
                data3.put("ua_id", "");
                data3.put("ua_city_id", "-1");

                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

}
