package com.interads.autoclickerapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.interads.autoclickerapp.adapter.ListConfigAdapter;
import com.interads.autoclickerapp.helper.ConfigDataHelper;
import com.interads.autoclickerapp.model.Config;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private ListView listViewConfig;
    List<Config> configList = new ArrayList<Config>();
    ListConfigAdapter configListAdapter;
    ConfigDataHelper configDataHelper = new ConfigDataHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set header bar and action
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        // get action bar id
        View view = getSupportActionBar().getCustomView();

        // set adapter list view and get data from sqlite
        configDataHelper = new ConfigDataHelper(getApplicationContext());
        listViewConfig = (ListView) findViewById(R.id.list_view_config);
        configListAdapter = new ListConfigAdapter(MainActivity.this,configList);

        if (isMyServiceRunning()) {
            stopService(new Intent(MainActivity.this, FloatingControlService.class));
        }

        // action new config
        ImageView btn_action_new_config = view.findViewById(R.id.btn_action_new_config);
        btn_action_new_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkOverlayDisplayPermission()) {
                    startService(new Intent(MainActivity.this, FloatingControlService.class));
                    moveTaskToBack(true);
                } else {
                    requestOverlayDisplayPermission();
                }


            }
        });

        // action setting
        ImageView btn_action_setting = view.findViewById(R.id.btn_action_setting);
        btn_action_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Setting",Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (FloatingControlService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void requestOverlayDisplayPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Screen Overlay Permission Needed");
        builder.setMessage("Enable 'Display over other apps' from System Settings.");
        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, RESULT_OK);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean checkOverlayDisplayPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}