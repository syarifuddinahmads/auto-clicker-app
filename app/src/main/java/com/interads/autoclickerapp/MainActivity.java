package com.interads.autoclickerapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.interads.autoclickerapp.adapter.ListConfigAdapter;
import com.interads.autoclickerapp.dialog.DialogCreateScenario;
import com.interads.autoclickerapp.helper.ScenarioDataHelper;
import com.interads.autoclickerapp.model.AppInstalled;
import com.interads.autoclickerapp.model.Config;
import com.interads.autoclickerapp.service.ActionControlService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String MAIN_ACTIVITY = "Main Activity";
    private AlertDialog alertDialog;
    private ListView listViewConfig;
    List<Config> configList = new ArrayList<Config>();
    ListConfigAdapter configListAdapter;
    ScenarioDataHelper scenarioDataHelper;
    PackageManager packageManager;
    List<PackageInfo> listPackageInfo;
    ArrayList<AppInstalled> appInstalleds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!Settings.canDrawOverlays(this)) {
            int REQUEST_CODE_101 = 101;
            Intent overlayPermission = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            overlayPermission.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(overlayPermission, REQUEST_CODE_101);

            int REQUEST_CODE_102 = 102;
            Intent accessibilitySetting = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(accessibilitySetting, REQUEST_CODE_102);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get package install on devices
        appInstalleds = new ArrayList<>();
        packageManager = getPackageManager();
        listPackageInfo = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        for(int i = 0 ; i<listPackageInfo.size();i++){
            appInstalleds.add(new AppInstalled(listPackageInfo.get(i).packageName, listPackageInfo.get(i).packageName));
        }


        // set header bar and action
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        View view = getSupportActionBar().getCustomView();

        // action new config
        ImageView btn_action_refresh = view.findViewById(R.id.btn_action_refresh);
        btn_action_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configList.clear();
                getDataConfig();
            }
        });

        // action new config
        ImageView btn_action_new_config = view.findViewById(R.id.btn_action_new_config);
        btn_action_new_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialogCreateScenario();

            }
        });

        // set adapter list view and get data from sqlite
        scenarioDataHelper = new ScenarioDataHelper(getApplicationContext());
        listViewConfig = findViewById(R.id.list_view_config);
        configListAdapter = new ListConfigAdapter(MainActivity.this,configList);
        listViewConfig.setAdapter(configListAdapter);

        // get data config from sqlite
        getDataConfig();

        listViewConfig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Test!!! = "+configList.get(i).getId(), Toast.LENGTH_SHORT).show();
                if (checkOverlayDisplayPermission()) {

                    Intent intent = new Intent(MainActivity.this, ActionControlService.class);
                    intent.putExtra(ActionControlService.ACTION, ActionControlService.SHOW);
                    intent.putExtra("interval", 10);
                    intent.putExtra("id_config",configList.get(i).getId());

                    startService(intent);
                    moveTaskToBack(true);
                } else {
                    requestOverlayDisplayPermission();
                }

            }
        });
    }

    private void alertDialogCreateScenario() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogCreateScenario alertDialog = DialogCreateScenario.newInstance(appInstalleds);
        alertDialog.show(fragmentManager, DialogCreateScenario.ACTIVITY_TAG);

    }

    private void getDataConfig(){
        ArrayList<HashMap<String,String>> rows = scenarioDataHelper.getAllDataConfig();
        for(int i = 0; i < rows.size();i++){
            String id = rows.get(i).get("id");
            String name = rows.get(i).get("name");
            String app = rows.get(i).get("app");
            String date = rows.get(i).get("date");
            String status = rows.get(i).get("status");

            Config config = new Config(Integer.parseInt(id),name,app,new Boolean(status),new Date(date));

            configList.add(config);
        }
        configListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configList.clear();
        getDataConfig();
    }

    private void insertConfig() {
        Config config = new Config();
        config.setName("Config");
        config.setApp("-");
        config.setDate(new Date());
        config.setStatus(false);

        scenarioDataHelper.insertConfig(config.getName(),config.getApp(),config.getDate(),config.getStatus());
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String broadcastType = intent.getStringExtra("broadcast_type");
            String packageNameApp = intent.getStringExtra("package_name_app");

            switch (broadcastType){
                case "BOOT_UP":
                    openAnotherApp(packageNameApp);
                    break;
            }

            if(isForeground(context,packageNameApp)){
                // run config scenario
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void openAnotherApp(String packageNameApp){
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageNameApp);
        if(intent.resolveActivity(getApplicationContext().getPackageManager()) != null){
            startActivity(intent);
        }
    }

    public static boolean isForeground(Context ctx, String packageNameApp){
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(1);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        if(componentInfo.getPackageName().equals(packageNameApp)) {
            return true;
        }
        return false;
    }

    private void requestOverlayDisplayPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setCancelable(true);
        builder.setTitle("Screen Overlay Permission Needed");
        builder.setMessage("Enable 'Display over other apps' from System Settings.");
        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getApplication().getPackageName()));
                startActivityForResult(intent, MainActivity.RESULT_OK);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean checkOverlayDisplayPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}