package com.ewheelers.eWheelersBuyers.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ewheelers.eWheelersBuyers.Interface.SmsListener;

public class SmsReceiver extends BroadcastReceiver {
    private static SmsListener mListener;
    Boolean b;
    String abcd,xyz;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for (int i=0; i<pdus.length;i++)
        {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            //Log.d("Otp...",sender);
//            b=sender.endsWith("57575701");
            String messageBody = smsMessage.getMessageBody();
            b=messageBody.contains("eWheelers.in");
            abcd=messageBody.replaceAll("[^0-9]","");   // contains otp
            //Log.d("Otp...",abcd+"...."+b);
            if(b==true) {

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("OTP Listener", "" + abcd);
                editor.commit();
                if (mListener != null) {
                    mListener.messageReceived(abcd);  // attach value to interface
                }
            }

        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

}

