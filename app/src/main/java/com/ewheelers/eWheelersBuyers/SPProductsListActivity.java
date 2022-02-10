package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.eWheelersBuyers.Adapters.SPProductsAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.AddonsClass;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ewheelers.eWheelersBuyers.SessionStorage.tokenvalue;

public class SPProductsListActivity extends AppCompatActivity {
    RecyclerView recyclerViewProd;
    SPProductsAdapter spProductsAdapter;
    List<AddonsClass> addonsClasses = new ArrayList<>();
    TextView hubtitle, empty_view;
    ImageView networkImageView;
    String tokenValue, uaid, uaid2, name, identifier2, identifier, shopId, producttype, distance, serviceProvider;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spproducts_list);
        progressBar = findViewById(R.id.progress);
        recyclerViewProd = findViewById(R.id.productsList);
        hubtitle = findViewById(R.id.hubtitle);
        networkImageView = findViewById(R.id.netwrkimg);
        empty_view = findViewById(R.id.emptyView);
        tokenValue = new SessionStorage().getStrings(this, tokenvalue);
        uaid = getIntent().getStringExtra("uaid");
        //uaid2 = getIntent().getStringExtra("uaidscan");
        name = getIntent().getStringExtra("name");
        identifier = getIntent().getStringExtra("identifier");
        identifier2 = getIntent().getStringExtra("identifier2");
        shopId = getIntent().getStringExtra("shopid");
        producttype = getIntent().getStringExtra("producttype");
        distance = getIntent().getStringExtra("distance");
        serviceProvider = getIntent().getStringExtra("serviceprovider");
        String jsonObject = getIntent().getStringExtra("options");

        //Toast.makeText(this, "uaid:"+uaid, Toast.LENGTH_SHORT).show();

        if (uaid != null) {
            //hubtitle.setText(name + " (" + distance + ")");
            if (jsonObject != null) {
                //Toast.makeText(this, "havejson:" + serviceProvider, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject);
                    Iterator iterator = jsonObject1.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        if (jsonObject1.getString(key).equals("Parking")) {
                            loadspProducts(uaid, identifier, shopId, producttype, key, serviceProvider);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Toast.makeText(this, "nojson:" + serviceProvider, Toast.LENGTH_SHORT).show();

                loadspProducts(uaid, identifier, shopId, producttype, "", serviceProvider);
            }
            //loadspProducts(uaid,"","","");
        } else {

            if (identifier2 != null) {
                //Toast.makeText(this, "scanserv:" + serviceProvider + "identifier:" + identifier2, Toast.LENGTH_SHORT).show();
                loadspProducts("", identifier2, "", "", "", serviceProvider);

               /* try {
                    JSONObject jsonObject1 = new JSONObject(identifier2);
                    String identity = jsonObject1.getString("identifier");
                    String shopid = jsonObject1.getString("shopid");
                    String servicetype = jsonObject1.getString("servicetype");
                    if(identity.isEmpty()){
                        loadspProducts("", "", shopid, "", servicetype);
                    }else {
                        loadspProducts("", identifier2, "", "", servicetype);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            } else {
               // Toast.makeText(this, "otherserv:" + serviceProvider, Toast.LENGTH_SHORT).show();
                //hubtitle.setText(name + " (" + distance + ")");
                try {
                    assert jsonObject != null;
                    JSONObject jsonObject1 = new JSONObject(jsonObject);
                    Iterator iterator = jsonObject1.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        if (serviceProvider.equals(jsonObject1.getString(key))) {
                            loadspProducts("", "", shopId, producttype, key, serviceProvider);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(this, serviceProvider, Toast.LENGTH_SHORT).show();
                //loadspProducts("", "", shopId, producttype, "");
            }
        }

        hubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadspProducts(String hubid, String identifier, String shopid, String producttype, String options, String serviceProvider) {
        progressBar.setVisibility(View.VISIBLE);
        addonsClasses.clear();
        double latitude = new NavAppBarActivity().setlatitude();
        double longitude = new NavAppBarActivity().setlongitude();
        String Login_url = Apis.spproductslist;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                progressBar.setVisibility(View.GONE);

                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                String shopurl = jsonObject1.getString("shop_banner_url");
                                String baseurl = "https://ewheelers.in" + shopurl;
                                //Toast.makeText(SPProductsListActivity.this, baseurl, Toast.LENGTH_SHORT).show();
                                Picasso.with(SPProductsListActivity.this).load(baseurl).into(networkImageView);
                                /*ImageLoader imageLoaderbaner = VolleySingleton.getInstance(SPProductsListActivity.this).getImageLoader();
                                imageLoaderbaner.get(baseurl, ImageLoader.getImageListener(networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                                networkImageView.setImageUrl(baseurl, imageLoaderbaner);*/
                                String stationname = jsonObject1.getString("stationName");
                                String stationAddress = jsonObject1.getString("stationAddress");
                                String stationdistance = jsonObject1.getString("stationDistance");
                                String rounded = "";
                                if (!stationdistance.equals("")) {
                                    double dis = Double.parseDouble(stationdistance);
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    df.setRoundingMode(RoundingMode.HALF_UP);
                                    rounded = df.format(dis);
                                    hubtitle.setText(stationname + "(" + rounded + " km )");
                                }
                                JSONArray jsonArray = jsonObject1.getJSONArray("result");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    String proid = jsonObject2.getString("selprod_id");
                                    String title = jsonObject2.getString("selprod_title");
                                    String price = jsonObject2.getString("theprice");
                                    String instock = jsonObject2.getString("in_stock");
                                    String logo = jsonObject2.getString("product_image_url");
                                    String uaName = jsonObject2.getString("ua_name");
                                    String stationaddress = jsonObject2.getString("station_address");

                                    AddonsClass addonsClass = new AddonsClass();
                                    addonsClass.setUa_id(hubid);
                                    addonsClass.setDistance(" (" + rounded + " km )");
                                    addonsClass.setLogo(logo);
                                    addonsClass.setBuywithimageurl(shopurl);
                                    addonsClass.setButwithselectedProductId(proid);
                                    addonsClass.setBuywithproductname(title);
                                    addonsClass.setBuywithproductprice(price);
                                    addonsClass.setUaname(uaName);
                                    addonsClass.setUaddress(stationaddress);
                                    addonsClass.setServiceprovider(serviceProvider);
                                    addonsClasses.add(addonsClass);
                                }
                                if (addonsClasses.isEmpty()) {
                                    empty_view.setVisibility(View.VISIBLE);
                                    recyclerViewProd.setVisibility(View.GONE);
                                } else {
                                    empty_view.setVisibility(View.GONE);
                                    recyclerViewProd.setVisibility(View.VISIBLE);
                                    spProductsAdapter = new SPProductsAdapter(SPProductsListActivity.this, addonsClasses);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SPProductsListActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerViewProd.setLayoutManager(linearLayoutManager);
                                    recyclerViewProd.setAdapter(spProductsAdapter);
                                    spProductsAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(SPProductsListActivity.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("ua_id", hubid);
                data3.put("ua_identifier", identifier);
                data3.put("product_type", producttype);
                data3.put("shop_id", shopid);
                data3.put("latitude", String.valueOf(latitude));
                data3.put("longitude", String.valueOf(longitude));
                data3.put("options", options);
                data3.put("pageSize", "1000");
                data3.put("page", "1");
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }

}
