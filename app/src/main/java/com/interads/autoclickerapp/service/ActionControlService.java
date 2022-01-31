package com.interads.autoclickerapp.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.interads.autoclickerapp.view.FloatingControlView;
import com.interads.autoclickerapp.model.Scenario;

import java.util.ArrayList;

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
    public static final String EDIT = "edit";
    public static final String SAVE = "save";
    public String _mode;
    private FloatingControlView floatingControlView;
    private int _interval;

    private Handler _handler;
    private IntervaRunnable _intervaRunnable;

    private ArrayList<Scenario> listScenario;

    @Override
    public void onCreate() {
        super.onCreate();
        floatingControlView = new FloatingControlView(this);
        listScenario = floatingControlView.getListScenario();
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
            }else if(ADD_SWIPE.equals(action)){
                Log.i(ACTIVITY_TAG,"ADD ACTION SWIPE");
                floatingControlView.addActionDrawSwipe();
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

                Log.i(ACTIVITY_TAG,"Save");
                _handler.removeCallbacksAndMessages(null);
            }else if(EDIT.equals(action)){

                Log.i(ACTIVITY_TAG,"Edit");
                floatingControlView.removeView();
                _handler.removeCallbacksAndMessages(null);
            }


        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void Tapping(Scenario scenario) {
        Path path = new Path();
        path.moveTo(scenario.getX(), scenario.getY());
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription.StrokeDescription clickstroke = new GestureDescription.StrokeDescription(path, scenario.getTime(), scenario.getDuration());
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

    private void Swipe(Scenario scenario){

        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();

        path.moveTo(scenario.getX(),scenario.getY());
        path.lineTo(scenario.getXx(),scenario.getYy());

        builder.addStroke(new GestureDescription.StrokeDescription(path,scenario.getTime(),scenario.getDuration()));
        boolean isDispatch = dispatchGesture(builder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                Log.i(ACTIVITY_TAG,"Gesture Completed");
                super.onCompleted(gestureDescription);
            }
        }, null);

        Log.e(ACTIVITY_TAG,"Result Swipe " + isDispatch);
    }

    private  class IntervaRunnable implements Runnable{

        @Override
        public void run() {

            Log.i(ACTIVITY_TAG,"List Scenario Size = "+listScenario.size());

            for(int i = 0; i < listScenario.size();i++){
                Log.i(ACTIVITY_TAG,"=======================");
                Log.i(ACTIVITY_TAG,"TYPE ="+listScenario.get(i).getType());
                Log.i(ACTIVITY_TAG,"X ="+listScenario.get(i).getX());
                Log.i(ACTIVITY_TAG,"Y ="+listScenario.get(i).getY());
                Log.i(ACTIVITY_TAG,"XX ="+listScenario.get(i).getXx());
                Log.i(ACTIVITY_TAG,"YY ="+listScenario.get(i).getYy());
                Log.i(ACTIVITY_TAG,"TIME ="+listScenario.get(i).getTime());
                Log.i(ACTIVITY_TAG,"DURATION ="+listScenario.get(i).getDuration());
                Log.i(ACTIVITY_TAG,"=======================");
            }

        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }
}