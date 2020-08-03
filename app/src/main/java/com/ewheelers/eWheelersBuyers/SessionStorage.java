package com.ewheelers.eWheelersBuyers;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionStorage {
    public static String tokenvalue = "token";
    public static String user_id = "userid";
    public static String user_name = "username";
    public static String location = "location";
    public static String vehicleno = "vehicleno";
    public static String vehiclemodel = "vehiclemodel";
    public static String starttime = "starttime";
    public static String endtime = "endtime";
    public static String tothrs = "totalhours";
    public static String stationaddress = "station";
    public static String productname = "productname";

    public static String dataToSave = "appData";
    public static String productslength = "length";


    public static String compareview = "viewcount";
    public static String productid = "proid";
    public static String productid2 = "prodid2";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStrings(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

    public static void clearString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, null);
        editor.apply();
    }
}
