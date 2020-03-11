package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.CartListingAdapter;
import com.ewheelers.ewheelersbuyer.Adapters.ServiceProvidersAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.CartListClass;
import com.ewheelers.ewheelersbuyer.ModelClass.ServiceProvidersClass;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowServiceProvidersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ServiceProvidersAdapter serviceProvidersAdapter;
    List<ServiceProvidersClass> serviceProvidersClassesList = new ArrayList<>();
    String provider_is;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_service_providers);
        recyclerView = findViewById(R.id.serviceProviders_list);
        provider_is = getIntent().getStringExtra("providerIs");

        assert provider_is != null;
        if(provider_is.equals("charge")){
            url = "";
        }
        if(provider_is.equals("mechanic")){
            url = "https://script.google.com/macros/s/AKfycbwNuWGvcBCdH_qzJ33MQqUW7O0LeavHiM5tggWp_1ezeaorzoU/exec?action=getItems";
        }
        if(provider_is.equals("puncture")){
            url = "https://script.google.com/macros/s/AKfycbxdsfQFL113Xtuj8uf7B10z85OIDwOav4eZv2S33V59gnSX-IxI/exec?action=getItems";
        }
        if(provider_is.equals("spares")){
            url = "";
        }
        if(provider_is.equals("accessories")){
            url = "";
        }

        if(provider_is.equals("water wash")){
            url = "";

        }
        if(provider_is.equals("battery")){
            url = "";

        }
        if(provider_is.equals("key repair")){
            url = "";

        }
        getItems(url);

    }

    private void getItems(String url_link) {
        final RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                            serviceProvidersClassesList.add(serviceProvidersClass);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowServiceProvidersActivity.this,RecyclerView.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        serviceProvidersAdapter = new ServiceProvidersAdapter(ShowServiceProvidersActivity.this,serviceProvidersClassesList);
                        recyclerView.setAdapter(serviceProvidersAdapter);
                        serviceProvidersAdapter.notifyDataSetChanged();


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
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

}
