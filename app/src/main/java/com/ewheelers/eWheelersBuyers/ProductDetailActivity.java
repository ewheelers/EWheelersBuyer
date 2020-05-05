package com.ewheelers.eWheelersBuyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.ewheelers.eWheelersBuyers.Fragments.PolicyFragment;
import com.ewheelers.eWheelersBuyers.ModelClass.AddonsClass;
import com.ewheelers.eWheelersBuyers.ModelClass.Comparemodelclass;
import com.ewheelers.eWheelersBuyers.ModelClass.OptionValues;
import com.ewheelers.eWheelersBuyers.ModelClass.ProductDetails;
import com.ewheelers.eWheelersBuyers.ModelClass.ProductSpecifications;
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static com.ewheelers.eWheelersBuyers.Dialogs.ShowAlerts.showfailedDialog;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    String productId, cart_Items;
    RecyclerView recyclerView, options, offersrecyclerview, buywithlistview, similarproductsview, bottomButtonView;
    ImageView imageView;
    ProductdetailsAdapter productdetailsAdapter, productdetailsAdapter2, productdetailsAdapterbuywith, productdetailsAdapterSimilar;
    AddonsAdapter addonsAdapter;
    String value = "";
    ProductDetails productDetailsButton;
    List<ProductDetails> productDetailsList = new ArrayList<>();
    ArrayList<ProductDetails> optionselect = new ArrayList<>();

    //List<ProductDetails> buyDetailsList = new ArrayList<>();

    List<AddonsClass> buyDetailsList = new ArrayList<>();

    List<ProductDetails> similarList = new ArrayList<>();
    List<ProductDetails> optionsList = new ArrayList<>();
    List<ProductSpecifications> productSpecs = new ArrayList<>();
    ArrayList<OptionValues> spinnerlist = new ArrayList<>();
    List<ProductDetails> buttondata = new ArrayList<>();
    List<Comparemodelclass> comparemodelclasses = new ArrayList<>();

    ImageLoader imageLoader;
    TextView textView_product_details, shareicon, offerstoshow;
    private InputMethodManager imm;
    String imageurls, productdescription, selproductid, productname, productprice, productmodel, isRent, testDriveEnable, booknowEnable, selbooknowEnable;
    String rentPrice, rentSecurity, bookPercentage;
    public static String selectProId = "";
    TextView brand, cost, policytext, totalreview, totalrating, buywithtit, similarproductTitle;
    //WebView productName;
    String optionname1, attTitle;

    TextView rentavailbility, findstore, rentalPrice, booknowTxt, testAvailtxt;

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
    RelativeLayout snackbarLayout;

    TextView cartCount, buyerGuide;
    String tokenvalue;
    String jsonaddon;
    String url, description, titile;

    LinearLayout layoutBottomSheet, layoutBottomSheetRent;
    BottomSheetBehavior sheetBehavior;
    BottomSheetBehavior bottomSheetBehaviorRent;

    TextView closeBottomLayout, closeBottomLayoutRent;
    ImageView closereqbtnimg;
    static EditText edit_date, edit_time;
    static TextView start_date_edt, end_date_edt;
    Button submitForDrive, submitforRent;
    private EditText location, mobile;
    TextView txtDate, txtTime, startDateImg, endDateImg;
    TextView totalPayment, minRentduration;
    float daysBetween;
    JSONObject selectedOptionValues;
    String optionvalue_id = "";
    String optionlistid = "";
    LinearLayout compareView;
    TextView comparetxt, viewcount, zoomImg;
    ImageView imageViewClose;
    int onclk;
    String pro1, pro2, savedData, plength;
    SearchView editTextSearch;
    TextView changeTxt, addressdealer, moreby_brand;
    String brand_Shortdesc, short_descript, brand_Name, brand_Id;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        productId = getIntent().getStringExtra("productid");

      //  Toast.makeText(this, "from: " + productId, Toast.LENGTH_SHORT).show();
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
        }
        if (viewcount.getText().toString().equals("View 1") || viewcount.getText().toString().equals("View 2")) {
            fab_main.setVisibility(View.VISIBLE);
        } else {
            fab_main.setVisibility(GONE);
        }


      /*  if(viewcount.getText().toString().equals("View 3")){
            fab_main.setVisibility(GONE);
        }*/

       /* if (cview != null && cview.equals("1") && pro1 != null) {
            compareView.setVisibility(View.VISIBLE);
            if (pro1.equals(productId)) {
                viewcount.setText("View 1");
                comparetxt.setText("added");
                comparetxt.setEnabled(false);
            } else if (pro2 != null && pro2.equals(productId)) {
                compareView.setVisibility(View.VISIBLE);
                viewcount.setText("View 2");
                comparetxt.setText("added");
                comparetxt.setEnabled(false);
            } else {
                viewcount.setText("View 1");
                comparetxt.setText("Compare");
                comparetxt.setEnabled(true);
                onclk = 2;
            }
        } else {
            compareView.setVisibility(GONE);
            comparetxt.setText("Compare");
            comparetxt.setEnabled(true);
        }*/


        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);


        layoutBottomSheetRent = findViewById(R.id.bottom_sheet_rent);
        bottomSheetBehaviorRent = BottomSheetBehavior.from(layoutBottomSheetRent);

        closeBottomLayout = findViewById(R.id.closebottom_layout);
        closeBottomLayoutRent = findViewById(R.id.closebottomrent_layout);

        txtDate = findViewById(R.id.date_image);
        txtTime = findViewById(R.id.time_image);

        startDateImg = findViewById(R.id.startdate_image);
        endDateImg = findViewById(R.id.enddate_image);

        edit_date = findViewById(R.id.date);
        edit_time = findViewById(R.id.time);

        start_date_edt = findViewById(R.id.startdate);
        end_date_edt = findViewById(R.id.enddate);

        location = findViewById(R.id.enter_location);
        mobile = findViewById(R.id.enter_phoneno);
        submitForDrive = findViewById(R.id.submitButton);
        submitforRent = findViewById(R.id.submitButtonRent);

        rentalPrice = findViewById(R.id.rent_price);
        totalPayment = findViewById(R.id.total_payment);
        minRentduration = findViewById(R.id.min_rent_duration);
        brand_name = findViewById(R.id.brandName);
        brand_descr = findViewById(R.id.brandDescript);
        moreby_brand = findViewById(R.id.morebybrand);
        moreby_brand.setOnClickListener(this);
        closeBottomLayout.setOnClickListener(this);
        closeBottomLayoutRent.setOnClickListener(this);
        submitforRent.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        submitForDrive.setOnClickListener(this);
        start_date_edt.setOnClickListener(this);
        end_date_edt.setOnClickListener(this);
        brand_descr.setOnClickListener(this);
        requestTestDrive.setOnClickListener(this);
        extendedFloatingButton.setOnClickListener(this);

        String test = getIntent().getStringExtra("test");
        assert test != null;
        if (test == null) {

        } else {
            if (test.equals("test")) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
        String rent = getIntent().getStringExtra("rent");
        assert rent != null;
        if (rent == null) {

        } else {
            if (rent.equals("rent")) {
                bottomSheetBehaviorRent.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }

//        Toast.makeText(this, "cat items:"+cart_Items, Toast.LENGTH_SHORT).show();

        /*try {
            imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));
        buyerGuide = findViewById(R.id.buyer_guide);
        findstore = findViewById(R.id.enterpincode);
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
        //productName = findViewById(R.id.product_name);
        cost = findViewById(R.id.product_price);

        options = findViewById(R.id.options_recyclerview);
        recyclerView = findViewById(R.id.horizontal_view);
        //offersrecyclerview = findViewById(R.id.offers_listview);
        //buywithlistview = findViewById(R.id.buywith_listview);
        similarproductsview = findViewById(R.id.similar_listview);

        imageView = findViewById(R.id.imageswitcher);
        textView_product_details = findViewById(R.id.product_details);
        // policytext = findViewById(R.id.policy);
        totalreview = findViewById(R.id.totalreviews);
        totalrating = findViewById(R.id.totalratings);
        //buywithtit = findViewById(R.id.buywithtitle);
        similarproductTitle = findViewById(R.id.similarproductstxt);
        dealers_List = findViewById(R.id.dealers_list);


        mBottomSheetDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View sheetView = getLayoutInflater().inflate(R.layout.addons_layout, null);
        buywithtit = sheetView.findViewById(R.id.buywithtitle);
        buywithlistview = sheetView.findViewById(R.id.buywith_listview);
        Button button = sheetView.findViewById(R.id.addtoCart);
        TextView textView = sheetView.findViewById(R.id.skip);
        mBottomSheetDialog.setContentView(sheetView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTocart(productId, "BUY");
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonaddons("");
                addTocart(productId, "BUY");
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
        //dealers_List.setOnClickListener(this);
        addcart.setOnClickListener(this);
        cartCount.setOnClickListener(this);
        findstore.setOnClickListener(this);
        buyerGuide.setOnClickListener(this);
        linearLayoutrent.setOnClickListener(this);
        bookLayout.setOnClickListener(this);
        fab_main.setOnClickListener(this);
        //dealers_List.setOnItemSelectedListener(this);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_anticlock);

        /*ProductdetailsAdapter productdetailsAdapter = new ProductdetailsAdapter(ProductDetailActivity.this, getOfferData());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        offersrecyclerview.setLayoutManager(linearLayoutManager);
        offersrecyclerview.setAdapter(productdetailsAdapter);
        productdetailsAdapter.notifyDataSetChanged();*/

        //getProductDetails(productId);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        // btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        bottomSheetBehaviorRent.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        // btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        start_date_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    String dateBeforeString = start_date_edt.getText().toString();
                    String dateAfterString = end_date_edt.getText().toString();
                    try {
                        Date dateBefore = myFormat.parse(dateBeforeString);
                        Date dateAfter = myFormat.parse(dateAfterString);
                        long difference = dateAfter.getTime() - dateBefore.getTime();
                        daysBetween = (difference / (1000 * 60 * 60 * 24));
                        /* You can also convert the milliseconds to days using this method
                         * float daysBetween =
                         *         TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
                         */
                        if (daysBetween == 0.0 || daysBetween < 0.0) {
                            Toast.makeText(ProductDetailActivity.this, "error in selecting dates", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(ProductDetailActivity.this, "Number of Days between dates:" + daysBetween, Toast.LENGTH_SHORT).show();
                            totalPayment.setText("Total Payment : \u20B9 " + (double) daysBetween * (Double.parseDouble(rentPrice) + Double.parseDouble(rentSecurity)));
                        }
                        Log.i("days:", String.valueOf(daysBetween));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        end_date_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    String dateBeforeString = start_date_edt.getText().toString();
                    String dateAfterString = end_date_edt.getText().toString();
                    try {
                        Date dateBefore = myFormat.parse(dateBeforeString);
                        Date dateAfter = myFormat.parse(dateAfterString);
                        long difference = dateAfter.getTime() - dateBefore.getTime();
                        daysBetween = (difference / (1000 * 60 * 60 * 24));
                        /* You can also convert the milliseconds to days using this method
                         * float daysBetween =
                         *         TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
                         */
                        if (daysBetween == 0.0 || daysBetween < 0.0) {
                            Toast.makeText(ProductDetailActivity.this, "error in selecting dates", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(ProductDetailActivity.this, "Number of Days between dates:" + daysBetween, Toast.LENGTH_SHORT).show();
                            totalPayment.setText("Total Payment : \u20B9 " + (double) daysBetween * (Double.parseDouble(rentPrice) + Double.parseDouble(rentSecurity)));
                        }
                        Log.i("days:", String.valueOf(daysBetween));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //getProductDetails(productId);


    }

  /*  public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
         jsonaddon = intent.getStringExtra("jsonaddons");
            Toast.makeText(context, "json addons:"+jsonaddon, Toast.LENGTH_SHORT).show();
        }
    };*/

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

        productDetailsList.clear();
        buyDetailsList.clear();
        similarList.clear();
        optionsList.clear();
        productSpecs.clear();
        buttondata.clear();
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

                       /* map = new HashMap<String, String>();
                        map.put(optionlistid,optionvalue_id);
                        Log.e("ashdkjdhkj",map.toString());*/

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
//                    ArrayList<String> strings = new ArrayList<>();
                    //HashMap<String, String> map = new HashMap<String, String>();
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
                    String minrentduration = jsonObjectdata.getString("sprodata_minimum_rental_duration");
                    brand_Id = jsonObjectdata.getString("brand_id");
                    brand_Name = jsonObjectdata.getString("brand_name");
                    brand_Shortdesc = jsonObjectdata.getString("brand_short_description");
                    String offerstobuyer = jsonObjectdata.getString("selprodComments");
                    if (offerstobuyer.isEmpty() || offerstobuyer == null) {
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


                    minRentduration.setText("Retail Security : \u20B9 " + rentSecurity + "\nMinimum Rental Duration : " + minrentduration + " Day(s)");
                    rentalPrice.setText("Rental Price: \u20B9 " + rentPrice + " + Rental Security \u20B9 " + rentSecurity);
                    totalPayment.setText("Total Payment : \u20B9 " + (Double.parseDouble(rentPrice) + Double.parseDouble(rentSecurity)));
                    start_date_edt.setText("");
                    end_date_edt.setText("");

                    brand.setText("( " + productmodel + " ) " + productname);
                    brand_name.setText(brand_Name);
                    brand_descr.setText(brand_Shortdesc);
                    if (brand_Shortdesc.length() > 45) {
                        brand_Shortdesc = brand_Shortdesc.substring(0, 45) + "...";
                        brand_descr.setText(Html.fromHtml(brand_Shortdesc + "<font color='blue'> <u>View More</u></font>"));
                    }
                   /* productName.getSettings().setJavaScriptEnabled(true); // enable javascript
                    productName.setWebViewClient(new WebViewClient() {
                        @SuppressWarnings("deprecation")
                        @Override
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                            Toast.makeText(ProductDetailActivity.this, description, Toast.LENGTH_SHORT).show();
                        }
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                            // Redirect to deprecated method, so you can use it in all SDK versions
                            onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                        }
                    });
                    productName.loadData(productdescription, "text/html", "UTF-8");
*/
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

                    dealers_List.setText(shopname);
                    addressdealer.setText(city + " " + statename + " " + countryname);
                    /*ArrayList<String> strings = new ArrayList<>();
                    strings.add(shopname + " " + city);
                    dealers_List.setAdapter(new ArrayAdapter<String>(ProductDetailActivity.this, android.R.layout.simple_spinner_dropdown_item, strings));*/


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
                        // String selectedProductId = jsonObjectbuywith.getString("selprod_product_id");
                        String selectedProductId = jsonObjectbuywith.getString("selprod_id");

                      /*  ProductDetails productDetails = new ProductDetails();
                        productDetails.setBuywithimageurl(productimgurl);
                        productDetails.setBuywithproductname(productName);
                        productDetails.setBuywithproductprice(productPrice);
                        productDetails.setButwithselectedProductId(selectedProductId);
                        productDetails.setTypeoflayout(3);
                        buyDetailsList.add(productDetails);*/

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

                  /*  LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.VERTICAL, false);
                    buywithlistview.setLayoutManager(linearLayoutManager2);
                    productdetailsAdapterbuywith = new ProductdetailsAdapter(ProductDetailActivity.this, buyDetailsList);
                    buywithlistview.setAdapter(productdetailsAdapterbuywith);
                    productdetailsAdapterbuywith.notifyDataSetChanged();*/

                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.VERTICAL, false);
                    buywithlistview.setLayoutManager(linearLayoutManager2);
                    addonsAdapter = new AddonsAdapter(ProductDetailActivity.this, buyDetailsList);
                    buywithlistview.setAdapter(addonsAdapter);
                    addonsAdapter.notifyDataSetChanged();


                    GridLayoutManager gridLayoutManagerSimilar = new GridLayoutManager(ProductDetailActivity.this, 2);
                    similarproductsview.setLayoutManager(gridLayoutManagerSimilar);
                    productdetailsAdapterSimilar = new ProductdetailsAdapter(ProductDetailActivity.this, similarList);
                    similarproductsview.setAdapter(productdetailsAdapterSimilar);
                    productdetailsAdapterSimilar.notifyDataSetChanged();

                   /* LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
                    bottomButtonView.setLayoutManager(linearLayoutManager3);
                    ProductdetailsAdapter productdetailsAdapter3 = new ProductdetailsAdapter(ProductDetailActivity.this, buttondata);
                    bottomButtonView.setAdapter(productdetailsAdapter3);
                    productdetailsAdapter3.notifyDataSetChanged();*/

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
                               /* if (products.length() == 1) {
                                    compareView.setVisibility(View.VISIBLE);
                                    //viewcount.setText("View "+products.length());
                                    comparetxt.setText("added");
                                    comparetxt.setEnabled(false);
                                    SessionStorage.saveString(ProductDetailActivity.this, SessionStorage.compareview, "1");
                                    SessionStorage.saveString(ProductDetailActivity.this, SessionStorage.productid, productId);
                                } else if (products.length() == 2) {
                                   // viewcount.setText("View "+products.length());
                                    comparetxt.setText("added");
                                    comparetxt.setEnabled(false);
                                    //SessionStorage.saveString(ProductDetailActivity.this, SessionStorage.compareview, "1");
                                    SessionStorage.saveString(ProductDetailActivity.this, SessionStorage.productid2, productId);
                                }*/


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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                } else {
                    mBottomSheetDialog.show();
                }
                break;
            case R.id.reqtestdrive:
                getBottomLayout();
                break;
            case R.id.testlayout:
                getBottomLayout();
                break;
            case R.id.booklayout:
                addTocart(selproductid, "Booknow");
                break;
            case R.id.rentlayout:
                getBottomLayoutforRent();
                break;
            case R.id.brandDescript:
                Tooltip tooltip = new Tooltip.Builder(v)
                        .setText(short_descript)
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.parseColor("#9c3c34"))
                        .setCancelable(true)
                        .show();

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

               /* if (onclk == 2) {
                    addcompare(productId, new SessionStorage().getStrings(this, SessionStorage.dataToSave));
                   *//* viewcount.setText("View 2");
                    comparetxt.setText("added");
                    comparetxt.setEnabled(false);
                    //SessionStorage.saveString(ProductDetailActivity.this, SessionStorage.compareview, "1");
                    SessionStorage.saveString(ProductDetailActivity.this, SessionStorage.productid2, productId);*//*
                }else {
                    *//*compareView.setVisibility(View.VISIBLE);
                    viewcount.setText("View 1");
                    comparetxt.setText("added");
                    comparetxt.setEnabled(false);
                    SessionStorage.saveString(ProductDetailActivity.this, SessionStorage.compareview, "1");
                    SessionStorage.saveString(ProductDetailActivity.this, SessionStorage.productid, productId);
*//*
                    addcompare(productId, "");

                }*/
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
               /* i.putExtra("prod1", new SessionStorage().getStrings(this, SessionStorage.productid));
                i.putExtra("prod2", new SessionStorage().getStrings(this, SessionStorage.productid2));*/
                startActivity(i);
               /* if (viewcount.getText().toString().equals("View 1")) {
                    Toast.makeText(this, "Select View 2 to compare", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), CompareActivity.class);
                    i.putExtra("prod1", new SessionStorage().getStrings(this, SessionStorage.productid));
                    i.putExtra("prod2", new SessionStorage().getStrings(this, SessionStorage.productid2));
                    startActivity(i);
                }*/
                break;
            case R.id.startdate_image:
                showTruitonDatePickerDialogRent(v);
                break;
            case R.id.enddate_image:
                showTruitonDatePickerDialogendRent(v);
                break;
            case R.id.startdate:
                showTruitonDatePickerDialogRent(v);
                break;
            case R.id.enddate:
                showTruitonDatePickerDialogendRent(v);
                break;
            case R.id.date_image:
                showTruitonDatePickerDialog(v);
                break;
            case R.id.time_image:
                showTruitonTimePickerDialog(v);
                break;
            case R.id.submitButtonRent:
                if (start_date_edt.getText().toString().isEmpty() || end_date_edt.getText().toString().isEmpty()) {
                    Toast.makeText(ProductDetailActivity.this, "Select dates", Toast.LENGTH_SHORT).show();
                } else {
                    addcart(selproductid);
                }
                break;
            case R.id.submitButton:
                if (location.getText().toString().isEmpty()) {
                    location.setError("Enter Location");
                } else if (mobile.getText().toString().isEmpty()) {
                    mobile.setError("Enter your phone number");
                } else if (edit_date.getText().toString().isEmpty()) {
                    edit_date.setError("Enter date to pickup");
                } else if (edit_time.getText().toString().isEmpty()) {
                    edit_time.setError("Enter time to pickup");
                } else {
                    TestDrive(v);
                }
                break;
            case R.id.closebottom_layout:
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.closebottomrent_layout:
                bottomSheetBehaviorRent.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.buyer_guide:
                Intent in = new Intent(getApplicationContext(), BuyerGuideActivity.class);
                in.putExtra("opens", "buyerguide");
                startActivity(in);
                break;
            case R.id.enterpincode:
                Intent inten = new Intent(getApplicationContext(), SellersListActivity.class);
                inten.putExtra("fromactivity", "details");
                inten.putExtra("selproductid", selproductid);
                inten.putExtra("shopname", shopname);
                inten.putExtra("shopaddress", city + "," + statename + "," + countryname);
                startActivity(inten);
                break;
            case R.id.change_text:
                inten = new Intent(getApplicationContext(), SellersListActivity.class);
                inten.putExtra("fromactivity", "details");
                inten.putExtra("selproductid", selproductid);
                inten.putExtra("shopname", shopname);
                inten.putExtra("shopaddress", city + "," + statename + "," + countryname);
                startActivity(inten);
                break;
            case R.id.cart_count:
                Intent inte = new Intent(getApplicationContext(), CartListingActivity.class);
                inte.putExtra("selid", selproductid);
                startActivity(inte);
                break;
            case R.id.add_cart:
                //  addTocart(selproductid, "cart");
                addTocart(selproductid, "addcart");

                break;
         /*   case R.id.dealers_list:
                selectDealerDialog();
                break;*/
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
               /* try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\neBike is for everyone. Let me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }*/

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
                addcompare(splitString(query),new SessionStorage().getStrings(ProductDetailActivity.this, SessionStorage.dataToSave));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    listView.setVisibility(View.VISIBLE);
                    getautotag(attr_grp_cat_id, "");
                }else {
                    listView.setVisibility(View.VISIBLE);
                    getautotag(attr_grp_cat_id, newText);
                   // Toast.makeText(ProductDetailActivity.this, "catid" + attr_grp_cat_id + newText, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void getautotag(String attr_grp_cat_id,String searchtxt) {
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
                                    strings.add(pid+" - "+pname);
                                }

                                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(ProductDetailActivity.this, android.R.layout.simple_dropdown_item_1line, strings);
                                listView.setAdapter(itemsAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        chooseserach.setQuery(strings.get(position),true);

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

    public String splitString(String str)
    {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i=0; i<str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if(Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }

        return String.valueOf(num);
    }

    private void showTruitonDatePickerDialogendRent(View v) {
        DialogFragment newFragment = new DatePickerFragmentend();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showTruitonDatePickerDialogRent(View v) {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment2 extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDate = new DatePickerDialog(getActivity(), this, year, month, day);
            mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            // Create a new instance of DatePickerDialog and return it
            return mDate;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            view.setMinDate(System.currentTimeMillis() - 1000);
            // Do something with the date chosen by the user
            start_date_edt.setText(year + "-" + (month + 1) + "-" + day);
        }
    }

    public static class DatePickerFragmentend extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDate = new DatePickerDialog(getActivity(), this, year, month, day);
            mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            // Create a new instance of DatePickerDialog and return it
            return mDate;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            view.setMinDate(System.currentTimeMillis() - 1000);
            // Do something with the date chosen by the user
            end_date_edt.setText(year + "-" + (month + 1) + "-" + day);
        }
    }

    public void TestDrive(View v) {
        String Login_url = Apis.testdrive;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("testresponse:", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {

                                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                                View dialogView = LayoutInflater.from(ProductDetailActivity.this).inflate(R.layout.success_layout, viewGroup, false);
                                TextView textView = dialogView.findViewById(R.id.message);
                                Button button = dialogView.findViewById(R.id.buttonOk);
                                textView.setText(message);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                                builder.setView(dialogView);
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                        Intent i = new Intent(ProductDetailActivity.this, MyTestDrivesActivity.class);
                                        startActivity(i);
                                        finish();
                                        alertDialog.dismiss();
                                    }
                                });
                            } else {

                                showfailedDialog(ProductDetailActivity.this, v, message);

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("selprod_id", productId);
                data3.put("ptdr_location", location.getText().toString());
                data3.put("ptdr_contact", mobile.getText().toString());
                data3.put("ptdr_date", edit_date.getText().toString() + " " + edit_time.getText().toString());
                data3.put("terms", "1");
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void getBottomLayout() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void getBottomLayoutforRent() {
        if (bottomSheetBehaviorRent.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorRent.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehaviorRent.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDate = new DatePickerDialog(getActivity(), this, year, month, day);
            mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            // Create a new instance of DatePickerDialog and return it
            return mDate;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            view.setMinDate(System.currentTimeMillis() - 1000);
            // Do something with the date chosen by the user
            edit_date.setText(year + "-" + (month + 1) + "-" + day);
        }
    }

    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            // DateEdit.setText(DateEdit.getText() + " " + hourOfDay + ":"	+ minute);
            edit_time.setText(hourOfDay + ":" + minute);

        }
    }

    public void showSnackbar(String offertitle) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, "Sorry. No " + offertitle + " Offers Available Now.", Snackbar.LENGTH_SHORT)
                .show();
    }

    public void OfferClick(int position) {
        if (position == 1) {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                // btnBottomSheet.setText("Close sheet");
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                // btnBottomSheet.setText("Expand sheet");
            }

        }
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

    public class OpencartRentListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), CartListingActivity.class);
            i.putExtra("selid", selproductid);
            startActivity(i);
        }
    }

    public void addcart(String productid) {
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
                            //setCartItemCount(cartcount);
                            if (getStatus.equals("1")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                String cartcount = jsonObjectData.getString("cartItemsCount");
                                cartCount.setText(cartcount);
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout),
                                        message, Snackbar.LENGTH_LONG);
                                mySnackbar.setAction("Opencart", new OpencartRentListner());
                                mySnackbar.show();
                            } else {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout),
                                        message, Snackbar.LENGTH_LONG);
                                // mySnackbar.setAction("Opencart", new OpencartRentListner());
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
                //params.put("Content-Type", "application/json");
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();

                data3.put("selprod_id", productid);
                data3.put("quantity", "1");
                data3.put("product_for", "2");
                data3.put("rental_start_date", start_date_edt.getText().toString());
                data3.put("rental_end_date", end_date_edt.getText().toString());
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    public void addTocart(String productid, String buttontext) {
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
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout), message, Snackbar.LENGTH_LONG);
                                // mySnackbar.setAction("Opencart", new OpencartListner()).setTextColor(Color.YELLOW);
                                mySnackbar.show();
                            }
                            //setCartItemCount(cartcount);
                           /* if (buttonText.equals("cart")) {
                                cartCount.setText(cartcount);
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout),
                                        "Added to cart", Snackbar.LENGTH_LONG);
                                mySnackbar.setAction("Opencart", new OpencartListner());
                                mySnackbar.show();
                            } else if (buttonText.equals("BUY")) {
                                Intent i = new Intent(getApplicationContext(), CartListingActivity.class);
                                startActivity(i);
                            } else if (buttonText.equals("book")) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout),
                                        message, Snackbar.LENGTH_LONG);
                                mySnackbar.show();
                            }*/
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

    private String setCartItemCount(String cartcount) {
        return cartcount;
    }


    public void SwitchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framecontent, fragment).commitAllowingStateLoss();
    }

    private void selectDealerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Dealers")
                .setMessage(shopname + "\n" + city + "," + statename + "," + countryname)
                .show();
    }


    @Override
    public void onResume() {
        getProductDetails(productId);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), NavAppBarActivity.class);
        startActivity(i);
        finish();
    }
}
