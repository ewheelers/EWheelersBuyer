package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.ewheelersbuyer.ModelClass.CartListClass;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;

import java.util.List;

public class CartListingAdapter extends RecyclerView.Adapter<CartListingAdapter.MyCartListHolder> {
    private Context context;
    private List<CartListClass> cartListClasses;

    public CartListingAdapter(Context context, List<CartListClass> cartListClasses) {
        this.context = context;
        this.cartListClasses = cartListClasses;
    }

    @NonNull
    @Override
    public MyCartListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlistinglayout,parent,false);
        return new MyCartListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartListHolder holder, int position) {
        holder.brandname.setText(cartListClasses.get(position).getBrandname());
        holder.options.setText(cartListClasses.get(position).getProductOption());
        holder.shopname.setText(cartListClasses.get(position).getShopname());
        holder.title.setText(cartListClasses.get(position).getProductName());
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        imageLoader.get(cartListClasses.get(position).getImageurl(), ImageLoader.getImageListener(holder.image_url, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
        holder.image_url.setImageUrl(cartListClasses.get(position).getImageurl(), imageLoader);
    }

    @Override
    public int getItemCount() {
        return cartListClasses.size();
    }

    public class MyCartListHolder extends RecyclerView.ViewHolder {
        TextView shopname,title,options,price,quantity,brandname;
        NetworkImageView image_url;
        Button addproduct,removeproduct;
        public MyCartListHolder(@NonNull View itemView) {
            super(itemView);
            image_url = itemView.findViewById(R.id.image);
            shopname = itemView.findViewById(R.id.shop_name);
            title = itemView.findViewById(R.id.title);
            options = itemView.findViewById(R.id.options);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.qty);
            brandname = itemView.findViewById(R.id.brand);
            addproduct = itemView.findViewById(R.id.addProduct);
            removeproduct = itemView.findViewById(R.id.removeproduct);
        }
    }
}
