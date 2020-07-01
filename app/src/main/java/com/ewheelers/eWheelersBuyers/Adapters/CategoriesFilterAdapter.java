package com.ewheelers.eWheelersBuyers.Adapters;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.eWheelersBuyers.ModelClass.FilterListClass;
import com.ewheelers.eWheelersBuyers.R;
import com.ewheelers.eWheelersBuyers.ShowAlleBikesActivity;
import com.ewheelers.eWheelersBuyers.ShowServiceProvidersActivity;
import com.google.android.material.chip.Chip;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CategoriesFilterAdapter extends RecyclerView.Adapter<CategoriesFilterAdapter.CatHolder> {
    Context context;
    private List<FilterListClass> filterListClassList;
    ArrayList<String> strings = new ArrayList<>();
    List<String> keyokey;
    private String fromActivity;
    String models;

    public CategoriesFilterAdapter(Context context, List<FilterListClass> filterListClassList, List<String> keyokey, String fromActivity) {
        this.context = context;
        this.filterListClassList = filterListClassList;
        this.fromActivity = fromActivity;
        this.keyokey = keyokey;
    }

    @NonNull
    @Override
    public CategoriesFilterAdapter.CatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case FilterListClass.SPINNER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_layout, parent, false);
                return new CatHolder(v);
            case FilterListClass.FILTERS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_filterchip_layout, parent, false);
                return new CatHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesFilterAdapter.CatHolder holder, int position) {
        // holder.chiptxt.setTextColor(context.getResources().getColor(R.color.Black));
        final int itemType = getItemViewType(position);
        switch (itemType) {
            case FilterListClass.SPINNER:
                List<String> listdata = new ArrayList<>();
                Iterator iterator1 = filterListClassList.get(position).getJsonObject().keys();
                listdata.add(0, keyokey.get(position));

                while (iterator1.hasNext()) {
                    String keys = (String) iterator1.next();
                    String values = null;
                    try {
                        values = filterListClassList.get(position).getJsonObject().getString(keys);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listdata.add("(" + keys + ") " + values);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listdata);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.autoCompleteTextView.setAdapter(adapter);
                holder.autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(context, "this "+splitString(listdata.get(position)), Toast.LENGTH_SHORT).show();
                        if (fromActivity.equals("Charge")) {
                            if (splitString(listdata.get(position)).equals("")) {

                                if (context instanceof ShowServiceProvidersActivity) {
                                    ((ShowServiceProvidersActivity) context).removeCatFilters();
                                }

                            } else {
                                if (context instanceof ShowServiceProvidersActivity) {
                                    ((ShowServiceProvidersActivity) context).getChargeStations("", splitString(listdata.get(position)), "1", "");
                                }
                            }
                        } else if (fromActivity.equals("Parking")) {
                            //Log.i("parkval: ", listdata.get(position));
                            if (splitString(listdata.get(position)).equals("")) {
                                if (context instanceof ShowServiceProvidersActivity) {
                                    ((ShowServiceProvidersActivity) context).removeParkFilters();
                                }
                            } else {
                                if (context instanceof ShowServiceProvidersActivity) {
                                    ((ShowServiceProvidersActivity) context).getChargeStations("", splitString(listdata.get(position)), "", "1");
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case FilterListClass.FILTERS:
                holder.chiptxt.setText(filterListClassList.get(position).getCatName());
                holder.chiptxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            holder.chiptxt.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            strings.add(filterListClassList.get(position).getCatId());
                        } else {
                            holder.chiptxt.setTextColor(context.getResources().getColor(R.color.Black));
                            strings.remove(filterListClassList.get(position).getCatId());
                        }

                        //if (!strings.isEmpty()) {
                        if (fromActivity.equals("Charge")) {
                            if (strings.isEmpty()) {
                                if (context instanceof ShowServiceProvidersActivity) {
                                    ((ShowServiceProvidersActivity) context).removeCatFilters();
                                }
                            } else {
                                models = TextUtils.join(",", strings);
                                //Log.i("charge", models);
                                if (context instanceof ShowServiceProvidersActivity) {
                                    ((ShowServiceProvidersActivity) context).getChargeStations("", models, "1", "");
                                }
                            }
                        } else if (fromActivity.equals("Parking")) {
                            if (strings.isEmpty()) {
                                if (context instanceof ShowServiceProvidersActivity) {
                                    ((ShowServiceProvidersActivity) context).removeParkFilters();
                                }
                            } else {
                                models = TextUtils.join(",", strings);
                                Log.i("parking", models);

                                if (context instanceof ShowServiceProvidersActivity) {
                                    ((ShowServiceProvidersActivity) context).getChargeStations("", models, "", "1");
                                }
                            }
                        } else {
                            if (strings.isEmpty()) {
                                if (context instanceof ShowAlleBikesActivity) {
                                    ((ShowAlleBikesActivity) context).removeCatFilters("empty");
                                }
                            } else {
                                if (context instanceof ShowAlleBikesActivity) {
                                    ((ShowAlleBikesActivity) context).applyMethodFilters(strings.toString(), "", 0, 0, "");
                                }
                            }
                        }

                    }
                });
                break;


        }


    }

    @Override
    public int getItemCount() {
        return filterListClassList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return filterListClassList.get(position).getType();
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


    public class CatHolder extends RecyclerView.ViewHolder {
        Chip chiptxt, allchiptxt;
        Spinner autoCompleteTextView;

        public CatHolder(@NonNull View itemView) {
            super(itemView);
            chiptxt = itemView.findViewById(R.id.choosechip);
            allchiptxt = itemView.findViewById(R.id.allchip);
            autoCompleteTextView = itemView.findViewById(R.id.languages);

        }

    }
}
