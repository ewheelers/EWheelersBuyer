package com.ewheelers.eWheelersBuyers;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class eWheelerApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
