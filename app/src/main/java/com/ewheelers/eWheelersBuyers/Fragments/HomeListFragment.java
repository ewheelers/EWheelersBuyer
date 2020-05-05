package com.ewheelers.eWheelersBuyers.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.ewheelers.eWheelersBuyers.Adapters.SlidingImage_Adapter;
import com.ewheelers.eWheelersBuyers.ModelClass.CirclePageIndicator;
import com.ewheelers.eWheelersBuyers.ModelClass.HomeCollectionProducts;
import com.ewheelers.eWheelersBuyers.ModelClass.HomeModelClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SessionStorage;
import com.ewheelers.eWheelersBuyers.Utilities.ConnectionStateMonitor;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class HomeListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView textViewoff;
    private List<HomeModelClass> homeModelClasses = new ArrayList<>();
    private HomeCollectionAdapter collectionAdapter;

    private String cartcount;
    private String tokenvalue;
    private JSONArray jsonArrayCollections = null;
    private JSONArray jsonArrayProducts, jsonArrayProductscat, jsonArrayProductsbrand, jsonArrayProductsshop;
    private List<HomeCollectionProducts> homeCollectionSliderList = new ArrayList<HomeCollectionProducts>();
    ProgressBar progressBar;
    private String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.CALL_PHONE","android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};
    int requestCode = 200;
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
        textViewoff = v.findViewById(R.id.offlinetext);
        progressBar = v.findViewById(R.id.progress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions( permissions, requestCode );
        }


        recyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //At this point the layout is complete and the
                        //dimensions of recyclerView and any child views are known.
                        //Remove listener after changed RecyclerView's height to prevent infinite loop
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        ConnectionStateMonitor connectionStateMonitor = new ConnectionStateMonitor(getActivity());
        connectionStateMonitor.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    // network availale
                    gethomecollections(v);
                    recyclerView.setVisibility(View.VISIBLE);
                    textViewoff.setVisibility(View.GONE);
                }else{
                    // network lost
                   // Toast.makeText(getActivity(), "no network", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                    textViewoff.setVisibility(View.VISIBLE);
                }
            }
        });
        return v;
    }


    private void gethomecollections(View v) {
        progressBar.setVisibility(View.VISIBLE);
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
                        progressBar.setVisibility(View.GONE);
                        homeModelClasses.clear();
                        homeCollectionSliderList.clear();

                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                        cartcount = dataJsonObject.getString("cartItemsCount");

                        JSONArray jsonArray = dataJsonObject.getJSONArray("slides");
                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObjectSlides = jsonArray.getJSONObject(k);
                            String slidimageurl = jsonObjectSlides.getString("slide_image_url");
                            HomeCollectionProducts homeCollectionProductsSlides = new HomeCollectionProducts();
                            homeCollectionProductsSlides.setSlideImageurl(slidimageurl);
                            homeCollectionSliderList.add(homeCollectionProductsSlides);

                        }

                        jsonArrayCollections = dataJsonObject.getJSONArray("collections");
                        for (int i = 0; i < jsonArrayCollections.length(); i++) {
                            JSONObject jsonObjectProducts = jsonArrayCollections.getJSONObject(i);
                            String coll_type = jsonObjectProducts.getString("collection_type");
                            String collectionName = jsonObjectProducts.getString("collection_name");
                            String primaryrecord = jsonObjectProducts.getString("collection_primary_records");
                            String collectionid = jsonObjectProducts.getString("collection_id");
                            List<HomeCollectionProducts> homeCollectionProductsList = new ArrayList<>();
                            List<HomeCollectionProducts> homeCollectionProductsListBrands = new ArrayList<>();
                            List<HomeCollectionProducts> homeCollectionShopsList = new ArrayList<>();
                            List<HomeCollectionProducts> homeCollectionCategoriesList = new ArrayList<>();
                            if (collectionName.equals("Garage Categories")) {

                            } else if (collectionName.equals("Garage Brands")) {

                            } else {

                                if (coll_type.equals("1")) {
                                    String primaryrecordprods = jsonObjectProducts.getString("collection_primary_records");
                                    jsonArrayProducts = jsonObjectProducts.getJSONArray("products");
                                    if (jsonArrayProducts.length() <= Integer.parseInt(primaryrecordprods)) {
                                        for (int j = 0; j < jsonArrayProducts.length(); j++) {
                                            try {
                                                JSONObject products = jsonArrayProducts.getJSONObject(j);
                                                String productName = products.getString("product_name");
                                                String productPrice = products.getString("selprod_price");
                                                String productImageurl = products.getString("product_image_url");
                                                String selproductid = products.getString("selprod_id");
                                                String productid = products.getString("product_id");
                                                String productcatname = products.getString("prodcat_name");
                                                String isSell = products.getString("is_sell");
                                                String isRent = products.getString("is_rent");
                                                String rentPrice = products.getString("rent_price");
                                                String rentaltype = products.getString("rental_type");

                                                HomeCollectionProducts homeCollectionProducts1 = new HomeCollectionProducts();
                                                homeCollectionProducts1.setProdcat_name(productcatname);
                                                homeCollectionProducts1.setProduct_name(productName);
                                                homeCollectionProducts1.setSelprod_price(productPrice);
                                                homeCollectionProducts1.setProduct_image_url(productImageurl);
                                                homeCollectionProducts1.setSelprod_id(selproductid);
                                                homeCollectionProducts1.setProduct_id(productid);
                                                homeCollectionProducts1.setIs_rent(isRent);
                                                homeCollectionProducts1.setType(0);
                                                homeCollectionProductsList.add(homeCollectionProducts1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        for (int j = 0; j < Integer.parseInt(primaryrecordprods); j++) {
                                            try {
                                                JSONObject products = jsonArrayProducts.getJSONObject(j);
                                                String productName = products.getString("product_name");
                                                String productPrice = products.getString("selprod_price");
                                                String productImageurl = products.getString("product_image_url");
                                                String selproductid = products.getString("selprod_id");
                                                String productid = products.getString("product_id");
                                                String productcatname = products.getString("prodcat_name");
                                                String isSell = products.getString("is_sell");
                                                String isRent = products.getString("is_rent");
                                                String rentPrice = products.getString("rent_price");
                                                String rentaltype = products.getString("rental_type");

                                                HomeCollectionProducts homeCollectionProducts1 = new HomeCollectionProducts();
                                                homeCollectionProducts1.setProdcat_name(productcatname);
                                                homeCollectionProducts1.setProduct_name(productName);
                                                homeCollectionProducts1.setSelprod_price(productPrice);
                                                homeCollectionProducts1.setProduct_image_url(productImageurl);
                                                homeCollectionProducts1.setSelprod_id(selproductid);
                                                homeCollectionProducts1.setProduct_id(productid);
                                                homeCollectionProducts1.setIs_rent(isRent);
                                                homeCollectionProducts1.setType(0);
                                                homeCollectionProductsList.add(homeCollectionProducts1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }


                                }
                                if (coll_type.equals("2")) {
                                    String primaryrecordcat = jsonObjectProducts.getString("collection_primary_records");
                                    jsonArrayProductscat = jsonObjectProducts.getJSONArray("categories");
                                    if (jsonArrayProductscat.length() <= Integer.parseInt(primaryrecordcat)) {
                                        for (int s = 0; s < jsonArrayProductscat.length(); s++) {
                                            JSONObject jsonObjectcat = jsonArrayProductscat.getJSONObject(s);
                                            String prodcat_id = jsonObjectcat.getString("prodcat_id");
                                            String prodcat_name = jsonObjectcat.getString("prodcat_name");
                                            String prodcat_description = jsonObjectcat.getString("prodcat_description");
                                            String category_image_url = jsonObjectcat.getString("category_image_url");
                                            HomeCollectionProducts homeCollectionProductsc = new HomeCollectionProducts();
                                            homeCollectionProductsc.setProdcategory_name(prodcat_name);
                                            homeCollectionProductsc.setProdcategory_imageurl(category_image_url);
                                            homeCollectionProductsc.setProdcategory_id(prodcat_id);
                                            homeCollectionProductsc.setType(3);
                                            homeCollectionCategoriesList.add(homeCollectionProductsc);
                                        }
                                    } else {
                                        for (int s = 0; s < Integer.parseInt(primaryrecordcat); s++) {
                                            JSONObject jsonObjectcat = jsonArrayProductscat.getJSONObject(s);
                                            String prodcat_id = jsonObjectcat.getString("prodcat_id");
                                            String prodcat_name = jsonObjectcat.getString("prodcat_name");
                                            String prodcat_description = jsonObjectcat.getString("prodcat_description");
                                            String category_image_url = jsonObjectcat.getString("category_image_url");
                                            HomeCollectionProducts homeCollectionProductsc = new HomeCollectionProducts();
                                            homeCollectionProductsc.setProdcategory_name(prodcat_name);
                                            homeCollectionProductsc.setProdcategory_imageurl(category_image_url);
                                            homeCollectionProductsc.setProdcategory_id(prodcat_id);
                                            homeCollectionProductsc.setType(3);
                                            homeCollectionCategoriesList.add(homeCollectionProductsc);
                                        }
                                    }


                                }
                                if (coll_type.equals("3")) {
                                    String primaryrecordshop = jsonObjectProducts.getString("collection_primary_records");
                                    jsonArrayProductsshop = jsonObjectProducts.getJSONArray("shops");
                                    if (jsonArrayProductsshop.length() <= Integer.parseInt(primaryrecordshop)) {
                                        for (int k = 0; k < jsonArrayProductsshop.length(); k++) {
                                            JSONObject jsonObjectshop1 = jsonArrayProductsshop.getJSONObject(k);
                                            String shopbanner = jsonObjectshop1.getString("shop_banner");
                                            String shop_id = jsonObjectshop1.getString("shop_id");
                                            String shop_user_id = jsonObjectshop1.getString("shop_user_id");
                                            String shop_name = jsonObjectshop1.getString("shop_name");
                                            String country_name = jsonObjectshop1.getString("country_name");
                                            String state_name = jsonObjectshop1.getString("state_name");
                                            String rating = jsonObjectshop1.getString("rating");
                                            String shop_logo = jsonObjectshop1.getString("shop_logo");

                                            HomeCollectionProducts homeCollectionProducts = new HomeCollectionProducts();
                                            homeCollectionProducts.setShopbanner(shopbanner);
                                            homeCollectionProducts.setShopid(shop_id);
                                            homeCollectionProducts.setShoplogo(shop_logo);
                                            homeCollectionProducts.setShopname(shop_name);
                                            homeCollectionProducts.setShopuserid(shop_user_id);
                                            homeCollectionProducts.setCountryname(country_name);
                                            homeCollectionProducts.setStatename(state_name);
                                            homeCollectionProducts.setRating(rating);
                                            homeCollectionProducts.setType(4);
                                            homeCollectionShopsList.add(homeCollectionProducts);
                                        }
                                    } else {
                                        for (int k = 0; k < Integer.parseInt(primaryrecordshop); k++) {
                                            JSONObject jsonObjectshop1 = jsonArrayProductsshop.getJSONObject(k);
                                            String shopbanner = jsonObjectshop1.getString("shop_banner");
                                            String shop_id = jsonObjectshop1.getString("shop_id");
                                            String shop_user_id = jsonObjectshop1.getString("shop_user_id");
                                            String shop_name = jsonObjectshop1.getString("shop_name");
                                            String country_name = jsonObjectshop1.getString("country_name");
                                            String state_name = jsonObjectshop1.getString("state_name");
                                            String rating = jsonObjectshop1.getString("rating");
                                            String shop_logo = jsonObjectshop1.getString("shop_logo");

                                            HomeCollectionProducts homeCollectionProducts = new HomeCollectionProducts();
                                            homeCollectionProducts.setShopbanner(shopbanner);
                                            homeCollectionProducts.setShopid(shop_id);
                                            homeCollectionProducts.setShoplogo(shop_logo);
                                            homeCollectionProducts.setShopname(shop_name);
                                            homeCollectionProducts.setShopuserid(shop_user_id);
                                            homeCollectionProducts.setCountryname(country_name);
                                            homeCollectionProducts.setStatename(state_name);
                                            homeCollectionProducts.setRating(rating);
                                            homeCollectionProducts.setType(4);
                                            homeCollectionShopsList.add(homeCollectionProducts);
                                        }
                                    }


                                }
                                if (coll_type.equals("4")) {
                                    String primaryrecordbrand = jsonObjectProducts.getString("collection_primary_records");
                                    jsonArrayProductsbrand = jsonObjectProducts.getJSONArray("brands");
                                    if (jsonArrayProductsbrand.length() <= Integer.parseInt(primaryrecordbrand)) {
                                        for (int t = 0; t < jsonArrayProductsbrand.length(); t++) {
                                            JSONObject productsbrand = null;
                                            try {
                                                productsbrand = jsonArrayProductsbrand.getJSONObject(t);
                                                String brandid = productsbrand.getString("brand_id");
                                                String brandname = productsbrand.getString("brand_name");
                                                String brandimage = productsbrand.getString("brand_image");

                                                HomeCollectionProducts homeCollectionProducts4 = new HomeCollectionProducts();
                                                homeCollectionProducts4.setBrandimageurl(brandimage);
                                                homeCollectionProducts4.setBrandid(brandid);
                                                homeCollectionProducts4.setBrandname(brandname);
                                                homeCollectionProducts4.setType(1);
                                                homeCollectionProductsListBrands.add(homeCollectionProducts4);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        for (int t = 0; t < Integer.parseInt(primaryrecordbrand); t++) {
                                            JSONObject productsbrand = null;
                                            try {
                                                productsbrand = jsonArrayProductsbrand.getJSONObject(t);
                                                String brandid = productsbrand.getString("brand_id");
                                                String brandname = productsbrand.getString("brand_name");
                                                String brandimage = productsbrand.getString("brand_image");

                                                HomeCollectionProducts homeCollectionProducts4 = new HomeCollectionProducts();
                                                homeCollectionProducts4.setBrandimageurl(brandimage);
                                                homeCollectionProducts4.setBrandid(brandid);
                                                homeCollectionProducts4.setBrandname(brandname);
                                                homeCollectionProducts4.setType(1);
                                                homeCollectionProductsListBrands.add(homeCollectionProducts4);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                HomeModelClass homeModelClass = new HomeModelClass();
                                homeModelClass.setHomeCollectionProducts(homeCollectionProductsList);
                                homeModelClass.setHomeCollectionProductsBrands(homeCollectionProductsListBrands);
                                homeModelClass.setHomeCollectionProductsShops(homeCollectionShopsList);
                                homeModelClass.setHomeCollectionProductsCategories(homeCollectionCategoriesList);
                                homeModelClass.setPrimaryrecord(primaryrecord);
                                homeModelClass.setCollectionId(collectionid);
                                homeModelClass.setHeadcatTitle(collectionName);
                                homeModelClass.setCollectiontype(coll_type);
                                homeModelClasses.add(homeModelClass);
                            }
                        }
                        collectionAdapter = new HomeCollectionAdapter(getActivity(), homeModelClasses, homeCollectionSliderList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(collectionAdapter);
                        //recyclerView.stopScroll();
                        //collectionAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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


}
