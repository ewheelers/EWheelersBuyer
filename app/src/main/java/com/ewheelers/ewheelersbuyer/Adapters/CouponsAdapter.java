package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelersbuyer.CartSummaryActivity;
import com.ewheelers.ewheelersbuyer.ModelClass.PromoCodesModel;
import com.ewheelers.ewheelersbuyer.R;

import java.util.List;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.CouponHolder> {
    Context context;
    List<PromoCodesModel> strings;

    public CouponsAdapter(Context context, List<PromoCodesModel> strings) {
        this.context = context;
        this.strings = strings;
    }

    @NonNull
    @Override
    public CouponHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CouponHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.promocodes_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CouponHolder holder, int position) {
        holder.radioButton.setText(strings.get(position).getPromoCode());
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ((CartSummaryActivity)context).setCoupon(strings.get(position).getPromoCode());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class CouponHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        public CouponHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.promo_coupon);
        }
    }
}
