package com.ewheelers.ewheelersbuyer;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class BottomedNavigationActivity extends AppCompatActivity{
   /* RecyclerView recyclerView;
    MenuIconAdapter menuIconAdapter;
    List<HomeMenuIcons> homeMenuIcons = new ArrayList<>();*/
    InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomed_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
//        recyclerView = findViewById(R.id.static_menus);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_alerts, R.id.navigation_wallet, R.id.navigation_help, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
      //  NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

      /*  menuIconAdapter = new MenuIconAdapter(this,homeMenuIcons());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(menuIconAdapter);*/
    }

   /* public List<HomeMenuIcons> homeMenuIcons() {
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_chargeplug, "Charge"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_mechanics, "Mechanic"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_punctureflat, "Puncture"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_spareparts, "Spares"));
        homeMenuIcons.add(new HomeMenuIcons(R.drawable.ic_accessroies, "Accessories"));
        return homeMenuIcons;
    }
*/
}

