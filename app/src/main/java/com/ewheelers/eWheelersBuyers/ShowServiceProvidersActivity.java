package com.ewheelers.eWheelersBuyers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.CategoriesFilterAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.OtherServices;
import com.ewheelers.eWheelersBuyers.Adapters.SPProductsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.ServiceProvidersAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.SpinnerFiltersAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.AddonsClass;
import com.ewheelers.eWheelersBuyers.ModelClass.FilterListClass;
import com.ewheelers.eWheelersBuyers.ModelClass.HomeMenuIcons;
import com.ewheelers.eWheelersBuyers.ModelClass.ServiceProvidersClass;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.kinda.alert.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ewheelers.eWheelersBuyers.SessionStorage.tokenvalue;

public class ShowServiceProvidersActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    ServiceProvidersAdapter serviceProvidersAdapter;
    OtherServices otherServicesAdapter;
    List<ServiceProvidersClass> serviceProvidersClassesList = new ArrayList<>();
    String provider_is;
    String url = "";
    KAlertDialog pDialog;
    double latitude, longitude;
    String distance = "";
    boolean isLoading = false;
    String tokenValue;
    NewGPSTracker newgps;
    LinearLayout imageView, separate_orders;
    ImageView leftImg;
    TextView textView;
    //ProgressBar progressBar;
    SearchView searchView;
    //Chip chip;
    //HorizontalScrollView horizontalScrollView;
    RelativeLayout relativeLayout;
    Button scanQr;
    int count = 0;
    ImageView imageViewclose;
    RecyclerView recyclerViewchargeFilters;
    // Spinner type_spinner, charge_spinner;
    List<FilterListClass> filterListSpinner = new ArrayList<>();
    List<FilterListClass> filterListServicetype = new ArrayList<>();
    List<FilterListClass> filterList = new ArrayList<>();
    TextView empty_view;
    // LinearLayout linearLayout;
    Chip chipall;
    CategoriesFilterAdapter spinnerFiltersAdapter;
    RecyclerView spinnerlist;
    JSONObject jsonObjectServicetype;
    LinearLayout linearLayoutPasses;
    int pagination = 1;
    String scrollOption;
    JSONObject scrollJsonObjectType ;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_service_providers);
       /* linearLayout = findViewById(R.id.layoutspinner);
        type_spinner = findViewById(R.id.type_spinner);
        charge_spinner = findViewById(R.id.charge_spinner);*/
        linearLayoutPasses = findViewById(R.id.mypasses);
        linearLayoutPasses.setOnClickListener(this);
        separate_orders = findViewById(R.id.seperatorders);
        spinnerlist = findViewById(R.id.spinners_list);
        empty_view = findViewById(R.id.emptyView);
        chipall = findViewById(R.id.allchip);
        chipall.setChecked(true);
        chipall.setTextColor(Color.parseColor("#9C3C34"));
        chipall.setVisibility(View.VISIBLE);
        recyclerViewchargeFilters = findViewById(R.id.chargefilters);
        imageViewclose = findViewById(R.id.closereqbtn);
        scanQr = findViewById(R.id.reqtestdrive);
        recyclerView = findViewById(R.id.serviceProviders_list);
        relativeLayout = findViewById(R.id.clrelay);
        tokenValue = new SessionStorage().getStrings(this, tokenvalue);
        //horizontalScrollView = findViewById(R.id.chipgroup);
        //progressBar = findViewById(R.id.progress);
        imageView = findViewById(R.id.mapview);
        leftImg = findViewById(R.id.lefticon);
        textView = findViewById(R.id.titile);
        //chip = findViewById(R.id.chargeandgo);
        separate_orders.setOnClickListener(this);
        imageView.setOnClickListener(this);
        scanQr.setOnClickListener(this);
        imageViewclose.setOnClickListener(this);
        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(false);
        searchView = findViewById(R.id.search);
        provider_is = getIntent().getStringExtra("providerIs");
        relativeLayout.setOnClickListener(this);
        // getSupportActionBar().setTitle(provider_is + " Services");
        longitude = new NavAppBarActivity().setlongitude();
        latitude = new NavAppBarActivity().setlatitude();
        Log.d("latlongs...",""+longitude+".."+latitude);
        //Toast.makeText(ShowServiceProvidersActivity.this, "lat:"+latitude+" long:"+longitude, Toast.LENGTH_SHORT).show();
        chipall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (provider_is.equals("Charge")) {
                        getChargeStations("", "", "1", "");
                    }
                    if (provider_is.equals("Parking")) {
                        getChargeStations("", "", "", "1");
                    }

                }
            }
        });


        assert provider_is != null;
       /* if (provider_is.equals("Parking")) {
            //linearLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
            leftImg.setBackgroundResource(R.drawable.ic_parking);
            //url = "https://www.ewheelers.in/app-api/2.0/addresses/search-stations";
            //getParking("","");
            getChargeStations("", "", "", "1");
            getChipFilters("4");
        }*/
       /* if (provider_is.equals("Charge")) {
            //linearLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
            //horizontalScrollView.setVisibility(View.VISIBLE);
            leftImg.setBackgroundResource(R.drawable.charging_station);
            //url = "https://www.ewheelers.in/app-api/2.0/addresses/search-stations";
            if (chipall.isChecked()) {
                getChargeStations("", "", "1", "");
            }
            getChipFilters("3");
        }*/

        if (provider_is.equals("Charge")) {
            scanQr.setText("Scan Charge Hub QR Code to Book Charging Hours");
            scanQr.setVisibility(View.GONE);
            textView.setText(provider_is + " Stations");
            relativeLayout.setVisibility(View.VISIBLE);
            leftImg.setBackgroundResource(R.drawable.charging_station);
            if (chipall.isChecked()) {
                getChargeStations("", "", "1", "");
            }
            getChipFilters("3");
        } else if (provider_is.equals("Parking")) {
            linearLayoutPasses.setVisibility(View.VISIBLE);
            scanQr.setText("Scan Parking Hub QR Code to buy Parking Pass");
            textView.setText(provider_is);
            relativeLayout.setVisibility(View.VISIBLE);
            leftImg.setBackgroundResource(R.drawable.ic_parking);
            getChargeStations("", "", "", "1");
            getChipFilters("4");
        } else if (provider_is.equals("Repair")) {
            scanQr.setText("Scan Mechanic QR Code to Pay Service Charge");
            scanQr.setVisibility(View.GONE);
            textView.setText(provider_is);
            relativeLayout.setVisibility(View.VISIBLE);
            leftImg.setBackgroundResource(R.drawable.ic_mechanics);
            //url = "https://script.google.com/macros/s/AKfycbwNuWGvcBCdH_qzJ33MQqUW7O0LeavHiM5tggWp_1ezeaorzoU/exec?action=getItems";
            getProfileattributes();
        } else if (provider_is.equals("Puncture")) {
            scanQr.setText("Scan Puncture Hub QR Code to Pay Service Charge");
            scanQr.setVisibility(View.GONE);
            textView.setText(provider_is);
            relativeLayout.setVisibility(View.VISIBLE);
            leftImg.setBackgroundResource(R.drawable.ic_tyre);
            //url = "https://script.google.com/macros/s/AKfycbxdsfQFL113Xtuj8uf7B10z85OIDwOav4eZv2S33V59gnSX-IxI/exec?action=getItems";
            getProfileattributes();
        } else if (provider_is.equals("Spares")) {
            textView.setText(provider_is);
            relativeLayout.setVisibility(View.VISIBLE);
            scanQr.setVisibility(View.GONE);
            // getProfileattributes();
            //url = "https://script.google.com/macros/s/AKfycbw1CBgKC8DKEeAnMRuBy3PwiL5CwcPx6nUIYNDEMrvA_ygtp_OB/exec?action=getItems";
        } else if (provider_is.equals("Accessories")) {
            textView.setText(provider_is);
            relativeLayout.setVisibility(View.VISIBLE);
            scanQr.setVisibility(View.GONE);
            //url = "https://script.google.com/macros/s/AKfycbwCazYe6eveZapEPtZVZUNCyZbz_GIww_Tncw1mbtDjlutAOWs/exec?action=getItems";
        } else if (provider_is.equals("Bike Wash")) {
            scanQr.setText("Scan Bike Wash Hub QR Code to Pay Service Charge");
            scanQr.setVisibility(View.GONE);
            textView.setText(provider_is);
            relativeLayout.setVisibility(View.VISIBLE);
            leftImg.setBackgroundResource(R.drawable.ic_waterwash);
            //url = "https://script.google.com/macros/s/AKfycbz2x7hyAkGeqZ4o_nmPXRxn7B5LWKflaj697eijstBBKY3Qr64/exec?action=getItems";
            getProfileattributes();
        } else if (provider_is.equals("Batteries")) {
            scanQr.setText("Scan Battery Service Hub QR Code to Pay Service Charge");
            scanQr.setVisibility(View.GONE);
            textView.setText(provider_is);
            relativeLayout.setVisibility(View.VISIBLE);
            leftImg.setBackgroundResource(R.drawable.ic_battery);
            getProfileattributes();
            //url = "https://script.google.com/macros/s/AKfycbw51r-VxmtwgRPFsPdlEAet83eVInQ4QP_khnEHip0J9M5YurE0/exec?action=getItems";
        } else if (provider_is.equals("Key Repair")) {
            scanQr.setText("Scan Key Service Hub QR Code to Pay Service Charge");
            scanQr.setVisibility(View.GONE);
            textView.setText(provider_is);
            relativeLayout.setVisibility(View.VISIBLE);
            leftImg.setBackgroundResource(R.drawable.ic_key);
            getProfileattributes();
            //url = "https://script.google.com/macros/s/AKfycbzKuNTXT2DD1yWYQZWCPUDlyZtUUx5wehGsDH3bOLAXvl1Z2Js/exec?action=getItems";
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (provider_is.equals("Charge")) {
                   /* if (newText.length() != 0) {
                        getChargeStations(newText, "", "1", "");
                    }*/
                    serviceProvidersAdapter.getFilter().filter(newText);

                } else if (provider_is.equals("Parking")) {
                   /* if (newText.length() != 0) {
                        getChargeStations(newText, "", "", "1");
                    }*/
                    serviceProvidersAdapter.getFilter().filter(newText);

                } else {
                    otherServicesAdapter.getFilter().filter(newText);
                }

                return false;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowServiceProvidersActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = recyclerView.getScrollY(); // For ScrollView
                int scrollX = recyclerView.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
                if (scrollY == recyclerView.getChildAt(0).getMeasuredHeight() - recyclerView.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    count++;
                    // on below line we are making our progress bar visible.

                    if (count == serviceProvidersClassesList.size()) {
                        // on below line we are again calling
                        // a method to load data in our array list.
                        getItems(scrollOption,scrollJsonObjectType);
                    }
                }
            }
        });



    }


    private void getChipFilters(String number) {
        //serviceProvidersClassesList.clear();
        String url_link = Apis.chipfilters + number;
        final RequestQueue queue = Volley.newRequestQueue(this);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String messg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("sellerProductAttribute");
                        String keys = "", values = "";
                        List<String> keysset = new ArrayList<>();
                        List<JSONObject> spinnerfilterList = new ArrayList<>();
                        List<String> keyval = new ArrayList<>();
                        Iterator iterat = jsonObject2.keys();
                        while (iterat.hasNext()) {
                            String keyOfkey = (String) iterat.next();
                            keysset.add(keyOfkey);
                            if (keyOfkey.equals(keysset.get(0))) {
                                JSONObject jsonObjectStationtype = jsonObject2.getJSONObject(keyOfkey);
                                Iterator iterator = jsonObjectStationtype.keys();
                                String formatValue = "";
                                while (iterator.hasNext()) {
                                    keys = (String) iterator.next();
                                    values = jsonObjectStationtype.getString(keys);
                                    if (values.contains("&amp;")) {
                                        formatValue = values.toString().replace("&amp;", "&");
                                    } else {
                                        formatValue = values;
                                    }
                                    FilterListClass filterListClass = new FilterListClass();
                                    filterListClass.setCatId(keys);
                                    filterListClass.setCatName(formatValue);
                                    filterListClass.setType(4);
                                    filterList.add(filterListClass);
                                }
                            } else {
                                if (keyOfkey.equals("Service Type")) {
                                    jsonObjectServicetype = jsonObject2.getJSONObject(keyOfkey);

                                } else {
                                    keyval.add(keyOfkey);
                                    JSONObject jsonObjectVehiletype = jsonObject2.getJSONObject(keyOfkey);
                                    //spinnerfilterList.add(jsonObjectVehiletype);
                                    FilterListClass filterListClass = new FilterListClass();
                                    filterListClass.setType(3);
                                    filterListClass.setJsonObject(jsonObjectVehiletype);
                                    filterListSpinner.add(filterListClass);
                                }
                            }

                        }

                        spinnerFiltersAdapter = new CategoriesFilterAdapter(ShowServiceProvidersActivity.this, filterListSpinner, keyval, provider_is);
                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ShowServiceProvidersActivity.this, RecyclerView.HORIZONTAL, false);
                        spinnerlist.setLayoutManager(linearLayoutManager2);
                        spinnerlist.setAdapter(spinnerFiltersAdapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowServiceProvidersActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewchargeFilters.setLayoutManager(linearLayoutManager);
                        CategoriesFilterAdapter filtersAdapter = new CategoriesFilterAdapter(ShowServiceProvidersActivity.this, filterList, null, provider_is);
                        recyclerViewchargeFilters.setAdapter(filtersAdapter);
                        filtersAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ShowServiceProvidersActivity.this, messg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        Log.e("Service....",stringRequest.toString());
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }


    public void getProfileattributes() {
        pDialog.show();
        String url_link = Apis.sellerprofileattributes;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response..",""+response);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        JSONObject jsonObject1 = jsonObjectData.getJSONObject("result");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("Partner Type");
                        Iterator iterator = jsonObject2.keys();
                        while (iterator.hasNext()) {
                            String keys = (String) iterator.next();
                            String values = jsonObject2.getString(keys);
                            if (provider_is.equals(values)) {
//                                getItems(keys);
                                getOtherServices(keys);
                           } else if (values.equals("EV Battery Services")) {
                                getOtherServices(keys);
                            } else {
                                pDialog.dismiss();
                            }

                        }
                    } else {
                        Toast.makeText(ShowServiceProvidersActivity.this, "check: " + msg, Toast.LENGTH_SHORT).show();
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

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }*/

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        Log.e("Service....",stringRequest.toString());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    private void getOtherServices(String keys) {
        String url_link = Apis.chipfilters + "4";
        final RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String messg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("sellerProductAttribute");
                        List<String> keysset = new ArrayList<>();
                        Iterator iterat = jsonObject2.keys();
                        while (iterat.hasNext()) {
                            String keyOfkey = (String) iterat.next();
                            keysset.add(keyOfkey);
                            if (keyOfkey.equals("Service Type")) {
                                jsonObjectServicetype = jsonObject2.getJSONObject(keyOfkey);
                                getItems(keys, jsonObjectServicetype);
                                //Toast.makeText(ShowServiceProvidersActivity.this, "batterirs:" + keys + " - " + jsonObjectServicetype, Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        Toast.makeText(ShowServiceProvidersActivity.this, messg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        Log.e("Service....",stringRequest.toString());
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    public void removeCatFilters() {
        //getChargeStations("", "", "1", "");
        chipall.setChecked(true);
        chipall.setTextColor(Color.parseColor("#9C3C34"));
    }

    public void removeParkFilters() {
        //getChargeStations("", "", "", "1");
        chipall.setChecked(true);
        chipall.setTextColor(Color.parseColor("#9C3C34"));
    }

    public void getChargeStations(String zipcode, String options, String chargeVal, String parkVal) {
        pDialog.show();
        if (options.equals("")) {
            chipall.setChecked(true);
        } else {
            chipall.setChecked(false);
        }
        serviceProvidersClassesList.clear();
        /*longitude = new NavAppBarActivity().setlongitude();
        latitude = new NavAppBarActivity().setlatitude();*/
        String url = Apis.chargeStations;
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                pDialog.dismiss();
                                JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                                JSONArray jsonArray = jsonObjectdata.getJSONArray("result");
                                if (jsonArray.length() == 0) {
                                    empty_view.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } else {
                                    empty_view.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObjectVal = jsonArray.getJSONObject(i);
                                        String uaid = jsonObjectVal.getString("ua_id");
                                        String ua_identifier = jsonObjectVal.getString("ua_identifier");
                                        String user_id = jsonObjectVal.getString("ua_user_id");
                                        String openstatus = jsonObjectVal.getString("ua_location_open_status");
                                        String identifier = jsonObjectVal.getString("ua_identifier");
                                        String uaname = jsonObjectVal.getString("ua_name");
                                        String ua_address1 = jsonObjectVal.getString("ua_address1");
                                        String ua_address2 = jsonObjectVal.getString("ua_address2");
                                        String uacity = jsonObjectVal.getString("ua_city");
                                        String uaphone = jsonObjectVal.getString("ua_phone");
                                        String uapincode = jsonObjectVal.getString("ua_zip");
                                        String uaautocomplete = jsonObjectVal.getString("ua_auto_complete");
                                        String ualatitude = jsonObjectVal.getString("ua_latitude");
                                        String ualongitude = jsonObjectVal.getString("ua_longitude");
                                        String uadistance = jsonObjectVal.getString("distance_in_km");
                                        String shop_logog = jsonObjectVal.getString("shop_logo");
                                        String shop_id = jsonObjectVal.getString("shop_id");
                                        String servicetype = jsonObjectVal.getString("ua_is_charging_station");

                                        JSONArray jsonArray1 = jsonObjectVal.getJSONArray("ua_operating_days");
                                        // stringDays.add(jsonArray1.toString());
                                        ArrayList<String> stringDays = new ArrayList<>();
                                        for (int jso = 0; jso < jsonArray1.length(); jso++) {
                                            String numb = jsonArray1.getString(jso);
                                            stringDays.add(numb);
                                        }
                                        JSONArray jsonArray2 = jsonObjectVal.getJSONArray("operating_hours");
                                        String opentime = "", closetime = "";
                                        if (jsonArray2.length() == 0) {

                                        } else {
                                            JSONObject jsonObject2 = jsonArray2.getJSONObject(0);
                                            opentime = jsonObject2.getString("oph_open_time");
                                            closetime = jsonObject2.getString("oph_close_time");
                                        }
                                        ServiceProvidersClass serviceProvidersClass = new ServiceProvidersClass();
                                        serviceProvidersClass.setShopid(shop_id);
                                        serviceProvidersClass.setUaidentifier(ua_identifier);
                                        serviceProvidersClass.setImageurl(shop_logog);
                                        serviceProvidersClass.setSetuid(uaid);
                                        serviceProvidersClass.setOpenstatus(openstatus);
                                        serviceProvidersClass.setServiceprovider_name(uaname);
                                        serviceProvidersClass.setServiceprovider_shopname(identifier);
                                        serviceProvidersClass.setServiceprovider_phone_number(uaphone);
                                        serviceProvidersClass.setServiceprovider_address(uaautocomplete);
                                        serviceProvidersClass.setServiceprovider_latitude(ualatitude);
                                        serviceProvidersClass.setServiceprovider_longitude(ualongitude);
                                        serviceProvidersClass.setStrings(stringDays);
                                        serviceProvidersClass.setOpentime(opentime);
                                        serviceProvidersClass.setClosetime(closetime);
                                        double dis = Double.parseDouble(uadistance);
                                        DecimalFormat df = new DecimalFormat("0.00");
                                        df.setRoundingMode(RoundingMode.HALF_UP);
                                        String rounded = df.format(dis);
                                        serviceProvidersClass.setDistance(rounded + "km");
                                        serviceProvidersClass.setServiceProviderIs(provider_is);
                                        serviceProvidersClass.setCurrentlatitude(latitude);
                                        serviceProvidersClass.setCurrentlongitude(longitude);
                                        if (parkVal.equals("1")) {
                                            serviceProvidersClass.setTypeofLayout(2);
                                            if (jsonObjectServicetype != null) {
                                                Log.d("jsonservicetype", jsonObjectServicetype.toString());
                                                serviceProvidersClass.setJsonServiceObject(jsonObjectServicetype);
                                            }
                                        } else {
                                            serviceProvidersClass.setTypeofLayout(0);
                                        }
                                        serviceProvidersClassesList.add(serviceProvidersClass);

                                        // getDistance(latitude,longitude,ualatitude,ualongitude,serviceProvidersClass);

                                    }

                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowServiceProvidersActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    serviceProvidersAdapter = new ServiceProvidersAdapter(ShowServiceProvidersActivity.this, serviceProvidersClassesList);
                                    recyclerView.setAdapter(serviceProvidersAdapter);
                                    serviceProvidersAdapter.notifyDataSetChanged();
                                    // progressBar.setVisibility(View.GONE);
                                }
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
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("latitude", String.valueOf(latitude));
                data3.put("longitude", String.valueOf(longitude));
                //data3.put("zip", zipcode);
                data3.put("address", zipcode);
                data3.put("charging_station", chargeVal);
                data3.put("service_station", parkVal);
                data3.put("options", options);
                data3.put("pageSize", "1000");
                data3.put("page", "1");
                Log.d("latlongs...",data3.toString());
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }

    public void getItems(String options, JSONObject jsonObjectType) {
        Log.d("Options...",options+".."+jsonObjectType.toString());
        if( pagination == 1){
            scrollOption = options;
            scrollJsonObjectType = jsonObjectType;
            serviceProvidersClassesList.clear();
        }

        String url_link = Apis.sellerlistprofile;
        final RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("result");
                        Log.e("PostData....","listsize.."+jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectdata = jsonArray.getJSONObject(i);
                            String shopid = jsonObjectdata.getString("shop_id");
                            String Service_Provider_name = jsonObjectdata.getString("user_name");
                            String Service_Provider_shopname = jsonObjectdata.getString("shop_identifier");
                            String Service_Provider_phonenumber = jsonObjectdata.getString("shop_phone");
                            String Service_Provider_adress = jsonObjectdata.getString("shop_auto_complete");
                            String Service_Provider_latitude = jsonObjectdata.getString("shop_latitude");
                            String Service_Provider_longitude = jsonObjectdata.getString("shop_longitude");
                            String Service_Provider_distance = jsonObjectdata.getString("distance_in_km");
                            String Service_Provider_logo = jsonObjectdata.getString("shop_logo");
                            ServiceProvidersClass serviceProvidersClass = new ServiceProvidersClass();
                            if (jsonObjectType != null) {
                                serviceProvidersClass.setJsonServiceObject(jsonObjectType);
                            }
                            serviceProvidersClass.setShopid(shopid);
                            serviceProvidersClass.setServiceprovider_name(Service_Provider_name);
                            serviceProvidersClass.setServiceprovider_shopname(Service_Provider_shopname);
                            serviceProvidersClass.setServiceprovider_phone_number(Service_Provider_phonenumber);
                            serviceProvidersClass.setServiceprovider_address(Service_Provider_adress);
                            serviceProvidersClass.setServiceprovider_latitude(Service_Provider_latitude);
                            serviceProvidersClass.setServiceprovider_longitude(Service_Provider_longitude);
                            double dis = Double.parseDouble(Service_Provider_distance);
                            DecimalFormat df = new DecimalFormat("0.00");
                            df.setRoundingMode(RoundingMode.HALF_UP);
                            String rounded = df.format(dis);
                            serviceProvidersClass.setDistance(rounded + "km");
                            serviceProvidersClass.setLogo(Service_Provider_logo);
                            serviceProvidersClass.setServiceProviderIs(provider_is);
                            serviceProvidersClass.setCurrentlatitude(latitude);
                            serviceProvidersClass.setCurrentlongitude(longitude);
                            serviceProvidersClassesList.add(serviceProvidersClass);
                        }
                        if (serviceProvidersClassesList.isEmpty()) {
                            empty_view.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            if(pagination > 1 ){
                                otherServicesAdapter.notifyDataSetChanged();
                            }else{
                                empty_view.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowServiceProvidersActivity.this, RecyclerView.VERTICAL, false);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                otherServicesAdapter = new OtherServices(ShowServiceProvidersActivity.this, serviceProvidersClassesList);
                                recyclerView.setAdapter(otherServicesAdapter);
                                otherServicesAdapter.notifyDataSetChanged();
                            }

                            pDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(ShowServiceProvidersActivity.this, "failed to load:" + msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }*/

            @Override
            public Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("options", options);
                data3.put("latitude", String.valueOf(latitude));
                data3.put("longitude", String.valueOf(longitude));
                data3.put("page",""+pagination);
                Log.e("PostData....",data3.toString());
                return data3;

            }

        };
        Log.e("Service....",stringRequest.toString());
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }


    public double lat() {
        return latitude;
    }

    public double longi() {
        return longitude;
    }


   /* public void getDistance(double currlat, double currlng, String servicelat, String servicelng, ServiceProvidersClass serviceProvidersClass) {
        serviceProvidersClassesList.clear();
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + currlat + "," + currlng + "&destinations=" + servicelat + "," + servicelng + "&key=AIzaSyAyuQjmdCe43w40mbR422_ix8QPzgDbgxs";
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray dist = jsonObject.getJSONArray("rows");
                            JSONObject obj2 = (JSONObject) dist.get(0);
                            JSONArray disting = (JSONArray) obj2.get("elements");
                            JSONObject obj3 = (JSONObject) disting.get(0);
                            JSONObject obj4 = (JSONObject) obj3.get("distance");
                            JSONObject obj5 = (JSONObject) obj3.get("duration");
                            distance = obj4.getString("text");

                            //  holder.locate.setText(distance);
                            // Log.e("sanhdhj", distance);

                            serviceProvidersClass.setDistance(distance);
                            serviceProvidersClassesList.add(serviceProvidersClass);

                            Collections.sort(serviceProvidersClassesList, new Comparator<ServiceProvidersClass>() {
                                @Override
                                public int compare(ServiceProvidersClass o1, ServiceProvidersClass o2) {
                                    return o1.getDistance().compareTo(o2.getDistance());
                                }
                            });

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowServiceProvidersActivity.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            serviceProvidersAdapter = new ServiceProvidersAdapter(ShowServiceProvidersActivity.this, serviceProvidersClassesList);
                            recyclerView.setAdapter(serviceProvidersAdapter);
                            serviceProvidersAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mypasses:
                Intent i = new Intent(getApplicationContext(),QRPasses.class);
                startActivity(i);
                break;
            case R.id.seperatorders:
                if (jsonObjectServicetype != null) {
                    Iterator iterator = jsonObjectServicetype.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        try {
                            String value = jsonObjectServicetype.getString(key);
                            if (provider_is.equals(value)) {
                                //Toast.makeText(ShowServiceProvidersActivity.this, "idis:" + key, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ShowServiceProvidersActivity.this, MyOrdersActivity.class);
                                intent.putExtra("servicetype", key);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.mapview:
                //Log.i("serivicelist", serviceProvidersClassesList.toString());
                Gson gson = new Gson();
                String jsonCars = gson.toJson(serviceProvidersClassesList);
                Intent intent = new Intent(ShowServiceProvidersActivity.this, MapsActivity.class);
                intent.putExtra("chargelist", jsonCars);
                intent.putExtra("icontype", provider_is);
                startActivity(intent);
                break;
            case R.id.reqtestdrive:
                gson = new Gson();
                jsonCars = gson.toJson(serviceProvidersClassesList);
                i = new Intent(ShowServiceProvidersActivity.this, ScanQRCode.class);
                i.putExtra("chargelist", jsonCars);
                i.putExtra("icontype", provider_is);
                startActivity(i);
                break;
            case R.id.closereqbtn:
                relativeLayout.setVisibility(View.GONE);
                break;
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.serviceprovidermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapview:
                Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                i.putExtra("chargelist", (Serializable) serviceProvidersClassesList);
                startActivity(i);
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }*/
}
