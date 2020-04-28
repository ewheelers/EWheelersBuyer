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
import com.ewheelers.eWheelersBuyers.ModelClass.StatementsModel;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import java.util.List;

public class StatementsAdapter extends RecyclerView.Adapter<StatementsAdapter.StateHolder> {
    Context context;
    List<StatementsModel> statementsModels;

    public StatementsAdapter(Context context, List<StatementsModel> statementsModels) {
        this.context = context;
        this.statementsModels = statementsModels;
    }

    @NonNull
    @Override
    public StatementsAdapter.StateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case StatementsModel.REWARD:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_statements_layout, parent, false);
                return new StateHolder(v);
            case StatementsModel.CREDITS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.credits_statement_layout, parent, false);
                return new StateHolder(v);
            case StatementsModel.COUPONS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupons_layout, parent, false);
                return new StateHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StatementsAdapter.StateHolder holder, int position) {
        final int itemType = getItemViewType(position);
        switch (itemType) {
            case StatementsModel.REWARD:
                holder.adddate.setText(statementsModels.get(position).getAdddate());
                holder.expiry.setText("Expiry Date: "+statementsModels.get(position).getExpiredate());
                holder.comment.setText(statementsModels.get(position).getComment());
                holder.points.setText(statementsModels.get(position).getPoints());
                break;
            case StatementsModel.CREDITS:
                holder.balance.setText("Balance - \u20B9 "+statementsModels.get(position).getBalance());
                holder.date.setText(statementsModels.get(position).getDate());
                holder.txnid.setText("Txn Id - "+statementsModels.get(position).getTxnid());
                holder.credit.setText("\u20B9 "+statementsModels.get(position).getCredit());
                holder.debit.setText("\u20B9 "+statementsModels.get(position).getDebit());
                holder.status.setText(statementsModels.get(position).getStatus());
                holder.comments.setText(statementsModels.get(position).getComments());
                break;
            case StatementsModel.COUPONS:
                holder.coponCode.setText(statementsModels.get(position).getOffercode());
                holder.coponComment.setText(statementsModels.get(position).getOffercomment());
                holder.expiryon.setText(statementsModels.get(position).getOfferexpires());
                holder.minOrder.setText("\u20B9 "+statementsModels.get(position).getOfferminorder());
                holder.coponvalue.setText("\u20B9 "+statementsModels.get(position).getOffervalue()+" Off");
                String url_image = statementsModels.get(position).getOfferimage();
                if (url_image != null) {
                    ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                    imageLoader.get(statementsModels.get(position).getOfferimage(), ImageLoader.getImageListener(holder.networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                    holder.networkImageView.setImageUrl(statementsModels.get(position).getOfferimage(), imageLoader);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return statementsModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return statementsModels.get(position).getTypeLayout();
    }

    public class StateHolder extends RecyclerView.ViewHolder {
    TextView expiry,adddate,comment,points;
    TextView txnid,date,credit,debit,balance,status,comments;
    NetworkImageView networkImageView;
    TextView coponvalue,coponComment,expiryon,minOrder,coponCode;
        public StateHolder(@NonNull View itemView) {
            super(itemView);
            expiry = itemView.findViewById(R.id.expiry);
            adddate = itemView.findViewById(R.id.adddate);
            comment = itemView.findViewById(R.id.comment);
            points = itemView.findViewById(R.id.points);
            txnid = itemView.findViewById(R.id.txnid);
            date = itemView.findViewById(R.id.date);
            credit = itemView.findViewById(R.id.credit);
            debit = itemView.findViewById(R.id.debit);
            balance = itemView.findViewById(R.id.balance);
            status = itemView.findViewById(R.id.status);
            comments = itemView.findViewById(R.id.comments);
            networkImageView = itemView.findViewById(R.id.offerimg);
            coponvalue = itemView.findViewById(R.id.couponValue);
            coponComment = itemView.findViewById(R.id.offercomment);
            expiryon = itemView.findViewById(R.id.expirydate);
            minOrder = itemView.findViewById(R.id.minorder);
            coponCode = itemView.findViewById(R.id.offercode);
        }
    }
}
