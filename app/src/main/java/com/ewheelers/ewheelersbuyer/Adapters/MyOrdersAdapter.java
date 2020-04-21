package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.ewheelersbuyer.ModelClass.MyOrdersModel;
import com.ewheelers.ewheelersbuyer.ModelClass.SellerListModel;
import com.ewheelers.ewheelersbuyer.MyOrdersDetails;
import com.ewheelers.ewheelersbuyer.NavAppBarActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.OrdersHolder> {
    Context context;
    List<MyOrdersModel> myOrdersModels;
    //List<MyOrdersModel> ordersFilter;

    int index_row=-1;
    public MyOrdersAdapter(Context context, List<MyOrdersModel> myOrdersModels) {
        this.context = context;
        this.myOrdersModels = myOrdersModels;
       // ordersFilter = new ArrayList<>(myOrdersModels);
    }

    @NonNull
    @Override
    public MyOrdersAdapter.OrdersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_layout, parent, false);
        return new OrdersHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapter.OrdersHolder holder, int position) {
        holder.orderid.setText("Order Id - "+myOrdersModels.get(position).getOrder_id());
        holder.orderamount.setText("\u20B9 " +myOrdersModels.get(position).getOrdernetamount());
        holder.orderdate.setText(myOrdersModels.get(position).getOrderdateadded());
        holder.orderoptions.setText(myOrdersModels.get(position).getOp_selprod_options());
        holder.orderprodname.setText(myOrdersModels.get(position).getOp_product_name());
        holder.orderstatus.setText(myOrdersModels.get(position).getOrderstatus_name());
        holder.ordertitle.setText(myOrdersModels.get(position).getOp_selprod_title());
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        imageLoader.get(myOrdersModels.get(position).getProduct_image_url(), ImageLoader.getImageListener(holder.networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
        holder.networkImageView.setImageUrl(myOrdersModels.get(position).getProduct_image_url(), imageLoader);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_row=position;
                notifyDataSetChanged();
                Intent i = new Intent(context, MyOrdersDetails.class);
                i.putExtra("orderid",myOrdersModels.get(position).getOrder_id());
                context.startActivity(i);
            }
        });

        if(index_row==position){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#9C3C34"));
            holder.orderid.setTextColor(Color.WHITE);
            holder.orderamount.setTextColor(Color.WHITE);
            holder.orderdate.setTextColor(Color.WHITE);
            holder.orderoptions.setTextColor(Color.WHITE);
            holder.orderprodname.setTextColor(Color.WHITE);
            holder.orderstatus.setTextColor(Color.WHITE);
            holder.ordertitle.setTextColor(Color.WHITE);
        }
        else
        {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.orderid.setTextColor(Color.BLACK);
            holder.orderamount.setTextColor(Color.BLACK);
            holder.orderdate.setTextColor(Color.BLACK);
            holder.orderoptions.setTextColor(Color.BLACK);
            holder.orderprodname.setTextColor(Color.BLACK);
            holder.orderstatus.setTextColor(Color.parseColor("#9C3C34"));
            holder.ordertitle.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return myOrdersModels.size();
    }

//    @Override
   /* public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MyOrdersModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(ordersFilter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MyOrdersModel item : ordersFilter) {
                    if (item.getOp_product_name().toLowerCase().contains(filterPattern)||item.getOp_selprod_title().toLowerCase().contains(filterPattern)||item.getOp_selprod_options().toLowerCase().contains(filterPattern)||item.getOrderstatus_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            myOrdersModels.clear();
            myOrdersModels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };*/


    public class OrdersHolder extends RecyclerView.ViewHolder {
        TextView orderid, orderdate, orderprodname, ordertitle, orderoptions, orderstatus, orderamount;
        NetworkImageView networkImageView;
        CardView linearLayout;
        public OrdersHolder(@NonNull View itemView) {
            super(itemView);
            orderid = itemView.findViewById(R.id.orderid);
            orderdate = itemView.findViewById(R.id.orderdate);
            orderprodname = itemView.findViewById(R.id.op_product_name);
            ordertitle = itemView.findViewById(R.id.op_selprod_title);
            orderoptions = itemView.findViewById(R.id.op_selprod_options);
            orderstatus = itemView.findViewById(R.id.orderstatus);
            orderamount = itemView.findViewById(R.id.amount);
            networkImageView = itemView.findViewById(R.id.orderimage);
            linearLayout = itemView.findViewById(R.id.linear);
        }
    }
}
