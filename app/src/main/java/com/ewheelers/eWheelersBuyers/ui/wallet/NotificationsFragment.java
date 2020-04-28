package com.ewheelers.eWheelersBuyers.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SessionStorage;
import com.ewheelers.eWheelersBuyers.ViewStatementsActivity;
import com.ewheelers.eWheelersBuyers.Volley.Apis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationsFragment extends Fragment implements View.OnClickListener{
    String tokenValue;
    private NotificationsViewModel notificationsViewModel;
    TextView rewardPoint,rewardValue,creditbalance;
    LinearLayout linearLayoutReward,linearCredits,linearOffers;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        tokenValue = new SessionStorage().getStrings(getActivity(), SessionStorage.tokenvalue);
        rewardPoint = root.findViewById(R.id.reward_points);
        rewardValue = root.findViewById(R.id.reward_value);
        linearLayoutReward = root.findViewById(R.id.rewardLayout);
        linearCredits = root.findViewById(R.id.mycredits);
        linearOffers = root.findViewById(R.id.myoffers);
        creditbalance = root.findViewById(R.id.creditbal);
        linearLayoutReward.setOnClickListener(this);
        linearCredits.setOnClickListener(this);
        linearOffers.setOnClickListener(this);

        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });
        getRewardsPonits();
        getCredits();
        return root;
    }

    public void getRewardsPonits(){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                        rewardPoint.setText(balance);
                        rewardValue.setText(convertedValue);

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
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);

    }

    public void getCredits(){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                        String creditbalnace = jsonObjectdata.getString("userTotalWalletBalance");
                        creditbalance.setText("\u20B9 "+creditbalnace);

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
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rewardLayout:
                Intent i = new Intent(getActivity(), ViewStatementsActivity.class);
                i.putExtra("type","rewards");
                startActivity(i);
                break;
            case R.id.mycredits:
                i = new Intent(getActivity(), ViewStatementsActivity.class);
                i.putExtra("type","credits");
                startActivity(i);
                break;
            case R.id.myoffers:
                i = new Intent(getActivity(), ViewStatementsActivity.class);
                i.putExtra("type","myoffers");
                startActivity(i);
                break;
        }
    }
}