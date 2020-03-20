package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
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
import com.ewheelers.ewheelersbuyer.ModelClass.BillingAddress;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingAddressesAdapter extends RecyclerView.Adapter<BillingAddressesAdapter.BillHolder> {
    Context context;
    List<BillingAddress> billingAddressList;
    BillingAddressesAdapter billingAddressesAdapter;

    public BillingAddressesAdapter(Context context, List<BillingAddress> billingAddressList) {
        this.context = context;
        this.billingAddressList = billingAddressList;
        this.billingAddressesAdapter=this;
    }

    @NonNull
    @Override
    public BillingAddressesAdapter.BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresscard_layout,parent,false);
        return new BillHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingAddressesAdapter.BillHolder holder, int position) {
        holder.label.setText(billingAddressList.get(position).getUa_identifier());
        holder.name.setText(billingAddressList.get(position).getUa_name());
        holder.ad_line1.setText(billingAddressList.get(position).getUa_address1()+" "+billingAddressList.get(position).getUa_address2());
        holder.city.setText(billingAddressList.get(position).getCity_name());
        holder.contry.setText(billingAddressList.get(position).getCountry_name());
        holder.state.setText(billingAddressList.get(position).getState_name());
        holder.pincode.setText(billingAddressList.get(position).getUa_zip());
        holder.mobile.setText(billingAddressList.get(position).getUa_phone());
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
                                        Toast.makeText(context, "Deleted:"+message, Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public int getItemCount() {
        return billingAddressList.size();
    }

    public class BillHolder extends RecyclerView.ViewHolder {
        TextView label,name,ad_line1,ad_line2,city,contry,state,pincode,mobile;
        ImageView deleteAddress;
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
        }

    }
}
