package com.interads.autoclickerapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // setting accebility service
        if(!Settings.canDrawOverlays(this)){
            // overlay manager
            int REQUEST_CODE_OVERLAY = 101;
            Intent intent_action_overlay_manager = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent_action_overlay_manager.setData(Uri.parse("package:"+getPackageName()));
            startActivityForResult(intent_action_overlay_manager,REQUEST_CODE_OVERLAY);

            // accebility manager
            int REQUEST_CODE_ACCESSBILITY = 102;
            Intent intent_action_accessbility_manager = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent_action_accessbility_manager,REQUEST_CODE_ACCESSBILITY);


        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set header bar and action
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        // get action bar id
        View view = getSupportActionBar().getCustomView();

        // action new config
        ImageView btn_action_new_config = view.findViewById(R.id.btn_action_new_config);
        btn_action_new_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"New Config",Toast.LENGTH_LONG).show();

                // call floating service view
                Intent intent_floating_view = new Intent(MainActivity.this,FloatingService.class);
                intent_floating_view.putExtra(FloatingService.ACTION,FloatingService.SHOW);
                startService(intent_floating_view);

                moveTaskToBack(true);


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
}