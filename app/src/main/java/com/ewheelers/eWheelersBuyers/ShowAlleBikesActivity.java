package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.ewheelers.eWheelersBuyers.Adapters.AllebikesAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.CategoriesFilterAdapter;
import com.ewheelers.eWheelersBuyers.Adapters.FiltersAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.AllebikesModelClass;
import com.ewheelers.eWheelersBuyers.ModelClass.FilterListClass;
import com.ewheelers.eWheelersBuyers.ModelClass.SubFilterModelClass;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

public class ShowAlleBikesActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    AllebikesAdapter allebikesAdapter;
    List<AllebikesModelClass> allebikelist = new ArrayList<>();
    TextView textView, title;
    String token;
    String collectionidbikes, collectionidcat, collectionidbrands, collectionidshops;
    String collectionid, tokenvalue, allproducts;
    String brandid, brandName, branddescription, categoryid, categoryName, shopid, shopname, shoplogo, shopaddress, typebtn, shopphone, shopbanner;
    String contentOfBrackets, collectid;
    int stock;
    TextView textView_empty;
    NetworkImageView networkImageView, bannerImage;
    LinearLayout linearLayoutEmpty, sort_items, filter_items;

    SearchView searchView;
    ListView list;
    List<AllebikesModelClass> allebikesModelClassesList = new ArrayList<>();

    String onlyTestDrive;
    BottomSheetDialog mBottomSheetDialog;
    TextView relevant, saleP, rentP, bookP, testP, priceLtoH, priceHtoL;

    LinearLayout sortFilterLayout;
    ProgressBar progressBar;
    TextView phonenoshop;
    Dialog dialog, catdialog;
    ImageView imageView, imageviewcat;
    Button button, buttoncat;
    RadioGroup radioGroup;
    RadioButton all, chip1, chip2, chip3, chip4;
    RecyclerView recyclrecat, recyclerbrand, recycleoptions;

    List<FilterListClass> filterListClasses = new ArrayList<>();
    List<FilterListClass> filterListClasses2 = new ArrayList<>();
    List<FilterListClass> filterListClassesCategory = new ArrayList<>();
    List<FilterListClass> filterListClassesidBased = new ArrayList<>();


    String categoriesarray, brandarray, optionarray;
    ProgressBar progressFilter;
    BubbleThumbRangeSeekbar bubbleThumbRangeSeekbar;
    RelativeLayout relativeseekbar;
    TextView maxprice, minprice;

    RecyclerView recyclerViewchipfilter;
    RecyclerView recyclerViewidbased;
    Chip chipall;
    Button floatingactionbutton;
    NestedScrollView nestedScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alle_bikes);
        token = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        progressBar = findViewById(R.id.progress);
        recyclerViewchipfilter = findViewById(R.id.chip_list);
        phonenoshop = findViewById(R.id.phoneno_shop);
        title = findViewById(R.id.texttitle);
        textView = findViewById(R.id.titleTxt);
        recyclerView = findViewById(R.id.all_ebikeslist);
        textView_empty = findViewById(R.id.emptyView);
        linearLayoutEmpty = findViewById(R.id.empty_layout);
        networkImageView = findViewById(R.id.shopimage);
        floatingactionbutton = findViewById(R.id.floating_action_button);
        nestedScrollView = findViewById(R.id.nestedscroll);
        chipall = findViewById(R.id.allchip);
        chipall.setChecked(true);
        chipall.setTextColor(Color.parseColor("#9C3C34"));
        sortFilterLayout = findViewById(R.id.sortFilter);

        sort_items = findViewById(R.id.sortItems);
        filter_items = findViewById(R.id.filterItems);
        filter_items.setOnClickListener(this);
        sort_items.setOnClickListener(this);
        phonenoshop.setOnClickListener(this);
        bannerImage = findViewById(R.id.banner);
        bannerImage.setOnClickListener(this);
        floatingactionbutton.setOnClickListener(this);
        searchView = findViewById(R.id.searchview);
        list = findViewById(R.id.listview);

        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.sort_layout, null);
        relevant = sheetView.findViewById(R.id.relevance);
        saleP = sheetView.findViewById(R.id.sale);
        rentP = sheetView.findViewById(R.id.rent);
        bookP = sheetView.findViewById(R.id.booking);
        testP = sheetView.findViewById(R.id.testdrive);
        priceLtoH = sheetView.findViewById(R.id.price_lowtohigh);
        priceHtoL = sheetView.findViewById(R.id.price_hightolow);
        mBottomSheetDialog.setContentView(sheetView);
        relevant.setTextColor(Color.parseColor("#9C3C34"));

        dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View dialogView = getLayoutInflater().inflate(R.layout.filter_layout, null);
        imageView = dialogView.findViewById(R.id.closeIcon);
        progressFilter = dialogView.findViewById(R.id.progressf);
        radioGroup = dialogView.findViewById(R.id.radio_group);
        button = dialogView.findViewById(R.id.apply);
        all = dialogView.findViewById(R.id.byall);
        chip1 = dialogView.findViewById(R.id.bycategory);
        chip2 = dialogView.findViewById(R.id.bybrand);
        chip4 = dialogView.findViewById(R.id.byoptions);
        chip3 = dialogView.findViewById(R.id.byprice);
        relativeseekbar = dialogView.findViewById(R.id.relativeseekbar);
        bubbleThumbRangeSeekbar = dialogView.findViewById(R.id.rangeSeekbar2);
        minprice = dialogView.findViewById(R.id.textMin2);
        maxprice = dialogView.findViewById(R.id.textMax2);
        recyclrecat = dialogView.findViewById(R.id.categories_list);
        recyclerbrand = dialogView.findViewById(R.id.brands_list);
        recycleoptions = dialogView.findViewById(R.id.options_list);
        LinearLayout linearLayoutall = dialogView.findViewById(R.id.allfilterslay);

        dialog.setContentView(dialogView);
        imageView.setOnClickListener(this);
        button.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast.makeText(ShowAlleBikesActivity.this, "keyword:" + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!=null) {
                    allebikesAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        //from CollectionProductsAdapter
        collectid = getIntent().getStringExtra("collid");
        brandid = getIntent().getStringExtra("brandid");
        brandName = getIntent().getStringExtra("brandname");
        branddescription = getIntent().getStringExtra("branddescription");
        categoryid = getIntent().getStringExtra("catid");
        categoryName = getIntent().getStringExtra("catname");
        shopid = getIntent().getStringExtra("shopid");
        shopname = getIntent().getStringExtra("shopname");
        shoplogo = getIntent().getStringExtra("shopimage");
        shopaddress = getIntent().getStringExtra("shopaddress");
        shopphone = getIntent().getStringExtra("shopphone");
        shopbanner = getIntent().getStringExtra("shopbanner");

        // stock = getIntent().getIntExtra("instock",0);
        typebtn = getIntent().getStringExtra("type");
        textView.setText(typebtn);

        nestedScrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (nestedScrollView.getChildAt(0).getBottom() <= (nestedScrollView.getHeight() + nestedScrollView.getScrollY())) {
                            //scroll view is at bottom
                            floatingactionbutton.setVisibility(View.VISIBLE);
                        } else {
                            //scroll view is not at bottom
                            floatingactionbutton.setVisibility(View.GONE);
                        }
                    }
                });

        if (collectid != null && brandid != null) {
            getBrandbannerurl(collectid, brandid);
        }

        if ((branddescription != null)) {
            int firstBracket = branddescription.indexOf('(');
            if (firstBracket == 0) {
                contentOfBrackets = branddescription.substring(firstBracket + 1, branddescription.indexOf(')', firstBracket));
                //Toast.makeText(this, "link:" + contentOfBrackets, Toast.LENGTH_SHORT).show();
            }
        }
        Log.i("ids:", "brandis-" + brandid + " categoryid-" + categoryid + " shopid-" + shopid);

        collectionidbikes = getIntent().getStringExtra("allpopularbikes");
        collectionidcat = getIntent().getStringExtra("allcategories");
        collectionidbrands = getIntent().getStringExtra("allbrands");
        collectionidshops = getIntent().getStringExtra("allshops");
        onlyTestDrive = getIntent().getStringExtra("onlytestdrives");

        // Toast.makeText(this, "coolectid" + collectionidbikes + "idcat:" + collectionidcat + "idbrand" + collectionidbrands + "idshops:" + collectionidshops, Toast.LENGTH_SHORT).show();

        if (all.isChecked()) {
            recyclrecat.setVisibility(View.VISIBLE);
            recyclerbrand.setVisibility(GONE);
            relativeseekbar.setVisibility(View.VISIBLE);
            recycleoptions.setVisibility(GONE);
            if (categoryid != null) {
                getIdbasedFiltersList(categoryid, "", "");
            } else if (shopid != null) {
                getIdbasedFiltersList("", shopid, "");
            } else if (brandid != null) {
                getIdbasedFiltersList("", "", brandid);
            } else {
                getIdbasedFiltersList("", "", "");
            }

        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = group.getCheckedRadioButtonId();
                if (selectedId == R.id.byall) {
                    recyclrecat.setVisibility(View.VISIBLE);
                    recyclerbrand.setVisibility(GONE);
                    relativeseekbar.setVisibility(View.VISIBLE);
                    recycleoptions.setVisibility(GONE);
                    if (categoryid != null) {
                        getIdbasedFiltersList(categoryid, "", "");
                    } else if (shopid != null) {
                        getIdbasedFiltersList("", shopid, "");
                    } else if (brandid != null) {
                        getIdbasedFiltersList("", "", brandid);
                    } else {
                        getIdbasedFiltersList("", "", "");
                    }
                }

                if (selectedId == R.id.bycategory) {
                    recyclrecat.setVisibility(View.VISIBLE);
                    recyclerbrand.setVisibility(GONE);
                    relativeseekbar.setVisibility(GONE);
                    recycleoptions.setVisibility(GONE);
                    if (categoryid != null) {
                        getFiltersList(categoryid, "", "");
                    } else if (shopid != null) {
                        getFiltersList("", shopid, "");
                    } else if (brandid != null) {
                        getFiltersList("", "", brandid);
                    } else {
                        getFiltersList("", "", "");
                    }
                    //  getIdbasedFiltersList();
                }
                if (selectedId == R.id.bybrand) {
                    recyclerbrand.setVisibility(View.VISIBLE);
                    recyclrecat.setVisibility(GONE);
                    relativeseekbar.setVisibility(GONE);
                    recycleoptions.setVisibility(GONE);
                    if (categoryid != null) {
                        getBrandFilterList(categoryid, "", "");
                    } else if (shopid != null) {
                        getBrandFilterList("", shopid, "");
                    } else if (brandid != null) {
                        getBrandFilterList("", "", brandid);

                    } else {
                        getBrandFilterList("", "", "");

                    }

                    // getIdbasedFiltersList();
                }

                if (selectedId == R.id.byoptions) {
                    recyclerbrand.setVisibility(GONE);
                    recyclrecat.setVisibility(GONE);
                    relativeseekbar.setVisibility(GONE);
                    recycleoptions.setVisibility(View.VISIBLE);
                    if (categoryid != null) {
                        getOptionsFilterList(categoryid, "", "");
                    } else if (shopid != null) {
                        getOptionsFilterList("", shopid, "");
                    } else if (brandid != null) {
                        getOptionsFilterList("", "", brandid);
                    } else {
                        getOptionsFilterList("", "", "");
                    }
                    // getIdbasedFiltersList();
                }

                if (selectedId == R.id.byprice) {
                    recyclerbrand.setVisibility(GONE);
                    recyclrecat.setVisibility(GONE);
                    relativeseekbar.setVisibility(View.VISIBLE);
                    recycleoptions.setVisibility(GONE);
                    if (categoryid != null) {
                        getPricerange(categoryid, "", "");
                    } else if (shopid != null) {
                        getPricerange("", shopid, "");
                    } else if (brandid != null) {
                        getPricerange("", "", brandid);
                    } else {
                        getPricerange("", "", "");
                    }
                }

            }
        });

        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Do something
            }
        });
        relevant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryid != null) {
                    getcategoryProducts(categoryid, "", "", "");
                    //getFiltersofcategory(categoryid);
                    //getIdbasedFiltersList();
                } else if (onlyTestDrive != null) {
                    if (onlyTestDrive.equals("0")) {
                        getOnlyRentProducts("2", "", "");
                    } else {
                        getOnlyRentProducts("1", "test_drive", "");
                    }
                } else if (brandid != null) {
                    getfilterProducts(brandid, "", "", "");
                } else if (shopid != null) {
                    getShopProducts(shopid, "", "", "");
                } else {
                    // getAllebikes("");
                    getCollectionProductsBikes(collectionidbikes);

                }
                relevant.setTextColor(Color.parseColor("#9C3C34"));
                saleP.setTextColor(Color.BLACK);
                bookP.setTextColor(Color.BLACK);
                rentP.setTextColor(Color.BLACK);
                testP.setTextColor(Color.BLACK);
                priceLtoH.setTextColor(Color.BLACK);
                priceHtoL.setTextColor(Color.BLACK);
                mBottomSheetDialog.dismiss();
            }
        });
        saleP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryid != null) {
                    getcategoryProducts(categoryid, "[1]", "", "");
                    //getFiltersofcategory(categoryid);
                } else if (brandid != null) {
                    getfilterProducts(brandid, "[1]", "", "");
                } else if (shopid != null) {
                    getShopProducts(shopid, "[1]", "", "");
                } else {
                    getOnlyRentProducts("1", "", "");
                }
                relevant.setTextColor(Color.BLACK);
                saleP.setTextColor(Color.parseColor("#9C3C34"));
                bookP.setTextColor(Color.BLACK);
                rentP.setTextColor(Color.BLACK);
                testP.setTextColor(Color.BLACK);
                priceLtoH.setTextColor(Color.BLACK);
                priceHtoL.setTextColor(Color.BLACK);
                mBottomSheetDialog.dismiss();
            }
        });
        rentP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryid != null) {
                    getcategoryProducts(categoryid, "[2]", "", "");
                    //getFiltersofcategory(categoryid);
                } else if (brandid != null) {
                    getfilterProducts(brandid, "[2]", "", "");
                } else if (shopid != null) {
                    getShopProducts(shopid, "[2]", "", "");
                } else {
                    getOnlyRentProducts("2", "", "");
                }
                relevant.setTextColor(Color.BLACK);
                saleP.setTextColor(Color.BLACK);
                bookP.setTextColor(Color.BLACK);
                rentP.setTextColor(Color.parseColor("#9C3C34"));
                testP.setTextColor(Color.BLACK);
                priceLtoH.setTextColor(Color.BLACK);
                priceHtoL.setTextColor(Color.BLACK);
                mBottomSheetDialog.dismiss();
            }
        });
        bookP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryid != null) {
                    getcategoryProducts(categoryid, "[1]", "", "book");
                    // getFiltersofcategory(categoryid);
                } else if (brandid != null) {
                    getfilterProducts(brandid, "[1]", "book", "");
                } else if (shopid != null) {
                    getShopProducts(shopid, "[1]", "book", "");
                } else {
                    getOnlyRentProducts("1", "book", "");
                }
                relevant.setTextColor(Color.BLACK);
                saleP.setTextColor(Color.BLACK);
                bookP.setTextColor(Color.parseColor("#9C3C34"));
                rentP.setTextColor(Color.BLACK);
                testP.setTextColor(Color.BLACK);
                priceLtoH.setTextColor(Color.BLACK);
                priceHtoL.setTextColor(Color.BLACK);
                mBottomSheetDialog.dismiss();
            }
        });
        testP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryid != null) {
                    getcategoryProducts(categoryid, "[1]", "", "test_drive");
                    // getFiltersofcategory(categoryid);
                } else if (brandid != null) {
                    getfilterProducts(brandid, "[1]", "test_drive", "");
                } else if (shopid != null) {
                    getShopProducts(shopid, "[1]", "test_drive", "");
                } else {
                    getOnlyRentProducts("1", "test_drive", "");
                }
                relevant.setTextColor(Color.BLACK);
                saleP.setTextColor(Color.BLACK);
                bookP.setTextColor(Color.BLACK);
                rentP.setTextColor(Color.BLACK);
                testP.setTextColor(Color.parseColor("#9C3C34"));
                priceLtoH.setTextColor(Color.BLACK);
                priceHtoL.setTextColor(Color.BLACK);
                mBottomSheetDialog.dismiss();
            }
        });
        priceLtoH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryid != null) {
                    getcategoryProducts(categoryid, "", "price_asc", "");
                    // getFiltersofcategory(categoryid);
                } else if (onlyTestDrive != null) {
                    if (onlyTestDrive.equals("0")) {
                        getOnlyRentProducts("2", "", "price_asc");
                    } else {
                        getOnlyRentProducts("1", "test_drive", "price_asc");
                    }
                } else if (brandid != null) {
                    getfilterProducts(brandid, "", "", "price_asc");
                } else if (shopid != null) {
                    getShopProducts(shopid, "", "", "price_asc");
                } else {
                    getAllebikes("price_asc");
                }
                relevant.setTextColor(Color.BLACK);
                saleP.setTextColor(Color.BLACK);
                bookP.setTextColor(Color.BLACK);
                rentP.setTextColor(Color.BLACK);
                priceLtoH.setTextColor(Color.parseColor("#9C3C34"));
                testP.setTextColor(Color.BLACK);
                priceHtoL.setTextColor(Color.BLACK);
                mBottomSheetDialog.dismiss();
            }
        });
        priceHtoL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryid != null) {
                    getcategoryProducts(categoryid, "", "price_desc", "");
                    // getFiltersofcategory(categoryid);
                } else if (onlyTestDrive != null) {
                    if (onlyTestDrive.equals("0")) {
                        getOnlyRentProducts("2", "", "price_desc");
                    } else {
                        getOnlyRentProducts("1", "test_drive", "price_desc");
                    }
                } else if (brandid != null) {
                    getfilterProducts(brandid, "", "", "price_desc");
                } else if (shopid != null) {
                    getShopProducts(shopid, "", "", "price_desc");
                } else {
                    getAllebikes("price_desc");
                }
                relevant.setTextColor(Color.BLACK);
                saleP.setTextColor(Color.BLACK);
                bookP.setTextColor(Color.BLACK);
                rentP.setTextColor(Color.BLACK);
                priceHtoL.setTextColor(Color.parseColor("#9C3C34"));
                testP.setTextColor(Color.BLACK);
                priceLtoH.setTextColor(Color.BLACK);
                mBottomSheetDialog.dismiss();
            }
        });

        if (brandid != null) {
            getfilterProducts(brandid, "", "", "");
            //getIdbasedFiltersList("","",brandid);
            title.setVisibility(View.VISIBLE);
            title.setText(brandName);
            //linearLayoutall.setVisibility(GONE);
        }

        if (categoryid != null) {
            getcategoryProducts(categoryid, "", "", "");
            getFiltersofcategory(categoryid);
            title.setVisibility(View.VISIBLE);
            title.setText(categoryName);
            chipall.setVisibility(View.VISIBLE);
            //getIdbasedFiltersList(categoryid,"","");
            //linearLayoutall.setVisibility(View.VISIBLE);
        }

        if (shopid != null) {
            getShopProducts(shopid, "", "", "");
            getShopProductsBanner(shopid);
            //getIdbasedFiltersList("",shopid,"");
            //linearLayoutall.setVisibility(GONE);
        }

        if (onlyTestDrive != null) {
            saleP.setVisibility(GONE);
            rentP.setVisibility(GONE);
            testP.setVisibility(GONE);
            bookP.setVisibility(GONE);
            if (onlyTestDrive.equals("0")) {
                getOnlyRentProducts("2", "", "");
            } else {
                getOnlyRentProducts("1", "test_drive", "");
            }
        }


        if (collectionidbikes != null) {
            //collectionid = collectionidbikes;
            // getAllebikes("");
            getCollectionProductsBikes(collectionidbikes);
            getFiltersofcategory("");
            chipall.setVisibility(View.VISIBLE);
        }

        if (collectionidcat != null) {
            sortFilterLayout.setVisibility(GONE);
            //collectionid = collectionidcat;
            getCollectionProductsCategories(collectionidcat);

        }
        if (collectionidbrands != null) {
            sortFilterLayout.setVisibility(GONE);
            //collectionid = collectionidbrands;
            //getCollectionProducts(collectionidcat);
            getCollectionProductsBrands(collectionidbrands);

        }
        if (collectionidshops != null) {
            sortFilterLayout.setVisibility(GONE);
            getAllShops();
            //collectionid = collectionidshops;
        }


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        getCollectionProducts(collectionid);
    }

    private void getBrandbannerurl(String collection_id, String brandedid) {
        progressBar.setVisibility(View.VISIBLE);
        String Login_url = Apis.collectionView;
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
                                JSONObject jsonObjectCollection = jsonObjectData.getJSONObject("collection");
                                String collectionid = jsonObjectCollection.getString("collection_id");

                                String collectionlayouttype = jsonObjectCollection.getString("collection_layout_type");
                                //brands
                                JSONObject jsonobjectbrands = jsonObjectData.getJSONObject("collectionItems");
                                JSONObject jsonObjectvallayouttype = jsonobjectbrands.getJSONObject(collectionlayouttype);
                                JSONObject jsonObjectvalcolid = jsonObjectvallayouttype.getJSONObject(collectionid);
                                JSONArray jsonArraybrands = jsonObjectvalcolid.getJSONArray("brands");
                                for (int l = 0; l < jsonArraybrands.length(); l++) {
                                    JSONObject jsonObjectbrand = jsonArraybrands.getJSONObject(l);
                                    String brandid = jsonObjectbrand.getString("brand_id");
                                    String brandname = jsonObjectbrand.getString("brand_name");
                                    String branddescript = jsonObjectbrand.getString("brand_short_description");
                                    String brandimage = jsonObjectbrand.getString("brand_image");

                                    if (brandid.equals(brandedid)) {
                                        if ((branddescript != null)) {
                                            int firstBracket = branddescript.indexOf('(');
                                            if (firstBracket == 0) {
                                                contentOfBrackets = branddescript.substring(firstBracket + 1, branddescript.indexOf(')', firstBracket));
                                            }
                                        }
                                    }
                                    /*AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setProductName(brandname);
                                    allebikesModelClass.setPrice(branddescript);
                                    allebikesModelClass.setProductid(brandid);
                                    allebikesModelClass.setNetworkImage(brandimage);
                                    allebikesModelClass.setTypeLayout(2);
                                    allebikelist.add(allebikesModelClass);*/
                                }


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
                data3.put("collection_id", collection_id);
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }

    private void getFiltersofcategory(String categoryid) {
//        progressFilter.setVisibility(View.VISIBLE);
        filterListClasses.clear();
        String Login_url = Apis.filters;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1"))
                            //   progressFilter.setVisibility(GONE);
                            {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONArray jsonArray = jsonObjectData.getJSONArray("categoriesArr");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectcat = jsonArray.getJSONObject(i);
                                    String cat_id = jsonObjectcat.getString("prodcat_id");
                                    String cat_name = jsonObjectcat.getString("prodcat_name");

                                    FilterListClass filterListClasscat = new FilterListClass();
                                    filterListClasscat.setCatId(cat_id);
                                    filterListClasscat.setCatName(cat_name);
                                    filterListClasscat.setType(4);
                                    filterListClassesCategory.add(filterListClasscat);
                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                recyclerViewchipfilter.setLayoutManager(linearLayoutManager);
                                CategoriesFilterAdapter filtersAdapter = new CategoriesFilterAdapter(ShowAlleBikesActivity.this, filterListClassesCategory,null,"bikes");
                                recyclerViewchipfilter.setAdapter(filtersAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressFilter.setVisibility(GONE);
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
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("category", categoryid);
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(ShowAlleBikesActivity.this).addToRequestQueue(strRequest);

    }

    private void getCollectionProductsBrands(String collectionidbrands) {
        allebikelist.clear();
        progressBar.setVisibility(View.VISIBLE);
        String Login_url = Apis.collectionView;
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
                                JSONObject jsonObjectCollection = jsonObjectData.getJSONObject("collection");
                                String collectionid = jsonObjectCollection.getString("collection_id");

                                String collectionlayouttype = jsonObjectCollection.getString("collection_layout_type");
                                //brands
                                JSONObject jsonobjectbrands = jsonObjectData.getJSONObject("collectionItems");
                                JSONObject jsonObjectvallayouttype = jsonobjectbrands.getJSONObject(collectionlayouttype);
                                JSONObject jsonObjectvalcolid = jsonObjectvallayouttype.getJSONObject(collectionid);
                                JSONArray jsonArraybrands = jsonObjectvalcolid.getJSONArray("brands");
                                for (int l = 0; l < jsonArraybrands.length(); l++) {
                                    JSONObject jsonObjectbrand = jsonArraybrands.getJSONObject(l);
                                    String brandid = jsonObjectbrand.getString("brand_id");
                                    String brandname = jsonObjectbrand.getString("brand_name");
                                    String branddescript = jsonObjectbrand.getString("brand_short_description");
                                    String brandimage = jsonObjectbrand.getString("brand_image");
                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setProductName(brandname);
                                    allebikesModelClass.setPrice(branddescript);
                                    allebikesModelClass.setProductid(brandid);
                                    allebikesModelClass.setNetworkImage(brandimage);
                                    allebikesModelClass.setTypeLayout(2);
                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(GONE);

                                } else {
                                    linearLayoutEmpty.setVisibility(GONE);
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    // GridLayoutManager gridLayoutManager = new GridLayoutManager(ShowAlleBikesActivity.this, 2);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(GONE);

                                }


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
                data3.put("collection_id", collectionidbrands);
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }

    private void getCollectionProductsCategories(String collectionidcat) {
        allebikelist.clear();
        progressBar.setVisibility(View.VISIBLE);
        String Login_url = Apis.collectionView;
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
                                JSONObject jsonObjectCollection = jsonObjectData.getJSONObject("collection");
                                String collectionid = jsonObjectCollection.getString("collection_id");

                                String collectionlayouttype = jsonObjectCollection.getString("collection_layout_type");
                                //Categories
                                JSONArray jsonArray = jsonObjectData.getJSONArray("collectionItems");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonCat = jsonArray.getJSONObject(j);
                                    String prodcatid = jsonCat.getString("prodcat_id");
                                    String prodcatname = jsonCat.getString("prodcat_name");
                                    String prodcatblocks = jsonCat.getString("prodcat_content_block");
                                    String prodcatcounts = jsonCat.getString("productCounts");
                                    String prodcatimage = jsonCat.getString("image");
                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setProductName(prodcatname);
                                    allebikesModelClass.setProductid(prodcatid);
                                    allebikesModelClass.setNetworkImage(prodcatimage);
                                    allebikesModelClass.setTypeLayout(1);
                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(GONE);

                                } else {
                                    linearLayoutEmpty.setVisibility(GONE);
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    GridLayoutManager linearLayoutManager = new GridLayoutManager(ShowAlleBikesActivity.this, 2);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(GONE);

                                }


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
                data3.put("collection_id", collectionidcat);
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    private void getCollectionProductsBikes(String collectionidbikes) {

        allebikelist.clear();
        progressBar.setVisibility(View.VISIBLE);
        String Login_url = Apis.collectionView;
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
                                JSONObject jsonObjectCollection = jsonObjectData.getJSONObject("collection");
                                String collectionid = jsonObjectCollection.getString("collection_id");

                                String collectionlayouttype = jsonObjectCollection.getString("collection_layout_type");
                                //popular bikes
                                JSONArray jsonArray = jsonObjectData.getJSONArray("collectionItems");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonCollection = jsonArray.getJSONObject(i);
                                    String productPrice = jsonCollection.getString("theprice");
                                    String productName = jsonCollection.getString("product_name");
                                    //String productName = jsonCollection.getString("selprod_title");
                                    String selproductid = jsonCollection.getString("selprod_id");
                                    String productImageurl = jsonCollection.getString("product_image_url");
                                    String testdriveenable = jsonCollection.getString("selprod_test_drive_enable");
                                    String issell = jsonCollection.getString("is_sell");
                                    String isrent = jsonCollection.getString("is_rent");
                                    String instock = jsonCollection.getString("in_stock");
                                    String book = jsonCollection.getString("selprod_book_now_enable");
                                    String prodbook = jsonCollection.getString("product_book");
                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setBooknow(book);
                                    allebikesModelClass.setProductbook(prodbook);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setInstock(instock);
                                    allebikesModelClass.setTypeLayout(0);

                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(GONE);

                                } else {
                                    linearLayoutEmpty.setVisibility(GONE);
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(GONE);

                                }


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
                data3.put("collection_id", collectionidbikes);
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    private void getPricerange(String categoryid, String shopid, String brandid) {
        progressFilter.setVisibility(View.VISIBLE);
        String Login_url = Apis.filters;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1"))
                                progressFilter.setVisibility(GONE);
                            {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                String minvalue = jsonObjectData.getString("filterDefaultMinValue");
                                String maxvalue = jsonObjectData.getString("filterDefaultMaxValue");
                                if (!minvalue.isEmpty() && !maxvalue.isEmpty()) {
                                    minprice.setText(String.valueOf(minvalue));
                                    maxprice.setText(String.valueOf(maxvalue));
                                    bubbleThumbRangeSeekbar.setMinValue(Float.parseFloat(minvalue));
                                    bubbleThumbRangeSeekbar.setMaxValue(Float.parseFloat(maxvalue));
                                    bubbleThumbRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                                        @Override
                                        public void valueChanged(Number minValue, Number maxValue) {
                                            minprice.setText(String.valueOf(minValue));
                                            maxprice.setText(String.valueOf(maxValue));
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressFilter.setVisibility(GONE);
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
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("category", categoryid);
                data3.put("shop_id", shopid);
                data3.put("brand_id", brandid);

                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(ShowAlleBikesActivity.this).addToRequestQueue(strRequest);

    }

    private void getAllebikes(String priceStr) {
        allebikelist.clear();
        progressBar.setVisibility(View.VISIBLE);
        String Login_url = Apis.filteredproducts;
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
                                JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                                    String productPrice = jsonObjectProducts.getString("theprice");
                                    //String productName = jsonObjectProducts.getString("product_name");
                                    String productName = jsonObjectProducts.getString("selprod_title");
                                    String selproductid = jsonObjectProducts.getString("selprod_id");
                                    String productImageurl = jsonObjectProducts.getString("product_image_url");
                                    String testdriveenable = jsonObjectProducts.getString("selprod_test_drive_enable");
                                    String issell = jsonObjectProducts.getString("is_sell");
                                    String isrent = jsonObjectProducts.getString("is_rent");
                                    String instock = jsonObjectProducts.getString("in_stock");
                                    String book = jsonObjectProducts.getString("selprod_book_now_enable");
                                    String prodbook = jsonObjectProducts.getString("product_book");

                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setBooknow(book);
                                    allebikesModelClass.setProductbook(prodbook);
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setInstock(instock);
                                    allebikesModelClass.setTypeLayout(0);

                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(GONE);

                                } else {
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(GONE);

                                }
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
                if (priceStr.isEmpty() || priceStr.equals(null) || priceStr == null) {
                    data3.put("keyword", "");
                } else {
                    data3.put("sortBy", priceStr);
                }
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }

    private void getOnlyRentProducts(String rent, String testdrive, String sorting) {
        allebikelist.clear();
        progressBar.setVisibility(View.VISIBLE);

        String Login_url = Apis.filteredproducts;
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
                                JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                                    String productPrice = jsonObjectProducts.getString("theprice");
                                    //String productName = jsonObjectProducts.getString("product_name");
                                    String productName = jsonObjectProducts.getString("selprod_title");
                                    String selproductid = jsonObjectProducts.getString("selprod_id");
                                    String productImageurl = jsonObjectProducts.getString("product_image_url");
                                    String testdriveenable = jsonObjectProducts.getString("selprod_test_drive_enable");
                                    String issell = jsonObjectProducts.getString("is_sell");
                                    String isrent = jsonObjectProducts.getString("is_rent");
                                    String instock = jsonObjectProducts.getString("in_stock");
                                    String book = jsonObjectProducts.getString("selprod_book_now_enable");
                                    String prodbook = jsonObjectProducts.getString("product_book");

                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setBooknow(book);
                                    allebikesModelClass.setProductbook(prodbook);
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setInstock(instock);
                                    allebikesModelClass.setTypeLayout(0);

                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(GONE);
                                } else {
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(GONE);
                                }
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
                ArrayList<String> strings = new ArrayList<>();
                strings.add(rent);
                Map<String, String> data3 = new HashMap<String, String>();
                if (testdrive.isEmpty()) {
                    data3.put("producttype", strings.toString());
                    data3.put("sortBy", sorting);
                } else {
                    data3.put("producttype", strings.toString());
                    data3.put("tdrive", testdrive);
                    data3.put("sortBy", sorting);
                }

                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    private void getAllShops() {
        progressBar.setVisibility(View.VISIBLE);
        allebikesModelClassesList.clear();
        String url_link = Apis.getallshops;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = jsonObjectData.getJSONArray("allShops");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject address = jsonArray.getJSONObject(i);
                        String shopid = address.getString("shop_id");
                        String shopuserid = address.getString("shop_user_id");
                        String shopname = address.getString("shop_name");
                        String shopcity = address.getString("shop_city");
                        String shopstate = address.getString("state_name");
                        String shopcontry = address.getString("country_name");
                        String shopphoneno = address.getString("shop_phone");
                        String shoppincode = address.getString("shop_postalcode");
                        String autocompleteaddres = address.getString("shop_auto_complete");
                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                        allebikesModelClass.setProductName(shopname);
                        allebikesModelClass.setProductid(shopid);
                        allebikesModelClass.setShopphone(shopphoneno);
                        //allebikesModelClass.setPrice(shopcity + " " + shopstate + " " + shopcontry + " - " + shoppincode);
                        allebikesModelClass.setPrice(autocompleteaddres);
                        allebikesModelClass.setTypeLayout(4);
                        allebikesModelClassesList.add(allebikesModelClass);
                    }

                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikesModelClassesList);
                    recyclerView.setAdapter(allebikesAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    allebikesAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
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
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    private void getShopProducts(String shopid, String protype, String testing, String sorting) {
        allebikelist.clear();
        progressBar.setVisibility(View.VISIBLE);
        String Login_url = Apis.filteredproducts;
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

                               /* JSONObject jsonObjectShop = jsonObjectData.getJSONObject("shop");
                                String shopname = jsonObjectShop.getString("shop_name");
                                String shoplogo = jsonObjectShop.getString("shop_logo");
                                String shopbanner = jsonObjectShop.getString("shop_banner");
                                bannerImage.setVisibility(View.VISIBLE);
                                ImageLoader imageLoader = VolleySingleton.getInstance(ShowAlleBikesActivity.this)
                                        .getImageLoader();
                                imageLoader.get(shopbanner, ImageLoader.getImageListener(bannerImage, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                                bannerImage.setImageUrl(shopbanner, imageLoader);*/
                                JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                                    String productPrice = jsonObjectProducts.getString("theprice");
                                    //String productName = jsonObjectProducts.getString("product_name");
                                    String productName = jsonObjectProducts.getString("selprod_title");
                                    String selproductid = jsonObjectProducts.getString("selprod_id");
                                    String productImageurl = jsonObjectProducts.getString("product_image_url");
                                    String testdriveenable = jsonObjectProducts.getString("selprod_test_drive_enable");
                                    String issell = jsonObjectProducts.getString("is_sell");
                                    String isrent = jsonObjectProducts.getString("is_rent");
                                    String instock = jsonObjectProducts.getString("in_stock");
                                    String book = jsonObjectProducts.getString("selprod_book_now_enable");
                                    String prodbook = jsonObjectProducts.getString("product_book");
                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setBooknow(book);
                                    allebikesModelClass.setProductbook(prodbook);
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setTypeLayout(0);
                                    allebikesModelClass.setInstock(instock);
                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    textView_empty.setText("Bikes not Available");
                                    progressBar.setVisibility(GONE);
                                    phonenoshop.setVisibility(GONE);
                                } else {

                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    if (shopphone.equals("0") || shopphone.equals(null)) {
                                        phonenoshop.setVisibility(GONE);
                                    } else {
                                        phonenoshop.setVisibility(View.VISIBLE);
                                    }

                                    textView_empty.setText(shopname + "\n" + shopaddress);
                                    phonenoshop.setText("Phone : " + shopphone);

                                    if (shopbanner != null) {
                                        bannerImage.setVisibility(View.VISIBLE);
                                        ImageLoader imageLoader = VolleySingleton.getInstance(ShowAlleBikesActivity.this)
                                                .getImageLoader();
                                        imageLoader.get(shopbanner, ImageLoader.getImageListener(bannerImage, 0, android.R.drawable.ic_dialog_alert));
                                        bannerImage.setImageUrl(shopbanner, imageLoader);
                                    }
                                   /* if (shoplogo != null) {
                                        imageLoader = VolleySingleton.getInstance(ShowAlleBikesActivity.this).getImageLoader();
                                        imageLoader.get(shoplogo, ImageLoader.getImageListener(networkImageView, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                                        networkImageView.setImageUrl(shoplogo, imageLoader);
                                    }*/
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(GONE);

                                }
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
                if (testing.isEmpty()) {
                    data3.put("shop_id", shopid);
                    data3.put("producttype", protype);
                    data3.put("sortBy", sorting);
                } else {
                    data3.put("shop_id", shopid);
                    data3.put("producttype", protype);
                    data3.put("tdrive", testing);
                    data3.put("sortBy", sorting);
                    data3.put("book", testing);
                }

                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }


    private void getShopProductsBanner(String shopid) {
        allebikelist.clear();
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
                                String shopname = jsonObjectShop.getString("shop_name");
                                String shoplogo = jsonObjectShop.getString("shop_logo");
                                String shopbanner = jsonObjectShop.getString("shop_banner");
                                bannerImage.setVisibility(View.VISIBLE);
                                ImageLoader imageLoader = VolleySingleton.getInstance(ShowAlleBikesActivity.this)
                                        .getImageLoader();
                                imageLoader.get(shopbanner, ImageLoader.getImageListener(bannerImage, 0, android.R.drawable.ic_dialog_alert));
                                bannerImage.setImageUrl(shopbanner, imageLoader);

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


    public void getcategoryProducts(String categoryid, String prodType, String soryby, String tdrive) {
        progressBar.setVisibility(View.VISIBLE);
        allebikelist.clear();
        String Login_url = Apis.filteredproducts;
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
                                JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                                    String productPrice = jsonObjectProducts.getString("theprice");
                                    //String productName = jsonObjectProducts.getString("product_name");
                                    String productName = jsonObjectProducts.getString("selprod_title");
                                    String selproductid = jsonObjectProducts.getString("selprod_id");
                                    String productImageurl = jsonObjectProducts.getString("product_image_url");
                                    String testdriveenable = jsonObjectProducts.getString("selprod_test_drive_enable");
                                    String issell = jsonObjectProducts.getString("is_sell");
                                    String isrent = jsonObjectProducts.getString("is_rent");
                                    String instock = jsonObjectProducts.getString("in_stock");
                                    String book = jsonObjectProducts.getString("selprod_book_now_enable");
                                    String prodbook = jsonObjectProducts.getString("product_book");

                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setBooknow(book);
                                    allebikesModelClass.setProductbook(prodbook);
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setInstock(instock);
                                    allebikesModelClass.setTypeLayout(0);

                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(GONE);

                                } else {
                                    linearLayoutEmpty.setVisibility(GONE);
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(GONE);

                                }
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
                ArrayList<String> strings = new ArrayList<>();
                strings.add(categoryid);
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("prodcat", strings.toString());
                data3.put("sortBy", soryby);
                data3.put("producttype", prodType);
                if (tdrive.equals("book")) {
                    data3.put("book", tdrive);
                } else {
                    data3.put("tdrive", tdrive);
                }
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }

    private void getfilterProducts(String brandid, String proType, String testing, String sorting) {
        allebikelist.clear();
        progressBar.setVisibility(View.VISIBLE);

        String Login_url = Apis.filteredproducts;
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
                                if (brandid != null) {
                                    String banner = jsonObjectData.getString("brand_banner");
                                    if (banner.isEmpty()) {
                                    } else {
                                        bannerImage.setVisibility(View.VISIBLE);
                                        ImageLoader imageLoader = VolleySingleton.getInstance(ShowAlleBikesActivity.this)
                                                .getImageLoader();
                                        imageLoader.get("https://ewheelers.in" + banner, ImageLoader.getImageListener(bannerImage, 0, android.R.drawable.ic_dialog_alert));
                                        bannerImage.setImageUrl("https://ewheelers.in" + banner, imageLoader);
                                    }
                                }
                                JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                                    String productPrice = jsonObjectProducts.getString("theprice");
                                    //String productName = jsonObjectProducts.getString("product_name");
                                    String productName = jsonObjectProducts.getString("selprod_title");
                                    String selproductid = jsonObjectProducts.getString("selprod_id");
                                    String productImageurl = jsonObjectProducts.getString("product_image_url");
                                    String testdriveenable = jsonObjectProducts.getString("selprod_test_drive_enable");
                                    String issell = jsonObjectProducts.getString("is_sell");
                                    String isrent = jsonObjectProducts.getString("is_rent");
                                    String instock = jsonObjectProducts.getString("in_stock");
                                    String book = jsonObjectProducts.getString("selprod_book_now_enable");
                                    String prodbook = jsonObjectProducts.getString("product_book");
                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setBooknow(book);
                                    allebikesModelClass.setProductbook(prodbook);
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setInstock(instock);
                                    allebikesModelClass.setTypeLayout(0);

                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(GONE);

                                } else {
                                    linearLayoutEmpty.setVisibility(GONE);
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(GONE);

                                }

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
                ArrayList<String> strings = new ArrayList<>();
                strings.add(brandid);
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("brand", strings.toString());
                data3.put("producttype", proType);
                data3.put("sortBy", sorting);
                if (testing.equals("book")) {
                    data3.put("book", testing);
                } else {
                    data3.put("tdrive", testing);
                }
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }

    public void getCollectionProducts(String collectionidfrom) {
        allebikelist.clear();
        progressBar.setVisibility(View.VISIBLE);
        String Login_url = Apis.collectionView;
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
                                JSONObject jsonObjectCollection = jsonObjectData.getJSONObject("collection");
                                String collectionid = jsonObjectCollection.getString("collection_id");

                                String collectionlayouttype = jsonObjectCollection.getString("collection_layout_type");
                                if (collectionid.equals(collectionidfrom)) { //popular bikes
                                    JSONArray jsonArray = jsonObjectData.getJSONArray("collectionItems");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonCollection = jsonArray.getJSONObject(i);
                                        String productPrice = jsonCollection.getString("theprice");
                                        String productName = jsonCollection.getString("product_name");
                                        //String productName = jsonCollection.getString("selprod_title");
                                        String selproductid = jsonCollection.getString("selprod_id");
                                        String productImageurl = jsonCollection.getString("product_image_url");
                                        String testdriveenable = jsonCollection.getString("selprod_test_drive_enable");
                                        String issell = jsonCollection.getString("is_sell");
                                        String isrent = jsonCollection.getString("is_rent");
                                        String instock = jsonCollection.getString("in_stock");
                                        String book = jsonCollection.getString("selprod_book_now_enable");
                                        String prodbook = jsonCollection.getString("product_book");
                                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                        allebikesModelClass.setPrice(productPrice);
                                        allebikesModelClass.setBooknow(book);
                                        allebikesModelClass.setProductbook(prodbook);
                                        allebikesModelClass.setProductName(productName);
                                        allebikesModelClass.setProductid(selproductid);
                                        allebikesModelClass.setNetworkImage(productImageurl);
                                        allebikesModelClass.setTestdriveenable(testdriveenable);
                                        allebikesModelClass.setIssell(issell);
                                        allebikesModelClass.setIsrent(isrent);
                                        allebikesModelClass.setInstock(instock);
                                        allebikesModelClass.setTypeLayout(0);

                                        allebikelist.add(allebikesModelClass);
                                    }
                                    if (allebikelist.isEmpty()) {
                                        linearLayoutEmpty.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(GONE);

                                    } else {
                                        linearLayoutEmpty.setVisibility(GONE);
                                        allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                        recyclerView.setAdapter(allebikesAdapter);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                        recyclerView.setLayoutManager(linearLayoutManager);
                                        allebikesAdapter.notifyDataSetChanged();
                                        progressBar.setVisibility(GONE);

                                    }
                                } else if (collectionid.equals(collectionidfrom)) { //categories
                                    JSONArray jsonArray = jsonObjectData.getJSONArray("collectionItems");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonCat = jsonArray.getJSONObject(j);
                                        String prodcatid = jsonCat.getString("prodcat_id");
                                        String prodcatname = jsonCat.getString("prodcat_name");
                                        String prodcatblocks = jsonCat.getString("prodcat_content_block");
                                        String prodcatcounts = jsonCat.getString("productCounts");
                                        String prodcatimage = jsonCat.getString("image");
                                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                        allebikesModelClass.setProductName(prodcatname);
                                        allebikesModelClass.setProductid(prodcatid);
                                        allebikesModelClass.setNetworkImage(prodcatimage);
                                        allebikesModelClass.setTypeLayout(1);
                                        allebikelist.add(allebikesModelClass);
                                    }
                                    if (allebikelist.isEmpty()) {
                                        linearLayoutEmpty.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(GONE);

                                    } else {
                                        linearLayoutEmpty.setVisibility(GONE);
                                        allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                        recyclerView.setAdapter(allebikesAdapter);
                                        GridLayoutManager linearLayoutManager = new GridLayoutManager(ShowAlleBikesActivity.this, 2);
                                        recyclerView.setLayoutManager(linearLayoutManager);
                                        allebikesAdapter.notifyDataSetChanged();
                                        progressBar.setVisibility(GONE);

                                    }
                                } else if (collectionid.equals(collectionidfrom)) { //dealers
                                    JSONObject jsonObjectItems = jsonObjectData.getJSONObject("collectionItems");
                                    Iterator iter = jsonObjectItems.keys();
                                    while (iter.hasNext()) {
                                        String key = (String) iter.next();
                                        // Log.e("keys",key);
                                        JSONObject jsonObjectShops = jsonObjectItems.getJSONObject(key);
                                        String shopid = jsonObjectShops.getString("shop_id");
                                        String shopName = jsonObjectShops.getString("shop_name");
                                        String cityName = jsonObjectShops.getString("shop_city");
                                        String shopStateName = jsonObjectShops.getString("state_name");
                                        String shopCountryName = jsonObjectShops.getString("country_name");
                                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                        allebikesModelClass.setProductName(shopName);
                                        allebikesModelClass.setProductid(shopid);
                                        allebikesModelClass.setPrice(cityName + " " + shopStateName + " " + shopCountryName);
                                        allebikesModelClass.setTypeLayout(3);
                                        allebikelist.add(allebikesModelClass);
                                    }
                                    if (allebikelist.isEmpty()) {
                                        linearLayoutEmpty.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(GONE);

                                    } else {
                                        linearLayoutEmpty.setVisibility(GONE);
                                        allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                        recyclerView.setAdapter(allebikesAdapter);
                                        GridLayoutManager linearLayoutManager = new GridLayoutManager(ShowAlleBikesActivity.this, 2);
                                        recyclerView.setLayoutManager(linearLayoutManager);
                                        allebikesAdapter.notifyDataSetChanged();
                                        progressBar.setVisibility(GONE);

                                    }
                                } else if (collectionid.equals(collectionidfrom)) { // brands
                                    // Toast.makeText(ShowAlleBikesActivity.this, "layout: "+collectionlayouttype+"\nid "+collectionid, Toast.LENGTH_SHORT).show();
                                    JSONObject jsonobjectbrands = jsonObjectData.getJSONObject("collectionItems");
                                    JSONObject jsonObjectvallayouttype = jsonobjectbrands.getJSONObject(collectionlayouttype);
                                    JSONObject jsonObjectvalcolid = jsonObjectvallayouttype.getJSONObject(collectionid);
                                    JSONArray jsonArraybrands = jsonObjectvalcolid.getJSONArray("brands");
                                    for (int l = 0; l < jsonArraybrands.length(); l++) {
                                        JSONObject jsonObjectbrand = jsonArraybrands.getJSONObject(l);
                                        String brandid = jsonObjectbrand.getString("brand_id");
                                        String brandname = jsonObjectbrand.getString("brand_name");
                                        String branddescript = jsonObjectbrand.getString("brand_short_description");
                                        String brandimage = jsonObjectbrand.getString("brand_image");
                                        AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                        allebikesModelClass.setProductName(brandname);
                                        allebikesModelClass.setPrice(branddescript);
                                        allebikesModelClass.setProductid(brandid);
                                        allebikesModelClass.setNetworkImage(brandimage);
                                        allebikesModelClass.setTypeLayout(2);
                                        allebikelist.add(allebikesModelClass);
                                    }
                                    if (allebikelist.isEmpty()) {
                                        linearLayoutEmpty.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(GONE);

                                    } else {
                                        linearLayoutEmpty.setVisibility(GONE);
                                        allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                        recyclerView.setAdapter(allebikesAdapter);
                                        // GridLayoutManager gridLayoutManager = new GridLayoutManager(ShowAlleBikesActivity.this, 2);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                        recyclerView.setLayoutManager(linearLayoutManager);
                                        allebikesAdapter.notifyDataSetChanged();
                                        progressBar.setVisibility(GONE);

                                    }
                                }

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
                data3.put("collection_id", collectionidfrom);
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    public void applyFilters(String catarray, String brands, String optionids, String shopids) {
        categoriesarray = catarray;
        brandarray = brands;
        optionarray = optionids;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_action_button:
                nestedScrollView.fling(0);
                nestedScrollView.smoothScrollTo(0, 0);
                break;
            case R.id.banner:
                Intent i = new Intent(getApplicationContext(), BuyerGuideActivity.class);
                i.putExtra("bannerurl", contentOfBrackets);
                startActivity(i);
                break;
            case R.id.sortItems:
                mBottomSheetDialog.show();
                break;
            case R.id.filterItems:
                dialog.show();
                break;
            case R.id.closeIcon:
                dialog.dismiss();
                break;
            case R.id.phoneno_shop:
                if (isPermissionGranted()) {
                    call_action(shopphone);
                }
                break;
            case R.id.apply:
                dialog.dismiss();
                applyMethodFilters(categoriesarray, brandarray, Integer.parseInt(minprice.getText().toString()), Integer.parseInt(maxprice.getText().toString()), optionarray);
                break;
        }
    }

    public void applyMethodFilters(String categoriesarray, String brandarray, int minprice, int maxprice, String optionvalues) {
        chipall.setChecked(false);
        progressBar.setVisibility(View.VISIBLE);
        allebikelist.clear();
        String Login_url = Apis.filteredproducts;
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
                                JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                                    String productPrice = jsonObjectProducts.getString("theprice");
                                    //String productName = jsonObjectProducts.getString("product_name");
                                    String productName = jsonObjectProducts.getString("selprod_title");
                                    String selproductid = jsonObjectProducts.getString("selprod_id");
                                    String productImageurl = jsonObjectProducts.getString("product_image_url");
                                    String testdriveenable = jsonObjectProducts.getString("selprod_test_drive_enable");
                                    String issell = jsonObjectProducts.getString("is_sell");
                                    String isrent = jsonObjectProducts.getString("is_rent");
                                    String instock = jsonObjectProducts.getString("in_stock");
                                    String book = jsonObjectProducts.getString("selprod_book_now_enable");
                                    String prodbook = jsonObjectProducts.getString("product_book");

                                    AllebikesModelClass allebikesModelClass = new AllebikesModelClass();
                                    allebikesModelClass.setBooknow(book);
                                    allebikesModelClass.setProductbook(prodbook);
                                    allebikesModelClass.setPrice(productPrice);
                                    allebikesModelClass.setProductName(productName);
                                    allebikesModelClass.setProductid(selproductid);
                                    allebikesModelClass.setNetworkImage(productImageurl);
                                    allebikesModelClass.setTestdriveenable(testdriveenable);
                                    allebikesModelClass.setIssell(issell);
                                    allebikesModelClass.setIsrent(isrent);
                                    allebikesModelClass.setInstock(instock);
                                    allebikesModelClass.setTypeLayout(0);

                                    allebikelist.add(allebikesModelClass);
                                }
                                if (allebikelist.isEmpty()) {
                                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(GONE);

                                } else {
                                    linearLayoutEmpty.setVisibility(GONE);
                                    allebikesAdapter = new AllebikesAdapter(ShowAlleBikesActivity.this, allebikelist);
                                    recyclerView.setAdapter(allebikesAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    allebikesAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(GONE);

                                }
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
                if (categoriesarray == null && brandarray == null) {
                    data3.put("prodcat", "");
                    data3.put("brand", "");
                } else {
                    data3.put("prodcat", categoriesarray);
                    data3.put("brand", brandarray);
                    data3.put("price-min-range", String.valueOf(minprice));
                    data3.put("price-max-range", String.valueOf(maxprice));
                    data3.put("currency_id", "1");
                    data3.put("optionvalue", optionvalues);
                }
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);

    }


    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions((this), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
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


    public void getFiltersList(String categoryid, String shopid, String brandid) {
        progressFilter.setVisibility(View.VISIBLE);
        filterListClasses.clear();
        filterListClasses2.clear();
        String Login_url = Apis.filters;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1"))
                                progressFilter.setVisibility(GONE);
                            {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONArray jsonArray = jsonObjectData.getJSONArray("categoriesArr");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectcat = jsonArray.getJSONObject(i);
                                    String cat_id = jsonObjectcat.getString("prodcat_id");
                                    String cat_name = jsonObjectcat.getString("prodcat_name");
                                    List<SubFilterModelClass> subFilterModelClasses = new ArrayList<>();
                                    JSONArray jsonArrayChildren = jsonObjectcat.getJSONArray("children");
                                    for (int x = 0; x < jsonArrayChildren.length(); x++) {
                                        JSONObject jsonObject1 = jsonArrayChildren.getJSONObject(x);
                                        String name = jsonObject1.getString("prodcat_name");
                                        String subcatid = jsonObject1.getString("prodcat_id");
                                        SubFilterModelClass filterListClasssub = new SubFilterModelClass();
                                        filterListClasssub.setSubcatid(subcatid);
                                        filterListClasssub.setSubname(name);
                                        filterListClasssub.setTypeOf(0);
                                        subFilterModelClasses.add(filterListClasssub);
                                    }

                                    FilterListClass filterListClass = new FilterListClass();
                                    filterListClass.setCatId(cat_id);
                                    filterListClass.setCatName(cat_name);
                                    filterListClass.setSubFilterModelClasses(subFilterModelClasses);
                                    filterListClass.setType(0);
                                    filterListClasses.add(filterListClass);

                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, LinearLayoutManager.VERTICAL, false);
                                recyclrecat.setLayoutManager(linearLayoutManager);
                                FiltersAdapter filtersAdapter = new FiltersAdapter(ShowAlleBikesActivity.this, filterListClasses);
                                recyclrecat.setAdapter(filtersAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressFilter.setVisibility(GONE);
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
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("category", categoryid);
                data3.put("shop_id", shopid);
                data3.put("brand_id", brandid);
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(ShowAlleBikesActivity.this).addToRequestQueue(strRequest);

    }

    public void getBrandFilterList(String categoryid, String shopid, String brandid) {
        progressFilter.setVisibility(View.VISIBLE);
        filterListClasses.clear();
        String Login_url = Apis.filters;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1"))
                                progressFilter.setVisibility(GONE);
                            {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONArray jsonArraybrands = jsonObjectData.getJSONArray("brandsArr");
                                for (int i = 0; i < jsonArraybrands.length(); i++) {
                                    JSONObject jsonObject1 = jsonArraybrands.getJSONObject(i);
                                    String branid = jsonObject1.getString("brand_id");
                                    String branname = jsonObject1.getString("brand_name");
                                    FilterListClass filterListClass = new FilterListClass();
                                    filterListClass.setBrandid(branid);
                                    filterListClass.setBrandname(branname);
                                    filterListClass.setType(1);
                                    filterListClasses.add(filterListClass);
                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, LinearLayoutManager.VERTICAL, false);
                                recyclerbrand.setLayoutManager(linearLayoutManager);
                                FiltersAdapter filtersAdapterbrand = new FiltersAdapter(ShowAlleBikesActivity.this, filterListClasses, null);
                                recyclerbrand.setAdapter(filtersAdapterbrand);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressFilter.setVisibility(GONE);
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
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("category", categoryid);
                data3.put("shop_id", shopid);
                data3.put("brand_id", brandid);

                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(ShowAlleBikesActivity.this).addToRequestQueue(strRequest);

    }

    public void getOptionsFilterList(String categoryid, String shopid, String brandid) {
        filterListClassesidBased.clear();
        String Login_url = Apis.filters;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1"))
                            //   progressFilter.setVisibility(GONE);
                            {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                String shopcatfilters = jsonObjectData.getString("shopCatFilters");
                                JSONArray prodcatarray = jsonObjectData.getJSONArray("prodcatArr");

                                JSONArray jsonArraybrandcheked = jsonObjectData.getJSONArray("brandsCheckedArr");
                                JSONArray jsonArrayoptionvaluecheked = jsonObjectData.getJSONArray("optionValueCheckedArr");
                                JSONArray jsonArrayconditons = jsonObjectData.getJSONArray("conditionsArr");
                                for (int c = 0; c < jsonArrayconditons.length(); c++) {
                                    JSONObject jsonObjectcond = jsonArrayconditons.getJSONObject(c);
                                    String title = jsonObjectcond.getString("title");
                                    String value = jsonObjectcond.getString("value");

                                }

                                JSONArray jsonArraycondchecked = jsonObjectData.getJSONArray("conditionsCheckedArr");


                                JSONArray jsonArrayoptions = jsonObjectData.getJSONArray("options");
                                for (int x = 0; x < jsonArrayoptions.length(); x++) {
                                    JSONObject jsonObjectopt = jsonArrayoptions.getJSONObject(x);
                                    String optionid = jsonObjectopt.getString("option_id");
                                    String optioniscolor = jsonObjectopt.getString("option_is_color");
                                    String optionname = jsonObjectopt.getString("option_name");
                                    List<SubFilterModelClass> subFilterModelClasses = new ArrayList<>();
                                    JSONArray jsonArrayvalues = jsonObjectopt.getJSONArray("values");
                                    for (int t = 0; t < jsonArrayvalues.length(); t++) {
                                        JSONObject jsonObjectofvaluesarray = jsonArrayvalues.getJSONObject(t);
                                        String optionvaluename = jsonObjectofvaluesarray.getString("optionvalue_name");
                                        String optionvalueid = jsonObjectofvaluesarray.getString("optionvalue_id");
                                        String optionvaluecolorcode = jsonObjectofvaluesarray.getString("optionvalue_color_code");
                                        SubFilterModelClass subFilterModelClass = new SubFilterModelClass();
                                        subFilterModelClass.setOptionvalue_id(optionvalueid);
                                        subFilterModelClass.setOptionvalue_name(optionvaluename);
                                        subFilterModelClass.setOptionvalue_color_code(optionvaluecolorcode);
                                        subFilterModelClass.setTypeOf(1);
                                        subFilterModelClasses.add(subFilterModelClass);
                                    }

                                    FilterListClass filterListClass = new FilterListClass();
                                    filterListClass.setSubFilterOptionsModelClasses(subFilterModelClasses);
                                    filterListClass.setType(2);
                                    filterListClass.setOptionid(optionid);
                                    filterListClass.setOptionname(optionname);
                                    filterListClass.setOptioniscolor(optioniscolor);
                                    filterListClassesidBased.add(filterListClass);

                                }

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, LinearLayoutManager.VERTICAL, false);
                                recycleoptions.setLayoutManager(linearLayoutManager);
                                FiltersAdapter filtersAdapter = new FiltersAdapter(ShowAlleBikesActivity.this, filterListClassesidBased);
                                recycleoptions.setAdapter(filtersAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressFilter.setVisibility(GONE);
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
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("category", categoryid);
                data3.put("shop_id", shopid);
                data3.put("brand_id", brandid);

                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(ShowAlleBikesActivity.this).addToRequestQueue(strRequest);

    }


    public void removeCatFilters(String empty) {
        getcategoryProducts(categoryid, "", "", "");
        chipall.setChecked(true);
        chipall.setTextColor(Color.parseColor("#9C3C34"));
    }

    public void getIdbasedFiltersList(String categoryid, String shopid, String brandid) {
        // progressFilter.setVisibility(View.VISIBLE);
        filterListClassesidBased.clear();
        String Login_url = Apis.filters;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1"))
                            //   progressFilter.setVisibility(GONE);
                            {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                String shopcatfilters = jsonObjectData.getString("shopCatFilters");
                                JSONArray prodcatarray = jsonObjectData.getJSONArray("prodcatArr");

                                JSONArray jsonArraybrandcheked = jsonObjectData.getJSONArray("brandsCheckedArr");
                                JSONArray jsonArrayoptionvaluecheked = jsonObjectData.getJSONArray("optionValueCheckedArr");

                                String minvalue = jsonObjectData.getString("filterDefaultMinValue");
                                String maxvalue = jsonObjectData.getString("filterDefaultMaxValue");
                                if (!minvalue.isEmpty() && !maxvalue.isEmpty()) {
                                    minprice.setText(String.valueOf(minvalue));
                                    maxprice.setText(String.valueOf(maxvalue));
                                    bubbleThumbRangeSeekbar.setMinValue(Float.parseFloat(minvalue));
                                    bubbleThumbRangeSeekbar.setMaxValue(Float.parseFloat(maxvalue));
                                    bubbleThumbRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                                        @Override
                                        public void valueChanged(Number minValue, Number maxValue) {
                                            minprice.setText(String.valueOf(minValue));
                                            maxprice.setText(String.valueOf(maxValue));
                                        }
                                    });
                                }

                                JSONArray jsonArrayconditons = jsonObjectData.getJSONArray("conditionsArr");
                                for (int c = 0; c < jsonArrayconditons.length(); c++) {
                                    JSONObject jsonObjectcond = jsonArrayconditons.getJSONObject(c);
                                    String title = jsonObjectcond.getString("title");
                                    String value = jsonObjectcond.getString("value");

                                }

                                JSONArray jsonArraycondchecked = jsonObjectData.getJSONArray("conditionsCheckedArr");


                                JSONArray jsonArray = jsonObjectData.getJSONArray("categoriesArr");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectcat = jsonArray.getJSONObject(i);
                                    String cat_id = jsonObjectcat.getString("prodcat_id");
                                    String cat_name = jsonObjectcat.getString("prodcat_name");

                                    List<SubFilterModelClass> filterListClasses2 = new ArrayList<>();

                                    JSONArray jsonArrayChildren = jsonObjectcat.getJSONArray("children");
                                    for (int x = 0; x < jsonArrayChildren.length(); x++) {
                                        JSONObject jsonObject1 = jsonArrayChildren.getJSONObject(x);
                                        String name = jsonObject1.getString("prodcat_name");
                                        String subcatid = jsonObject1.getString("prodcat_id");
                                        SubFilterModelClass filterListClasssub = new SubFilterModelClass();
                                        filterListClasssub.setSubcatid(subcatid);
                                        filterListClasssub.setSubname(name);
                                        filterListClasssub.setTypeOf(0);
                                        filterListClasses2.add(filterListClasssub);
                                    }
                                    FilterListClass filterListClass = new FilterListClass();
                                    filterListClass.setCatId(cat_id);
                                    filterListClass.setCatName(cat_name);
                                    filterListClass.setSubFilterModelClasses(filterListClasses2);
                                    filterListClass.setType(0);
                                    filterListClassesidBased.add(filterListClass);

                                }


                                JSONArray brandsarray = jsonObjectData.getJSONArray("brandsArr");
                                for (int b = 0; b < brandsarray.length(); b++) {
                                    JSONObject jsonObjectbrands = brandsarray.getJSONObject(b);
                                    String brandid = jsonObjectbrands.getString("brand_id");
                                    String brandname = jsonObjectbrands.getString("brand_name");
                                    String priority = jsonObjectbrands.getString("priority");
                                    FilterListClass filterListClass3 = new FilterListClass();
                                    filterListClass3.setBrandid(brandid);
                                    filterListClass3.setBrandname(brandname);
                                    filterListClass3.setType(1);
                                    filterListClassesidBased.add(filterListClass3);
                                }

                                JSONArray jsonArrayoptions = jsonObjectData.getJSONArray("options");
                                for (int x = 0; x < jsonArrayoptions.length(); x++) {
                                    JSONObject jsonObjectopt = jsonArrayoptions.getJSONObject(x);
                                    String optionid = jsonObjectopt.getString("option_id");
                                    String optioniscolor = jsonObjectopt.getString("option_is_color");
                                    String optionname = jsonObjectopt.getString("option_name");
                                    List<SubFilterModelClass> subFilterModelClasses = new ArrayList<>();
                                    JSONArray jsonArrayvalues = jsonObjectopt.getJSONArray("values");
                                    for (int t = 0; t < jsonArrayvalues.length(); t++) {
                                        JSONObject jsonObjectofvaluesarray = jsonArrayvalues.getJSONObject(t);
                                        String optionvaluename = jsonObjectofvaluesarray.getString("optionvalue_name");
                                        String optionvalueid = jsonObjectofvaluesarray.getString("optionvalue_id");
                                        String optionvaluecolorcode = jsonObjectofvaluesarray.getString("optionvalue_color_code");
                                        SubFilterModelClass subFilterModelClass = new SubFilterModelClass();
                                        subFilterModelClass.setOptionvalue_id(optionvalueid);
                                        subFilterModelClass.setOptionvalue_name(optionvaluename);
                                        subFilterModelClass.setOptionvalue_color_code(optionvaluecolorcode);
                                        subFilterModelClass.setTypeOf(1);
                                        subFilterModelClasses.add(subFilterModelClass);
                                    }

                                    FilterListClass filterListClass = new FilterListClass();
                                    filterListClass.setSubFilterOptionsModelClasses(subFilterModelClasses);
                                    filterListClass.setType(2);
                                    filterListClass.setOptionid(optionid);
                                    filterListClass.setOptionname(optionname);
                                    filterListClass.setOptioniscolor(optioniscolor);
                                    filterListClassesidBased.add(filterListClass);

                                }

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAlleBikesActivity.this, LinearLayoutManager.VERTICAL, false);
                                recyclrecat.setLayoutManager(linearLayoutManager);
                                FiltersAdapter filtersAdapter = new FiltersAdapter(ShowAlleBikesActivity.this, filterListClassesidBased);
                                recyclrecat.setAdapter(filtersAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressFilter.setVisibility(GONE);
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
            public Map<String, String> getParams() {

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("category", categoryid);
                data3.put("shop_id", shopid);
                data3.put("brand_id", brandid);
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(ShowAlleBikesActivity.this).addToRequestQueue(strRequest);

    }

}
