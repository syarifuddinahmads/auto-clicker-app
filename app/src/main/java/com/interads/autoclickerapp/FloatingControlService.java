package com.interads.autoclickerapp;

import android.app.Dialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.interads.autoclickerapp.model.Config;
import com.interads.autoclickerapp.model.ConfigDetail;
import com.interads.autoclickerapp.model.Scenario;

import java.util.ArrayList;

public class FloatingControlService extends Service {

    private static final String FLOAT_CONTROL = "FLOAT CONTROL";
    private ViewGroup floatView;
    private int LAYOUT_TYPE;
    private WindowManager.LayoutParams floatWindowLayoutParam;
    private WindowManager windowManager;
    private Context context;

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
    private int indexChildViewDraw = 0;
    private ArrayList<ViewGroup> viewList;
    private ArrayList<ViewGroup> viewListDraw;
    private ArrayList<Scenario> scenarioList;

    @Override
    public void onCreate() {
        super.onCreate();

        viewList = new ArrayList<ViewGroup>();
        viewListDraw = new ArrayList<ViewGroup>();
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

        // action add new click position
        btn_action_add_new_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActionClick();
                indexChildView++;
            }
        });

        // action add swipe
        btn_action_add_swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActionDrawSwipe();
                indexChildView++;
            }
        });

        // action play temp scenario
        btn_action_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // action pause temp scenario
        btn_action_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // action edit scenario
        btn_action_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    // === FLOATING CONTROL VIEW === //
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

    private void addActionClick(){

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
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        windowManager.addView(floatClickView, floatWindowLayoutParam);
        addScenario(floatWindowLayoutParam.x,floatWindowLayoutParam.y,0,0,"click",0,0);

        floatClickView.setOnTouchListener(new View.OnTouchListener() {

            int indexScenario = Integer.parseInt(number_action.getText().toString()) - 1;
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

                        // update temp scenario list
                        updateScenario(indexScenario,floatWindowLayoutUpdateParam.x,floatWindowLayoutUpdateParam.y,0,0,"click",0,0);

                        break;
                }

                return false;
            }
        });

        viewList.add(floatClickView);
    }

    // === ACTION DRAW SWIPE VIEW === //
    private void addActionDrawSwipe(){

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup drawSwipeLayoutView = (ViewGroup) inflater.inflate(R.layout.draw_layout_view,null);
        drawSwipeLayoutView.setId(indexChildViewDraw);

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
        windowManager.addView(drawSwipeLayoutView,floatWindowLayoutParam);

        // add drawing swipe layout to window
        drawSwipeLayoutView.addView(new DrawingView(getApplicationContext()));

        // add to list view
        viewList.add(drawSwipeLayoutView);



    }

    private void drawSwipePosition(float xStart,float yStart,float xEnd,float yEnd){

        // ===== REMOVE LAST VIEW LAYOUT AFTER DRAW ===== //
        if(indexChildView != 0 && indexChildView > 0){
            float totalIndexView = viewList.size()-1;
            windowManager.removeView(viewList.get((int) totalIndexView));
            viewList.remove(totalIndexView);
            indexChildView--;
        }

        // ===== START POINT SWIPE ===== //

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup floatSwipeStartView = (ViewGroup) inflater.inflate(R.layout.start_swipe_view, null);
        floatSwipeStartView.setId(indexChildView);
        TextView number_action_start = floatSwipeStartView.findViewById(R.id.start_action_swipe);
        number_action_start.setText(String.valueOf(indexChildView+1));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }


        WindowManager.LayoutParams floatWindowLayoutParamStart = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatWindowLayoutParamStart.gravity = Gravity.TOP | Gravity.LEFT;
        floatWindowLayoutParamStart.x = (int) xStart;
        floatWindowLayoutParamStart.y = (int) yStart;
        windowManager.addView(floatSwipeStartView, floatWindowLayoutParamStart);


        // ===== END POINT SWIPE ===== //
        ViewGroup floatSwipeEndView = (ViewGroup) inflater.inflate(R.layout.end_swipe_view, null);
        floatSwipeEndView.setId(indexChildView);
        TextView number_action_end = floatSwipeEndView.findViewById(R.id.end_action_swipe);
        number_action_end.setText(String.valueOf(indexChildView+1));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }


        WindowManager.LayoutParams floatWindowLayoutParamEnd = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatWindowLayoutParamEnd.gravity = Gravity.TOP | Gravity.LEFT;
        floatWindowLayoutParamEnd.x = (int) xEnd;
        floatWindowLayoutParamEnd.y = (int) yEnd;
        windowManager.addView(floatSwipeEndView, floatWindowLayoutParamEnd);



    }

    private void _drawSwipePosition(float xStart,float yStart,float xEnd,float yEnd,float centerPointWidth,float centerPointHeight){

        float totalIndexView = viewListDraw.size()-1;
        windowManager.removeView(viewListDraw.get((int) totalIndexView));
        viewListDraw.remove(totalIndexView);


        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup floatClickView = (ViewGroup) inflater.inflate(R.layout.swipe_layout_view, null);
        floatClickView.setId(indexChildView);
        TextView number_action_start = floatClickView.findViewById(R.id.start_action_swipe);
        number_action_start.setText(String.valueOf(indexChildView+1));
        TextView number_action_end = floatClickView.findViewById(R.id.end_action_swipe);
        number_action_end.setText(String.valueOf(indexChildView+1));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }


        floatWindowLayoutParam = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatClickView.setMinimumHeight((int)(xEnd));
        windowManager.addView(floatClickView, floatWindowLayoutParam);
        addScenario(floatWindowLayoutParam.x,floatWindowLayoutParam.y,0,0,"swipe",0,0);

        floatClickView.setOnTouchListener(new View.OnTouchListener() {

            int indexScenario = Integer.parseInt(number_action_start.getText().toString()) - 1;
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

                        // update temp scenario list
                        updateScenario(indexScenario,floatWindowLayoutUpdateParam.x,floatWindowLayoutUpdateParam.y,0,0,"click",0,0);

                        break;
                }

                return false;
            }
        });

        viewList.add(floatClickView);
        indexChildView++;


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

        // === TOUCH START X,Y COORDINATE === //
        private float mX, mY,xStart,yStart;
        private static final float TOUCH_TOLERANCE = 2;


        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            xStart = x;
            yStart = y;
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

        public int dpToPx(int dp) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }
        public int pxToDp(int px) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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
                    drawSwipePosition((int) xStart,(int) yStart,(int) x,(int) y);
                    break;
            }
            return true;
        }
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

        for(int i = 0;i< scenarioList.size();i++){
//            Log.i(FLOAT_CONTROL,"=============== "+i+" ================");
//            Log.i(FLOAT_CONTROL,"X = "+scenarioList.get(i).getX());
//            Log.i(FLOAT_CONTROL,"Y = "+scenarioList.get(i).getY());
//            Log.i(FLOAT_CONTROL,"XX = "+scenarioList.get(i).getXx());
//            Log.i(FLOAT_CONTROL,"YY = "+scenarioList.get(i).getYy());
//            Log.i(FLOAT_CONTROL,"Time = "+scenarioList.get(i).getTime());
//            Log.i(FLOAT_CONTROL,"Duration = "+scenarioList.get(i).getDuration());
//            Log.i(FLOAT_CONTROL,"Type = "+scenarioList.get(i).getType());
//            Log.i(FLOAT_CONTROL,"===============================");
        }

    }



}

