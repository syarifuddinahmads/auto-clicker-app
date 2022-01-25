package com.interads.autoclickerapp.dialog;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.interads.autoclickerapp.R;

import java.util.ArrayList;
import java.util.List;

public class DialogSaveScenario extends DialogFragment {
    private EditText configName;
    private Spinner appName;
    private Spinner status;

    public DialogSaveScenario(){

    }

    public static DialogSaveScenario newInstance(String title){
        DialogSaveScenario dialogSaveScenario = new DialogSaveScenario();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        dialogSaveScenario.setArguments(bundle);
        return dialogSaveScenario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_save_scenario,container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configName = (EditText) view.findViewById(R.id.config_name);
        appName = (Spinner) view.findViewById(R.id.app_name);
        status = (Spinner) view.findViewById(R.id.status);

        configName.requestFocus();

        String titleDialog = getArguments().getString("title");
        getDialog().setTitle(titleDialog);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }
}
