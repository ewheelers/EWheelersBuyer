package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ewheelers.ewheelersbuyer.ModelClass.HomeMenuIcons;
import com.ewheelers.ewheelersbuyer.R;

import java.util.List;

public class MenuIconAdapter extends RecyclerView.Adapter<MenuIconAdapter.MyHolder> {

    Context context;
    List<HomeMenuIcons> homeMenuIcons;

    public MenuIconAdapter(Context context, List<HomeMenuIcons> homeMenuIcons) {
        this.context = context;
        this.homeMenuIcons = homeMenuIcons;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.top_menu_layout,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuIconAdapter.MyHolder holder, int position) {
        holder.imageView.setImageResource(homeMenuIcons.get(position).getMenu_icon());
        holder.textView.setText(homeMenuIcons.get(position).getMenutitle());
    }

    @Override
    public int getItemCount() {
        return homeMenuIcons.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.menu_image);
            textView = itemView.findViewById(R.id.menuTitle);
        }
    }
}
