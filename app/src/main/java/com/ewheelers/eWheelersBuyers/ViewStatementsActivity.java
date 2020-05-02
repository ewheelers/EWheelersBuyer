package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.StatementsAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.StatementsModel;
import com.ewheelers.eWheelersBuyers.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewStatementsActivity extends AppCompatActivity {
String type_statement;
RecyclerView recyclerView;
String tokenValue;
TextView rewardPoints,titleTxt;
List<StatementsModel> statementsModels = new ArrayList<>();
StatementsAdapter statementsAdapter;
TextView textViewempty;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statements);

        tokenValue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        textViewempty = findViewById(R.id.emptyview);
        rewardPoints = findViewById(R.id.reward_points);
        type_statement = getIntent().getStringExtra("type");
        recyclerView = findViewById(R.id.recycler_reward);
        progressBar = findViewById(R.id.progress);
        titleTxt = findViewById(R.id.title);
        if(type_statement.equals("rewards")) {
            titleTxt.setText("Reward Points");
            rewardStatement();
        }
        if(type_statement.equals("credits")) {
            titleTxt.setText("My Credits");
            getCreditStatement();
        }
        if(type_statement.equals("myoffers")) {
            titleTxt.setText("My Offers/Coupons");
            getCoupons();
        }


    }

    private void getCoupons() {
        progressBar.setVisibility(View.VISIBLE);
        statementsModels.clear();
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.offerslisting;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equals("1")){
                        JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                        JSONArray jsonObjectdetail = jsonObjectdata.getJSONArray("offers");
                        for(int i=0;i<jsonObjectdetail.length();i++){
                            JSONObject detailReward = jsonObjectdetail.getJSONObject(i);
                            String offercode = detailReward.getString("coupon_code");
                            String offerminorder = detailReward.getString("coupon_min_order_value");
                            String offerdisvalue = detailReward.getString("coupon_discount_value");
                            String offerexpiryon = detailReward.getString("coupon_end_date");
                            String offercomments = detailReward.getString("coupon_description");
                            String offerimage = detailReward.getString("offerImage");

                            StatementsModel statementsModel = new StatementsModel();
                            statementsModel.setOffercode(offercode);
                            statementsModel.setOffercomment(offercomments);
                            statementsModel.setOfferexpires(offerexpiryon);
                            statementsModel.setOfferimage(offerimage);
                            statementsModel.setOfferminorder(offerminorder);
                            statementsModel.setOffervalue(offerdisvalue);
                            statementsModel.setTypeLayout(2);
                            statementsModels.add(statementsModel);
                        }
                        if(statementsModels.isEmpty()){
                            progressBar.setVisibility(View.GONE);
                            textViewempty.setVisibility(View.VISIBLE);
                        }else {
                            progressBar.setVisibility(View.GONE);
                            textViewempty.setVisibility(View.GONE);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewStatementsActivity.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            statementsAdapter = new StatementsAdapter(ViewStatementsActivity.this, statementsModels);
                            recyclerView.setAdapter(statementsAdapter);
                            statementsAdapter.notifyDataSetChanged();
                        }
                    }else {
                        progressBar.setVisibility(View.GONE);
                        textViewempty.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    private void getCreditStatement() {
        progressBar.setVisibility(View.VISIBLE);
        statementsModels.clear();
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.getcreditpoints;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equals("1")){
                        JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                        String balance = jsonObjectdata.getString("userTotalWalletBalance");
                        rewardPoints.setText("Available Balance - \u20B9 "+balance);

                        JSONArray jsonObjectdetail = jsonObjectdata.getJSONArray("creditsListing");
                        for(int i=0;i<jsonObjectdetail.length();i++){
                            JSONObject detailReward = jsonObjectdetail.getJSONObject(i);
                            String txnid = detailReward.getString("utxn_id");
                            String txndate = detailReward.getString("utxn_date");
                            String credit = detailReward.getString("utxn_credit");
                            String debit = detailReward.getString("utxn_debit");
                            String txncomments = detailReward.getString("utxn_comments");
                            String txnbalance = detailReward.getString("balance");
                            String txnstatus = detailReward.getString("utxn_statusLabel");

                            StatementsModel statementsModel = new StatementsModel();
                            statementsModel.setTxnid(txnid);
                            statementsModel.setComments(txncomments);
                            statementsModel.setDate(txndate);
                            statementsModel.setBalance(txnbalance);
                            statementsModel.setCredit(credit);
                            statementsModel.setDebit(debit);
                            statementsModel.setStatus(txnstatus);
                            statementsModel.setTypeLayout(1);
                            statementsModels.add(statementsModel);
                        }
                        if(statementsModels.isEmpty()){
                            textViewempty.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        }else {
                            progressBar.setVisibility(View.GONE);
                            textViewempty.setVisibility(View.GONE);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewStatementsActivity.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            statementsAdapter = new StatementsAdapter(ViewStatementsActivity.this, statementsModels);
                            recyclerView.setAdapter(statementsAdapter);
                            statementsAdapter.notifyDataSetChanged();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    public void rewardStatement(){
        progressBar.setVisibility(View.VISIBLE);
        statementsModels.clear();
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.getrewardpoints;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equals("1")){
                        JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                        JSONObject jsonObjectPoints = jsonObjectdata.getJSONObject("rewardPointsDetail");
                        String balance = jsonObjectPoints.getString("balance");
                        String convertedValue = jsonObjectPoints.getString("convertedValue");
                        rewardPoints.setText("Current Reward Points ("+balance+") - "+convertedValue);
                        JSONArray jsonObjectdetail = jsonObjectdata.getJSONArray("rewardPointsStatement");
                        for(int i=0;i<jsonObjectdetail.length();i++){
                            JSONObject detailReward = jsonObjectdetail.getJSONObject(i);
                            String points = detailReward.getString("urp_points");
                            String comments = detailReward.getString("urp_comments");
                            String adddate = detailReward.getString("urp_date_added");
                            String expirydate = detailReward.getString("urp_date_expiry");
                            StatementsModel statementsModel = new StatementsModel();
                            statementsModel.setPoints(points);
                            statementsModel.setComment(comments);
                            statementsModel.setAdddate(adddate);
                            statementsModel.setExpiredate(expirydate);
                            statementsModel.setTypeLayout(0);
                            statementsModels.add(statementsModel);
                        }
                        if(statementsModels.isEmpty()){
                            textViewempty.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        }else {
                            progressBar.setVisibility(View.GONE);
                            textViewempty.setVisibility(View.GONE);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewStatementsActivity.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            statementsAdapter = new StatementsAdapter(ViewStatementsActivity.this, statementsModels);
                            recyclerView.setAdapter(statementsAdapter);
                            statementsAdapter.notifyDataSetChanged();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

}
