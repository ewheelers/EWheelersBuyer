package com.ewheelers.ewheelersbuyer.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.ewheelers.ewheelersbuyer.BuildConfig;
import com.ewheelers.ewheelersbuyer.BuyerGuideActivity;
import com.ewheelers.ewheelersbuyer.ModelClass.CirclePageIndicator;
import com.ewheelers.ewheelersbuyer.ModelClass.HomeCollectionProducts;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.ShowAlleBikesActivity;
import com.ewheelers.ewheelersbuyer.ShowServiceProvidersActivity;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private ImageLoader imageLoader;
    private NetworkImageView bookTestDrive, rentEbike;
    private HomeViewModel homeViewModel;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView, brandsRecyclerview, shopsRecyclerView, categoriesRecyclerView;
    CollectionProductsAdapter collectionProductsAdapter, collectionbrandAdapter, collectionshopsAdapter, categoriesAdapter;
    List<HomeCollectionProducts> homeCollectionProductsList = new ArrayList<>();
    List<HomeCollectionProducts> homeCollectionbrandList = new ArrayList<>();
    List<HomeCollectionProducts> homeCollectionSliderList = new ArrayList<>();
    List<HomeCollectionProducts> homeCollectionShopsList = new ArrayList<>();
    List<HomeCollectionProducts> homeCollectionCategoriesList = new ArrayList<>();

    TextView showalle_Bikes;
    TextView collectionTitle, brandsTitle, shopsTitle, categoriesTitle;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    private String tokenvalue, cartcount;

    Switch simpleSwitch;
    LinearLayout linearLayout_new, linearLayout_old;
    TextView mechanictxt, puncturetxt, waterwashtxt,termsConditions;
    ImageView openingshort;
    Button how_works, share_app;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tokenvalue = new SessionStorage().getStrings(getActivity(), SessionStorage.tokenvalue);
        //Toast.makeText(getActivity(), "token val: "+tokenvalue, Toast.LENGTH_SHORT).show();

       /* final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        termsConditions = root.findViewById(R.id.terms);
        how_works = root.findViewById(R.id.howworks);
        share_app = root.findViewById(R.id.shareapp);
        openingshort = root.findViewById(R.id.openingshortly);
        mechanictxt = root.findViewById(R.id.mechanic);
        puncturetxt = root.findViewById(R.id.puncture);
        waterwashtxt = root.findViewById(R.id.waterwash);
        mechanictxt.setOnClickListener(this);
        puncturetxt.setOnClickListener(this);
        waterwashtxt.setOnClickListener(this);
        openingshort.setOnClickListener(this);
        how_works.setOnClickListener(this);
        share_app.setOnClickListener(this);
        termsConditions.setOnClickListener(this);
        simpleSwitch = root.findViewById(R.id.simpleSwitch);
        linearLayout_new = root.findViewById(R.id.new_ui);
        linearLayout_old = root.findViewById(R.id.old_ui);
        linearLayout_old.setVisibility(View.GONE);
        linearLayout_new.setVisibility(View.VISIBLE);
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayout_old.setVisibility(View.VISIBLE);
                    linearLayout_new.setVisibility(View.GONE);
                } else {
                    linearLayout_old.setVisibility(View.GONE);
                    linearLayout_new.setVisibility(View.VISIBLE);
                }
            }
        });

        shopsRecyclerView = root.findViewById(R.id.collection_shops);
        categoriesRecyclerView = root.findViewById(R.id.collection_categories);
        shimmerFrameLayout = root.findViewById(R.id.shimmer_view_container);
        recyclerView = root.findViewById(R.id.collection_products);
        brandsRecyclerview = root.findViewById(R.id.collection_brands);
        collectionTitle = root.findViewById(R.id.collection_title);
        brandsTitle = root.findViewById(R.id.brands_title);
        shopsTitle = root.findViewById(R.id.shops_title);
        categoriesTitle = root.findViewById(R.id.categories_title);
        bookTestDrive = root.findViewById(R.id.book_test_drive);
        rentEbike = root.findViewById(R.id.rent_ebike);
        showalle_Bikes = root.findViewById(R.id.showallebikes);
        // imageViewBanner = root.findViewById(R.id.banner);
        showalle_Bikes.setOnClickListener(this);
        bookTestDrive.setOnClickListener(this);
        rentEbike.setOnClickListener(this);

        imageLoader = VolleySingleton.getInstance(getActivity()).getImageLoader();
        imageLoader.get("https:/www.ewheelers.in/image/slide/24/3/1/MOBILE?t=1580018050", ImageLoader.getImageListener(bookTestDrive, R.drawable.cart, android.R.drawable.ic_dialog_alert));
        bookTestDrive.setImageUrl("https://www.ewheelers.in//image//slide//24//3//1//MOBILE?t=1580018050", imageLoader);

        imageLoader.get("https://www.ewheelers.in/image/slide/19/3/1/MOBILE?t=1580017670", ImageLoader.getImageListener(rentEbike, R.drawable.cart, android.R.drawable.ic_dialog_alert));
        rentEbike.setImageUrl("https://www.ewheelers.in/image/slide/19/3/1/MOBILE?t=1580017670", imageLoader);

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
        homeCollectionSliderList.clear();
        homeCollectionCategoriesList.clear();
        homeCollectionShopsList.clear();
        homeCollectionProductsList.clear();
        homeCollectionbrandList.clear();

        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        String serverurl = Apis.home;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                        cartcount = dataJsonObject.getString("cartItemsCount");

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
                        if (jsonArrayCollections != null) {
                            for (int i = 0; i < jsonArrayCollections.length(); i++) {
                                JSONObject jsonObjectProducts = jsonArrayCollections.getJSONObject(i);
                               /* Iterator keys = jsonObjectProducts.keys();
                                while(keys.hasNext()) {
                                    String currentDynamicKey = (String) keys.next();
                                    String jsonString = jsonObjectProducts.getString(currentDynamicKey);
                                    JSONArray jsonArrayCategories = jsonObjectProducts.getJSONArray(currentDynamicKey);
                                }*/
                                String collectionType = jsonObjectProducts.getString("collection_type");

                                //collectionTitle.setText(collectionname);

                                if (collectionType.equals("2")) {

                                    String collection_id = jsonObjectProducts.getString("collection_id");
                                    //  String collectionimage = jsonObjectProducts.getString("collection_image");
                                    String collection_name = jsonObjectProducts.getString("collection_name");
                                    categoriesTitle.setText(collection_name);

                                    JSONArray jsonArrayCategories = jsonObjectProducts.getJSONArray("categories");

                                    for (int s = 0; s < jsonArrayCategories.length(); s++) {
                                        JSONObject jsonObjectcat = jsonArrayCategories.getJSONObject(s);
                                        String prodcat_id = jsonObjectcat.getString("prodcat_id");
                                        String prodcat_name = jsonObjectcat.getString("prodcat_name");
                                        String prodcat_description = jsonObjectcat.getString("prodcat_description");
                                        String category_image_url = jsonObjectcat.getString("category_image_url");
                                        HomeCollectionProducts homeCollectionProducts = new HomeCollectionProducts();
                                        homeCollectionProducts.setProdcategory_name(prodcat_name);
                                        homeCollectionProducts.setProdcategory_imageurl(category_image_url);
                                        homeCollectionProducts.setProdcategory_id(prodcat_id);
                                        homeCollectionProducts.setType(3);
                                        homeCollectionCategoriesList.add(homeCollectionProducts);

                                    }

                                }

                                if (collectionType.equals("3")) {
                                    String collectionName = jsonObjectProducts.getString("collection_name");
                                    shopsTitle.setText(collectionName);
                                    JSONArray jsonArrayshops = jsonObjectProducts.getJSONArray("shops");
                                    for (int k = 0; k < jsonArrayshops.length(); k++) {
                                        JSONObject jsonObjectshop1 = jsonArrayshops.getJSONObject(k);
                                        String shopbanner = jsonObjectshop1.getString("shop_banner");
                                        String shop_id = jsonObjectshop1.getString("shop_id");
                                        String shop_user_id = jsonObjectshop1.getString("shop_user_id");
                                        String shop_name = jsonObjectshop1.getString("shop_name");
                                        String country_name = jsonObjectshop1.getString("country_name");
                                        String state_name = jsonObjectshop1.getString("state_name");
                                        String rating = jsonObjectshop1.getString("rating");
                                        String shop_logo = jsonObjectshop1.getString("shop_logo");
                                        HomeCollectionProducts homeCollectionProducts = new HomeCollectionProducts();
                                        homeCollectionProducts.setShopbanner(shopbanner);
                                        homeCollectionProducts.setShopid(shop_id);
                                        homeCollectionProducts.setShoplogo(shop_logo);
                                        homeCollectionProducts.setShopname(shop_name);
                                        homeCollectionProducts.setShopuserid(shop_user_id);
                                        homeCollectionProducts.setCountryname(country_name);
                                        homeCollectionProducts.setStatename(state_name);
                                        homeCollectionProducts.setRating(rating);
                                        homeCollectionProducts.setType(4);
                                        homeCollectionShopsList.add(homeCollectionProducts);
                                        //  Picasso.get().load(shopbanner).fit().into(imageViewBanner);
                                    }
                                }


                                if (collectionType.equals("1")) {
                                    String collection_id = jsonObjectProducts.getString("collection_id");
                                    //   String collectionimage = jsonObjectProducts.getString("collection_image");
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
                                        homeCollectionProducts.setCartItems(cartcount);
                                        homeCollectionProductsList.add(homeCollectionProducts);
                                    }

                                }

                                if (collectionType.equals("4")) {
                                    String collection_Name = jsonObjectProducts.getString("collection_name");
                                    brandsTitle.setText(collection_Name);
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

                        }
                        collectionProductsAdapter = new CollectionProductsAdapter(getActivity(), homeCollectionProductsList);
                        collectionbrandAdapter = new CollectionProductsAdapter(getActivity(), homeCollectionbrandList);
                        collectionshopsAdapter = new CollectionProductsAdapter(getActivity(), homeCollectionShopsList);
                        categoriesAdapter = new CollectionProductsAdapter(getActivity(), homeCollectionCategoriesList);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        final LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

                        recyclerView.setLayoutManager(linearLayoutManager);
                        brandsRecyclerview.setLayoutManager(linearLayoutManager2);
                        shopsRecyclerView.setLayoutManager(linearLayoutManager3);
                        categoriesRecyclerView.setLayoutManager(linearLayoutManager4);

                        recyclerView.setAdapter(collectionProductsAdapter);
                        brandsRecyclerview.setAdapter(collectionbrandAdapter);
                        shopsRecyclerView.setAdapter(collectionshopsAdapter);
                        categoriesRecyclerView.setAdapter(categoriesAdapter);

                        collectionProductsAdapter.notifyDataSetChanged();
                        collectionbrandAdapter.notifyDataSetChanged();
                        collectionshopsAdapter.notifyDataSetChanged();
                        categoriesAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
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
            case R.id.terms:
                Intent i = new Intent(getActivity(), BuyerGuideActivity.class);
                i.putExtra("opens", "terms");
                startActivity(i);
                break;
            case R.id.howworks:
                i = new Intent(getActivity(), BuyerGuideActivity.class);
                i.putExtra("opens", "howworks");
                startActivity(i);
                break;
            case R.id.shareapp:
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
                break;
            case R.id.openingshortly:
                i = new Intent(getActivity(), BuyerGuideActivity.class);
                i.putExtra("opens", "openshort");
                startActivity(i);
                break;
            case R.id.mechanic:
                i = new Intent(getActivity(), ShowServiceProvidersActivity.class);
                i.putExtra("providerIs", "Mechanic");
                startActivity(i);
                break;
            case R.id.puncture:
                i = new Intent(getActivity(), ShowServiceProvidersActivity.class);
                i.putExtra("providerIs", "Puncture");
                startActivity(i);
                break;
            case R.id.waterwash:
                i = new Intent(getActivity(), ShowServiceProvidersActivity.class);
                i.putExtra("providerIs", "Water wash");
                startActivity(i);
                break;
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

    private void init(List<HomeCollectionProducts> slideUrl, View v) {
        for (int i = 0; i < slideUrl.size(); i++)
            ImagesArray.add(slideUrl.get(i).getSlideImageurl());

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), ImagesArray));

        CirclePageIndicator indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = slideUrl.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, false);
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