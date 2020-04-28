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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.MyTestDrivesAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.MyTestDriveModel;
import com.ewheelers.eWheelersBuyers.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTestDrivesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recyclerView;
    String tokenvalue;
    List<MyTestDriveModel> myTestDriveModelList = new ArrayList<>();
    MyTestDrivesAdapter myTestDrivesAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView goBack;
    androidx.appcompat.widget.SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test_drives);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiprefresh);
        recyclerView = findViewById(R.id.testdrive_list);
        goBack = findViewById(R.id.goback);
        searchView = findViewById(R.id.searchview);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query!=null) {
                    getAllTestdrives(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // myOrdersAdapter.getFilter().filter(newText);
                if (newText.length() > 1) {
                    getAllTestdrives(newText);
                }else {
                    getAllTestdrives("");
                }
                return false;
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSwipeRefreshLayout.setRefreshing(true);

                                        getAllTestdrives("");
                                    }
                                }
        );
       // getAllTestdrives();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(MyTestDrivesActivity.this,NavAppBarActivity.class);
                startActivity(i);
                finish();*/
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MyTestDrivesActivity.this, NavAppBarActivity.class);
        startActivity(i);
        finish();
    }

    private void getAllTestdrives(String keyword) {
        mSwipeRefreshLayout.setRefreshing(true);
       // myTestDriveModelList.clear();
        String url_link = Apis.alltestdriverequests;
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
                        JSONArray jsonArray = jsonObjectData.getJSONArray("arr_listing");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectarray = jsonArray.getJSONObject(i);
                            String product_name = jsonObjectarray.getString("product_name");
                            String reqstatus = jsonObjectarray.getString("ptdr_status");
                            String requestid = jsonObjectarray.getString("ptdr_id");
                            String dealercontact = jsonObjectarray.getString("ptdr_contact");
                            String dealermsg = jsonObjectarray.getString("ptdr_comments");
                            String rewueston = jsonObjectarray.getString("ptdr_date");
                            String dealrename = jsonObjectarray.getString("sellername");

                            MyTestDriveModel myTestDriveModel = new MyTestDriveModel();
                            myTestDriveModel.setProduct_name(product_name);
                            myTestDriveModel.setPtdr_comments(dealermsg);
                            myTestDriveModel.setPtdr_contact(dealercontact);
                            myTestDriveModel.setPtdr_id(requestid);
                            myTestDriveModel.setPtdr_request_added_on(rewueston);
                            myTestDriveModel.setPtdr_status(reqstatus);
                            myTestDriveModel.setSellername(dealrename);
                            myTestDriveModelList.add(myTestDriveModel);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyTestDrivesActivity.this,RecyclerView.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        myTestDrivesAdapter = new MyTestDrivesAdapter(MyTestDrivesActivity.this,myTestDriveModelList);
                        recyclerView.setAdapter(myTestDrivesAdapter);
                       //myTestDrivesAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        myTestDrivesAdapter.notifyDataSetChanged();
                       /* recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(MyTestDrivesActivity.this, R.anim.layoutanimationleft));
                        recyclerView.getAdapter().notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();*/

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
                mSwipeRefreshLayout.setRefreshing(false);

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
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("status", "-1");
                hashMap.put("keyword", keyword);
                return hashMap;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        getAllTestdrives("");

    }
}
