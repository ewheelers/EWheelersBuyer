package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.eWheelersBuyers.ModelClass.Comparemodelclass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import java.util.List;

public class ComparatorIndexAdapter extends RecyclerView.Adapter<ComparatorIndexAdapter.CompareHolder> {
    Context context;
    List<Comparemodelclass> comparemodelclassList;

    public ComparatorIndexAdapter(Context context, List<Comparemodelclass> comparemodelclassList) {
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
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.compare_index_layout, parent, false);
                return new CompareHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CompareHolder holder, int position) {
        final int itemType = getItemViewType(position);
        switch (itemType) {
            case Comparemodelclass.head:
                if(position==0){
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.sellerlayout.setVisibility(View.VISIBLE);
                    holder.sellerinfolayout.setVisibility(View.VISIBLE);

                    ImageLoader imageLoader  = VolleySingleton.getInstance(context).getImageLoader();
                    if(comparemodelclassList.get(0).getImageurl()!=null) {
                        imageLoader.get(comparemodelclassList.get(0).getImageurl(), ImageLoader.getImageListener(holder.img2, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                        holder.img2.setImageUrl(comparemodelclassList.get(0).getImageurl(), imageLoader);
                    }if(comparemodelclassList.get(0).getImageurl2()!=null){
                        imageLoader.get(comparemodelclassList.get(0).getImageurl2(), ImageLoader.getImageListener(holder.img3, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                        holder.img3.setImageUrl(comparemodelclassList.get(0).getImageurl2(), imageLoader);
                    }
                    if(comparemodelclassList.get(0).getImageurl3()!=null){
                        imageLoader.get(comparemodelclassList.get(0).getImageurl3(), ImageLoader.getImageListener(holder.img4, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                        holder.img4.setImageUrl(comparemodelclassList.get(0).getImageurl3(), imageLoader);
                    }

                    holder.prodTitle2.setText(comparemodelclassList.get(0).getTitle1());
                    holder.prodTitle3.setText(comparemodelclassList.get(0).getTitle2());
                    holder.prodTitle4.setText(comparemodelclassList.get(0).getTitle3());

                    holder.prod_title_2.setText(comparemodelclassList.get(0).getTitone());
                    holder.prod_title_3.setText(comparemodelclassList.get(0).getTittwo());
                    holder.prod_title_4.setText(comparemodelclassList.get(0).getTitthree());

                    holder.prod_brand_2.setText(comparemodelclassList.get(0).getBrandone());
                    holder.prod_brand_3.setText(comparemodelclassList.get(0).getBrandtwo());
                    holder.prod_brand_4.setText(comparemodelclassList.get(0).getBrandthree());

                    holder.prod_price_2.setText(comparemodelclassList.get(0).getPriceone());
                    holder.prod_price_3.setText(comparemodelclassList.get(0).getPricetwo());
                    holder.prod_price_4.setText(comparemodelclassList.get(0).getPricethree());

                    holder.heading.setText(comparemodelclassList.get(0).getHeading());

                    holder.seller.setText(comparemodelclassList.get(position).getSellertitle());
                    holder.sellerinfo.setText(comparemodelclassList.get(position).getSellerinfo());

                    holder.seler1.setText(comparemodelclassList.get(position).getSeller1());
                    holder.seler2.setText(comparemodelclassList.get(position).getSeller2());
                    holder.seler3.setText(comparemodelclassList.get(position).getSeller3());

                    holder.selerinfo1.setText(comparemodelclassList.get(position).getSellinfo1());
                    holder.selerinfo2.setText(comparemodelclassList.get(position).getSelinfo2());
                    holder.selerinfo3.setText(comparemodelclassList.get(position).getSelinfo3());

                }else {
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.sellerlayout.setVisibility(View.GONE);
                    holder.sellerinfolayout.setVisibility(View.GONE);
                    holder.heading.setText(comparemodelclassList.get(position).getHeading());
                }
                break;
            case Comparemodelclass.sublist:
                holder.spectitle.setText(comparemodelclassList.get(position).getValues());
                holder.prod_one.setText(comparemodelclassList.get(position).getSubvalue());
                holder.prod_two.setText(comparemodelclassList.get(position).getSubvalue2());
                holder.prod_three.setText(comparemodelclassList.get(position).getSubvalue3());

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
        TextView heading,spectitle,prod_one,prod_two,prod_three,prodTitle,prodTitle2,prodTitle3,prodTitle4;
        LinearLayout linearLayout,sellerlayout,sellerinfolayout;
        TextView seller,sellerinfo,seler1,seler2,seler3,selerinfo1,selerinfo2,selerinfo3;
        NetworkImageView img1,img2,img3,img4;
        TextView prod_title_4,prod_title_3,prod_title_2,prod_brand_3,prod_brand_4,prod_brand_2,prod_price_3,prod_price_4,prod_price_2;
        public CompareHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            prod_title_4 = itemView.findViewById(R.id.prod_title_4);
            prod_title_3 = itemView.findViewById(R.id.prod_title_3);
            prod_title_2 = itemView.findViewById(R.id.prod_title_2);

            prod_brand_3 = itemView.findViewById(R.id.prod_brand_3);
            prod_brand_4 = itemView.findViewById(R.id.prod_brand_4);
            prod_brand_2 = itemView.findViewById(R.id.prod_brand_2);

            prod_price_3 = itemView.findViewById(R.id.prod_price_3);
            prod_price_4 = itemView.findViewById(R.id.prod_price_4);
            prod_price_2 = itemView.findViewById(R.id.prod_price_2);

            spectitle = itemView.findViewById(R.id.spectitile);
            prod_one = itemView.findViewById(R.id.prod_one_spec);
            prod_two = itemView.findViewById(R.id.prod_two_spec);
            prod_three = itemView.findViewById(R.id.prod_three_spec);
            prodTitle = itemView.findViewById(R.id.prod_title);
            prodTitle2 = itemView.findViewById(R.id.prod_title_two);
            prodTitle3 = itemView.findViewById(R.id.prod_title_three);
            prodTitle4 = itemView.findViewById(R.id.prod_title_four);
            img1 = itemView.findViewById(R.id.image1);
            img2 = itemView.findViewById(R.id.image2);
            img3 = itemView.findViewById(R.id.image3);
            img4 = itemView.findViewById(R.id.image4);
            linearLayout = itemView.findViewById(R.id.head_tit);
            sellerlayout = itemView.findViewById(R.id.sellerlayout);
            sellerinfolayout = itemView.findViewById(R.id.sellerinfolayout);
            seller = itemView.findViewById(R.id.seller);
            sellerinfo = itemView.findViewById(R.id.sellerinfo);
            seler1 = itemView.findViewById(R.id.seller1);
            seler2 = itemView.findViewById(R.id.seller2);
            seler3 = itemView.findViewById(R.id.seller3);
            selerinfo1 = itemView.findViewById(R.id.sellerinfo1);
            selerinfo2 = itemView.findViewById(R.id.sellerinfo2);
            selerinfo3 = itemView.findViewById(R.id.sellerinfo3);

        }
    }
}

