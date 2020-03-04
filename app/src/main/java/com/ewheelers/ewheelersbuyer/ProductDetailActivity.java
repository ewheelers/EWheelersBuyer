package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.ProductdetailsAdapter;
import com.ewheelers.ewheelersbuyer.Fragments.PolicyFragment;
import com.ewheelers.ewheelersbuyer.ModelClass.OptionValues;
import com.ewheelers.ewheelersbuyer.ModelClass.ProductDetails;
import com.ewheelers.ewheelersbuyer.ModelClass.ProductSpecifications;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {
    String productId;
    RecyclerView recyclerView, options, offersrecyclerview, buywithlistview, similarproductsview, bottomButtonView;
    ImageView imageView;
    ProductdetailsAdapter productdetailsAdapter, productdetailsAdapter2, productdetailsAdapterbuywith, productdetailsAdapterSimilar;
    ProductDetails productDetailsButton;
    List<ProductDetails> productDetailsList = new ArrayList<>();
    List<ProductDetails> buyDetailsList = new ArrayList<>();
    List<ProductDetails> similarList = new ArrayList<>();
    List<ProductDetails> optionsList = new ArrayList<>();
    List<ProductSpecifications> productSpecs = new ArrayList<>();
    List<OptionValues> spinnerlist = new ArrayList<>();
    List<ProductDetails> buttondata = new ArrayList<>();
    ImageLoader imageLoader;
    TextView textView_product_details, shareicon;
    private InputMethodManager imm;
    String imageurls, productdescription, selproductid, productname, productprice, productmodel, isRent, testDriveEnable, booknowEnable;
    public static String selectProId = "";
    TextView brand, productName, cost, policytext, totalreview, totalrating, buywithtit, similarproductTitle;
    String optionname1;

    TextView rentavailbility;

    String paymetnpolicy, deliverpolicy, refundpolicy, shopname, countryname, statename, city, shopuserid;
    FrameLayout frameLayout;
    Button butn_payPolicy, delivery_policy, refund_policy, return_policy, cancellation_policy;
    LinearLayout linearLayoutrent;

    Button plus, minus;
    TextView editqty;
    int quantity = 1;
    TextView dealers_List;
    Button addcart;
    RelativeLayout snackbarLayout;

    TextView cartCount;
    String tokenvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        productId = getIntent().getStringExtra("productid");
        Toast.makeText(this, "token val: " + tokenvalue, Toast.LENGTH_SHORT).show();


        //  Toast.makeText(this, "selected id from home:"+productId, Toast.LENGTH_SHORT).show();

       /* try {
            imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        cartCount = findViewById(R.id.cart_count);
        snackbarLayout = findViewById(R.id.snak_layout);
        addcart = findViewById(R.id.add_cart);
        editqty = findViewById(R.id.edit_number);
        plus = findViewById(R.id.plus_img);
        minus = findViewById(R.id.minus_img);
        linearLayoutrent = findViewById(R.id.rentlayout);
        rentavailbility = findViewById(R.id.rentAvail);
        butn_payPolicy = findViewById(R.id.payment_policy);
        delivery_policy = findViewById(R.id.delivery_policy);
        refund_policy = findViewById(R.id.refund_policy);
        return_policy = findViewById(R.id.return_policy);
        cancellation_policy = findViewById(R.id.cancellation_policy);
        bottomButtonView = findViewById(R.id.bottom_buttons);
        frameLayout = findViewById(R.id.framecontent);
        shareicon = findViewById(R.id.share);
        brand = findViewById(R.id.brand_name);
        productName = findViewById(R.id.product_name);
        cost = findViewById(R.id.product_price);

        options = findViewById(R.id.options_recyclerview);
        recyclerView = findViewById(R.id.horizontal_view);
        offersrecyclerview = findViewById(R.id.offers_listview);
        buywithlistview = findViewById(R.id.buywith_listview);
        similarproductsview = findViewById(R.id.similar_listview);

        imageView = findViewById(R.id.imageswitcher);
        textView_product_details = findViewById(R.id.product_details);
        // policytext = findViewById(R.id.policy);
        totalreview = findViewById(R.id.totalreviews);
        totalrating = findViewById(R.id.totalratings);
        buywithtit = findViewById(R.id.buywithtitle);
        similarproductTitle = findViewById(R.id.similarproductstxt);
        dealers_List = findViewById(R.id.dealers_list);


        textView_product_details.setOnClickListener(this);
        shareicon.setOnClickListener(this);
        butn_payPolicy.setOnClickListener(this);
        delivery_policy.setOnClickListener(this);
        refund_policy.setOnClickListener(this);
        return_policy.setOnClickListener(this);
        cancellation_policy.setOnClickListener(this);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        dealers_List.setOnClickListener(this);
        addcart.setOnClickListener(this);
        cartCount.setOnClickListener(this);

        getProductDetails(productId);

        ProductdetailsAdapter productdetailsAdapter = new ProductdetailsAdapter(ProductDetailActivity.this, getOfferData());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        offersrecyclerview.setLayoutManager(linearLayoutManager);
        offersrecyclerview.setAdapter(productdetailsAdapter);
        productdetailsAdapter.notifyDataSetChanged();

    }

    public String selctedprod_ID() {
        return productId;
    }

    private List<ProductDetails> getOfferData() {
        List<ProductDetails> offerData = new ArrayList<>();
        offerData.add(new ProductDetails(2, "Cash back", R.drawable.ic_rupee));
        offerData.add(new ProductDetails(2, "Test drive", R.drawable.ic_bestoffer));
        offerData.add(new ProductDetails(2, "Exchange", R.drawable.ic_exchange));
        offerData.add(new ProductDetails(2, " Finance", R.drawable.ic_rupee));
        return offerData;
    }

    public void getProductDetails(String productid) {
        productDetailsList.clear();
        buyDetailsList.clear();
        similarList.clear();
        optionsList.clear();
        productSpecs.clear();
        buttondata.clear();
        //optionvalueList.clear();
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.productdetails + productid;
        Log.i("url", serverurl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                    JSONArray jsonArrayDataOptions = dataJsonObject.getJSONArray("optionRows");
                    for (int optionarray = 0; optionarray < jsonArrayDataOptions.length(); optionarray++) {
                        JSONObject jsonObjectOptionTitle = jsonArrayDataOptions.getJSONObject(optionarray);
                        optionname1 = jsonObjectOptionTitle.getString("option_name");

                        JSONArray jsonArrayOptionValue = jsonObjectOptionTitle.getJSONArray("values");

                        List<OptionValues> optionValueList = new ArrayList<OptionValues>();

                        for (int k = 0; k < jsonArrayOptionValue.length(); k++) {
                            JSONObject option_value = jsonArrayOptionValue.getJSONObject(k);
                            String optionUrlvalue = option_value.getString("optionUrlValue");

                            OptionValues optionValues = new OptionValues();
                            optionValues.setOptionUrlValue(optionUrlvalue);
                            optionValues.setOptionValuenames(option_value.getString("optionvalue_name"));
                            optionValueList.add(optionValues);
                            spinnerlist = optionValueList;
                        }
                        ProductDetails productDetailsoptions = new ProductDetails();
                        productDetailsoptions.setOptionName(optionname1);
                        productDetailsoptions.setOptionValuesArrayList(spinnerlist);
                        productDetailsoptions.setTypeoflayout(0);
                        optionsList.add(productDetailsoptions);
                    }

                    JSONObject jsonArraySpecifications = dataJsonObject.getJSONObject("productSpecifications");
                    JSONArray jsonArraySpecData = jsonArraySpecifications.getJSONArray("data");
                    for (int specdata = 0; specdata < jsonArraySpecData.length(); specdata++) {
                        JSONObject jsonObjectdata = jsonArraySpecData.getJSONObject(specdata);
                        String specid = jsonObjectdata.getString("prodspec_id");
                        String spec_name = jsonObjectdata.getString("prodspec_name");
                        String spec_value = jsonObjectdata.getString("prodspec_value");
                        ProductSpecifications productSpecifications = new ProductSpecifications();
                        productSpecifications.setProdspecid(specid);
                        productSpecifications.setProductspecname(spec_name);
                        productSpecifications.setProductspecvalue(spec_value);
                        productSpecs.add(productSpecifications);
                    }

                    JSONObject jsonObjectProduct = dataJsonObject.getJSONObject("product");
                    JSONObject jsonObjectdata = jsonObjectProduct.getJSONObject("data");
                    selproductid = jsonObjectdata.getString("selprod_id");
                    productname = jsonObjectdata.getString("product_name");
                    productprice = jsonObjectdata.getString("selprod_price");
                    productmodel = jsonObjectdata.getString("product_model");
                    productdescription = jsonObjectdata.getString("product_description");
                    isRent = jsonObjectdata.getString("is_rent");
                    testDriveEnable = jsonObjectdata.getString("selprod_test_drive_enable");
                    booknowEnable = jsonObjectdata.getString("selprod_book_now_enable");
                    String rentPrice = jsonObjectdata.getString("rent_price");
                    brand.setText("( " + productmodel + " ) " + productname);
                    cost.setText("\u20B9 " + productprice);

                    if (booknowEnable.equals("1")) {
                        linearLayoutrent.setVisibility(View.GONE);
                        buttondata.add(new ProductDetails(5, "Book Now", R.color.colorPrimary, selproductid));
                    }
                    if (testDriveEnable.equals("1")) {
                        linearLayoutrent.setVisibility(View.GONE);
                        buttondata.add(new ProductDetails(5, "Test Drive", R.color.colorGrey, selproductid));
                    }
                    if (isRent.equals("1")) {
                        linearLayoutrent.setVisibility(View.VISIBLE);
                        rentavailbility.setText("Get it on Rent for just\n" + "\u20B9 " + rentPrice);
                        buttondata.add(new ProductDetails(5, "Rent", R.color.colorPrimary, selproductid));
                    }


                    buttondata.add(new ProductDetails(5, "BUY", R.color.colorPrimary, selproductid));


                    JSONObject jsonObjectpolicies = dataJsonObject.getJSONObject("shop");
                    paymetnpolicy = jsonObjectpolicies.getString("shop_payment_policy");
                    deliverpolicy = jsonObjectpolicies.getString("shop_delivery_policy");
                    refundpolicy = jsonObjectpolicies.getString("shop_refund_policy");
                    shopname = jsonObjectpolicies.getString("shop_name");
                    countryname = jsonObjectpolicies.getString("shop_country_name");
                    statename = jsonObjectpolicies.getString("shop_state_name");
                    city = jsonObjectpolicies.getString("shop_city");
                    dealers_List.setText(shopname);

                    butn_payPolicy.setBackgroundColor(Color.parseColor("#9C3C34"));
                    butn_payPolicy.setTextColor(Color.WHITE);
                    SwitchFragment(new PolicyFragment().newInstance(paymetnpolicy));

                    // policytext.setText("Payment \n" + paymetnpolicy + "\n\nShipping \n" + deliverpolicy + "\n\nRefund \n" + refundpolicy);

                    String totalReviews = dataJsonObject.getString("shopTotalReviews");
                    totalreview.setText("Based on " + totalReviews + " ratings");
                    totalrating.setText(totalReviews);

                    JSONObject jsonobjectBuyTogether = dataJsonObject.getJSONObject("buyTogether");
                    String buywithTitle = jsonobjectBuyTogether.getString("title");
                    buywithtit.setText(buywithTitle);
                    JSONArray jsonArraybuyWithData = jsonobjectBuyTogether.getJSONArray("data");
                    for (int buytogetherobjects = 0; buytogetherobjects < jsonArraybuyWithData.length(); buytogetherobjects++) {
                        JSONObject jsonObjectbuywith = jsonArraybuyWithData.getJSONObject(buytogetherobjects);
                        String productimgurl = jsonObjectbuywith.getString("product_image_url");
                        String productName = jsonObjectbuywith.getString("product_name");
                        String productPrice = jsonObjectbuywith.getString("selprod_price");
                        String selectedProductId = jsonObjectbuywith.getString("selprod_product_id");

                        ProductDetails productDetails = new ProductDetails();
                        productDetails.setBuywithimageurl(productimgurl);
                        productDetails.setBuywithproductname(productName);
                        productDetails.setBuywithproductprice(productPrice);
                        productDetails.setButwithselectedProductId(selectedProductId);
                        productDetails.setTypeoflayout(3);
                        buyDetailsList.add(productDetails);

                    }

                    JSONObject jsonobjectSimilar = dataJsonObject.getJSONObject("relatedProducts");
                    String similarTitle = jsonobjectSimilar.getString("title");
                    similarproductTitle.setText(similarTitle);
                    JSONArray jsonArraySimilarData = jsonobjectSimilar.getJSONArray("data");
                    for (int buysimilarobjects = 0; buysimilarobjects < jsonArraySimilarData.length(); buysimilarobjects++) {
                        JSONObject jsonObjectsimilar = jsonArraySimilarData.getJSONObject(buysimilarobjects);
                        String productimgurl = jsonObjectsimilar.getString("product_image_url");
                        String productName = jsonObjectsimilar.getString("product_name");
                        String productPrice = jsonObjectsimilar.getString("selprod_price");
                        String selsimilarproductid = jsonObjectsimilar.getString("selprod_id");
                        ProductDetails productDetails = new ProductDetails();
                        productDetails.setSimilarimageurl(productimgurl);
                        productDetails.setSimilarproductname(productName);
                        productDetails.setSimilarproductprice(productPrice);
                        productDetails.setSimilarproductid(selsimilarproductid);
                        productDetails.setTypeoflayout(4);
                        similarList.add(productDetails);
                    }


                    JSONArray jsonArrayCollections = dataJsonObject.getJSONArray("productImagesArr");
                    for (int i = 0; i < jsonArrayCollections.length(); i++) {
                        JSONObject jsonObjectProductdetail = jsonArrayCollections.getJSONObject(i);
                        imageurls = jsonObjectProductdetail.getString("product_image_url");
                        ProductDetails productDetails = new ProductDetails();
                        productDetails.setProductimg_url(imageurls);
                        productDetails.setTypeoflayout(1);
                        productDetailsList.add(productDetails);
                    }


                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    productdetailsAdapter = new ProductdetailsAdapter(ProductDetailActivity.this, productDetailsList);
                    recyclerView.setAdapter(productdetailsAdapter);
                    productdetailsAdapter.notifyDataSetChanged();

                    LinearLayoutManager gridLayoutManager = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.VERTICAL, false);
                    options.setLayoutManager(gridLayoutManager);
                    productdetailsAdapter2 = new ProductdetailsAdapter(ProductDetailActivity.this, optionsList);
                    options.setAdapter(productdetailsAdapter2);
                    productdetailsAdapter2.notifyDataSetChanged();

                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.VERTICAL, false);
                    buywithlistview.setLayoutManager(linearLayoutManager2);
                    productdetailsAdapterbuywith = new ProductdetailsAdapter(ProductDetailActivity.this, buyDetailsList);
                    buywithlistview.setAdapter(productdetailsAdapterbuywith);
                    productdetailsAdapterbuywith.notifyDataSetChanged();

                    GridLayoutManager gridLayoutManagerSimilar = new GridLayoutManager(ProductDetailActivity.this, 2);
                    similarproductsview.setLayoutManager(gridLayoutManagerSimilar);
                    productdetailsAdapterSimilar = new ProductdetailsAdapter(ProductDetailActivity.this, similarList);
                    similarproductsview.setAdapter(productdetailsAdapterSimilar);
                    productdetailsAdapterSimilar.notifyDataSetChanged();

                    LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
                    bottomButtonView.setLayoutManager(linearLayoutManager3);
                    ProductdetailsAdapter productdetailsAdapter3 = new ProductdetailsAdapter(ProductDetailActivity.this, buttondata);
                    bottomButtonView.setAdapter(productdetailsAdapter3);
                    productdetailsAdapter3.notifyDataSetChanged();

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
                params.put("HTTP_X_TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        queue.add(stringRequest);
    }


    public void onClickcalled(String url) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_dashboard_black_24dp)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(imageView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_count:
                Intent i = new Intent(getApplicationContext(), CartListingActivity.class);
                startActivity(i);
                break;
            case R.id.add_cart:
                addTocart();
                break;
            case R.id.dealers_list:
                selectDealerDialog();
                break;
            case R.id.plus_img:
                quantity++;
                editqty.setText(String.valueOf(quantity));
                break;
            case R.id.minus_img:
                if (quantity > 1)
                    quantity--;
                editqty.setText(String.valueOf(quantity));
                break;
            case R.id.product_details:
                Intent intent = new Intent(ProductDetailActivity.this, ProductDescriptionActivity.class);
                intent.putExtra("description", productdescription);
                intent.putExtra("Specifications", (Serializable) productSpecs);
                intent.putExtra("image", imageurls);
                intent.putExtra("title", productname);
                intent.putExtra("price", productprice);
                intent.putExtra("model", productmodel);
                startActivity(intent);
                break;
            case R.id.share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\neBike is for everyone. Let me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.payment_policy:
                butn_payPolicy.setBackgroundColor(Color.parseColor("#9C3C34"));
                butn_payPolicy.setTextColor(Color.WHITE);
                delivery_policy.setBackgroundColor(Color.WHITE);
                delivery_policy.setTextColor(Color.BLACK);
                refund_policy.setBackgroundColor(Color.WHITE);
                refund_policy.setTextColor(Color.BLACK);
                return_policy.setBackgroundColor(Color.WHITE);
                return_policy.setTextColor(Color.BLACK);
                cancellation_policy.setBackgroundColor(Color.WHITE);
                cancellation_policy.setTextColor(Color.BLACK);
                SwitchFragment(new PolicyFragment().newInstance(paymetnpolicy));
                break;
            case R.id.delivery_policy:
                delivery_policy.setBackgroundColor(Color.parseColor("#9C3C34"));
                delivery_policy.setTextColor(Color.WHITE);
                butn_payPolicy.setBackgroundColor(Color.WHITE);
                butn_payPolicy.setTextColor(Color.BLACK);
                refund_policy.setBackgroundColor(Color.WHITE);
                refund_policy.setTextColor(Color.BLACK);
                return_policy.setBackgroundColor(Color.WHITE);
                return_policy.setTextColor(Color.BLACK);
                cancellation_policy.setBackgroundColor(Color.WHITE);
                cancellation_policy.setTextColor(Color.BLACK);
                SwitchFragment(new PolicyFragment().newInstance(deliverpolicy));
                break;
            case R.id.refund_policy:
                refund_policy.setBackgroundColor(Color.parseColor("#9C3C34"));
                refund_policy.setTextColor(Color.WHITE);
                delivery_policy.setBackgroundColor(Color.WHITE);
                delivery_policy.setTextColor(Color.BLACK);
                butn_payPolicy.setBackgroundColor(Color.WHITE);
                butn_payPolicy.setTextColor(Color.BLACK);
                return_policy.setBackgroundColor(Color.WHITE);
                return_policy.setTextColor(Color.BLACK);
                cancellation_policy.setBackgroundColor(Color.WHITE);
                cancellation_policy.setTextColor(Color.BLACK);
                SwitchFragment(new PolicyFragment().newInstance(refundpolicy));
                break;
            case R.id.return_policy:
                return_policy.setBackgroundColor(Color.parseColor("#9C3C34"));
                return_policy.setTextColor(Color.WHITE);
                delivery_policy.setBackgroundColor(Color.WHITE);
                delivery_policy.setTextColor(Color.BLACK);
                refund_policy.setBackgroundColor(Color.WHITE);
                refund_policy.setTextColor(Color.BLACK);
                butn_payPolicy.setBackgroundColor(Color.WHITE);
                butn_payPolicy.setTextColor(Color.BLACK);
                cancellation_policy.setBackgroundColor(Color.WHITE);
                cancellation_policy.setTextColor(Color.BLACK);
                SwitchFragment(new PolicyFragment().newInstance(""));
                break;
            case R.id.cancellation_policy:
                cancellation_policy.setBackgroundColor(Color.parseColor("#9C3C34"));
                cancellation_policy.setTextColor(Color.WHITE);
                delivery_policy.setBackgroundColor(Color.WHITE);
                delivery_policy.setTextColor(Color.BLACK);
                refund_policy.setBackgroundColor(Color.WHITE);
                refund_policy.setTextColor(Color.BLACK);
                return_policy.setBackgroundColor(Color.WHITE);
                return_policy.setTextColor(Color.BLACK);
                butn_payPolicy.setBackgroundColor(Color.WHITE);
                butn_payPolicy.setTextColor(Color.BLACK);
                SwitchFragment(new PolicyFragment().newInstance(""));
                break;
            case R.id.rentAvail:
                break;
        }

    }

    public class OpencartListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), CartListingActivity.class);
            startActivity(i);
        }
    }

    private void addTocart() {
        String Login_url = Apis.addtocart;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            Toast.makeText(ProductDetailActivity.this, "addcart msg:" + message, Toast.LENGTH_SHORT).show();
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            String cartcount = jsonObjectData.getString("cartItemsCount");
                            //setCartItemCount(cartcount);
                            cartCount.setText(cartcount);
                            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout),
                                    "Added to cart", Snackbar.LENGTH_LONG);
                            mySnackbar.setAction("Opencart", new OpencartListner());
                            mySnackbar.show();
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
                //params.put("Content-Type", "application/json");
                params.put("HTTP_X_TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();

                data3.put("selprod_id", selproductid);
                data3.put("quantity", editqty.getText().toString());
                data3.put("addons", "");

                return data3;

            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }


    private String setCartItemCount(String cartcount) {
        return cartcount;
    }


    public void SwitchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framecontent, fragment).commit();
    }

    private void selectDealerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Dealers")
                .setMessage(shopname + "\n" + city + "," + statename + "," + countryname)
                .show();
    }


}
