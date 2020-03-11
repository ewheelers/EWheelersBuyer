package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.ewheelersbuyer.ModelClass.HomeCollectionProducts;
import com.ewheelers.ewheelersbuyer.ProductDetailActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;

import java.util.List;

public class CollectionProductsAdapter extends RecyclerView.Adapter<CollectionProductsAdapter.CollectionProductHolder> {
    private Context context;
    private List<HomeCollectionProducts> homeCollectionProducts;
    private ImageLoader imageLoader;

    public CollectionProductsAdapter(Context context, List<HomeCollectionProducts> homeCollectionProducts) {
        this.context = context;
        this.homeCollectionProducts = homeCollectionProducts;

    }

    public CollectionProductsAdapter(List<HomeCollectionProducts> allItemsInSection) {
        this.homeCollectionProducts = allItemsInSection;
    }

    @NonNull
    @Override
    public CollectionProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case HomeCollectionProducts.PRODUCTS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_products_layout, parent, false);
                return new CollectionProductHolder(v);
            case HomeCollectionProducts.BRANDS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.brands_layout, parent, false);
                return new CollectionProductHolder(v);
            case HomeCollectionProducts.PREVIEW:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_product_layout, parent, false);
                return new CollectionProductHolder(v);
            case HomeCollectionProducts.SHOPS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_shops_layout, parent, false);
                return new CollectionProductHolder(v);
            case HomeCollectionProducts.CATEGORY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_layout, parent, false);
                return new CollectionProductHolder(v);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionProductHolder holder, final int position) {

        final int itemType = getItemViewType(position);
        switch (itemType) {
            case HomeCollectionProducts.PRODUCTS:
                holder.model_name.setText("(" + homeCollectionProducts.get(position).getProdcat_name() + ")\n" + homeCollectionProducts.get(position).getProduct_name());
                holder.price.setText("\u20B9 " + homeCollectionProducts.get(position).getSelprod_price());
                String url_image = homeCollectionProducts.get(position).getProduct_image_url();
                if (url_image != null) {
                    imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                    imageLoader.get(homeCollectionProducts.get(position).getProduct_image_url(), ImageLoader.getImageListener(holder.product_image, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                    holder.product_image.setImageUrl(homeCollectionProducts.get(position).getProduct_image_url(), imageLoader);
                }
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("productid", homeCollectionProducts.get(position).getSelprod_id());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });

              /*  if (homeCollectionProducts.get(position).getIs_rent().equals("1")) {
                    holder.availRent.setText("Available for Rent");
                } else {
                    holder.availRent.setText("");
                }*/

                holder.availRent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                break;
            case HomeCollectionProducts.BRANDS:
                String urlImage = homeCollectionProducts.get(position).getBrandimageurl();
                if (urlImage != null) {
                    imageLoader = VolleySingleton.getInstance(context)
                            .getImageLoader();
                    imageLoader.get(homeCollectionProducts.get(position).getBrandimageurl(), ImageLoader.getImageListener(holder.brand_image, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                    holder.brand_image.setImageUrl(homeCollectionProducts.get(position).getBrandimageurl(), imageLoader);
                }
                break;
            case HomeCollectionProducts.PREVIEW:
                String url = homeCollectionProducts.get(position).getBrandimageurl();
                if (url != null) {
                    imageLoader = VolleySingleton.getInstance(context)
                            .getImageLoader();
                    imageLoader.get(homeCollectionProducts.get(position).getBrandimageurl(), ImageLoader.getImageListener(holder.preview_image, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                    holder.preview_image.setImageUrl(homeCollectionProducts.get(position).getBrandimageurl(), imageLoader);
                }
                break;
            case HomeCollectionProducts.SHOPS:
                holder.shop_name.setText(homeCollectionProducts.get(position).getShopname());
                holder.shop_address.setText(homeCollectionProducts.get(position).getStatename()+","+homeCollectionProducts.get(position).getCountryname());
                String urlshop = homeCollectionProducts.get(position).getShoplogo();
                if (urlshop != null) {
                    imageLoader = VolleySingleton.getInstance(context)
                            .getImageLoader();
                    imageLoader.get(urlshop, ImageLoader.getImageListener(holder.shopLogo, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                    holder.shopLogo.setImageUrl(urlshop, imageLoader);
                }
                break;
            case HomeCollectionProducts.CATEGORY:
                holder.category_name.setText(homeCollectionProducts.get(position).getProdcategory_name());
                String urlcat = homeCollectionProducts.get(position).getProdcategory_imageurl();
                if (urlcat != null) {
                    imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                    imageLoader.get(urlcat, ImageLoader.getImageListener(holder.categoryImage, R.drawable.cart, android.R.drawable.ic_dialog_alert));
                    holder.categoryImage.setImageUrl(urlcat, imageLoader);
                }
                break;

        }

    }


    @Override
    public int getItemCount() {
        if (homeCollectionProducts.size() == 0) {
            return 0;
        } else {
            return homeCollectionProducts.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return homeCollectionProducts.get(position).getType();
    }

    public class CollectionProductHolder extends RecyclerView.ViewHolder {
        NetworkImageView product_image, brand_image, preview_image, shopLogo, categoryImage;
        TextView model_name, price, availRent, shop_name, shop_address, category_name;
        Button shop_nowBtn;
        LinearLayout linearLayout;

        public CollectionProductHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.product_price);
            model_name = itemView.findViewById(R.id.product_name);
            product_image = itemView.findViewById(R.id.image_view);
            brand_image = itemView.findViewById(R.id.brand_image);
            linearLayout = itemView.findViewById(R.id.click_product);
            preview_image = itemView.findViewById(R.id.preview_small_img);
            availRent = itemView.findViewById(R.id.isRentavailable);
            shopLogo = itemView.findViewById(R.id.imageView_shopLogo);
            shop_nowBtn = itemView.findViewById(R.id.shopNow);
            shop_name = itemView.findViewById(R.id.shopName);
            shop_address = itemView.findViewById(R.id.addess);
            category_name = itemView.findViewById(R.id.category_name);
            categoryImage = itemView.findViewById(R.id.category_image);
        }
    }
}
