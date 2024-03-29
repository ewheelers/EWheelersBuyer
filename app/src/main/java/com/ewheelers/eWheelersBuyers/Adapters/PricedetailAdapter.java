package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.ModelClass.PriceDetailsClass;
import com.ewheelers.eWheelersBuyers.R;

import java.util.List;

public class PricedetailAdapter extends RecyclerView.Adapter<PricedetailAdapter.PriceDetailholder> {
    private Context context;
    private List<PriceDetailsClass> priceDetailsClasses;

    public PricedetailAdapter(Context context, List<PriceDetailsClass> priceDetailsClasses) {
        this.context = context;
        this.priceDetailsClasses = priceDetailsClasses;
    }

    @NonNull
    @Override
    public PriceDetailholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pricelistlayout, parent, false);
        return new PriceDetailholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceDetailholder holder, int position) {
        holder.key.setText(priceDetailsClasses.get(position).getKey());
        holder.value.setText(priceDetailsClasses.get(position).getValue());
        if(holder.key.getText().toString().equals("Discount")){
            holder.key.setTextColor(Color.parseColor("#00B300"));
            holder.value.setTextColor(Color.parseColor("#00B300"));
        }
    }

    @Override
    public int getItemCount() {
        return priceDetailsClasses.size();
    }

    public class PriceDetailholder extends RecyclerView.ViewHolder {
        TextView key, value;

        public PriceDetailholder(@NonNull View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.key);
            value = itemView.findViewById(R.id.value);

        }
    }
}
