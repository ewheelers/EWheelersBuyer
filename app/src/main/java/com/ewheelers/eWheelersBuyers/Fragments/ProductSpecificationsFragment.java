package com.ewheelers.eWheelersBuyers.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewheelers.eWheelersBuyers.Adapters.ProductSpecificationsAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.ProductSpecifications;
import com.ewheelers.eWheelersBuyers.ProductDescriptionActivity;
import com.ewheelers.eWheelersBuyers.R;

import java.util.List;


public class ProductSpecificationsFragment extends Fragment {
    RecyclerView recyclerView;
    ProductSpecificationsAdapter productSpecificationsAdapter;
    public ProductSpecificationsFragment() {
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
        View v = inflater.inflate(R.layout.fragment_product_specifications, container, false);
        recyclerView = v.findViewById(R.id.specifications_list);
        ProductDescriptionActivity productDescriptionActivity = (ProductDescriptionActivity)getActivity();
        List<ProductSpecifications> productSpecifications =  productDescriptionActivity.sendSpecification();

        productSpecificationsAdapter = new ProductSpecificationsAdapter(getActivity(),productSpecifications);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(productSpecificationsAdapter);
        productSpecificationsAdapter.notifyDataSetChanged();
        return v;
    }


}
