package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.eWheelersBuyers.ModelClass.buyingList;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;


import java.util.List;

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.Holdbuyer> {
    Context context;
    List<buyingList> buyListList;

    public BuyListAdapter(Context context, List<buyingList> buyListList) {
        this.context = context;
        this.buyListList = buyListList;
    }

    @NonNull
    @Override
    public Holdbuyer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_buying_prods, parent, false);
        return new Holdbuyer(v);    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull Holdbuyer holder, int position) {
        holder.brand.setText(buyListList.get(position).getBrand());
        holder.city.setText(buyListList.get(position).getCity());
        holder.model.setText(buyListList.get(position).getModel());
        holder.postedon.setText(String.format("Posted On: %s", buyListList.get(position).getPostedOn()));
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        imageLoader.get(buyListList.get(position).getImgUrl(), ImageLoader.getImageListener(holder.networkImageView, 0, android.R.drawable.ic_dialog_alert));
        holder.networkImageView.setImageUrl(buyListList.get(position).getImgUrl(), imageLoader);
        /*holder.see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailItemActivity.class);
                i.putExtra("brandname",buyListList.get(position).getBrand());
                i.putExtra("city",buyListList.get(position).getCity());
                i.putExtra("model",buyListList.get(position).getModel());
                i.putExtra("postedon",holder.postedon.getText().toString());
                i.putExtra("imgurl",buyListList.get(position).getImgUrl());
                i.putExtra("manyr",buyListList.get(position).getManufactur_yr());
                i.putExtra("regyr",buyListList.get(position).getReg_yr());
                i.putExtra("state", buyListList.get(position).getState());
                i.putExtra("pincode",buyListList.get(position).getPincode());
                i.putExtra("mobileno",buyListList.get(position).getPhone());
                i.putExtra("userid",buyListList.get(position).getUserid());
                context.startActivity(i);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return buyListList.size();
    }

    public class Holdbuyer extends RecyclerView.ViewHolder {
        TextView brand,model,city,postedon,see;
        NetworkImageView networkImageView;
        public Holdbuyer(@NonNull View itemView) {
            super(itemView);
            networkImageView = itemView.findViewById(R.id.image);
            brand = itemView.findViewById(R.id.brand_name);
            model = itemView.findViewById(R.id.model_name);
            city = itemView.findViewById(R.id.city_name);
            postedon = itemView.findViewById(R.id.posted_date);
            //see = itemView.findViewById(R.id.see_details);
        }
    }
}
