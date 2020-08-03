package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.eWheelersBuyers.AddCartfromServices;
import com.ewheelers.eWheelersBuyers.ModelClass.CartListClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SessionStorage;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCartServiceAdapter extends RecyclerView.Adapter<AddCartServiceAdapter.SummaryHolder> {
    Context context;
    List<CartListClass> cartListClassList;
    private AddCartServiceAdapter cartListingAdapter;

    public AddCartServiceAdapter(Context cartSummaryActivity, List<CartListClass> cartListClassList) {
        this.context = cartSummaryActivity;
        this.cartListClassList = cartListClassList;
        this.cartListingAdapter = this;
    }

    @NonNull
    @Override
    public AddCartServiceAdapter.SummaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_summary_layout, parent, false);
        return new AddCartServiceAdapter.SummaryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddCartServiceAdapter.SummaryHolder holder, int position) {
        holder.shopname.setText(cartListClassList.get(position).getShopname());
        holder.brandname.setText(cartListClassList.get(position).getBrandname());
        holder.tit.setText(cartListClassList.get(position).getProductName());
        List<String> strings = cartListClassList.get(position).getOptions();
        holder.optins.setText(strings.toString());
        holder.price.setText("\u20B9 " + cartListClassList.get(position).getProductPrice() + "/" + splitStringAlpha(cartListClassList.get(position).getProduct_qty()));
        holder.qty.setText("Time : " + cartListClassList.get(position).getProduct_qty());
        double price = Double.parseDouble(cartListClassList.get(position).getProductPrice());
        double time = Double.parseDouble(splitString(cartListClassList.get(position).getProduct_qty()));
        holder.productAmount.setText("\u20B9 " + (price * time));
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        imageLoader.get(cartListClassList.get(position).getImageurl(), ImageLoader.getImageListener(holder.image, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
        holder.image.setImageUrl(cartListClassList.get(position).getImageurl(), imageLoader);
        if (cartListClassList.get(position).getVehicleno().isEmpty()) {
            holder.vehicleno.setText("----");
        }else {
            holder.vehicleno.setText(cartListClassList.get(position).getVehicleno());
        }

        if (cartListClassList.get(position).getVehiclemodel().isEmpty()) {
            holder.vehiclemodel.setText("----");
        }else {
            holder.vehiclemodel.setText(cartListClassList.get(position).getVehiclemodel());
        }

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tokenvalue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                String Login_url = Apis.removecartItems;

                StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String getStatus = jsonObject.getString("status");
                                    String message = jsonObject.getString("msg");
                                    if (getStatus.equals("1")) {
                                        // holder.item_lay.setVisibility(View.GONE);
                                        if (context instanceof AddCartfromServices) {
                                            ((AddCartfromServices) context).onRefresh();
                                        }
                                        // cartListClasses.remove(cartListClasses.get(position));
                                        cartListingAdapter.notifyDataSetChanged();

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
                        data3.put("key", cartListClassList.get(position).getKeyvalue());
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
        return cartListClassList.size();
    }

    public String splitString(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }

       /* System.out.println(alpha);
        System.out.println(num);
        System.out.println(special);*/
        return String.valueOf(num);
    }

    public String splitStringAlpha(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }

       /* System.out.println(alpha);
        System.out.println(num);
        System.out.println(special);*/
        return alpha.substring(0, alpha.length() - 1);
    }


    public class SummaryHolder extends RecyclerView.ViewHolder {
        TextView shopname, brandname, tit, optins, price, qty, productAmount, vehicleno, vehiclemodel;
        NetworkImageView image;
        Button remove;

        public SummaryHolder(@NonNull View itemView) {
            super(itemView);
            vehiclemodel = itemView.findViewById(R.id.vehicleModel);
            vehicleno = itemView.findViewById(R.id.vehicleNo);
            shopname = itemView.findViewById(R.id.shop_name);
            image = itemView.findViewById(R.id.image);
            brandname = itemView.findViewById(R.id.brand);
            tit = itemView.findViewById(R.id.title);
            optins = itemView.findViewById(R.id.options);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            remove = itemView.findViewById(R.id.remove);
            productAmount = itemView.findViewById(R.id.pro_amount);
        }
    }
}
