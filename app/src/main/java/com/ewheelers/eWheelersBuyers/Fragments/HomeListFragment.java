package com.ewheelers.eWheelersBuyers.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.HomeCollectionAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.HomeCollectionProducts;
import com.ewheelers.eWheelersBuyers.ModelClass.HomeModelClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SessionStorage;
import com.ewheelers.eWheelersBuyers.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class HomeListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private List<HomeModelClass> homeModelClasses = new ArrayList<>();
    private HomeCollectionAdapter collectionAdapter;
    private String cartcount;
    private String tokenvalue;
    private JSONArray jsonArrayCollections = null;
    private JSONArray jsonArrayProducts = null, jsonArrayProducts2 = null, jsonArrayProducts3 = null, jsonArrayProducts4 = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<HomeCollectionProducts> homeCollectionbannersList = new ArrayList<>();

    public HomeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_list, container, false);
        tokenvalue = new SessionStorage().getStrings(Objects.requireNonNull(getActivity()), SessionStorage.tokenvalue);
        recyclerView = v.findViewById(R.id.homelistview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiprefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);
                                         gethomecollections();
                                     }
                                 }
        );

        return v;
    }

    private void gethomecollections() {
        mSwipeRefreshLayout.setRefreshing(true);
        homeModelClasses.clear();
        final RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        String serverurl = Apis.home;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                        cartcount = dataJsonObject.getString("cartItemsCount");

                       /* JSONArray jsonArray = dataJsonObject.getJSONArray("slides");
                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObjectSlides = jsonArray.getJSONObject(k);
                            String slidimageurl = jsonObjectSlides.getString("slide_image_url");
                            HomeCollectionProducts homeCollectionProductsSlides = new HomeCollectionProducts();
                            homeCollectionProductsSlides.setSlideImageurl(slidimageurl);
                            homeCollectionSliderList.add(homeCollectionProductsSlides);

                        }
                        init(homeCollectionSliderList, view);*/

                        jsonArrayCollections = dataJsonObject.getJSONArray("collections");
                        for (int i = 0; i < jsonArrayCollections.length(); i++) {
                            JSONObject jsonObjectProducts = jsonArrayCollections.getJSONObject(i);
                            String coll_type = jsonObjectProducts.getString("collection_type");
                            String collectionName = jsonObjectProducts.getString("collection_name");
                            String primaryrecord = jsonObjectProducts.getString("collection_primary_records");

                            if (coll_type.equals("1")) {
                                jsonArrayProducts = jsonObjectProducts.getJSONArray("products");

                                HomeModelClass homeModelClass = new HomeModelClass();
                                homeModelClass.setHeadcatTitle(collectionName);
                                homeModelClass.setTypeoflayout(0);
                                homeModelClass.setJsonArraylist(jsonArrayProducts);
                                homeModelClasses.add(homeModelClass);
                            }
                            if (coll_type.equals("2")) {
                                jsonArrayProducts2 = jsonObjectProducts.getJSONArray("categories");

                                HomeModelClass homeModelClass2 = new HomeModelClass();
                                homeModelClass2.setHeadcatTitle(collectionName);
                                homeModelClass2.setTypeoflayout(1);
                                homeModelClass2.setJsonArraylist(jsonArrayProducts2);
                                homeModelClasses.add(homeModelClass2);
                            }
                            if (coll_type.equals("3")) {
                                jsonArrayProducts3 = jsonObjectProducts.getJSONArray("shops");

                                HomeModelClass homeModelClass3 = new HomeModelClass();
                                homeModelClass3.setHeadcatTitle(collectionName);
                                homeModelClass3.setTypeoflayout(3);
                                homeModelClass3.setJsonArraylist(jsonArrayProducts3);
                                homeModelClasses.add(homeModelClass3);
                            }

                            if (coll_type.equals("4")) {
                                jsonArrayProducts4 = jsonObjectProducts.getJSONArray("brands");

                                HomeModelClass homeModelClass4 = new HomeModelClass();
                                homeModelClass4.setHeadcatTitle(collectionName);
                                homeModelClass4.setTypeoflayout(2);
                                homeModelClass4.setJsonArraylist(jsonArrayProducts4);
                                homeModelClasses.add(homeModelClass4);

                            }

                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        collectionAdapter = new HomeCollectionAdapter(getActivity(), homeModelClasses);
                        recyclerView.setAdapter(collectionAdapter);
                        recyclerView.stopScroll();
                        collectionAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);


                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());


            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }


    @Override
    public void onRefresh() {
        gethomecollections();
    }
}
