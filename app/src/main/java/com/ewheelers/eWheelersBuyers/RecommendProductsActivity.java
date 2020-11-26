package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.AddonsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.ProductdetailsAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.AddonsClass;
import com.ewheelers.eWheelersBuyers.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecommendProductsActivity extends AppCompatActivity  {
    RecyclerView recyclerView;
   // ArrayList<ProductDetails> buyDetailsList = new ArrayList<>();
    List<AddonsClass> buyDetailsList = new ArrayList<>();

    ProductdetailsAdapter productdetailsAdapter;
    String buttonText;
    String productid,qty;
    AddonsAdapter addonsAdapter;
    Button buttonContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_products);
        recyclerView = findViewById(R.id.recommended_products);
        buttonContinue = findViewById(R.id.addtoCart);
        String buttonText = getIntent().getStringExtra("buttontext");
        productid = getIntent().getStringExtra("selproductid");
        //qty = getIntent().getStringExtra("quantity");
        if(buttonText.equals("BUY")) {
            getRecommends(productid);
        }

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // new ProductDetailActivity().addTocart(productid,"BUY","1",getApplicationContext());
            }
        });

    }

    private void getRecommends(String id) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.productdetails + id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equals("1")){
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                    JSONObject jsonobjectBuyTogether = dataJsonObject.getJSONObject("buyTogether");
                    String buywithTitle = jsonobjectBuyTogether.getString("title");
                    JSONArray jsonArraybuyWithData = jsonobjectBuyTogether.getJSONArray("data");
                    for (int buytogetherobjects = 0; buytogetherobjects < jsonArraybuyWithData.length(); buytogetherobjects++) {
                        JSONObject jsonObjectbuywith = jsonArraybuyWithData.getJSONObject(buytogetherobjects);
                        String productimgurl = jsonObjectbuywith.getString("product_image_url");
                        String productName = jsonObjectbuywith.getString("product_name");
                        String productPrice = jsonObjectbuywith.getString("selprod_price");
                        String selectedProductId = jsonObjectbuywith.getString("selprod_product_id");

                        /*ProductDetails productDetails = new ProductDetails();
                        productDetails.setBuywithimageurl(productimgurl);
                        productDetails.setBuywithproductname(productName);
                        productDetails.setBuywithproductprice(productPrice);
                        productDetails.setButwithselectedProductId(selectedProductId);
                        productDetails.setTypeoflayout(3);*/
                        AddonsClass productDetailsaddons = new AddonsClass();
                        productDetailsaddons.setBuywithimageurl(productimgurl);
                        productDetailsaddons.setBuywithproductname(productName);
                        productDetailsaddons.setBuywithproductprice(productPrice);
                        productDetailsaddons.setButwithselectedProductId(selectedProductId);
                        buyDetailsList.add(productDetailsaddons);
                    }
                       /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        productdetailsAdapter = new ProductdetailsAdapter(RecommendProductsActivity.this,buyDetailsList);
                        recyclerView.setAdapter(productdetailsAdapter);*/
                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(RecommendProductsActivity.this, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager2);
                        addonsAdapter = new AddonsAdapter(RecommendProductsActivity.this, buyDetailsList,"");
                        recyclerView.setAdapter(addonsAdapter);
                        addonsAdapter.notifyDataSetChanged();
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
            public Map<String, String> getParams() {
                return null;
            }

        };
        queue.add(stringRequest);

    }
}


