package com.ewheelers.eWheelersBuyers;

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

import com.ewheelers.eWheelersBuyers.Fragments.ProdcutDescriptionFragment;
import com.ewheelers.eWheelersBuyers.Fragments.ProductAttributesFragment;
import com.ewheelers.eWheelersBuyers.Fragments.ProductSpecificationsFragment;
import com.ewheelers.eWheelersBuyers.ModelClass.Comparemodelclass;
import com.ewheelers.eWheelersBuyers.ModelClass.ProductSpecifications;
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
    List<Comparemodelclass> productAttributes;
    String att_title;
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
        att_title = getIntent().getStringExtra("attributetitle");

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        productSpecifications = (List<ProductSpecifications>) getIntent().getSerializableExtra("Specifications");
        productAttributes = (List<Comparemodelclass>) getIntent().getSerializableExtra("attributes");

        Log.i("intentstrings",ImageView+ProName+ProPrice);

        Picasso.with(ProductDescriptionActivity.this).load(ImageView).placeholder(R.drawable.ic_dashboard_black_24dp).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(imageView);
        proName.setText("( "+Model+" ) "+ProName);
        proPrice.setText("\u20B9 "+ProPrice);



        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ProductViewPagerAdapter adapter = new ProductViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProductAttributesFragment(), att_title);
        adapter.addFragment(new ProdcutDescriptionFragment(), "Description");
        adapter.addFragment(new ProductSpecificationsFragment(), "Specifications");
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
    public List<Comparemodelclass> sendAttributes() {
        return productAttributes;
    }
}
