package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.ModelClass.Comparemodelclass;
import com.ewheelers.eWheelersBuyers.ModelClass.FilterListClass;
import com.ewheelers.eWheelersBuyers.ModelClass.SubFilterModelClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.ShowAlleBikesActivity;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.FilterHolder> {
    Context context;
    List<FilterListClass> filterListClassList;
    List<SubFilterModelClass> subFilterModelClasses;
    ArrayList<String> subids = new ArrayList<>();
    ArrayList<String> subbrandids = new ArrayList<>();

    public FiltersAdapter(Context context, List<FilterListClass> filterListClassList, List<SubFilterModelClass> subFilterModelClasses) {
        this.context = context;
        this.filterListClassList = filterListClassList;
        this.subFilterModelClasses = subFilterModelClasses;
    }


    @NonNull
    @Override
    public FiltersAdapter.FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_chip_layout, parent, false);
        return new FiltersAdapter.FilterHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FiltersAdapter.FilterHolder holder, int position) {


        int typeOf = filterListClassList.get(position).getType();
        if(typeOf==0){
            holder.textViewcatTitle.setText(filterListClassList.get(position).getCatName());
            holder.chiptxt.setText(subFilterModelClasses.get(position).getSubname());

            holder.chiptxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        subids.add(subFilterModelClasses.get(position).getSubcatid());
                    } else {
                        subids.remove(subFilterModelClasses.get(position).getSubcatid());
                    }
                    Log.e("subcatids", subids.toString());
                    if(context instanceof ShowAlleBikesActivity){
                        ((ShowAlleBikesActivity)context).applyFilters(subids.toString(),subbrandids.toString());
                    }

                }
            });
        }
        if(typeOf==1){
            holder.chiptxt.setText(filterListClassList.get(position).getBrandname());
            holder.textViewcatTitle.setVisibility(View.GONE);
            holder.chiptxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        subbrandids.add(filterListClassList.get(position).getBrandid());
                    } else {
                        subbrandids.remove(filterListClassList.get(position).getBrandid());
                    }
                    Log.e("subbrandids", subbrandids.toString());
                    if(context instanceof ShowAlleBikesActivity){
                        ((ShowAlleBikesActivity)context).applyFilters(subids.toString(),subbrandids.toString());
                    }

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return filterListClassList.size();
    }

    public class FilterHolder extends RecyclerView.ViewHolder {
        TextView textViewcatTitle;
        Chip chiptxt;

        public FilterHolder(@NonNull View itemView) {
            super(itemView);
            textViewcatTitle = itemView.findViewById(R.id.cattitle);
            chiptxt = itemView.findViewById(R.id.selchip);
        }
    }
}
