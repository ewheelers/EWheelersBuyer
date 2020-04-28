package com.ewheelers.eWheelersBuyers.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewheelers.eWheelersBuyers.R;


public class PolicyFragment extends Fragment {
String param1,param2;
TextView textView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public PolicyFragment() {
        // Required empty public constructor
    }

    public static PolicyFragment newInstance(String param1) {
        PolicyFragment fragment = new PolicyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param1 = getArguments().getString(ARG_PARAM1);
            param2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_policy, container, false);
        textView = v.findViewById(R.id.view_policytext);
        textView.setText(param1);

        return v;
    }

}
