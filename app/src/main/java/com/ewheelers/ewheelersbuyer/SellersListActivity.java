package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ewheelers.ewheelersbuyer.Adapters.SellerListAdapter;
import com.ewheelers.ewheelersbuyer.ModelClass.SellerListModel;

import java.util.ArrayList;
import java.util.List;

public class SellersListActivity extends AppCompatActivity {
RecyclerView recyclerView;
SellerListAdapter sellerListAdapter;
List<SellerListModel> sellerListModelList = new ArrayList<>();
String shopName,shopAddres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_list);
        recyclerView = findViewById(R.id.sellers_list);
        shopAddres = getIntent().getStringExtra("shopaddress");
        shopName = getIntent().getStringExtra("shopname");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        SellerListModel sellerListModel = new SellerListModel();
        sellerListModel.setSellersname(shopName);
        sellerListModel.setSellersaddress(shopAddres);
        sellerListModelList.add(sellerListModel);
        sellerListAdapter = new SellerListAdapter(this,sellerListModelList);
        recyclerView.setAdapter(sellerListAdapter);
    }



}
