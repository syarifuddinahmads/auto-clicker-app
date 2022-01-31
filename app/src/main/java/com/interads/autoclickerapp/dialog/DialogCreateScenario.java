package com.interads.autoclickerapp.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.interads.autoclickerapp.service.ActionControlService;
import com.interads.autoclickerapp.R;
import com.interads.autoclickerapp.adapter.ListAppInstalledAdapter;
import com.interads.autoclickerapp.helper.ConfigDataHelper;
import com.interads.autoclickerapp.model.AppInstalled;
import com.interads.autoclickerapp.model.Config;

import java.util.ArrayList;
import java.util.Date;

public class DialogCreateScenario extends DialogFragment{
    public static final String ACTIVITY_TAG = "Dialog Create Scenario";
    private EditText configName;
    private Spinner listApp;
    private RadioGroup statusScenario;
    private TextView btnCancel,btnSave;
    private ArrayList<AppInstalled> appInstalleds;
    private AlertDialog alertDialog;
    private ConfigDataHelper configDataHelper;

    public DialogCreateScenario(ArrayList<AppInstalled> installedApps){
        appInstalleds = installedApps;
    }

    public static DialogCreateScenario newInstance(ArrayList<AppInstalled> installedApps){
        DialogCreateScenario dialogCreateScenario = new DialogCreateScenario(installedApps);
        return dialogCreateScenario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_create_scenario,container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configDataHelper = new ConfigDataHelper(getContext());

        btnCancel = view.findViewById(R.id.btn_cancel_save_config);
        btnSave = view.findViewById(R.id.btn_save_config);
        configName = (EditText) view.findViewById(R.id.config_name);
        configName.requestFocus();
        statusScenario = (RadioGroup) view.findViewById(R.id.status_scenario);
        statusScenario.clearCheck();

        statusScenario.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton)radioGroup.findViewById(i);
            }
        });


        listApp = (Spinner) view.findViewById(R.id.app_name);
        ListAppInstalledAdapter appInstalledAdapter = new ListAppInstalledAdapter(getContext(),appInstalleds);
        listApp.setAdapter(appInstalledAdapter);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkOverlayDisplayPermission()) {

                    int selectedStatusScenario = statusScenario.getCheckedRadioButtonId();
                    RadioButton radioButton = statusScenario.findViewById(selectedStatusScenario);

                    Config config = new Config();
                    config.setName(configName.getText().toString());
                    config.setApp("");
                    config.setStatus(radioButton.getText().toString().toLowerCase().equals("in active") ? false:true);
                    config.setDate(new Date());

                    configDataHelper.insert(config.getName(),config.getApp(),config.getDate(),config.getStatus());


                    Intent intent = new Intent(getActivity(), ActionControlService.class);
                    intent.putExtra(ActionControlService.ACTION, ActionControlService.SHOW);
                    intent.putExtra("interval", 10);

                    getActivity().startService(intent);
                    getActivity().moveTaskToBack(true);
                } else {
                    requestOverlayDisplayPermission();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void requestOverlayDisplayPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Screen Overlay Permission Needed");
        builder.setMessage("Enable 'Display over other apps' from System Settings.");
        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, getActivity().RESULT_OK);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean checkOverlayDisplayPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getContext())) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
