package com.interads.autoclickerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;

public class FloatingView extends FrameLayout implements View.OnClickListener ,View.OnScrollChangeListener{
    private static final String ACTIVITY_TAG = "FloatingView";
    private Context context;
    private View floating_control_view;
    private WindowManager.LayoutParams floating_control_params;
    private WindowManager windowManager;
    public FloatingView(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {

    }

    @Override
    public AccessibilityNodeInfo createAccessibilityNodeInfo() {
        return super.createAccessibilityNodeInfo();
    }

    public void show(){

        // set view control action
        floating_control_params = new WindowManager.LayoutParams();
        floating_control_params.gravity = Gravity.LEFT;
        floating_control_params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        floating_control_params.format = PixelFormat.RGB_888;
        floating_control_params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
        floating_control_params.width =LayoutParams.WRAP_CONTENT;
        floating_control_params.height = LayoutParams.WRAP_CONTENT;
        windowManager.addView(floating_control_view,floating_control_params);

    }

    public void hide(){

    }
}