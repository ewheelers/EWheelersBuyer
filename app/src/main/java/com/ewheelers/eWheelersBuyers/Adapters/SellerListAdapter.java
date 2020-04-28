package com.ewheelers.eWheelersBuyers.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.ModelClass.SellerListModel;
import com.ewheelers.eWheelersBuyers.ProductDetailActivity;
import com.ewheelers.eWheelersBuyers.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.SellHolder> implements Filterable {
    Context context;
    List<SellerListModel> sellerListModels;
    List<SellerListModel> sellerFilter;


    public SellerListAdapter(Context context, List<SellerListModel> sellerListModels) {
        this.context = context;
        this.sellerListModels = sellerListModels;
        sellerFilter = new ArrayList<>(sellerListModels);
    }

    @NonNull
    @Override
    public SellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sellers_list_layout, parent, false);
        return new SellHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull SellHolder holder, int position) {
        holder.sellerName.setText(sellerListModels.get(position).getSellersname());
        holder.sellerAddress.setText(sellerListModels.get(position).getSellersaddress());
        String sellerphone = sellerListModels.get(position).getSellersphoneno();
        holder.sellerPrice.setText("Selling Price \u20B9" + sellerListModels.get(position).getSellerPrice());
        String codavail = sellerListModels.get(position).getSellerCod();
        if (codavail.equals("1")) {
            holder.sellerCod.setText("COD Available");
        } else {
            holder.sellerCod.setText("COD Not available");
        }

        holder.sellerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    call_action(sellerListModels.get(position).getSellersphoneno());
                }
            }
        });
        holder.sellerNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Float.parseFloat(sellerListModels.get(position).getSellerslatitude()), Float.parseFloat(sellerListModels.get(position).getSellerslongitude()));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });
        holder.viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetailActivity.class);
                i.putExtra("productid", sellerListModels.get(position).getSelproductid());
                context.startActivity(i);
            }
        });


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
        return sellerListModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return sellerListModels.get(position).getTypeoflayout();
    }

    @Override
    public Filter getFilter() {

        return exampleFilter;
    }


    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SellerListModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(sellerFilter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SellerListModel item : sellerFilter) {
                    if (item.getSellersname().toLowerCase().contains(filterPattern)||item.getSellersaddress().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sellerListModels.clear();
            sellerListModels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class SellHolder extends RecyclerView.ViewHolder {
        TextView sellerName, sellerAddress, sellerPrice, sellerCod, viewdetails;
        ImageView sellerCall, sellerNavigate;

        public SellHolder(@NonNull View itemView) {
            super(itemView);
            sellerName = itemView.findViewById(R.id.seller_name);
            sellerAddress = itemView.findViewById(R.id.seller_address);
            sellerCall = itemView.findViewById(R.id.call_seller);
            sellerNavigate = itemView.findViewById(R.id.navigate_map);
            sellerPrice = itemView.findViewById(R.id.seller_price);
            sellerCod = itemView.findViewById(R.id.seller_cod);
            viewdetails = itemView.findViewById(R.id.seller_viewdetails);
        }
    }
}
