package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.ewheelers.eWheelersBuyers.Adapters.AddCartServiceAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.CouponsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.PricedetailAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.ServiceCouponsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.StatementsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.SummaryAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.CartListClass;
import com.ewheelers.eWheelersBuyers.ModelClass.PriceDetailsClass;
import com.ewheelers.eWheelersBuyers.ModelClass.PromoCodesModel;
import com.ewheelers.eWheelersBuyers.ModelClass.StatementsModel;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

public class AddCartfromServices extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    String product_id, timeIs, tokenvalue, vehicleModel, vehicleNo,stationaddress,productname,stationname,timing;
    ProgressBar progressBar;
    TextView ordercount;
    List<CartListClass> cartListClassList = new ArrayList<>();
    List<CartListClass> cartListQrcodeIs = new ArrayList<>();

    //private SummaryAdapter cartListingAdapter;
    List<PriceDetailsClass> priceDetailsClasses = new ArrayList<>();
    RecyclerView pricelist;
    PricedetailAdapter pricedetailAdapter;
    RecyclerView recyclerViewlist;
    ArrayList<String> options;
    String optionname, optionvalue_name;
    String net_amount;
    TextView netamount;
    private AddCartServiceAdapter cartListingAdapter;
    ImageView have_coupon;
    TextView couponsTxt, promo_result;
    List<PromoCodesModel> promoCodesModels = new ArrayList<>();
    ServiceCouponsAdapter couponsAdapter;
    RecyclerView recyclerViewCoupons;
    ImageView imageViewclose;
    SwipeRefreshLayout swipeRefreshLayout;
    Button contiune;
    Dialog fulldialog;
    TextView empty_view;
    ProgressBar progressBarCopons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cartfrom_services);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        product_id = getIntent().getStringExtra("productid");
        timeIs = getIntent().getStringExtra("tottime");
        vehicleNo = getIntent().getStringExtra("vehicleno");
        vehicleModel = getIntent().getStringExtra("vehiclemodel");
        productname = getIntent().getStringExtra("productname");
        stationaddress = getIntent().getStringExtra("stationaddress");
        stationname = getIntent().getStringExtra("stationname");
        timing = getIntent().getStringExtra("timings");
        swipeRefreshLayout = findViewById(R.id.swiprefresh);
        promo_result = findViewById(R.id.promo_result);
        contiune = findViewById(R.id.payment);
        progressBar = findViewById(R.id.progress);
        ordercount = findViewById(R.id.ordercount);
        recyclerViewlist = findViewById(R.id.recycler_id);
        netamount = findViewById(R.id.amount);
        pricelist = findViewById(R.id.price_list);
//        recyclerViewCoupons = findViewById(R.id.couponlist);
        have_coupon = findViewById(R.id.have_coupons);
        couponsTxt = findViewById(R.id.applycoupons);
        contiune.setOnClickListener(this);
        have_coupon.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        cartListing();
                                    }
                                }
        );
        // addTocart(product_id);

    }

    public String splitString(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }

       /* System.out.println(alpha);
        System.out.println(num);
        System.out.println(special);*/
        return String.valueOf(alpha);
    }

    public void cartListing() {
        swipeRefreshLayout.setRefreshing(true);

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
                        cartListClass.setTiming(timing);
                        cartListClass.setStationname(stationname);
                        cartListClass.setStationAddress(stationaddress);
                        cartListClass.setVehicleno(vehicleNo);
                        cartListClass.setVehiclemodel(vehicleModel);
                        cartListClass.setProductid(selprodid);
                        cartListClass.setBrandname(brandname);
                        cartListClass.setImageurl(imageurl);
                        cartListClass.setProductName(productname);
                        cartListClass.setProductPrice(price);
                        cartListClass.setProduct_qty(quantity + " " + splitString(timeIs));
                        //cartListClass.setProduct_qty(timeIs);
                        cartListClass.setShopname(shopname);
                        cartListClass.setSellerAddress(shopname + "\n" + shopcontact_name + "\n" + shopautocomplete + " " + shop_address_line_1 + " " + shop_address_line_2 + " \nPhone : " + shop_phone);

                        if (options.isEmpty()) {
                            cartListClass.setOptions(null);
                            cartListClass.setType(0);
                        } else {
                            cartListClass.setOptions(options);
                            cartListClass.setType(1);
                        }
                        cartListClass.setKeyvalue(key);
                        cartListClassList.add(cartListClass);

                        CartListClass cartListClassQrcode = new CartListClass();
                        cartListClassQrcode.setTiming(timing);
                        cartListClassQrcode.setStationname(stationname);
                        cartListClassQrcode.setStationAddress(stationaddress);
                        cartListClassQrcode.setVehicleno(vehicleNo);
                        cartListClassQrcode.setVehiclemodel(vehicleModel);
                        cartListClassQrcode.setProductName(productname);
                        cartListClassQrcode.setProduct_qty(quantity + " " + splitString(timeIs));
                        cartListQrcodeIs.add(cartListClassQrcode);
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

                    if (cartListClassList.isEmpty()) {
                        swipeRefreshLayout.setRefreshing(false);
                        onBackPressed();
                    } else {
                        swipeRefreshLayout.setRefreshing(false);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        pricelist.setLayoutManager(linearLayoutManager);
                        pricedetailAdapter = new PricedetailAdapter(AddCartfromServices.this, priceDetailsClasses);
                        pricelist.setAdapter(pricedetailAdapter);
                        pricedetailAdapter.notifyDataSetChanged();

                        cartListingAdapter = new AddCartServiceAdapter(AddCartfromServices.this, cartListClassList);
                        LinearLayoutManager linearLayoutManagersummary = new LinearLayoutManager(AddCartfromServices.this, RecyclerView.VERTICAL, false);
                        recyclerViewlist.setLayoutManager(linearLayoutManagersummary);
                        recyclerViewlist.setAdapter(cartListingAdapter);
                        cartListingAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
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
            case R.id.have_coupons:

                fulldialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                View sheetView = getLayoutInflater().inflate(R.layout.coupons_alert_layout, null);
                fulldialog.setCancelable(false);
                recyclerViewCoupons = sheetView.findViewById(R.id.couponlist);
                imageViewclose = sheetView.findViewById(R.id.close_img);
                progressBarCopons = sheetView.findViewById(R.id.progresscopons);
                empty_view = sheetView.findViewById(R.id.emptyview);
                imageViewclose.setOnClickListener(this);
                fulldialog.setContentView(sheetView);
                fulldialog.show();
                showCoupons(recyclerViewCoupons);

                break;
            case R.id.close_img:
                fulldialog.dismiss();
                break;
            case R.id.payment:
                Gson gson = new Gson();
                String jsonCars = gson.toJson(cartListQrcodeIs);
                Intent in = new Intent(getApplicationContext(), StartPaymentActivity.class);
                in.putExtra("netamount", net_amount);
                in.putExtra("cartdata", jsonCars);
                startActivity(in);
                break;
        }
    }


    private void showCoupons(RecyclerView recyclerView) {
        progressBarCopons.setVisibility(View.VISIBLE);
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
                    //Toast.makeText(AddCartfromServices.this, msg, Toast.LENGTH_SHORT).show();
                    if(status.equals("1")) {
                        progressBarCopons.setVisibility(GONE);
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObjectData.getJSONArray("couponsList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String coupon = jsonObject1.getString("coupon_code");
                            String coupondescription = jsonObject1.getString("coupon_description");
                            String startdate = jsonObject1.getString("coupon_start_date");
                            String enddate = jsonObject1.getString("coupon_end_date");
                            String imageurl = jsonObject1.getString("offerImage");
                            String coupontitle = jsonObject1.getString("coupon_title");
                            String minimumval = jsonObject1.getString("coupon_min_order_value");
                            String maxdiscount = jsonObject1.getString("coupon_max_discount_value");
                            PromoCodesModel promoCodesModel = new PromoCodesModel();
                            promoCodesModel.setPromoCode(coupon);
                            promoCodesModel.setDescription(coupondescription);
                            promoCodesModel.setStartdate(startdate);
                            promoCodesModel.setEnddate(enddate);
                            promoCodesModel.setTitle(coupontitle);
                            promoCodesModel.setImagelur(imageurl);
                            promoCodesModel.setMinimumval(minimumval);
                            promoCodesModel.setMaxval(maxdiscount);
                            promoCodesModels.add(promoCodesModel);
                        }
                        if (promoCodesModels.isEmpty()) {
                            empty_view.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(GONE);
                        } else {
                            empty_view.setVisibility(GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddCartfromServices.this, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            couponsAdapter = new ServiceCouponsAdapter(AddCartfromServices.this, promoCodesModels);
                            recyclerView.setAdapter(couponsAdapter);
                            couponsAdapter.notifyDataSetChanged();
                        }
                    }else {
                        progressBarCopons.setVisibility(GONE);
                        empty_view.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(GONE);
                        empty_view.setText(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarCopons.setVisibility(GONE);
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
        String tokenvalue = new SessionStorage().getStrings(AddCartfromServices.this, SessionStorage.tokenvalue);
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
                                promo_result.setText("Discount Applied");
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
                data3.put("coupon_code", couponsTxt.getText().toString());
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }


    public void setCoupon(String promoCode) {
        couponsTxt.setText(promoCode);
        fulldialog.dismiss();
        if(!couponsTxt.getText().toString().isEmpty()){
            ApplyPromocode();
        }
    }

    @Override
    public void onRefresh() {
        cartListing();
    }
}
