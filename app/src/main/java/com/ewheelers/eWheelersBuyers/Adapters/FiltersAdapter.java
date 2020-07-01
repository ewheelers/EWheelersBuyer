package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.Interface.ChipClickListner;
import com.ewheelers.eWheelersBuyers.Interface.colorselectListner;
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
    ArrayList<String> optionids = new ArrayList<>();
    ArrayList<String> shopids = new ArrayList<>();

    public FiltersAdapter(Context context, List<FilterListClass> filterListClassList, List<SubFilterModelClass> subFilterModelClasses) {
        this.context = context;
        this.filterListClassList = filterListClassList;
        this.subFilterModelClasses = subFilterModelClasses;
    }

    public FiltersAdapter(Context context, List<FilterListClass> filterListClassList) {
        this.context = context;
        this.filterListClassList = filterListClassList;
    }

    @NonNull
    @Override
    public FiltersAdapter.FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case FilterListClass.CATHEAD:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_chip_layout, parent, false);
                return new FiltersAdapter.FilterHolder(v);
            case FilterListClass.CATSUB:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_filterchip_layout, parent, false);
                return new FiltersAdapter.FilterHolder(v);
            case FilterListClass.OPTIONS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.options_filter_layout, parent, false);
                return new FiltersAdapter.FilterHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FiltersAdapter.FilterHolder holder, int position) {

        final int itemType = getItemViewType(position);
        switch (itemType) {
            case FilterListClass.CATHEAD:
                holder.textViewcatTitle.setText(filterListClassList.get(position).getCatName());

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                holder.recyclerViewsublist.setLayoutManager(linearLayoutManager);
                ChildrenFiltersAdapter childrenFiltersAdapter = new ChildrenFiltersAdapter(filterListClassList.get(position).getSubFilterModelClasses(), context, new ChipClickListner() {
                    @Override
                    public void setClickListner(String catidadd, String catidremove) {
                        if (catidremove.isEmpty()) {
                            subids.add(catidadd);
                        } else {
                            subids.remove(catidremove);
                        }
                        Log.e("subids", subids.toString());
                        if (context instanceof ShowAlleBikesActivity) {
                            ((ShowAlleBikesActivity) context).applyFilters(subids.toString(), subbrandids.toString(),"","");
                        }
                    }
                });
                holder.recyclerViewsublist.setAdapter(childrenFiltersAdapter);
                break;
            case FilterListClass.CATSUB:
               /* if (position==0) {
                    holder.brandtext.setVisibility(View.VISIBLE);
                } else {
                    holder.brandtext.setVisibility(View.GONE);
                }*/
                holder.chiptxt.setText(filterListClassList.get(position).getBrandname());
                holder.chiptxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            subbrandids.add(filterListClassList.get(position).getBrandid());
                        } else {
                            subbrandids.remove(filterListClassList.get(position).getBrandid());
                        }
                        Log.e("subbrandids", subbrandids.toString());
                        if (context instanceof ShowAlleBikesActivity) {
                            ((ShowAlleBikesActivity) context).applyFilters(subids.toString(), subbrandids.toString(),"","");
                        }

                    }
                });
                break;
            case FilterListClass.OPTIONS:
                holder.textViewcatTitle.setText(filterListClassList.get(position).getOptionname());
                holder.textViewcatTitle.setTextColor(Color.parseColor("#9C3C34"));
                LinearLayoutManager linearLayoutManageropt = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                holder.recyclerViewsublist.setLayoutManager(linearLayoutManageropt);
                ChildrenFiltersAdapter childrenFiltersAdapteropt = new ChildrenFiltersAdapter(filterListClassList.get(position).getSubFilterOptionsModelClasses(), context, new colorselectListner() {
                    @Override
                    public void choosecolor(String colorid, String colorremove) {
                        if (colorremove.isEmpty()) {
                            optionids.add(colorid);
                        } else {
                            optionids.remove(colorremove);
                        }
                        Log.e("optids", optionids.toString());
                        if (context instanceof ShowAlleBikesActivity) {
                            ((ShowAlleBikesActivity) context).applyFilters(subids.toString(), subbrandids.toString(),optionids.toString(),"");
                        }
                    }
                });
                holder.recyclerViewsublist.setAdapter(childrenFiltersAdapteropt);
                break;
        }




    }

    @Override
    public int getItemCount() {
        return filterListClassList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return filterListClassList.get(position).getType();
    }

    public class FilterHolder extends RecyclerView.ViewHolder {
        TextView textViewcatTitle, brandtext;
        Chip chiptxt;
        RecyclerView recyclerViewsublist;

        public FilterHolder(@NonNull View itemView) {
            super(itemView);
            textViewcatTitle = itemView.findViewById(R.id.cattitle);
            chiptxt = itemView.findViewById(R.id.choosechip);
            recyclerViewsublist = itemView.findViewById(R.id.chip_list);
            brandtext = itemView.findViewById(R.id.brandtxt);
        }
    }
}
