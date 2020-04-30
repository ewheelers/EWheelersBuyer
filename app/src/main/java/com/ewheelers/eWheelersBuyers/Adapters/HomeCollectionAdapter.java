package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.ModelClass.HomeCollectionProducts;
import com.ewheelers.eWheelersBuyers.ModelClass.HomeModelClass;
import com.ewheelers.eWheelersBuyers.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeCollectionAdapter extends RecyclerView.Adapter<HomeCollectionAdapter.HomeHolder> {
    Context context;
    private List<HomeModelClass> homeCollectionProducts;
    private List<HomeCollectionProducts> homeCollectionProductsList = new ArrayList<>();
    private List<HomeCollectionProducts> homeCollectionCategoryList = new ArrayList<>();
    private List<HomeCollectionProducts> homeCollectionbannersList = new ArrayList<>();
    private List<HomeCollectionProducts> homeCollectionshopsList = new ArrayList<>();

    private ArrayList<ArrayList<Integer>> mDataList;
    private SparseIntArray positionList = new SparseIntArray();

    public HomeCollectionAdapter(Context context, List<HomeModelClass> homeCollectionProducts) {
        this.context = context;
        this.homeCollectionProducts = homeCollectionProducts;
    }


    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case HomeModelClass.PRODUCTLAYOUT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_heading_layout, parent, false);
                return new HomeHolder(v);
            case HomeModelClass.CATEGORYLAYOUT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_layout, parent, false);
                return new HomeHolder(v);
            case HomeModelClass.BRANDLAYOUT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.brandslayout, parent, false);
                return new HomeHolder(v);
            case HomeModelClass.SHOPSLAYOUT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_layout, parent, false);
                return new HomeHolder(v);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        final int itemType = getItemViewType(position);
        switch (itemType) {
            case HomeModelClass.PRODUCTLAYOUT:
                homeCollectionProductsList.clear();
                holder.title.setText(homeCollectionProducts.get(position).getHeadcatTitle());
                for (int j = 0; j < homeCollectionProducts.get(position).getJsonArraylist().length(); j++) {
                    try {
                        JSONObject products = homeCollectionProducts.get(position).getJsonArraylist().getJSONObject(j);
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

                        HomeCollectionProducts homeCollectionProducts1 = new HomeCollectionProducts();
                        homeCollectionProducts1.setProdcat_name(productcatname);
                        homeCollectionProducts1.setProduct_name(productName);
                        homeCollectionProducts1.setSelprod_price(productPrice);
                        homeCollectionProducts1.setProduct_image_url(productImageurl);
                        homeCollectionProducts1.setSelprod_id(selproductid);
                        homeCollectionProducts1.setProduct_id(productid);
                        homeCollectionProducts1.setIs_rent(isRent);
                        homeCollectionProducts1.setType(0);
                        homeCollectionProductsList.add(homeCollectionProducts1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                CollectionProductsAdapter collectionProductsAdapter = new CollectionProductsAdapter(context, homeCollectionProductsList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                holder.recyclerlist.setLayoutManager(linearLayoutManager);
                holder.recyclerlist.setAdapter(collectionProductsAdapter);
                //collectionProductsAdapter.notifyDataSetChanged();
                break;
            case HomeModelClass.CATEGORYLAYOUT:
                homeCollectionCategoryList.clear();
                holder.title.setText(homeCollectionProducts.get(position).getHeadcatTitle());
                for (int s = 0; s < homeCollectionProducts.get(position).getJsonArraylist().length(); s++) {
                    try {
                        JSONObject jsonObjectcat = homeCollectionProducts.get(position).getJsonArraylist().getJSONObject(s);
                        String prodcat_id = jsonObjectcat.getString("prodcat_id");
                        String prodcat_name = jsonObjectcat.getString("prodcat_name");
                        String prodcat_description = jsonObjectcat.getString("prodcat_description");
                        String category_image_url = jsonObjectcat.getString("category_image_url");
                        HomeCollectionProducts homeCollectionProducts2 = new HomeCollectionProducts();
                        homeCollectionProducts2.setProdcategory_name(prodcat_name);
                        homeCollectionProducts2.setProdcategory_imageurl(category_image_url);
                        homeCollectionProducts2.setProdcategory_id(prodcat_id);
                        homeCollectionProducts2.setType(3);
                        homeCollectionCategoryList.add(homeCollectionProducts2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CollectionProductsAdapter collectionCategoryAdapter = new CollectionProductsAdapter(context, homeCollectionCategoryList);
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                holder.recyclerlist.setLayoutManager(linearLayoutManager2);
                holder.recyclerlist.setAdapter(collectionCategoryAdapter);
                collectionCategoryAdapter.notifyDataSetChanged();
                break;
            case HomeModelClass.SHOPSLAYOUT:
                homeCollectionshopsList.clear();
                holder.title.setText(homeCollectionProducts.get(position).getHeadcatTitle());
                for (int k = 0; k < homeCollectionProducts.get(position).getJsonArraylist().length(); k++) {
                    JSONObject jsonObjectshop1 = null;
                    try {
                        jsonObjectshop1 = homeCollectionProducts.get(position).getJsonArraylist().getJSONObject(k);
                        String shopbanner = jsonObjectshop1.getString("shop_banner");
                        String shop_id = jsonObjectshop1.getString("shop_id");
                        String shop_user_id = jsonObjectshop1.getString("shop_user_id");
                        String shop_name = jsonObjectshop1.getString("shop_name");
                        String country_name = jsonObjectshop1.getString("country_name");
                        String state_name = jsonObjectshop1.getString("state_name");
                        String rating = jsonObjectshop1.getString("rating");
                        String shop_logo = jsonObjectshop1.getString("shop_logo");

                        HomeCollectionProducts homeCollectionProducts3 = new HomeCollectionProducts();
                        homeCollectionProducts3.setShopbanner(shopbanner);
                        homeCollectionProducts3.setShopid(shop_id);
                        homeCollectionProducts3.setShoplogo(shop_logo);
                        homeCollectionProducts3.setShopname(shop_name);
                        homeCollectionProducts3.setShopuserid(shop_user_id);
                        homeCollectionProducts3.setCountryname(country_name);
                        homeCollectionProducts3.setStatename(state_name);
                        homeCollectionProducts3.setRating(rating);
                        homeCollectionProducts3.setType(4);
                        homeCollectionshopsList.add(homeCollectionProducts3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CollectionProductsAdapter collectionShopsAdapter = new CollectionProductsAdapter(context, homeCollectionshopsList);
                LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                holder.recyclerlist.setLayoutManager(linearLayoutManager3);
                holder.recyclerlist.setAdapter(collectionShopsAdapter);
                collectionShopsAdapter.notifyDataSetChanged();
                break;
            case HomeModelClass.BRANDLAYOUT:
                homeCollectionbannersList.clear();
                holder.title.setText(homeCollectionProducts.get(position).getHeadcatTitle());
                for (int d = 0; d < homeCollectionProducts.get(position).getJsonArraylist().length(); d++) {
                    try {
                        JSONObject products = homeCollectionProducts.get(position).getJsonArraylist().getJSONObject(d);
                        String brandid = products.getString("brand_id");
                        String brandname = products.getString("brand_name");
                        String brandimage = products.getString("brand_image");

                        HomeCollectionProducts homeCollectionProducts4 = new HomeCollectionProducts();
                        homeCollectionProducts4.setBrandimageurl(brandimage);
                        homeCollectionProducts4.setBrandid(brandid);
                        homeCollectionProducts4.setBrandname(brandname);
                        homeCollectionProducts4.setType(1);
                        homeCollectionbannersList.add(homeCollectionProducts4);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CollectionProductsAdapter collectionbannersAdapter = new CollectionProductsAdapter(context, homeCollectionbannersList);
                GridLayoutManager linearLayoutManager4 = new GridLayoutManager(context, 3);
                holder.recyclerlist.setLayoutManager(linearLayoutManager4);
                holder.recyclerlist.setAdapter(collectionbannersAdapter);
                collectionbannersAdapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public int getItemCount() {
        return homeCollectionProducts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return homeCollectionProducts.get(position).getTypeoflayout();
    }


    public class HomeHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerlist;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            recyclerlist = itemView.findViewById(R.id.collection_listview);
            title = itemView.findViewById(R.id.collection_title);
        }
    }


}
