package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.ModelClass.BillingAddress;
import com.ewheelers.ewheelersbuyer.ModelClass.NotificationsAlertModel;
import com.ewheelers.ewheelersbuyer.NavAppBarActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.SetupBillingAddressActivity;
import com.ewheelers.ewheelersbuyer.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.AlertAdapter> {
    Context context;
    List<NotificationsAlertModel> notificationsAlertModels;
    AlertsAdapter alertsAdapter;
    public AlertsAdapter(Context context, List<NotificationsAlertModel> notificationsAlertModels) {
        this.context = context;
        this.notificationsAlertModels = notificationsAlertModels;
        this.alertsAdapter=this;
    }

    @NonNull
    @Override
    public AlertAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alerts_layout, parent, false);
        return new AlertAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertAdapter holder, int position) {
        holder.alerttype.setText(notificationsAlertModels.get(position).getNotificationtype());
        holder.alertbody.setText(notificationsAlertModels.get(position).getNotificationbody());
        holder.alertdate.setText(notificationsAlertModels.get(position).getNotificationdate());
        String read = notificationsAlertModels.get(position).getNotificationread();
        String alertid = notificationsAlertModels.get(position).getNotificationid();
        if (read.equals("1")) {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
            holder.alertbody.setTextColor(Color.BLACK);
            holder.alertdate.setTextColor(Color.BLACK);
            holder.alerttype.setTextColor(Color.BLACK);
        } else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#9C3C34"));
            holder.alertbody.setTextColor(Color.WHITE);
            holder.alertdate.setTextColor(Color.WHITE);
            holder.alerttype.setTextColor(Color.WHITE);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(notificationsAlertModels.get(position).getNotificationbody()).setTitle(notificationsAlertModels.get(position).getNotificationtype())
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String tokenValue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                                String url_link = Apis.markalertread+notificationsAlertModels.get(position).getNotificationid();
                                final RequestQueue queue = Volley.newRequestQueue(context);
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String status = jsonObject.getString("status");
                                            String msg = jsonObject.getString("msg");
                                            if(status.equals("1")){
                                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                                String unread = jsonObjectData.getString("totalUnreadNotificationCount");
                                                new NavAppBarActivity().setcount(unread);
                                                //new NavAppBarActivity().navView.getOrCreateBadge(R.id.navigation_alerts).setNumber(Integer.parseInt(unread));
                                                notificationsAlertModels.remove(notificationsAlertModels.get(position));
                                                alertsAdapter.notifyDataSetChanged();
                                                holder.linearLayout.setBackgroundColor(Color.WHITE);
                                                holder.alertbody.setTextColor(Color.BLACK);
                                                holder.alertdate.setTextColor(Color.BLACK);
                                                holder.alerttype.setTextColor(Color.BLACK);
                                            }else {
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsAlertModels.size();
    }

    public class AlertAdapter extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView alerttype, alertbody, alertdate;

        public AlertAdapter(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.alert_layout);
            alerttype = itemView.findViewById(R.id.alert_type);
            alertbody = itemView.findViewById(R.id.alert_body);
            alertdate = itemView.findViewById(R.id.alert_date);

        }
    }
}
