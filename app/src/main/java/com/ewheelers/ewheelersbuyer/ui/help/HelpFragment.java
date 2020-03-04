package com.ewheelers.ewheelersbuyer.ui.help;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewheelers.ewheelersbuyer.R;

public class HelpFragment extends Fragment {

    private HelpViewModel mViewModel;
    private TextView textView;
    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.help_fragment, container, false);
        textView = root.findViewById(R.id.text_help);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HelpViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
              //  textView.setText(s);
            }
        });
    }

}
