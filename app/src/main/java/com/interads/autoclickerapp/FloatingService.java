package com.interads.autoclickerapp;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.accessibility.AccessibilityEvent;

public class FloatingService extends AccessibilityService {

    public static final String ACTIVITY_TAG = "FloatingService";
    public static final String ACTION = "action";
    public static final String SHOW = "show";
    public static final String HIDE = "hide";
    public static final String PLAY = "play";
    public static final String STOP = "stop";
    public static final String MODE = "mode";
    public static final String TAP = "tap";
    public static final String SWIPE = "swipe";
    public static final String SAVE = "save";
    public static final String EDIT = "edit";
    public static final String ADD = "add";
    public static final String MINUS = "minus";
    public static final String CLOSE = "close";
    public static final String PAUSE = "pause";


    private FloatingView floatingView;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        // set floating view and thread handling
        floatingView = new FloatingView(this);
        HandlerThread handlerThread = new HandlerThread("auto-handler");
        handlerThread.start();
        handler =new Handler(handlerThread.getLooper());
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent !=null){

            String action = intent.getStringExtra(ACTION);
            if (SHOW.equals(action)){
                floatingView.show();
            }else if (HIDE.equals(action)){

            }else if (CLOSE.equals(action)){

            }else if (PLAY.equals(action)){

            }else if (PAUSE.equals(action)){

            }else if (STOP.equals(action)){

            }else if (CLOSE.equals(action)){

            }else if (ADD.equals(action)){

            }else if (MINUS.equals(action)){

            }

        }

        return super.onStartCommand(intent, flags, startId);
    }
}
