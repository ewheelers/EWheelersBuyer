package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.renderscript.Double2;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.AddonsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.CategoriesFilterAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.HrsAdpater;
import com.ewheelers.eWheelersBuyers.Adapters.PaymentGatewayAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.ProductdetailsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.SPProductsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.ServiceOptionsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.SpinnerFiltersAdapter;
import com.ewheelers.eWheelersBuyers.Fragments.PolicyFragment;
import com.ewheelers.eWheelersBuyers.Interface.ItemClickListener;
import com.ewheelers.eWheelersBuyers.ModelClass.AddonsClass;
import com.ewheelers.eWheelersBuyers.ModelClass.Comparemodelclass;
import com.ewheelers.eWheelersBuyers.ModelClass.FilterListClass;
import com.ewheelers.eWheelersBuyers.ModelClass.OptionValues;
import com.ewheelers.eWheelersBuyers.ModelClass.PaymentGatewaysModel;
import com.ewheelers.eWheelersBuyers.ModelClass.ProductDetails;
import com.ewheelers.eWheelersBuyers.ModelClass.ProductSpecifications;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static android.view.View.GONE;

public class ChargeDetailPage extends AppCompatActivity implements View.OnClickListener {
    ArrayList<ProductDetails> optionselect = new ArrayList<>();
    List<ProductDetails> optionsList = new ArrayList<>();
    String optionname1;
    JSONObject selectedOptionValues;
    String optionvalue_id = "";
    String optionlistid = "";
    ArrayList<OptionValues> spinnerlist = new ArrayList<>();
    String value = "";
    ServiceOptionsAdapter productdetailsAdapter2;

    String product_id;
    TextView brand_name, brand_address;
    BottomSheetDialog mBottomSheetDialog2;
    Dialog mBottomSheetDialog3;
    ImageView backimg;
    /* RecyclerView recyclerView;
     TextView netamount, descript;
     Button confirm;*/
    Button ok_btn;
    //Spinner spinone, spintwo;
    List<PaymentGatewaysModel> paymentGatewaysModelsList = new ArrayList<>();
    PaymentGatewayAdapter paymentGatewayAdapter;
    Button minus, plus;
    int i = 1;
    TextView hours;
    TextView txtview;
    Button stopcharge;
    private static CountDownTimer countDownTimer;
    ProgressBar progressBarcircle;
    RecyclerView recyclerViewSpinners;
    SpinnerFiltersAdapter spinnerFiltersAdapter;
    NetworkImageView networkImageViewdialog;
    Button buttonapplyCoupon;
    String tokenvalue, product_name, uaName, stationaddress, service_provider, distanceIs, productAMount, hubid;
    TextView textViewAmount, perunitcharge;
    String productprice, selproductid;
    TextView netamount, productName, hrsTitle, noteTxt, select_time, tot_hrs, totalAmount, choose_coupons;
    String typeOfhrs;
    RecyclerView recyclerViewhrs;
    HrsAdpater hrsAdpater;
    ProgressBar progressBar;
    int noOfqtyasTime;
    EditText vehicle_no, vehicle_model;
    LinearLayout linearLayout_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_detail_page);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        product_id = getIntent().getStringExtra("productid");
        product_name = getIntent().getStringExtra("productname");
        uaName = getIntent().getStringExtra("uaname");
        stationaddress = getIntent().getStringExtra("stationaddress");
        service_provider = getIntent().getStringExtra("provider");
        distanceIs = getIntent().getStringExtra("distance");
        productAMount = getIntent().getStringExtra("productamount");
        hubid = getIntent().getStringExtra("uaid");
        //typeOfhrs = findViewById(R.id.type_of_hours);
        // choose_coupons = findViewById(R.id.choosecoupons);
        linearLayout_no = findViewById(R.id.vehicleno_layout);
        vehicle_model = findViewById(R.id.vehicleModel);
        vehicle_no = findViewById(R.id.vehicleno);
        progressBar = findViewById(R.id.progress);
        totalAmount = findViewById(R.id.tot_amount);
        tot_hrs = findViewById(R.id.totalhrs);
        select_time = findViewById(R.id.selecttime);
        noteTxt = findViewById(R.id.note);
        recyclerViewhrs = findViewById(R.id.hrs_list);
        productName = findViewById(R.id.productname);
        hrsTitle = findViewById(R.id.hours_title);
        perunitcharge = findViewById(R.id.perunit);
        textViewAmount = findViewById(R.id.amount);
        //buttonapplyCoupon = findViewById(R.id.applycoupon);
        //buttonapplyCoupon.setOnClickListener(this);
        recyclerViewSpinners = findViewById(R.id.options_recyclerview);
        hours = findViewById(R.id.hours);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        backimg = findViewById(R.id.back);
        brand_name = findViewById(R.id.brandname);
        brand_address = findViewById(R.id.brandaddress);
        ok_btn = findViewById(R.id.okbtn);
        ok_btn.setOnClickListener(this);
        backimg.setOnClickListener(this);
        minus.setOnClickListener(this);
        plus.setOnClickListener(this);

        mBottomSheetDialog2 = new BottomSheetDialog(this);
        mBottomSheetDialog3 = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        View sheetView = getLayoutInflater().inflate(R.layout.bottom_payment_layout, null);
        mBottomSheetDialog2.setCancelable(false);
        RecyclerView recyclerView = sheetView.findViewById(R.id.payment_methods_list);
        netamount = sheetView.findViewById(R.id.net_amount);
        TextView descript = sheetView.findViewById(R.id.descript);
        Button confirm = sheetView.findViewById(R.id.confirm_order);
        descript.setOnClickListener(this);
        confirm.setOnClickListener(this);
        mBottomSheetDialog2.setContentView(sheetView);

        sheetView = getLayoutInflater().inflate(R.layout.count_layout, null);
        mBottomSheetDialog3.setCancelable(false);
        txtview = sheetView.findViewById(R.id.timer);
        stopcharge = sheetView.findViewById(R.id.stop);
        progressBarcircle = sheetView.findViewById(R.id.progressBarCircle);
        stopcharge.setOnClickListener(this);
        mBottomSheetDialog3.setContentView(sheetView);

        brand_name.setText(uaName + distanceIs);
        brand_address.setText(stationaddress);
        productName.setText(product_name);
        if (service_provider.equals("Parking")) {
            //hrsTitle.setText("Select Time");
            if (product_name.contains("Cycle")) {
                linearLayout_no.setVisibility(GONE);
            }
            if (product_name.contains("Hourly")) {
                //typeOfhrs ="hour(s)";
                select_time.setText("Select Hours");
                noteTxt.setText("Note : select daily Parking for more than 7 Hours");
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
                hrsAdpater = new HrsAdpater(this, strings());
                recyclerViewhrs.setLayoutManager(gridLayoutManager);
                recyclerViewhrs.setAdapter(hrsAdpater);

            } else if (product_name.contains("Daily")) {
                //typeOfhrs = "day(s)";
                select_time.setText("Select Days");
                noteTxt.setText("Note : select Weekly Parking for more than 6 Days");
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
                hrsAdpater = new HrsAdpater(this, stringsDays());
                recyclerViewhrs.setLayoutManager(gridLayoutManager);
                recyclerViewhrs.setAdapter(hrsAdpater);

            } else if (product_name.contains("Weekly")) {
                //typeOfhrs = "day(s)";
                select_time.setText("Select Weeks");
                noteTxt.setText("Note : select Weekly Parking for more than 6 Days");
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
                hrsAdpater = new HrsAdpater(this, stringsWeek());
                recyclerViewhrs.setLayoutManager(gridLayoutManager);
                recyclerViewhrs.setAdapter(hrsAdpater);
            } else if (product_name.contains("Month")) {
                //typeOfhrs = "day(s)";
                select_time.setText("Selected Month");
                //noteTxt.setText("Note : select Weekly Parking for more than 6 Days");
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
                hrsAdpater = new HrsAdpater(this, stringsMonth());
                recyclerViewhrs.setLayoutManager(gridLayoutManager);
                recyclerViewhrs.setAdapter(hrsAdpater);
            }

        }
        //Toast.makeText(this, "provider:"+service_provider, Toast.LENGTH_SHORT).show();
        //getProductDetails(product_id);

    }


    public List<String> strings() {
        List<String> strings = new ArrayList<>();
        strings.add("1 H");
        strings.add("2 H");
        strings.add("3 H");
        strings.add("4 H");
        strings.add("5 H");
        strings.add("6 H");
        strings.add("7 H");
        return strings;
    }

    public List<String> stringsDays() {
        List<String> strings = new ArrayList<>();
        strings.add("1 D");
        strings.add("2 D");
        strings.add("3 D");
        strings.add("4 D");
        strings.add("5 D");
        strings.add("6 D");
        return strings;
    }

    public List<String> stringsWeek() {
        List<String> strings = new ArrayList<>();
        strings.add("1 W");
        strings.add("2 W");
        strings.add("3 W");
        strings.add("4 W");
        return strings;
    }

    public List<String> stringsMonth() {
        List<String> strings = new ArrayList<>();
        strings.add("1 M");
        return strings;
    }


    public void getProductDetails(String productid) {
        optionsList.clear();
        optionselect.clear();
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.productdetails + productid;
        Log.i("url", serverurl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    String cartItemCount = dataJsonObject.getString("cartItemsCount");

                    JSONArray jsonArrayDataOptions = dataJsonObject.getJSONArray("optionRows");
                    for (int optionarray = 0; optionarray < jsonArrayDataOptions.length(); optionarray++) {
                        JSONObject jsonObjectOptionTitle = jsonArrayDataOptions.getJSONObject(optionarray);
                        String optionid = jsonObjectOptionTitle.getString("option_id");
                        String optioniscolor = jsonObjectOptionTitle.getString("option_is_color");
                        optionname1 = jsonObjectOptionTitle.getString("option_name");

                        JSONArray jsonArrayOptionValue = jsonObjectOptionTitle.getJSONArray("values");

                        ArrayList<OptionValues> optionValueList = new ArrayList<OptionValues>();

                        for (int k = 0; k < jsonArrayOptionValue.length(); k++) {
                            JSONObject option_value = jsonArrayOptionValue.getJSONObject(k);
                            String optionUrlvalue = option_value.getString("optionUrlValue");
                            String optionvaluename = option_value.getString("optionvalue_name");
                            optionlistid = option_value.getString("option_id");
                            optionvalue_id = option_value.getString("optionvalue_id");

                            OptionValues optionValues = new OptionValues();
                            optionValues.setOptionUrlValue(optionUrlvalue);
                            optionValues.setOptionValuenames(optionvaluename);

                            optionValues.setOptionId(optionlistid);
                            optionValues.setOptionvalueid(optionvalue_id);

                            optionValueList.add(optionValues);
                            spinnerlist = optionValueList;


                        }

                        ProductDetails productDetailsoptions = new ProductDetails();
                        productDetailsoptions.setOptionName(optionname1);
                        productDetailsoptions.setOptionid(optionid);
                        productDetailsoptions.setOptionValuesArrayList(spinnerlist);
                        productDetailsoptions.setTypeoflayout(0);
                        // productDetailsoptions.setMap(map);
                        optionsList.add(productDetailsoptions);

                    }


                    JSONObject jsonObjectProduct = dataJsonObject.getJSONObject("product");
                    JSONObject jsonObjectdata = jsonObjectProduct.getJSONObject("data");
                    selproductid = jsonObjectdata.getString("selprod_id");
                    String productname = jsonObjectdata.getString("product_name");
                    productprice = jsonObjectdata.getString("selprod_price");
                    String productmodel = jsonObjectdata.getString("product_model");
                    String productdescription = jsonObjectdata.getString("product_description");
                    String brand_Id = jsonObjectdata.getString("brand_id");
                    String brand_Name = jsonObjectdata.getString("brand_name");
                    String brand_Shortdesc = jsonObjectdata.getString("brand_short_description");
                    String offerstobuyer = jsonObjectdata.getString("selprodComments");
                    textViewAmount.setText("\u20B9 " + productprice);
                    //perunitcharge.setText("( \u20B9 "+productprice+ " / hour )");
                    netamount.setText(textViewAmount.getText().toString());

                    selectedOptionValues = jsonObjectdata.getJSONObject("selectedOptionValues");
                    HashMap<String, String> map = new HashMap<String, String>();
                    Iterator iter = selectedOptionValues.keys();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        value = selectedOptionValues.getString(key);
                        map.put(key, value);
                        Log.e("selected options", "value: " + value);
                        ProductDetails productDetailSelect = new ProductDetails();
                        productDetailSelect.setOptionselectid(value);
                        optionselect.add(productDetailSelect);
                    }

                    JSONObject jsonObjectpolicies = dataJsonObject.getJSONObject("shop");
                    String paymetnpolicy = jsonObjectpolicies.getString("shop_payment_policy");
                    String deliverpolicy = jsonObjectpolicies.getString("shop_delivery_policy");
                    String refundpolicy = jsonObjectpolicies.getString("shop_refund_policy");
                    String shopname = jsonObjectpolicies.getString("shop_name");
                    String countryname = jsonObjectpolicies.getString("shop_country_name");
                    String statename = jsonObjectpolicies.getString("shop_state_name");
                    String city = jsonObjectpolicies.getString("shop_city");


                    LinearLayoutManager gridLayoutManager = new LinearLayoutManager(ChargeDetailPage.this, RecyclerView.VERTICAL, false);
                    recyclerViewSpinners.setLayoutManager(gridLayoutManager);
                    productdetailsAdapter2 = new ServiceOptionsAdapter(ChargeDetailPage.this, optionsList);
                    recyclerViewSpinners.setAdapter(productdetailsAdapter2);
                    productdetailsAdapter2.notifyDataSetChanged();


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


    public void setTimeofHrs(int numberInHr) {
        totalAmount = findViewById(R.id.tot_amount);
        textViewAmount = findViewById(R.id.amount);
        tot_hrs = findViewById(R.id.totalhrs);
        if (product_name.contains("Hourly")) {
            tot_hrs.setText(numberInHr + " Hour(s)");
            double amount = Double.parseDouble(String.valueOf(numberInHr)) * Double.parseDouble(productAMount);
            textViewAmount.setText("\u20B9 " + amount);
            totalAmount.setText("\u20B9 " + amount);
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            cal.add(Calendar.HOUR_OF_DAY, +numberInHr);
            String strDateFormat = "dd-MM-yyyy hh:mm aa";
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat(strDateFormat);
            perunitcharge.setText("Parking Time from - " + sdf.format(date) + " To " + sdf.format(cal.getTime()));
            noOfqtyasTime = numberInHr;
        } else if (product_name.contains("Daily")) {
            tot_hrs.setText(numberInHr + " Day(s)");
            double amount = Double.parseDouble(String.valueOf(numberInHr)) * Double.parseDouble(productAMount);
            textViewAmount.setText("\u20B9 " + amount);
            totalAmount.setText("\u20B9 " + amount);
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            cal.add(Calendar.DATE, +numberInHr);
            String strDateFormat = "dd-MM-yyyy hh:mm aa";
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat(strDateFormat);
            perunitcharge.setText("Parking Time from - " + sdf.format(date) + " To " + sdf.format(cal.getTime()));
            noOfqtyasTime = numberInHr;
        } else if (product_name.contains("Weekly")) {
            tot_hrs.setText(numberInHr + " Week(s)");
            double amount = Double.parseDouble(String.valueOf(numberInHr)) * Double.parseDouble(productAMount);
            textViewAmount.setText("\u20B9 " + amount);
            totalAmount.setText("\u20B9 " + amount);
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            cal.add(Calendar.WEEK_OF_YEAR, +numberInHr);
            String strDateFormat = "dd-MM-yyyy hh:mm aa";
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat(strDateFormat);
            perunitcharge.setText("Parking Time from - " + sdf.format(date) + " To " + sdf.format(cal.getTime()));
            noOfqtyasTime = numberInHr;

        } else if ((product_name.contains("Month"))) {
            tot_hrs.setText(numberInHr + " Month(s)");
            double amount = Double.parseDouble(String.valueOf(numberInHr)) * Double.parseDouble(productAMount);
            textViewAmount.setText("\u20B9 " + amount);
            totalAmount.setText("\u20B9 " + amount);
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            cal.add(Calendar.MONTH, +numberInHr);
            String strDateFormat = "dd-MM-yyyy hh:mm aa";
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat(strDateFormat);
            perunitcharge.setText("Parking Time from - " + sdf.format(date) + " To " + sdf.format(cal.getTime()));
            noOfqtyasTime = numberInHr;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /* case R.id.choosecoupons:
             *//* Intent intent = new Intent(ChargeDetailPage.this, AddCartfromServices.class);
                intent.putExtra("productid",selproductid);
                startActivity(intent);*//*
                break;*/
            case R.id.stop:
                AlertDialog alert = new AlertDialog.Builder(this)
                        .create();
                alert.setTitle("Stop ! ");
                alert.setMessage("Do you like to Stop ?");
                alert.setCancelable(false);
                alert.setIcon(getResources().getDrawable(R.drawable.ic_closetest));
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                        mBottomSheetDialog3.dismiss();
                        mBottomSheetDialog2.dismiss();
                        stopCountdown();
                    }
                });
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                    }
                });
                alert.show();
                break;
            case R.id.minus:
                if (i > 1)
                    i--;
                hours.setText(i + typeOfhrs);
                textViewAmount.setText("\u20B9 " + i * Integer.parseInt(productprice));
                netamount.setText(textViewAmount.getText().toString());
                break;
            case R.id.plus:
                i++;
                hours.setText(i + typeOfhrs);
                textViewAmount.setText("\u20B9 " + i * Integer.parseInt(productprice));
                netamount.setText(textViewAmount.getText().toString());
                break;
            case R.id.confirm_order:
                showNoFlashmsg(i);
                break;
            case R.id.descript:
                mBottomSheetDialog2.dismiss();
                break;
            case R.id.okbtn:
                //mBottomSheetDialog2.show();
                /*Intent inte = new Intent(ChargeDetailPage.this,AddCartfromServices.class);
                startActivities(inte);*/
                if(linearLayout_no.getVisibility()==View.VISIBLE){
                    if (vehicle_no.getText().toString().isEmpty() || vehicle_model.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Enter Vehicle details", Toast.LENGTH_SHORT).show();
                    } else {
                        clearCart(product_id, noOfqtyasTime);
                    }
                }else {
                    clearCart(product_id, noOfqtyasTime);
                }

                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.home:
                finish();
                break;
            /*case R.id.proceed:
                mBottomSheetDialog.dismiss();
                break;*/
           /* case R.id.cancel:
                onBackPressed();
                break;*/
           /* case R.id.start:
                if (countDownTimer == null) {
                    String minutes = String.valueOf(i*60);
                    String getMinutes = minutes;//Get minutes from edittexf
                    //Check validation over edittext
                    if (!getMinutes.equals("") && getMinutes.length() > 0) {
                        int noOfMinutes = Integer.parseInt(getMinutes) * 60 * 1000;//Convert minutes into milliseconds
                        startTimer(noOfMinutes);//start countdown
                    }
                }
                else {
                    //Else stop timer and change text
                    stopCountdown();
                }
                break;*/
        }
    }

    public void clearCart(String product_id, int noOfqtyasTime) {
        progressBar.setVisibility(View.VISIBLE);

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
                                progressBar.setVisibility(GONE);
                                addTocart(product_id, noOfqtyasTime, hubid);
                            } else {
                                Toast.makeText(ChargeDetailPage.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(GONE);

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
        VolleySingleton.getInstance(ChargeDetailPage.this).addToRequestQueue(strRequest);

    }

    public void addTocart(String productid, int qtyasTime, String addressid) {
        ok_btn.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        String Login_url = Apis.addtocart;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");

                            if (getStatus.equals("1")) {
                                progressBar.setVisibility(GONE);
                                ok_btn.setVisibility(View.VISIBLE);

                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                Intent intent = new Intent(ChargeDetailPage.this, AddCartfromServices.class);
                                intent.putExtra("productid", selproductid);
                                intent.putExtra("tottime", tot_hrs.getText().toString());
                                intent.putExtra("vehicleno", vehicle_no.getText().toString());
                                intent.putExtra("vehiclemodel", vehicle_model.getText().toString());
                                intent.putExtra("stationaddress", stationaddress);
                                intent.putExtra("productname", product_name);
                                intent.putExtra("stationname", uaName);
                                intent.putExtra("timings", perunitcharge.getText().toString());
                                startActivity(intent);
                                /*Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout), message, Snackbar.LENGTH_LONG);
                                mySnackbar.show();*/
                            } else {
                                progressBar.setVisibility(GONE);
                                ok_btn.setVisibility(View.VISIBLE);

                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout), message, Snackbar.LENGTH_LONG);
                                mySnackbar.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(GONE);
                ok_btn.setVisibility(View.VISIBLE);
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json");
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();

                data3.put("selprod_id", productid);
                data3.put("quantity", String.valueOf(qtyasTime));
                data3.put("station_address", addressid);
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    public void showNoFlashmsg(int i) {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Success ! ");
        alert.setMessage("Payment done");
        alert.setCancelable(false);
        alert.setIcon(getResources().getDrawable(R.drawable.ic_check_circle_black_48dp));
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
                mBottomSheetDialog3.show();
                if (countDownTimer == null) {
                    String minutes = String.valueOf(i * 60);
                    String getMinutes = minutes;//Get minutes from edittexf
                    //Check validation over edittext
                    if (!getMinutes.equals("") && getMinutes.length() > 0) {
                        int noOfMinutes = Integer.parseInt(getMinutes) * 60 * 1000;//Convert minutes into milliseconds

                        startTimer(noOfMinutes);//start countdown

                    }
                } else {
                    //Else stop timer and change text
                    stopCountdown();
                }
            }
        });
        alert.show();
    }

    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txtview.setText(hms);//set text
                progressBarcircle.setProgress(noOfMinutes);
            }

            public void onFinish() {
                txtview.setText("TIME'S UP!!"); //On finish change timer text
                countDownTimer = null;//set CountDownTimer to null
                progressBarcircle.setProgress(100);
            }
        }.start();

    }


    public ArrayList<ProductDetails> productDetailSelectValues() {
        //Log.i("seperatemethod", String.valueOf(optionselect));
        return optionselect;
    }
}
