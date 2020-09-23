package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.MyOrdersAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.QrListAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.MyOrdersModel;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ewheelers.eWheelersBuyers.SessionStorage.tokenvalue;

public class QRPasses extends AppCompatActivity {
    RecyclerView recyclerViewQrlist;
    private JSONObject jsonObjectServicetype;
    List<MyOrdersModel> myOrdersModelList = new ArrayList<>();
    QrListAdapter qrListAdapter;
    String tokenValue;
    LinearLayout linearLayout;
    TextView empty_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrpasses);
        recyclerViewQrlist = findViewById(R.id.qr_pass_list);
        linearLayout = findViewById(R.id.progresslayout);
        empty_view = findViewById(R.id.emptyView);
        tokenValue = new SessionStorage().getStrings(this, tokenvalue);
        getOtherServices();
    }

    private void getOtherServices() {
        linearLayout.setVisibility(View.VISIBLE);
        String url_link = Apis.chipfilters + "4";
        final RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String messg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("sellerProductAttribute");
                        List<String> keysset = new ArrayList<>();
                        Iterator iterat = jsonObject2.keys();
                        while (iterat.hasNext()) {
                            String keyOfkey = (String) iterat.next();
                            keysset.add(keyOfkey);
                            if (keyOfkey.equals("Service Type")) {
                                try {
                                    jsonObjectServicetype = jsonObject2.getJSONObject(keyOfkey);
                                    Iterator iterator = jsonObjectServicetype.keys();
                                    while (iterator.hasNext()) {
                                        String key = (String) iterator.next();
                                        String value = jsonObjectServicetype.getString(key);
                                        if (value.equals("Parking")) {
                                            getOrderslist("", key);
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    } else {
                        Toast.makeText(QRPasses.this, messg, Toast.LENGTH_SHORT).show();
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
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    private void getOrderslist(String s, String key) {
        String url_link = Apis.orderslist;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        JSONArray jsonArrayorders = jsonObjectData.getJSONArray("orders");
                        for (int i = 0; i < jsonArrayorders.length(); i++) {
                            JSONObject jsonObjectOrder = jsonArrayorders.getJSONObject(i);
                            String orderid = jsonObjectOrder.getString("order_id");
                            String order_date_added = jsonObjectOrder.getString("order_date_added");
                            String order_net_amount = jsonObjectOrder.getString("order_net_amount");
                            String op_selprod_title = jsonObjectOrder.getString("op_selprod_title");
                            String op_product_name = jsonObjectOrder.getString("op_product_name");
                            String op_selprod_options = jsonObjectOrder.getString("op_selprod_options");
                            String op_brand_name = jsonObjectOrder.getString("op_brand_name");
                            String op_qty = jsonObjectOrder.getString("op_qty");
                            String opId = jsonObjectOrder.getString("op_id");
                            String orderstatus_name = jsonObjectOrder.getString("orderstatus_name");
                            String product_image_url = jsonObjectOrder.getString("product_image_url");
                            if (orderstatus_name.equals("Payment Confirmed")) {
                                MyOrdersModel myOrdersModel = new MyOrdersModel();
                                myOrdersModel.setOrder_id(orderid);
                                myOrdersModel.setOrderdateadded(order_date_added);
                                myOrdersModel.setOrdernetamount(order_net_amount);
                                myOrdersModel.setOp_selprod_title(op_selprod_title);
                                myOrdersModel.setOp_product_name(op_product_name);
                                myOrdersModel.setOp_selprod_options(op_selprod_options);
                                myOrdersModel.setOrderstatus_name(orderstatus_name);
                                myOrdersModel.setProduct_image_url(product_image_url);
                                myOrdersModel.setOp_qty(op_qty);
                                myOrdersModel.setOp_id(opId);
                                generateBarCode(myOrdersModel);
                            }

                        }

                        // Log.i("orederslist",myOrdersModelList.toString());

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
                data3.put("keyword", s);
                data3.put("options", key);
                return data3;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    private void generateBarCode(MyOrdersModel myOrdersModel) {
        List<MyOrdersModel> stringsOrders = new ArrayList<>();
        stringsOrders.add(myOrdersModel);
        String Login_url = "https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl=" + myOrdersModel.getOp_id();
        final ImageRequest imageRequest = new ImageRequest(Login_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                getExcelList(response, myOrdersModel);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QRPasses.this, "Some Thing Goes Wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });

        imageRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(QRPasses.this).addToRequestQueue(imageRequest);
    }

    private void getExcelList(Bitmap responseQr, MyOrdersModel myOrdersModel) {
        String url_link = "https://script.google.com/macros/s/AKfycbxxYKFslwo8bfMOFDNTgo3DMCKpjrcecu5Dyl4lRJ1E7I2MugM/exec?action=getItems";
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.startsWith("<")){
                    linearLayout.setVisibility(View.GONE);
                    empty_view.setVisibility(View.VISIBLE);
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectdata = jsonArray.getJSONObject(i);
                            String orderid = jsonObjectdata.getString("orderid");
                            String vehno = jsonObjectdata.getString("vehno");
                            String vehmodel = jsonObjectdata.getString("vehmodel");
                            String timing = jsonObjectdata.getString("timing");
                            String address = jsonObjectdata.getString("address");
                            if (orderid.equals(myOrdersModel.getOrder_id())) {
                                myOrdersModel.setVehiclemodel(vehmodel);
                                myOrdersModel.setVehicleno(vehno);
                                myOrdersModel.setTimings(timing);
                                myOrdersModel.setStatAddress(address);
                                myOrdersModel.setBitmap(responseQr);
                                myOrdersModelList.add(myOrdersModel);
                            }
                        }
                        empty_view.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                        recyclerViewQrlist.setVisibility(View.VISIBLE);
                        qrListAdapter = new QrListAdapter(QRPasses.this, myOrdersModelList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QRPasses.this, RecyclerView.VERTICAL, false);
                        recyclerViewQrlist.setLayoutManager(linearLayoutManager);
                        recyclerViewQrlist.setAdapter(qrListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    public void QRPass(Bitmap bitmap, String order_id, String text) {
        Dialog fulldialog = new Dialog(QRPasses.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View sheetView = getLayoutInflater().inflate(R.layout.qrcode_layout, null);
        ImageView closeImg = sheetView.findViewById(R.id.closeimg);
        ImageView qrImage = sheetView.findViewById(R.id.qr_image);
        TextView textView = sheetView.findViewById(R.id.textView);
        fulldialog.setContentView(sheetView);
        textView.setText(text + "\n\nOrder Id # " + order_id + "\n\nPlace QR scanner on this QR code get Access.");
        qrImage.setImageBitmap(bitmap);
        fulldialog.show();
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fulldialog.dismiss();
            }
        });
    }


}
