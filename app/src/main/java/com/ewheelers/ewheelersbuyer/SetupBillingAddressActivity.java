package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.ewheelers.ewheelersbuyer.Adapters.BillingAddressesAdapter;
import com.ewheelers.ewheelersbuyer.Adapters.ViewPagerAdapter;
import com.ewheelers.ewheelersbuyer.Fragments.AddNewAddressFragment;
import com.ewheelers.ewheelersbuyer.Fragments.AddressListFragment;
import com.ewheelers.ewheelersbuyer.Fragments.FragmentForgotPassword;
import com.ewheelers.ewheelersbuyer.Fragments.FragmentSignIn;
import com.ewheelers.ewheelersbuyer.Fragments.FragmentSignUp;
import com.ewheelers.ewheelersbuyer.Fragments.ShppingAddressFragment;
import com.ewheelers.ewheelersbuyer.ModelClass.BillingAddress;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetupBillingAddressActivity extends AppCompatActivity  {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Button buttonadd;
    Button continueBtn;
    CheckBox checkBoxSame;
    String billid,shipid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_billing_address);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        buttonadd = findViewById(R.id.addnewaddress);
        continueBtn = findViewById(R.id.contin);
        checkBoxSame = findViewById(R.id.sameaddress);
        checkBoxSame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setCheckoutSelctAddress();
                }else {

                }
            }
        });
        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddNewAddressActivity.class);
                startActivity(i);
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckoutSelctAddress();
            }
        });
    }

    public void setUid(String uaid) {
        billid = uaid;
    }

    public void setUidsecond(String uaid2) {
        shipid = uaid2;
    }

    private void setCheckoutSelctAddress() {
        //Toast.makeText(this, "uids:"+billid+", shipid:"+shipid, Toast.LENGTH_SHORT).show();
        if(billid==null&&shipid==null){
            Toast.makeText(this, "Select Bill Address", Toast.LENGTH_SHORT).show();
            checkBoxSame.setChecked(false);
        }else {
            String tokenvalue = new SessionStorage().getStrings(SetupBillingAddressActivity.this, SessionStorage.tokenvalue);
            String Login_url = Apis.setupaddressselection;
            StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String getStatus = jsonObject.getString("status");
                                String message = jsonObject.getString("msg");
                                if (getStatus.equals("1")) {
                                   /* Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), message, Snackbar.LENGTH_SHORT);
                                    mySnackbar.show();*/
                                   Intent i = new Intent(getApplicationContext(),CartSummaryActivity.class);
                                   startActivity(i);
                                   finish();

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
                    data3.put("billing_address_id", billid);
                    if (shipid == null) {
                        data3.put("shipping_address_id", billid);
                    } else {
                        data3.put("shipping_address_id", shipid);
                    }
                    return data3;

                }
            };
            strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
            VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
        }

    }




    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new Fragment();
            if (position == 0) {
                fragment = new AddressListFragment();
            } else if (position == 1) {
                fragment = new ShppingAddressFragment();
               // fragment = new AddNewAddressFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0) {
                title = "Billing Addresses";
            } else if (position == 1) {
                title = "Shipping Addresses";
            }
            return title;
        }
    }



}
