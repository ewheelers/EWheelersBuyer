package com.ewheelers.ewheelersbuyer.Dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ewheelers.ewheelersbuyer.R;

public class ShowAlerts {

    Context context;

    public ShowAlerts(Context context) {
        this.context = context;
    }

    public static void showSuccessDialog(Context context, View v, String message) {
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.success_layout, viewGroup, false);
        TextView textView = dialogView.findViewById(R.id.message);
        Button button = dialogView.findViewById(R.id.buttonOk);
        textView.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public static void showfailedDialog(Context context,View v,String message) {
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.failed_layout, viewGroup, false);
        TextView textView = dialogView.findViewById(R.id.message);
        Button button = dialogView.findViewById(R.id.buttonOk);
        textView.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


}
