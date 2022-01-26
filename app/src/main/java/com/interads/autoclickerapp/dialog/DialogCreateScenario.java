package com.interads.autoclickerapp.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.interads.autoclickerapp.R;
import com.interads.autoclickerapp.adapter.ListAppInstalledAdapter;
import com.interads.autoclickerapp.model.AppInstalled;

import java.util.ArrayList;

public class DialogCreateScenario extends DialogFragment implements AdapterView.OnItemSelectedListener {
    public static final String ACTIVITY_TAG = "Dialog Create Scenario";
    private EditText configName;
    private Spinner listApp;
    private RadioButton status;
    private Button btnCancel,btnSave;
    private ArrayList<AppInstalled> appInstalleds;

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

        btnCancel = view.findViewById(R.id.btn_cancel_save_config);
        btnSave = view.findViewById(R.id.btn_save_config);
        configName = (EditText) view.findViewById(R.id.config_name);
        configName.requestFocus();
        listApp = (Spinner) view.findViewById(R.id.app_name);
        listApp.setOnItemSelectedListener(this);

        ListAppInstalledAdapter appInstalledAdapter = new ListAppInstalledAdapter(getContext(),appInstalleds);
        listApp.setAdapter(appInstalledAdapter);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
