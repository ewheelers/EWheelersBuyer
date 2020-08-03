package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.eWheelersBuyers.AddCartfromServices;
import com.ewheelers.eWheelersBuyers.CartSummaryActivity;
import com.ewheelers.eWheelersBuyers.ModelClass.PromoCodesModel;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import java.util.List;

public class ServiceCouponsAdapter extends RecyclerView.Adapter<ServiceCouponsAdapter.CouponHolder> {
    Context context;
    private List<PromoCodesModel> strings;
    int index_row=-1;
    public ServiceCouponsAdapter(Context context, List<PromoCodesModel> strings) {
        this.context = context;
        this.strings = strings;
    }

    @NonNull
    @Override
    public ServiceCouponsAdapter.CouponHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceCouponsAdapter.CouponHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_coupons_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceCouponsAdapter.CouponHolder holder, int position) {
        holder.servicCoupon.setText(strings.get(position).getPromoCode());
        holder.description.setText(strings.get(position).getDescription());
        holder.toDate.setText("Expires on : "+strings.get(position).getEnddate());
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        imageLoader.get(strings.get(position).getImagelur(), ImageLoader.getImageListener(holder.networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
        holder.networkImageView.setImageUrl(strings.get(position).getImagelur(), imageLoader);
        holder.offer.setText(strings.get(position).getTitle());
        holder.minVal.setText("Minimum Order : \u20B9 "+Double.parseDouble(strings.get(position).getMinimumval()));
        holder.maxDisount.setText("Max. Discount : \u20B9 "+strings.get(position).getMaxval());

        holder.apply_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddCartfromServices) context).setCoupon(strings.get(position).getPromoCode());
            }
        });

        holder.viewOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_row=position;
                notifyDataSetChanged();
                /*holder.viewOffers.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);*/
            }
        });

        if(index_row==position){
            holder.viewOffers.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.description.setVisibility(View.VISIBLE);
            holder.viewOffers.setTextColor(Color.parseColor("#FF1784C7"));
        }else {
            holder.viewOffers.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.description.setVisibility(View.GONE);
            holder.viewOffers.setTextColor(Color.parseColor("#FF1784C7"));
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.viewOffers.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);
                holder.description.setVisibility(View.GONE);
            }
        });
       /* holder.radioButton.setText(strings.get(position).getPromoCode());
        holder.radioButton.setChecked(lastSelectedPosition == position);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = position;
                notifyDataSetChanged();
                ((AddCartfromServices) context).setCoupon(strings.get(position).getPromoCode());
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class CouponHolder extends RecyclerView.ViewHolder {
        TextView servicCoupon,description,toDate,offer,minVal,maxDisount;
        NetworkImageView networkImageView;
        TextView apply_coupon,viewOffers;
        ImageView imageView;
        //RadioButton radioButton;
        public CouponHolder(@NonNull View itemView) {
            super(itemView);
            minVal = itemView.findViewById(R.id.min_order);
            maxDisount = itemView.findViewById(R.id.max_discount);
            offer = itemView.findViewById(R.id.off);
            imageView = itemView.findViewById(R.id.closedesc);
            viewOffers = itemView.findViewById(R.id.view_offers);
            networkImageView = itemView.findViewById(R.id.coupon_img);
            //radioButton = itemView.findViewById(R.id.promo_coupon);
            servicCoupon = itemView.findViewById(R.id.couponcode);
            description = itemView.findViewById(R.id.description);
            apply_coupon = itemView.findViewById(R.id.applycoupons);
            toDate = itemView.findViewById(R.id.to_date);
        }
    }
}

