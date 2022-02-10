package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.ProductdetailsAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.ProductDetails;
import com.ewheelers.eWheelersBuyers.Utilities.TouchImageView;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoomingActivity extends AppCompatActivity {
    RecyclerView galleryimg;
    List<ProductDetails> productDetailsList = new ArrayList<>();
    String productid,tokenvalue;
    ProductdetailsAdapter productdetailsAdapter;
    TouchImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zooming);
        galleryimg = findViewById(R.id.gallery);
        imageView = findViewById(R.id.imageswitcher);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        productid = getIntent().getStringExtra("productid");

        getimagelist(productid);
    }

    public void getimagelist(String prodid) {
        productDetailsList.clear();
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.productdetails + prodid;
        Log.i("url", serverurl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    String cartItemCount = dataJsonObject.getString("cartItemsCount");


                    JSONArray jsonArrayCollections = dataJsonObject.getJSONArray("productImagesArr");
                    for(int i=0;i<jsonArrayCollections.length();i++) {
                        JSONObject jsonObjectProductdetail = jsonArrayCollections.getJSONObject(i);
                        String producturl = jsonObjectProductdetail.getString("product_image_url");
                        ProductDetails productDetails = new ProductDetails();
                        productDetails.setProductimg_url(producturl);
                        productDetails.setTypeoflayout(1);
                        productDetailsList.add(productDetails);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ZoomingActivity.this, RecyclerView.HORIZONTAL, false);
                    galleryimg.setLayoutManager(linearLayoutManager);
                    productdetailsAdapter = new ProductdetailsAdapter(ZoomingActivity.this, productDetailsList, 0,"zoom");
                    galleryimg.setAdapter(productdetailsAdapter);

                    galleryimg.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(ZoomingActivity.this, R.anim.layoutanimationleft));
                    galleryimg.getAdapter().notifyDataSetChanged();
                    galleryimg.scheduleLayoutAnimation();
                    //productdetailsAdapter.notifyDataSetChanged();

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
                return null;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    public void onClickcalled(String url) {
        Picasso.with(ZoomingActivity.this)
                .load(url)
                .placeholder(R.drawable.ic_dashboard_black_24dp)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(imageView);
    }
}
