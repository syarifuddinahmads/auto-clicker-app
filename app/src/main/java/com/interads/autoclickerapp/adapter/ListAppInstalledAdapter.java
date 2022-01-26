package com.interads.autoclickerapp.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.interads.autoclickerapp.R;
import com.interads.autoclickerapp.model.AppInstalled;

import java.util.ArrayList;

public class ListAppInstalledAdapter extends BaseAdapter {

    Context context;
    ArrayList<AppInstalled> listAppInstalled;
    LayoutInflater layoutInflater;

    public ListAppInstalledAdapter(Context context,ArrayList<AppInstalled> listApp) {
        this.context = context;
        this.listAppInstalled = listApp;
        layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listAppInstalled.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.list_item_app_installed,null);
        ImageView icon = view.findViewById(R.id.item_app_thumbnail);
        TextView name = view.findViewById(R.id.item_app_name);
        Drawable iconApp = null;
        try {
            iconApp = context.getPackageManager().getApplicationIcon(listAppInstalled.get(i).getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        icon.setImageDrawable(iconApp);
        name.setText(listAppInstalled.get(i).getName());
        return view;
    }
}
