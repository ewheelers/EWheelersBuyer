package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.CartListingAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.CouponsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.PricedetailAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.SummaryAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.CartListClass;
import com.ewheelers.eWheelersBuyers.ModelClass.PriceDetailsClass;
import com.ewheelers.eWheelersBuyers.ModelClass.PromoCodesModel;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CartSummaryActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    TextView netamount, sellerAddress, customerAddress, applyCode, promoResult;
    String tokenvalue;
    EditText editTextPromo;
    LinearLayout promoLayout, linear_layout, apply_layout;
    Button removeButton, pay;
    List<PriceDetailsClass> priceDetailsClasses = new ArrayList<>();
    RecyclerView pricelist;
    PricedetailAdapter pricedetailAdapter;
    String net_amount;
    TextView changeAddress, customerChangeAddress, customerShippingAddress;
    RecyclerView recyclerViewCoupons;
    Button have_coupon;
    List<PromoCodesModel> promoCodesModels = new ArrayList<>();
    CouponsAdapter couponsAdapter;
    // Button cod_option;

    RecyclerView recyclerViewlist;
    TextView ordercount;
    ArrayList<String> options;
    String optionname, optionvalue_name;
    List<CartListClass> cartListClassList = new ArrayList<>();
    private SummaryAdapter cartListingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_summary);
        ordercount = findViewById(R.id.ordercount);
        recyclerViewlist = findViewById(R.id.recycler_id);
        customerShippingAddress = findViewById(R.id.customer_shipping_address);
        //cod_option = findViewById(R.id.cod);
        netamount = findViewById(R.id.amount);
        sellerAddress = findViewById(R.id.seller_address);
        //walletBalance = findViewById(R.id.walletBal);
        customerAddress = findViewById(R.id.customer_address);
        //walletDetails = findViewById(R.id.wallet_details);
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
        customerChangeAddress = findViewById(R.id.shippingAddressChange);
        have_coupon = findViewById(R.id.have_coupons);
        recyclerViewCoupons = findViewById(R.id.couponlist);
        applyCode.setOnClickListener(this);
        removeButton.setOnClickListener(this);
        editTextPromo.addTextChangedListener(this);
        pay.setOnClickListener(this);
        customerAddress.setOnClickListener(this);
        changeAddress.setOnClickListener(this);
        have_coupon.setOnClickListener(this);
        //cod_option.setOnClickListener(this);
        customerChangeAddress.setOnClickListener(this);
        customerShippingAddress.setOnClickListener(this);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        //walletDetails.setText("Wallet Balance " + "\u20B9 0.00 " + "can be Applied " + getResources().getString(R.string.details));
        //cartListing();
        cartListing();
    }

    public void cartListing() {
        cartListClassList.clear();
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
                    //walletBalance.setText("Wallet Balance " + wallet_balance);
                    String cartcount = dataJsonObject.getString("cartItemsCount");
                    ordercount.setText("Order Summary - " + cartcount + " item(s)");


                    JSONArray jsonArraycartList = dataJsonObject.getJSONArray("products");
                    for (int k = 0; k < jsonArraycartList.length(); k++) {
                        JSONObject jsonObjectListdata = jsonArraycartList.getJSONObject(k);
                        String selprodid = jsonObjectListdata.getString("selprod_id");
                        String imageurl = jsonObjectListdata.getString("product_image_url");
                        String productname = jsonObjectListdata.getString("selprod_title");
                        String key = jsonObjectListdata.getString("key");
                        String quantity = jsonObjectListdata.getString("quantity");
                        String price = jsonObjectListdata.getString("theprice");
                        String brandname = jsonObjectListdata.getString("brand_name");
                        String minOrderquantity = jsonObjectListdata.getString("selprod_min_order_qty");
                        JSONObject jsonObjectSelleraddressArray = jsonObjectListdata.getJSONObject("seller_address");
                        String shopname = jsonObjectSelleraddressArray.getString("shop_name");
                        String shopcontact_name = jsonObjectSelleraddressArray.getString("shop_contact_person");
                        String shop_city = jsonObjectSelleraddressArray.getString("shop_city");
                        String shop_address_line_1 = jsonObjectSelleraddressArray.getString("shop_address_line_1");
                        String shop_address_line_2 = jsonObjectSelleraddressArray.getString("shop_address_line_2");
                        String shopautocomplete = jsonObjectSelleraddressArray.getString("shop_auto_complete");
                        String shop_phone = jsonObjectSelleraddressArray.getString("shop_phone");
                        String deliveryProcess = jsonObjectSelleraddressArray.getString("shop_delivery_policy");
                       // sellerAddress.setText(shopname + "\n" + shopcontact_name + "\n" + shopautocomplete + " " + shop_address_line_1 + " " + shop_address_line_2 + " \nPhone : " + shop_phone);

                        JSONArray jsonArrayOptions = jsonObjectListdata.getJSONArray("options");
                        options = new ArrayList<>();
                        for (int j = 0; j < jsonArrayOptions.length(); j++) {
                            JSONObject jsonObjectoptionList = jsonArrayOptions.getJSONObject(j);
                            optionname = jsonObjectoptionList.getString("option_name");
                            optionvalue_name = jsonObjectoptionList.getString("optionvalue_name");
                            options.add(optionname + " : " + optionvalue_name);
                        }

                        CartListClass cartListClass = new CartListClass();
                        cartListClass.setProductid(selprodid);
                        cartListClass.setBrandname(brandname);
                        cartListClass.setImageurl(imageurl);
                        cartListClass.setProductName(productname);
                        cartListClass.setProductPrice(price);
                        cartListClass.setProduct_qty(quantity);
                        cartListClass.setShopname(shopname);
                        cartListClass.setDeliveryPolicy(deliveryProcess);
                        cartListClass.setSellerAddress(shopname + "\n" + shopcontact_name + "\n" + shopautocomplete + " " + shop_address_line_1 + " " + shop_address_line_2 + " \nPhone : " + shop_phone);

                        if (options.isEmpty()) {
                            cartListClass.setOptions(null);
                            cartListClass.setType(0);
                        } else {
                            cartListClass.setOptions(options);
                            cartListClass.setType(1);
                        }
                        cartListClass.setKeyvalue(key);

                        Iterator iterator = jsonObjectListdata.keys();
                        String rentStartdate = null;
                        String rentEnddate = null;
                        while (iterator.hasNext()){
                            String stratkey = (String) iterator.next();
                            if(stratkey.equals("rentalStartDate")){
                                rentStartdate = jsonObjectListdata.getString("rentalStartDate");
                                cartListClass.setRentStartdate(rentStartdate);
                            }
                            if(stratkey.equals("rentalEndDate")){
                                rentEnddate = jsonObjectListdata.getString("rentalEndDate");
                                cartListClass.setRentEnddate(rentEnddate);
                            }

                        }

                        cartListClassList.add(cartListClass);
                    }


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
                    if (jsonObjectBillAddress.toString().equals("{}")) {
                        customerAddress.setText("Setup Billing Address");
                        customerAddress.setTextColor(Color.RED);
                    } else {
                        String customername = jsonObjectBillAddress.getString("ua_name");
                        String u_address1 = jsonObjectBillAddress.getString("ua_address1");
                        String ua_address2 = jsonObjectBillAddress.getString("ua_address2");
                        String u_city = jsonObjectBillAddress.getString("ua_city");
                        String u_phone = jsonObjectBillAddress.getString("ua_phone");
                        String u_autocomplete = jsonObjectBillAddress.getString("ua_auto_complete");
                        customerAddress.setText(customername + "\n" + u_autocomplete + "\n" + u_address1 + " Phone " + u_phone);
                        customerAddress.setTextColor(Color.BLACK);
                        //customerAddress.setText(customername + "," + u_address1 + "," + ua_address2 + "," + u_city + u_phone);
                    }

                    JSONObject jsonObjectCartShipping = dataJsonObject.getJSONObject("cartSelectedShippingAddress");
                    if (jsonObjectCartShipping.toString().equals("{}")) {
                        customerShippingAddress.setText("Setup Shipping Address");
                        customerShippingAddress.setTextColor(Color.RED);
                    } else {
                        String customername = jsonObjectCartShipping.getString("ua_name");
                        String u_address1 = jsonObjectCartShipping.getString("ua_address1");
                        String ua_address2 = jsonObjectCartShipping.getString("ua_address2");
                        String u_city = jsonObjectCartShipping.getString("ua_city");
                        String u_phone = jsonObjectCartShipping.getString("ua_phone");
                        String u_autocomplete = jsonObjectCartShipping.getString("ua_auto_complete");
                        customerShippingAddress.setText(customername + "\n" + u_autocomplete + "\n" + u_address1 + " Phone " + u_phone);
                        //customerShippingAddress.setText(customername + "," + u_address1 + "," + ua_address2 + "," + u_phone);
                        customerShippingAddress.setTextColor(Color.BLACK);
                    }
                   /* JSONArray jsonArrayProduct = dataJsonObject.getJSONArray("products");
                    for (int i = 0; i < jsonArrayProduct.length(); i++) {
                        JSONObject jsonObjectProducts = jsonArrayProduct.getJSONObject(i);
                        JSONObject jsonObjectselleraddress = jsonObjectProducts.getJSONObject("seller_address");
                        String shopname = jsonObjectselleraddress.getString("shop_name");
                        String shopcontact_name = jsonObjectselleraddress.getString("shop_contact_person");
                        String shop_city = jsonObjectselleraddress.getString("shop_city");
                        String shop_address_line_1 = jsonObjectselleraddress.getString("shop_address_line_1");
                        String shop_address_line_2 = jsonObjectselleraddress.getString("shop_address_line_2");
                        String shopautocomplete = jsonObjectselleraddress.getString("shop_auto_complete");
                        String shop_phone = jsonObjectselleraddress.getString("shop_phone");
                        //sellerAddress.setText(shopcontact_name + "\n" + shop_address_line_1 + " " + shop_address_line_2+"\n"+shop_city);
                        sellerAddress.setText(shopname + "\n" + shopcontact_name + "\n" + shopautocomplete + " " + shop_address_line_1 + " " + shop_address_line_2 + " \nPhone : " + shop_phone);

                    }*/

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    pricelist.setLayoutManager(linearLayoutManager);
                    pricedetailAdapter = new PricedetailAdapter(CartSummaryActivity.this, priceDetailsClasses);
                    pricelist.setAdapter(pricedetailAdapter);
                    pricedetailAdapter.notifyDataSetChanged();

                    cartListingAdapter = new SummaryAdapter(CartSummaryActivity.this, cartListClassList);
                    LinearLayoutManager linearLayoutManagersummary = new LinearLayoutManager(CartSummaryActivity.this, RecyclerView.VERTICAL, false);
                    recyclerViewlist.setLayoutManager(linearLayoutManagersummary);
                    recyclerViewlist.setAdapter(cartListingAdapter);
                    cartListingAdapter.notifyDataSetChanged();

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
            case R.id.shippingAddressChange:
                Intent i = new Intent(getApplicationContext(), SetupBillingAddressActivity.class);
                startActivity(i);
                break;
            case R.id.have_coupons:
                showCoupons();
                break;
            case R.id.apply_code:
                ApplyPromocode();
                break;
            case R.id.remove_promocode:
                RemovePromoCode();
                break;
            case R.id.payment:
                if (customerAddress.getText().toString().equals("Setup Billing Address")) {
                    Toast.makeText(this, "Select Billing Address to continue", Toast.LENGTH_SHORT).show();
                } else if (customerShippingAddress.getText().toString().equals("Setup Shipping Address")) {
                    Toast.makeText(this, "Select Shipping Address to continue", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(getApplicationContext(), StartPaymentActivity.class);
                    in.putExtra("netamount", net_amount);
                    startActivity(in);
                }

                break;
            case R.id.changeAddress:
                i = new Intent(getApplicationContext(), SetupBillingAddressActivity.class);
                startActivity(i);
                break;
            case R.id.customer_address:
                i = new Intent(getApplicationContext(), SetupBillingAddressActivity.class);
                startActivity(i);
                break;
           /* case R.id.cod:
                paymentsummary();
                break;*/
        }
    }

    private void showCoupons() {
        promoCodesModels.clear();
        String url_link = Apis.validcoupons;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(CartSummaryActivity.this, msg, Toast.LENGTH_SHORT).show();
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = jsonObjectData.getJSONArray("couponsList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String coupon = jsonObject1.getString("coupon_code");
                        PromoCodesModel promoCodesModel = new PromoCodesModel();
                        promoCodesModel.setPromoCode(coupon);
                        promoCodesModels.add(promoCodesModel);
                    }
                    GridLayoutManager linearLayoutManager = new GridLayoutManager(CartSummaryActivity.this, 2);
                    recyclerViewCoupons.setLayoutManager(linearLayoutManager);
                    couponsAdapter = new CouponsAdapter(CartSummaryActivity.this, promoCodesModels);
                    recyclerViewCoupons.setAdapter(couponsAdapter);
                    couponsAdapter.notifyDataSetChanged();

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

   /* @Override
    public void onResume() {
        cartListing();
        super.onResume();
    }*/

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

    public void setCoupon(String promoCode) {
        editTextPromo.setText(promoCode);
    }

   /* @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(),CartListingActivity.class);
        startActivity(i);
        finish();
    }*/

}
