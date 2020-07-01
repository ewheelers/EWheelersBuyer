package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.eWheelersBuyers.ChargeDetailPage;
import com.ewheelers.eWheelersBuyers.ModelClass.AddonsClass;
import com.ewheelers.eWheelersBuyers.ProductDetailActivity;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import java.util.List;

public class SPProductsAdapter extends RecyclerView.Adapter<SPProductsAdapter.ProductHolder> {
    Context context;
    private List<AddonsClass> addonsClasses;

    public SPProductsAdapter(Context context, List<AddonsClass> addonsClasses) {
        this.context = context;
        this.addonsClasses = addonsClasses;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_product_layout,parent,false);
        return new ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.titile.setText(addonsClasses.get(position).getBuywithproductname());
        holder.price.setText("\u20B9 "+addonsClasses.get(position).getBuywithproductprice());
        String buywithurl = addonsClasses.get(position).getLogo();
        if (buywithurl != null) {
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
            imageLoader.get(buywithurl, ImageLoader.getImageListener(holder.networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
            holder.networkImageView.setImageUrl(buywithurl, imageLoader);
        }
        holder.bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChargeDetailPage.class);
                i.putExtra("productid",addonsClasses.get(position).getButwithselectedProductId());
                i.putExtra("productname",addonsClasses.get(position).getBuywithproductname());
                i.putExtra("uaname",addonsClasses.get(position).getUaname());
                i.putExtra("stationaddress",addonsClasses.get(position).getUaddress());
                i.putExtra("provider",addonsClasses.get(position).getServiceprovider());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addonsClasses.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView titile,price;
        NetworkImageView networkImageView;
        Button bookbtn;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            networkImageView = itemView.findViewById(R.id.produt_img);
            titile=itemView.findViewById(R.id.sel_pro_tit);
            price=itemView.findViewById(R.id.the_price);
            bookbtn=itemView.findViewById(R.id.book_pro);
        }
    }
}

