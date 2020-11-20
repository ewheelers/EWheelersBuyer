package com.ewheelers.eWheelersBuyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Adapters.AddonsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.ProductdetailsAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.SellerListAdapter;
import com.ewheelers.eWheelersBuyers.Fragments.PolicyFragment;
import com.ewheelers.eWheelersBuyers.ModelClass.AddonsClass;
import com.ewheelers.eWheelersBuyers.ModelClass.Comparemodelclass;
import com.ewheelers.eWheelersBuyers.ModelClass.OptionValues;
import com.ewheelers.eWheelersBuyers.ModelClass.ProductDetails;
import com.ewheelers.eWheelersBuyers.ModelClass.ProductSpecifications;
import com.ewheelers.eWheelersBuyers.ModelClass.SellerListModel;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tooltip.Tooltip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;
import static com.ewheelers.eWheelersBuyers.Dialogs.ShowAlerts.showfailedDialog;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    TextView distantance;
    String productId, cart_Items;
    RecyclerView recyclerView, options, buywithlistview, similarproductsview, bottomButtonView;
    ImageView imageView;
    ProductdetailsAdapter productdetailsAdapter, productdetailsAdapter2, productdetailsAdapterbuywith, productdetailsAdapterSimilar;
    AddonsAdapter addonsAdapter;
    String value = "";
    List<ProductDetails> productDetailsList = new ArrayList<>();
    ArrayList<ProductDetails> optionselect = new ArrayList<>();

    List<AddonsClass> buyDetailsList = new ArrayList<>();
    List<ProductDetails> bannerslist = new ArrayList<>();
    List<ProductDetails> similarList = new ArrayList<>();
    List<ProductDetails> optionsList = new ArrayList<>();
    List<ProductSpecifications> productSpecs = new ArrayList<>();
    ArrayList<OptionValues> spinnerlist = new ArrayList<>();
    List<ProductDetails> buttondata = new ArrayList<>();
    List<Comparemodelclass> comparemodelclasses = new ArrayList<>();

    ImageLoader imageLoader;
    TextView textView_product_details, shareicon, offerstoshow;
    private InputMethodManager imm;
    String shop_id,shop_name,shoplogo,shopbanner;
    String imageurls, productdescription, selproductid, productname, productprice, productmodel, isRent, testDriveEnable, booknowEnable, selbooknowEnable;
    String rentPrice, rentSecurity, bookPercentage, minrentduration;
    TextView brand, cost, totalreview, totalrating, buywithtit, similarproductTitle;
    //WebView productName;
    String optionname1, attTitle;

    TextView rentavailbility, findstore, booknowTxt, testAvailtxt;

    String paymetnpolicy, deliverpolicy, refundpolicy, shopname, countryname, statename, city, shopuserid;
    FrameLayout frameLayout;
    Button butn_payPolicy, delivery_policy, refund_policy, return_policy, cancellation_policy;
    LinearLayout linearLayoutrent, bookLayout, testLayout;

    Button plus, minus;
    TextView editqty;
    int quantity = 1;
    TextView dealers_List, brand_name, brand_descr;
    //Spinner dealers_List;
    Button addcart;
    TextView cartCount, buyerGuide;
    String tokenvalue;
    String jsonaddon;
    String url, description, titile;

    ImageView closereqbtnimg;
    JSONObject selectedOptionValues;
    String optionvalue_id = "";
    String optionlistid = "";
    LinearLayout compareView;
    TextView comparetxt, viewcount, zoomImg;
    ImageView imageViewClose;
    String pro1, pro2, savedData, plength;
    SearchView editTextSearch;
    TextView changeTxt, addressdealer, moreby_brand;
    String brand_Shortdesc, short_descript, brand_Name, brand_Id, shop_Id , shop_Name;
    Button requestTestDrive;
    ExtendedFloatingActionButton extendedFloatingButton;
    Boolean isOpen = false;
    FloatingActionButton fab_main;
    android.widget.SearchView chooseserach;
    ListView listView;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    RelativeLayout clrelay;
    Dialog mBottomSheetDialog;
    String attr_grp_cat_id;
    ArrayList<String> strings = new ArrayList<>();

    ProgressBar progressBar, mainprogress;
    RecyclerView recyclerViewBanners;
    boolean clicktype = false;
    NewGPSTracker newgps;
    Context mContext;
    Geocoder geocoder;
    List<Address> addresses;
    ImageView findaddress;
    TextView calltoseller;
    String shoplatitude;
    String shoplogitude;
    String shopmobile, username;
    LinearLayout goto_shop;
    ImageView logoImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        productId = getIntent().getStringExtra("productid");
        getSellerList(productId);
        mContext = this;
        calltoseller = findViewById(R.id.call_seller);
        findaddress = findViewById(R.id.navigate_map);
        goto_shop = findViewById(R.id.goto_shop);
        goto_shop.setOnClickListener(this);
        logoImg = findViewById(R.id.logo_img);
        //Toast.makeText(this, "from: " + productId, Toast.LENGTH_SHORT).show();
        distantance = findViewById(R.id.distant);
        recyclerViewBanners = findViewById(R.id.autobanners);
        mainprogress = findViewById(R.id.progress);
        clrelay = findViewById(R.id.clrelay);
        listView = findViewById(R.id.searchAdapter);
        closereqbtnimg = findViewById(R.id.closereqbtn);
        offerstoshow = findViewById(R.id.offerstext);
        fab_main = findViewById(R.id.fab);
        chooseserach = findViewById(R.id.searchview);
        extendedFloatingButton = findViewById(R.id.buybtn);
        requestTestDrive = findViewById(R.id.reqtestdrive);
        bookLayout = findViewById(R.id.booklayout);
        testLayout = findViewById(R.id.testlayout);
        testAvailtxt = findViewById(R.id.testAvail);
        booknowTxt = findViewById(R.id.booknowtxt);
        compareView = findViewById(R.id.view_copmare);
        comparetxt = findViewById(R.id.compare);
        viewcount = findViewById(R.id.view_txt);
        imageViewClose = findViewById(R.id.closeimg);
        zoomImg = findViewById(R.id.zoom);
        editTextSearch = findViewById(R.id.searchText);
        changeTxt = findViewById(R.id.change_text);
        addressdealer = findViewById(R.id.dealers_list_address);
        changeTxt.setOnClickListener(this);
        testLayout.setOnClickListener(this);
        calltoseller.setOnClickListener(this);
        findaddress.setOnClickListener(this);
        editTextSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getApplicationContext(), SearchResultActivity.class);
                i.putExtra("keyword", query);
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1) {
                    Intent i = new Intent(getApplicationContext(), SearchResultActivity.class);
                    i.putExtra("keyword", newText);
                    startActivity(i);
                }
                return false;
            }
        });
        imageViewClose.setOnClickListener(this);
        comparetxt.setOnClickListener(this);
        viewcount.setOnClickListener(this);
        zoomImg.setOnClickListener(this);
        closereqbtnimg.setOnClickListener(this);
        String cview = new SessionStorage().getStrings(this, SessionStorage.compareview);
        pro1 = new SessionStorage().getStrings(this, SessionStorage.productid);
        pro2 = new SessionStorage().getStrings(this, SessionStorage.productid2);
        savedData = new SessionStorage().getStrings(this, SessionStorage.dataToSave);
        plength = new SessionStorage().getStrings(this, SessionStorage.productslength);

        //Toast.makeText(this, "pro1 - "+pro1+" pro2 -" +pro2, Toast.LENGTH_SHORT).show();

        if (savedData != null) {
            compareView.setVisibility(View.VISIBLE);
            viewcount.setText("View " + plength);
            fab_main.setVisibility(View.VISIBLE);
        } else {
            fab_main.setVisibility(GONE);
        }

        if (viewcount.getText().toString().equals("View 3")) {
            fab_main.setVisibility(GONE);
        }

        brand_name = findViewById(R.id.brandName);
        brand_name.setOnClickListener(this);
        brand_descr = findViewById(R.id.brandDescript);
        moreby_brand = findViewById(R.id.morebybrand);
        moreby_brand.setOnClickListener(this);
        brand_descr.setOnClickListener(this);
        requestTestDrive.setOnClickListener(this);
        extendedFloatingButton.setOnClickListener(this);

        buyerGuide = findViewById(R.id.buyer_guide);
        findstore = findViewById(R.id.enterpincode);
        cartCount = findViewById(R.id.cart_count);
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
        cost = findViewById(R.id.product_price);

        options = findViewById(R.id.options_recyclerview);
        recyclerView = findViewById(R.id.horizontal_view);
        similarproductsview = findViewById(R.id.similar_listview);

        imageView = findViewById(R.id.imageswitcher);
        textView_product_details = findViewById(R.id.product_details);
        totalreview = findViewById(R.id.totalreviews);
        totalrating = findViewById(R.id.totalratings);
        similarproductTitle = findViewById(R.id.similarproductstxt);
        dealers_List = findViewById(R.id.dealers_list);


        mBottomSheetDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View sheetView = getLayoutInflater().inflate(R.layout.addons_layout, null);
        buywithtit = sheetView.findViewById(R.id.buywithtitle);
        buywithlistview = sheetView.findViewById(R.id.buywith_listview);
        Button button = sheetView.findViewById(R.id.addtoCart);
        TextView textView = sheetView.findViewById(R.id.skip);
        progressBar = sheetView.findViewById(R.id.progress_addons);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        button.setBackground(getResources().getDrawable(R.drawable.button_bg));
        mBottomSheetDialog.setContentView(sheetView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicktype) {
                    addTocart(productId, "BUY");
                } else {
                    addTocart(selproductid, "Booknow");
                }
                button.setBackground(getResources().getDrawable(R.drawable.gray_button_background));
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicktype) {
                    jsonaddons("");
                    addTocart(productId, "BUY");
                } else {
                    jsonaddons("");
                    addTocart(selproductid, "Booknow");
                }
                textView.setTextColor(getResources().getColor(R.color.dark_gray));
            }
        });

        imageView.setOnClickListener(this);
        textView_product_details.setOnClickListener(this);
        shareicon.setOnClickListener(this);
        butn_payPolicy.setOnClickListener(this);
        delivery_policy.setOnClickListener(this);
        refund_policy.setOnClickListener(this);
        return_policy.setOnClickListener(this);
        cancellation_policy.setOnClickListener(this);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        addcart.setOnClickListener(this);
        cartCount.setOnClickListener(this);
        findstore.setOnClickListener(this);
        buyerGuide.setOnClickListener(this);
        linearLayoutrent.setOnClickListener(this);
        bookLayout.setOnClickListener(this);
        fab_main.setOnClickListener(this);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_anticlock);

    }

    public String selctedprod_ID() {
        return productId;
    }

    private ArrayList<ProductDetails> getOfferData() {
        ArrayList<ProductDetails> offerData = new ArrayList<>();
        offerData.add(new ProductDetails(2, "Cash back", R.drawable.ic_rupee, selctedprod_ID()));
        offerData.add(new ProductDetails(2, "Test drive", R.drawable.ic_bestoffer, selctedprod_ID()));
        offerData.add(new ProductDetails(2, "Exchange", R.drawable.ic_exchange, selctedprod_ID()));
        offerData.add(new ProductDetails(2, " Finance", R.drawable.ic_rupee, selctedprod_ID()));
        return offerData;
    }

    public void getProductDetails(String productid) {
        mainprogress.setVisibility(View.VISIBLE);
        productDetailsList.clear();
        buyDetailsList.clear();
        similarList.clear();
        optionsList.clear();
        productSpecs.clear();
        buttondata.clear();
        bannerslist.clear();
        optionselect.clear();
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
                    String cartItemCount = dataJsonObject.getString("cartItemsCount");
                    cartCount.setText(cartItemCount);

                    JSONObject bannersjsonobject = dataJsonObject.getJSONObject("banners");
                    JSONObject jsonObjectHead = bannersjsonobject.getJSONObject("Product_Detail_Page_Banner");
                    JSONArray jsonArraybanners = jsonObjectHead.getJSONArray("banners");
                    for (int ban = 0; ban < jsonArraybanners.length(); ban++) {
                        JSONObject jsonObjectBanners = jsonArraybanners.getJSONObject(ban);
                        String bannerurl = jsonObjectBanners.getString("banner_url");
                        String bannerImage = jsonObjectBanners.getString("banner_img");
                        ProductDetails productDetailsBanner = new ProductDetails();
                        productDetailsBanner.setDetailspageBanners("https://ewheelers.in" + bannerImage);
                        productDetailsBanner.setDetailbannerurl(bannerurl);
                        productDetailsBanner.setTypeoflayout(7);
                        bannerslist.add(productDetailsBanner);
                    }

                    JSONObject jsonObjectSocialShare = dataJsonObject.getJSONObject("socialShareContent");
                    String type = jsonObjectSocialShare.getString("type");
                    titile = jsonObjectSocialShare.getString("title");
                    description = jsonObjectSocialShare.getString("description");
                    url = jsonObjectSocialShare.getString("image");

                    JSONArray jsonArrayDataOptions = dataJsonObject.getJSONArray("optionRows");
                    for (int optionarray = 0; optionarray < jsonArrayDataOptions.length(); optionarray++) {
                        JSONObject jsonObjectOptionTitle = jsonArrayDataOptions.getJSONObject(optionarray);
                        String optionid = jsonObjectOptionTitle.getString("option_id");
                        String optioniscolor = jsonObjectOptionTitle.getString("option_is_color");
                        optionname1 = jsonObjectOptionTitle.getString("option_name");

                        JSONArray jsonArrayOptionValue = jsonObjectOptionTitle.getJSONArray("values");

                        ArrayList<OptionValues> optionValueList = new ArrayList<OptionValues>();

                        for (int k = 0; k < jsonArrayOptionValue.length(); k++) {
                            JSONObject option_value = jsonArrayOptionValue.getJSONObject(k);
                            String optionUrlvalue = option_value.getString("optionUrlValue");
                            String optionvaluename = option_value.getString("optionvalue_name");
                            optionlistid = option_value.getString("option_id");
                            optionvalue_id = option_value.getString("optionvalue_id");

                            OptionValues optionValues = new OptionValues();
                            optionValues.setOptionUrlValue(optionUrlvalue);
                            optionValues.setOptionValuenames(optionvaluename);

                            optionValues.setOptionId(optionlistid);
                            optionValues.setOptionvalueid(optionvalue_id);

                            optionValueList.add(optionValues);
                            spinnerlist = optionValueList;


                        }

                        ProductDetails productDetailsoptions = new ProductDetails();
                        productDetailsoptions.setOptionName(optionname1);
                        productDetailsoptions.setOptionid(optionid);
                        productDetailsoptions.setOptionValuesArrayList(spinnerlist);
                        productDetailsoptions.setTypeoflayout(0);
                        // productDetailsoptions.setMap(map);
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


                    JSONObject jsonObjectAttributes = dataJsonObject.getJSONObject("attributes");
                    attTitle = jsonObjectAttributes.getString("title");
                    JSONObject jsonObjectsubattarray = jsonObjectAttributes.getJSONObject("attributesArray");
                    Iterator itera = jsonObjectsubattarray.keys();
                    while (itera.hasNext()) {
                        String key = (String) itera.next();
                        //String value = jsonObjectsubattarray.getString(key);
                        Comparemodelclass comparemodelclass = new Comparemodelclass();
                        comparemodelclass.setHeading(key);
                        comparemodelclass.setTypeofLay(0);
                        comparemodelclasses.add(comparemodelclass);
                        JSONObject value = jsonObjectsubattarray.getJSONObject(key);
                        Iterator subiterator = value.keys();
                        while (subiterator.hasNext()) {
                            String subkey = (String) subiterator.next();
                            String subvalue = value.getString(subkey);
                            // String valueOfheading = subkey + ":" + subvalue;
                            String valueOfheading = subkey;
                            String valueOfvalue = subvalue;
                            comparemodelclass = new Comparemodelclass();
                            comparemodelclass.setValues(valueOfheading);
                            comparemodelclass.setSubvalue(valueOfvalue);
                            comparemodelclass.setTypeofLay(1);
                            comparemodelclasses.add(comparemodelclass);
                        }
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
                    selbooknowEnable = jsonObjectdata.getString("selprod_book_now_enable");
                    booknowEnable = jsonObjectdata.getString("product_book");
                    rentPrice = jsonObjectdata.getString("rent_price");
                    bookPercentage = jsonObjectdata.getString("product_book_percentage");
                    rentSecurity = jsonObjectdata.getString("sprodata_rental_security");
                    minrentduration = jsonObjectdata.getString("sprodata_minimum_rental_duration");
                    brand_Id = jsonObjectdata.getString("brand_id");
                    shop_Id = jsonObjectdata.getString("shop_id");
                    shop_Name = jsonObjectdata.getString("shop_name");
                    brand_Name = jsonObjectdata.getString("brand_name");
                    brand_Shortdesc = jsonObjectdata.getString("brand_short_description");
                    String offerstobuyer = jsonObjectdata.getString("selprodComments");
                    getShopProductsBanner(shop_Id);
                    if (offerstobuyer.isEmpty() || offerstobuyer.equals(null)) {
                        offerstoshow.setText("No offers on this Product");
                    } else {
                        offerstoshow.setText(offerstobuyer);
                    }
                    short_descript = brand_Shortdesc;
                    if (brand_Shortdesc.isEmpty()) {
                        brand_descr.setVisibility(GONE);
                    }

                    double bookingprice = (Double.parseDouble(productprice) / 100.0f) * Double.parseDouble(bookPercentage);

                    selectedOptionValues = jsonObjectdata.getJSONObject("selectedOptionValues");
                    HashMap<String, String> map = new HashMap<String, String>();
                    Iterator iter = selectedOptionValues.keys();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        value = selectedOptionValues.getString(key);
                        map.put(key, value);
                        Log.e("selected options", "value: " + value);
                        ProductDetails productDetailSelect = new ProductDetails();
                        productDetailSelect.setOptionselectid(value);
                        optionselect.add(productDetailSelect);
                    }

                    brand.setText("( " + productmodel + " ) " + productname);
                    //brand_name.setText(brand_Name);
                    brand_descr.setText(brand_Shortdesc);
                    if (brand_Shortdesc.length() > 45) {
                        brand_Shortdesc = brand_Shortdesc.substring(0, 45) + "...";
                        brand_descr.setText(Html.fromHtml(brand_Shortdesc + "<font color='blue'> <u>View More</u></font>"));
                    }

                    // productName.setText(productdescription);
                    cost.setText("\u20B9 " + productprice);

                    String key = null;
                    String value = null;
                    JSONObject jsonObjectBuystatus = dataJsonObject.getJSONObject("productBuyStatusArr");
                    Iterator iterator = jsonObjectBuystatus.keys();
                    while (iterator.hasNext()) {
                        key = (String) iterator.next();
                        value = jsonObjectBuystatus.getString(key);
                    }

                    if (booknowEnable.equals("1")) {
                        if (selbooknowEnable.equals("1")) {
                            linearLayoutrent.setVisibility(GONE);
                            extendedFloatingButton.setVisibility(View.VISIBLE);
                            // buttondata.add(new ProductDetails(5, "BUY", R.color.colorPrimary, selproductid));
                        }
                        if (selbooknowEnable.equals("2")) {
                            linearLayoutrent.setVisibility(GONE);
                            bookLayout.setVisibility(View.VISIBLE);
                            booknowTxt.setText("Book now with Amount \u20B9" + bookingprice);
                            // buttondata.add(new ProductDetails(5, "Book Now", R.color.colorPrimary, selproductid));
                        }
                        if (selbooknowEnable.equals("0")) {
                            linearLayoutrent.setVisibility(GONE);
                            bookLayout.setVisibility(View.VISIBLE);
                            booknowTxt.setText("Book now with Amount \u20B9" + bookingprice);
                            extendedFloatingButton.setVisibility(View.VISIBLE);

                            //  buttondata.add(new ProductDetails(5, "Book Now", R.color.colorPrimary, selproductid));
                            //  buttondata.add(new ProductDetails(5, "BUY", R.color.colorPrimary, selproductid));
                        }

                    } else {
                        linearLayoutrent.setVisibility(GONE);
                        addcart.setVisibility(View.VISIBLE);
                        extendedFloatingButton.setVisibility(View.VISIBLE);
                        // buttondata.add(new ProductDetails(5, "BUY", R.color.colorPrimary, selproductid));
                    }

                    if (testDriveEnable.equals("1")) {
                        linearLayoutrent.setVisibility(GONE);
                        requestTestDrive.setVisibility(View.VISIBLE);
                        clrelay.setVisibility(View.VISIBLE);
                        testLayout.setVisibility(View.VISIBLE);
                        testAvailtxt.setText("Request For Test Drive");
                        // buttondata.add(new ProductDetails(5, "Test Drive", R.color.colorGrey, selproductid));
                    }

                    if (isRent.equals("1")) {
                        linearLayoutrent.setVisibility(View.VISIBLE);
                        rentavailbility.setText("Get it on Rent for just Amount " + "\u20B9 " + rentPrice);
                        //  buttondata.add(new ProductDetails(5, "Rent", R.color.colorPrimary, selproductid));
                    }


                    JSONObject jsonObjectpolicies = dataJsonObject.getJSONObject("shop");
                    paymetnpolicy = jsonObjectpolicies.getString("shop_payment_policy");
                    deliverpolicy = jsonObjectpolicies.getString("shop_delivery_policy");
                    refundpolicy = jsonObjectpolicies.getString("shop_refund_policy");
                    shopname = jsonObjectpolicies.getString("shop_name");
                    countryname = jsonObjectpolicies.getString("shop_country_name");
                    statename = jsonObjectpolicies.getString("shop_state_name");
                    city = jsonObjectpolicies.getString("shop_city");

                    butn_payPolicy.setBackgroundColor(Color.parseColor("#9C3C34"));
                    butn_payPolicy.setTextColor(Color.WHITE);
                    SwitchFragment(new PolicyFragment().newInstance(paymetnpolicy));

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
                        // String selectedProductId = jsonObjectbuywith.getString("selprod_product_id");
                        String selectedProductId = jsonObjectbuywith.getString("selprod_id");

                        AddonsClass productDetailsaddons = new AddonsClass();
                        productDetailsaddons.setBuywithimageurl(productimgurl);
                        productDetailsaddons.setBuywithproductname(productName);
                        productDetailsaddons.setBuywithproductprice(productPrice);
                        productDetailsaddons.setButwithselectedProductId(selectedProductId);
                        // productDetails.setTypeoflayout(3);
                        buyDetailsList.add(productDetailsaddons);

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
                    productdetailsAdapter = new ProductdetailsAdapter(ProductDetailActivity.this, productDetailsList, 0, "notzoom");
                    recyclerView.setAdapter(productdetailsAdapter);
                    productdetailsAdapter.notifyDataSetChanged();

                    LinearLayoutManager gridLayoutManager = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.VERTICAL, false);
                    options.setLayoutManager(gridLayoutManager);
                    productdetailsAdapter2 = new ProductdetailsAdapter(ProductDetailActivity.this, optionsList);
                    options.setAdapter(productdetailsAdapter2);
                    productdetailsAdapter2.notifyDataSetChanged();

                    LinearLayoutManager linearLayoutManagerbanners = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.HORIZONTAL, false);
                    recyclerViewBanners.setLayoutManager(linearLayoutManagerbanners);
                    productdetailsAdapterbuywith = new ProductdetailsAdapter(ProductDetailActivity.this, bannerslist);
                    recyclerViewBanners.setAdapter(productdetailsAdapterbuywith);
                    productdetailsAdapterbuywith.notifyDataSetChanged();

                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.VERTICAL, false);
                    buywithlistview.setLayoutManager(linearLayoutManager2);
                    addonsAdapter = new AddonsAdapter(ProductDetailActivity.this, buyDetailsList);
                    buywithlistview.setAdapter(addonsAdapter);
                    addonsAdapter.notifyDataSetChanged();


                    LinearLayoutManager gridLayoutManagerSimilar = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.HORIZONTAL, false);
                    similarproductsview.setLayoutManager(gridLayoutManagerSimilar);
                    productdetailsAdapterSimilar = new ProductdetailsAdapter(ProductDetailActivity.this, similarList);
                    similarproductsview.setAdapter(productdetailsAdapterSimilar);
                    productdetailsAdapterSimilar.notifyDataSetChanged();
                    mainprogress.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mainprogress.setVisibility(View.GONE);
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
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_dashboard_black_24dp)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(imageView);
    }

    public void jsonaddons(String addons) {
        // Toast.makeText(this, "get addons as:"+addons, Toast.LENGTH_SHORT).show();
        jsonaddon = addons;
    }

    public ArrayList<ProductDetails> productDetailSelectValues() {
        Log.i("seperatemethod", String.valueOf(optionselect));
        return optionselect;
    }

    private void addcompare(String productId, String app_data) {
        String tokenvalue = new SessionStorage().getStrings(ProductDetailActivity.this, SessionStorage.tokenvalue);
        String Login_url = Apis.addcompare;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONObject jsonObjectDatatoSave = jsonObjectData.getJSONObject("dataToSave");
                                attr_grp_cat_id = jsonObjectDatatoSave.getString("attr_grp_cat_id");
                                JSONObject products = jsonObjectDatatoSave.getJSONObject("products");
                                SessionStorage.saveString(ProductDetailActivity.this, SessionStorage.dataToSave, jsonObjectDatatoSave.toString());
                                compareView.setVisibility(View.VISIBLE);
                                viewcount.setText("View " + products.length());
                                SessionStorage.saveString(ProductDetailActivity.this, SessionStorage.productslength, String.valueOf(products.length()));
                                if (products.length() == 3) {
                                    fab_main.setVisibility(GONE);
                                } else {
                                    fab_main.setVisibility(View.VISIBLE);
                                }


                            } else {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout), message, Snackbar.LENGTH_SHORT);
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
                if (app_data == null) {
                    data3.put("selProdId", productId);
                } else {
                    data3.put("selProdId", productId);
                    data3.put("appData", app_data);
                }
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    public void getSellerList(String prodid) {
        String url_link = Apis.sellerslist + prodid;
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
                        username = jsonObjectProduct.getString("user_name");
                        String shopname = jsonObjectProduct.getString("shop_name");
                        shoplatitude = jsonObjectProduct.getString("shop_latitude");
                        shoplogitude = jsonObjectProduct.getString("shop_longitude");
                        shopmobile = jsonObjectProduct.getString("shop_phone");
                        dealers_List.setText(shopname);
                        newgps = new NewGPSTracker(mContext, ProductDetailActivity.this);
                        geocoder = new Geocoder(ProductDetailActivity.this, Locale.ENGLISH);
                        try {
                            addresses = geocoder.getFromLocation(Double.parseDouble(shoplatitude), Double.parseDouble(shoplogitude), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        StringBuilder str1 = new StringBuilder();
                        StringBuilder str2 = new StringBuilder();
                        StringBuilder str3 = new StringBuilder();
                        StringBuilder str4 = new StringBuilder();
                        StringBuilder str5 = new StringBuilder();
                        StringBuilder str6 = new StringBuilder();

                        if (Geocoder.isPresent()) {

                            Address returnAddress = addresses.get(0);

                            String address = returnAddress.getAddressLine(0);
                            String localityString = returnAddress.getSubLocality();
                            String citys = returnAddress.getLocality();
                            String region_code = returnAddress.getCountryName();
                            String zipcode = returnAddress.getPostalCode();
                            String statenam = returnAddress.getAdminArea();
                            str1.append(address);
                            str2.append(localityString);
                            str3.append(citys);
                            str4.append(region_code);
                            str5.append(zipcode);
                            str6.append(statenam);
                            addressdealer.setText(username + "\n" + str1);

                        }
                        getDistance(new NavAppBarActivity().setlatitude(), new NavAppBarActivity().setlongitude(), shoplatitude, shoplogitude);
                    } else {
                        Toast.makeText(ProductDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void call_action(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        startActivity(callIntent);
    }

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    public void getDistance(double currlat, double currlng, String servicelat, String servicelng) {
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
                            distantance.setText(distance);

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


    private void getShopProductsBanner(String shopid) {
        progressBar.setVisibility(View.VISIBLE);
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
                                shop_id = jsonObjectShop.getString("shop_id");
                                shop_name = jsonObjectShop.getString("shop_name");
                                shoplogo = jsonObjectShop.getString("shop_logo");
                                shopbanner = jsonObjectShop.getString("shop_banner");
                                brand_name.setText(shop_name);
                                Picasso.get().load(shoplogo).fit().into(logoImg);
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
                progressBar.setVisibility(GONE);

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
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_seller:
                if (isPermissionGranted()) {
                    call_action(shopmobile);
                }
                break;
            case R.id.navigate_map:
                findaddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Float.parseFloat(shoplatitude), Float.parseFloat(shoplogitude));
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    }
                });
                break;
            case R.id.closereqbtn:
                requestTestDrive.setVisibility(GONE);
                clrelay.setVisibility(GONE);
                break;
            case R.id.fab:
                if (isOpen) {
                    chooseserach.setVisibility(View.INVISIBLE);
                    chooseserach.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    isOpen = false;
                    listView.setVisibility(GONE);
                } else {
                    chooseserach.setVisibility(View.VISIBLE);
                    chooseserach.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    isOpen = true;
                    openchoosesearch(attr_grp_cat_id);
                }
                break;
            case R.id.buybtn:
                //addTocart(selproductid, "BUY");
                if (buyDetailsList.isEmpty()) {
                    mBottomSheetDialog.dismiss();
                    addTocart(selproductid, "BUY");
                } else {
                    clicktype = false;
                    mBottomSheetDialog.show();
                }

                break;
            case R.id.reqtestdrive:
                Intent intenr = new Intent(ProductDetailActivity.this, TestDriveAndRentabike.class);
                intenr.putExtra("typeoflayout", "test");
                intenr.putExtra("productid", selproductid);
              /*  intenr.putExtra("productid", selproductid);
                intenr.putExtra("prodname", productname);
                intenr.putExtra("prodprice", productprice);
                intenr.putExtra("prodmodel", productmodel);
                intenr.putExtra("proddescription", productdescription);
                intenr.putExtra("image", imageurls);
                intenr.putExtra("brand", brand_Name);*/
                intenr.putExtra("qty", editqty.getText().toString());
               /* intenr.putExtra("shopname", shopname);
                intenr.putExtra("shopaddress", addressdealer.getText().toString() + "\nPhone : " + shopmobile);*/
                startActivity(intenr);
                break;
            case R.id.testlayout:
                intenr = new Intent(ProductDetailActivity.this, TestDriveAndRentabike.class);
                intenr.putExtra("typeoflayout", "test");
                intenr.putExtra("productid", selproductid);
                intenr.putExtra("qty", editqty.getText().toString());
                startActivity(intenr);
                break;
            case R.id.booklayout:
                if (buyDetailsList.isEmpty()) {
                    mBottomSheetDialog.dismiss();
                    addTocart(selproductid, "Booknow");
                } else {
                    clicktype = true;
                    mBottomSheetDialog.show();
                }
                break;
            case R.id.rentlayout:
                intenr = new Intent(ProductDetailActivity.this, TestDriveAndRentabike.class);
                intenr.putExtra("typeoflayout", "rent");
                intenr.putExtra("productid", selproductid);
                intenr.putExtra("qty", editqty.getText().toString());
                startActivity(intenr);
                break;
            case R.id.brandDescript:
                Tooltip tooltip = new Tooltip.Builder(v)
                        .setText(short_descript)
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.parseColor("#9c3c34"))
                        .setCancelable(true)
                        .show();

                break;
            case R.id.goto_shop:
                Intent intentIs = new Intent(ProductDetailActivity.this, ShowAlleBikesActivity.class);
                intentIs.putExtra("shopid", shop_id);
                intentIs.putExtra("shopname", shop_name);
                intentIs.putExtra("shopphone", shopmobile);
                intentIs.putExtra("shopaddress", addressdealer.getText().toString());
                intentIs.putExtra("shopbanner", shopbanner);
                startActivity(intentIs);
                break;
            case R.id.morebybrand:
                Intent intent = new Intent(ProductDetailActivity.this, ShowAlleBikesActivity.class);
                intent.putExtra("brandid", brand_Id);
                intent.putExtra("brandname", brand_Name);
                startActivity(intent);
                break;
            case R.id.zoom:
                Intent iz = new Intent(getApplicationContext(), ZoomingActivity.class);
                iz.putExtra("productid", productId);
                startActivity(iz);
                break;
            case R.id.imageswitcher:
                iz = new Intent(getApplicationContext(), ZoomingActivity.class);
                iz.putExtra("productid", productId);
                startActivity(iz);
                break;
            case R.id.compare:
                if (savedData == null || savedData.isEmpty()) {
                    addcompare(productId, "");
                    //fab_main.setVisibility(View.VISIBLE);
                } else {
                    addcompare(productId, new SessionStorage().getStrings(this, SessionStorage.dataToSave));
                    //fab_main.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.closeimg:
                fab_main.setVisibility(GONE);
                compareView.setVisibility(GONE);
                comparetxt.setText("Compare");
                comparetxt.setEnabled(true);
                SessionStorage.clearString(ProductDetailActivity.this, SessionStorage.dataToSave);
                SessionStorage.clearString(ProductDetailActivity.this, SessionStorage.compareview);
                SessionStorage.clearString(ProductDetailActivity.this, SessionStorage.productid);
                SessionStorage.clearString(ProductDetailActivity.this, SessionStorage.productid2);

                break;
            case R.id.view_txt:
                Intent i = new Intent(getApplicationContext(), CompareIndexActivity.class);
                i.putExtra("appData", new SessionStorage().getStrings(this, SessionStorage.dataToSave));
                startActivity(i);
                break;

            case R.id.buyer_guide:
                Intent in = new Intent(getApplicationContext(), BuyerGuideActivity.class);
                in.putExtra("opens", "buyerguide");
                startActivity(in);
                break;
            case R.id.enterpincode:
                Intent inten = new Intent(getApplicationContext(), SellersListActivity.class);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inten.putExtra("fromactivity", "details");
                inten.putExtra("selproductid", selproductid);
                inten.putExtra("shopname", shopname);
                inten.putExtra("shopaddress", city + "," + statename + "," + countryname);
                startActivity(inten);
                break;
            case R.id.change_text:
                inten = new Intent(getApplicationContext(), SellersListActivity.class);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inten.putExtra("fromactivity", "details");
                inten.putExtra("selproductid", selproductid);
                inten.putExtra("shopname", dealers_List.getText().toString());
                inten.putExtra("shopaddress", city + "," + statename + "," + countryname);
                startActivity(inten);
                break;
            case R.id.cart_count:
                Intent inte = new Intent(getApplicationContext(), CartListingActivity.class);
                inte.putExtra("selid", selproductid);
                startActivity(inte);
                break;
            case R.id.add_cart:
                addTocart(selproductid, "addcart");
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
                intent = new Intent(ProductDetailActivity.this, ProductDescriptionActivity.class);
                intent.putExtra("description", productdescription);
                intent.putExtra("Specifications", (Serializable) productSpecs);
                intent.putExtra("attributes", (Serializable) comparemodelclasses);
                intent.putExtra("image", imageurls);
                intent.putExtra("title", productname);
                intent.putExtra("price", productprice);
                intent.putExtra("model", productmodel);
                intent.putExtra("attributetitle", attTitle);
                startActivity(intent);
                break;
            case R.id.share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "(" + titile + ") " + description);
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

    private void openchoosesearch(String attr_grp_cat_id) {
        chooseserach.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listView.setVisibility(GONE);
                addcompare(splitString(query), new SessionStorage().getStrings(ProductDetailActivity.this, SessionStorage.dataToSave));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    listView.setVisibility(View.VISIBLE);
                    getautotag(attr_grp_cat_id, "");
                } else {
                    listView.setVisibility(View.VISIBLE);
                    getautotag(attr_grp_cat_id, newText);
                    // Toast.makeText(ProductDetailActivity.this, "catid" + attr_grp_cat_id + newText, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void getautotag(String attr_grp_cat_id, String searchtxt) {
        strings.clear();
        String tokenvalue = new SessionStorage().getStrings(ProductDetailActivity.this, SessionStorage.tokenvalue);
        String Login_url = Apis.autocompletesearch;
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
                                JSONArray jsonArray = jsonObjectData.getJSONArray("productList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsons = jsonArray.getJSONObject(i);
                                    String pid = jsons.getString("id");
                                    String pname = jsons.getString("name");
                                    strings.add(pid + " - " + pname);
                                }

                                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(ProductDetailActivity.this, android.R.layout.simple_dropdown_item_1line, strings);
                                listView.setAdapter(itemsAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        chooseserach.setQuery(strings.get(position), true);

                                    }
                                });

                            } else {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout), message, Snackbar.LENGTH_SHORT);
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
                data3.put("attr_grp_cat_id", attr_grp_cat_id);
                data3.put("keyword", searchtxt);
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    public String splitString(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }

        return String.valueOf(num);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class OpencartListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), CartListingActivity.class);
            i.putExtra("selid", selproductid);
            startActivity(i);
        }
    }

    public void addTocart(String productid, String buttontext) {
        progressBar.setVisibility(View.VISIBLE);
        String Login_url = Apis.addtocart;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            // Toast.makeText(ProductDetailActivity.this, "addcart msg:" + message, Toast.LENGTH_SHORT).show();

                            if (getStatus.equals("1")) {
                                progressBar.setVisibility(GONE);

                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                String cartcount = jsonObjectData.getString("cartItemsCount");
                                cartCount.setText(cartcount);
                                if (buttontext.equals("BUY")) {

                                    Intent i = new Intent(getApplicationContext(), CartListingActivity.class);
                                    i.putExtra("selid", selproductid);
                                    startActivity(i);
                                } else if (buttontext.equals("addcart") || buttontext.equals("rent")) {
                                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout),
                                            message, Snackbar.LENGTH_LONG);
                                    mySnackbar.setAction("Opencart", new OpencartListner()).setTextColor(Color.YELLOW);
                                    mySnackbar.show();
                                } else if (buttontext.equals("Booknow")) {
                                    Intent i = new Intent(getApplicationContext(), CartListingActivity.class);
                                    i.putExtra("selid", selproductid);
                                    startActivity(i);
                                }
                            } else {
                                progressBar.setVisibility(GONE);

                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout), message, Snackbar.LENGTH_LONG);
                                mySnackbar.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(GONE);
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json");
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();

                data3.put("selprod_id", productid);
                data3.put("quantity", editqty.getText().toString());
                if (buttontext.equals("Booknow")) {
                    data3.put("type", "book");
                    if (jsonaddon == null) {
                        data3.put("addons", "");
                    } else {
                        data3.put("addons", jsonaddon);
                    }
                } else {
                    if (jsonaddon == null) {
                        data3.put("addons", "");
                    } else {
                        data3.put("addons", jsonaddon);
                    }
                }
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    public void SwitchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framecontent, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        getProductDetails(productId);
        super.onResume();


    }

}
