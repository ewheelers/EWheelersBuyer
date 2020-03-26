package com.ewheelers.ewheelersbuyer.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewheelers.ewheelersbuyer.ProductDescriptionActivity;
import com.ewheelers.ewheelersbuyer.R;


public class ProdcutDescriptionFragment extends Fragment {
    String description;
    TextView html_reponse;
    private Spanned htmlAsSpanned;

    public ProdcutDescriptionFragment() {
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
        View root = inflater.inflate(R.layout.fragment_prodcut_description, container, false);
        html_reponse = root.findViewById(R.id.html_reponsetxt);
        ProductDescriptionActivity activity = (ProductDescriptionActivity) getActivity();
        String getData = activity.sendData();
        htmlAsSpanned = Html.fromHtml(getData);
        html_reponse.setText(htmlAsSpanned);
        return root;

    }

}
