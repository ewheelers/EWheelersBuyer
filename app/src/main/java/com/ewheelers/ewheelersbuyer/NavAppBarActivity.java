package com.ewheelers.ewheelersbuyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.CollectionProductsAdapter;
import com.ewheelers.ewheelersbuyer.Adapters.MenuIconAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.HomeCollectionProducts;
import com.ewheelers.ewheelersbuyer.ModelClass.HomeMenuIcons;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.ui.home.HomeFragment;
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

import static com.ewheelers.ewheelersbuyer.SessionStorage.tokenvalue;

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
    double latitude, longitude;

    TextView getuser_location;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;

    TextView mainCartCount;
    String tokenvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_app_bar);

        getuser_location = findViewById(R.id.fetch_location);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_drawer);
        mHeaderView = navigationView.getHeaderView(0);
       /* username = mHeaderView.findViewById(R.id.main_username);
        emailId = mHeaderView.findViewById(R.id.emailid);*/
        linearLayoutback = mHeaderView.findViewById(R.id.back_layout);
        recyclerView = findViewById(R.id.static_menus);
        showmenuicon = findViewById(R.id.showmenu);
        mainCartCount = findViewById(R.id.maincartcount);
        tokenvalue = new SessionStorage().getStrings(NavAppBarActivity.this,SessionStorage.tokenvalue);

        mainCartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CartListingActivity.class);
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
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_alerts, R.id.navigation_wallet, R.id.navigation_help, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


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
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,4);
       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(menuIconAdapter);

        Intent intent = new Intent(getApplicationContext(), DiscountActivity.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);

      //  getcurrentLocation();
        //getCartCountHome();
        initGoogleAPIClient();
        showSettingDialog();

        String location = new SessionStorage().getStrings(this,SessionStorage.location);
        if(location!=null){
            getuser_location.setText(location);
            getuser_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setcityDialog();
                }
            });
        }
        if(getuser_location.getText().toString().equals("location")){
            final TourGuide mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click) .setPointer(new Pointer()) .setToolTip(new ToolTip().setTitle("Choose location!").setDescription("Click on Get Started to begin...")) .setOverlay(new Overlay()) .playOn(getuser_location);
            getuser_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTourGuideHandler.cleanUp();
                    setcityDialog();
                }
            });
        }



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
                        new SessionStorage().clearString(NavAppBarActivity.this,SessionStorage.location);
                        new SessionStorage().saveString(NavAppBarActivity.this,SessionStorage.location,multiChoiceItems[which]);
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

    public void getcurrentLocation() {
        gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // \n is for new line

            // latTextView.setText(String.valueOf(latitude));
            // lonTextView.setText(String.valueOf(longitude));

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

                    getuser_location.setText(str3);
                    //  countrycode.setText(str4);
                    //  pincodeno.setText(str5);
                    //  statename.setText(str6);
                    //Toast.makeText(getApplicationContext(), str1, Toast.LENGTH_SHORT).show();
                    //donator_addre.setEnabled(false);
                    //city.setText(str1);
                } else {
                    Toast.makeText(this, "Unable to find Geocoder", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
// TODO Auto-generated catch block
                Log.e("tag", e.getMessage());
            }
        } else {
            //gps.showSettingsAlert();
            try {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                // Setting Dialog Title
                alertDialog.setTitle("GPS is not enabled.");
                // Setting Dialog Message
                alertDialog.setMessage("Enable and Refresh App. Do you want to go to settings menu?");
                // On pressing Settings button
                alertDialog.setCancelable(false);
                alertDialog.create();
                alertDialog.setPositiveButton("Settings", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                //startActivity(intent);
                                startActivityForResult(intent, 1);
                            }
                        });
                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public List<HomeMenuIcons> homeMenuIcons() {
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_chargeplug, "charge"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_mechanics, "mechanic"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_punctureflat, "puncture"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_spareparts, "spares"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_accessroies, "accessories"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_waterwash, "water wash"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_battery, "battery"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_keyrepair, "key repair"));
        return homeMenuIcons;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dealer) {
            // Handle the camera action
            Toast.makeText(this, "under development", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.nav_logout){
            SessionStorage.clearString(NavAppBarActivity.this,SessionStorage.tokenvalue);
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

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

    public void getCartCountHome(){
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = Apis.home;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equals("1")) {
                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                        String cartcount = dataJsonObject.getString("cartItemsCount");
                        mainCartCount.setText(cartcount);

                    }else {
                        Toast.makeText(NavAppBarActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
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

}
