package com.interads.autoclickerapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.interads.autoclickerapp.model.ConfigDetail;
import com.interads.autoclickerapp.model.Scenario;

import java.util.ArrayList;

public class PlayScenarioService extends AccessibilityService {

    private static final String PLAY_SCENARIO_SERVICE = "Play Scenario Service";
    public static final String PLAY = "play";
    public static final String STOP = "stop";
    public String actionScenario;
    private int interval = 10;
    private IntervalRunnable intervalRunnable;
    private Handler handler;
    private ArrayList<Scenario> scenarioList;


    PlayScenarioService(ArrayList<Scenario> scenarios,String action){
        scenarioList = scenarios;
        actionScenario = action;
        HandlerThread handlerThread = new HandlerThread("auto-handler");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        intervalRunnable = new IntervalRunnable();

        if (actionScenario.equals("play")) {
            Log.i(PLAY_SCENARIO_SERVICE,"Play Scenario");
            Log.i(PLAY_SCENARIO_SERVICE,"Interval Runnable 1 = "+intervalRunnable);
            handler.postDelayed(intervalRunnable, interval);
        } else if (actionScenario.equals("stop")) {
            Log.i(PLAY_SCENARIO_SERVICE,"Stop Scenario");
            Log.i(PLAY_SCENARIO_SERVICE,"Interval Runnable 3 = "+intervalRunnable);
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }

    public void tapping(Scenario scenario){
        Path path = new Path();
        path.moveTo(scenario.getX(),scenario.getY());
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription.StrokeDescription strokeDescription = new GestureDescription.StrokeDescription(path,scenario.getTime(),scenario.getDuration());
        builder.addStroke(strokeDescription);
        GestureDescription gestureDescription = builder.build();

        boolean isDispatch = this.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback(){
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.i(PLAY_SCENARIO_SERVICE,"Interval Runnable 2 = "+intervalRunnable);
                handler.postDelayed(intervalRunnable, interval);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);

        Log.e(PLAY_SCENARIO_SERVICE,"Result Tap " + isDispatch);
    }

    private void swipe(Scenario scenario){

        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();

        path.moveTo(scenario.getX(),scenario.getY());
        path.lineTo(scenario.getXx(),scenario.getYy());

        builder.addStroke(new GestureDescription.StrokeDescription(path,scenario.getTime(),scenario.getDuration()));
        boolean isDispatch = dispatchGesture(builder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                Log.i(PLAY_SCENARIO_SERVICE,"Gesture Completed");
                super.onCompleted(gestureDescription);
            }
        }, null);

        Log.e(PLAY_SCENARIO_SERVICE,"Result Swipe " + isDispatch);
    }

    private class IntervalRunnable implements Runnable{

        @Override
        public void run() {
            for (int i=0;i<scenarioList.size();i++){
                Log.i(PLAY_SCENARIO_SERVICE,"=== "+scenarioList.get(i).getType()+" ===");
                Log.i(PLAY_SCENARIO_SERVICE,"X "+scenarioList.get(i).getX());
                Log.i(PLAY_SCENARIO_SERVICE,"Y "+scenarioList.get(i).getY());
                Log.i(PLAY_SCENARIO_SERVICE,"XX "+scenarioList.get(i).getXx());
                Log.i(PLAY_SCENARIO_SERVICE,"YY "+scenarioList.get(i).getYy());
                Log.i(PLAY_SCENARIO_SERVICE,"Time "+scenarioList.get(i).getTime());
                Log.i(PLAY_SCENARIO_SERVICE,"Duration "+scenarioList.get(i).getDuration());
                Log.i(PLAY_SCENARIO_SERVICE,"=== "+scenarioList.get(i).getType()+" ===");
                if(scenarioList.get(i).getType().equals("click")){
                    tapping(scenarioList.get(i));
                }else{
                    swipe(scenarioList.get(i));
                }
            }
        }
    }
}
