package com.ewheelers.eWheelersBuyers.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.ShowServiceProvidersActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SpinnerFiltersAdapter extends RecyclerView.Adapter<SpinnerFiltersAdapter.SpinnerHolder> {
    Context context;
    List<JSONObject> spinnerData;
    List<String> keyokey;
    String typeofservice;
    public SpinnerFiltersAdapter(Context context, List<JSONObject> spinnerData, List<String> keyokey,String typeofservice) {
        this.context = context;
        this.spinnerData = spinnerData;
        this.keyokey = keyokey;
        this.typeofservice = typeofservice;
    }

    @NonNull
    @Override
    public SpinnerFiltersAdapter.SpinnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_layout, parent, false);
        return new SpinnerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SpinnerFiltersAdapter.SpinnerHolder holder, int position) {
        List<String> listdata = new ArrayList<>();
        Iterator iterator1 = spinnerData.get(position).keys();
        listdata.add(0,keyokey.get(position));

        while (iterator1.hasNext()) {
            String keys = (String) iterator1.next();
            String values = null;
            try {
                values = spinnerData.get(position).getString(keys);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listdata.add("(" + keys + ") " + values);
        }

        // Log.i("stringdata", listdata.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listdata);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.autoCompleteTextView.setAdapter(adapter);
        holder.autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(context, "this "+splitString(listdata.get(position)), Toast.LENGTH_SHORT).show();
                if(typeofservice.equals("Charge")) {
                    if (context instanceof ShowServiceProvidersActivity) {
                        ((ShowServiceProvidersActivity) context).getChargeStations("", splitString(listdata.get(position)), "1", "");
                    }
                }else if(typeofservice.equals("Parking")){
                    if (context instanceof ShowServiceProvidersActivity) {
                        ((ShowServiceProvidersActivity) context).getChargeStations("", splitString(listdata.get(position)), "", "1");
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return spinnerData.size();
    }


    public class SpinnerHolder extends RecyclerView.ViewHolder {
        Spinner autoCompleteTextView;

        public SpinnerHolder(@NonNull View itemView) {
            super(itemView);
            autoCompleteTextView = itemView.findViewById(R.id.languages);
        }
    }

    public String splitString(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }

        return String.valueOf(num);
    }


}
