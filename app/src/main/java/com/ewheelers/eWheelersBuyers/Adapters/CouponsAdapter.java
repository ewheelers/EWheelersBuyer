package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.CartSummaryActivity;
import com.ewheelers.eWheelersBuyers.ModelClass.PromoCodesModel;
import com.ewheelers.eWheelersBuyers.R;

import java.util.List;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.CouponHolder> {
    Context context;
    List<PromoCodesModel> strings;
    private int lastSelectedPosition = -1;

    public CouponsAdapter(Context context, List<PromoCodesModel> strings) {
        this.context = context;
        this.strings = strings;
    }

    @NonNull
    @Override
    public CouponHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CouponHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.promocodes_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CouponHolder holder, int position) {
        holder.radioButton.setText(strings.get(position).getPromoCode());
        holder.radioButton.setChecked(lastSelectedPosition == position);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = position;
                notifyDataSetChanged();
                ((CartSummaryActivity) context).setCoupon(strings.get(position).getPromoCode());
            }
        });
       /* holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ((CartSummaryActivity)context).setCoupon(strings.get(position).getPromoCode());
                }
            }
        });*/
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
