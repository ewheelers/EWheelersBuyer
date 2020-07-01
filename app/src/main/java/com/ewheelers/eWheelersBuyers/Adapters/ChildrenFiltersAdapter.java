package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.Interface.ChipClickListner;
import com.ewheelers.eWheelersBuyers.Interface.colorselectListner;
import com.ewheelers.eWheelersBuyers.ModelClass.FilterListClass;
import com.ewheelers.eWheelersBuyers.ModelClass.SubFilterModelClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.ShowAlleBikesActivity;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class ChildrenFiltersAdapter extends RecyclerView.Adapter<ChildrenFiltersAdapter.ChildHolder> {
    List<SubFilterModelClass> subFilterModelClassList;
    Context context;
    ChipClickListner chipClickListner;
    colorselectListner colorselectListner;
    public ChildrenFiltersAdapter(List<SubFilterModelClass> subFilterModelClassList, Context context, ChipClickListner chipClickListner) {
        this.subFilterModelClassList = subFilterModelClassList;
        this.context = context;
        this.chipClickListner = chipClickListner;
    }
    public ChildrenFiltersAdapter(List<SubFilterModelClass> subFilterModelClassList, Context context, colorselectListner chipClickListner) {
        this.subFilterModelClassList = subFilterModelClassList;
        this.context = context;
        this.colorselectListner = chipClickListner;
    }
    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case SubFilterModelClass.CATGORY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_filterchip_layout, parent, false);
                return new ChildHolder(v);
            case SubFilterModelClass.COLORS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_chip_layout, parent, false);
                return new ChildHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChildHolder holder, int position) {
        final int itemType = getItemViewType(position);
        switch (itemType) {
            case SubFilterModelClass.CATGORY:
                holder.chip.setText(subFilterModelClassList.get(position).getSubname());
                holder.chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            chipClickListner.setClickListner(subFilterModelClassList.get(position).getSubcatid(), "");
                        } else {
                            chipClickListner.setClickListner("", subFilterModelClassList.get(position).getSubcatid());
                        }
                    }
                });
                break;
            case SubFilterModelClass.COLORS:
                holder.chip.setText(subFilterModelClassList.get(position).getOptionvalue_name());
                if(subFilterModelClassList.get(position).getOptionvalue_color_code().isEmpty()){
                    holder.color_chip.setVisibility(GONE);
                }else {
                    holder.color_chip.setBackgroundColor(Color.parseColor("#" + subFilterModelClassList.get(position).getOptionvalue_color_code()));
                }
                holder.chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            colorselectListner.choosecolor(subFilterModelClassList.get(position).getOptionvalue_id(), "");
                        } else {
                            colorselectListner.choosecolor("", subFilterModelClassList.get(position).getOptionvalue_id());
                        }
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return subFilterModelClassList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return subFilterModelClassList.get(position).getTypeOf();
    }


    public class ChildHolder extends RecyclerView.ViewHolder {
        Chip chip;
        Button color_chip;

        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.choosechip);
            color_chip = itemView.findViewById(R.id.colortype);
        }
    }
}
