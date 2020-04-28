package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ewheelers.eWheelersBuyers.CartListingActivity;
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

public class CartListingAdapter extends RecyclerView.Adapter<CartListingAdapter.MyCartListHolder> {
    private Context context;
    private List<CartListClass> cartListClasses;
    private int quantity = 1;
    private CartListingAdapter cartListingAdapter;

    public CartListingAdapter(Context context, List<CartListClass> cartListClasses) {
        this.context = context;
        this.cartListClasses = cartListClasses;
        this.cartListingAdapter = this;
    }

    @NonNull
    @Override
    public MyCartListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case CartListClass.ADDONLAYOUT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addoncart_layout, parent, false);
                return new MyCartListHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlistinglayout, parent, false);
                return new MyCartListHolder(v);
        }
      /*  View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlistinglayout, parent, false);
        return new MyCartListHolder(v);*/
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartListHolder holder, int position) {

        final int itemType = getItemViewType(position);
        switch (itemType) {
            case CartListClass.ADDONLAYOUT:
                holder.brandname.setText(cartListClasses.get(position).getBrandname());
                holder.shopname.setText(cartListClasses.get(position).getShopname());
                holder.title.setText(cartListClasses.get(position).getProductName());
                holder.price.setText("\u20B9 " + cartListClasses.get(position).getProductPrice());
                holder.quantity.setText(cartListClasses.get(position).getProduct_qty());
                ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                imageLoader.get(cartListClasses.get(position).getImageurl(), ImageLoader.getImageListener(holder.image_url, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                holder.image_url.setImageUrl(cartListClasses.get(position).getImageurl(), imageLoader);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String key = cartListClasses.get(position).getKeyvalue();
                        Log.e("key", key);
                        if (context instanceof CartListingActivity) {
                            // ((CartListingActivity) context).removecartItem(key,position);
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
                                                    if (context instanceof CartListingActivity) {
                                                        ((CartListingActivity) context).onRefresh();
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
                                    data3.put("key", key);
                                    return data3;

                                }
                            };
                            strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                            VolleySingleton.getInstance(context).addToRequestQueue(strRequest);


                        }
                    }
                });
                holder.addproduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quant = holder.quantity.getText().toString();
                        quantity = Integer.parseInt(quant);
                        quantity++;

                        String key = cartListClasses.get(position).getKeyvalue();
                        Log.i("productKey", key);
                        String tokenvalue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                        String Login_url = Apis.updatecartitems;

                        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String getStatus = jsonObject.getString("status");
                                            String message = jsonObject.getString("msg");
                                            if (getStatus.equals("1")) {
                                                holder.quantity.setText(String.valueOf(quantity));
                                                if (context instanceof CartListingActivity) {
                                                    ((CartListingActivity) context).onRefresh();
                                                }

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
                                data3.put("key", key);
                                data3.put("quantity", String.valueOf(quantity));
                                return data3;
                            }
                        };
                        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                        VolleySingleton.getInstance(context).addToRequestQueue(strRequest);
                    }
                });
                holder.removeproduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quant = holder.quantity.getText().toString();
                        quantity = Integer.parseInt(quant);
                        quantity--;

                        //  if (quantity > 0) {

                        String key = cartListClasses.get(position).getKeyvalue();

                        String tokenvalue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                        String Login_url = Apis.updatecartitems;

                        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String getStatus = jsonObject.getString("status");
                                            String message = jsonObject.getString("msg");
                                            if (getStatus.equals("1")) {
                                                holder.quantity.setText(String.valueOf(quantity));
                                                if (context instanceof CartListingActivity) {
                                                    ((CartListingActivity) context).onRefresh();
                                                }

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
                                data3.put("key", key);
                                data3.put("quantity", String.valueOf(quantity));
                                return data3;

                            }
                        };
                        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                        VolleySingleton.getInstance(context).addToRequestQueue(strRequest);
                        //  }
                    }
                });

                break;
            default:
                holder.brandname.setText(cartListClasses.get(position).getBrandname());
                List<String> strings = cartListClasses.get(position).getOptions();
                holder.options.setText(strings.toString());
                holder.shopname.setText(cartListClasses.get(position).getShopname());
                holder.title.setText(cartListClasses.get(position).getProductName());
                holder.price.setText("\u20B9 " + cartListClasses.get(position).getProductPrice());
                holder.quantity.setText(cartListClasses.get(position).getProduct_qty());
                imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                imageLoader.get(cartListClasses.get(position).getImageurl(), ImageLoader.getImageListener(holder.image_url, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                holder.image_url.setImageUrl(cartListClasses.get(position).getImageurl(), imageLoader);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String key = cartListClasses.get(position).getKeyvalue();
                        Log.e("key", key);
                        if (context instanceof CartListingActivity) {
                            // ((CartListingActivity) context).removecartItem(key,position);
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
                                                    if (context instanceof CartListingActivity) {
                                                        ((CartListingActivity) context).onRefresh();
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
                                    data3.put("key", key);
                                    return data3;

                                }
                            };
                            strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                            VolleySingleton.getInstance(context).addToRequestQueue(strRequest);


                        }
                    }
                });
                holder.addproduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quant = holder.quantity.getText().toString();
                        quantity = Integer.parseInt(quant);
                        quantity++;

                        String key = cartListClasses.get(position).getKeyvalue();
                        Log.i("productKey", key);
                        String tokenvalue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                        String Login_url = Apis.updatecartitems;

                        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String getStatus = jsonObject.getString("status");
                                            String message = jsonObject.getString("msg");
                                            if (getStatus.equals("1")) {
                                                holder.quantity.setText(String.valueOf(quantity));
                                                if (context instanceof CartListingActivity) {
                                                    ((CartListingActivity) context).onRefresh();
                                                }

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
                                data3.put("key", key);
                                data3.put("quantity", String.valueOf(quantity));
                                return data3;
                            }
                        };
                        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                        VolleySingleton.getInstance(context).addToRequestQueue(strRequest);
                    }
                });
                holder.removeproduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quant = holder.quantity.getText().toString();
                        quantity = Integer.parseInt(quant);
                        quantity--;

                        //  if (quantity > 0) {

                        String key = cartListClasses.get(position).getKeyvalue();

                        String tokenvalue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                        String Login_url = Apis.updatecartitems;

                        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String getStatus = jsonObject.getString("status");
                                            String message = jsonObject.getString("msg");
                                            if (getStatus.equals("1")) {
                                                holder.quantity.setText(String.valueOf(quantity));
                                                if (context instanceof CartListingActivity) {
                                                    ((CartListingActivity) context).onRefresh();
                                                }

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
                                data3.put("key", key);
                                data3.put("quantity", String.valueOf(quantity));
                                return data3;

                            }
                        };
                        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                        VolleySingleton.getInstance(context).addToRequestQueue(strRequest);
                        //  }
                    }
                });

        }


    }


    @Override
    public int getItemCount() {
        return cartListClasses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return cartListClasses.get(position).getType();
    }

    public class MyCartListHolder extends RecyclerView.ViewHolder {
        TextView shopname, title, options, price, quantity, brandname;
        NetworkImageView image_url;
        Button addproduct, removeproduct;
        ImageView delete;
        LinearLayout item_lay;

        // TextView duration;
        public MyCartListHolder(@NonNull View itemView) {
            super(itemView);
            image_url = itemView.findViewById(R.id.image);
            shopname = itemView.findViewById(R.id.shop_name);
            title = itemView.findViewById(R.id.title);
            options = itemView.findViewById(R.id.options);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.qty);
            brandname = itemView.findViewById(R.id.brand);
            addproduct = itemView.findViewById(R.id.addProduct);
            removeproduct = itemView.findViewById(R.id.removeproduct);
            delete = itemView.findViewById(R.id.delete_image);
            item_lay = itemView.findViewById(R.id.item_lay);
            // duration = itemView.findViewById(R.id.duration);
        }
    }
}
