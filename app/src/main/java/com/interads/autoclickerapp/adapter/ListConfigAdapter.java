package com.interads.autoclickerapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.interads.autoclickerapp.MainActivity;
import com.interads.autoclickerapp.R;
import com.interads.autoclickerapp.helper.ConfigDataHelper;
import com.interads.autoclickerapp.model.Config;

import org.w3c.dom.Text;

import java.util.List;

public class ListConfigAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Config> configList;
    private ConfigDataHelper configDataHelper;
    public ListConfigAdapter(Activity activity, List<Config> list) {
        this.activity = activity;
        this.configList = list;
        this.configDataHelper = new ConfigDataHelper(activity.getApplicationContext());
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
    public View getView(int itemPosition, View view, ViewGroup viewGroup) {
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = layoutInflater.inflate(R.layout.item_list_content_main,null);
        }

        TextView id_config = view.findViewById(R.id.id_config);
        TextView name = view.findViewById(R.id.name);
        TextView date =  view.findViewById(R.id.date);
        TextView status =  view.findViewById(R.id.status);

        Config config = configList.get(itemPosition);
        id_config.setText(String.valueOf(config.getId()));
        name.setText(config.getName());
        date.setText(config.getDate().toString());
        status.setText(config.getStatus() == true ?"Active":"In Active");
        if(config.getStatus() == true){
            status.setBackgroundColor(Color.parseColor("#4caf50"));
        }else{
            status.setBackgroundColor(Color.parseColor("#f44336"));
        }

        ImageView btn_item_option_menu = view.findViewById(R.id.item_action_more);
        btn_item_option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(activity, view.findViewById(R.id.item_action_more));
                popup.getMenuInflater().inflate(R.menu.option_menu_item_list_content, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemAction = item.getItemId();
                        if (itemAction == R.id.option_item_rename) {
                            //do something
                            Toast.makeText(activity,"Rename ",Toast.LENGTH_LONG).show();
                            return true;
                        }
                        else if (itemAction == R.id.option_item_edit){
                            //do something
                            Toast.makeText(activity,"Edit",Toast.LENGTH_LONG).show();
                            return true;
                        }
                        else if (itemAction == R.id.option_item_set_active) {
                            android.app.AlertDialog.Builder alertSetActiveConfirm = new android.app.AlertDialog.Builder(activity);
                            alertSetActiveConfirm.setTitle("Activate Config");
                            alertSetActiveConfirm.setMessage("Are you sure to activate this config ?");
                            alertSetActiveConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    configDataHelper.activate_config(config.getId(),true);
                                    Toast.makeText(activity,"Config activated... ",Toast.LENGTH_LONG).show();
                                }
                            });

                            alertSetActiveConfirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Toast.makeText(activity,"Activate config cancelled...",Toast.LENGTH_LONG).show();
                                }
                            });

                            alertSetActiveConfirm.show();
                            return true;
                        }
                        else if (itemAction == R.id.option_item_Delete) {
                            android.app.AlertDialog.Builder alertDeleteConfirm = new android.app.AlertDialog.Builder(activity);
                            alertDeleteConfirm.setTitle("Delete Config");
                            alertDeleteConfirm.setMessage("Are you sure to delete this config ?");
                            alertDeleteConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    configDataHelper.delete(config.getId());
                                    Toast.makeText(activity,"Delete data config successfully... ",Toast.LENGTH_LONG).show();
                                }
                            });

                            alertDeleteConfirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Toast.makeText(activity,"Delete data config was cancelled...",Toast.LENGTH_LONG).show();
                                }
                            });

                            alertDeleteConfirm.show();
                            return true;
                        }
                        else {
                            return onMenuItemClick(item);
                        }
                    }
                });

                popup.show();
            }
        });

        return view;
    }


}
