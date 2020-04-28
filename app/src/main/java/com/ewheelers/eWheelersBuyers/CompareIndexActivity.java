package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.eWheelersBuyers.Adapters.ComparatorIndexAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.Comparemodelclass;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CompareIndexActivity extends AppCompatActivity {
    String save_data;
    TextView compareText, comparecount;
    List<Comparemodelclass> comparemodelclasses = new ArrayList<>();
    RecyclerView list2;
    ComparatorIndexAdapter compareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_index);
        list2 = findViewById(R.id.listtwo);
        compareText = findViewById(R.id.compareText);
        comparecount = findViewById(R.id.compareCount);
        save_data = getIntent().getStringExtra("appData");
        getCompareIndex(save_data);
    }

    private void getCompareIndex(String save_data) {
        String tokenvalue = new SessionStorage().getStrings(CompareIndexActivity.this, SessionStorage.tokenvalue);
        String Login_url = Apis.compareindex;
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
                                JSONObject jsonObjectHeaddata = jsonObjectData.getJSONObject("headData");
                                JSONObject jsonObjectComparearray = jsonObjectHeaddata.getJSONObject("productCompareArray");
                                String compare_test = jsonObjectComparearray.getString("compareText");
                                String productcount = jsonObjectComparearray.getString("productCountText");
                                compareText.setText(compare_test);
                                comparecount.setText(productcount);

                                JSONObject jsonObjectProduData = jsonObjectComparearray.getJSONObject("compareProductData");
                                List<Comparemodelclass> stringdatas = new ArrayList<>();
                                ArrayList<String> imagedataurl = new ArrayList<>();

                                Iterator subiteratorvalueslist = jsonObjectProduData.keys();
                                while (subiteratorvalueslist.hasNext()) {
                                    String subvalkey = (String) subiteratorvalueslist.next();
                                    JSONObject val = jsonObjectProduData.getJSONObject(subvalkey);
                                    String producttitle = val.getString("productTitle");
                                    String productname = val.getString("productName");
                                    String brandname = val.getString("brandName");
                                    String startprice = val.getString("startingPriceText");
                                    String imageurl = val.getString("productImageUrl");
                                    Comparemodelclass comparemodelclas = new Comparemodelclass();
                                    comparemodelclas.setProductname(productname);
                                    comparemodelclas.setProducttitle(producttitle);
                                    comparemodelclas.setBrandname(brandname);
                                    comparemodelclas.setStartprice(startprice);
                                    stringdatas.add(comparemodelclas);
                                    //stringdatas.add(productname + "\n\n" + producttitle + "\n\n" + brandname + "\n\n" + startprice);
                                    imagedataurl.add("https://ewheelers.in" + imageurl);
                                }
                                String tit1 = null, tit2 = null, tit3 = null;
                                String titone = null, tittwo = null, titthree = null;
                                String brand1 = null, brand2 = null, brand3 = null;
                                String price1 = null, price2 = null, price3 = null;

                                String imgu1 = null, imgu2 = null, imgu3 = null;
                                if (stringdatas.size() == 1 && imagedataurl.size() == 1) {
                                    tit1 = stringdatas.get(0).getProductname();
                                    titone = stringdatas.get(0).getProducttitle();
                                    brand1 = stringdatas.get(0).getBrandname();
                                    price1 = stringdatas.get(0).getStartprice();

                                    imgu1 = imagedataurl.get(0);
                                }
                                if (stringdatas.size() == 2 && imagedataurl.size() == 2) {
                                    tit1 = stringdatas.get(0).getProductname();
                                    tit2 = stringdatas.get(1).getProductname();
                                    titone = stringdatas.get(0).getProducttitle();
                                    tittwo = stringdatas.get(1).getProducttitle();
                                    brand1 = stringdatas.get(0).getBrandname();
                                    brand2 = stringdatas.get(1).getBrandname();
                                    price1 = stringdatas.get(0).getStartprice();
                                    price2 = stringdatas.get(1).getStartprice();


                                    imgu1 = imagedataurl.get(0);
                                    imgu2 = imagedataurl.get(1);

                                }
                                if (stringdatas.size() == 3 && imagedataurl.size() == 3) {
                                    tit1 = stringdatas.get(0).getProductname();
                                    tit2 = stringdatas.get(1).getProductname();
                                    tit3 = stringdatas.get(2).getProductname();
                                    titone = stringdatas.get(0).getProducttitle();
                                    tittwo = stringdatas.get(1).getProducttitle();
                                    titthree = stringdatas.get(2).getProducttitle();
                                    brand1 = stringdatas.get(0).getBrandname();
                                    brand2 = stringdatas.get(1).getBrandname();
                                    brand3 = stringdatas.get(2).getBrandname();
                                    price1 = stringdatas.get(0).getStartprice();
                                    price2 = stringdatas.get(1).getStartprice();
                                    price3 = stringdatas.get(2).getStartprice();

                                    imgu1 = imagedataurl.get(0);
                                    imgu2 = imagedataurl.get(1);
                                    imgu3 = imagedataurl.get(2);

                                }

                                JSONObject jsonObjectSellerData = jsonObjectData.getJSONObject("sellerData");
                                JSONObject jsonsellerarray = jsonObjectSellerData.getJSONObject("sellerArray");
                                ArrayList<String> stringseler = new ArrayList<>();
                                ArrayList<String> sellsublist = new ArrayList<>();
                                ArrayList<String> sellinfosublist = new ArrayList<>();

                                String sel1 = null, sel2 = null, sel3 = null;
                                String selelrtit = null, infotxt = null;
                                String sel1info = null, sel2info = null, sel3info = null;

                                Iterator selleriterator = jsonsellerarray.keys();
                                String sellertit = null;
                                while (selleriterator.hasNext()) {
                                    sellertit = (String) selleriterator.next();
                                    if (sellertit.equals("Seller")) {
                                        JSONObject jsonObjectsellerlist = jsonsellerarray.getJSONObject(sellertit);
                                        Iterator iteratorli = jsonObjectsellerlist.keys();
                                        while (iteratorli.hasNext()) {
                                            String selkey = (String) iteratorli.next();
                                            String selval = jsonObjectsellerlist.getString(selkey);
                                            sellsublist.add(selval);
                                        }
                                        if (sellsublist.size() == 1) {
                                            sel1 = sellsublist.get(0);
                                        }
                                        if (sellsublist.size() == 2) {
                                            sel1 = sellsublist.get(0);
                                            sel2 = sellsublist.get(1);
                                        }
                                        if (sellsublist.size() == 3) {
                                            sel1 = sellsublist.get(0);
                                            sel2 = sellsublist.get(1);
                                            sel3 = sellsublist.get(2);
                                        }

                                    }
                                    if (sellertit.equals("Seller Info")) {
                                        JSONObject jsonObjectsellerlist = jsonsellerarray.getJSONObject(sellertit);
                                        Iterator iteratorli = jsonObjectsellerlist.keys();
                                        while (iteratorli.hasNext()) {
                                            String selkey = (String) iteratorli.next();
                                            String selval = jsonObjectsellerlist.getString(selkey);
                                            JSONObject jsonObject1 = new JSONObject(selval);
                                            String seller_name = jsonObject1.getString("seller_name");
                                            sellinfosublist.add(seller_name);
                                        }
                                        if (sellinfosublist.size() == 1) {
                                            sel1info = sellinfosublist.get(0);
                                        }
                                        if (sellinfosublist.size() == 2) {
                                            sel1info = sellinfosublist.get(0);
                                            sel2info = sellinfosublist.get(1);
                                        }
                                        if (sellinfosublist.size() == 3) {
                                            sel1info = sellinfosublist.get(0);
                                            sel2info = sellinfosublist.get(1);
                                            sel3info = sellinfosublist.get(2);
                                        }
                                    }

                                    stringseler.add(sellertit);
                                }
                                if (stringseler.size() == 1) {
                                    selelrtit = stringseler.get(0);
                                }
                                if (stringseler.size() == 2) {
                                    selelrtit = stringseler.get(0);
                                    infotxt = stringseler.get(1);

                                }

                                JSONObject jsonObjectAttributes = jsonObjectData.getJSONObject("attributes");
                                JSONObject jsonObjectattributeArray = jsonObjectAttributes.getJSONObject("attributesArray");
                                Iterator itera = jsonObjectattributeArray.keys();
                                while (itera.hasNext()) {
                                    String key = (String) itera.next();
                                    Comparemodelclass comparemodelclass = new Comparemodelclass();
                                    comparemodelclass.setHeading(key);

                                    comparemodelclass.setTitle1(tit1);
                                    comparemodelclass.setTitle2(tit2);
                                    comparemodelclass.setTitle3(tit3);

                                    comparemodelclass.setTitone(titone);
                                    comparemodelclass.setTittwo(tittwo);
                                    comparemodelclass.setTitthree(titthree);

                                    comparemodelclass.setBrandone(brand1);
                                    comparemodelclass.setBrandtwo(brand2);
                                    comparemodelclass.setBrandthree(brand3);

                                    comparemodelclass.setPriceone(price1);
                                    comparemodelclass.setPricetwo(price2);
                                    comparemodelclass.setPricethree(price3);

                                    comparemodelclass.setImageurl(imgu1);
                                    comparemodelclass.setImageurl2(imgu2);
                                    comparemodelclass.setImageurl3(imgu3);

                                    comparemodelclass.setSellertitle(selelrtit);
                                    comparemodelclass.setSellerinfo(infotxt);

                                    comparemodelclass.setSeller1(sel1);
                                    comparemodelclass.setSeller2(sel2);
                                    comparemodelclass.setSeller3(sel3);

                                    comparemodelclass.setSellinfo1(sel1info);
                                    comparemodelclass.setSelinfo2(sel2info);
                                    comparemodelclass.setSelinfo3(sel3info);

                                    comparemodelclass.setTypeofLay(0);
                                    comparemodelclasses.add(comparemodelclass);

                                    JSONObject jsonObjectofKey = jsonObjectattributeArray.getJSONObject(key);
                                    Iterator subiterator = jsonObjectofKey.keys();
                                    while (subiterator.hasNext()) {
                                        String subkey = (String) subiterator.next();

                                        JSONObject jsonObjectSpecs = jsonObjectofKey.getJSONObject(subkey);
                                        JSONObject jsonObjectSave = new JSONObject(save_data);
                                        JSONObject jsonObjectProducts = jsonObjectSave.getJSONObject("products");
                                        ArrayList<String> strings = new ArrayList<>();
                                        Iterator subiteratorvalues = jsonObjectProducts.keys();
                                        while (subiteratorvalues.hasNext()) {
                                            String subvalkey = (String) subiteratorvalues.next();
                                            String val = jsonObjectProducts.getString(subvalkey);
                                            strings.add(val);
                                        }
                                        String prod1 = null, prod2 = null, prod3 = null;

                                        if (strings.size() == 1) {
                                            prod1 = jsonObjectSpecs.getString(strings.get(0));
                                        }
                                        if (strings.size() == 2) {
                                            prod1 = jsonObjectSpecs.getString(strings.get(0));
                                            prod2 = jsonObjectSpecs.getString(strings.get(1));
                                        }
                                        if (strings.size() == 3) {
                                            prod1 = jsonObjectSpecs.getString(strings.get(0));
                                            prod2 = jsonObjectSpecs.getString(strings.get(1));
                                            prod3 = jsonObjectSpecs.getString(strings.get(2));
                                        }

                                        comparemodelclass = new Comparemodelclass();
                                        comparemodelclass.setValues(subkey);
                                        comparemodelclass.setSubvalue(prod1);
                                        comparemodelclass.setSubvalue2(prod2);
                                        comparemodelclass.setSubvalue3(prod3);
                                        comparemodelclass.setTypeofLay(1);
                                        comparemodelclasses.add(comparemodelclass);

                                    }

                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CompareIndexActivity.this, RecyclerView.VERTICAL, false);
                                list2.setLayoutManager(linearLayoutManager);
                                compareAdapter = new ComparatorIndexAdapter(CompareIndexActivity.this, comparemodelclasses);
                                list2.setAdapter(compareAdapter);
                            } else {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
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
                data3.put("appData", save_data);
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

}
