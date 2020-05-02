package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.CartListingAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.CartListClass;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartListingActivity extends AppCompatActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    RecyclerView cartListing;
    CartListingAdapter cartListingAdapter;
    List<CartListClass> cartListClassList = new ArrayList<>();
    String tokenvalue;
    String optionname, optionvalue_name;
    private int mStatusCode = 0;
    Button placeOrder,clearcart;
    TextView cartEmpty;
    String addons;
    TextView total, tax, netpayab;
    String rentalprice, rentalsecurity, rentStartdate, rentEnddate;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String produt_id;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_listing);

        linearLayout = findViewById(R.id.total_amount);
        cartListing = findViewById(R.id.cart_listing);
        placeOrder = findViewById(R.id.place_order);
        clearcart = findViewById(R.id.clearcart);
        cartEmpty = findViewById(R.id.emptyview);
        total = findViewById(R.id.subtotal);
        tax = findViewById(R.id.tax);
        netpayab = findViewById(R.id.netpay);
        produt_id = getIntent().getStringExtra("selid");
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiprefresh);
       // Toast.makeText(this, "prod id: " + produt_id, Toast.LENGTH_SHORT).show();

        placeOrder.setOnClickListener(this);
        clearcart.setOnClickListener(this);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
      /*  Toast.makeText(this, "token val: " + tokenvalue, Toast.LENGTH_SHORT).show();
        Log.i("token_val:",tokenvalue);*/
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);
                                         cartListing();
                                     }
                                 }
        );

        //cartListing();

    }

    ArrayList<String> options;

    public void cartListing() {
        mSwipeRefreshLayout.setRefreshing(true);

        cartListClassList.clear();

        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.cartListing;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    Log.i("responsecartlist:", "status = " + status + "message " + msg + "status code" + mStatusCode);
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    String cartcount = dataJsonObject.getString("cartItemsCount");
                    //  String netpayble = dataJsonObject.getString("netPayable");

                    JSONObject jsonObjectSummary = dataJsonObject.getJSONObject("cartSummary");
                    String cartsubtotal = jsonObjectSummary.getString("cartTotal");
                    String cartTex = jsonObjectSummary.getString("cartTaxTotal");
                    total.setText("\u20B9" + cartsubtotal);
                    tax.setText("\u20B9" + cartTex);
                    JSONObject jsonObjectNetpayable = dataJsonObject.getJSONObject("netPayable");
                    String netpayable = jsonObjectNetpayable.getString("value");
                    netpayab.setText(netpayable);


                    JSONArray jsonArraycartList = dataJsonObject.getJSONArray("products");
                    for (int k = 0; k < jsonArraycartList.length(); k++) {
                        JSONObject jsonObjectListdata = jsonArraycartList.getJSONObject(k);
                        String imageurl = jsonObjectListdata.getString("product_image_url");
                        String productname = jsonObjectListdata.getString("selprod_title");
                        String key = jsonObjectListdata.getString("key");
                        String quantity = jsonObjectListdata.getString("quantity");
                        String price = jsonObjectListdata.getString("theprice");
                        String brandname = jsonObjectListdata.getString("brand_name");
                        String minOrderquantity = jsonObjectListdata.getString("selprod_min_order_qty");
                        JSONObject jsonObjectSelleraddressArray = jsonObjectListdata.getJSONObject("seller_address");
                        String shopname = jsonObjectSelleraddressArray.getString("shop_name");

                       /* rentalprice = jsonObjectListdata.getString("sprodata_rental_price");
                        rentalsecurity = jsonObjectListdata.getString("sprodata_rental_security");
                        rentStartdate = jsonObjectListdata.getString("rentalStartDate");
                        rentEnddate = jsonObjectListdata.getString("rentalEndDate");*/

                        JSONArray jsonArrayOptions = jsonObjectListdata.getJSONArray("options");
                        options = new ArrayList<>();
                        for (int j = 0; j < jsonArrayOptions.length(); j++) {
                            JSONObject jsonObjectoptionList = jsonArrayOptions.getJSONObject(j);
                            optionname = jsonObjectoptionList.getString("option_name");
                            optionvalue_name = jsonObjectoptionList.getString("optionvalue_name");
                            options.add(optionname + " : " + optionvalue_name);
                        }

                        CartListClass cartListClass = new CartListClass();
                        cartListClass.setBrandname(brandname);
                        cartListClass.setImageurl(imageurl);
                        cartListClass.setProductName(productname);
                        cartListClass.setProductPrice(price);
                        cartListClass.setProduct_qty(quantity);
                        cartListClass.setShopname(shopname);
                        if (options.isEmpty()) {
                            cartListClass.setOptions(null);
                            cartListClass.setType(0);
                        } else {
                            cartListClass.setOptions(options);
                            cartListClass.setType(1);
                        }
                        cartListClass.setKeyvalue(key);
                        cartListClassList.add(cartListClass);

                    }

                    if (cartListClassList.isEmpty()) {
                        cartEmpty.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        cartEmpty.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        cartListingAdapter = new CartListingAdapter(CartListingActivity.this, cartListClassList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartListingActivity.this, RecyclerView.VERTICAL, false);
                        cartListing.setLayoutManager(linearLayoutManager);
                        cartListing.setAdapter(cartListingAdapter);
                        cartListingAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);

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
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    mStatusCode = response.statusCode;
                }
                assert response != null;
                return super.parseNetworkResponse(response);
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
            case R.id.place_order:
                Intent i = new Intent(getApplicationContext(), CartSummaryActivity.class);
                startActivity(i);
                break;
            case R.id.clearcart:
                clearAllcartItems();
                break;
        }
    }

    private void clearAllcartItems() {
        String tokenvalue = new SessionStorage().getStrings(CartListingActivity.this, SessionStorage.tokenvalue);

        String Login_url = Apis.removecartItems;

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                cartListingAdapter.notifyDataSetChanged();
                                onRefresh();
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
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("key", "all");
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(CartListingActivity.this).addToRequestQueue(strRequest);

    }

  /*  public void restartActivity() {
        onRefresh();
        *//*Intent intent = getIntent();
        finish();
        startActivity(intent);*//*
    }*/

    @Override
    public void onRefresh() {
        cartListing();
    }

    @Override
    public void onBackPressed(){
        if(produt_id==null||produt_id.isEmpty()) {
            Intent i = new Intent(getApplicationContext(), NavAppBarActivity.class);
            startActivity(i);
            finish();
        }else {
            Intent i = new Intent(getApplicationContext(), ProductDetailActivity.class);
            i.putExtra("productid", produt_id);
            startActivity(i);
            finish();
        }
    }
}
