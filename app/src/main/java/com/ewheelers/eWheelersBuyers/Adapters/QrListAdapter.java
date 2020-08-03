package com.ewheelers.eWheelersBuyers.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.ewheelers.eWheelersBuyers.ModelClass.MyOrdersModel;
import com.ewheelers.eWheelersBuyers.MyOrdersDetails;
import com.ewheelers.eWheelersBuyers.QRPasses;
import com.ewheelers.eWheelersBuyers.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class QrListAdapter extends RecyclerView.Adapter<QrListAdapter.QrHolder> {
    Context context;
    List<MyOrdersModel> qrImages;
    public static int SECONDS_IN_A_DAY = 24 * 60 * 60;
    int index_row = -1;

    public QrListAdapter(Context context, List<MyOrdersModel> qrImages) {
        this.context = context;
        this.qrImages = qrImages;
    }

    @NonNull
    @Override
    public QrHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qr_code_layout, parent, false);
        return new QrHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QrHolder holder, int position) {
        //holder.imageView.setImageBitmap(qrImages.get(position).getBitmap());
        holder.bookedOn.setText("Booked On : " + qrImages.get(position).getOrderdateadded());
        holder.orderId.setText(qrImages.get(position).getOrder_id());
        holder.orderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_row = position;
                notifyDataSetChanged();
                Intent i = new Intent(context, MyOrdersDetails.class);
                i.putExtra("orderid", qrImages.get(position).getOrder_id());
                i.putExtra("vehicleno", qrImages.get(position).getVehicleno());
                context.startActivity(i);
            }
        });

        if(index_row==position){
         holder.orderId.setTextColor(Color.parseColor("#9C3C34"));
        }else {
            holder.orderId.setTextColor(Color.parseColor("#2278d4"));
        }
        String noofqty = qrImages.get(position).getOp_qty();
        holder.imageView.setText(qrImages.get(position).getVehicleno().substring(0,4)+"\n\n"+qrImages.get(position).getVehicleno().substring(4,6)+"\n\n"+qrImages.get(position).getVehicleno().substring(6,10));
        holder.textView.setText(qrImages.get(position).getStatAddress()+"\n\n"+qrImages.get(position).getOp_selprod_title()+"\n\n"+qrImages.get(position).getTimings());

      /*  holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof QRPasses) {
                    ((QRPasses) context).QRPass(qrImages.get(position).getBitmap(), qrImages.get(position).getOrder_id(), holder.textView.getText().toString());
                }
            }
        });*/

        holder.show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.qrpassImage.setVisibility(View.VISIBLE);
                holder.qrpassImage.setImageBitmap(qrImages.get(position).getBitmap());
                holder.close_image.setVisibility(View.VISIBLE);
                holder.show_pass.setVisibility(View.GONE);
            }
        });
        holder.close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.qrpassImage.setVisibility(View.GONE);
                holder.close_image.setVisibility(View.GONE);
                holder.show_pass.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return qrImages.size();
    }

    public class QrHolder extends RecyclerView.ViewHolder {
        TextView imageView, show_pass, bookedOn, orderId;
        TextView textView, timer;
        CardView cardView;
        ImageView qrpassImage, close_image;

        public QrHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            bookedOn = itemView.findViewById(R.id.booked_on);
            close_image = itemView.findViewById(R.id.closeimg);
            qrpassImage = itemView.findViewById(R.id.qr_image);
            show_pass = itemView.findViewById(R.id.showQrPass);
            imageView = itemView.findViewById(R.id.qrImage);
            textView = itemView.findViewById(R.id.qr_title);
            cardView = itemView.findViewById(R.id.card_layout);
            timer = itemView.findViewById(R.id.timer);
        }
    }

}
