package com.interads.autoclickerapp;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup swipeLayoutView = (ViewGroup) inflater.inflate(R.layout.swipe_layout_view,null);
        swipeLayoutView.setId(indexChildView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }

        floatWindowLayoutParam = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatWindowLayoutParam.gravity = Gravity.CENTER;
        floatWindowLayoutParam.x = 0;
        floatWindowLayoutParam.y = 0;
        windowManager.addView(swipeLayoutView,floatWindowLayoutParam);

        // add drawing swipe layout to window
        swipeLayoutView.addView(new DrawingView(getApplicationContext()));

        // add to list view
        viewList.add(swipeLayoutView);

        // close draw action view
        View close_draw_action = (View) swipeLayoutView.findViewById(R.id.close_draw_action);
        close_draw_action.setOnClickListener(new View.OnClickListener() {
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

    private void closeSwipeLayout(ViewGroup viewGroupSwipeLayout){
        if(indexChildView != 0 && indexChildView > 0){
            int totalIndexView = viewList.size()-1;
            windowManager.removeView(viewList.get(totalIndexView));
            viewList.remove(totalIndexView);
            indexChildView--;
        }
    }

    // =============== DRAWING VIEW CLASS =============== //
    public class DrawingView extends View {
        private Paint mPaint;
        public int width;
        public  int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(Color.GREEN);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(25);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
            canvas.drawPath( circlePath,  circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }

}

