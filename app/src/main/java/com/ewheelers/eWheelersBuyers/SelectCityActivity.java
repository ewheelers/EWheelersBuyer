package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ewheelers.eWheelersBuyers.Adapters.SelectCityAdapter;
import com.ewheelers.eWheelersBuyers.ModelClass.SelectCitiesModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity {
RecyclerView recyclerView;
SelectCityAdapter selectCityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        recyclerView = findViewById(R.id.recycler_cities);
        selectCityAdapter = new SelectCityAdapter(this,selectCitiesModelList());
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(selectCityAdapter);
    }
    public List<SelectCitiesModel> selectCitiesModelList(){
        List<SelectCitiesModel> selectCitiesModelList = new ArrayList<>();
        selectCitiesModelList.add(new SelectCitiesModel("Hyderabad",R.drawable.ic_charminar));
        return selectCitiesModelList;
    }

    public void setSnackbar(String cityname) {
        Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), "Selected "+cityname, Snackbar.LENGTH_LONG);
        mySnackbar.setAction("Go", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mySnackbar.show();
    }
}
