package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.NetworkImageView;
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
import com.google.android.material.snackbar.Snackbar;
import com.tooltip.Tooltip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.BreakIterator;
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

public class TestDriveAndRentabike extends AppCompatActivity implements View.OnClickListener {
    NetworkImageView networkImageView;
    TextView shop_name, brand, title, options, priceamount, qty, seller_address;
    String imagepic, name, model, price, brandname, quantity, shopname, shopaddress, productId, typeoflay;
    EditText location, mobile;
    TextView txtDate, txtTime;
    static TextView edit_date, edit_time;
    Button submitForDrive;
    String tokenvalue, minrent, rentprice, rentsecure;
    ProgressBar progressBar;
    TextView startDateImg, endDateImg;
    static TextView start_date_edt, end_date_edt;
    Button submitforRent;
    LinearLayout linearLayouttest, linearLayoutrenting;
    float daysBetween;
    TextView totalPayment, minRentduration, minduration, lease_pday, rentalPrice, test_drive;
    NewGPSTracker newgps;
    Context mContext;
    Geocoder geocoder;
    List<Address> addresses;
    TextView deliveryprocess, short_descr, rentsecurity;
    //WebView prod_descript;
    String deliverpolicy, selproductid;
    TextView changepickaddress;
    TextView buywithtit, textView;
    Button button;
    RecyclerView buywithlistview;
    private TextView rentTerm;
    Dialog mBottomSheetDialog;
    private AddonsAdapter addonsAdapter;
    private List<AddonsClass> buyDetailsList = new ArrayList<>();
    private String jsonaddon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drive_and_rentabike);
        changepickaddress = findViewById(R.id.changepickaddress);
        rentTerm = findViewById(R.id.rentTerms);
        changepickaddress.setOnClickListener(this);
        test_drive = findViewById(R.id.TestDrive);
        //prod_descript = findViewById(R.id.prod_descript);
        short_descr = findViewById(R.id.short_descr);
        deliveryprocess = findViewById(R.id.delivery);
        linearLayoutrenting = findViewById(R.id.linearrent);
        linearLayouttest = findViewById(R.id.lineardrive);
        rentsecurity = findViewById(R.id.rentSecurity);

        rentalPrice = findViewById(R.id.rent_price);
        totalPayment = findViewById(R.id.total_payment);
        minRentduration = findViewById(R.id.min_rent_duration);
        lease_pday = findViewById(R.id.lease_pday);
        minduration = findViewById(R.id.minduration);
        startDateImg = findViewById(R.id.startdate_image);
        endDateImg = findViewById(R.id.enddate_image);
        start_date_edt = findViewById(R.id.startdate);
        end_date_edt = findViewById(R.id.enddate);
        submitforRent = findViewById(R.id.submitButtonRent);

        progressBar = findViewById(R.id.loadprogress);
        networkImageView = findViewById(R.id.imageView4);
        priceamount = findViewById(R.id.price);
        location = findViewById(R.id.enter_location);
        mobile = findViewById(R.id.enter_phoneno);
        txtDate = findViewById(R.id.date_image);
        txtTime = findViewById(R.id.time_image);
        edit_date = findViewById(R.id.date);
        edit_time = findViewById(R.id.time);
        submitForDrive = findViewById(R.id.submitButton);
        shop_name = findViewById(R.id.shop_name);
        brand = findViewById(R.id.brand);
        title = findViewById(R.id.title);
        options = findViewById(R.id.options);
        qty = findViewById(R.id.qty);
        seller_address = findViewById(R.id.seller_address);
        edit_date.setOnClickListener(this);
        edit_time.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        submitForDrive.setOnClickListener(this);

        start_date_edt.setOnClickListener(this);
        end_date_edt.setOnClickListener(this);
        submitforRent.setOnClickListener(this);
        deliveryprocess.setOnClickListener(this);

        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        typeoflay = getIntent().getStringExtra("typeoflayout");
        productId = getIntent().getStringExtra("productid");
        quantity = getIntent().getStringExtra("qty");

        mBottomSheetDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View sheetView = getLayoutInflater().inflate(R.layout.addons_layout, null);
        buywithtit = sheetView.findViewById(R.id.buywithtitle);
        buywithlistview = sheetView.findViewById(R.id.buywith_listview);
        button = sheetView.findViewById(R.id.addtoCart);
        textView = sheetView.findViewById(R.id.skip);
        progressBar = sheetView.findViewById(R.id.progress_addons);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        button.setBackground(getResources().getDrawable(R.drawable.button_bg));
        mBottomSheetDialog.setContentView(sheetView);

        getProductDetails(productId, typeoflay);
        getSellerList(productId);

    }

    public void addcart(String productid, String startdate, String enddate) {
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
                                //cartCount.setText(cartcount);
                               /* Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout),
                                        message, Snackbar.LENGTH_LONG);
                                mySnackbar.setAction("Opencart", new OpencartRentListner());
                                mySnackbar.show();*/
                                mBottomSheetDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), CartListingActivity.class);
                                i.putExtra("selid", productId);
                                i.putExtra("typeoflay",typeoflay);
                                startActivity(i);
                            } else {
                                mBottomSheetDialog.dismiss();
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snak_layout),
                                        message, Snackbar.LENGTH_LONG);
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
                data3.put("rental_start_date", startdate);
                data3.put("rental_end_date", enddate);
                if (jsonaddon == null) {
                    data3.put("addons", "");
                } else {
                    data3.put("addons", jsonaddon);
                }
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    public void TestDrive(View v) {
        progressBar.setVisibility(View.VISIBLE);
        submitForDrive.setVisibility(View.GONE);
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
                                View dialogView = LayoutInflater.from(TestDriveAndRentabike.this).inflate(R.layout.success_layout, viewGroup, false);
                                TextView textView = dialogView.findViewById(R.id.message);
                                Button button = dialogView.findViewById(R.id.buttonOk);
                                textView.setText(message);
                                AlertDialog.Builder builder = new AlertDialog.Builder(TestDriveAndRentabike.this);
                                builder.setView(dialogView);
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                alertDialog.setCancelable(false);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                        Intent i = new Intent(TestDriveAndRentabike.this, MyTestDrivesActivity.class);
                                        startActivity(i);
                                        finish();
                                        alertDialog.dismiss();
                                    }
                                });

                                progressBar.setVisibility(View.GONE);
                                submitForDrive.setVisibility(View.GONE);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                submitForDrive.setVisibility(View.VISIBLE);
                                showfailedDialog(TestDriveAndRentabike.this, v, message);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                submitForDrive.setVisibility(View.GONE);
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

    public void jsonaddons(String addons) {
        jsonaddon = addons;
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
                        String username = jsonObjectProduct.getString("user_name");
                        String shopname = jsonObjectProduct.getString("shop_name");
                        String shoplatitude = jsonObjectProduct.getString("shop_latitude");
                        String shoplogitude = jsonObjectProduct.getString("shop_longitude");
                        String shopmobile = jsonObjectProduct.getString("shop_phone");
                        shop_name.setText(shopname);
                        newgps = new NewGPSTracker(mContext, TestDriveAndRentabike.this);
                        geocoder = new Geocoder(TestDriveAndRentabike.this, Locale.ENGLISH);
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
                            seller_address.setText(username + "\n" + str1 + "\nPhone : " + shopmobile);

                        }

                    } else {
                        Toast.makeText(TestDriveAndRentabike.this, msg, Toast.LENGTH_SHORT).show();
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

    public void getProductDetails(String productid, String typeoflay) {
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

                    JSONObject jsonObjectProduct = dataJsonObject.getJSONObject("product");
                    JSONObject jsonObjectdata = jsonObjectProduct.getJSONObject("data");
                    String profDescription = jsonObjectdata.getString("product_description");
                    String shortDescription = jsonObjectdata.getString("brand_short_description");
                    // prod_descript.loadData(profDescription,"text/html","UTF-8");
                    short_descr.setText(shortDescription);
                    selproductid = jsonObjectdata.getString("selprod_id");
                    String productname = jsonObjectdata.getString("product_name");
                    String productprice = jsonObjectdata.getString("selprod_price");
                    String productmodel = jsonObjectdata.getString("product_model");
                    String rentPrice = jsonObjectdata.getString("rent_price");
                    String rentSecurity = jsonObjectdata.getString("sprodata_rental_security");
                    String rentalTerms = jsonObjectdata.getString("sprodata_rental_terms");
                    String minrentduration = jsonObjectdata.getString("sprodata_minimum_rental_duration");
                    String brand_Id = jsonObjectdata.getString("brand_id");
                    String brand_Name = jsonObjectdata.getString("brand_name");

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

                    JSONObject jsonObjectpolicies = dataJsonObject.getJSONObject("shop");
                    deliverpolicy = jsonObjectpolicies.getString("shop_delivery_policy");
                    deliveryprocess.setText(deliverpolicy);
                    JSONArray jsonArrayCollections = dataJsonObject.getJSONArray("productImagesArr");
                    JSONObject jsonObjectProductdetail = jsonArrayCollections.getJSONObject(0);
                    String imageurls = jsonObjectProductdetail.getString("product_image_url");
                    title.setText(productname + "(" + productmodel + ")");
                    if (quantity != null) {
                        qty.setText("Qty : " + quantity);
                    } else {
                        qty.setText("Qty : " + "1");
                    }
                    brand.setText(brand_Name);
                    priceamount.setText("\u20B9 " + productprice);
                    if (imageurls != null) {
                        ImageLoader imageLoader = VolleySingleton.getInstance(TestDriveAndRentabike.this).getImageLoader();
                        imageLoader.get(imageurls, ImageLoader.getImageListener(networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                        networkImageView.setImageUrl(imageurls, imageLoader);
                    }


                    if (typeoflay.equals("test")) {
                        linearLayouttest.setVisibility(View.VISIBLE);
                        linearLayoutrenting.setVisibility(View.GONE);
                        test_drive.setText("Requesting Test Drive");

                    } else {
                        rentTerm.setText(rentalTerms);
                        rentsecurity.setText("\u20B9 " + rentSecurity);
                        minduration.setText(minrentduration + " Day(s)");
                        test_drive.setText("Rent a Bike");
                        lease_pday.setText("\u20B9 " + rentPrice + " + Rental Security \u20B9 " + rentSecurity);
                        //minRentduration.setText("Retail Security : \u20B9 " + rentSecurity + "\nMinimum Rental Duration : " + minrentduration + " Day(s)");
                        //rentalPrice.setText("Rental Price: \u20B9 " + rentPrice + " + Rental Security \u20B9 " + rentSecurity);
                        totalPayment.setText("Total Payment : \u20B9 " + (Double.parseDouble(rentPrice) + Double.parseDouble(rentSecurity)));
                        linearLayouttest.setVisibility(View.GONE);
                        linearLayoutrenting.setVisibility(View.VISIBLE);

                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(TestDriveAndRentabike.this, RecyclerView.VERTICAL, false);
                        buywithlistview.setLayoutManager(linearLayoutManager2);
                        addonsAdapter = new AddonsAdapter(TestDriveAndRentabike.this, buyDetailsList, "rent");
                        buywithlistview.setAdapter(addonsAdapter);
                        addonsAdapter.notifyDataSetChanged();

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
                                        if (daysBetween == 0.0 || daysBetween < 0.0) {
                                            Toast.makeText(TestDriveAndRentabike.this, "error in selecting dates", Toast.LENGTH_SHORT).show();
                                        } else {
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
                                        if (daysBetween == 0.0 || daysBetween < 0.0) {
                                            Toast.makeText(TestDriveAndRentabike.this, "error in selecting dates", Toast.LENGTH_SHORT).show();
                                        } else {
                                            totalPayment.setText("Total Payment : \u20B9 " + (double) daysBetween * (Double.parseDouble(rentPrice) + Double.parseDouble(rentSecurity)));
                                        }
                                        Log.i("days:", String.valueOf(daysBetween));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changepickaddress:
                Intent inten = new Intent(getApplicationContext(), SellersListActivity.class);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inten.putExtra("fromactivity", "details");
                inten.putExtra("selproductid", selproductid);
                startActivity(inten);
                break;
            case R.id.submitButton:
                if (location.getText().toString().isEmpty()) {
                    location.setError("Enter Location");
                } else if (mobile.getText().toString().isEmpty()) {
                    mobile.setError("Enter your phone number");
                } else if (edit_date.getText().toString().equals("yyyy-mm-dd")) {
                    edit_date.setError("click here to pick date");
                } else if (edit_time.getText().toString().equals("hr:min")) {
                    edit_time.setError("click here to pick time");
                } else {
                    TestDrive(v);
                }
                break;
            case R.id.date_image:
                showTruitonDatePickerDialog(v);
                break;
            case R.id.date:
                showTruitonDatePickerDialog(v);
                break;
            case R.id.time_image:
                showTruitonTimePickerDialog(v);
                break;
            case R.id.time:
                showTruitonTimePickerDialog(v);
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
            case R.id.submitButtonRent:
                if (start_date_edt.getText().toString().isEmpty() || end_date_edt.getText().toString().isEmpty()) {
                    Toast.makeText(TestDriveAndRentabike.this, "Select dates", Toast.LENGTH_SHORT).show();
                } else {
                    if(buyDetailsList.size()!=0) {
                        mBottomSheetDialog.show();
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addcart(productId, start_date_edt.getText().toString(), end_date_edt.getText().toString());
                            }
                        });

                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                jsonaddons("");
                                addcart(productId, start_date_edt.getText().toString(), end_date_edt.getText().toString());
                            }
                        });
                    }else {
                        mBottomSheetDialog.dismiss();
                        jsonaddons("");
                        addcart(productId, start_date_edt.getText().toString(), end_date_edt.getText().toString());
                    }

                }
                break;
           /* case R.id.delivery:
                Tooltip tooltip = new Tooltip.Builder(v)
                        .setText(deliverpolicy)
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.parseColor("#9c3c34"))
                        .setCancelable(true)
                        .show();
                break;*/
        }
    }


}
