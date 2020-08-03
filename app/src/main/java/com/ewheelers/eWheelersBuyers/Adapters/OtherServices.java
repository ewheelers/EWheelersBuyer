package com.ewheelers.eWheelersBuyers.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.eWheelersBuyers.ModelClass.ServiceProvidersClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SPProductsListActivity;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class OtherServices extends RecyclerView.Adapter<OtherServices.OtherHolders> {
    private Context context;
    private List<ServiceProvidersClass> serviceProvidersClassList;

    public OtherServices(Context context, List<ServiceProvidersClass> serviceProvidersClassList) {
        this.context = context;
        this.serviceProvidersClassList = serviceProvidersClassList;
    }

    @NonNull
    @Override
    public OtherHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_providers_layout, parent, false);
        return new OtherHolders(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherHolders holder, int position) {
        String str = serviceProvidersClassList.get(position).getServiceprovider_shopname();
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        holder.shop_Name.setText(cap);
        holder.shopOwner_name.setText(serviceProvidersClassList.get(position).getServiceprovider_name());
        holder.shop_Address.setText(serviceProvidersClassList.get(position).getServiceprovider_address());
        String mailid = serviceProvidersClassList.get(position).getServiceprovider_emailid();
        holder.contactno.setText("Contact: " + serviceProvidersClassList.get(position).getServiceprovider_phone_number());
        holder.locate.setText(serviceProvidersClassList.get(position).getDistance());

        String baseurl = "https://ewheelers.in";
        String buywithurl = serviceProvidersClassList.get(position).getLogo();
        if (buywithurl != null) {
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
            imageLoader.get(baseurl + buywithurl, ImageLoader.getImageListener(holder.netImage, R.drawable.ic_dashboard_black_24dp, android.R.drawable.ic_dialog_alert));
            holder.netImage.setImageUrl(baseurl + buywithurl, imageLoader);
        }

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    call_action(serviceProvidersClassList.get(position).getServiceprovider_phone_number());
                }
            }
        });

        holder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openWhatsApp(serviceProvidersClassList.get(position).getServiceprovider_phone_number());
                if (!serviceProvidersClassList.isEmpty()||serviceProvidersClassList!=null) {
                    Intent intent = new Intent(context, SPProductsListActivity.class);
                    //intent.putExtra("uaid", serviceProvidersClassList.get(position).getSetuid());
                    intent.putExtra("name", serviceProvidersClassList.get(position).getServiceprovider_shopname());
                    //intent.putExtra("identifier", serviceProvidersClassList.get(position).getUaidentifier());
                    intent.putExtra("shopid", serviceProvidersClassList.get(position).getShopid());
                    intent.putExtra("distance", serviceProvidersClassList.get(position).getDistance());
                    //if(serviceProvidersClassList.get(position).getServiceProviderIs().equals("Repair")){}
                    intent.putExtra("producttype", "4");
                    intent.putExtra("options", serviceProvidersClassList.get(position).getJsonServiceObject().toString());
                    intent.putExtra("serviceprovider", serviceProvidersClassList.get(position).getServiceProviderIs());
                    context.startActivity(intent);
                }
            }
        });

        holder.locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + serviceProvidersClassList.get(position).getCurrentlatitude() + "," + serviceProvidersClassList.get(position).getCurrentlongitude() + "&daddr=" + serviceProvidersClassList.get(position).getServiceprovider_latitude() + "," + serviceProvidersClassList.get(position).getServiceprovider_longitude()));
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);

               /* String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Float.parseFloat(serviceProvidersClassList.get(position).getServiceprovider_latitude()), Float.parseFloat(serviceProvidersClassList.get(position).getServiceprovider_longitude()));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceProvidersClassList.size();
    }

    public class OtherHolders extends RecyclerView.ViewHolder {
        NetworkImageView netImage;
        TextView shop_Name, shop_Address, shopOwner_name, locate, contactno;
        Button mail;
        ImageView call;

        public OtherHolders(@NonNull View itemView) {
            super(itemView);
            netImage = itemView.findViewById(R.id.net_img);
            shop_Name = itemView.findViewById(R.id.ShopName);
            shop_Address = itemView.findViewById(R.id.address);
            shopOwner_name = itemView.findViewById(R.id.providerName);
            call = itemView.findViewById(R.id.call);
            mail = itemView.findViewById(R.id.mail);
            locate = itemView.findViewById(R.id.locate);
            contactno = itemView.findViewById(R.id.contact);
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

    public void onShareClick(View v, String servicename, String number, double lat, double lon, String username, String usermobile) {
        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!resInfos.isEmpty()) {
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName + number);
                if (packageName.contains("com.whatsapp") || packageName.contains("com.android.mms")) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    if (packageName.equals("com.whatsapp")) {
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, username + " Requesting for " + servicename + " Service\n" + context.getResources().getString(R.string.contact) + " : " + usermobile + "\nLocate: " + "https://maps.google.com/?q=" + lat + "," + lon);
                        intent.putExtra("jid", "91" + number + "@s.whatsapp.net");
                    }

                    if (packageName.equals("com.android.mms")) {
                        //intent = new Intent(Intent.ACTION_VIEW);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("smsto:"));
                        intent.setType("vnd.android-dir/mms-sms");
                        intent.putExtra("address", number);
                        intent.putExtra("sms_body", username + " Requesting for " + servicename + " Service\n" + context.getResources().getString(R.string.contact) + " : " + usermobile + "\nLocate: " + "https://maps.google.com/?q=" + lat + "," + lon);
                    }

                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                context.startActivity(chooserIntent);
            } else {
                Toast.makeText(context, "Do not Have Intent", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
