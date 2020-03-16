package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaCas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.ewheelers.ewheelersbuyer.Adapters.CartListingAdapter;
import com.ewheelers.ewheelersbuyer.Adapters.ProductdetailsAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.CartListClass;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ewheelers.ewheelersbuyer.Dialogs.ShowAlerts.showfailedDialog;

public class CartListingActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView cartListing;
    CartListingAdapter cartListingAdapter;
    List<CartListClass> cartListClassList = new ArrayList<>();
    String tokenvalue;
    String optionname, optionvalue_name;
    private int mStatusCode = 0;
    Button placeOrder;
    TextView cartEmpty;
    String addons;
    TextView total,tax,netpayab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_listing);
        cartListing = findViewById(R.id.cart_listing);
        placeOrder = findViewById(R.id.place_order);
        cartEmpty = findViewById(R.id.emptyview);
        total = findViewById(R.id.subtotal);
        tax = findViewById(R.id.tax);
        netpayab = findViewById(R.id.netpay);

        placeOrder.setOnClickListener(this);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
      /*  Toast.makeText(this, "token val: " + tokenvalue, Toast.LENGTH_SHORT).show();
        Log.i("token_val:",tokenvalue);*/


        cartListing();


    }

    ArrayList<String> options;

    public void cartListing() {
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
                    total.setText("\u20B9"+cartsubtotal);
                    tax.setText("\u20B9"+cartTex);
                    JSONObject jsonObjectNetpayable= dataJsonObject.getJSONObject("netPayable");
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

                        JSONArray jsonArrayOptions = jsonObjectListdata.getJSONArray("options");
                        options = new ArrayList<>();
                        for (int j = 0; j < jsonArrayOptions.length(); j++) {
                            JSONObject jsonObjectoptionList = jsonArrayOptions.getJSONObject(j);
                            optionname = jsonObjectoptionList.getString("option_name");
                            optionvalue_name = jsonObjectoptionList.getString("optionvalue_name");
                            options.add(optionname + ":" + optionvalue_name);
                        }

                        CartListClass cartListClass = new CartListClass();
                        cartListClass.setBrandname(brandname);
                        cartListClass.setImageurl(imageurl);
                        cartListClass.setProductName(productname);
                        cartListClass.setProductPrice(price);
                        cartListClass.setProduct_qty(quantity);
                        cartListClass.setShopname(shopname);
                       /* cartListClass.setProductOption(optionname);
                        cartListClass.setOptionvalue(optionvalue_name);*/
                        cartListClass.setOptions(options);
                        cartListClass.setKeyvalue(key);
                        cartListClassList.add(cartListClass);
                    }

                    if (cartListClassList.isEmpty()) {
                        cartEmpty.setVisibility(View.VISIBLE);
                    } else {
                        cartEmpty.setVisibility(View.GONE);
                        cartListingAdapter = new CartListingAdapter(CartListingActivity.this, cartListClassList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartListingActivity.this, RecyclerView.VERTICAL, false);
                        cartListing.setLayoutManager(linearLayoutManager);
                        cartListing.setAdapter(cartListingAdapter);
                        cartListingAdapter.notifyDataSetChanged();
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
        }
    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}
