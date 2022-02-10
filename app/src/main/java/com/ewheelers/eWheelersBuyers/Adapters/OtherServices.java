package com.ewheelers.eWheelersBuyers.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.ewheelers.eWheelersBuyers.SessionStorage;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class OtherServices extends RecyclerView.Adapter<OtherServices.OtherHolders> implements Filterable {
    private Context context;
    private List<ServiceProvidersClass> serviceProvidersClassList;
    private List<ServiceProvidersClass> classeFilter;

    public OtherServices(Context context, List<ServiceProvidersClass> serviceProvidersClassList) {
        this.context = context;
        this.serviceProvidersClassList = serviceProvidersClassList;
        classeFilter = new ArrayList<>(serviceProvidersClassList);
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
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert manager != null;
                manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                dialog.setContentView(R.layout.edit_number_layout);
                TextView textViewClose = dialog.findViewById(R.id.close);
                TextView txtview = dialog.findViewById(R.id.text_change);
                EditText editText = dialog.findViewById(R.id.txt_dia);
                TextView edit_txt = dialog.findViewById(R.id.edit);
                TextView continue_dia = dialog.findViewById(R.id.proceed);
                String buyer_ph = new SessionStorage().getStrings(context, SessionStorage.mobileno);
                if (buyer_ph == null || buyer_ph.equals("")) {
                    txtview.setText("Please give your's contact number. Service Provider will reach you");
                    editText.setText("");
                } else {
                    txtview.setText("Is this your's contact number ? If not you can change here, so that Service Provider will reach you");
                    editText.setText(buyer_ph);
                }
                textViewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                });
                edit_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setEnabled(true);
                        editText.setTextColor(Color.WHITE);
                        editText.setBackgroundColor(R.drawable.background_drawable);
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.showSoftInput(v,InputMethodManager.SHOW_FORCED);
                    }
                });
                continue_dia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().toString().isEmpty()) {
                            editText.setError("Enter Contact Number");
                        } else {
                            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert manager != null;
                            manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            dialog.dismiss();
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                            builder.setTitle("Send Via ...");
                            builder.setMessage("Use SMS or Watsapp to send request to have service. If the service provider not having Watsapp account, Please try to request with SMS.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("via SMS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String text = "eWheelers Customer Requesting Roadside Assistance. Contact No: "+ editText.getText().toString() + "\n\n My Location is: " + "http://maps.google.com/maps?daddr=" + serviceProvidersClassList.get(position).getCurrentlatitude() + "," + serviceProvidersClassList.get(position).getCurrentlongitude();
                                    Uri uri = Uri.parse("smsto:" + serviceProvidersClassList.get(position).getServiceprovider_phone_number());
                                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                                    intent.putExtra("sms_body", text);
                                    context.startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("via Watsapp", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    openWhatsApp(serviceProvidersClassList.get(position).getServiceprovider_phone_number(), position,editText.getText().toString());
                                }
                            });
                            builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    builder.setCancelable(true);
                                }
                            });
                            builder.show();
                        }
                    }
                });
                dialog.show();


               /* if (!serviceProvidersClassList.isEmpty()||serviceProvidersClassList!=null) {
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
                }*/

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

    public void openWhatsApp(String ph_no, int position, String contct) {
        String text = "eWheelers Customer Requesting Roadside Assistance. Contact No: " +contct+ "\n\n My Location is: " + "http://maps.google.com/maps?daddr=" + serviceProvidersClassList.get(position).getCurrentlatitude() + "," + serviceProvidersClassList.get(position).getCurrentlongitude();
        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        try {
            String url = "https://api.whatsapp.com/send?phone=91" + ph_no + "&text=" + URLEncoder.encode(text, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            } else {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle("Use Message app ...");
                builder.setMessage("Wats app not installed in Your Mobile. Use Message app instead of Watsapp !");
                builder.setCancelable(false);
                builder.setPositiveButton("Open SMS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("smsto:" + ph_no);
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra("sms_body", text);
                        context.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                    }
                });
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return serviceProvidersClassList.size();
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
                    if (item.getServiceprovider_address().toLowerCase().contains(filterPattern) || item.getServiceprovider_name().toLowerCase().contains(filterPattern) || item.getServiceprovider_shopname().toLowerCase().contains(filterPattern) || item.getServiceprovider_phone_number().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
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


}
