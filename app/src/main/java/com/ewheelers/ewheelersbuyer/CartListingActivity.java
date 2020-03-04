package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaCas;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.CartListingAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.CartListClass;
import com.ewheelers.ewheelersbuyer.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartListingActivity extends AppCompatActivity {
    RecyclerView cartListing;
    CartListingAdapter cartListingAdapter;
    List<CartListClass> cartListClassList = new ArrayList<>();
    String tokenvalue;
    String optionname, optionvalue_name;
    private int mStatusCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_listing);
        cartListing = findViewById(R.id.cart_listing);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        Toast.makeText(this, "token val: " + tokenvalue, Toast.LENGTH_SHORT).show();
        Log.i("token_val:",tokenvalue);
        cartListing();
    }

    public void cartListing() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.cartListing;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    Log.i("responsecartlist:",status+msg+mStatusCode);
                    Toast.makeText(CartListingActivity.this, "message:" +status + msg, Toast.LENGTH_SHORT).show();
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    String cartcount = dataJsonObject.getString("cartItemsCount");
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
                        JSONArray jsonArrayOptions = jsonObjectListdata.getJSONArray("options");
                        for (int j = 0; j < jsonArrayOptions.length(); j++) {
                            JSONObject jsonObjectoptionList = jsonArrayOptions.getJSONObject(j);
                            optionname = jsonObjectoptionList.getString("option_name");
                            optionvalue_name = jsonObjectoptionList.getString("optionvalue_name");
                        }
                        JSONObject jsonObjectSelleraddressArray = jsonObjectListdata.getJSONObject("seller_address");
                        String shopname = jsonObjectSelleraddressArray.getString("shop_name");

                        CartListClass cartListClass = new CartListClass();
                        cartListClass.setBrandname(brandname);
                        cartListClass.setImageurl(imageurl);
                        cartListClass.setProductName(productname);
                        cartListClass.setProductPrice(price);
                        cartListClass.setMinimuborderqty(minOrderquantity);
                        cartListClass.setShopname(shopname);
                        cartListClass.setProductOption(optionname);
                        cartListClassList.add(cartListClass);
                        Log.i("cartlistdata:",cartListClassList.toString());
                    }
                    cartListingAdapter = new CartListingAdapter(CartListingActivity.this, cartListClassList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartListingActivity.this, RecyclerView.VERTICAL, false);
                    cartListing.setLayoutManager(linearLayoutManager);
                    cartListing.setAdapter(cartListingAdapter);

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
                params.put("Content-Type", "application/json");
                params.put("HTTP_X_TOKEN", tokenvalue);
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
        queue.add(stringRequest);
    }
}
