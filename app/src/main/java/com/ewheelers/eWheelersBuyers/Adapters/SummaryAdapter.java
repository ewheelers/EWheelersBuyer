package com.ewheelers.eWheelersBuyers.Adapters;

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
import com.ewheelers.eWheelersBuyers.CartSummaryActivity;
import com.ewheelers.eWheelersBuyers.ModelClass.CartListClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SellersListActivity;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryHolder> {
    Context context;
    List<CartListClass> cartListClassList;

    public SummaryAdapter(Context cartSummaryActivity, List<CartListClass> cartListClassList) {
        this.context = cartSummaryActivity;
        this.cartListClassList = cartListClassList;
    }

    @NonNull
    @Override
    public SummaryAdapter.SummaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_layout, parent, false);
        return new SummaryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.SummaryHolder holder, int position) {
        holder.shopname.setText(cartListClassList.get(position).getShopname());
        holder.brandname.setText(cartListClassList.get(position).getBrandname());
        holder.tit.setText(cartListClassList.get(position).getProductName());
        List<String> strings = cartListClassList.get(position).getOptions();
        if(strings!=null) {
            holder.optins.setText(strings.toString());
        }
        holder.price.setText("\u20B9 " + cartListClassList.get(position).getProductPrice());
        holder.qty.setText("Qty : "+cartListClassList.get(position).getProduct_qty());
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        imageLoader.get(cartListClassList.get(position).getImageurl(), ImageLoader.getImageListener(holder.image, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
        holder.image.setImageUrl(cartListClassList.get(position).getImageurl(), imageLoader);
        holder.sellerAddress.setText(cartListClassList.get(position).getSellerAddress());
        holder.changepickaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(context, SellersListActivity.class);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inten.putExtra("fromactivity", "details");
                inten.putExtra("selproductid", cartListClassList.get(position).getProductid());
                context.startActivity(inten);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartListClassList.size();
    }

    public class SummaryHolder extends RecyclerView.ViewHolder {
        TextView shopname, brandname, tit, optins, price, qty, sellerAddress, changepickaddress;
        NetworkImageView image;

        public SummaryHolder(@NonNull View itemView) {
            super(itemView);
            shopname = itemView.findViewById(R.id.shop_name);
            image = itemView.findViewById(R.id.image);
            brandname = itemView.findViewById(R.id.brand);
            tit = itemView.findViewById(R.id.title);
            optins = itemView.findViewById(R.id.options);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            sellerAddress = itemView.findViewById(R.id.seller_address);
            changepickaddress = itemView.findViewById(R.id.changepickaddress);
        }
    }
}
