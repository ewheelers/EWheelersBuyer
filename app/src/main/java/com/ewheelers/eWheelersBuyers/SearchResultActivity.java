package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.eWheelersBuyers.Adapters.AllebikesAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.AllebikesModelClass;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {
    String tokenvalue;
    AllebikesAdapter allebikesAdapter;
    List<AllebikesModelClass> allebikelist = new ArrayList<>();
    RecyclerView recyclerView;
    String key;
    TextView textView,empty,tittxt;
    SearchView searchView;
    ListView list;
    ArrayList<String> strings = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        recyclerView = findViewById(R.id.all_ebikeslist);
        textView = findViewById(R.id.searchbar);
        empty = findViewById(R.id.emptyview);
        tittxt = findViewById(R.id.titleTxt);
        searchView = findViewById(R.id.searchview);
        list = findViewById(R.id.listview);
        key = getIntent().getStringExtra("keyword");
        searchView.setQuery(key,false);
        textView.setText(key);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getShopProducts(query);
                textView.setText(query);
                list.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()==0){
                    list.setVisibility(View.GONE);
                    getShopProducts("");
                    textView.setText("All Products");
                }
                else  {
                    list.setVisibility(View.VISIBLE);
                    setSuggestions(newText);
                    //getShopProducts(newText);
                    textView.setText(newText);
                }
                return false;
            }
        });

        getShopProducts(key);
        tittxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setSuggestions(String querynewtext) {
        strings.clear();
        String Login_url = Apis.searchbytags;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                                JSONArray jsonArray = jsonObjectdata.getJSONArray("suggestions");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObjectVal = jsonArray.getJSONObject(i);
                                    String value = jsonObjectVal.getString("value");
                                    strings.add(value);
                                }
                                list.setAdapter(new ArrayAdapter<String>(SearchResultActivity.this, android.R.layout.simple_spinner_dropdown_item, strings));
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String s = list.getItemAtPosition(position).toString();
                                        searchView.setQuery(s, true);
                                    }
                                });
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
                data3.put("keyword", querynewtext);
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }



    private void getShopProducts(String keyword) {
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
                                    String book = jsonObjectProducts.getString("selprod_book_now_enable");
                                    String prodbook = jsonObjectProducts.getString("product_book");
                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setProductbook(prodbook);
                                    allebikesModelClass.setBooknow(book);
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
                                    empty.setVisibility(View.VISIBLE);
                                } else {
                                    empty.setVisibility(View.GONE);
                                    allebikesAdapter = new AllebikesAdapter(SearchResultActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchResultActivity.this, RecyclerView.VERTICAL, false);
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
                data3.put("keyword", keyword);
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }

}
