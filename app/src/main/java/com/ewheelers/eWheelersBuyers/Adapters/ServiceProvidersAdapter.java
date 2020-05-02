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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.ModelClass.AllebikesModelClass;
import com.ewheelers.eWheelersBuyers.ModelClass.ServiceProvidersClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.SessionStorage;
import com.ewheelers.eWheelersBuyers.UpdateProfileActivity;
import com.ewheelers.eWheelersBuyers.Volley.Apis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ServiceProvidersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context context;
    private List<ServiceProvidersClass> serviceProvidersClassList;
    private String tokenValue;
    List<ServiceProvidersClass> classeFilter;

 /*   private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;*/

    public ServiceProvidersAdapter(Context context, List<ServiceProvidersClass> serviceProvidersClassList) {
        this.context = context;
        this.serviceProvidersClassList = serviceProvidersClassList;
        classeFilter = new ArrayList<>(serviceProvidersClassList);

    }



    @NonNull
    @Override
    public ServiceProvidersAdapter.ServiceProviderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       /* if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_providers_layout, parent, false);
            return new ServiceProviderHolder(v);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loadinglayout, parent, false);
            return new ServiceProviderHolder(view);
        }*/

        View v = null;
        switch (viewType) {
            case ServiceProvidersClass.CHARGELAY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chargestation_layout, parent, false);
                return new ServiceProviderHolder(v);
            case ServiceProvidersClass.OTHERLAY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_providers_layout, parent, false);
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
            case ServiceProvidersClass.OTHERLAY:
                if (holder instanceof ServiceProviderHolder) {
                    populateItemRows((ServiceProviderHolder) holder, position);
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


  /*  @Override
    public int getItemCount() {
        return serviceProvidersClassList.size();
    }*/

   /* @Override
    public int getItemCount() {
        return serviceProvidersClassList == null ? 0 : serviceProvidersClassList.size();
    }*/

  /*  @Override
    public int getItemViewType(int position) {
        return serviceProvidersClassList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }*/

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
                    if(item.getTypeofLayout()==1){
                        if (item.getServiceprovider_address().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
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
        ImageView imageView;
        public ServiceProviderHolder(@NonNull View itemView) {
            super(itemView);
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


    private void populateItemRows(ServiceProviderHolder holder, int position) {
        // Toast.makeText(context, "lat:"+latitude+"lon:"+longitude, Toast.LENGTH_SHORT).show();
        String str = serviceProvidersClassList.get(position).getServiceprovider_shopname();
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        holder.shop_Name.setText(cap);
        holder.shopOwner_name.setText(serviceProvidersClassList.get(position).getServiceprovider_name());
        holder.shop_Address.setText(serviceProvidersClassList.get(position).getServiceprovider_address());
        String mailid = serviceProvidersClassList.get(position).getServiceprovider_emailid();
        holder.contactno.setText("Contact: " + serviceProvidersClassList.get(position).getServiceprovider_phone_number());
        holder.locate.setText(serviceProvidersClassList.get(position).getDistance());


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
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.send_request_layout, viewGroup, false);
                TextView textView = dialogView.findViewById(R.id.msgtext);
                LinearLayout linearLayout = dialogView.findViewById(R.id.alert_layout);
                Button button = dialogView.findViewById(R.id.watsapp);
                TextView cancelbutton = dialogView.findViewById(R.id.cancel_btn);
                textView.setText("Sending Message to request\n" + serviceProvidersClassList.get(position).getServiceProviderIs());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //openWhatsApp(serviceProvidersClassList.get(position).getServiceprovider_phone_number());
                        tokenValue = new SessionStorage().getStrings(context, SessionStorage.tokenvalue);

                        String url_link = Apis.profileInfo;
                        final RequestQueue queue = Volley.newRequestQueue(context);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String msg = jsonObject.getString("msg");
                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");
                                    JSONObject jsonObjectProfileInfo = jsonObjectData.getJSONObject("personalInfo");
                                    String user_name = jsonObjectProfileInfo.getString("user_name");
                                    String user_phone = jsonObjectProfileInfo.getString("user_phone");
                                    String user_profile_info = jsonObjectProfileInfo.getString("user_profile_info");
                                    String user_regdate = jsonObjectProfileInfo.getString("user_regdate");
                                    String credential_username = jsonObjectProfileInfo.getString("credential_username");
                                    String credential_email = jsonObjectProfileInfo.getString("credential_email");
                                    String user_dob = jsonObjectProfileInfo.getString("user_dob");
                                    String user_address1 = jsonObjectProfileInfo.getString("user_address1");
                                    String user_address2 = jsonObjectProfileInfo.getString("user_address2");
                                    String user_city = jsonObjectProfileInfo.getString("user_city");
                                    String user_referral_code = jsonObjectProfileInfo.getString("user_referral_code");
                                    String user_order_tracking_url = jsonObjectProfileInfo.getString("user_order_tracking_url");
                                    String user_img_updated_on = jsonObjectProfileInfo.getString("user_img_updated_on");
                                    String country_name = jsonObjectProfileInfo.getString("country_name");
                                    String state_name = jsonObjectProfileInfo.getString("state_name");
                                    String userImage = jsonObjectProfileInfo.getString("userImage");


                                    // private TextView PrivacyPolicy, Edit, Bank, Faq;
                                   /* if (user_phone.isEmpty()) {
                                        Snackbar mySnackbar = Snackbar.make(dialogView.findViewById(R.id.alert_layout),
                                                "Update your Contact number", Snackbar.LENGTH_SHORT);
                                        mySnackbar.setAction("Update", new MyUndoListener());
                                        mySnackbar.show();
                                    }else {
                                        onShareClick(v, serviceProvidersClassList.get(position).getServiceProviderIs(), serviceProvidersClassList.get(position).getServiceprovider_phone_number(), latitude, longitude, user_name, user_phone);
                                    }*/

                                    onShareClick(v, serviceProvidersClassList.get(position).getServiceProviderIs(), serviceProvidersClassList.get(position).getServiceprovider_phone_number(), serviceProvidersClassList.get(position).getCurrentlatitude(), serviceProvidersClassList.get(position).getCurrentlongitude(), user_name, user_phone);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("Main", "Error: " + error.getMessage());
                                Log.d("Main", "" + error.getMessage() + "," + error.toString());

                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("X-TOKEN", tokenValue);
                                return params;
                            }

                            @Override
                            public Map<String, String> getParams() {
                                return null;
                            }

                        };
                        // Add the realibility on the connection.
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                        queue.add(stringRequest);
                    }
                });
                cancelbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        holder.locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Float.parseFloat(serviceProvidersClassList.get(position).getServiceprovider_latitude()), Float.parseFloat(serviceProvidersClassList.get(position).getServiceprovider_longitude()));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);
            }
        });

    }

    private void populateItemRows2(ServiceProviderHolder holder, int position) {
        // Toast.makeText(context, "lat:"+latitude+"lon:"+longitude, Toast.LENGTH_SHORT).show();
        String str = serviceProvidersClassList.get(position).getServiceprovider_shopname();
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        holder.shop_Name.setText(cap);
        holder.shopOwner_name.setText(serviceProvidersClassList.get(position).getServiceprovider_name());
        holder.shop_Address.setText(serviceProvidersClassList.get(position).getServiceprovider_address());
        String mailid = serviceProvidersClassList.get(position).getServiceprovider_emailid();
        holder.contactno.setText("Contact: " + serviceProvidersClassList.get(position).getServiceprovider_phone_number());
        holder.locate.setText(serviceProvidersClassList.get(position).getDistance());

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
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Float.parseFloat(serviceProvidersClassList.get(position).getServiceprovider_latitude()), Float.parseFloat(serviceProvidersClassList.get(position).getServiceprovider_longitude()));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);
            }
        });

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


    private void openWhatsApp(String smsNumber) {
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

          /*  Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
            sendIntent.setPackage("com.whatsapp");
            context.startActivity(sendIntent);*/

           /* Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
            context.startActivity(sendIntent);*/
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(context, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            context.startActivity(goToMarket);
        }
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

    private class MyUndoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, UpdateProfileActivity.class);
            context.startActivity(intent);
        }
    }
}