package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.ModelClass.Comparemodelclass;
import com.ewheelers.eWheelersBuyers.ModelClass.FilterListClass;
import com.ewheelers.eWheelersBuyers.R;

import java.util.List;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.FilterHolder> {
    Context context;
    List<FilterListClass> filterListClassList;

    public FiltersAdapter(Context context, List<FilterListClass> filterListClassList) {
        this.context = context;
        this.filterListClassList = filterListClassList;
    }

    @NonNull
    @Override
    public FiltersAdapter.FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case Comparemodelclass.head:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_chip_layout, parent, false);
                return new FiltersAdapter.FilterHolder(v);
            case Comparemodelclass.sublist:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_filter_layout, parent, false);
                return new FiltersAdapter.FilterHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FiltersAdapter.FilterHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return filterListClassList.size();
    }

    public class FilterHolder extends RecyclerView.ViewHolder {
        TextView textViewcatTitle;
        public FilterHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
