package com.ewheelers.ewheelersbuyer.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.CollectionProductsAdapter;
import com.ewheelers.ewheelersbuyer.Adapters.SlidingImage_Adapter;
import com.ewheelers.ewheelersbuyer.GPSTracker;
import com.ewheelers.ewheelersbuyer.MainActivity;
import com.ewheelers.ewheelersbuyer.ModelClass.CirclePageIndicator;
import com.ewheelers.ewheelersbuyer.ModelClass.HomeCollectionProducts;
import com.ewheelers.ewheelersbuyer.NavAppBarActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.ShowAlleBikesActivity;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private ImageLoader imageLoader;
    private NetworkImageView bookTestDrive, rentEbike;
    private HomeViewModel homeViewModel;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView, brandsRecyclerview;
    CollectionProductsAdapter collectionProductsAdapter, collectionbrandAdapter;
    List<HomeCollectionProducts> homeCollectionProductsList = new ArrayList<>();
    List<HomeCollectionProducts> homeCollectionbrandList = new ArrayList<>();
    List<HomeCollectionProducts> homeCollectionSliderList = new ArrayList<>();

    TextView showalle_Bikes;
    TextView collectionTitle, brandsTitle;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    private String tokenvalue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tokenvalue = new SessionStorage().getStrings(getActivity(),SessionStorage.tokenvalue);
        Toast.makeText(getActivity(), "token val: "+tokenvalue, Toast.LENGTH_SHORT).show();

       /* final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        shimmerFrameLayout = root.findViewById(R.id.shimmer_view_container);
        recyclerView = root.findViewById(R.id.collection_products);
        brandsRecyclerview = root.findViewById(R.id.collection_brands);
        collectionTitle = root.findViewById(R.id.collection_title);
        brandsTitle = root.findViewById(R.id.brands_title);
        bookTestDrive = root.findViewById(R.id.book_test_drive);
        rentEbike = root.findViewById(R.id.rent_ebike);
        showalle_Bikes = root.findViewById(R.id.showallebikes);
        // imageViewBanner = root.findViewById(R.id.banner);
        showalle_Bikes.setOnClickListener(this);
        bookTestDrive.setOnClickListener(this);
        rentEbike.setOnClickListener(this);

        imageLoader = VolleySingleton.getInstance(getActivity()).getImageLoader();
        imageLoader.get("http://www.ewheelers.in//image//slide//24//3//1//MOBILE?t=1580018050", ImageLoader.getImageListener(bookTestDrive, R.drawable.cart, android.R.drawable.ic_dialog_alert));
        bookTestDrive.setImageUrl("http://www.ewheelers.in//image//slide//24//3//1//MOBILE?t=1580018050", imageLoader);

        imageLoader.get("http://www.ewheelers.in/image/slide/19/3/1/MOBILE?t=1580017670", ImageLoader.getImageListener(rentEbike, R.drawable.cart, android.R.drawable.ic_dialog_alert));
        rentEbike.setImageUrl("http://www.ewheelers.in/image/slide/19/3/1/MOBILE?t=1580017670", imageLoader);

        getCollectionProducts(root);


        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmerFrameLayout.stopShimmerAnimation();
        super.onPause();
    }


    public void getCollectionProducts(final View view) {
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                       // Toast.makeText(getActivity(), "cartcountMain:" + cartcount, Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = dataJsonObject.getJSONArray("slides");
                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObjectSlides = jsonArray.getJSONObject(k);
                            String slidimageurl = jsonObjectSlides.getString("slide_image_url");
                            HomeCollectionProducts homeCollectionProductsSlides = new HomeCollectionProducts();
                            homeCollectionProductsSlides.setSlideImageurl(slidimageurl);
                            homeCollectionSliderList.add(homeCollectionProductsSlides);

                        }
                        init(homeCollectionSliderList, view);

                        JSONArray jsonArrayCollections = dataJsonObject.getJSONArray("collections");

                        for (int i = 0; i < jsonArrayCollections.length(); i++) {
                            JSONObject jsonObjectProducts = jsonArrayCollections.getJSONObject(i);
                            String collection_id = jsonObjectProducts.getString("collection_id");

                            if (collection_id.equals("3")) {
                                JSONArray jsonArrayshops = jsonObjectProducts.getJSONArray("shops");
                                JSONObject jsonObjectshop1 = jsonArrayshops.getJSONObject(0);
                                String shopbanner = jsonObjectshop1.getString("shop_banner");
                                //  Picasso.get().load(shopbanner).fit().into(imageViewBanner);
                            }

                            if (collection_id.equals("5")) {
                                String collection_name = jsonObjectProducts.getString("collection_name");
                                collectionTitle.setText(collection_name);
                                JSONArray jsonArrayProducts = jsonObjectProducts.getJSONArray("products");
                                for (int j = 0; j < jsonArrayProducts.length(); j++) {
                                    JSONObject products = jsonArrayProducts.getJSONObject(j);
                                    String productName = products.getString("product_name");
                                    String productPrice = products.getString("selprod_price");
                                    String productImageurl = products.getString("product_image_url");
                                    String selproductid = products.getString("selprod_id");
                                    String productid = products.getString("product_id");
                                    String productcatname = products.getString("prodcat_name");
                                    String isSell = products.getString("is_sell");
                                    String isRent = products.getString("is_rent");
                                    String rentPrice = products.getString("rent_price");
                                    String rentaltype = products.getString("rental_type");

                                    HomeCollectionProducts homeCollectionProducts = new HomeCollectionProducts();
                                    homeCollectionProducts.setProdcat_name(productcatname);
                                    homeCollectionProducts.setProduct_name(productName);
                                    homeCollectionProducts.setSelprod_price(productPrice);
                                    homeCollectionProducts.setProduct_image_url(productImageurl);
                                    homeCollectionProducts.setSelprod_id(selproductid);
                                    homeCollectionProducts.setProduct_id(productid);
                                    homeCollectionProducts.setIs_rent(isRent);
                                    homeCollectionProducts.setType(0);
                                    homeCollectionProductsList.add(homeCollectionProducts);
                                }

                            }

                            if (collection_id.equals("4")) {
                                String collection_name = jsonObjectProducts.getString("collection_name");
                                brandsTitle.setText(collection_name);
                                JSONArray jsonArrayBrands = jsonObjectProducts.getJSONArray("brands");
                                for (int j = 0; j < jsonArrayBrands.length(); j++) {
                                    JSONObject products = jsonArrayBrands.getJSONObject(j);
                                    String brandid = products.getString("brand_id");
                                    String brandname = products.getString("brand_name");
                                    String brandimage = products.getString("brand_image");

                                    HomeCollectionProducts homeCollectionProducts = new HomeCollectionProducts();
                                    homeCollectionProducts.setBrandimageurl(brandimage);
                                    homeCollectionProducts.setType(1);
                                    homeCollectionbrandList.add(homeCollectionProducts);
                                }

                            }

                        }
                        collectionProductsAdapter = new CollectionProductsAdapter(getActivity(), homeCollectionProductsList);
                        collectionbrandAdapter = new CollectionProductsAdapter(getActivity(), homeCollectionbrandList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        final LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        brandsRecyclerview.setLayoutManager(linearLayoutManager2);
                        recyclerView.setAdapter(collectionProductsAdapter);
                        brandsRecyclerview.setAdapter(collectionbrandAdapter);

                        collectionProductsAdapter.notifyDataSetChanged();
                        collectionbrandAdapter.notifyDataSetChanged();

                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_test_drive:

                break;
            case R.id.rent_ebike:

                break;
            case R.id.showallebikes:
                Intent intent = new Intent(getActivity(), ShowAlleBikesActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void init(List<HomeCollectionProducts> slideUrl,View v) {
        for(int i=0;i<slideUrl.size();i++)
            ImagesArray.add(slideUrl.get(i).getSlideImageurl());

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(),ImagesArray));

        CirclePageIndicator indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =slideUrl.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 4000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}