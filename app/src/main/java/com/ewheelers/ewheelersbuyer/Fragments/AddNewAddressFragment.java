package com.ewheelers.ewheelersbuyer.Fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.ewheelers.ewheelersbuyer.MapsActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.SetupBillingAddressActivity;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddNewAddressFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, OnMapReadyCallback {
    EditText address_label, name, address_line1, address_line2, postalcode, phone;
    Spinner country, state, city;
    Button savechanges;
    ArrayList<String> countrieslist = new ArrayList<>();
    ArrayList<String> statelist = new ArrayList<>();
    ArrayList<String> citieslist = new ArrayList<>();
    String tokenValue, countryString, stateString, cityString;
    public static LinearLayout linearLayout;

    EditText autocompletetxt,lat,lang;

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GoogleMap.OnCameraIdleListener onCameraIdleListener;

    public AddNewAddressFragment() {
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
        View v = inflater.inflate(R.layout.fragment_add_new_address, container, false);

        tokenValue = new SessionStorage().getStrings(getActivity(), SessionStorage.tokenvalue);

        linearLayout = v.findViewById(R.id.add_address);

        autocompletetxt = v.findViewById(R.id.autocomplete);
        lang = v.findViewById(R.id.longitude);
        lat = v.findViewById(R.id.latitude);

        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        configureCameraIdle();


        address_label = v.findViewById(R.id.office_address);
        name = v.findViewById(R.id.name);
        address_line1 = v.findViewById(R.id.addressline1);
        address_line2 = v.findViewById(R.id.addressline2);

        country = v.findViewById(R.id.country);
        state = v.findViewById(R.id.state);
        city = v.findViewById(R.id.city);

        savechanges = v.findViewById(R.id.save);
        postalcode = v.findViewById(R.id.postalCode);
        phone = v.findViewById(R.id.phone);
        country.setOnItemSelectedListener(this);
        state.setOnItemSelectedListener(this);
        savechanges.setOnClickListener(this);

        getCountries();


        return v;
    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(getActivity());
                lat.setText(String.valueOf(latLng.latitude));
                lang.setText(String.valueOf(latLng.longitude));
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        String zip = addressList.get(0).getPostalCode();

                        if (!locality.isEmpty() && !country.isEmpty())
                            autocompletetxt.setText(locality + "  " + country);
                            postalcode.setText(zip);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                String addressLabel = address_label.getText().toString();
                String names = name.getText().toString();
                String addressLine1 = address_line1.getText().toString();
                String addressLine2 = address_line2.getText().toString();
                String postalCode = postalcode.getText().toString();
                String mobile = phone.getText().toString();
                if (names.isEmpty() || addressLine1.isEmpty() || postalCode.isEmpty() || mobile.isEmpty()) {
                    Toast.makeText(getActivity(), "Fill all details", Toast.LENGTH_SHORT).show();
                } else if (state.getSelectedItem() == null) {
                    Toast.makeText(getActivity(), "Select State", Toast.LENGTH_SHORT).show();
                }
                else if(autocompletetxt.getText().toString().isEmpty()){
                    autocompletetxt.setError("Need complete address");
                }else if(lat.getText().toString().isEmpty()||lat.getText().toString().equals("0.0")||lang.getText().toString().isEmpty()||lang.getText().toString().equals("0.0")){
                    Toast.makeText(getActivity(), "Set Latitude and Longitude using Map", Toast.LENGTH_SHORT).show();
                }
               /* else if (city.getSelectedItem() == null) {
                    Toast.makeText(this, "Select city", Toast.LENGTH_SHORT).show();
                }*/
                else {
                    saveChanges("new");
                }
                break;
        }
    }

    private void getCountries() {
        String url_link = Apis.getCountries;
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

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("countries");
                    for (int i = 0; i < jsonArrayCountries.length(); i++) {
                        JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                        String countryid = jsonObjectcountry.getString("id");
                        String countryname = jsonObjectcountry.getString("name");
                        countrieslist.add(countryid + " - " + countryname);
                    }

                    country.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countrieslist));


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

    private void getStatesNames(String splitString) {
        statelist.clear();
        String url_link = Apis.getStates + splitString;
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

                    state.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, statelist));
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
        Log.i("cityurl:", url_link);
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

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("cities");
                    if (jsonArrayCountries != null) {
                        for (int i = 0; i < jsonArrayCountries.length(); i++) {
                            JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                            String cityid = jsonObjectcountry.getString("id");
                            String cityname = jsonObjectcountry.getString("name");

                            citieslist.add(cityid + " - " + cityname);
                        }
                        city.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, citieslist));
                        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cityString = city.getSelectedItem().toString();
                                Toast.makeText(getActivity(), splitStringAlpha(cityString), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "No cities are added to show", Toast.LENGTH_SHORT).show();
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

    public void saveChanges(String update) {
        String Login_url = Apis.setupAddress;

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //Log.i("updtaeresponse", response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                               /* Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();*/
                            } else {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                               /* Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();*/
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

                Log.i("citystring",splitStringAlpha(cityString));

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("ua_identifier", addressLabel);
                data3.put("ua_name", names);
                data3.put("ua_is_charging_station", "0");
                data3.put("ua_auto_complete", autocompletetxt.getText().toString());
                data3.put("ua_address1", addressLine1);
                data3.put("ua_address2", addressLine2);
                data3.put("ua_country_id", splitString(countryString));
                data3.put("ua_state_id", splitString(stateString));
                data3.put("ua_city_id", splitString(cityString));
                data3.put("ua_city", splitStringAlpha(cityString));
                data3.put("ua_zip", postalCode);
                data3.put("ua_phone", mobile);
                data3.put("ua_latitude", lat.getText().toString());
                data3.put("ua_longitude", lang.getText().toString());
                if (update.equals("new")) {
                    data3.put("ua_id", "");
                } else {
                    data3.put("ua_id", update);
                }

                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(strRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(14.0f);
        mMap.setMyLocationEnabled(true);
    }
}
