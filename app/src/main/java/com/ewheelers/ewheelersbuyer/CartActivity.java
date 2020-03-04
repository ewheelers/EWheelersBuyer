package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Volley.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements View.OnClickListener{
    String buttonText, productid;
    ImageView btnDatePicker, btnTimePicker;
    TextView txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar c;
    EditText edit_date,edit_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        txtDate = findViewById(R.id.date_image);
        txtTime = findViewById(R.id.time_image);
        edit_date = findViewById(R.id.date);
        edit_time = findViewById(R.id.time);

        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        buttonText = getIntent().getStringExtra("buttontext");
        productid = getIntent().getStringExtra("selproductid");

        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ;
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);

        Toast.makeText(this, "" + buttonText + productid, Toast.LENGTH_SHORT).show();
        if(buttonText.equals("")){

        }
     /*   btnDatePicker=(ImageView) findViewById(R.id.btn_date);
        btnTimePicker=(ImageView) findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
*/
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        if (buttonText.equals("Test Drive")) {

        }
    }



    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.date_image:
            show_Datepicker();
            break;
        case R.id.time_image:
            show_Timepicker();
            break;
    }
    }

    private void show_Datepicker() {
        c = Calendar.getInstance();
        int mYearParam = mYear;
        int mMonthParam = mMonth-1;
        int mDayParam = mDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mMonth = monthOfYear + 1;
                        mYear=year;
                        mDay=dayOfMonth;
                    }
                }, mYearParam, mMonthParam, mDayParam);

        datePickerDialog.show();
    }

    private void show_Timepicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {

                        mHour = pHour;
                        mMinute = pMinute;
                    }
                }, mHour, mMinute, true);

        timePickerDialog.show();
    }
}
