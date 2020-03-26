package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ewheelers.ewheelersbuyer.ModelClass.OptionValues;

import java.util.ArrayList;

public class ViewOptionsActivity extends AppCompatActivity {
TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_options);
        textView = findViewById(R.id.showoptions);
        ArrayList<OptionValues> items = getIntent().getParcelableArrayListExtra("optionvaluelist");
        Log.i("listArray", String.valueOf(items));
    }
}
