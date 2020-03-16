package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.ewheelers.ewheelersbuyer.Adapters.CartListingAdapter;
import com.ewheelers.ewheelersbuyer.Adapters.PricedetailAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.CartListClass;
import com.ewheelers.ewheelersbuyer.ModelClass.PriceDetailsClass;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartSummaryActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    TextView netamount, sellerAddress, walletBalance, customerAddress, walletDetails, applyCode, promoResult;
    String tokenvalue;
    EditText editTextPromo;
    LinearLayout promoLayout, linear_layout, apply_layout;
    Button removeButton, pay;
    List<PriceDetailsClass> priceDetailsClasses = new ArrayList<>();
    RecyclerView pricelist;
    PricedetailAdapter pricedetailAdapter;
    String net_amount;
    TextView changeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_summary);
        netamount = findViewById(R.id.amount);
        sellerAddress = findViewById(R.id.seller_address);
        walletBalance = findViewById(R.id.walletBal);
        customerAddress = findViewById(R.id.customer_address);
        walletDetails = findViewById(R.id.wallet_details);
        editTextPromo = findViewById(R.id.promo_code);
        applyCode = findViewById(R.id.apply_code);
        promoResult = findViewById(R.id.promo_result);
        promoLayout = findViewById(R.id.promoLayout);
        linear_layout = findViewById(R.id.linear_layout);
        removeButton = findViewById(R.id.remove_promocode);
        pricelist = findViewById(R.id.price_list);
        apply_layout = findViewById(R.id.apply_promo_layout);
        pay = findViewById(R.id.payment);
        changeAddress = findViewById(R.id.changeAddress);
        applyCode.setOnClickListener(this);
        removeButton.setOnClickListener(this);
        editTextPromo.addTextChangedListener(this);
        pay.setOnClickListener(this);
        changeAddress.setOnClickListener(this);

        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        walletDetails.setText("Wallet Balance " + "\u20B9 0.00 " + "can be Applied " + getResources().getString(R.string.details));
        //cartListing();

    }

    public void cartListing() {
        priceDetailsClasses.clear();
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.cartListing;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    String wallet_balance = dataJsonObject.getString("displayUserWalletBalance");
                    walletBalance.setText("Wallet Balance " + wallet_balance);
                    String cartcount = dataJsonObject.getString("cartItemsCount");
                    net_amount = dataJsonObject.getString("orderNetAmount");
                    JSONArray jsonArrayPricedetails = dataJsonObject.getJSONArray("priceDetail");
                    for (int i = 0; i < jsonArrayPricedetails.length(); i++) {
                        JSONObject jsonObjectvalue = jsonArrayPricedetails.getJSONObject(i);
                        String key = jsonObjectvalue.getString("key");
                        String val = jsonObjectvalue.getString("value");
                        PriceDetailsClass priceDetailsClass = new PriceDetailsClass();
                        priceDetailsClass.setKey(key);
                        priceDetailsClass.setValue(val);
                        priceDetailsClasses.add(priceDetailsClass);
                    }
                    JSONObject jsonObjectNet = dataJsonObject.getJSONObject("netPayable");
                    String netpayble = jsonObjectNet.getString("key");
                    String amount = jsonObjectNet.getString("value");
                    netamount.setText(amount);

                    JSONObject jsonObjectBillAddress = dataJsonObject.getJSONObject("cartSelectedBillingAddress");
                    if(jsonObjectBillAddress.toString().equals("{}")){
                        customerAddress.setText("Setup Billing Address");
                        customerAddress.setTextColor(Color.RED);
                    }else {
                        String customername = jsonObjectBillAddress.getString("ua_name");
                        String u_address1 = jsonObjectBillAddress.getString("ua_address1");
                        String ua_address2 = jsonObjectBillAddress.getString("ua_address2");
                        String u_city = jsonObjectBillAddress.getString("ua_city");
                        String u_phone = jsonObjectBillAddress.getString("ua_phone");

                        customerAddress.setText(customername + "," + u_address1 + "," + ua_address2 + "," + u_phone);
                    }
                    JSONArray jsonArrayProduct = dataJsonObject.getJSONArray("products");
                    for (int i = 0; i < jsonArrayProduct.length(); i++) {
                        JSONObject jsonObjectProducts = jsonArrayProduct.getJSONObject(i);
                        JSONObject jsonObjectselleraddress = jsonObjectProducts.getJSONObject("seller_address");
                        String shopcontact_name = jsonObjectselleraddress.getString("shop_contact_person");
                        String shop_city = jsonObjectselleraddress.getString("shop_city");
                        String shop_address_line_1 = jsonObjectselleraddress.getString("shop_address_line_1");
                        String shop_address_line_2 = jsonObjectselleraddress.getString("shop_address_line_2");
                        sellerAddress.setText(shopcontact_name + "," + shop_city + "," + shop_address_line_1 + "," + shop_address_line_2);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    pricelist.setLayoutManager(linearLayoutManager);
                    pricedetailAdapter = new PricedetailAdapter(CartSummaryActivity.this, priceDetailsClasses);
                    pricelist.setAdapter(pricedetailAdapter);
                    pricedetailAdapter.notifyDataSetChanged();


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
            case R.id.apply_code:
                ApplyPromocode();
                break;
            case R.id.remove_promocode:
                RemovePromoCode();
                break;
            case R.id.payment:
                Intent in = new Intent(getApplicationContext(), StartPaymentActivity.class);
                in.putExtra("netamount", net_amount);
               /* in.putExtra("name",);
                in.putExtra("mobile",);
                in.putExtra("Productname",);*/
                startActivity(in);
                break;
            case R.id.changeAddress:
                Intent i = new Intent(getApplicationContext(),SetupBillingAddressActivity.class);
                startActivity(i);
                break;
        }
    }

    private void ApplyPromocode() {
        String tokenvalue = new SessionStorage().getStrings(CartSummaryActivity.this, SessionStorage.tokenvalue);
        String Login_url = Apis.applypromocode;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                apply_layout.setVisibility(View.GONE);
                                promoLayout.setVisibility(View.VISIBLE);
                                promoResult.setText("Coupon '" + editTextPromo.getText().toString() + "' applied");
                                cartListing();
                            } else {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
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
                data3.put("coupon_code", editTextPromo.getText().toString());
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }


    private void RemovePromoCode() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.removepromocode;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        apply_layout.setVisibility(View.VISIBLE);
                        promoLayout.setVisibility(View.GONE);
                        cartListing();
                    } else {
                        Toast.makeText(CartSummaryActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
            public Map<String, String> getParams() {
                return null;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    @Override
    public void onResume() {
        cartListing();
        super.onResume();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        CharSequence charSequence = s.toString();
        if (charSequence.equals(editTextPromo.getEditableText().toString())) {
            if (charSequence.length() == 0) {
                promoLayout.setVisibility(View.GONE);
                promoResult.setText("");
            }
        }
    }
}
