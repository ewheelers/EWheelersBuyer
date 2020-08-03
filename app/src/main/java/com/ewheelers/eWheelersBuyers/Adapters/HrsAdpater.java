package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.ChargeDetailPage;
import com.ewheelers.eWheelersBuyers.R;

import java.util.List;

public class HrsAdpater extends RecyclerView.Adapter<HrsAdpater.HrsHolder> {
    Context context;
    List<String> strings;
    int indexrow = 0;

    public HrsAdpater(Context context, List<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    @NonNull
    @Override
    public HrsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hrs_layout,parent,false);
        return new HrsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HrsHolder holder, int position) {
    holder.textView.setText(strings.get(position));

    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            indexrow=position;
            notifyDataSetChanged();
            if(context instanceof ChargeDetailPage){
                ((ChargeDetailPage) context).setTimeofHrs(position+1);
            }
        }
    });

        if(indexrow==position){
            holder.textView.setBackgroundColor(Color.parseColor("#9C3C34"));
            holder.textView.setTextColor(Color.WHITE);
            if(context instanceof ChargeDetailPage){
                ((ChargeDetailPage) context).setTimeofHrs(position+1);

            }
        }
        else
        {
            holder.textView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.textView.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class HrsHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout linearLayout;
        public HrsHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.hrsno);
            linearLayout = itemView.findViewById(R.id.lay_hr);
        }
    }
}
