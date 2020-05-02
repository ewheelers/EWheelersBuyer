package com.ewheelers.eWheelersBuyers.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), CONNECTIVITY_ACTION)) {
            new ConnectionStateMonitor().updateConnection();
        }
    }
}
