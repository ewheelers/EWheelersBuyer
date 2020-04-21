package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.AllebikesAdapter;
import com.ewheelers.ewheelersbuyer.Adapters.BillingAddressesAdapter;
import com.ewheelers.ewheelersbuyer.Adapters.SellerListAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.AllebikesModelClass;
import com.ewheelers.ewheelersbuyer.ModelClass.BillingAddress;
import com.ewheelers.ewheelersbuyer.ModelClass.SellerListModel;
import com.ewheelers.ewheelersbuyer.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellersListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SellerListAdapter sellerListAdapter;
    List<SellerListModel> sellerListModelList = new ArrayList<>();
    List<AllebikesModelClass> allebikesModelClassesList = new ArrayList<>();
    String shopName, shopAddres, selectprodid, tokenValue, typeofactivity;
    ProgressBar progressBar;
    AllebikesAdapter allebikesAdapter;
    TextView heading;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_list);
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.sellers_list);
        heading = findViewById(R.id.headtitle);
        searchView = findViewById(R.id.searchview);
        tokenValue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        typeofactivity = getIntent().getStringExtra("fromactivity");
        selectprodid = getIntent().getStringExtra("selproductid");
        shopAddres = getIntent().getStringExtra("shopaddress");
        shopName = getIntent().getStringExtra("shopname");

        if (typeofactivity.equals("details")) {
            heading.setText("Dealers");
            getSellerList();
        } else {
            heading.setText("All Dealers");
            getAllShops();
        }

        heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getAllShops() {
        progressBar.setVisibility(View.VISIBLE);
        String url_link = Apis.getallshops;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = jsonObjectData.getJSONArray("allShops");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject address = jsonArray.getJSONObject(i);
                        String shopid = address.getString("shop_id");
                        String shopuserid = address.getString("shop_user_id");
                        String shopname = address.getString("shop_name");
                        String shopcity = address.getString("shop_city");
                        String shopstate = address.getString("state_name");
                        String shopcontry = address.getString("country_name");
                        String shopphoneno = address.getString("shop_phone");
                        String shoppincode = address.getString("shop_postalcode");

                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                        allebikesModelClass.setProductName(shopname);
                        allebikesModelClass.setProductid(shopid);
                        allebikesModelClass.setShopphone(shopphoneno);
                        allebikesModelClass.setPrice(shopcity + " " + shopstate + " " + shopcontry + " - " + shoppincode);
                        allebikesModelClass.setTypeLayout(4);
                        allebikesModelClassesList.add(allebikesModelClass);
                    }

                    allebikesAdapter = new AllebikesAdapter(SellersListActivity.this, allebikesModelClassesList);
                    recyclerView.setAdapter(allebikesAdapter);
                    GridLayoutManager linearLayoutManager = new GridLayoutManager(SellersListActivity.this, 2);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    allebikesAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            allebikesAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });

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


    public void getSellerList() {
        progressBar.setVisibility(View.VISIBLE);
        String url_link = Apis.sellerslist + selectprodid;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    JSONObject jsonObjectProduct = jsonObjectData.getJSONObject("product");
                    JSONArray jsonArray = jsonObjectProduct.getJSONArray("moreSellersArr");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject address = jsonArray.getJSONObject(i);
                        String sellerprodid = address.getString("selprod_id");
                        String sellingprice = address.getString("selprod_price");
                        String codavail = address.getString("selprod_cod_enabled");
                        String sellershopname = address.getString("shop_name");
                        String sellerlatitude = address.getString("shop_latitude");
                        String sellerlongitude = address.getString("shop_longitude");
                        String sellerphoneno = address.getString("shop_phone");
                        String sellercity = address.getString("shop_city");
                        String sellerstate = address.getString("shop_state_name");
                        String sellercountry = address.getString("shop_country_name");
                        SellerListModel sellerListModel = new SellerListModel();
                        sellerListModel.setSelproductid(sellerprodid);
                        sellerListModel.setSellerPrice(sellingprice);
                        sellerListModel.setSellerCod(codavail);
                        sellerListModel.setSellersname(sellershopname);
                        sellerListModel.setSellerslatitude(sellerlatitude);
                        sellerListModel.setSellerslongitude(sellerlongitude);
                        sellerListModel.setSellersphoneno(sellerphoneno);
                        sellerListModel.setSellersaddress(sellercity + " " + sellerstate + " " + sellercountry);
                        sellerListModelList.add(sellerListModel);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SellersListActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    sellerListAdapter = new SellerListAdapter(SellersListActivity.this, sellerListModelList);
                    recyclerView.setAdapter(sellerListAdapter);
                    sellerListAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            sellerListAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });

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
