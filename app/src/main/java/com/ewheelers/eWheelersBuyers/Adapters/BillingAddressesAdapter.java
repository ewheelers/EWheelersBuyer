package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.eWheelersBuyers.AddNewAddressActivity;
import com.ewheelers.eWheelersBuyers.Interface.SetDefault;
import com.ewheelers.eWheelersBuyers.ModelClass.BillingAddress;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SessionStorage;
import com.ewheelers.eWheelersBuyers.SetupBillingAddressActivity;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingAddressesAdapter extends RecyclerView.Adapter<BillingAddressesAdapter.BillHolder> {
    Context context;
    List<BillingAddress> billingAddressList;
    BillingAddressesAdapter billingAddressesAdapter;
    int index_row = -1;

    SetDefault deSetDefault;


    public BillingAddressesAdapter(Context context, List<BillingAddress> billingAddressList) {
        this.context = context;
        this.billingAddressList = billingAddressList;
        this.billingAddressesAdapter = this;
    }

    public BillingAddressesAdapter(Context context, List<BillingAddress> billingAddressList, SetDefault deSetDefault) {
        this.context = context;
        this.billingAddressList = billingAddressList;
        this.billingAddressesAdapter = this;
        this.deSetDefault = deSetDefault;
    }

    @NonNull
    @Override
    public BillingAddressesAdapter.BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case BillingAddress.bill:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresscard_layout, parent, false);
                return new BillHolder(v);
            case BillingAddress.ship:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresscard_layout, parent, false);
                return new BillHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BillingAddressesAdapter.BillHolder holder, int position) {

        final int itemType = getItemViewType(position);
        switch (itemType) {
            case BillingAddress.bill:
                holder.label.setText(billingAddressList.get(position).getUa_identifier());
                holder.name.setText(billingAddressList.get(position).getUa_name());
                holder.ad_line1.setText(billingAddressList.get(position).getAutocomplete());
                holder.city.setText(billingAddressList.get(position).getCity_name());
                holder.contry.setText(billingAddressList.get(position).getCountry_name());
                holder.state.setText(billingAddressList.get(position).getState_name());
                holder.pincode.setText(billingAddressList.get(position).getUa_zip());
                holder.mobile.setText(billingAddressList.get(position).getUa_phone());
                String uaid = billingAddressList.get(position).getUa_id();
                holder.deleteAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String tokenValue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                        String Login_url = Apis.deleteaddress;

                        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String getStatus = jsonObject.getString("status");
                                            String message = jsonObject.getString("msg");
                                            if (getStatus.equals("1")) {
                                                Toast.makeText(context, "Deleted:" + message, Toast.LENGTH_SHORT).show();
                                                billingAddressList.remove(billingAddressList.get(position));
                                                billingAddressesAdapter.notifyDataSetChanged();
                                            } else {
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("Main", "Error :" + error.getMessage());
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
                                data3.put("id", billingAddressList.get(position).getUa_id());


                                return data3;

                            }
                        };
                        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                        VolleySingleton.getInstance(context).addToRequestQueue(strRequest);
                    }
                });

                holder.updateAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inte = new Intent(context, AddNewAddressActivity.class);
                        inte.putExtra("uid", billingAddressList.get(position).getUa_id());
                        inte.putExtra("postalcode", billingAddressList.get(position).getUa_zip());
                        inte.putExtra("addlabel", billingAddressList.get(position).getUa_identifier());
                        inte.putExtra("name", billingAddressList.get(position).getUa_name());
                        inte.putExtra("addressline1", billingAddressList.get(position).getUa_address1());
                        inte.putExtra("phone", billingAddressList.get(position).getUa_phone());
                        context.startActivity(inte);
                    }
                });

                holder.chooseaddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        index_row = position;
                        notifyDataSetChanged();
                        if (context instanceof SetupBillingAddressActivity) {
                            ((SetupBillingAddressActivity) context).setUid(billingAddressList.get(position).getUa_id());
                        }

                    }
                });
                if (index_row == position) {
                    holder.chooseaddress.setBackground(context.getResources().getDrawable(R.drawable.button_bg));
                    holder.chooseaddress.setTextColor(Color.WHITE);
                } else {
                    holder.chooseaddress.setBackground(context.getResources().getDrawable(R.drawable.button_bg_redtransperent));
                    holder.chooseaddress.setTextColor(Color.BLACK);
                }

                holder.setdefault.setVisibility(View.VISIBLE);


                holder.setdefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tokenvalue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);
                        String Login_url = Apis.setdefaultaddress;
                        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String getStatus = jsonObject.getString("status");
                                            String message = jsonObject.getString("msg");
                                            if (getStatus.equals("1")) {
                                                deSetDefault.defaultAddress();
                                            } else {
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
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
                            public Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> data3 = new HashMap<String, String>();
                                data3.put("id", billingAddressList.get(position).getUa_id());
                                return data3;

                            }
                        };
                        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                        VolleySingleton.getInstance(context).addToRequestQueue(strRequest);
                    }
                });

                if(billingAddressList.get(position).getUa_is_default().equals("1")){
                    holder.setdefault.setBackground(context.getResources().getDrawable(R.drawable.button_bg));
                    holder.setdefault.setText("Default");
                    holder.setdefault.setTextColor(Color.WHITE);
                }else {
                    holder.setdefault.setBackground(context.getResources().getDrawable(R.drawable.button_bg_transperent));
                    holder.setdefault.setText("Set Default");
                    holder.setdefault.setTextColor(Color.BLACK);
                }



                break;

            case BillingAddress.ship:
                holder.setdefault.setVisibility(View.GONE);

                holder.label.setText(billingAddressList.get(position).getUa_identifier());
                holder.name.setText(billingAddressList.get(position).getUa_name());
                holder.ad_line1.setText(billingAddressList.get(position).getAutocomplete());
                holder.city.setText(billingAddressList.get(position).getCity_name());
                holder.contry.setText(billingAddressList.get(position).getCountry_name());
                holder.state.setText(billingAddressList.get(position).getState_name());
                holder.pincode.setText(billingAddressList.get(position).getUa_zip());
                holder.mobile.setText(billingAddressList.get(position).getUa_phone());
                String uaid2 = billingAddressList.get(position).getUa_id();

                holder.deleteAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String tokenValue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                        String Login_url = Apis.deleteaddress;

                        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String getStatus = jsonObject.getString("status");
                                            String message = jsonObject.getString("msg");
                                            if (getStatus.equals("1")) {
                                                Toast.makeText(context, "Deleted:" + message, Toast.LENGTH_SHORT).show();
                                                billingAddressList.remove(billingAddressList.get(position));
                                                billingAddressesAdapter.notifyDataSetChanged();
                                            } else {
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("Main", "Error :" + error.getMessage());
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
                                data3.put("id", billingAddressList.get(position).getUa_id());


                                return data3;

                            }
                        };
                        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                        VolleySingleton.getInstance(context).addToRequestQueue(strRequest);
                    }
                });

                holder.updateAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inte = new Intent(context, AddNewAddressActivity.class);
                        inte.putExtra("uid", billingAddressList.get(position).getUa_id());
                        inte.putExtra("postalcode", billingAddressList.get(position).getUa_zip());
                        inte.putExtra("addlabel", billingAddressList.get(position).getUa_identifier());
                        inte.putExtra("name", billingAddressList.get(position).getUa_name());
                        inte.putExtra("addressline1", billingAddressList.get(position).getUa_address1());
                        inte.putExtra("phone", billingAddressList.get(position).getUa_phone());

                        context.startActivity(inte);
                    }
                });

                holder.chooseaddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        index_row = position;
                        notifyDataSetChanged();
                        if (context instanceof SetupBillingAddressActivity) {
                            ((SetupBillingAddressActivity) context).setUidsecond(billingAddressList.get(position).getUa_id());
                        }
                       // Toast.makeText(context, "selctshipid " + billingAddressList.get(position).getUa_id(), Toast.LENGTH_SHORT).show();
                    }
                });
                if (index_row == position) {
                    holder.chooseaddress.setBackground(context.getResources().getDrawable(R.drawable.button_bg));
                    holder.chooseaddress.setTextColor(Color.WHITE);
                } else {
                    holder.chooseaddress.setBackground(context.getResources().getDrawable(R.drawable.button_bg_redtransperent));
                    holder.chooseaddress.setTextColor(Color.BLACK);
                }
                break;

        }
    }

    @Override
    public int getItemCount() {
        return billingAddressList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return billingAddressList.get(position).getTypelayout();
    }

    public class BillHolder extends RecyclerView.ViewHolder {
        TextView label, name, ad_line1, ad_line2, city, contry, state, pincode, mobile, chooseaddress, setdefault;
        ImageView deleteAddress, updateAddress;

        public BillHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            name = itemView.findViewById(R.id.name);
            ad_line1 = itemView.findViewById(R.id.Address_line);
            city = itemView.findViewById(R.id.Address_city);
            contry = itemView.findViewById(R.id.Address_Country);
            state = itemView.findViewById(R.id.Address_state);
            pincode = itemView.findViewById(R.id.Address_pincode);
            mobile = itemView.findViewById(R.id.Address_phoneno);
            deleteAddress = itemView.findViewById(R.id.delete_address);
            chooseaddress = itemView.findViewById(R.id.select);
            updateAddress = itemView.findViewById(R.id.update_address);
            setdefault = itemView.findViewById(R.id.setDefault);
        }

    }
}
