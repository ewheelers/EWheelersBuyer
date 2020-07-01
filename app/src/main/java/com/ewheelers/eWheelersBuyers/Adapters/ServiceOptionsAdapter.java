package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.ChargeDetailPage;
import com.ewheelers.eWheelersBuyers.ModelClass.OptionValues;
import com.ewheelers.eWheelersBuyers.ModelClass.ProductDetails;
import com.ewheelers.eWheelersBuyers.ProductDetailActivity;
import com.ewheelers.eWheelersBuyers.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceOptionsAdapter extends RecyclerView.Adapter<ServiceOptionsAdapter.ServiceHolder> {
    private Context context;
    private List<ProductDetails> productDetails;
    private ArrayList<OptionValues> items = new ArrayList<>();
    boolean mSpinnerInitialized = true;
    private int selectedItem = 0;

    public ServiceOptionsAdapter(Context context, List<ProductDetails> productDetails) {
        this.context = context;
        this.productDetails = productDetails;
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_options_layout, parent, false);
        return new ServiceHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {
        holder.optionnames.setText(productDetails.get(position).getOptionName());
        items = productDetails.get(position).getOptionValuesArrayList();
        holder.optionvalues.setAdapter(new ArrayAdapter<OptionValues>(context, android.R.layout.simple_spinner_dropdown_item, items));

        ArrayList<ProductDetails> productDetailsSelectOptions = ((ChargeDetailPage) context).productDetailSelectValues();
        Log.i("service_selectList", ((ChargeDetailPage) context).productDetailSelectValues().get(position).getOptionselectid());

        int i;
        String firstselected = null;
        for (i = 0; i < items.size(); i++) {
            Log.i("optionrowsarray ", items.get(i).getOptionValuenames());
            if (items.get(i).getOptionvalueid().equals(productDetailsSelectOptions.get(position).getOptionselectid())) {
                holder.optionvalues.setSelection(i);
                firstselected = items.get(i).getOptionValuenames();
                // Toast.makeText(context, "pics: " + firstselected, Toast.LENGTH_SHORT).show();
            }
        }


        ArrayList<OptionValues> optionValuesData = items;
        String finalFirstselected = firstselected;
        holder.optionvalues.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (!mSpinnerInitialized) {
                    if (!finalFirstselected.equals(optionValuesData.get(pos).getOptionValuenames())) {
                        ((ChargeDetailPage) context).getProductDetails(optionValuesData.get(pos).getOptionUrlValue());
                    }
                }
                mSpinnerInitialized = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    public class ServiceHolder extends RecyclerView.ViewHolder {
        Spinner optionvalues;
        TextView optionnames;

        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            optionnames = itemView.findViewById(R.id.option_name);
            optionvalues = itemView.findViewById(R.id.spinner_view);
        }
    }
}
