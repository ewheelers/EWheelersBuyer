package com.ewheelers.ewheelersbuyer.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.BillingAddressesAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.BillingAddress;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShppingAddressFragment extends Fragment {
    RecyclerView recyclerView;
    List<BillingAddress> billingAddressList = new ArrayList<>();
    BillingAddressesAdapter billingAddressesAdapter;
    String tokenValue;

    public ShppingAddressFragment() {
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
        View v = inflater.inflate(R.layout.fragment_shpping_address, container, false);
        recyclerView = v.findViewById(R.id.addresses_list);
        tokenValue = new SessionStorage().getStrings(getActivity(), SessionStorage.tokenvalue);
        getAddresses();

        return v;
    }

    private void getAddresses() {
        billingAddressList.clear();
        String url_link = Apis.getaddresses;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                        String city = address.getString("city_name");
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
                        String  autocomplete = address.getString("ua_auto_complete");
                        BillingAddress billingAddress = new BillingAddress();
                        billingAddress.setUa_id(uaid);
                        billingAddress.setAutocomplete(autocomplete);
                        billingAddress.setUa_identifier(uaidentifier);
                        billingAddress.setUa_name(uaname);
                        billingAddress.setUa_address1(address1);
                        billingAddress.setUa_address2(address2);
                        billingAddress.setCity_name(city);
                        billingAddress.setState_name(statename);
                        billingAddress.setCountry_name(countryname);
                        billingAddress.setUa_zip(zip);
                        billingAddress.setUa_phone(phone);
                        billingAddress.setIsShippingAddress(shippingAddress);
                        billingAddress.setUa_is_default(uadefault);
                        billingAddress.setTypelayout(1);
                        billingAddressList.add(billingAddress);

                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    billingAddressesAdapter = new BillingAddressesAdapter(getActivity(),billingAddressList);
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


}
