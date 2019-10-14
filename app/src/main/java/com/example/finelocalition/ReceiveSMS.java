package com.example.finelocalition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class ReceiveSMS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("i"," i received");
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle=intent.getExtras();
            if(bundle !=null){
                Object[] pdus= (Object[]) bundle.get("pdus");
                SmsMessage[] messages= new SmsMessage[pdus.length];
                for (int i=0;i<pdus.length;i++)
                    messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
                for(SmsMessage message:messages){
                    String m=message.getMessageBody();
                    String n=message.getOriginatingAddress();
                    if(m.startsWith("Where")){
                        sendSMS(n,"Position Report 21.03524 105.850182");
                    }

                }
            }
        }
    }

    public void sendSMS(String numero,String message){
        SmsManager smsMessage= SmsManager.getDefault();
        smsMessage.sendTextMessage(numero,null,message,null,null);
    }




}
