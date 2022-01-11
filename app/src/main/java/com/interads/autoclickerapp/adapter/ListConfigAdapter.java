package com.interads.autoclickerapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.interads.autoclickerapp.R;
import com.interads.autoclickerapp.model.Config;

import java.util.List;

public class ListConfigAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Config> configList;
    public ListConfigAdapter(Activity activity, List<Config> list) {
        this.activity = activity;
        this.configList = list;
    }

    @Override
    public int getCount() {
        return configList.size();
    }

    @Override
    public Object getItem(int i) {
        return configList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = layoutInflater.inflate(R.layout.item_list_content_main,null);
        }

        TextView name = view.findViewById(R.id.name);
        TextView date =  view.findViewById(R.id.date);
        TextView status =  view.findViewById(R.id.status);

        Config config = configList.get(i);

        name.setText(config.getName());
        date.setText(config.getDate().toString());
        status.setText(config.getStatus().toString().equals("1") ?"Active":"In Active");

        return view;
    }
}
