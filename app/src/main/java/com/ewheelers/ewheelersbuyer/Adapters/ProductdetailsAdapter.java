package com.ewheelers.ewheelersbuyer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.TooltipCompat;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.ewheelersbuyer.CartActivity;
import com.ewheelers.ewheelersbuyer.CartListingActivity;
import com.ewheelers.ewheelersbuyer.ModelClass.AddonsClass;
import com.ewheelers.ewheelersbuyer.ModelClass.OptionValues;
import com.ewheelers.ewheelersbuyer.ModelClass.ProductDetails;
import com.ewheelers.ewheelersbuyer.ProductDetailActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.tooltip.OnClickListener;
import com.tooltip.Tooltip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class ProductdetailsAdapter extends RecyclerView.Adapter<ProductdetailsAdapter.MyHolder> {
    private Context context;
    private List<ProductDetails> productDetails;
    private ArrayList<OptionValues> items = new ArrayList<>();
    private int optionIdselection;
    int quantity;
    String homeproid;
    JSONObject jsonObject1;
    String addons;
    String tokenvalue;

    public ProductdetailsAdapter(Context context, List<ProductDetails> productDetails) {
        this.context = context;
        this.productDetails = productDetails;
    }

    public ProductdetailsAdapter() {

    }

    @NonNull
    @Override
    public ProductdetailsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case ProductDetails.PREVIEWIMAGES:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_product_layout, parent, false);
                return new MyHolder(v);
            case ProductDetails.OPTIONS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_options_layout, parent, false);
                return new MyHolder(v);
            case ProductDetails.OFFERS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_offers_layout, parent, false);
                return new MyHolder(v);
           /* case ProductDetails.BUYWITH:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buytogether_layout, parent, false);
                return new MyHolder(v);*/
            case ProductDetails.SIMILARPRODUCTS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.similarproducts_layout, parent, false);
                return new MyHolder(v);
            case ProductDetails.BOTTOMEDBUTTONS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottomed_button_layout, parent, false);
                return new MyHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductdetailsAdapter.MyHolder holder, final int position) {

        final int itemType = getItemViewType(position);
        switch (itemType) {
            case ProductDetails.PREVIEWIMAGES:
                final String url = productDetails.get(position).getProductimg_url();
                if (url != null) {
                    ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                    imageLoader.get(url, ImageLoader.getImageListener(holder.networkImageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                    holder.networkImageView.setImageUrl(url, imageLoader);

                    ((ProductDetailActivity) context).onClickcalled(url);

                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((ProductDetailActivity) context).onClickcalled(url);
                        }
                    });
                } else {
                    Toast.makeText(context, "no image to load", Toast.LENGTH_SHORT).show();
                }
                break;
            case ProductDetails.OPTIONS:
                holder.optionnames.setText(productDetails.get(position).getOptionName());
                items = productDetails.get(position).getOptionValuesArrayList();
                //Collections.singletonList(items.toString()
                holder.optionvalues.setAdapter(new ArrayAdapter<OptionValues>(context, android.R.layout.simple_spinner_dropdown_item, items));

                if (context instanceof ProductDetailActivity) {
                    homeproid = ((ProductDetailActivity) context).selctedprod_ID();
                }

                for (int j = 0; j < items.size(); j++) {
                    if (homeproid.equals(items.get(j).getOptionUrlValue())) {
                        holder.optionvalues.setSelection(j);
                    }
                }
                holder.optionvalues.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

               /* holder.viewoptionslayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ViewOptionsActivity.class);
                        i.putParcelableArrayListExtra("optionvaluelist",items);
                        context.startActivity(i);
                    }
                });*/

                break;

          /*  case ProductDetails.BUYWITH:
                holder.buywithname.setText(productDetails.get(position).getBuywithproductname());
                holder.buywithprice.setText("\u20B9 " + productDetails.get(position).getBuywithproductprice());
                String buywithurl = productDetails.get(position).getBuywithimageurl();
                if (buywithurl != null) {
                    ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                    imageLoader.get(buywithurl, ImageLoader.getImageListener(holder.buywithimgIcon, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                    holder.buywithimgIcon.setImageUrl(buywithurl, imageLoader);
                }

                quantity = 1;

                holder.buyWithplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //int count= Integer.parseInt(String.valueOf(holder.txtQuantity.getText()));
                        quantity++;
                        holder.buywithinteger.setText(String.valueOf(quantity));
                        holder.buyWithcheckBox.setChecked(false);
                       *//* if (holder.buyWithcheckBox.isChecked()) {
                            String selbuywithprodid = productDetails.get(position).getButwithselectedProductId();
                            Toast.makeText(context, "selected: " + selbuywithprodid + ":" + holder.buywithinteger.getText().toString(), Toast.LENGTH_SHORT).show();

                        }*//*
                    }
                });

                holder.buyWithminus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = Integer.parseInt(String.valueOf(holder.buywithinteger.getText()));

                        if (quantity > 1)
                            quantity--;
                        holder.buywithinteger.setText(String.valueOf(quantity));
                        holder.buyWithcheckBox.setChecked(false);
                        *//*if (holder.buyWithcheckBox.isChecked()) {
                            String selbuywithprodid = productDetails.get(position).getButwithselectedProductId();
                            Toast.makeText(context, "selected: " + selbuywithprodid + ":" + holder.buywithinteger.getText().toString(), Toast.LENGTH_SHORT).show();
                        }*//*

                    }
                });
                holder.buyWithcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            //jsonObject1 = new JSONObject();

                            try {
                              *//*  for (int i = 0; i < productDetails.size(); i++) {

                                    String selbuywithprodid = productDetails.get(i).getButwithselectedProductId();
                                    jsonObject1.put(selbuywithprodid, String.valueOf(quantity));

                                }

                            *//*
                              for(int i =0;i<productDetails.size();i++) {
                                  if (buttonView.isChecked()) {
                                      String selbuywithprodid = productDetails.get(position).getButwithselectedProductId();
                                      jsonObject1 = new JSONObject();
                                      jsonObject1.put(selbuywithprodid, String.valueOf(quantity));
                                  }
                              }

                                addons = jsonObject1.toString();
                                Log.i("jsonObjectList", jsonObject1.toString());
                                // Toast.makeText(context, "addons: "+addons, Toast.LENGTH_SHORT).show();
                                ((ProductDetailActivity) context).jsonaddons(addons);

                             *//*   Intent intent = new Intent("custom-message");
                                intent.putExtra("jsonaddons", addons);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);*//*

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                    }
                });

                break;*/
            case ProductDetails.SIMILARPRODUCTS:
                holder.pro_name.setText(productDetails.get(position).getSimilarproductname());
                holder.price.setText("\u20B9 " + productDetails.get(position).getSimilarproductprice());
                String similarurl = productDetails.get(position).getSimilarimageurl();
                final String similarprodid = productDetails.get(position).getSimilarproductid();
                if (similarurl != null) {
                    ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
                    imageLoader.get(similarurl, ImageLoader.getImageListener(holder.product_image, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
                    holder.product_image.setImageUrl(similarurl, imageLoader);
                }
                holder.linearLayoutsimilar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* if (context instanceof ProductDetailActivity) {
                            ((ProductDetailActivity)context).getProductDetails(similarprodid);
                        }*/
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("productid", similarprodid);
                        context.startActivity(intent);
                    }
                });
                break;
            case ProductDetails.BOTTOMEDBUTTONS:
                holder.bottomBtn.setText(productDetails.get(position).getButtonText());
                holder.bottomBtn.setBackgroundResource(productDetails.get(position).getButtonBackground());
                if (holder.bottomBtn.getText().toString().equals("Book Now")) {
                    holder.bottomBtn.setTextColor(Color.WHITE);
                }
                if (holder.bottomBtn.getText().toString().equals("Rent")) {
                    holder.bottomBtn.setTextColor(Color.WHITE);
                }
                if (holder.bottomBtn.getText().toString().equals("BUY")) {
                    holder.bottomBtn.setTextColor(Color.WHITE);
                }
                holder.bottomBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (holder.bottomBtn.getText().toString().equals("Test Drive")) {
                            Intent i = new Intent(context, CartActivity.class);
                            i.putExtra("selproductid", String.valueOf(productDetails.get(position).getSelproductid()));
                            i.putExtra("buttontext", String.valueOf(productDetails.get(position).getButtonText()));
                            context.startActivity(i);
                        }
                        if (holder.bottomBtn.getText().toString().equals("Book Now")) {
                          /*  if (context instanceof ProductDetailActivity) {
                                ((ProductDetailActivity) context).addTocart(String.valueOf(productDetails.get(position).getSelproductid()), "book");
                            }*/

                        }
                        if (holder.bottomBtn.getText().toString().equals("Rent")) {

                        }
                       /* if (holder.bottomBtn.getText().toString().equals("BUY")) {
                            if (context instanceof ProductDetailActivity) {
                                ((ProductDetailActivity) context).addTocart(String.valueOf(productDetails.get(position).getSelproductid()), "BUY");
                            }
                        }*/


                    }
                });
                break;
            case ProductDetails.OFFERS:
                holder.icon.setImageResource(productDetails.get(position).getImageicon());
                holder.offertitle.setText(productDetails.get(position).getOffertitle());
                // holder.offercaption.setText(productDetails.get(position).getOffercaption());
                holder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position==1){
                            ((ProductDetailActivity)context).OfferClick(position);
                           /* Intent i = new Intent(context,CartActivity.class);
                            i.putExtra("selproductid", String.valueOf(productDetails.get(position).getSelproductid()));
                            i.putExtra("buttontext", String.valueOf(productDetails.get(position).getOffertitle()));
                            context.startActivity(i);*/
                        }else {
                            Tooltip tooltip = new Tooltip.Builder(v)
                                    .setText("Sorry. No "+productDetails.get(position).getOffertitle()+" Offers Available Now.")
                                    .setTextColor(Color.WHITE)
                                    .setBackgroundColor(Color.parseColor("#9c3c34"))
                                    .setCancelable(true)
                                    .show();
                           /* new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tooltip.dismiss();
                                }
                            },2000);*/
                            //((ProductDetailActivity)context).showSnackbar(productDetails.get(position).getOffertitle());
                        }
                    }
                });
                break;
        }


    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    @Override
    public int getItemViewType(int position) {
        return productDetails.get(position).getTypeoflayout();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        NetworkImageView networkImageView, buywithimgIcon, product_image;
        CardView cardView;
        Spinner optionvalues;
        TextView optionnames;
        ImageView icon;
        TextView offertitle, offercaption, price, pro_name;
       // TextView  buywithname, buywithprice, buywithinteger;
        Button buyWithminus, buyWithplus, bottomBtn;
        LinearLayout linearLayoutsimilar;
       // CheckBox buyWithcheckBox;
        LinearLayout viewoptionslayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            networkImageView = itemView.findViewById(R.id.preview_small_img);
            cardView = itemView.findViewById(R.id.card_click);
            optionnames = itemView.findViewById(R.id.option_name);
            optionvalues = itemView.findViewById(R.id.spinner_view);
            icon = itemView.findViewById(R.id.image_icon);
            offertitle = itemView.findViewById(R.id.title_offer);
            // offercaption = itemView.findViewById(R.id.caption_offer);
            buywithimgIcon = itemView.findViewById(R.id.thumviewimage);
           /* buywithname = itemView.findViewById(R.id.buywith_title);
            buywithprice = itemView.findViewById(R.id.buywith_price);
            buywithinteger = itemView.findViewById(R.id.buywith_integer_number);
            buyWithminus = itemView.findViewById(R.id.buywith_decrease);
            buyWithplus = itemView.findViewById(R.id.buywith_increase);*/

            product_image = itemView.findViewById(R.id.image_view);
            price = itemView.findViewById(R.id.product_price);
            pro_name = itemView.findViewById(R.id.product_name);
            linearLayoutsimilar = itemView.findViewById(R.id.click_similarproduct);
            bottomBtn = itemView.findViewById(R.id.bottom_button);
          //  buyWithcheckBox = itemView.findViewById(R.id.checkoradd);

            viewoptionslayout = itemView.findViewById(R.id.viewoptions_layout);

        }

    }
}
