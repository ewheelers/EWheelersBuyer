package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.ModelClass.Comparemodelclass;
import com.ewheelers.eWheelersBuyers.R;

import java.util.List;

public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.CompareHolder> {
    Context context;
    List<Comparemodelclass> comparemodelclassList;

    public CompareAdapter(Context context, List<Comparemodelclass> comparemodelclassList) {
        this.context = context;
        this.comparemodelclassList=comparemodelclassList;
    }

    @NonNull
    @Override
    public CompareHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case Comparemodelclass.head:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.compare_attribute_layout, parent, false);
                return new CompareHolder(v);
            case Comparemodelclass.sublist:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sublist_compare_layout, parent, false);
                return new CompareHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CompareHolder holder, int position) {
        final int itemType = getItemViewType(position);
        switch (itemType) {
            case Comparemodelclass.head:
                holder.heading.setText(comparemodelclassList.get(position).getHeading());
                holder.linearLayout.setVisibility(View.GONE);
                holder.sellerinfolayout.setVisibility(View.GONE);
                holder.sellerlayout.setVisibility(View.GONE);
                break;
            case Comparemodelclass.sublist:
                holder.sublisting.setText(comparemodelclassList.get(position).getValues());
                holder.sublistvalue.setText(comparemodelclassList.get(position).getSubvalue());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return comparemodelclassList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return comparemodelclassList.get(position).getTypeofLay();
    }


    public class CompareHolder extends RecyclerView.ViewHolder {
        TextView heading,sublisting,sublistvalue;
        LinearLayout linearLayout,sellerlayout,sellerinfolayout;

        public CompareHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            sublisting = itemView.findViewById(R.id.sublist);
            sublistvalue = itemView.findViewById(R.id.sublistvalue);
            linearLayout = itemView.findViewById(R.id.head_tit);
            sellerlayout = itemView.findViewById(R.id.sellerlayout);
            sellerinfolayout = itemView.findViewById(R.id.sellerinfolayout);
        }
    }
}
