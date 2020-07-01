package com.ewheelers.eWheelersBuyers.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.eWheelersBuyers.ModelClass.AllebikesModelClass;
import com.ewheelers.eWheelersBuyers.ProductDetailActivity;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.ShowAlleBikesActivity;
import com.ewheelers.eWheelersBuyers.TestDriveAndRentabike;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.ewheelers.eWheelersBuyers.ZoomingActivity;

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
                String isbook = allebikesModelClasses.get(position).getBooknow();
                String isrent = allebikesModelClasses.get(position).getIsrent();
                String instock = allebikesModelClasses.get(position).getInstock();
                if (allebikesModelClasses.get(position).getInstock().equals("0")) {
                    holder.soldlayout.setVisibility(View.VISIBLE);
                    holder.test.setVisibility(View.GONE);
                    holder.buybike.setVisibility(View.GONE);
                    holder.rent.setVisibility(View.GONE);
                } else {
                    if (allebikesModelClasses.get(position).getTestdriveenable().equals("1")) {
                        holder.test.setVisibility(View.VISIBLE);
                    }

                    if (allebikesModelClasses.get(position).getProductbook().equals("1")) {
                        if (allebikesModelClasses.get(position).getBooknow().equals("1")) {
                            holder.buybike.setVisibility(View.VISIBLE);
                            holder.book.setVisibility(View.GONE);
                        }
                        if (allebikesModelClasses.get(position).getBooknow().equals("2")) {
                            holder.book.setVisibility(View.VISIBLE);
                            holder.buybike.setVisibility(View.GONE);
                        }
                        if (allebikesModelClasses.get(position).getBooknow().equals("0")) {
                            holder.book.setVisibility(View.VISIBLE);
                            holder.buybike.setVisibility(View.VISIBLE);
                        }

                    } else {
                        holder.buybike.setVisibility(View.VISIBLE);
                        holder.book.setVisibility(View.GONE);
                    }

                    if (allebikesModelClasses.get(position).getIsrent().equals("1")) {
                        holder.rent.setVisibility(View.VISIBLE);
                    }
                }

                holder.zoomimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent iz = new Intent(context, ZoomingActivity.class);
                        iz.putExtra("productid", allebikesModelClasses.get(position).getProductid());
                        context.startActivity(iz);
                    }
                });

                holder.networkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ProductDetailActivity.class);
                        i.putExtra("productid", allebikesModelClasses.get(position).getProductid());
                        context.startActivity(i);
                    }
                });
                holder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      /* if(context instanceof ProductDetailActivity){
                           ((ProductDetailActivity)context).addTocart(allebikesModelClasses.get(position).getProductid(),"Booknow");
                       }*/
                        Intent i = new Intent(context, ProductDetailActivity.class);
                        i.putExtra("productid", allebikesModelClasses.get(position).getProductid());
                        context.startActivity(i);
                    }
                });
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
                        Intent i = new Intent(context, TestDriveAndRentabike.class);
                        i.putExtra("typeoflayout", "test");
                        i.putExtra("productid", allebikesModelClasses.get(position).getProductid());
                        context.startActivity(i);
                    }
                });
                holder.rent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ProductDetailActivity.class);
                        i.putExtra("typeoflayout", "rent");
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
                        intent.putExtra("catid", allebikesModelClasses.get(position).getProductid());
                        intent.putExtra("catname", allebikesModelClasses.get(position).getProductName());
                        context.startActivity(intent);
                    }
                });
                break;
            case AllebikesModelClass.ALLBRANDS:
                imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                imageLoader.get(allebikesModelClasses.get(position).getNetworkImage(), ImageLoader.getImageListener(holder.brandImage, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                holder.brandImage.setImageUrl(allebikesModelClasses.get(position).getNetworkImage(), imageLoader);
                holder.brandtitle.setText(allebikesModelClasses.get(position).getProductName());

                if (allebikesModelClasses.get(position).getPrice().length() > 80) {
                    String brand_Shortdesc = allebikesModelClasses.get(position).getPrice().substring(0, 80) + "...";

                    if(brand_Shortdesc.charAt(0) == '(')
                        brand_Shortdesc = brand_Shortdesc.substring(brand_Shortdesc.indexOf(")") + 1).trim();

                    holder.branddescription.setText(Html.fromHtml(brand_Shortdesc + "<font color='blue'> <u>View More</u></font>"));
                    holder.branddescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.brandviewmore.setVisibility(View.VISIBLE);
                            String shortdesc = allebikesModelClasses.get(position).getPrice();
                            if(shortdesc.charAt(0) == '(')
                                shortdesc = shortdesc.substring(shortdesc.indexOf(")") + 1).trim();
                            holder.brandviewmore.setText(Html.fromHtml( shortdesc + "<font color='red'> <u>View Less</u></font>"));
                            holder.branddescription.setVisibility(View.GONE);
                        }
                    });
                } else {
                    holder.branddescription.setText(allebikesModelClasses.get(position).getPrice());
                }

                holder.brandviewmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.brandviewmore.setVisibility(View.GONE);
                        holder.branddescription.setVisibility(View.VISIBLE);
                    }
                });

                holder.brandsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                        intent.putExtra("brandid", allebikesModelClasses.get(position).getProductid());
                        intent.putExtra("brandname", allebikesModelClasses.get(position).getProductName());
                        intent.putExtra("branddescription", allebikesModelClasses.get(position).getPrice());
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
                        intent.putExtra("shopid", allebikesModelClasses.get(position).getProductid());
                        intent.putExtra("shopname", allebikesModelClasses.get(position).getProductName());
                        context.startActivity(intent);
                    }
                });
                break;
            case AllebikesModelClass.ALLDEALERS:
                holder.shopname.setText(allebikesModelClasses.get(position).getProductName());
                holder.shopaddress.setText(allebikesModelClasses.get(position).getPrice());
                holder.shopphoneno.setText("Phone - " + allebikesModelClasses.get(position).getShopphone());
                holder.shopphoneno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPermissionGranted()) {
                            call_action(allebikesModelClasses.get(position).getShopphone());
                        }
                    }
                });
                holder.shopNowbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ShowAlleBikesActivity.class);
                        intent.putExtra("shopid", allebikesModelClasses.get(position).getProductid());
                        intent.putExtra("shopname", allebikesModelClasses.get(position).getProductName());
                        intent.putExtra("shopaddress", allebikesModelClasses.get(position).getPrice());
                        intent.putExtra("shopphone", allebikesModelClasses.get(position).getShopphone());
                        context.startActivity(intent);
                    }
                });
                break;

        }
    }

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
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
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        context.startActivity(callIntent);
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
                    if (item.getTypeLayout() == 4) {
                        if (item.getProductName().toLowerCase().contains(filterPattern) || item.getPrice().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    } else {
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
        NetworkImageView networkImageView, categoryImage, brandImage, shopNetworkImg;
        TextView bikename, bikePrice, catname, brandtitle, branddescription, shopname, shopaddress, shopphoneno, shoppincode;
        CardView buybike, test, rent, book;
        RelativeLayout soldlayout;
        LinearLayout categoryLayout;
        ImageView brandsLayout;
        TextView zoomimg, brandviewmore;
        Button shopNowbtn;

        public BikeHolder(@NonNull View itemView) {
            super(itemView);
            zoomimg = itemView.findViewById(R.id.zoom);
            bikename = itemView.findViewById(R.id.bikename);
            networkImageView = itemView.findViewById(R.id.network_image);
            buybike = itemView.findViewById(R.id.buyBike);
            bikePrice = itemView.findViewById(R.id.bikePrice);
            test = itemView.findViewById(R.id.testBike);
            rent = itemView.findViewById(R.id.rentBike);
            book = itemView.findViewById(R.id.bookBike);
            catname = itemView.findViewById(R.id.category_name);
            categoryImage = itemView.findViewById(R.id.category_image);
            brandImage = itemView.findViewById(R.id.brand_image);
            brandtitle = itemView.findViewById(R.id.brand_name);
            branddescription = itemView.findViewById(R.id.brand_description);
            soldlayout = itemView.findViewById(R.id.sold_layout);
            categoryLayout = itemView.findViewById(R.id.cat_layout);
            brandsLayout = itemView.findViewById(R.id.brands_layout);
            brandviewmore = itemView.findViewById(R.id.brand_viewmore);
            shopNetworkImg = itemView.findViewById(R.id.imageView_shopLogo);
            shopname = itemView.findViewById(R.id.shopName);
            shopaddress = itemView.findViewById(R.id.addess);
            shopNowbtn = itemView.findViewById(R.id.shopNow);
            shopphoneno = itemView.findViewById(R.id.phoneno_shop);
            // shoppincode = itemView.findViewById(R.id.);
        }
    }
}
