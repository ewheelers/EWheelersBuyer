package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.ewheelers.eWheelersBuyers.Adapters.AddCartServiceAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.CartListingAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.OrderDetailsAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.CartListClass;
import com.ewheelers.eWheelersBuyers.ModelClass.OrderDetailModelclass;
import com.ewheelers.eWheelersBuyers.ModelClass.ServiceProvidersClass;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrdersDetails extends AppCompatActivity implements View.OnClickListener {
    String tokenValue, orderId, vehcleno;
    TextView billingAddress, shippingAddress, headerTxt, myorders, homebutton;
    List<OrderDetailModelclass> orderDetailModelclassList = new ArrayList<>();
    OrderDetailsAdapter orderDetailsAdapter;
    RecyclerView recyclerView;
    TextView orderBilltext;
   /* String jsonData,carListAsString;
    List<CartListClass> cartListClassList = new ArrayList<>();
    AddCartServiceAdapter cartListingAdapter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_details);
        tokenValue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        orderId = getIntent().getStringExtra("orderid");
        vehcleno = getIntent().getStringExtra("vehicleno");
        //jsonData = getIntent().getStringExtra("jsondata");
        //carListAsString = getIntent().getStringExtra("cartdata");
        orderBilltext = findViewById(R.id.orderbilladdress);
        headerTxt = findViewById(R.id.header);
        billingAddress = findViewById(R.id.biladd);
        shippingAddress = findViewById(R.id.ordershippingaddress);
        recyclerView = findViewById(R.id.orderdetails_list);
        myorders = findViewById(R.id.myorders);
        homebutton = findViewById(R.id.homebtn);
        headerTxt.setText("Order Id: " + orderId);

        homebutton.setOnClickListener(this);
        myorders.setOnClickListener(this);

        getOrderDetails();

      /*  if(carListAsString==null){
            getOrderDetails();
        }else {
           *//* Gson gson = new Gson();
            Type type = new TypeToken<List<ServiceProvidersClass>>() {}.getType();
            cartListClassList = gson.fromJson(carListAsString, type);*//*
            shippingAddress.setText(carListAsString);
            try {
                JSONArray jsonArray = new JSONArray(carListAsString);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }*/

    }

    public void getOrderDetails() {
        orderDetailModelclassList.clear();
        String url_link = Apis.vieworderdetails + orderId;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                        JSONObject jsonObjectOrderdetails = jsonObjectdata.getJSONObject("orderDetail");
                        JSONObject jsonobjectbillingaddress = jsonObjectOrderdetails.getJSONObject("billingAddress");
                        String bname = jsonobjectbillingaddress.getString("oua_name");
                        String badd1 = jsonobjectbillingaddress.getString("oua_address1");
                        String badd2 = jsonobjectbillingaddress.getString("oua_address2");
                        String bcity = jsonobjectbillingaddress.getString("oua_city");
                        String bstate = jsonobjectbillingaddress.getString("oua_state");
                        String bcontry = jsonobjectbillingaddress.getString("oua_country");
                        String bzip = jsonobjectbillingaddress.getString("oua_zip");
                        String bphone = jsonobjectbillingaddress.getString("oua_phone");
                        billingAddress.setText(bname + "\n" + badd1 + badd2 + "\n" + bcity + ", " + bstate + ", " + bcontry + " - " + bzip + "\n" + bphone);
                        JSONObject jsonobjectshippingaddress = jsonObjectOrderdetails.getJSONObject("shippingAddress");
                        if (jsonobjectshippingaddress.length()==0){
                            orderBilltext.setVisibility(View.GONE);
                            shippingAddress.setText("Vehicle Number : "+vehcleno);
                        }else {
                            String sname = jsonobjectshippingaddress.getString("oua_name");
                            String sadd1 = jsonobjectshippingaddress.getString("oua_address1");
                            String sadd2 = jsonobjectshippingaddress.getString("oua_address2");
                            String scity = jsonobjectshippingaddress.getString("oua_city");
                            String sstate = jsonobjectshippingaddress.getString("oua_state");
                            String scontry = jsonobjectshippingaddress.getString("oua_country");
                            String szip = jsonobjectshippingaddress.getString("oua_zip");
                            String sphone = jsonobjectshippingaddress.getString("oua_phone");
                            orderBilltext.setVisibility(View.VISIBLE);
                            shippingAddress.setText(sname + "\n" + sadd1 + sadd2 + "\n" + scity + ", " + sstate + ", " + scontry + " - " + szip + "\n" + sphone);
                        }
                        JSONArray jsonarraydetail = jsonObjectdata.getJSONArray("childOrderDetail");
                        for (int i = 0; i < jsonarraydetail.length(); i++) {
                            JSONObject jsonObjectorder = jsonarraydetail.getJSONObject(i);
                            String orderid = jsonObjectorder.getString("op_order_id");
                            String orderinvoiceno = jsonObjectorder.getString("op_invoice_number");
                            String orderdate = jsonObjectorder.getString("order_date_added");
                            String ordername = jsonObjectorder.getString("op_product_name");
                            String ordertitle = jsonObjectorder.getString("op_selprod_title");
                            String orderseloptions = jsonObjectorder.getString("op_selprod_options");
                            String orderbrandname = jsonObjectorder.getString("op_brand_name");
                            String ordershopname = jsonObjectorder.getString("op_shop_name");
                            String orderimage = jsonObjectorder.getString("product_image_url");
                            OrderDetailModelclass orderDetailModelclass = new OrderDetailModelclass();
                            orderDetailModelclass.setOrdername(ordername);
                            orderDetailModelclass.setOrdertitle(ordertitle);
                            orderDetailModelclass.setOrderoptions(orderseloptions);
                            orderDetailModelclass.setOrderbrandname(orderbrandname);
                            orderDetailModelclass.setOrdershopname(ordershopname);
                            orderDetailModelclass.setOrderimageurl(orderimage);
                            orderDetailModelclass.setOrderid(orderid);
                            orderDetailModelclass.setOrderinvoice(orderinvoiceno);
                            orderDetailModelclass.setOrderdate(orderdate);

                            orderDetailModelclassList.add(orderDetailModelclass);

                            JSONArray jsonArray = jsonObjectorder.getJSONArray("priceDetail");


                            JSONObject jsonObjectAmount = jsonObjectorder.getJSONObject("totalAmount");
                            String key = jsonObjectAmount.getString("key");
                            String amount = jsonObjectAmount.getString("value");
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyOrdersDetails.this, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        orderDetailsAdapter = new OrderDetailsAdapter(MyOrdersDetails.this, orderDetailModelclassList);
                        recyclerView.setAdapter(orderDetailsAdapter);
                        orderDetailsAdapter.notifyDataSetChanged();
                        //linear
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
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                return data3;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myorders:
                onBackPressed();
                /*Intent i = new Intent(MyOrdersDetails.this, MyOrdersActivity.class);
                startActivity(i);
                finish();*/
                break;
            case R.id.homebtn:
                Intent i = new Intent(MyOrdersDetails.this, NavAppBarActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
}
