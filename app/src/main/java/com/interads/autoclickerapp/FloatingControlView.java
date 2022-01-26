package com.interads.autoclickerapp;

import android.content.Context;
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

import java.util.ArrayList;

public class FloatingControlView extends FrameLayout implements View.OnClickListener {

    private static final String FLOATING_CONTROL_VIEW_TAG = "Floating Control View";
    private Context _context;
    private View floatingControlView;
    private WindowManager _windowManager;
    private WindowManager.LayoutParams _layoutParam;
    private String currentState;

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
    private ArrayList<View> viewActionList;

    public FloatingControlView(@NonNull Context context) {
        super(context);

        _context = context.getApplicationContext();
        viewActionList = new ArrayList<View>();

        LayoutInflater fcvInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        floatingControlView = fcvInflater.inflate(R.layout.floating_control_view,null);

        // set floating view action id
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

        floatingControlView.setOnTouchListener(_onTouchListener);
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

        DisplayMetrics metrics = _context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        LayoutInflater fcvInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionClickView = fcvInflater.inflate(R.layout.click_view,null);
        actionClickView.setId(indexChildView);
        TextView number_action = actionClickView.findViewById(R.id.number_action_click);
        number_action.setText(String.valueOf(indexChildView+1));

        _layoutParam = new WindowManager.LayoutParams();
        _layoutParam.gravity = Gravity.TOP | Gravity.LEFT;
        _layoutParam.x = width/2;
        _layoutParam.y = height/2;

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
                Toast.makeText(getContext(), "Play", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_pause:
                _layoutParam.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
                currentState = ActionControlService.STOP;
                intent.putExtra(ActionControlService.ACTION, ActionControlService.STOP);
                Toast.makeText(getContext(), "Pause", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_close:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.CLOSE);
                Toast.makeText(getContext(), "Close", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_new_click:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.ADD_TAP);
                Toast.makeText(getContext(), "Add New Click", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_add_swipe:
                Toast.makeText(getContext(), "Add New Swipe", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_add:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.PLUS);
                Toast.makeText(getContext(), "Plus", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_minus:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.MINUS);
                Toast.makeText(getContext(), "Minus", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_edit:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.EDIT);
                Toast.makeText(getContext(), "Edit", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_save:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.SAVE);
                Toast.makeText(getContext(), "Save", Toast.LENGTH_LONG).show();
                break;
        }

        getContext().startService(intent);

    }

    private OnTouchListener _onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
            }

            return true;
        }
    };
}
