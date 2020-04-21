package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.icu.text.Edits;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelersbuyer.ModelClass.Comparemodelclass;
import com.ewheelers.ewheelersbuyer.ModelClass.SuggestionModel;
import com.ewheelers.ewheelersbuyer.R;

import java.util.ArrayList;
import java.util.Iterator;
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
                break;
            case Comparemodelclass.sublist:
                holder.sublisting.setText(comparemodelclassList.get(position).getValues());
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
        TextView heading,sublisting;

        public CompareHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            sublisting = itemView.findViewById(R.id.sublist);
        }
    }
}
