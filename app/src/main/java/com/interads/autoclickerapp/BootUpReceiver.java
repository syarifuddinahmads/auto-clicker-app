package com.interads.autoclickerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.interads.autoclickerapp.helper.ConfigDataHelper;
import com.interads.autoclickerapp.model.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BootUpReceiver extends BroadcastReceiver {
    private static String BOOT_UP_RECEIVER = "Boot Up Receiver";
    ConfigDataHelper configDataHelper = new ConfigDataHelper(null);

    @Override
    public void onReceive(Context context, Intent intent) {

        String packageNameApp = "com.idntimes.idntimes";
        Boolean statusConfigApp = true;

        ArrayList<HashMap<String,String>> rows = configDataHelper.getAllData();
        for(int i = 0; i < rows.size();i++){
            if(rows.get(i).get("status").equals("1")){
                packageNameApp = rows.get(i).get("");
                statusConfigApp = true;
            }
        }

        // check config status app
        // format data params intent
        // {
        //      status_config_app:Boolean,
        //      package_name_app:String,
        //      broadcast_type:String
        // }
        if(!packageNameApp.equals("") && statusConfigApp == true){
            Intent intentApp = new Intent(context,MainActivity.class);
            intentApp.putExtra("status_config_app",statusConfigApp);
            intentApp.putExtra("package_name_app",packageNameApp);
            intentApp.putExtra("broadcast_type","BOOT_UP");
            context.sendBroadcast(intentApp);
        }

    }
}
