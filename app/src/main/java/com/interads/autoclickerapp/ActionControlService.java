package com.interads.autoclickerapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Service;
import android.content.Intent;
import android.graphics.Path;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class ActionControlService extends AccessibilityService {
    public static final String ACTIVITY_TAG = "Auto Control Service";
    public static final String ACTION = "action";
    public static final String SHOW = "show";
    public static final String HIDE = "hide";
    public static final String CLOSE = "close";
    public static final String PLAY = "play";
    public static final String STOP = "stop";
    public static final String MODE = "mode";
    public static final String TAP = "tap";
    public static final String SWIPE = "swipe";
    public static final String ADD_TAP = "add_tap";
    public static final String ADD_SWIPE = "add_swipe";
    public static final String PLUS = "plus";
    public static final String MINUS = "minus";
    public static final String EDIT = "minus";
    public static final String SAVE = "minus";
    public String _mode;
    private FloatingControlView floatingControlView;
    private int _interval;

    private Handler _handler;
    private IntervaRunnable _intervaRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        floatingControlView = new FloatingControlView(this);
        HandlerThread handlerThread = new HandlerThread("auto-handler");
        handlerThread.start();
        _handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null){

            Log.i(ACTIVITY_TAG,"=========== ACTION RUNNING ===========");

            String action = intent.getStringExtra(ACTION);

            if(SHOW.equals(action)){

                Log.i(ACTIVITY_TAG,"SHOW");

                _interval = intent.getIntExtra("interval",10)*1000;
                _mode = intent.getStringExtra(MODE);
                floatingControlView.showFloatingControlView();
            }else if(HIDE.equals(action)){

                Log.i(ACTIVITY_TAG,"HIDE");

                floatingControlView.hideFloatingControlView();
                _handler.removeCallbacksAndMessages(null);
                Toast.makeText(getBaseContext(), "AutoClicker closed.", Toast.LENGTH_LONG).show();
            }else if(CLOSE.equals(action)){

                Log.i(ACTIVITY_TAG,"CLOSE");

                floatingControlView.closeView();
                _handler.removeCallbacksAndMessages(null);
                Toast.makeText(getBaseContext(), "AutoClicker closed.", Toast.LENGTH_LONG).show();
            } else if(PLAY.equals(action)){

                Log.i(ACTIVITY_TAG,"PLAY");

                if (_intervaRunnable == null){
                    _intervaRunnable = new IntervaRunnable();
                }
                _handler.postDelayed(_intervaRunnable,_interval);

            }else if(STOP.equals(action)){

                Log.i(ACTIVITY_TAG,"STOP");
                _handler.removeCallbacksAndMessages(null);
            }else if(ADD_TAP.equals(action)){

                Log.i(ACTIVITY_TAG,"ADD ACTION TAP");
                floatingControlView.addActionClick();
                _handler.removeCallbacksAndMessages(null);
            }else if(PLUS.equals(action)){

                Log.i(ACTIVITY_TAG,"PLUS");
                floatingControlView.addActionClick();
                _handler.removeCallbacksAndMessages(null);
            }else if(MINUS.equals(action)){

                Log.i(ACTIVITY_TAG,"MINUS");
                floatingControlView.removeView();
                _handler.removeCallbacksAndMessages(null);
            }else if(SAVE.equals(action)){

                Log.i(ACTIVITY_TAG,"PLUS");
                floatingControlView.addActionClick();
                _handler.removeCallbacksAndMessages(null);
            }else if(EDIT.equals(action)){

                Log.i(ACTIVITY_TAG,"MINUS");
                floatingControlView.removeView();
                _handler.removeCallbacksAndMessages(null);
            }


        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void Tapping(int x, int y) {
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription.StrokeDescription clickstroke = new GestureDescription.StrokeDescription(path, 0, 50);
        builder.addStroke(clickstroke);
        GestureDescription gestureDescription = builder.build();
        Log.e(ACTIVITY_TAG,"built.");
        boolean isDispatch = this.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback(){
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                Log.e(ACTIVITY_TAG,"touch success.");
                super.onCompleted(gestureDescription);
                _handler.postDelayed(_intervaRunnable, _interval);
                Log.e(ACTIVITY_TAG,"Just a test.");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                Log.e(ACTIVITY_TAG,"Didn't touch anything.");
                super.onCancelled(gestureDescription);
            }
        }, null);

        Log.e(ACTIVITY_TAG,"The result is " + isDispatch);
    }

    private  class IntervaRunnable implements Runnable{

        @Override
        public void run() {

        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }
}