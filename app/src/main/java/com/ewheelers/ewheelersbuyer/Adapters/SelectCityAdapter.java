package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelersbuyer.ModelClass.SelectCitiesModel;
import com.ewheelers.ewheelersbuyer.NavAppBarActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SelectCityActivity;
import com.ewheelers.ewheelersbuyer.SessionStorage;

import java.util.List;

public class SelectCityAdapter extends RecyclerView.Adapter<SelectCityAdapter.CityHolder> {
    Context context;
    List<SelectCitiesModel> selectCitiesModelList;
    int index_row=-1;
    public SelectCityAdapter(Context context, List<SelectCitiesModel> selectCitiesModelList) {
        this.context = context;
        this.selectCitiesModelList = selectCitiesModelList;
    }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cities_select_layout, parent, false);
        return new CityHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CityHolder holder, int position) {
        holder.cityname.setText(selectCitiesModelList.get(position).getCityname());
        holder.imageView.setBackgroundResource(selectCitiesModelList.get(position).getCityicon());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_row=position;
                notifyDataSetChanged();
            }
        });

        if(index_row==position){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#9C3C34"));
            holder.cityname.setTextColor(Color.parseColor("#ffffff"));
            //Toast.makeText(context, "Selected "+selectCitiesModelList.get(position).getCityname(), Toast.LENGTH_SHORT).show();
           // ((SelectCityActivity) context).setSnackbar(selectCitiesModelList.get(position).getCityname());
           /* new SessionStorage().clearString(context,SessionStorage.location);
            new SessionStorage().saveString(context,SessionStorage.location,selectCitiesModelList.get(position).getCityname());*/
            Intent i = new Intent(context,NavAppBarActivity.class);
            //i.putExtra("city",selectCitiesModelList.get(position).getCityname());
            new SessionStorage().clearString(context,SessionStorage.location);
            new SessionStorage().saveString(context,SessionStorage.location,selectCitiesModelList.get(position).getCityname());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }
        else
        {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.cityname.setTextColor(Color.parseColor("#000000"));
        }


    }

    @Override
    public int getItemCount() {
        return selectCitiesModelList.size();
    }

    public class CityHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView cityname;
        LinearLayout linearLayout;

        public CityHolder(@NonNull View itemView) {
            super(itemView);
            cityname = itemView.findViewById(R.id.city_name);
            imageView = itemView.findViewById(R.id.city_img);
            linearLayout = itemView.findViewById(R.id.layout_select);
        }
    }
}
