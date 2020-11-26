package com.ewheelers.eWheelersBuyers.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewheelers.eWheelersBuyers.Adapters.CompareAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.Comparemodelclass;
import com.ewheelers.eWheelersBuyers.ProductDescriptionActivity;
import com.ewheelers.eWheelersBuyers.R;

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
        ProductDescriptionActivity productDescriptionActivity = (ProductDescriptionActivity) getActivity();
        List<Comparemodelclass> product_attributes = null;
        if (productDescriptionActivity != null) {
            product_attributes = productDescriptionActivity.sendAttributes();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        list2.setLayoutManager(linearLayoutManager);
        compareAdapter = new CompareAdapter(getActivity(), product_attributes);
        list2.setAdapter(compareAdapter);
        compareAdapter.notifyDataSetChanged();
        return v;
    }

}
