package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.ewheelersbuyer.Interface.ItemClickListener;
import com.ewheelers.ewheelersbuyer.ModelClass.PaymentGatewaysModel;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;

import java.util.List;

public class PaymentGatewayAdapter extends RecyclerView.Adapter<PaymentGatewayAdapter.GatewayHolder> {
    Context context;
    List<PaymentGatewaysModel> paymentGatewaysModelList ;
    int index_row=-1;

    ItemClickListener itemClickListener;

    public PaymentGatewayAdapter(Context context, List<PaymentGatewaysModel> paymentGatewaysModelList,ItemClickListener itemClickListener) {
        this.context = context;
        this.paymentGatewaysModelList = paymentGatewaysModelList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public GatewayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View c = LayoutInflater.from(parent.getContext()).inflate(R.layout.pay_gateway_layout,parent,false);
        return new GatewayHolder(c);
    }

    @Override
    public void onBindViewHolder(@NonNull GatewayHolder holder, int position) {
        holder.castTxt.setText(paymentGatewaysModelList.get(position).getPmethodname());
        String url  = paymentGatewaysModelList.get(position).getImage();
        if(url!=null) {
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
            imageLoader.get(url, ImageLoader.getImageListener(holder.networkImageView, 0, android.R.drawable.ic_dialog_alert));
            holder.networkImageView.setImageUrl(url, imageLoader);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_row=position;
                notifyDataSetChanged();
                itemClickListener.description(paymentGatewaysModelList.get(position).getPmethidid(),paymentGatewaysModelList.get(position).getPmethodcode(),paymentGatewaysModelList.get(position).getPmethoddescription());
            }
        });

        if(index_row==position){
            holder.linearLayout.setBackground(context.getResources().getDrawable(R.drawable.button_bg_transperent));
        }else {
            holder.linearLayout.setBackground(context.getResources().getDrawable(R.drawable.border_bg));
        }

    }

    @Override
    public int getItemCount() {
        return paymentGatewaysModelList.size();
    }

    public class GatewayHolder extends RecyclerView.ViewHolder {
        NetworkImageView networkImageView;
        TextView castTxt;
        LinearLayout linearLayout;
        public GatewayHolder(@NonNull View itemView) {

            super(itemView);
            castTxt = itemView.findViewById(R.id.cash_txt);
            networkImageView = itemView.findViewById(R.id.img);
            linearLayout = itemView.findViewById(R.id.linear);
        }
    }
}
