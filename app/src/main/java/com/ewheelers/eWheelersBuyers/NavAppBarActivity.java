package com.ewheelers.eWheelersBuyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.ewheelers.eWheelersBuyers.Adapters.MenuIconAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.HomeMenuIcons;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class NavAppBarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    View mHeaderView;
    TextView username, emailId;
    LinearLayout linearLayoutback;
    ImageView showmenuicon;

    RecyclerView recyclerView;
    MenuIconAdapter menuIconAdapter;
    List<HomeMenuIcons> homeMenuIcons = new ArrayList<>();
    DrawerLayout drawer;

    GPSTracker gps;
    Geocoder geocoder;
    List<Address> addresses;
    static double latitude, longitude;

    TextView getuser_location;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;

    TextView mainCartCount;
    String tokenvalue;

    NewGPSTracker newgps;
    Context mContext;
    String tokenValue;

    public static BottomNavigationView navView;
    SearchView searchView;
    ListView list;
    ArrayList<String> strings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_app_bar);
        tokenValue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);

        getuser_location = findViewById(R.id.fetch_location);

        searchView = findViewById(R.id.searchview);
        list = findViewById(R.id.listview);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_drawer);
        mHeaderView = navigationView.getHeaderView(0);
       /* username = mHeaderView.findViewById(R.id.main_username);
        emailId = mHeaderView.findViewById(R.id.emailid);*/
        linearLayoutback = mHeaderView.findViewById(R.id.back_layout);
        recyclerView = findViewById(R.id.static_menus);
        showmenuicon = findViewById(R.id.showmenu);
        mainCartCount = findViewById(R.id.maincartcount);
        tokenvalue = new SessionStorage().getStrings(NavAppBarActivity.this, SessionStorage.tokenvalue);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               // Toast.makeText(NavAppBarActivity.this, "keyword:" + query, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),SearchResultActivity.class);
                i.putExtra("keyword",query);
                startActivity(i);
                list.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1) {
                    list.setVisibility(View.VISIBLE);
                    setSuggestions(newText);
                } else {
                    list.setVisibility(View.GONE);
                }
                return false;
            }
        });
       /* FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("instancsmsg", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("tokenFCM", msg);

                        //Toast.makeText(NavAppBarActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });*/

        mainCartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CartListingActivity.class);
                i.putExtra("selid","");
                startActivity(i);
            }
        });

        showmenuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        linearLayoutback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        navView = findViewById(R.id.nav_view);

      /*  AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_alerts, R.id.navigation_wallet, R.id.navigation_help, R.id.navigation_profile)
                .build();*/
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // navView.getOrCreateBadge(R.id.navigation_alerts).setNumber(2);


     /*   mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_chats, R.id.nav_orders,
                R.id.nav_my_account, R.id.nav_contactus, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();*/
        // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        // NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        menuIconAdapter = new MenuIconAdapter(this, homeMenuIcons());
        // GridLayoutManager linearLayoutManager = new GridLayoutManager(this,4);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(menuIconAdapter);

       /* Intent intent = new Intent(getApplicationContext(), DiscountActivity.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);*/

        //getcurrentLocation();
        //getCartCountHome();
        initGoogleAPIClient();
        showSettingDialog();

        String location = new SessionStorage().getStrings(this, SessionStorage.location);
        if (location != null) {
            getuser_location.setText(location);
            getuser_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //setcityDialog();
                    Intent i = new Intent(getApplicationContext(), SelectCityActivity.class);
                    startActivity(i);
                }
            });
        }
        if (getuser_location.getText().toString().equals("location")) {
            final TourGuide mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click).setPointer(new Pointer()).setToolTip(new ToolTip().setTitle("Choose location!").setDescription("Click on Get Started to begin...")).setOverlay(new Overlay()).playOn(getuser_location);
            getuser_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTourGuideHandler.cleanUp();
                    // setcityDialog();
                    Intent i = new Intent(getApplicationContext(), SelectCityActivity.class);
                    startActivity(i);
                }
            });
        }

       /* String cityname = new SessionStorage().getStrings(this,SessionStorage.location);
        if(cityname.isEmpty()){
            final TourGuide mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click) .setPointer(new Pointer()) .setToolTip(new ToolTip().setTitle("Choose location!").setDescription("Click on Get Started to begin...")) .setOverlay(new Overlay()) .playOn(getuser_location);
            getuser_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTourGuideHandler.cleanUp();
                    // setcityDialog();
                    Intent i = new Intent(getApplicationContext(),SelectCityActivity.class);
                    startActivity(i);
                }
            });
        }else {
            getuser_location.setText(cityname);
        }*/

        mContext = this;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NavAppBarActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
           // Toast.makeText(mContext, "You need have granted permission", Toast.LENGTH_SHORT).show();
            newgps = new NewGPSTracker(mContext, NavAppBarActivity.this);

            // Check if GPS enabled
            if (newgps.canGetLocation()) {

                latitude = newgps.getLatitude();
                longitude = newgps.getLongitude();

                // \n is for new line

                // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                try {
                    geocoder = new Geocoder(this, Locale.ENGLISH);
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
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

                        //getuser_location.setText(str3);
                        //  countrycode.setText(str4);
                        //  pincodeno.setText(str5);
                        //  statename.setText(str6);
                        //Toast.makeText(getApplicationContext(), str1, Toast.LENGTH_SHORT).show();
                        //donator_addre.setEnabled(false);
                        //city.setText(str1);
                        Toast.makeText(getApplicationContext(), "Your Location is -" + str3, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(this, "Unable to find Geocoder", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }

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
                                list.setAdapter(new ArrayAdapter<String>(NavAppBarActivity.this, android.R.layout.simple_spinner_dropdown_item, strings));
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    newgps = new NewGPSTracker(mContext, NavAppBarActivity.this);

                    // Check if GPS enabled
                    if (newgps.canGetLocation()) {

                        latitude = newgps.getLatitude();
                        longitude = newgps.getLongitude();

                        // \n is for new line
                        //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " +   latitude + "\nLong: " +  longitude, Toast.LENGTH_LONG).show();
                        try {
                            geocoder = new Geocoder(this, Locale.ENGLISH);
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
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

                                //getuser_location.setText(str3);
                                //  countrycode.setText(str4);
                                //  pincodeno.setText(str5);
                                //  statename.setText(str6);
                                //Toast.makeText(getApplicationContext(), str1, Toast.LENGTH_SHORT).show();
                                //donator_addre.setEnabled(false);
                                //city.setText(str1);
                                Toast.makeText(getApplicationContext(), "Your Location is -" + str3, Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(this, "Unable to find Geocoder", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("tag", e.getMessage());
                        }
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public double setlatitude() {
        return latitude;
    }

    public double setlongitude() {
        return longitude;
    }


    private void setcityDialog() {
        final String[] multiChoiceItems = {"Hyderabad"};
        new AlertDialog.Builder(this)
                .setTitle("Select your City")
                .setCancelable(false)
                .setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, multiChoiceItems), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getuser_location.setText(multiChoiceItems[which]);
                        new SessionStorage().clearString(NavAppBarActivity.this, SessionStorage.location);
                        new SessionStorage().saveString(NavAppBarActivity.this, SessionStorage.location, multiChoiceItems[which]);
                    }
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public List<HomeMenuIcons> homeMenuIcons() {
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_parking, "Parking"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_evcharg, "Charge"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_mechanics, "Repair"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_tyre, "Puncture"));
        //homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_spareparts, "spares"));
        // homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_accessroies, "accessories"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_battery, "Batteries"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_key, "Key Repair"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_waterwash, "Bike wash"));
        return homeMenuIcons;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_my_testdrives) {
            Intent i = new Intent(this, MyTestDrivesActivity.class);
            startActivity(i);
        }
        if (id == R.id.nav_dealer) {
            // Handle the camera action
            // Toast.makeText(this, "under development", Toast.LENGTH_SHORT).show();
            Intent inten = new Intent(getApplicationContext(), SellersListActivity.class);
            inten.putExtra("fromactivity", "navmenu");
            startActivity(inten);
        }
        if (id == R.id.nav_my_orders) {
            Intent i = new Intent(this, MyOrdersActivity.class);
            startActivity(i);
        }
        if (id == R.id.nav_logout) {
           /* SessionStorage.clearString(NavAppBarActivity.this,SessionStorage.tokenvalue);
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();*/
            logoutviaApi();
        }

        if(id==R.id.nav_partner){
            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.ewheelers.ewheelers"));
            startActivity(viewIntent);
        }

        if (id == R.id.share_app) {
            // Handle the share action
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLargest eBike Digital Store\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        }

        if(id==R.id.nav_blog){
            Intent in = new Intent(getApplicationContext(), BuyerGuideActivity.class);
            in.putExtra("opens", "blog");
            startActivity(in);
        }
        if(id==R.id.nav_contactus){
            Intent in = new Intent(getApplicationContext(), BuyerGuideActivity.class);
            in.putExtra("opens", "openshort");
            startActivity(in);
        }
        if(id==R.id.nav_share){
            Intent in = new Intent(getApplicationContext(), BuyerGuideActivity.class);
            in.putExtra("opens", "support");
            startActivity(in);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void logoutviaApi() {

        String url_link = Apis.logout;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        SessionStorage.clearString(NavAppBarActivity.this, SessionStorage.tokenvalue);
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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

    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(NavAppBarActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void getCartCountHome() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.home;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                        String cartcount = dataJsonObject.getString("cartItemsCount");
                        String unreadnoti = dataJsonObject.getString("totalUnreadNotificationCount");

                        mainCartCount.setText(cartcount);

                        navView.getOrCreateBadge(R.id.navigation_alerts).setNumber(Integer.parseInt(unreadnoti));

                    } else {
                        Toast.makeText(NavAppBarActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
    public void onResume() {
        getCartCountHome();
        super.onResume();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                finish();
            }
        }, 2000);
    }

    public void setcount(String unread) {
        navView.getOrCreateBadge(R.id.navigation_alerts).setNumber(Integer.parseInt(unread));
       /* if(unread.equals("0")){
            BadgeDrawable badge = navView.getOrCreateBadge(R.id.navigation_alerts);
            badge.setVisible(false);
        }else {
            BadgeDrawable badge = navView.getOrCreateBadge(R.id.navigation_alerts);
            badge.setBadgeGravity(BadgeDrawable.BOTTOM_END);
            badge.setNumber(Integer.parseInt(unread));
            badge.setVisible(true);
        }*/
    }

}
