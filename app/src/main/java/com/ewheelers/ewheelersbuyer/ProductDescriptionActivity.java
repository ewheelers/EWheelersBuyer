package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewheelers.ewheelersbuyer.Fragments.ProdcutDescriptionFragment;
import com.ewheelers.ewheelersbuyer.Fragments.ProductSpecificationsFragment;
import com.ewheelers.ewheelersbuyer.ModelClass.ProductSpecifications;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDescriptionActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imageView;
    TextView proName, proPrice;
    String description;
    List<ProductSpecifications> productSpecifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        imageView = findViewById(R.id.imageView);
        proName = findViewById(R.id.product_title);
        proPrice = findViewById(R.id.product_price);

        description = getIntent().getStringExtra("description");
        String ImageView = getIntent().getStringExtra("image");
        String ProName = getIntent().getStringExtra("title");
        String ProPrice = getIntent().getStringExtra("price");
        String Model = getIntent().getStringExtra("model");

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        productSpecifications = (List<ProductSpecifications>) getIntent().getSerializableExtra("Specifications");

        Log.i("intentstrings",ImageView+ProName+ProPrice);

        Picasso.get().load(ImageView).placeholder(R.drawable.ic_dashboard_black_24dp).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(imageView);
        proName.setText("( "+Model+" ) "+ProName);
        proPrice.setText("\u20B9 "+ProPrice);



        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ProductViewPagerAdapter adapter = new ProductViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProductSpecificationsFragment(), "Specifications");
        adapter.addFragment(new ProdcutDescriptionFragment(), "Description");
        viewPager.setAdapter(adapter);
    }

    class ProductViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ProductViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public String sendData() {
        return description;
    }
    public List<ProductSpecifications> sendSpecification() {
        return productSpecifications;
    }
}
