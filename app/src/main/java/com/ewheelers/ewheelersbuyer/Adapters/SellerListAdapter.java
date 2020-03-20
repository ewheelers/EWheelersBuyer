package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelersbuyer.ModelClass.SellerListModel;
import com.ewheelers.ewheelersbuyer.R;

import java.util.List;

public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.SellHolder> {
    Context context;
    List<SellerListModel> sellerListModels;

    public SellerListAdapter(Context context, List<SellerListModel> sellerListModels) {
        this.context = context;
        this.sellerListModels = sellerListModels;
    }

    @NonNull
    @Override
    public SellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sellers_list_layout, parent, false);
        return new SellHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SellHolder holder, int position) {
        holder.sellerName.setText(sellerListModels.get(position).getSellersname());
        holder.sellerAddress.setText(sellerListModels.get(position).getSellersaddress());
        holder.sellerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.sellerNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return sellerListModels.size();
    }

    public class SellHolder extends RecyclerView.ViewHolder {
        TextView sellerName, sellerAddress;
        ImageView sellerCall, sellerNavigate;

        public SellHolder(@NonNull View itemView) {
            super(itemView);
            sellerName = itemView.findViewById(R.id.seller_name);
            sellerAddress = itemView.findViewById(R.id.seller_address);
            sellerCall = itemView.findViewById(R.id.call_seller);
            sellerNavigate = itemView.findViewById(R.id.navigate_map);

        }
    }
}
