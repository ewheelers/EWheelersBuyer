package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.ewheelersbuyer.ModelClass.AddonsClass;
import com.ewheelers.ewheelersbuyer.ProductDetailActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AddonsAdapter extends RecyclerView.Adapter<AddonsAdapter.ViewHolder> {
    private Context context;
    private List<AddonsClass> addonsClasses;
    private int quantity = 1;
    JSONObject jsonObject1 = new JSONObject();;
    String selbuywithprodid;

    public AddonsAdapter(Context context, List<AddonsClass> addonsClasses) {
        this.context = context;
        this.addonsClasses = addonsClasses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buytogether_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.buywithname.setText(addonsClasses.get(position).getBuywithproductname());
        holder.buywithprice.setText("\u20B9 " + addonsClasses.get(position).getBuywithproductprice());
        String buywithurl = addonsClasses.get(position).getBuywithimageurl();
        if (buywithurl != null) {
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
            imageLoader.get(buywithurl, ImageLoader.getImageListener(holder.buywithimgIcon, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
            holder.buywithimgIcon.setImageUrl(buywithurl, imageLoader);
        }

        // quantity = 1;

        holder.buyWithplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int count= Integer.parseInt(String.valueOf(holder.txtQuantity.getText()));
                quantity++;
                holder.buywithinteger.setText(String.valueOf(quantity));

            }
        });

        holder.buyWithminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(String.valueOf(holder.buywithinteger.getText()));

                if (quantity > 1)
                    quantity--;
                holder.buywithinteger.setText(String.valueOf(quantity));
            }
        });


        holder.buyWithcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < addonsClasses.size(); i++) {
                    if (buttonView.isChecked()) {
                        try {
                            selbuywithprodid = addonsClasses.get(position).getButwithselectedProductId();
                            jsonObject1.put(selbuywithprodid, holder.buywithinteger.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        jsonObject1.remove(addonsClasses.get(position).getButwithselectedProductId());
                    }

                }

                //Log.i("jsonObjectList", jsonObject1.toString());
                String addons = jsonObject1.toString();
                ((ProductDetailActivity) context).jsonaddons(addons);
               /* if (context instanceof ProductDetailActivity) {
                    ((ProductDetailActivity) context).jsonaddons(addons);
                }*/

            }

        });


    }

    @Override
    public int getItemCount() {
        return addonsClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox buyWithcheckBox;
        Button buyWithminus, buyWithplus;
        TextView buywithname, buywithprice, buywithinteger;
        NetworkImageView buywithimgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buywithimgIcon = itemView.findViewById(R.id.thumviewimage);
            buywithname = itemView.findViewById(R.id.buywith_title);
            buywithprice = itemView.findViewById(R.id.buywith_price);
            buywithinteger = itemView.findViewById(R.id.buywith_integer_number);
            buyWithminus = itemView.findViewById(R.id.buywith_decrease);
            buyWithplus = itemView.findViewById(R.id.buywith_increase);
            buyWithcheckBox = itemView.findViewById(R.id.checkoradd);

        }
    }
}
