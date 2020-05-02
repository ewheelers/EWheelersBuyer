package com.ewheelers.eWheelersBuyers.ui.alerts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.eWheelersBuyers.Adapters.AlertsAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.NotificationsAlertModel;
import com.ewheelers.eWheelersBuyers.NavAppBarActivity;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SessionStorage;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {
    RecyclerView recyclerView;
    AlertsAdapter alertsAdapter;
    private DashboardViewModel dashboardViewModel;
    List<NotificationsAlertModel> notificationsAlertModels = new ArrayList<>();
    String tokenvalue,unread;
    ProgressBar progressBar;
    TextView textViewempty;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = root.findViewById(R.id.alerts_list);
        progressBar = root.findViewById(R.id.progressbar);
        textViewempty = root.findViewById(R.id.emptyview);
        tokenvalue = new SessionStorage().getStrings(getActivity(), SessionStorage.tokenvalue);
        alertslist();
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    public void alertslist() {
        progressBar.setVisibility(View.VISIBLE);
        notificationsAlertModels.clear();
        String Login_url = Apis.notificationslist;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                unread = jsonObjectData.getString("totalUnreadNotificationCount");

                                new NavAppBarActivity().setcount(unread);

                                JSONArray jsonArray = jsonObjectData.getJSONArray("notifications");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectalerts = jsonArray.getJSONObject(i);
                                    String notificationid = jsonObjectalerts.getString("unotification_id");
                                    String alertbody = jsonObjectalerts.getString("unotification_body");
                                    String alertdate = jsonObjectalerts.getString("unotification_date");
                                    String alerttype = jsonObjectalerts.getString("unotification_type");
                                    String alertread = jsonObjectalerts.getString("unotification_is_read");
                                    NotificationsAlertModel notificationsAlertModel = new NotificationsAlertModel();
                                    notificationsAlertModel.setNotificationid(notificationid);
                                    notificationsAlertModel.setNotificationbody(alertbody);
                                    notificationsAlertModel.setNotificationdate(alertdate);
                                    notificationsAlertModel.setNotificationtype(alerttype);
                                    notificationsAlertModel.setNotificationread(alertread);
                                    notificationsAlertModels.add(notificationsAlertModel);
                                }
                                if(notificationsAlertModels.isEmpty()){
                                    //Toast.makeText(getActivity(), "No notifications to show", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    textViewempty.setVisibility(View.VISIBLE);
                                }else {
                                    textViewempty.setVisibility(View.GONE);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    alertsAdapter = new AlertsAdapter(getActivity(), notificationsAlertModels);
                                    recyclerView.setAdapter(alertsAdapter);
                                    alertsAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);

                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                textViewempty.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("page", "1");
                data3.put("pagesize", "100");
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(strRequest);
    }

   /* @Override
    public void onResume() {
        alertslist();
        super.onResume();
    }*/

}