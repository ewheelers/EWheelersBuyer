package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.AllebikesAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.SellerListAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.ServiceProvidersAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.AllebikesModelClass;
import com.ewheelers.eWheelersBuyers.ModelClass.SellerListModel;
import com.ewheelers.eWheelersBuyers.ModelClass.ServiceProvidersClass;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

public class SellersListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SellerListAdapter sellerListAdapter;
    List<SellerListModel> sellerListModelList = new ArrayList<>();
    List<AllebikesModelClass> allebikesModelClassesList = new ArrayList<>();
    String shopName, shopAddres, selectprodid, tokenValue, typeofactivity;
    ProgressBar progressBar;
    AllebikesAdapter allebikesAdapter;
    TextView heading;
    SearchView searchView;
    TextView textView;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_list);
        textView = findViewById(R.id.emptyview);
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.sellers_list);
        heading = findViewById(R.id.headtitle);
        searchView = findViewById(R.id.searchview);
        tokenValue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        typeofactivity = getIntent().getStringExtra("fromactivity");
        selectprodid = getIntent().getStringExtra("selproductid");
        shopAddres = getIntent().getStringExtra("shopaddress");
        shopName = getIntent().getStringExtra("shopname");
        longitude = new NavAppBarActivity().setlongitude();
        latitude = new NavAppBarActivity().setlatitude();
        if (typeofactivity.equals("details")) {
            heading.setText("Dealers");
            getSellerList();
        } else {
            heading.setText("All Dealers");
            getAllShop();
        }

        heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getAllShops() {
        progressBar.setVisibility(View.VISIBLE);
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

                    allebikesAdapter = new AllebikesAdapter(SellersListActivity.this, allebikesModelClassesList);
                    recyclerView.setAdapter(allebikesAdapter);
                    GridLayoutManager linearLayoutManager = new GridLayoutManager(SellersListActivity.this, 2);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    allebikesAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            allebikesAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                VolleyLog.d("Main", "Error: " + error.getMessage());
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
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }


    private void getAllShop() {
        progressBar.setVisibility(View.VISIBLE);
        allebikesModelClassesList.clear();
        String url_link = Apis.getallshops;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equals("1")) {
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
                            String autocompleteaddres = address.getString("shop_auto_complete");
                            AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                            allebikesModelClass.setProductName(shopname);
                            allebikesModelClass.setProductid(shopid);
                            allebikesModelClass.setShopphone(shopphoneno);
                            //allebikesModelClass.setPrice(shopcity + " " + shopstate + " " + shopcontry + " - " + shoppincode);
                            allebikesModelClass.setPrice(autocompleteaddres);
                            allebikesModelClass.setTypeLayout(4);
                            allebikesModelClassesList.add(allebikesModelClass);
                        }
                        if(allebikesModelClassesList.isEmpty()){
                            textView.setVisibility(View.VISIBLE);
                        }else {
                            textView.setVisibility(GONE);
                            allebikesAdapter = new AllebikesAdapter(SellersListActivity.this, allebikesModelClassesList);
                            recyclerView.setAdapter(allebikesAdapter);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SellersListActivity.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            allebikesAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(GONE);
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    // Toast.makeText(ShowAlleBikesActivity.this, "keyword:" + query, Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    allebikesAdapter.getFilter().filter(newText);
                                    return false;
                                }
                            });
                        }
                    }else {
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
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
                progressBar.setVisibility(GONE);

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
                //data3.put("page","100");
                return data3;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }


    public void getSellerList() {
        progressBar.setVisibility(View.VISIBLE);
        String url_link = Apis.sellerslist + selectprodid;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        JSONObject jsonObjectProduct = jsonObjectData.getJSONObject("product");
                        String booknow = jsonObjectProduct.getString("selprod_book_now_enable");
                        String testnow = jsonObjectProduct.getString("selprod_test_drive_enable");
                        JSONArray jsonArray = jsonObjectProduct.getJSONArray("moreSellersArr");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject address = jsonArray.getJSONObject(i);
                            String sellerprodid = address.getString("selprod_id");
                            String sellingprice = address.getString("selprod_price");
                            String codavail = address.getString("selprod_cod_enabled");
                            String sellershopname = address.getString("shop_name");
                            String sellerlatitude = address.getString("shop_latitude");
                            String sellerlongitude = address.getString("shop_longitude");
                            String sellerphoneno = address.getString("shop_phone");
                            String sellercity = address.getString("shop_city");
                            String sellerstate = address.getString("shop_state_name");
                            String sellercountry = address.getString("shop_country_name");
                            String postalcode = address.getString("shop_postalcode");
                            SellerListModel sellerListModel = new SellerListModel();
                            sellerListModel.setSelproductid(sellerprodid);
                            sellerListModel.setSellerPrice(sellingprice);
                            sellerListModel.setSellerCod(codavail);
                            sellerListModel.setSellersname(sellershopname);
                            sellerListModel.setSellerslatitude(sellerlatitude);
                            sellerListModel.setSellerslongitude(sellerlongitude);
                            sellerListModel.setSellersphoneno(sellerphoneno);
                            sellerListModel.setSellersaddress(sellercity + " " + sellerstate + " " + sellercountry + " - " + postalcode);
                            //sellerListModelList.add(sellerListModel);
                            getDistance(latitude, longitude, sellerlatitude, sellerlongitude, sellerListModel);

                        }
                       /* if (sellerListModelList.isEmpty()) {
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            textView.setVisibility(GONE);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SellersListActivity.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            sellerListAdapter = new SellerListAdapter(SellersListActivity.this, sellerListModelList);
                            recyclerView.setAdapter(sellerListAdapter);
                            sellerListAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    sellerListAdapter.getFilter().filter(newText);
                                    return false;
                                }
                            });
                        }*/
                    } else {
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                VolleyLog.d("Main", "Error: " + error.getMessage());
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
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    public void getDistance(double currlat, double currlng, String servicelat, String servicelng, SellerListModel serviceProvidersClass) {
        sellerListModelList.clear();
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + currlat + "," + currlng + "&destinations=" + servicelat + "," + servicelng + "&key=AIzaSyAyuQjmdCe43w40mbR422_ix8QPzgDbgxs";
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray dist = jsonObject.getJSONArray("rows");
                            JSONObject obj2 = (JSONObject) dist.get(0);
                            JSONArray disting = (JSONArray) obj2.get("elements");
                            JSONObject obj3 = (JSONObject) disting.get(0);
                            JSONObject obj4 = (JSONObject) obj3.get("distance");
                            JSONObject obj5 = (JSONObject) obj3.get("duration");
                            String distance = obj4.getString("text");

                            serviceProvidersClass.setDistance(distance);

                            sellerListModelList.add(serviceProvidersClass);


                            if (sellerListModelList.isEmpty()) {
                                textView.setVisibility(View.VISIBLE);
                            } else {

                                /*Collections.sort(sellerListModelList, new Comparator<SellerListModel>() {
                                    @Override
                                    public int compare(SellerListModel o1, SellerListModel o2) {
                                        return o1.getDistance().compareTo(o2.getDistance());
                                    }
                                });*/

                                textView.setVisibility(GONE);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SellersListActivity.this, RecyclerView.VERTICAL, false);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                sellerListAdapter = new SellerListAdapter(SellersListActivity.this, sellerListModelList);
                                recyclerView.setAdapter(sellerListAdapter);
                                sellerListAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(GONE);
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        sellerListAdapter.getFilter().filter(newText);
                                        return false;
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
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);
    }


}
