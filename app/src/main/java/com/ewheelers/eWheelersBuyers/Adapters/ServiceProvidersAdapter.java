package com.ewheelers.eWheelersBuyers.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.ChargeDetailPage;
import com.ewheelers.eWheelersBuyers.ModelClass.AllebikesModelClass;
import com.ewheelers.eWheelersBuyers.ModelClass.ServiceProvidersClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SPProductsListActivity;
import com.ewheelers.eWheelersBuyers.SessionStorage;
import com.ewheelers.eWheelersBuyers.ShowServiceProvidersActivity;
import com.ewheelers.eWheelersBuyers.UpdateProfileActivity;
import com.ewheelers.eWheelersBuyers.Volley.Apis;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.google.android.gms.common.util.Strings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class ServiceProvidersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context context;
    private List<ServiceProvidersClass> serviceProvidersClassList;
    private String tokenValue;
    private List<ServiceProvidersClass> classeFilter;
    private int index_row = -1;
    boolean upanddown = false;
    private String formatedTime, endtime;
    /*   private final int VIEW_TYPE_ITEM = 0;
       private final int VIEW_TYPE_LOADING = 1;*/
    JSONObject optionno;

    public ServiceProvidersAdapter(Context context, List<ServiceProvidersClass> serviceProvidersClassList) {
        this.context = context;
        this.serviceProvidersClassList = serviceProvidersClassList;
        classeFilter = new ArrayList<>(serviceProvidersClassList);

    }


    @NonNull
    @Override
    public ServiceProvidersAdapter.ServiceProviderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case ServiceProvidersClass.CHARGELAY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chargestation_layout, parent, false);
                return new ServiceProviderHolder(v);
            case ServiceProvidersClass.PARKING:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_layout, parent, false);
                return new ServiceProviderHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final int itemType = getItemViewType(position);
        switch (itemType) {
            case ServiceProvidersClass.CHARGELAY:
                if (holder instanceof ServiceProviderHolder) {
                    populateItemRows2((ServiceProviderHolder) holder, position);
                } else if (holder instanceof LoadingViewHolder) {
                    showLoadingView((LoadingViewHolder) holder, position);
                }
                break;
            case ServiceProvidersClass.PARKING:
                if (holder instanceof ServiceProviderHolder) {
                    populateItemRows2((ServiceProviderHolder) holder, position);
                } else if (holder instanceof LoadingViewHolder) {
                    showLoadingView((LoadingViewHolder) holder, position);
                }
                break;
        }
    }

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    public void call_action(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        context.startActivity(callIntent);
    }

    @Override
    public int getItemCount() {
        return serviceProvidersClassList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return serviceProvidersClassList.get(position).getTypeofLayout();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ServiceProvidersClass> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(classeFilter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ServiceProvidersClass item : classeFilter) {
                    if (item.getServiceprovider_address().toLowerCase().contains(filterPattern) || item.getServiceprovider_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    /*if (item.getTypeofLayout() == 1) {
                        if (item.getServiceprovider_address().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }*/
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            serviceProvidersClassList.clear();
            serviceProvidersClassList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ServiceProviderHolder extends RecyclerView.ViewHolder {
        TextView shop_Name, shop_Address, shopOwner_name, locate, contactno;
        Button mail;
        ImageView call;
        TextView close_img;
        NetworkImageView imageView;
        TextView timingsimg;
        LinearLayout charge_card;
        TextView chargehub;
        TextView listView, open_state;

        public ServiceProviderHolder(@NonNull View itemView) {
            super(itemView);
            open_state = itemView.findViewById(R.id.openstate);
            close_img = itemView.findViewById(R.id.closeimg);
            listView = itemView.findViewById(R.id.list_timings);
            timingsimg = itemView.findViewById(R.id.timings);
            chargehub = itemView.findViewById(R.id.chragehub);
            charge_card = itemView.findViewById(R.id.chargecard);
            imageView = itemView.findViewById(R.id.roundedimage);
            shop_Name = itemView.findViewById(R.id.ShopName);
            shop_Address = itemView.findViewById(R.id.address);
            shopOwner_name = itemView.findViewById(R.id.providerName);
            call = itemView.findViewById(R.id.call);
            mail = itemView.findViewById(R.id.mail);
            locate = itemView.findViewById(R.id.locate);
            contactno = itemView.findViewById(R.id.contact);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows2(ServiceProviderHolder holder, int position) {
        // Toast.makeText(context, "lat:"+latitude+"lon:"+longitude, Toast.LENGTH_SHORT).show();
        String identifier = serviceProvidersClassList.get(position).getUaidentifier();
        StringBuilder stringBuild = new StringBuilder();
        stringBuild.append("(");
        stringBuild.append(identifier);
        stringBuild.append(")");
        String str = serviceProvidersClassList.get(position).getServiceprovider_shopname();
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        holder.shop_Name.setText(cap);
        holder.shopOwner_name.setText(serviceProvidersClassList.get(position).getServiceprovider_name() + stringBuild);
        holder.shop_Address.setText(serviceProvidersClassList.get(position).getServiceprovider_address());
        String mailid = serviceProvidersClassList.get(position).getServiceprovider_emailid();
        holder.contactno.setText("Contact: " + serviceProvidersClassList.get(position).getServiceprovider_phone_number());
        holder.locate.setText(serviceProvidersClassList.get(position).getDistance());
        String openState = serviceProvidersClassList.get(position).getOpenstatus();
        String typetitle = serviceProvidersClassList.get(position).getServiceProviderIs();

        if (openState.equals("1")) {
            holder.open_state.setText("Open");
            holder.open_state.setTextColor(Color.parseColor("#00B300"));
            holder.chargehub.setEnabled(true);
            holder.chargehub.setText("Select");
            holder.chargehub.setTextColor(Color.parseColor("#9C3C34"));
            holder.chargehub.setBackground(context.getResources().getDrawable(R.drawable.button_bg_redtransperent));
            if (index_row == position) {
                holder.chargehub.setBackground(context.getResources().getDrawable(R.drawable.button_bg));
                holder.chargehub.setTextColor(Color.WHITE);
            } else {
                holder.chargehub.setBackground(context.getResources().getDrawable(R.drawable.button_bg_redtransperent));
                holder.chargehub.setTextColor(Color.parseColor("#9C3C34"));
            }
        } else {
            holder.open_state.setText("Close");
            holder.open_state.setTextColor(Color.parseColor("#9C3C34"));
            holder.chargehub.setEnabled(false);
            holder.chargehub.setTextColor(Color.GRAY);
            holder.chargehub.setText("Closed");
            holder.chargehub.setBackground(context.getResources().getDrawable(R.drawable.border_bg));
        }
        String baseurl = "https://ewheelers.in";
        String buywithurl = serviceProvidersClassList.get(position).getImageurl();
        if (buywithurl != null) {
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
            imageLoader.get(baseurl + buywithurl, ImageLoader.getImageListener(holder.imageView, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
            holder.imageView.setImageUrl(baseurl + buywithurl, imageLoader);
        }
        //String formatedTime, endtime;
        String open_time = serviceProvidersClassList.get(position).getOpentime();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            Date dt;
            assert open_time != null;
            if (!open_time.equals("")) {
                dt = sdf.parse(open_time);
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
                assert dt != null;
                formatedTime = sdfs.format(dt);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String close_time = serviceProvidersClassList.get(position).getClosetime();

        try {
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");
            Date dts2;
            assert close_time != null;
            if (!close_time.equals("")) {
                dts2 = sdf2.parse(close_time);
                SimpleDateFormat sdfs2 = new SimpleDateFormat("hh:mm a");
                assert dts2 != null;
                endtime = sdfs2.format(dts2);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String number = "";
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> jsonArray = serviceProvidersClassList.get(position).getStrings();
        for (int j = 0; j < jsonArray.size(); j++) {
            number = jsonArray.get(j);
            if (number.equals("1")) {
                stringBuilder.append("Monday\n");
                stringBuilder.append(formatedTime);
                stringBuilder.append(" - ");
                stringBuilder.append(endtime);
            }
            if (number.equals("2")) {
                stringBuilder.append("\nTuesday\n");
                stringBuilder.append(formatedTime);
                stringBuilder.append(" - ");
                stringBuilder.append(endtime);
            }
            if (number.equals("3")) {
                stringBuilder.append("\nWednesday\n");
                stringBuilder.append(formatedTime);
                stringBuilder.append(" - ");
                stringBuilder.append(endtime);
            }
            if (number.equals("4")) {
                stringBuilder.append("\nThursday\n");
                stringBuilder.append(formatedTime);
                stringBuilder.append(" - ");
                stringBuilder.append(endtime);
            }
            if (number.equals("5")) {
                stringBuilder.append("\nFriday\n");
                stringBuilder.append(formatedTime);
                stringBuilder.append(" - ");
                stringBuilder.append(endtime);
            }
            if (number.equals("6")) {
                stringBuilder.append("\nSatuday\n");
                stringBuilder.append(formatedTime);
                stringBuilder.append(" - ");
                stringBuilder.append(endtime);
            }
            if (number.equals("7")) {
                stringBuilder.append("\nSunday\n");
                stringBuilder.append(formatedTime);
                stringBuilder.append(" - ");
                stringBuilder.append(endtime);
            }
        }
        //Log.i("numeris:",number);
        holder.timingsimg.setVisibility(VISIBLE);
        holder.chargehub.setVisibility(VISIBLE);
        holder.timingsimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.listView.setVisibility(VISIBLE);
                holder.listView.setText(stringBuilder);
                holder.timingsimg.setVisibility(GONE);
                holder.close_img.setVisibility(VISIBLE);
            }
        });

        holder.close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.listView.setVisibility(GONE);
                holder.timingsimg.setVisibility(VISIBLE);
                holder.close_img.setVisibility(GONE);
            }
        });

        holder.chargehub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_row = position;
                notifyDataSetChanged();
               /* if (context instanceof ShowServiceProvidersActivity) {
                    ((ShowServiceProvidersActivity) context).showDiaglog(serviceProvidersClassList.get(position).getSetuid(),serviceProvidersClassList.get(position).getServiceprovider_name());
                }*/

                if (typetitle.equals("Charge")) {
                    Intent intent = new Intent(context, SPProductsListActivity.class);
                    intent.putExtra("uaid", serviceProvidersClassList.get(position).getSetuid());
                    intent.putExtra("name", serviceProvidersClassList.get(position).getServiceprovider_name());
                    intent.putExtra("identifier", serviceProvidersClassList.get(position).getUaidentifier());
                    intent.putExtra("shopid", serviceProvidersClassList.get(position).getShopid());
                    intent.putExtra("distance", serviceProvidersClassList.get(position).getDistance());
                    intent.putExtra("producttype", "3");
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, SPProductsListActivity.class);
                    intent.putExtra("uaid", serviceProvidersClassList.get(position).getSetuid());
                    intent.putExtra("name", serviceProvidersClassList.get(position).getServiceprovider_name());
                    intent.putExtra("identifier", serviceProvidersClassList.get(position).getUaidentifier());
                    intent.putExtra("shopid", serviceProvidersClassList.get(position).getShopid());
                    intent.putExtra("distance", serviceProvidersClassList.get(position).getDistance());
                    if(serviceProvidersClassList.get(position).getJsonServiceObject()!=null) {
                        intent.putExtra("options", serviceProvidersClassList.get(position).getJsonServiceObject().toString());
                    }else {
                        Toast.makeText(context, "options are not loaded please wait", Toast.LENGTH_SHORT).show();
                    }
                    intent.putExtra("producttype", "4");
                    context.startActivity(intent);
                }

            }
        });

      /*  if (index_row == position) {
            holder.chargehub.setBackground(context.getResources().getDrawable(R.drawable.button_bg));
            holder.chargehub.setTextColor(Color.WHITE);
        } else {
            holder.chargehub.setBackground(context.getResources().getDrawable(R.drawable.button_bg_redtransperent));
            holder.chargehub.setTextColor(Color.parseColor("#9C3C34"));
        }*/

         /*   holder.charge_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChargeDetailPage.class);
                    intent.putExtra("brandname", serviceProvidersClassList.get(position).getServiceprovider_name());
                    intent.putExtra("address", serviceProvidersClassList.get(position).getServiceprovider_address());
                    context.startActivity(intent);
                }
            });*/

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    call_action(serviceProvidersClassList.get(position).getServiceprovider_phone_number());
                }
            }
        });

        holder.locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + serviceProvidersClassList.get(position).getCurrentlatitude() + "," + serviceProvidersClassList.get(position).getCurrentlongitude() + "&daddr=" + serviceProvidersClassList.get(position).getServiceprovider_latitude() + "," + serviceProvidersClassList.get(position).getServiceprovider_longitude()));
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);
              /*  String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Float.parseFloat(serviceProvidersClassList.get(position).getServiceprovider_latitude()), Float.parseFloat(serviceProvidersClassList.get(position).getServiceprovider_longitude()));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);*/
            }
        });

    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /*private class MyUndoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, UpdateProfileActivity.class);
            context.startActivity(intent);
        }
    }*/
}
