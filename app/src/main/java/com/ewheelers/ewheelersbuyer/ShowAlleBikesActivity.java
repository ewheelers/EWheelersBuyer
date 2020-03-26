package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.AllebikesAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.AllebikesModelClass;
import com.ewheelers.ewheelersbuyer.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowAlleBikesActivity extends AppCompatActivity {
RecyclerView recyclerView;
AllebikesAdapter allebikesAdapter;
List<AllebikesModelClass> allebikelist = new ArrayList<>();
TextView textView;
String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alle_bikes);
        token = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);

        textView = findViewById(R.id.title);
        recyclerView = findViewById(R.id.all_ebikeslist);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getCollectionProducts();
    }

/*    public List<AllebikesModelClass> allebikesModelClasses(){
        allebikesModelClasses.add(new AllebikesModelClass("https://www.ewheelers.in/image/product/4/CLAYOUT3/5/0/1?t=1584001863","EAGAN","\u20B9 60000"));
        allebikesModelClasses.add(new AllebikesModelClass("https://www.ewheelers.in/image/product/1/CLAYOUT3/2/0/1?t=1584265015","iZitto by GOOD LUCK","\u20B9 65000"));
        return allebikesModelClasses;
    }*/

    public void getCollectionProducts() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.home;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {

                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                        JSONArray jsonArrayCollections = dataJsonObject.getJSONArray("collections");
                        if (jsonArrayCollections != null) {
                            for (int i = 0; i < jsonArrayCollections.length(); i++) {
                                JSONObject jsonObjectProducts = jsonArrayCollections.getJSONObject(i);
                                String collectionType = jsonObjectProducts.getString("collection_type");
                                //collectionTitle.setText(collectionname);

                                if (collectionType.equals("1")) {
                                    String collection_id = jsonObjectProducts.getString("collection_id");
                                    //   String collectionimage = jsonObjectProducts.getString("collection_image");
                                    String collection_name = jsonObjectProducts.getString("collection_name");
                                    JSONArray jsonArrayProducts = jsonObjectProducts.getJSONArray("products");
                                    for (int j = 0; j < jsonArrayProducts.length(); j++) {
                                        JSONObject products = jsonArrayProducts.getJSONObject(j);
                                        String productName = products.getString("product_name");
                                        String productPrice = products.getString("selprod_price");
                                        String productImageurl = products.getString("product_image_url");
                                        String selproductid = products.getString("selprod_id");
                                        String productid = products.getString("product_id");
                                        String productcatname = products.getString("prodcat_name");
                                        String isSell = products.getString("is_sell");
                                        String isRent = products.getString("is_rent");
                                        String rentPrice = products.getString("rent_price");
                                        String rentaltype = products.getString("rental_type");

                                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                        allebikesModelClass.setPrice(productPrice);
                                        allebikesModelClass.setProductName(productName);
                                        allebikesModelClass.setProductid(selproductid);
                                        allebikesModelClass.setNetworkImage(productImageurl);
                                        allebikelist.add(allebikesModelClass);
                                    }

                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this,allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this,RecyclerView.VERTICAL,false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
                                }


                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("X-TOKEN", token);
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

}
