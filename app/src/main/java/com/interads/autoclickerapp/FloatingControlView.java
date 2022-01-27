package com.interads.autoclickerapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.interads.autoclickerapp.helper.ConfigDataHelper;
import com.interads.autoclickerapp.model.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FloatingControlView extends FrameLayout implements View.OnClickListener {

    private static final String ACTIVITY_TAG = "Floating Control View";
    private Context _context;
    private View floatingControlView;
    private WindowManager _windowManager;
    private WindowManager.LayoutParams _layoutParam;
    private String currentState;
    private ConfigDataHelper configDataHelper;
    private Config lastConfig;

    private ImageView btn_action_play;
    private ImageView btn_action_pause;
    private ImageView btn_action_edit;
    private ImageView btn_action_save;
    private ImageView btn_action_add_swipe;
    private ImageView btn_action_add_new_click;
    private ImageView btn_action_minus;
    private ImageView btn_action_close;
    private ImageView btn_action_add;

    private ArrayList<View> viewActionList;

    public FloatingControlView(@NonNull Context context) {
        super(context);

        configDataHelper = new ConfigDataHelper(getContext());
        ArrayList<HashMap<String,String>> rows = configDataHelper.getAllData();
        int lastRow = rows.size()-1;
        String id = rows.get(lastRow).get("id");
        String name = rows.get(lastRow).get("name");
        String app = rows.get(lastRow).get("app");
        String date = rows.get(lastRow).get("date");
        String status = rows.get(lastRow).get("status");

        lastConfig = new Config(Integer.parseInt(id),name,app,new Boolean(status),new Date(date));
        Log.i(ACTIVITY_TAG,"ID = "+id);
        Log.i(ACTIVITY_TAG,"Name = "+name);
        Log.i(ACTIVITY_TAG,"App = "+app);
        Log.i(ACTIVITY_TAG,"Date = "+date);
        Log.i(ACTIVITY_TAG,"Status = "+status);


        _context = context.getApplicationContext();
        viewActionList = new ArrayList<View>();

        LayoutInflater fcvInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        floatingControlView = fcvInflater.inflate(R.layout.floating_control_view,null);

        btn_action_play = floatingControlView.findViewById(R.id.action_play);
        btn_action_pause = floatingControlView.findViewById(R.id.action_pause);
        btn_action_edit = floatingControlView.findViewById(R.id.action_edit);
        btn_action_save = floatingControlView.findViewById(R.id.action_save);
        btn_action_add_swipe = floatingControlView.findViewById(R.id.action_add_swipe);
        btn_action_add_new_click = floatingControlView.findViewById(R.id.action_new_click);
        btn_action_minus = floatingControlView.findViewById(R.id.action_minus);
        btn_action_add = floatingControlView.findViewById(R.id.action_add);
        btn_action_close = floatingControlView.findViewById(R.id.action_close);

        // set action listener
        btn_action_play.setOnClickListener(this);
        btn_action_pause.setOnClickListener(this);
        btn_action_edit.setOnClickListener(this);
        btn_action_save.setOnClickListener(this);
        btn_action_add_swipe.setOnClickListener(this);
        btn_action_add_new_click.setOnClickListener(this);
        btn_action_minus.setOnClickListener(this);
        btn_action_add.setOnClickListener(this);
        btn_action_close.setOnClickListener(this);

        _windowManager = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void showFloatingControlView(){

        DisplayMetrics metrics = _context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        _layoutParam = new WindowManager.LayoutParams();
        _layoutParam.gravity = Gravity.LEFT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _layoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            _layoutParam.type = WindowManager.LayoutParams.TYPE_TOAST;
        }

        _layoutParam.format = PixelFormat.RGBA_8888;
        _layoutParam.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
        _layoutParam.width = (int) (width * (0.10f));
        _layoutParam.height = (int) (height * (0.75f));
        _windowManager.addView(floatingControlView, _layoutParam);

    }

    public void hideFloatingControlView(){
        _windowManager.removeView(floatingControlView);
    }

    public void addActionClick(){


        int indexChildView = viewActionList.size()+1;

        LayoutInflater fcvInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionClickView = fcvInflater.inflate(R.layout.click_view,null);
        actionClickView.setId(indexChildView);
        TextView number_action = actionClickView.findViewById(R.id.number_action_click);
        number_action.setText(String.valueOf(indexChildView));

        _layoutParam = new WindowManager.LayoutParams();
        _layoutParam.gravity = Gravity.CENTER;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _layoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            _layoutParam.type = WindowManager.LayoutParams.TYPE_TOAST;
        }

        _layoutParam.format = PixelFormat.RGBA_8888;
        _layoutParam.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
        _layoutParam.width = LayoutParams.WRAP_CONTENT;
        _layoutParam.height = LayoutParams.WRAP_CONTENT;
        _windowManager.addView(actionClickView, _layoutParam);

        actionClickView.setOnTouchListener(new View.OnTouchListener() {

            final WindowManager.LayoutParams floatWindowLayoutUpdateParam = _layoutParam;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i(ACTIVITY_TAG,"X ========== "+event.getX());
                Log.i(ACTIVITY_TAG,"Y ========== "+event.getY());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = floatWindowLayoutUpdateParam.x;
                        y = floatWindowLayoutUpdateParam.y;
                        px = event.getRawX();
                        py = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        floatWindowLayoutUpdateParam.x = (int) ((x + event.getRawX()) - px);
                        floatWindowLayoutUpdateParam.y = (int) ((y + event.getRawY()) - py);

                        _windowManager.updateViewLayout(actionClickView, floatWindowLayoutUpdateParam);

                        break;
                }

                return false;
            }
        });

        viewActionList.add(actionClickView);
    }

    public void removeView(){
        int indexChildView = viewActionList.size();
        if(indexChildView != 0 && indexChildView > 0){
            int totalIndexView = viewActionList.size()-1;
            _windowManager.removeView(viewActionList.get(totalIndexView));
            viewActionList.remove(totalIndexView);
            indexChildView--;
        }
    }

    public void closeView(){
        for(int i = 0 ; i <viewActionList.size(); i++){
            _windowManager.removeView(viewActionList.get(i));
        }
        _windowManager.removeView(floatingControlView);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(getContext(),ActionControlService.class);
        switch (view.getId()){
            case R.id.action_play:

                currentState = ActionControlService.PLAY;

                intent.putExtra(ActionControlService.ACTION, ActionControlService.PLAY);
                intent.putExtra("x", 100);
                intent.putExtra("y", 55);
                break;
            case R.id.action_pause:
                _layoutParam.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
                currentState = ActionControlService.STOP;
                intent.putExtra(ActionControlService.ACTION, ActionControlService.STOP);
                break;
            case R.id.action_close:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.CLOSE);
                break;
            case R.id.action_new_click:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.ADD_TAP);
                break;
            case R.id.action_add_swipe:
                break;
            case R.id.action_add:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.PLUS);
                break;
            case R.id.action_minus:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.MINUS);
                break;
            case R.id.action_edit:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.EDIT);
                break;
            case R.id.action_save:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.SAVE);
                break;
        }

        getContext().startService(intent);

    }
}
