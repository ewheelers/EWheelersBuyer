package com.ewheelers.ewheelersbuyer.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewheelers.ewheelersbuyer.Adapters.CompareAdapter;
import com.ewheelers.ewheelersbuyer.CompareActivity;
import com.ewheelers.ewheelersbuyer.ModelClass.Comparemodelclass;
import com.ewheelers.ewheelersbuyer.ModelClass.ProductSpecifications;
import com.ewheelers.ewheelersbuyer.ProductDescriptionActivity;
import com.ewheelers.ewheelersbuyer.R;

import java.util.List;


public class ProductAttributesFragment extends Fragment {
    RecyclerView list2;
    CompareAdapter compareAdapter;
    public ProductAttributesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_attributes, container, false);
        list2 = v.findViewById(R.id.listtwo);
        ProductDescriptionActivity productDescriptionActivity = (ProductDescriptionActivity)getActivity();
        List<Comparemodelclass> product_attributes =  productDescriptionActivity.sendAttributes();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        list2.setLayoutManager(linearLayoutManager);
        compareAdapter = new CompareAdapter(getActivity(), product_attributes);
        list2.setAdapter(compareAdapter);
        return v;
    }

}
