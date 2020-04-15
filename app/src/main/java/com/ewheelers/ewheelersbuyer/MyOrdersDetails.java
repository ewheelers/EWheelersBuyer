package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.MyOrdersAdapter;
import com.ewheelers.ewheelersbuyer.Adapters.OrderDetailsAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.MyOrdersModel;
import com.ewheelers.ewheelersbuyer.ModelClass.OrderDetailModelclass;
import com.ewheelers.ewheelersbuyer.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrdersDetails extends AppCompatActivity {
String tokenValue,orderId;
TextView billingAddress,shippingAddress;
List<OrderDetailModelclass> orderDetailModelclassList = new ArrayList<>();
OrderDetailsAdapter orderDetailsAdapter;
RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_details);
        tokenValue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        orderId = getIntent().getStringExtra("orderid");
        billingAddress = findViewById(R.id.biladd);
        shippingAddress = findViewById(R.id.ordershippingaddress);
        recyclerView = findViewById(R.id.orderdetails_list);
        getOrderDetails();
    }

    public void getOrderDetails(){
        orderDetailModelclassList.clear();
        String url_link = Apis.vieworderdetails+orderId;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equals("1")){
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
                        billingAddress.setText(bname+"\n"+badd1+"\n"+badd2+"\n"+bcity+", "+bstate+", "+bcontry+" - "+bzip+"\n"+bphone);
                        JSONObject jsonobjectshippingaddress = jsonObjectOrderdetails.getJSONObject("shippingAddress");
                        String sname = jsonobjectshippingaddress.getString("oua_name");
                        String sadd1 = jsonobjectshippingaddress.getString("oua_address1");
                        String sadd2 = jsonobjectshippingaddress.getString("oua_address2");
                        String scity = jsonobjectshippingaddress.getString("oua_city");
                        String sstate = jsonobjectshippingaddress.getString("oua_state");
                        String scontry = jsonobjectshippingaddress.getString("oua_country");
                        String szip = jsonobjectshippingaddress.getString("oua_zip");
                        String sphone = jsonobjectshippingaddress.getString("oua_phone");
                        shippingAddress.setText(sname+"\n"+sadd1+"\n"+sadd2+"\n"+scity+", "+sstate+", "+scontry+" - "+szip+"\n"+sphone);

                        JSONArray jsonarraydetail = jsonObjectdata.getJSONArray("childOrderDetail");
                        for(int i=0;i<jsonarraydetail.length();i++){
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

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyOrdersDetails.this,RecyclerView.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        orderDetailsAdapter = new OrderDetailsAdapter(MyOrdersDetails.this,orderDetailModelclassList);
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
}
