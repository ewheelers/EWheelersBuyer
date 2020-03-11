package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ewheelers.ewheelersbuyer.Adapters.ProductdetailsAdapter;
import com.ewheelers.ewheelersbuyer.Fragments.PolicyFragment;
import com.ewheelers.ewheelersbuyer.ModelClass.OptionValues;
import com.ewheelers.ewheelersbuyer.ModelClass.ProductDetails;
import com.ewheelers.ewheelersbuyer.ModelClass.ProductSpecifications;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ewheelers.ewheelersbuyer.Dialogs.ShowAlerts.showfailedDialog;

public class CartActivity extends AppCompatActivity implements View.OnClickListener{
    String buttonText, productid;
    TextView txtDate, txtTime,termsandconditions, productName, productPrice;
    private EditText location,mobile;
    static EditText edit_date,edit_time;
    String tokenvalue;
    CheckBox checkBoxTerms;
    Button submitForDrive,cancel_button;
    String selproductid,productname,productprice, productmodel, testDriveEnable, producturl;
    NetworkImageView networkImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);

        txtDate = findViewById(R.id.date_image);
        txtTime = findViewById(R.id.time_image);
        edit_date = findViewById(R.id.date);
        edit_time = findViewById(R.id.time);
        location = findViewById(R.id.enter_location);
        mobile = findViewById(R.id.enter_phoneno);
        checkBoxTerms = findViewById(R.id.check_terms);
        submitForDrive = findViewById(R.id.submitButton);
        termsandconditions = findViewById(R.id.termsandcond);
        cancel_button = findViewById(R.id.cancelButton);
        networkImageView = findViewById(R.id.image);
        productName = findViewById(R.id.title);
        productPrice = findViewById(R.id.price);

        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        submitForDrive.setOnClickListener(this);
        termsandconditions.setOnClickListener(this);
        cancel_button.setOnClickListener(this);

        buttonText = getIntent().getStringExtra("buttontext");
        productid = getIntent().getStringExtra("selproductid");

        Toast.makeText(this, "" + buttonText + productid, Toast.LENGTH_SHORT).show();

        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        getProductDetails(productid);

    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.cancelButton:
            onBackPressed();
            break;
        case R.id.termsandcond:
            String url = "http://ewheelers.in/cms/view/2";
            showPrivacyPolicies(url);
            break;
        case R.id.date_image:
            showTruitonDatePickerDialog(v);
            break;
        case R.id.time_image:
            showTruitonTimePickerDialog(v);
            break;
        case R.id.submitButton:
            if(location.getText().toString().isEmpty()){
                location.setError("Enter Location");
            }else if(mobile.getText().toString().isEmpty()){
                mobile.setError("Enter your phone number");
            }else if(edit_date.getText().toString().isEmpty()){
                edit_date.setError("Enter date to pickup");
            }else if(edit_time.getText().toString().isEmpty()){
                edit_time.setError("Enter time to pickup");
            }else if(!checkBoxTerms.isChecked()){
                checkBoxTerms.setError("Check the terms and conditions");
            }else{
                submitForDrive.setBackgroundResource(R.drawable.button_bg);
                submitForDrive.setTextColor(Color.WHITE);
                submitForDrive.setEnabled(true);
                TestDrive();
            }
            break;
    }
    }

    private void showPrivacyPolicies(String url) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        WebView webView = new WebView(this);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        dialog.setView(webView);
        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            edit_date.setText(day + "/" + (month + 1) + "/" + year);
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
            edit_time.setText(hourOfDay + ":"	+ minute);

        }
    }

    public void getProductDetails(String productid) {
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
                    if(status.equals("1")) {
                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                        JSONObject jsonObjectProduct = dataJsonObject.getJSONObject("product");
                        JSONObject jsonObjectdata = jsonObjectProduct.getJSONObject("data");
                        selproductid = jsonObjectdata.getString("selprod_id");
                        productname = jsonObjectdata.getString("product_name");
                        productprice = jsonObjectdata.getString("selprod_price");
                        productmodel = jsonObjectdata.getString("product_model");
                        testDriveEnable = jsonObjectdata.getString("selprod_test_drive_enable");
                        producturl = jsonObjectdata.getString("productUrl");
                        productName.setText(productname+"( "+productmodel+" )");
                        productPrice.setText("\u20B9 "+productprice);
                        ImageLoader imageLoader = VolleySingleton.getInstance(CartActivity.this).getImageLoader();
                        imageLoader.get(producturl, ImageLoader.getImageListener(networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                        networkImageView.setImageUrl(producturl, imageLoader);
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
        queue.add(stringRequest);
    }

    public void TestDrive(){
        String Login_url = Apis.testdrive;

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(getStatus.equals("1")){
                                submitForDrive.setTextColor(Color.parseColor("#9C3C34"));
                                submitForDrive.setBackgroundColor(Color.WHITE);
                                submitForDrive.setEnabled(false);

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
                data3.put("selprod_id", productid);
                data3.put("ptdr_location", location.getText().toString());
                data3.put("ptdr_contact", mobile.getText().toString());
                data3.put("ptdr_date", edit_date.getText().toString()+" "+edit_time.getText().toString());
                data3.put("terms", String.valueOf(checkBoxTerms.isChecked()));

                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }


}
