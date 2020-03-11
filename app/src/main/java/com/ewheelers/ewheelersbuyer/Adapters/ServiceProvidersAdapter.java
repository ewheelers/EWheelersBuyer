package com.ewheelers.ewheelersbuyer.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelersbuyer.ModelClass.ServiceProvidersClass;
import com.ewheelers.ewheelersbuyer.R;

import java.util.List;


public class ServiceProvidersAdapter extends RecyclerView.Adapter<ServiceProvidersAdapter.ServiceProviderHolder> {
    private Context context;
    private List<ServiceProvidersClass> serviceProvidersClassList;

    public ServiceProvidersAdapter(Context context, List<ServiceProvidersClass> serviceProvidersClassList) {
        this.context = context;
        this.serviceProvidersClassList = serviceProvidersClassList;
    }

    @NonNull
    @Override
    public ServiceProvidersAdapter.ServiceProviderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_providers_layout, parent, false);
        return new ServiceProviderHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceProvidersAdapter.ServiceProviderHolder holder, int position) {
        holder.shop_Name.setText(serviceProvidersClassList.get(position).getServiceprovider_shopname());
        holder.shopOwner_name.setText(serviceProvidersClassList.get(position).getServiceprovider_name());
        holder.shop_Address.setText(serviceProvidersClassList.get(position).getServiceprovider_address());
        String mailid = serviceProvidersClassList.get(position).getServiceprovider_emailid();
        String latitude = serviceProvidersClassList.get(position).getServiceprovider_latitude();
        String longitude = serviceProvidersClassList.get(position).getServiceprovider_longitude();
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "cliked call"+serviceProvidersClassList.get(position).getServiceprovider_phone_number(), Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + serviceProvidersClassList.get(position).getServiceprovider_phone_number()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                context.startActivity(callIntent);
            }
        });
        holder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return serviceProvidersClassList.size();
    }

    public class ServiceProviderHolder extends RecyclerView.ViewHolder {
        TextView shop_Name, shop_Address, shopOwner_name,locate;
        Button  mail;
        ImageView call;

        public ServiceProviderHolder(@NonNull View itemView) {
            super(itemView);
            shop_Name = itemView.findViewById(R.id.ShopName);
            shop_Address = itemView.findViewById(R.id.address);
            shopOwner_name = itemView.findViewById(R.id.providerName);
            call = itemView.findViewById(R.id.call);
            mail = itemView.findViewById(R.id.mail);
            locate = itemView.findViewById(R.id.locate);

        }
    }
}
