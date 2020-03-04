package com.ewheelers.ewheelersbuyer.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ewheelers.ewheelersbuyer.Fragments.FragmentForgotPassword;
import com.ewheelers.ewheelersbuyer.Fragments.FragmentSignIn;
import com.ewheelers.ewheelersbuyer.Fragments.FragmentSignUp;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        if (position == 0) {
            fragment = new FragmentSignIn();
        } else if (position == 1) {
            fragment = new FragmentSignUp();
        }else if(position==2){
            fragment = new FragmentForgotPassword();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Login";
        } else if (position == 1) {
            title = "Register";
        }else if (position == 2) {
            title = "Forgot Password";
        }
        return title;
    }
}
