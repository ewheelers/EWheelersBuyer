package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.ewheelersbuyer.ModelClass.AllebikesModelClass;
import com.ewheelers.ewheelersbuyer.ProductDetailActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.ShowAlleBikesActivity;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class AllebikesAdapter extends RecyclerView.Adapter<AllebikesAdapter.BikeHolder> implements Filterable {
    Context context;
    List<AllebikesModelClass> allebikesModelClasses;
    List<AllebikesModelClass> classeFilter;

    public AllebikesAdapter(Context context, List<AllebikesModelClass> allebikesModelClasses) {
        this.context = context;
        this.allebikesModelClasses = allebikesModelClasses;
        classeFilter = new ArrayList<>(allebikesModelClasses);

    }

    @NonNull
    @Override
    public BikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case AllebikesModelClass.ALLBIKES:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_ebikes_layout, parent, false);
                return new BikeHolder(v);
            case AllebikesModelClass.ALLBRANDS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.allbrands_layout, parent, false);
                return new BikeHolder(v);
            case AllebikesModelClass.ALLCATEGORIES:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_layout, parent, false);
                return new BikeHolder(v);
            case AllebikesModelClass.ALLSHOPS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_shops_layout, parent, false);
                return new BikeHolder(v);
            case AllebikesModelClass.ALLDEALERS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_dealers_layout, parent, false);
                return new BikeHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BikeHolder holder, int position) {
        final int itemType = getItemViewType(position);
        switch (itemType) {
            case AllebikesModelClass.ALLBIKES:
                holder.bikename.setText(allebikesModelClasses.get(position).getProductName());
                holder.bikePrice.setText("Price : \u20B9 " + allebikesModelClasses.get(position).getPrice());
                ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                imageLoader.get(allebikesModelClasses.get(position).getNetworkImage(), ImageLoader.getImageListener(holder.networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                holder.networkImageView.setImageUrl(allebikesModelClasses.get(position).getNetworkImage(), imageLoader);
                String testdriveenable = allebikesModelClasses.get(position).getTestdriveenable();
                String issell = allebikesModelClasses.get(position).getIssell();
                String isrent = allebikesModelClasses.get(position).getIsrent();
                String instock = allebikesModelClasses.get(position).getInstock();
                if(instock.equals("0")){
                    holder.soldlayout.setVisibility(View.VISIBLE);
                    holder.test.setVisibility(View.GONE);
                    holder.buybike.setVisibility(View.GONE);
                    holder.rent.setVisibility(View.GONE);
                }else {
                    if (testdriveenable.equals("1")) {
                        holder.test.setVisibility(View.VISIBLE);
                    }
                    if (issell.equals("1")) {
                        holder.buybike.setVisibility(View.VISIBLE);
                    }
                    if (isrent.equals("1")) {
                        holder.rent.setVisibility(View.VISIBLE);
                    }
                }
                holder.buybike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ProductDetailActivity.class);
                        i.putExtra("productid", allebikesModelClasses.get(position).getProductid());
                        context.startActivity(i);
                    }
                });
                holder.test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ProductDetailActivity.class);
                        i.putExtra("test", "test");
                        i.putExtra("productid", allebikesModelClasses.get(position).getProductid());
                        context.startActivity(i);
                    }
                });
                holder.rent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ProductDetailActivity.class);
                        i.putExtra("rent", "rent");
                        i.putExtra("productid", allebikesModelClasses.get(position).getProductid());
                        context.startActivity(i);
                    }
                });
                break;
            case AllebikesModelClass.ALLCATEGORIES:
                holder.catname.setText(allebikesModelClasses.get(position).getProductName());
                imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                imageLoader.get(allebikesModelClasses.get(position).getNetworkImage(), ImageLoader.getImageListener(holder.categoryImage, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                holder.categoryImage.setImageUrl(allebikesModelClasses.get(position).getNetworkImage(), imageLoader);
                holder.categoryLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                        intent.putExtra("catid",allebikesModelClasses.get(position).getProductid());
                        intent.putExtra("catname",allebikesModelClasses.get(position).getProductName());
                        context.startActivity(intent);
                    }
                });
                break;
            case AllebikesModelClass.ALLBRANDS:
                holder.brandtitle.setText(allebikesModelClasses.get(position).getProductName());
                holder.branddescription.setText(allebikesModelClasses.get(position).getPrice());
                holder.brandsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                        intent.putExtra("brandid",allebikesModelClasses.get(position).getProductid());
                        intent.putExtra("brandname",allebikesModelClasses.get(position).getProductName());
                        context.startActivity(intent);
                    }
                });
                break;
            case AllebikesModelClass.ALLSHOPS:
                holder.shopNetworkImg.setVisibility(View.GONE);
                holder.shopname.setText(allebikesModelClasses.get(position).getProductName());
                holder.shopaddress.setText(allebikesModelClasses.get(position).getPrice());
                holder.shopNowbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                        intent.putExtra("shopid",allebikesModelClasses.get(position).getProductid());
                        intent.putExtra("shopname",allebikesModelClasses.get(position).getProductName());
                        context.startActivity(intent);
                    }
                });
                break;
                case AllebikesModelClass.ALLDEALERS:
                    holder.shopname.setText(allebikesModelClasses.get(position).getProductName());
                    holder.shopaddress.setText(allebikesModelClasses.get(position).getPrice());
                    holder.shopphoneno.setText("phone - "+allebikesModelClasses.get(position).getShopphone());
                    holder.shopNowbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                            intent.putExtra("shopid",allebikesModelClasses.get(position).getProductid());
                            intent.putExtra("shopname",allebikesModelClasses.get(position).getProductName());
                            intent.putExtra("shopaddress",allebikesModelClasses.get(position).getPrice());
                            context.startActivity(intent);
                        }
                    });
                    break;

        }
    }

    @Override
    public int getItemCount() {
        return allebikesModelClasses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return allebikesModelClasses.get(position).getTypeLayout();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AllebikesModelClass> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(classeFilter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AllebikesModelClass item : classeFilter) {
                    if(item.getTypeLayout()==4){
                        if (item.getProductName().toLowerCase().contains(filterPattern)||item.getPrice().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }else {
                        if (item.getProductName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            allebikesModelClasses.clear();
            allebikesModelClasses.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class BikeHolder extends RecyclerView.ViewHolder {
        NetworkImageView networkImageView, categoryImage, brandImage,shopNetworkImg;
        TextView bikename, buybike, bikePrice, test, rent, catname, brandtitle, branddescription,shopname,shopaddress,shopphoneno,shoppincode;
        RelativeLayout soldlayout;
        LinearLayout categoryLayout,brandsLayout;
        Button shopNowbtn;
        public BikeHolder(@NonNull View itemView) {
            super(itemView);
            bikename = itemView.findViewById(R.id.bikename);
            networkImageView = itemView.findViewById(R.id.network_image);
            buybike = itemView.findViewById(R.id.buyBike);
            bikePrice = itemView.findViewById(R.id.bikePrice);
            test = itemView.findViewById(R.id.testBike);
            rent = itemView.findViewById(R.id.rentBike);
            catname = itemView.findViewById(R.id.category_name);
            categoryImage = itemView.findViewById(R.id.category_image);
            brandImage = itemView.findViewById(R.id.brand_image);
            brandtitle = itemView.findViewById(R.id.brand_name);
            branddescription = itemView.findViewById(R.id.brand_description);
            soldlayout = itemView.findViewById(R.id.sold_layout);
            categoryLayout = itemView.findViewById(R.id.cat_layout);
            brandsLayout = itemView.findViewById(R.id.brands_layout);

            shopNetworkImg = itemView.findViewById(R.id.imageView_shopLogo);
            shopname = itemView.findViewById(R.id.shopName);
            shopaddress = itemView.findViewById(R.id.addess);
            shopNowbtn = itemView.findViewById(R.id.shopNow);
            shopphoneno = itemView.findViewById(R.id.phoneno_shop);
           // shoppincode = itemView.findViewById(R.id.);
        }
    }
}
