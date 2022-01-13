package com.interads.autoclickerapp;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.interads.autoclickerapp.model.Scenario;

import java.util.ArrayList;

public class FloatingControlService extends Service {

    private ViewGroup floatView;
    private int LAYOUT_TYPE;
    private WindowManager.LayoutParams floatWindowLayoutParam,floatWindowLayoutParamStart,floatWindowLayoutParamEnd;
    private WindowManager windowManager;

    private ImageView btn_action_play;
    private ImageView btn_action_pause;
    private ImageView btn_action_edit;
    private ImageView btn_action_save;
    private ImageView btn_action_add_swipe;
    private ImageView btn_action_add_new_click;
    private ImageView btn_action_minus;
    private ImageView btn_action_close;
    private ImageView btn_action_add;

    private int indexChildView = 0;
    private ArrayList<ViewGroup> viewList;
    private ArrayList<Scenario> scenarioList;

    @Override
    public void onCreate() {
        super.onCreate();

        viewList = new ArrayList<ViewGroup>();
        scenarioList = new ArrayList<Scenario>();
        // load config floating control view
        floatingControlView();

        // action close floating control view
        btn_action_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent main_intent = new Intent(FloatingControlService.this,MainActivity.class);
                main_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main_intent);

            }
        });


        // action add new action click (default)
        btn_action_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActionClick();
                indexChildView++;
            }
        });

        // action minus action click (default)
        btn_action_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(indexChildView != 0 && indexChildView > 0){
                    int totalIndexView = viewList.size()-1;
                    windowManager.removeView(viewList.get(totalIndexView));
                    viewList.remove(totalIndexView);
                    indexChildView--;
                }
            }
        });

        // action save data action to sqlite
        btn_action_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_action_add_new_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActionClick();
                indexChildView++;
            }
        });

        btn_action_add_swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActionSwipe();
                indexChildView++;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        for(int i = 0 ; i <viewList.size(); i++){
            windowManager.removeView(viewList.get(i));
        }
        windowManager.removeView(floatView);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void addActionClick(){
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup floatClickView = (ViewGroup) inflater.inflate(R.layout.click_view, null);
        floatClickView.setId(indexChildView);
        TextView number_action = floatClickView.findViewById(R.id.number_action_click);
        number_action.setText(String.valueOf(indexChildView+1));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }


        floatWindowLayoutParam = new WindowManager.LayoutParams(
                (int) (width * (0.10f)),
                (int) (height* (0.1f)),
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatWindowLayoutParam.gravity = Gravity.CENTER;
        floatWindowLayoutParam.x = 0;
        floatWindowLayoutParam.y = 0;

        windowManager.addView(floatClickView, floatWindowLayoutParam);
        addScenario(floatWindowLayoutParam.x,floatWindowLayoutParam.y,0,0,"click",0,0);

        floatClickView.setOnTouchListener(new View.OnTouchListener() {

            final WindowManager.LayoutParams floatWindowLayoutUpdateParam = floatWindowLayoutParam;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

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

                        windowManager.updateViewLayout(floatClickView, floatWindowLayoutUpdateParam);

                        break;
                }

                return false;
            }
        });

        viewList.add(floatClickView);
    }

    private void floatingControlView(){
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        floatView = (ViewGroup) inflater.inflate(R.layout.floating_view, null);

        // set view id
        btn_action_play = floatView.findViewById(R.id.action_play);
        btn_action_pause = floatView.findViewById(R.id.action_pause);
        btn_action_edit = floatView.findViewById(R.id.action_edit);
        btn_action_save = floatView.findViewById(R.id.action_save);
        btn_action_add_swipe = floatView.findViewById(R.id.action_add_swipe);
        btn_action_add_new_click = floatView.findViewById(R.id.action_new_click);
        btn_action_minus = floatView.findViewById(R.id.action_minus);
        btn_action_add = floatView.findViewById(R.id.action_add);
        btn_action_close = floatView.findViewById(R.id.action_close);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }


        floatWindowLayoutParam = new WindowManager.LayoutParams(
                (int) (width * (0.10f)),
                (int) (height * (0.50f)),
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatWindowLayoutParam.gravity = Gravity.LEFT;
        floatWindowLayoutParam.x = ViewGroup.LayoutParams.WRAP_CONTENT;
        floatWindowLayoutParam.y = ViewGroup.LayoutParams.WRAP_CONTENT;

        windowManager.addView(floatView, floatWindowLayoutParam);

        floatView.setOnTouchListener(new View.OnTouchListener() {

            final WindowManager.LayoutParams floatWindowLayoutUpdateParam = floatWindowLayoutParam;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

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

                        windowManager.updateViewLayout(floatView, floatWindowLayoutUpdateParam);
                        break;
                }

                return false;
            }
        });
    }

    private void addActionSwipe(){
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        // =============================== Start View Group ========================================== //
        ViewGroup floatStartSwipeView = (ViewGroup) inflater.inflate(R.layout.start_swipe_view, null);
        floatStartSwipeView.setId(indexChildView);
        TextView startNumberSwipe = floatStartSwipeView.findViewById(R.id.start_action_swipe);
        startNumberSwipe.setText(String.valueOf(indexChildView+1));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }


        floatWindowLayoutParamStart = new WindowManager.LayoutParams(
                (int) (width * (0.10f)),
                (int) (height* (0.1f)),
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatWindowLayoutParamStart.gravity = Gravity.TOP;
        floatWindowLayoutParamStart.x = 0;
        floatWindowLayoutParamStart.y = 0;
        windowManager.addView(floatStartSwipeView,floatWindowLayoutParamStart);

        floatStartSwipeView.setOnTouchListener(new View.OnTouchListener() {

            final WindowManager.LayoutParams floatWindowLayoutUpdateParam = floatWindowLayoutParamStart;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

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

                        windowManager.updateViewLayout(floatStartSwipeView, floatWindowLayoutUpdateParam);
                        break;
                }

                return false;
            }
        });

        // =============================== Start View Group ========================================== //

        // =============================== End View Group ========================================== //
        ViewGroup floatEndSwipeView = (ViewGroup) inflater.inflate(R.layout.end_swipe_view, null);
        floatEndSwipeView.setId(indexChildView);
        TextView endNumberSwipe = floatEndSwipeView.findViewById(R.id.end_action_swipe);
        endNumberSwipe.setText(String.valueOf(indexChildView+1));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }


        floatWindowLayoutParamEnd = new WindowManager.LayoutParams(
                (int) (width * (0.10f)),
                (int) (height* (0.1f)),
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatWindowLayoutParamEnd.gravity = Gravity.CENTER;
        floatWindowLayoutParamEnd.x = 20;
        floatWindowLayoutParamEnd.y = 20;
        windowManager.addView(floatEndSwipeView,floatWindowLayoutParamEnd);

        floatEndSwipeView.setOnTouchListener(new View.OnTouchListener() {

            final WindowManager.LayoutParams floatWindowLayoutUpdateParam = floatWindowLayoutParamEnd;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

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

                        windowManager.updateViewLayout(floatEndSwipeView, floatWindowLayoutUpdateParam);
                        break;
                }

                return false;
            }
        });

        // =============================== End View Group ========================================== //


        viewList.add(floatStartSwipeView);
        viewList.add(floatEndSwipeView);
    }

    private void addScenario(double x, double y, double xx, double yy, String type,int time, int duration){
        Scenario scenario = new Scenario();
        scenario.setTime(time);
        scenario.setDuration(duration);
        scenario.setX(x);
        scenario.setY(y);
        scenario.setXx(xx);
        scenario.setYy(yy);
        scenario.setType(type);
        scenarioList.add(scenario);
    }

    private void updateScenario(int indexScenario,double x, double y, double xx, double yy, String type,int time, int duration){
        Scenario scenario =new Scenario();
        scenario.setTime(time);
        scenario.setDuration(duration);
        scenario.setX(x);
        scenario.setY(y);
        scenario.setXx(xx);
        scenario.setYy(yy);
        scenario.setType(type);
        scenarioList.set(indexScenario,scenario);
    }
}

