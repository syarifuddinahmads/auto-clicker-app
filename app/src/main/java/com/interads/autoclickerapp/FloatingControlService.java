package com.interads.autoclickerapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FloatingControlService extends Service {

    private ViewGroup floatView;
    private int LAYOUT_TYPE;
    private WindowManager.LayoutParams floatWindowLayoutParam;
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

    @Override
    public void onCreate() {
        super.onCreate();

        viewList = new ArrayList<ViewGroup>();

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


        // action close floating control view
        btn_action_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
                windowManager.removeView(floatView);
                Intent main_intent = new Intent(FloatingControlService.this,MainActivity.class);
                main_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main_intent);
            }
        });


        // action add new action click (default)
        btn_action_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_action_click();
                indexChildView++;
            }
        });

        // action minus action click (default)
        btn_action_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                add_action_click();
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

    private void add_action_click(){
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

}
