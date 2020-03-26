package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.ewheelersbuyer.ModelClass.AllebikesModelClass;
import com.ewheelers.ewheelersbuyer.ProductDetailActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;

import java.util.List;

public class AllebikesAdapter extends RecyclerView.Adapter<AllebikesAdapter.BikeHolder> {
    Context context;
    List<AllebikesModelClass> allebikesModelClasses;

    public AllebikesAdapter(Context context, List<AllebikesModelClass> allebikesModelClasses) {
        this.context = context;
        this.allebikesModelClasses = allebikesModelClasses;
    }

    @NonNull
    @Override
    public BikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_ebikes_layout, parent, false);
        return new BikeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BikeHolder holder, int position) {
        holder.bikename.setText(allebikesModelClasses.get(position).getProductName());
        holder.bikePrice.setText(allebikesModelClasses.get(position).getPrice());
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        imageLoader.get(allebikesModelClasses.get(position).getNetworkImage(), ImageLoader.getImageListener(holder.networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
        holder.networkImageView.setImageUrl(allebikesModelClasses.get(position).getNetworkImage(), imageLoader);
        holder.buybike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetailActivity.class);
                i.putExtra("productid",allebikesModelClasses.get(position).getProductid());
                context.startActivity(i);
            }
        });
        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetailActivity.class);
                i.putExtra("test","test");
                i.putExtra("productid",allebikesModelClasses.get(position).getProductid());
                context.startActivity(i);
            }
        });
        holder.rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetailActivity.class);
                i.putExtra("rent","rent");
                i.putExtra("productid",allebikesModelClasses.get(position).getProductid());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allebikesModelClasses.size();
    }

    public class BikeHolder extends RecyclerView.ViewHolder {
        NetworkImageView networkImageView;
        TextView bikename, buybike, bikePrice,test,rent;

        public BikeHolder(@NonNull View itemView) {
            super(itemView);
            bikename = itemView.findViewById(R.id.bikename);
            networkImageView = itemView.findViewById(R.id.network_image);
            buybike = itemView.findViewById(R.id.buyBike);
            bikePrice = itemView.findViewById(R.id.bikePrice);
            test = itemView.findViewById(R.id.testBike);
            rent = itemView.findViewById(R.id.rentBike);

        }
    }
}
