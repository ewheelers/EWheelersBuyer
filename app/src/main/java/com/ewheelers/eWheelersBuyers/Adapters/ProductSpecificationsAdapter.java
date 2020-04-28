package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.ModelClass.ProductSpecifications;
import com.ewheelers.eWheelersBuyers.R;

import java.util.List;

public class ProductSpecificationsAdapter extends RecyclerView.Adapter<ProductSpecificationsAdapter.SpecHolder> {
    private Context context;
    private List<ProductSpecifications> productSpecifications;

    public ProductSpecificationsAdapter(Context context, List<ProductSpecifications> productSpecifications) {
        this.context = context;
        this.productSpecifications = productSpecifications;
    }

    @NonNull
    @Override
    public SpecHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.specifications_layout, parent, false);
        return new SpecHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecHolder holder, int position) {
        holder.spec_name.setText(productSpecifications.get(position).getProductspecname());
        holder.spec_value.setText(productSpecifications.get(position).getProductspecvalue());
    }

    @Override
    public int getItemCount() {
        return productSpecifications.size();
    }

    public class SpecHolder extends RecyclerView.ViewHolder {
        TextView spec_name, spec_value;

        public SpecHolder(@NonNull View itemView) {
            super(itemView);
            spec_name = itemView.findViewById(R.id.specName);
            spec_value = itemView.findViewById(R.id.specValue);
        }
    }
}
