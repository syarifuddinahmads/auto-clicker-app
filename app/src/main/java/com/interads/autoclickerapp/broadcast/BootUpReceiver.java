package com.interads.autoclickerapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.interads.autoclickerapp.MainActivity;
import com.interads.autoclickerapp.helper.ScenarioDataHelper;
import com.interads.autoclickerapp.model.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BootUpReceiver extends BroadcastReceiver {
    private static String ACTIVITY_TAG = "Boot Up Receiver";
    ScenarioDataHelper scenarioDataHelper = new ScenarioDataHelper(null);

    @Override
    public void onReceive(Context context, Intent intent) {

        String packageNameApp = "";
        Boolean statusConfigApp = true;
        String idConfigApp = "";

        ArrayList<HashMap<String,String>> rows = scenarioDataHelper.getAllDataConfig();
        for(int i = 0; i < rows.size();i++){
            if(rows.get(i).get("status").equals("1")){
                packageNameApp = rows.get(i).get("app");
                statusConfigApp = true;
                idConfigApp = rows.get(i).get("id");
            }
        }

        // check config status app
        // format data params intent
        // {id_config_app,status_config_app:Boolean,package_name_app:String,broadcast_type:String}
        if(!packageNameApp.equals("") && statusConfigApp == true){
            Intent intentApp = new Intent(context, MainActivity.class);
            intent.putExtra("id_config_app",idConfigApp);
            intentApp.putExtra("status_config_app",statusConfigApp);
            intentApp.putExtra("package_name_app",packageNameApp);
            intentApp.putExtra("broadcast_type","BOOT_UP");
            context.sendBroadcast(intentApp);
        }

    }
}
