package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.ServiceProvidersAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.ServiceProvidersClass;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.kinda.alert.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowServiceProvidersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ServiceProvidersAdapter serviceProvidersAdapter;
    List<ServiceProvidersClass> serviceProvidersClassesList = new ArrayList<>();
    String provider_is;
    String url = "";
    KAlertDialog pDialog;
    double latitude, longitude;
    String distance = "";
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_service_providers);
        recyclerView = findViewById(R.id.serviceProviders_list);


        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(false);

        provider_is = getIntent().getStringExtra("providerIs");

        getSupportActionBar().setTitle(provider_is + " Services");

        longitude = new NavAppBarActivity().setlongitude();
        latitude = new NavAppBarActivity().setlatitude();

        assert provider_is != null;
        if (provider_is.equals("Charge")) {
            url = "";
        }
        if (provider_is.equals("Mechanic")) {
            url = "https://script.google.com/macros/s/AKfycbwNuWGvcBCdH_qzJ33MQqUW7O0LeavHiM5tggWp_1ezeaorzoU/exec?action=getItems";
            getItems(url);
        }
        if (provider_is.equals("Puncture")) {
            url = "https://script.google.com/macros/s/AKfycbxdsfQFL113Xtuj8uf7B10z85OIDwOav4eZv2S33V59gnSX-IxI/exec?action=getItems";
            getItems(url);
        }
        if (provider_is.equals("Spares")) {
            url = "https://script.google.com/macros/s/AKfycbw1CBgKC8DKEeAnMRuBy3PwiL5CwcPx6nUIYNDEMrvA_ygtp_OB/exec?action=getItems";
            getItems(url);
        }
        if (provider_is.equals("Accessories")) {
            url = "https://script.google.com/macros/s/AKfycbwCazYe6eveZapEPtZVZUNCyZbz_GIww_Tncw1mbtDjlutAOWs/exec?action=getItems";
            getItems(url);
        }

        if (provider_is.equals("Water wash")) {
            url = "https://script.google.com/macros/s/AKfycbz2x7hyAkGeqZ4o_nmPXRxn7B5LWKflaj697eijstBBKY3Qr64/exec?action=getItems";
            getItems(url);
        }
        if (provider_is.equals("Battery")) {
            url = "https://script.google.com/macros/s/AKfycbw51r-VxmtwgRPFsPdlEAet83eVInQ4QP_khnEHip0J9M5YurE0/exec?action=getItems";
            getItems(url);
        }
        if (provider_is.equals("Key repair")) {
            url = "https://script.google.com/macros/s/AKfycbzKuNTXT2DD1yWYQZWCPUDlyZtUUx5wehGsDH3bOLAXvl1Z2Js/exec?action=getItems";
            getItems(url);
        }


    }

    private void getItems(String url_link) {
        serviceProvidersClassesList.clear();
        final RequestQueue queue = Volley.newRequestQueue(this);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectdata = jsonArray.getJSONObject(i);
                        String Service_Provider_name = jsonObjectdata.getString("Service Provider name");
                        String Service_Provider_shopname = jsonObjectdata.getString("Service ProviderShop Name");
                        String Service_Provider_phonenumber = jsonObjectdata.getString("Phone Number");
                        String Service_Provider_alternatenumber = jsonObjectdata.getString("Alternate Nnumber");
                        String Service_Provider_emailid = jsonObjectdata.getString("Email Id");
                        String Service_Provider_adress = jsonObjectdata.getString("Address");
                        String Service_Provider_latitude = jsonObjectdata.getString("Latitude");
                        String Service_Provider_longitude = jsonObjectdata.getString("Longitude");

                        ServiceProvidersClass serviceProvidersClass = new ServiceProvidersClass();
                        serviceProvidersClass.setServiceprovider_name(Service_Provider_name);
                        serviceProvidersClass.setServiceprovider_shopname(Service_Provider_shopname);
                        serviceProvidersClass.setServiceprovider_phone_number(Service_Provider_phonenumber);
                        serviceProvidersClass.setServiceprovider_alternate_number(Service_Provider_alternatenumber);
                        serviceProvidersClass.setServiceprovider_emailid(Service_Provider_emailid);
                        serviceProvidersClass.setServiceprovider_address(Service_Provider_adress);
                        serviceProvidersClass.setServiceprovider_latitude(Service_Provider_latitude);
                        serviceProvidersClass.setServiceprovider_longitude(Service_Provider_longitude);
                        serviceProvidersClass.setServiceProviderIs(provider_is);
                        serviceProvidersClass.setCurrentlatitude(latitude);
                        serviceProvidersClass.setCurrentlongitude(longitude);

                        getDistance(latitude,longitude,Service_Provider_latitude,Service_Provider_longitude,serviceProvidersClass);

                       // serviceProvidersClassesList.add(serviceProvidersClass);
                    }

                  /*  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowServiceProvidersActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    serviceProvidersAdapter = new ServiceProvidersAdapter(ShowServiceProvidersActivity.this, serviceProvidersClassesList);
                    recyclerView.setAdapter(serviceProvidersAdapter);
                    serviceProvidersAdapter.notifyDataSetChanged();*/

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


    public void getDistance(double currlat, double currlng, String servicelat, String servicelng,ServiceProvidersClass serviceProvidersClass) {
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
                            Log.e("sanhdhj", distance);

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
                            //serviceProvidersAdapter.notifyDataSetChanged();

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
    }


}
