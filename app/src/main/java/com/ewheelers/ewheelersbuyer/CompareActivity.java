package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CompareActivity extends AppCompatActivity {
    String proone, protwo;
    String tokenvalue;
    NetworkImageView netimg1, netimg2;
    TextView txt1, txt2;
    ListView listView1,listView2;
    String subkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);


        proone = getIntent().getStringExtra("prod1");
        protwo = getIntent().getStringExtra("prod2");
        Log.i("getIds", proone + ", " + protwo);

        netimg1 = findViewById(R.id.netimg1);
        netimg2 = findViewById(R.id.netimg2);
        txt1 = findViewById(R.id.textone);
        txt2 = findViewById(R.id.text2);

        listView1 = findViewById(R.id.listone);
        listView2 = findViewById(R.id.listtwo);

        getproductone(proone);
        getproducttwo(protwo);
    }

    public void getproductone(String proone) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.productdetails + proone;
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

                    JSONObject jsonObjectatt = dataJsonObject.getJSONObject("attributes");
                    JSONObject jsonObjectsubattarray = jsonObjectatt.getJSONObject("attributesArray");
                    Iterator iter = jsonObjectsubattarray.keys();
                    ArrayList<String> strings = new ArrayList<>();
                    ArrayList<String> substrings = new ArrayList<>();
                    HashMap<String, String> map = new HashMap<String, String>();

                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        String value = jsonObjectsubattarray.getString(key);
                        map.put(key, value);
                        strings.add(key+"\n\n"+value+"\n");
                        //substrings.add(value);
                       // JSONObject value = jsonObjectsubattarray.getJSONObject(key);
                        /*Iterator subiterator = value.keys();
                        while (subiterator.hasNext()) {
                            subkey = (String) subiterator.next();
                            String subvalue = value.getString(subkey);
                            substrings.add(subkey+":"+subvalue);
                            Log.i("sublist",substrings.toString());
                        }*/

                    }

                    ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(CompareActivity.this, android.R.layout.simple_list_item_1, strings);
                    listView1.setAdapter(itemsAdapter);

                    JSONObject jsonObjectProduct = dataJsonObject.getJSONObject("product");
                    JSONObject jsonObjectdata = jsonObjectProduct.getJSONObject("data");
                    String selproductid = jsonObjectdata.getString("selprod_id");
                    String productname = jsonObjectdata.getString("product_name");
                    String productprice = jsonObjectdata.getString("selprod_price");
                    String productmodel = jsonObjectdata.getString("product_model");
                    String productdescription = jsonObjectdata.getString("product_description");
                    String isRent = jsonObjectdata.getString("is_rent");
                    String testDriveEnable = jsonObjectdata.getString("selprod_test_drive_enable");
                    String booknowEnable = jsonObjectdata.getString("selprod_book_now_enable");
                    String rentPrice = jsonObjectdata.getString("rent_price");
                    String rentSecurity = jsonObjectdata.getString("sprodata_rental_security");
                    String minrentduration = jsonObjectdata.getString("sprodata_minimum_rental_duration");

                    JSONArray jsonArrayCollections = dataJsonObject.getJSONArray("productImagesArr");
                    JSONObject jsonObjectProductdetail = jsonArrayCollections.getJSONObject(0);
                    String producturl = jsonObjectProductdetail.getString("product_image_url");


                    //txt1.setText("Name\n\n" + productname + "\n\nPrice\n\n\u20B9 " + productprice + "\n\nModel\n\n" + productmodel);
                    ImageLoader imageLoader = VolleySingleton.getInstance(CompareActivity.this)
                            .getImageLoader();
                    imageLoader.get(producturl, ImageLoader.getImageListener(netimg1, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                    netimg1.setImageUrl(producturl, imageLoader);

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

    public void getproducttwo(String protwo) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.productdetails + protwo;
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

                    JSONObject jsonObjectatt = dataJsonObject.getJSONObject("attributes");
                    JSONObject jsonObjectsubattarray = jsonObjectatt.getJSONObject("attributesArray");
                    Iterator iter = jsonObjectsubattarray.keys();
                    ArrayList<String> strings = new ArrayList<>();
                    ArrayList<String> substrings = new ArrayList<>();
                    HashMap<String, String> map = new HashMap<String, String>();

                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        String value = jsonObjectsubattarray.getString(key);
                        map.put(key, value);
                        strings.add(key+"\n\n"+value+"\n");
                        //substrings.add(value);
                        // JSONObject value = jsonObjectsubattarray.getJSONObject(key);
                        /*Iterator subiterator = value.keys();
                        while (subiterator.hasNext()) {
                            subkey = (String) subiterator.next();
                            String subvalue = value.getString(subkey);
                            substrings.add(subkey+":"+subvalue);
                            Log.i("sublist",substrings.toString());
                        }*/

                    }

                    ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(CompareActivity.this, android.R.layout.simple_list_item_1, strings);
                    listView2.setAdapter(itemsAdapter);

                    JSONObject jsonObjectProduct = dataJsonObject.getJSONObject("product");
                    JSONObject jsonObjectdata = jsonObjectProduct.getJSONObject("data");
                    String selproductid = jsonObjectdata.getString("selprod_id");
                    String productname = jsonObjectdata.getString("product_name");
                    String productprice = jsonObjectdata.getString("selprod_price");
                    String productmodel = jsonObjectdata.getString("product_model");
                    String productdescription = jsonObjectdata.getString("product_description");
                    String isRent = jsonObjectdata.getString("is_rent");
                    String testDriveEnable = jsonObjectdata.getString("selprod_test_drive_enable");
                    String booknowEnable = jsonObjectdata.getString("selprod_book_now_enable");
                    String rentPrice = jsonObjectdata.getString("rent_price");
                    String rentSecurity = jsonObjectdata.getString("sprodata_rental_security");
                    String minrentduration = jsonObjectdata.getString("sprodata_minimum_rental_duration");

                    JSONArray jsonArrayCollections = dataJsonObject.getJSONArray("productImagesArr");
                    JSONObject jsonObjectProductdetail = jsonArrayCollections.getJSONObject(0);
                    String producturl = jsonObjectProductdetail.getString("product_image_url");

                   // txt2.setText("Name\n\n" + productname + "\n\nPrice\n\n\u20B9 " + productprice + "\n\nModel\n\n" + productmodel);
                    ImageLoader imageLoader = VolleySingleton.getInstance(CompareActivity.this)
                            .getImageLoader();
                    imageLoader.get(producturl, ImageLoader.getImageListener(netimg2, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                    netimg2.setImageUrl(producturl, imageLoader);

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
}
