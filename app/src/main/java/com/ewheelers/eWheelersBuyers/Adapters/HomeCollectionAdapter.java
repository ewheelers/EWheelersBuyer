package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.eWheelersBuyers.BuyerGuideActivity;
import com.ewheelers.eWheelersBuyers.ModelClass.CirclePageIndicator;
import com.ewheelers.eWheelersBuyers.ModelClass.HomeCollectionProducts;
import com.ewheelers.eWheelersBuyers.ModelClass.HomeModelClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.ShowAlleBikesActivity;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.ewheelers.eWheelersBuyers.SessionStorage.tokenvalue;


public class HomeCollectionAdapter extends RecyclerView.Adapter<HomeCollectionAdapter.HomeHolder> {
    Context context;
    private List<HomeModelClass> homeCollectionProducts;
    private List<HomeCollectionProducts> homeCollectionProductsSliders;

    public HomeCollectionAdapter(Context context, List<HomeModelClass> homeCollectionProducts, List<HomeCollectionProducts> homeCollectionProductsSliders) {
        this.context = context;
        this.homeCollectionProducts = homeCollectionProducts;
        this.homeCollectionProductsSliders = homeCollectionProductsSliders;

    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_heading_layout, parent, false);
        return new HomeHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        //holder.title.setText(homeCollectionProducts.get(position).getHeadcatTitle());
        String prima = homeCollectionProducts.get(position).getPrimaryrecord();
        int prodarrlenth = homeCollectionProducts.get(position).getHomeCollectionProducts().size();
        int catarrlenth = homeCollectionProducts.get(position).getHomeCollectionProductsCategories().size();
        int shopsarrlenth = homeCollectionProducts.get(position).getHomeCollectionProductsShops().size();
        int brandsarrlenth = homeCollectionProducts.get(position).getHomeCollectionProductsBrands().size();


        holder.title.setText(homeCollectionProducts.get(position).getHeadcatTitle());


        String collType = homeCollectionProducts.get(position).getCollectiontype();

        if (position == 0) {
            holder.relativeLayout.setVisibility(View.VISIBLE);
        }
        else {
            holder.relativeLayout.setVisibility(View.GONE);
        }

        if(position==1){
            if(homeCollectionProducts.get(position).getHomeModelClassesBanners().isEmpty()){
                holder.topbanner.setVisibility(View.GONE);
            }else {
                holder.topbanner.setVisibility(View.VISIBLE);
                ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                imageLoader.get(homeCollectionProducts.get(position).getHomeModelClassesBanners().get(0).getBannerimageurl(), ImageLoader.getImageListener(holder.topbanner, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                holder.topbanner.setImageUrl(homeCollectionProducts.get(position).getHomeModelClassesBanners().get(0).getBannerimageurl(), imageLoader);
                holder.topbanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, BuyerGuideActivity.class);
                        i.putExtra("openurl", homeCollectionProducts.get(position).getHomeModelClassesBanners().get(0).getBannerurl());
                        context.startActivity(i);
                    }
                });
            }
        }

        if(position==2){
            holder.linearLayout.setVisibility(View.VISIBLE);
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
            imageLoader.get("https:/www.ewheelers.in/image/slide/24/3/1/MOBILE?t=1580018050", ImageLoader.getImageListener(holder.networkImageView1, R.drawable.cart, android.R.drawable.ic_dialog_alert));
            holder.networkImageView1.setImageUrl("https://www.ewheelers.in//image//slide//24//3//1//MOBILE?t=1580018050", imageLoader);

            imageLoader.get("https://www.ewheelers.in/image/slide/19/3/1/MOBILE?t=1580017670", ImageLoader.getImageListener(holder.networkImageView2, R.drawable.cart, android.R.drawable.ic_dialog_alert));
            holder.networkImageView2.setImageUrl("https://www.ewheelers.in/image/slide/19/3/1/MOBILE?t=1580017670", imageLoader);
            holder.testlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                    intent.putExtra("onlytestdrives", "1");
                    intent.putExtra("type", "Test Drives");
                    context.startActivity(intent);
                }
            });
            holder.rentlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                    intent.putExtra("onlytestdrives", "0");
                    intent.putExtra("type", "Rental Bikes");
                    context.startActivity(intent);
                }
            });
        }

        if(position==3){
            if(homeCollectionProducts.get(position).getHomeModelClassesBannersTop().isEmpty()){
                holder.topbanner.setVisibility(View.GONE);
            }else {
                holder.topbanner.setVisibility(View.VISIBLE);
                ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                imageLoader.get(homeCollectionProducts.get(position).getHomeModelClassesBannersTop().get(0).getBannerimageurl(), ImageLoader.getImageListener(holder.topbanner, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                holder.topbanner.setImageUrl(homeCollectionProducts.get(position).getHomeModelClassesBannersTop().get(0).getBannerimageurl(), imageLoader);
                holder.topbanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, BuyerGuideActivity.class);
                        i.putExtra("openurltop", homeCollectionProducts.get(position).getHomeModelClassesBannersTop().get(0).getBannerurl());
                        context.startActivity(i);
                    }
                });
            }
        }


        if(position==5){
            if(homeCollectionProducts.get(position).getHomeModelClassesBannersBottom().isEmpty()){
                holder.topbanner.setVisibility(View.GONE);
            }else {
                holder.topbanner.setVisibility(View.VISIBLE);
                ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                imageLoader.get(homeCollectionProducts.get(position).getHomeModelClassesBannersBottom().get(0).getBannerimageurl(), ImageLoader.getImageListener(holder.topbanner, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                holder.topbanner.setImageUrl(homeCollectionProducts.get(position).getHomeModelClassesBannersBottom().get(0).getBannerimageurl(), imageLoader);
                holder.topbanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, BuyerGuideActivity.class);
                        i.putExtra("openurlbottom", homeCollectionProducts.get(position).getHomeModelClassesBannersBottom().get(0).getBannerurl());
                        context.startActivity(i);
                    }
                });
            }
        }

        if (collType.equals("1")) {

            if(prodarrlenth>=Integer.parseInt(prima)){
                holder.showall.setVisibility(View.VISIBLE);
            }else {
                holder.showall.setVisibility(View.VISIBLE);
            }

            holder.progressBar.setVisibility(View.VISIBLE);
            if (homeCollectionProducts.get(position).getHomeCollectionProducts().isEmpty()) {
                holder.progressBar.setVisibility(View.GONE);
            } else {
                holder.progressBar.setVisibility(View.GONE);
                CollectionProductsAdapter collectionbannersAdapter = new CollectionProductsAdapter(context, homeCollectionProducts.get(position).getHomeCollectionProducts());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                holder.recyclerlist.setLayoutManager(linearLayoutManager);
                holder.recyclerlist.setAdapter(collectionbannersAdapter);
                collectionbannersAdapter.notifyDataSetChanged();
            }

            holder.showall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                    intent.putExtra("allpopularbikes", homeCollectionProducts.get(position).getCollectionId());
                    intent.putExtra("instock", 1);
                    intent.putExtra("type", "eBikes");
                    context.startActivity(intent);
                }
            });

        }

        if (collType.equals("2")) {
            if(catarrlenth>=Integer.parseInt(prima)){
                holder.showall.setVisibility(View.VISIBLE);
            }else {
                holder.showall.setVisibility(View.VISIBLE);
            }
            holder.progressBar.setVisibility(View.VISIBLE);
            if (homeCollectionProducts.get(position).getHomeCollectionProductsCategories().isEmpty()) {
                holder.progressBar.setVisibility(View.GONE);
            } else {
                holder.progressBar.setVisibility(View.GONE);
                CollectionProductsAdapter collectionbannersAdapter = new CollectionProductsAdapter(context, homeCollectionProducts.get(position).getHomeCollectionProductsCategories());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                holder.recyclerlist.setLayoutManager(linearLayoutManager);
                holder.recyclerlist.setAdapter(collectionbannersAdapter);
                collectionbannersAdapter.notifyDataSetChanged();
            }
            holder.showall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                    intent.putExtra("allcategories", homeCollectionProducts.get(position).getCollectionId());
                    intent.putExtra("type", "Categories");
                    context.startActivity(intent);
                }
            });

        }

        if (collType.equals("3")) {
            if(shopsarrlenth>=Integer.parseInt(prima)){
                holder.showall.setVisibility(View.VISIBLE);
            }else {
                holder.showall.setVisibility(View.VISIBLE);
            }
            holder.progressBar.setVisibility(View.VISIBLE);
            if (homeCollectionProducts.get(position).getHomeCollectionProductsShops().isEmpty()) {
                holder.progressBar.setVisibility(View.GONE);
            } else {
                holder.progressBar.setVisibility(View.GONE);
                CollectionProductsAdapter collectionbannersAdapter = new CollectionProductsAdapter(context, homeCollectionProducts.get(position).getHomeCollectionProductsShops());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                holder.recyclerlist.setLayoutManager(linearLayoutManager);
                holder.recyclerlist.setAdapter(collectionbannersAdapter);
                collectionbannersAdapter.notifyDataSetChanged();
            }

            holder.showall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                    intent.putExtra("allshops", homeCollectionProducts.get(position).getCollectionId());
                    intent.putExtra("type", "Dealers");
                    context.startActivity(intent);
                }
            });
        }

        if (collType.equals("4")) {
            if(brandsarrlenth>=Integer.parseInt(prima)){
                holder.showall.setVisibility(View.VISIBLE);
            }else {
                holder.showall.setVisibility(View.VISIBLE);
            }
            holder.progressBar.setVisibility(View.VISIBLE);
            if (homeCollectionProducts.get(position).getHomeCollectionProductsBrands().isEmpty()) {
                holder.progressBar.setVisibility(View.GONE);
            } else {
                holder.progressBar.setVisibility(View.GONE);
                CollectionProductsAdapter collectionbannersAdapter = new CollectionProductsAdapter(context, homeCollectionProducts.get(position).getHomeCollectionProductsBrands());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                holder.recyclerlist.setLayoutManager(linearLayoutManager);
                holder.recyclerlist.setAdapter(collectionbannersAdapter);
                collectionbannersAdapter.notifyDataSetChanged();
            }
            holder.showall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent  intent = new Intent(context, ShowAlleBikesActivity.class);
                    intent.putExtra("allbrands", homeCollectionProducts.get(position).getCollectionId());
                    intent.putExtra("type", "Brands");
                    context.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return homeCollectionProducts.size();
    }

   /* @Override
    public int getItemViewType(int position) {
        return homeCollectionProducts.get(position).getTypeoflayout();
    }*/

    public class HomeHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerlist;
        TextView showall;
        ProgressBar progressBar;
        RelativeLayout relativeLayout;
        private ViewPager mPager;
        private int currentPage = 0;
        private int NUM_PAGES = 0;
        private ArrayList<String> ImagesArray = new ArrayList<String>();

        LinearLayout linearLayout,testlayout,rentlayout;
        NetworkImageView networkImageView1,networkImageView2;

        NetworkImageView topbanner;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            topbanner = itemView.findViewById(R.id.topbannerimage);
            recyclerlist = itemView.findViewById(R.id.recycl);
            title = itemView.findViewById(R.id.collection_title);
            showall = itemView.findViewById(R.id.showall);
            progressBar = itemView.findViewById(R.id.progress);
            relativeLayout = itemView.findViewById(R.id.screen);
            linearLayout = itemView.findViewById(R.id.buttonslay);
            networkImageView1 = itemView.findViewById(R.id.book_test_drive);
            networkImageView2 = itemView.findViewById(R.id.rent_ebike);
            testlayout = itemView.findViewById(R.id.testDrive_layout);
            rentlayout = itemView.findViewById(R.id.rent_layout);

            if (homeCollectionProductsSliders != null) {
                for (int i = 0; i < homeCollectionProductsSliders.size(); i++)
                    ImagesArray.add(homeCollectionProductsSliders.get(i).getSlideImageurl());
            }
            mPager = (ViewPager) itemView.findViewById(R.id.pager);
            mPager.setAdapter(new SlidingImage_Adapter(context, ImagesArray));

            CirclePageIndicator indicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
            indicator.setViewPager(mPager);

            final float density = context.getResources().getDisplayMetrics().density;

//Set circle indicator radius
            indicator.setRadius(5 * density);

            NUM_PAGES = homeCollectionProductsSliders.size();

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


}
