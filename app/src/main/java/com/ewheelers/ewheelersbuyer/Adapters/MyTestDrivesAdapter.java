package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.CartListingActivity;
import com.ewheelers.ewheelersbuyer.ModelClass.MyTestDriveModel;
import com.ewheelers.ewheelersbuyer.MyTestDrivesActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTestDrivesAdapter extends RecyclerView.Adapter<MyTestDrivesAdapter.TestDrivesholder> {
    Context context;
    List<MyTestDriveModel> myTestDriveModelList;
    private MyTestDrivesAdapter myTestDrivesAdapter;
    int mStatusCode = 0;

    public MyTestDrivesAdapter(Context context, List<MyTestDriveModel> myTestDriveModelList) {
        this.context = context;
        this.myTestDriveModelList = myTestDriveModelList;
        this.myTestDrivesAdapter = this;

    }

    @NonNull
    @Override
    public TestDrivesholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mytest_drives_layout, parent, false);
        return new TestDrivesholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TestDrivesholder holder, int position) {
        holder.prodname.setText(myTestDriveModelList.get(position).getProduct_name());
        holder.sellername.setText(myTestDriveModelList.get(position).getSellername());
        holder.sellercontact.setText(myTestDriveModelList.get(position).getPtdr_contact());
        holder.requeston.setText(myTestDriveModelList.get(position).getPtdr_request_added_on());
        String status = myTestDriveModelList.get(position).getPtdr_status();
        if (status.equals("0")) {
            holder.viewdrive.setVisibility(View.VISIBLE);
            holder.canceldrive.setVisibility(View.VISIBLE);
            holder.confirmdrive.setVisibility(View.GONE);
            holder.completetxt.setVisibility(View.GONE);
            holder.linearLayoutTrack.setVisibility(View.VISIBLE);

            holder.pendingtxt.setTextColor(Color.parseColor("#9C3C34"));

            holder.comfirmtxt.setTextColor(Color.GRAY);
            holder.canceltxt.setVisibility(View.GONE);
            holder.delivertxt.setTextColor(Color.GRAY);
            holder.accepttxt.setTextColor(Color.GRAY);

        } else if (status.equals("1")) {
            holder.viewdrive.setVisibility(View.VISIBLE);
            holder.canceldrive.setVisibility(View.GONE);
            holder.confirmdrive.setVisibility(View.GONE);
            holder.completetxt.setVisibility(View.GONE);
            holder.linearLayoutTrack.setVisibility(View.VISIBLE);

            holder.pendingtxt.setTextColor(Color.parseColor("#9C3C34"));
            holder.accepttxt.setTextColor(Color.parseColor("#9C3C34"));
            holder.comfirmtxt.setTextColor(Color.parseColor("#9C3C34"));

            //holder.pendingtxt.setTextColor(Color.GRAY);
            holder.canceltxt.setVisibility(View.GONE);
            holder.delivertxt.setTextColor(Color.GRAY);
            //holder.accepttxt.setTextColor(Color.GRAY);
        } else if (status.equals("2")) {
            holder.viewdrive.setVisibility(View.GONE);
            holder.canceldrive.setVisibility(View.GONE);
            holder.confirmdrive.setVisibility(View.GONE);
            holder.completetxt.setVisibility(View.GONE);
            holder.linearLayoutTrack.setVisibility(View.GONE);

           /* holder.completetxt.setText(myTestDriveModelList.get(position).getPtdr_comments());
            holder.completetxt.setTextColor(Color.parseColor("#9C3C34"));*/

            holder.canceltxt.setVisibility(View.VISIBLE);
            holder.cancelUnder.setVisibility(View.VISIBLE);
           /* holder.pendingtxt.setTextColor(Color.GRAY);
            holder.comfirmtxt.setTextColor(Color.GRAY);
            holder.delivertxt.setTextColor(Color.GRAY);
            holder.accepttxt.setTextColor(Color.GRAY);*/
        } else if (status.equals("3")) {
            holder.viewdrive.setVisibility(View.VISIBLE);
            holder.canceldrive.setVisibility(View.GONE);
            holder.confirmdrive.setVisibility(View.GONE);
            holder.completetxt.setVisibility(View.GONE);

            holder.linearLayoutTrack.setVisibility(View.VISIBLE);

            holder.pendingtxt.setTextColor(Color.parseColor("#9C3C34"));
            holder.accepttxt.setTextColor(Color.parseColor("#9C3C34"));
            holder.comfirmtxt.setTextColor(Color.parseColor("#9C3C34"));
            holder.delivertxt.setTextColor(Color.parseColor("#9C3C34"));

            // holder.pendingtxt.setTextColor(Color.GRAY);
            // holder.comfirmtxt.setTextColor(Color.GRAY);
            holder.canceltxt.setVisibility(View.GONE);
            //holder.accepttxt.setTextColor(Color.GRAY);
        } else if (status.equals("4")) {
            holder.viewdrive.setVisibility(View.VISIBLE);
            holder.canceldrive.setVisibility(View.GONE);
            holder.confirmdrive.setVisibility(View.GONE);

            holder.linearLayoutTrack.setVisibility(View.GONE);
            holder.cancelUnder.setVisibility(View.VISIBLE);
            holder.completetxt.setVisibility(View.VISIBLE);
/*
            holder.pendingtxt.setTextColor(Color.GRAY);
            holder.comfirmtxt.setTextColor(Color.GRAY);
            holder.canceltxt.setVisibility(View.GONE);
            holder.delivertxt.setTextColor(Color.GRAY);
            holder.accepttxt.setTextColor(Color.GRAY);*/
        } else if (status.equals("5")) {
            holder.viewdrive.setVisibility(View.VISIBLE);
            holder.canceldrive.setVisibility(View.VISIBLE);
            holder.confirmdrive.setVisibility(View.VISIBLE);
            holder.completetxt.setVisibility(View.GONE);
            holder.linearLayoutTrack.setVisibility(View.VISIBLE);

            holder.pendingtxt.setTextColor(Color.parseColor("#9C3C34"));
            holder.accepttxt.setTextColor(Color.parseColor("#9C3C34"));

            // holder.pendingtxt.setTextColor(Color.GRAY);
            holder.comfirmtxt.setTextColor(Color.GRAY);
            holder.canceltxt.setVisibility(View.GONE);
            holder.delivertxt.setTextColor(Color.GRAY);
        }

        holder.canceldrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(context).inflate(R.layout.cancel_confirmation_layout, viewGroup, false);
                ImageView closeimg = dialogView.findViewById(R.id.close_img);
                Button yesbutton = dialogView.findViewById(R.id.yesbtn);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setCancelable(false);
                closeimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                yesbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tokenvalue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);
                        String url_link = Apis.cancelrequest;
                        final RequestQueue queue = Volley.newRequestQueue(context);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_link, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                /*if (mStatusCode == 200) {
                                    if (context instanceof MyTestDrivesActivity) {
                                        ((MyTestDrivesActivity) context).onRefresh();
                                    }
                                    holder.linearLayoutTrack.setVisibility(View.GONE);
                                    alertDialog.dismiss();
                                }*/

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("msg");
                                    if (status.equals("1")) {
                                        if (context instanceof MyTestDrivesActivity) {
                                            ((MyTestDrivesActivity) context).onRefresh();
                                        }
                                        holder.linearLayoutTrack.setVisibility(View.GONE);
                                        alertDialog.dismiss();
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
                                params.put("X-TOKEN", tokenvalue);
                                return params;
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                if (response != null) {
                                    mStatusCode = response.statusCode;
                                }
                                assert response != null;
                                return super.parseNetworkResponse(response);
                            }

                            @Override
                            public Map<String, String> getParams() {
                                Map<String, String> hashMap = new HashMap<>();
                                hashMap.put("requestId", myTestDriveModelList.get(position).getPtdr_id());
                                return hashMap;
                            }

                        };
                        // Add the realibility on the connection.
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                        queue.add(stringRequest);

                    }
                });

            }
        });

        holder.confirmdrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(context).inflate(R.layout.feedback_layout, viewGroup, false);
                EditText pname = dialogView.findViewById(R.id.feedback_edt);
                Button button = dialogView.findViewById(R.id.submit_cfm);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (pname.getText().toString().isEmpty()) {
                            pname.setError("Give your feedback");
                        } else {

                            String tokenvalue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);
                            String url_link = Apis.changerequeststatus;
                            final RequestQueue queue = Volley.newRequestQueue(context);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_link, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String message = jsonObject.getString("msg");
                                        if (status.equals("1")) {
                                            if (context instanceof MyTestDrivesActivity) {
                                                ((MyTestDrivesActivity) context).onRefresh();
                                            }
                                            alertDialog.dismiss();
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
                                    params.put("X-TOKEN", tokenvalue);
                                    return params;
                                }

                                @Override
                                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                    if (response != null) {
                                        mStatusCode = response.statusCode;
                                    }
                                    assert response != null;
                                    return super.parseNetworkResponse(response);
                                }

                                @Override
                                public Map<String, String> getParams() {
                                    Map<String, String> hashMap = new HashMap<>();
                                    hashMap.put("ptdr_id", myTestDriveModelList.get(position).getPtdr_id());
                                    hashMap.put("ptdr_status", "1");
                                    hashMap.put("ptdr_feedback", pname.getText().toString());
                                    hashMap.put("ptdr_comments", " -- ");
                                    return hashMap;
                                }

                            };
                            // Add the realibility on the connection.
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                            queue.add(stringRequest);
                        }
                    }

                });


            }
        });

        holder.viewdrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tokenvalue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                String url_link = Apis.getdriveinfo + myTestDriveModelList.get(position).getPtdr_id();
                final RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            if (status.equals("1")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONObject jsonArray = jsonObjectData.getJSONObject("data");
                                String product_name = jsonArray.getString("product_name");
                                String reqstatus = jsonArray.getString("ptdr_status");
                                String requestid = jsonArray.getString("ptdr_feedback");
                                String dealercontact = jsonArray.getString("ptdr_contact");
                                String dealermsg = jsonArray.getString("ptdr_comments");
                                String rewueston = jsonArray.getString("ptdr_request_added_on");
                                String dealrename = jsonArray.getString("sellername");
                                String datetime = jsonArray.getString("ptdr_date");
                                String location = jsonArray.getString("ptdr_location");

                                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                                final View dialogView = LayoutInflater.from(context).inflate(R.layout.view_test_drive_layout, viewGroup, false);
                                TextView pname = dialogView.findViewById(R.id.prod_name);
                                TextView dname = dialogView.findViewById(R.id.dealer_name);
                                TextView locate = dialogView.findViewById(R.id.location);
                                TextView cont = dialogView.findViewById(R.id.contact);
                                TextView reqon = dialogView.findViewById(R.id.requestedon);
                                TextView date = dialogView.findViewById(R.id.dateandtime);
                                TextView feedback = dialogView.findViewById(R.id.cust_feedback);
                                TextView statusreq = dialogView.findViewById(R.id.req_status);
                                TextView dmsg = dialogView.findViewById(R.id.dealer_mesg);
                                Button button = dialogView.findViewById(R.id.booknow);
                                ImageButton imageButton = dialogView.findViewById(R.id.closealert);

                                pname.setText(product_name);
                                date.setText(rewueston);
                                dname.setText(dealrename);
                                locate.setText(location);
                                cont.setText(dealercontact);
                                feedback.setText(requestid);
                                dmsg.setText(dealermsg);
                                reqon.setText(datetime);

                                if (reqstatus.equals("0")) {
                                    statusreq.setText("Pending");
                                } else if (reqstatus.equals("1")) {
                                    statusreq.setText("Confirmed");

                                } else if (reqstatus.equals("2")) {
                                    statusreq.setText("Cancelled");

                                } else if (reqstatus.equals("3")) {
                                    statusreq.setText("Delivered");

                                } else if (reqstatus.equals("4")) {
                                    statusreq.setText("Completed");

                                } else if (reqstatus.equals("5")) {
                                    statusreq.setText("Accepted");

                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setView(dialogView);
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                                imageButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });


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
                        params.put("X-TOKEN", tokenvalue);
                        return params;
                    }

                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("status", "-1");
                        return hashMap;
                    }

                };
                // Add the realibility on the connection.
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                queue.add(stringRequest);

            }
        });

        holder.cancelUnder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tokenvalue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                String url_link = Apis.getdriveinfo + myTestDriveModelList.get(position).getPtdr_id();
                final RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            if (status.equals("1")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONObject jsonArray = jsonObjectData.getJSONObject("data");
                                String product_name = jsonArray.getString("product_name");
                                String reqstatus = jsonArray.getString("ptdr_status");
                                String requestid = jsonArray.getString("ptdr_feedback");
                                String dealercontact = jsonArray.getString("ptdr_contact");
                                String dealermsg = jsonArray.getString("ptdr_comments");
                                String rewueston = jsonArray.getString("ptdr_request_added_on");
                                String dealrename = jsonArray.getString("sellername");
                                String datetime = jsonArray.getString("ptdr_date");
                                String location = jsonArray.getString("ptdr_location");

                                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                                final View dialogView = LayoutInflater.from(context).inflate(R.layout.view_test_drive_layout, viewGroup, false);
                                TextView pname = dialogView.findViewById(R.id.prod_name);
                                TextView dname = dialogView.findViewById(R.id.dealer_name);
                                TextView locate = dialogView.findViewById(R.id.location);
                                TextView cont = dialogView.findViewById(R.id.contact);
                                TextView reqon = dialogView.findViewById(R.id.requestedon);
                                TextView date = dialogView.findViewById(R.id.dateandtime);
                                TextView feedback = dialogView.findViewById(R.id.cust_feedback);
                                TextView statusreq = dialogView.findViewById(R.id.req_status);
                                TextView dmsg = dialogView.findViewById(R.id.dealer_mesg);
                                Button button = dialogView.findViewById(R.id.booknow);
                                ImageButton imageButton = dialogView.findViewById(R.id.closealert);
                                pname.setText(product_name);
                                date.setText(rewueston);
                                dname.setText(dealrename);
                                locate.setText(location);
                                cont.setText(dealercontact);
                                feedback.setText(requestid);
                                dmsg.setText(dealermsg);
                                reqon.setText(datetime);

                                if (reqstatus.equals("0")) {
                                    statusreq.setText("Pending");
                                } else if (reqstatus.equals("1")) {
                                    statusreq.setText("Confirmed");

                                } else if (reqstatus.equals("2")) {
                                    statusreq.setText("Cancelled");

                                } else if (reqstatus.equals("3")) {
                                    statusreq.setText("Delivered");

                                } else if (reqstatus.equals("4")) {
                                    statusreq.setText("Completed");

                                } else if (reqstatus.equals("5")) {
                                    statusreq.setText("Accepted");

                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setView(dialogView);
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                alertDialog.setCancelable(false);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                                imageButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });

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
                        params.put("X-TOKEN", tokenvalue);
                        return params;
                    }

                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("status", "-1");
                        return hashMap;
                    }

                };
                // Add the realibility on the connection.
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                queue.add(stringRequest);

            }
        });

    }

    @Override
    public int getItemCount() {
        return myTestDriveModelList.size();
    }

    public class TestDrivesholder extends RecyclerView.ViewHolder {
        TextView prodname, sellername, sellercontact, requeston, pendingtxt, accepttxt, comfirmtxt, delivertxt;
        LinearLayout linearLayoutTrack;
        CardView viewdrive, canceldrive, confirmdrive;
        ImageView canceltxt, completetxt;
        ImageButton cancelUnder;

        public TestDrivesholder(@NonNull View itemView) {
            super(itemView);
            viewdrive = itemView.findViewById(R.id.viewdrive);
            canceldrive = itemView.findViewById(R.id.canceldrive);
            confirmdrive = itemView.findViewById(R.id.confirmdrive);

            linearLayoutTrack = itemView.findViewById(R.id.tracktestlayout);

            prodname = itemView.findViewById(R.id.productname);
            sellername = itemView.findViewById(R.id.dealername);
            sellercontact = itemView.findViewById(R.id.dealercontact);
            requeston = itemView.findViewById(R.id.rewueston);
            pendingtxt = itemView.findViewById(R.id.pending);
            canceltxt = itemView.findViewById(R.id.cancel);
            accepttxt = itemView.findViewById(R.id.accepted);
            comfirmtxt = itemView.findViewById(R.id.Confirm);
            delivertxt = itemView.findViewById(R.id.deleivered);
            completetxt = itemView.findViewById(R.id.complete);
            cancelUnder = itemView.findViewById(R.id.view_under);
        }
    }
}
