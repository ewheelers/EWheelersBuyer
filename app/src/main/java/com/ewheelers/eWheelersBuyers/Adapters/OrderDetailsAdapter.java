package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.eWheelersBuyers.ModelClass.OrderDetailModelclass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderHolder> {
    Context context;
    List<OrderDetailModelclass> orderDetailModelclasses;

    public OrderDetailsAdapter(Context context, List<OrderDetailModelclass> orderDetailModelclasses) {
        this.context = context;
        this.orderDetailModelclasses = orderDetailModelclasses;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetail_layout,parent,false);
        return new OrderHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        holder.brand.setText("Brand - "+orderDetailModelclasses.get(position).getOrderbrandname());
        holder.date.setText("Date - "+orderDetailModelclasses.get(position).getOrderdate());
        holder.orderid.setText("Order Id - "+orderDetailModelclasses.get(position).getOrderid());
        holder.invoice.setText("Invoice # - "+orderDetailModelclasses.get(position).getOrderinvoice());
        holder.name.setText(orderDetailModelclasses.get(position).getOrdername());
        holder.options.setText(orderDetailModelclasses.get(position).getOrderoptions());
        holder.soldby.setText("Sold by - "+orderDetailModelclasses.get(position).getOrdershopname());
        holder.title.setText(orderDetailModelclasses.get(position).getOrdertitle());
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        imageLoader.get(orderDetailModelclasses.get(position).getOrderimageurl(), ImageLoader.getImageListener(holder.networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
        holder.networkImageView.setImageUrl(orderDetailModelclasses.get(position).getOrderimageurl(), imageLoader);
    }

    @Override
    public int getItemCount() {
        return orderDetailModelclasses.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
    TextView orderid,invoice,date,name,title,brand,options,soldby;
    NetworkImageView networkImageView;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            networkImageView = itemView.findViewById(R.id.ordernetworkimg);
            orderid = itemView.findViewById(R.id.orderid);
            invoice = itemView.findViewById(R.id.invoice);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.ordername);
            title = itemView.findViewById(R.id.ordertitle);
            brand = itemView.findViewById(R.id.orderbrand);
            options = itemView.findViewById(R.id.orderoptions);
            soldby = itemView.findViewById(R.id.ordersoldby);
        }
    }
}
