package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.MyOrdersAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.MyOrdersModel;
import com.ewheelers.eWheelersBuyers.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrdersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    MyOrdersAdapter myOrdersAdapter;
    List<MyOrdersModel> myOrdersModelList = new ArrayList<>();
    String tokenValue;
    TextView goBack;
    SearchView searchView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout linearLayout;
    Button buttonshopnow;
    String service_optionid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        recyclerView = findViewById(R.id.recycler_orders);
        goBack = findViewById(R.id.goback);
        linearLayout = findViewById(R.id.emptyView);
        buttonshopnow = findViewById(R.id.shop_now);
        searchView = findViewById(R.id.searchview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiprefresh);
        service_optionid = getIntent().getStringExtra("servicetype");
        tokenValue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);
                                         if (service_optionid != null) {
                                             getOrderslist("", service_optionid);
                                         } else {
                                             getOrderslist("", "");
                                         }
                                     }
                                 }
        );

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        buttonshopnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    if (service_optionid != null) {
                        getOrderslist(query, service_optionid);
                    } else {
                        getOrderslist(query, "");
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // myOrdersAdapter.getFilter().filter(newText);
                if (newText.length() > 1) {
                    if (service_optionid != null) {
                        getOrderslist(newText, service_optionid);
                    } else {
                        getOrderslist(newText, "");

                    }
                } else {
                    if (service_optionid != null) {
                        getOrderslist("", service_optionid);
                    } else {
                        getOrderslist("", "");
                    }
                }
                return false;
            }
        });
    }

    public void getOrderslist(String keyword, String serviceTypeid) {
        mSwipeRefreshLayout.setRefreshing(true);
        String url_link = Apis.orderslist;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                myOrdersModelList.clear();

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
                            String orderstatus_name = jsonObjectOrder.getString("orderstatus_name");
                            String product_image_url = jsonObjectOrder.getString("product_image_url");

                            MyOrdersModel myOrdersModel = new MyOrdersModel();
                            myOrdersModel.setOrder_id(orderid);
                            myOrdersModel.setOrderdateadded(order_date_added);
                            myOrdersModel.setOrdernetamount(order_net_amount);
                            myOrdersModel.setOp_selprod_title(op_selprod_title);
                            myOrdersModel.setOp_product_name(op_product_name);
                            myOrdersModel.setOp_selprod_options(op_selprod_options);
                            myOrdersModel.setOrderstatus_name(orderstatus_name);
                            myOrdersModel.setProduct_image_url(product_image_url);
                            myOrdersModelList.add(myOrdersModel);

                        }
                        if (myOrdersModelList.isEmpty()) {
                            linearLayout.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            linearLayout.setVisibility(View.GONE);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyOrdersActivity.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            myOrdersAdapter = new MyOrdersAdapter(MyOrdersActivity.this, myOrdersModelList);
                            recyclerView.setAdapter(myOrdersAdapter);
                    /*recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(MyOrdersActivity.this, R.anim.layoutanimationleft));
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();*/
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        myOrdersAdapter.notifyDataSetChanged();
                    } else {
                        linearLayout.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
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
                data3.put("keyword", keyword);
                data3.put("options", serviceTypeid);
                return data3;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MyOrdersActivity.this, NavAppBarActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onRefresh() {
        if (service_optionid != null) {
            getOrderslist("", service_optionid);
        } else {
            getOrderslist("", "");
        }
    }
}
