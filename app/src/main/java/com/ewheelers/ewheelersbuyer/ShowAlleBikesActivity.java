package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.AllebikesAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.AllebikesModelClass;
import com.ewheelers.ewheelersbuyer.ModelClass.ProductDetails;
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

import static com.ewheelers.ewheelersbuyer.Dialogs.ShowAlerts.showfailedDialog;

public class ShowAlleBikesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AllebikesAdapter allebikesAdapter;
    List<AllebikesModelClass> allebikelist = new ArrayList<>();
    TextView textView;
    String token;
    String collectionidbikes, collectionidcat, collectionidbrands, collectionidshops;
    String collectionid, tokenvalue, allproducts;
    String brandid, categoryid, shopid, shopname, shoplogo, typebtn;
    int stock;
    TextView textView_empty;
    NetworkImageView networkImageView, bannerImage;
    LinearLayout linearLayoutEmpty;

    SearchView searchView;
    ListView list;
    List<AllebikesModelClass> allebikesModelClassesList = new ArrayList<>();

    String onlyTestDrive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alle_bikes);
        token = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);

        textView = findViewById(R.id.titleTxt);
        recyclerView = findViewById(R.id.all_ebikeslist);
        textView_empty = findViewById(R.id.emptyView);
        linearLayoutEmpty = findViewById(R.id.empty_layout);
        networkImageView = findViewById(R.id.shopimage);

        bannerImage = findViewById(R.id.banner);

        searchView = findViewById(R.id.searchview);
        list = findViewById(R.id.listview);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(ShowAlleBikesActivity.this, "keyword:" + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1) {
                    list.setVisibility(View.VISIBLE);
                    setSuggestions();
                    String[] strings = {"abc", "cda"};
                    list.setAdapter(new ArrayAdapter<String>(ShowAlleBikesActivity.this, android.R.layout.simple_spinner_dropdown_item, strings));
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String s = list.getItemAtPosition(position).toString();
                            searchView.setQuery(s, true);
                        }
                    });
                } else {
                    list.setVisibility(View.GONE);
                }
                return false;
            }
        });

        //from CollectionProductsAdapter
        brandid = getIntent().getStringExtra("brandid");
        categoryid = getIntent().getStringExtra("catid");
        shopid = getIntent().getStringExtra("shopid");
        shopname = getIntent().getStringExtra("shopname");
        shoplogo = getIntent().getStringExtra("shopimage");
        // stock = getIntent().getIntExtra("instock",0);
        typebtn = getIntent().getStringExtra("type");
        textView.setText(typebtn);

        Log.i("ids:", "brandis-" + brandid + " categoryid-" + categoryid + " shopid-" + shopid);

        if (brandid != null) {
            getfilterProducts(brandid);
        }

        if (categoryid != null) {
            getcategoryProducts(categoryid);
        }

        if (shopid != null) {
            getShopProducts(shopid);
        }

        collectionidbikes = getIntent().getStringExtra("allpopularbikes");
        collectionidcat = getIntent().getStringExtra("allcategories");
        collectionidbrands = getIntent().getStringExtra("allbrands");
        collectionidshops = getIntent().getStringExtra("allshops");
        onlyTestDrive = getIntent().getStringExtra("onlytestdrives");
        // Toast.makeText(this, "coolectid" + collectionidbikes + collectionidcat + collectionidbrands + collectionidshops, Toast.LENGTH_SHORT).show();


        if (onlyTestDrive!=null) {
            if(onlyTestDrive.equals("0")){
                getOnlyRentProducts("2","");
            }else {
                getOnlyRentProducts("1","test_drive");

            }
        } else {

        }


        if (collectionidbikes != null) {

            collectionid = collectionidbikes;
        }
        if (collectionidcat != null) {

            collectionid = collectionidcat;
        }
        if (collectionidbrands != null) {

            collectionid = collectionidbrands;
        }
        if (collectionidshops != null) {
            getAllShops();
            //collectionid = collectionidshops;
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getCollectionProducts(collectionid);
    }

    private void getOnlyRentProducts(String rent,String testdrive) {
        allebikelist.clear();
        String Login_url = Apis.filteredproducts;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                                    String productPrice = jsonObjectProducts.getString("theprice");
                                    String productName = jsonObjectProducts.getString("product_name");
                                    String selproductid = jsonObjectProducts.getString("selprod_id");
                                    String productImageurl = jsonObjectProducts.getString("product_image_url");
                                    String testdriveenable = jsonObjectProducts.getString("selprod_test_drive_enable");
                                    String issell = jsonObjectProducts.getString("is_sell");
                                    String isrent = jsonObjectProducts.getString("is_rent");
                                    String instock = jsonObjectProducts.getString("in_stock");

                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setInstock(instock);
                                    allebikesModelClass.setTypeLayout(0);

                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                } else {
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
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
                ArrayList<String> strings = new ArrayList<>();
                strings.add(rent);
                Map<String, String> data3 = new HashMap<String, String>();
                if(testdrive.isEmpty()){
                    data3.put("producttype", strings.toString());
                }else {
                    data3.put("producttype", strings.toString());
                    data3.put("tdrive", testdrive);
                }
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    private void getAllShops() {
        String url_link = Apis.getallshops;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = jsonObjectData.getJSONArray("allShops");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject address = jsonArray.getJSONObject(i);
                        String shopid = address.getString("shop_id");
                        String shopuserid = address.getString("shop_user_id");
                        String shopname = address.getString("shop_name");
                        String shopcity = address.getString("shop_city");
                        String shopstate = address.getString("state_name");
                        String shopcontry = address.getString("country_name");
                        String shopphoneno = address.getString("shop_phone");
                        String shoppincode = address.getString("shop_postalcode");

                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                        allebikesModelClass.setProductName(shopname);
                        allebikesModelClass.setProductid(shopid);
                        allebikesModelClass.setShopphone(shopphoneno);
                        allebikesModelClass.setPrice(shopcity + " " + shopstate + " " + shopcontry + " - " + shoppincode);
                        allebikesModelClass.setTypeLayout(4);
                        allebikesModelClassesList.add(allebikesModelClass);
                    }

                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikesModelClassesList);
                    recyclerView.setAdapter(allebikesAdapter);
                    GridLayoutManager linearLayoutManager = new GridLayoutManager(ShowAlleBikesActivity.this, 2);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    allebikesAdapter.notifyDataSetChanged();


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
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    private void getShopProducts(String shopid) {
        allebikelist.clear();
        String Login_url = Apis.viewshopbyid + shopid;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");

                                JSONObject jsonObjectShop = jsonObjectData.getJSONObject("shop");
                                String shopname = jsonObjectShop.getString("shop_name");
                                String shoplogo = jsonObjectShop.getString("shop_logo");
                                String shopbanner = jsonObjectShop.getString("shop_banner");
                                bannerImage.setVisibility(View.VISIBLE);
                                ImageLoader imageLoader = VolleySingleton.getInstance(ShowAlleBikesActivity.this)
                                        .getImageLoader();
                                imageLoader.get(shopbanner, ImageLoader.getImageListener(bannerImage, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                                bannerImage.setImageUrl(shopbanner, imageLoader);
                                JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                                    String productPrice = jsonObjectProducts.getString("theprice");
                                    String productName = jsonObjectProducts.getString("product_name");
                                    String selproductid = jsonObjectProducts.getString("selprod_id");
                                    String productImageurl = jsonObjectProducts.getString("product_image_url");
                                    String testdriveenable = jsonObjectProducts.getString("selprod_test_drive_enable");
                                    String issell = jsonObjectProducts.getString("is_sell");
                                    String isrent = jsonObjectProducts.getString("is_rent");
                                    String instock = jsonObjectProducts.getString("in_stock");

                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setTypeLayout(0);
                                    allebikesModelClass.setInstock(instock);
                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                } else {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    textView_empty.setText(shopname);
                                    if (shoplogo != null) {
                                        imageLoader = VolleySingleton.getInstance(ShowAlleBikesActivity.this).getImageLoader();
                                        imageLoader.get(shoplogo, ImageLoader.getImageListener(networkImageView, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                                        networkImageView.setImageUrl(shoplogo, imageLoader);
                                    }
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
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
                // data3.put("shop_id", shopid);
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }

    private void getcategoryProducts(String categoryid) {
        allebikelist.clear();
        String Login_url = Apis.filteredproducts;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                                    String productPrice = jsonObjectProducts.getString("theprice");
                                    String productName = jsonObjectProducts.getString("product_name");
                                    String selproductid = jsonObjectProducts.getString("selprod_id");
                                    String productImageurl = jsonObjectProducts.getString("product_image_url");
                                    String testdriveenable = jsonObjectProducts.getString("selprod_test_drive_enable");
                                    String issell = jsonObjectProducts.getString("is_sell");
                                    String isrent = jsonObjectProducts.getString("is_rent");
                                    String instock = jsonObjectProducts.getString("in_stock");

                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setInstock(instock);
                                    allebikesModelClass.setTypeLayout(0);

                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                } else {
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
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
                ArrayList<String> strings = new ArrayList<>();
                strings.add(categoryid);
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("prodcat", strings.toString());
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }

    private void getfilterProducts(String brandid) {
        allebikelist.clear();
        String Login_url = Apis.filteredproducts;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                                    String productPrice = jsonObjectProducts.getString("theprice");
                                    String productName = jsonObjectProducts.getString("product_name");
                                    String selproductid = jsonObjectProducts.getString("selprod_id");
                                    String productImageurl = jsonObjectProducts.getString("product_image_url");
                                    String testdriveenable = jsonObjectProducts.getString("selprod_test_drive_enable");
                                    String issell = jsonObjectProducts.getString("is_sell");
                                    String isrent = jsonObjectProducts.getString("is_rent");
                                    String instock = jsonObjectProducts.getString("in_stock");

                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setInstock(instock);
                                    allebikesModelClass.setTypeLayout(0);

                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                } else {
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
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
                ArrayList<String> strings = new ArrayList<>();
                strings.add(brandid);
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("brand", strings.toString());
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }


    public void getCollectionProducts(String collectionid) {
        allebikelist.clear();
        String Login_url = Apis.collectionView;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONObject jsonObjectCollection = jsonObjectData.getJSONObject("collection");
                                String collectionid = jsonObjectCollection.getString("collection_id");
                                String collectionlayouttype = jsonObjectCollection.getString("collection_layout_type");
                                if (collectionid.equals("1")) { //popular bikes
                                    JSONArray jsonArray = jsonObjectData.getJSONArray("collectionItems");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonCollection = jsonArray.getJSONObject(i);
                                        String productPrice = jsonCollection.getString("theprice");
                                        String productName = jsonCollection.getString("product_name");
                                        String selproductid = jsonCollection.getString("selprod_id");
                                        String productImageurl = jsonCollection.getString("product_image_url");
                                        String testdriveenable = jsonCollection.getString("selprod_test_drive_enable");
                                        String issell = jsonCollection.getString("is_sell");
                                        String isrent = jsonCollection.getString("is_rent");
                                        String instock = jsonCollection.getString("in_stock");

                                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                        allebikesModelClass.setPrice(productPrice);
                                        allebikesModelClass.setProductName(productName);
                                        allebikesModelClass.setProductid(selproductid);
                                        allebikesModelClass.setNetworkImage(productImageurl);
                                        allebikesModelClass.setTestdriveenable(testdriveenable);
                                        allebikesModelClass.setIssell(issell);
                                        allebikesModelClass.setIsrent(isrent);
                                        allebikesModelClass.setInstock(instock);
                                        allebikesModelClass.setTypeLayout(0);

                                        allebikelist.add(allebikesModelClass);
                                    }
                                    if (allebikelist.isEmpty()) {
                                        linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    } else {
                                        allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                        recyclerView.setAdapter(allebikesAdapter);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                        recyclerView.setLayoutManager(linearLayoutManager);
                                        allebikesAdapter.notifyDataSetChanged();
                                    }
                                }

                                if (collectionid.equals("4")) { //categories
                                    JSONArray jsonArray = jsonObjectData.getJSONArray("collectionItems");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonCat = jsonArray.getJSONObject(j);
                                        String prodcatid = jsonCat.getString("prodcat_id");
                                        String prodcatname = jsonCat.getString("prodcat_name");
                                        String prodcatblocks = jsonCat.getString("prodcat_content_block");
                                        String prodcatcounts = jsonCat.getString("productCounts");
                                        String prodcatimage = jsonCat.getString("image");
                                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                        allebikesModelClass.setProductName(prodcatname);
                                        allebikesModelClass.setProductid(prodcatid);
                                        allebikesModelClass.setNetworkImage(prodcatimage);
                                        allebikesModelClass.setTypeLayout(1);
                                        allebikelist.add(allebikesModelClass);
                                    }
                                    if (allebikelist.isEmpty()) {
                                        linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    } else {
                                        allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                        recyclerView.setAdapter(allebikesAdapter);
                                        GridLayoutManager linearLayoutManager = new GridLayoutManager(ShowAlleBikesActivity.this, 2);
                                        recyclerView.setLayoutManager(linearLayoutManager);
                                        allebikesAdapter.notifyDataSetChanged();
                                    }
                                }

                                if (collectionid.equals("5")) { //dealers
                                    JSONObject jsonObjectItems = jsonObjectData.getJSONObject("collectionItems");
                                    Iterator iter = jsonObjectItems.keys();
                                    while (iter.hasNext()) {
                                        String key = (String) iter.next();
                                        // Log.e("keys",key);
                                        JSONObject jsonObjectShops = jsonObjectItems.getJSONObject(key);
                                        String shopid = jsonObjectShops.getString("shop_id");
                                        String shopName = jsonObjectShops.getString("shop_name");
                                        String cityName = jsonObjectShops.getString("shop_city");
                                        String shopStateName = jsonObjectShops.getString("state_name");
                                        String shopCountryName = jsonObjectShops.getString("country_name");
                                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                        allebikesModelClass.setProductName(shopName);
                                        allebikesModelClass.setProductid(shopid);
                                        allebikesModelClass.setPrice(cityName + " " + shopStateName + " " + shopCountryName);
                                        allebikesModelClass.setTypeLayout(3);
                                        allebikelist.add(allebikesModelClass);
                                    }
                                    if (allebikelist.isEmpty()) {
                                        linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    } else {
                                        allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                        recyclerView.setAdapter(allebikesAdapter);
                                        GridLayoutManager linearLayoutManager = new GridLayoutManager(ShowAlleBikesActivity.this, 2);
                                        recyclerView.setLayoutManager(linearLayoutManager);
                                        allebikesAdapter.notifyDataSetChanged();
                                    }
                                }

                                if (collectionid.equals("6")) { // brands
                                    // Toast.makeText(ShowAlleBikesActivity.this, "layout: "+collectionlayouttype+"\nid "+collectionid, Toast.LENGTH_SHORT).show();
                                    JSONObject jsonobjectbrands = jsonObjectData.getJSONObject("collectionItems");
                                    JSONObject jsonObjectvallayouttype = jsonobjectbrands.getJSONObject(collectionlayouttype);
                                    JSONObject jsonObjectvalcolid = jsonObjectvallayouttype.getJSONObject(collectionid);
                                    JSONArray jsonArraybrands = jsonObjectvalcolid.getJSONArray("brands");
                                    for (int l = 0; l < jsonArraybrands.length(); l++) {
                                        JSONObject jsonObjectbrand = jsonArraybrands.getJSONObject(l);
                                        String brandid = jsonObjectbrand.getString("brand_id");
                                        String brandname = jsonObjectbrand.getString("brand_name");
                                        String branddescript = jsonObjectbrand.getString("brand_short_description");
                                        //String brandimage = jsonObjectbrand.getString("");
                                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                        allebikesModelClass.setProductName(brandname);
                                        allebikesModelClass.setPrice(branddescript);
                                        allebikesModelClass.setProductid(brandid);
                                        allebikesModelClass.setTypeLayout(2);
                                        allebikelist.add(allebikesModelClass);
                                    }
                                    if (allebikelist.isEmpty()) {
                                        linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    } else {
                                        allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                        recyclerView.setAdapter(allebikesAdapter);
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ShowAlleBikesActivity.this, 2);
                                        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                        recyclerView.setLayoutManager(gridLayoutManager);
                                        allebikesAdapter.notifyDataSetChanged();
                                    }
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
                data3.put("collection_id", collectionid);
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }


    private void setSuggestions() {

    }

}
