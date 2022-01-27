package com.interads.autoclickerapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.interads.autoclickerapp.helper.ConfigDataHelper;
import com.interads.autoclickerapp.model.Config;
import com.interads.autoclickerapp.service.ActionControlService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FloatingControlView extends FrameLayout implements View.OnClickListener {

    private static final String ACTIVITY_TAG = "Floating Control View";
    private Context _context;
    private View floatingControlView;
    private WindowManager _windowManager;
    private WindowManager.LayoutParams _layoutParam;
    private String currentState;
    private ConfigDataHelper configDataHelper;
    private Config lastConfig;

    private ImageView btn_action_play;
    private ImageView btn_action_pause;
    private ImageView btn_action_edit;
    private ImageView btn_action_save;
    private ImageView btn_action_add_swipe;
    private ImageView btn_action_add_new_click;
    private ImageView btn_action_minus;
    private ImageView btn_action_close;
    private ImageView btn_action_add;

    private ArrayList<View> viewActionList;
    private ArrayList<View> viewDrawerList;

    public FloatingControlView(@NonNull Context context) {
        super(context);

        configDataHelper = new ConfigDataHelper(getContext());
        ArrayList<HashMap<String,String>> rows = configDataHelper.getAllData();
        int lastRow = rows.size()-1;
        String id = rows.get(lastRow).get("id");
        String name = rows.get(lastRow).get("name");
        String app = rows.get(lastRow).get("app");
        String date = rows.get(lastRow).get("date");
        String status = rows.get(lastRow).get("status");

        lastConfig = new Config(Integer.parseInt(id),name,app,new Boolean(status),new Date(date));


        _context = context.getApplicationContext();
        viewActionList = new ArrayList<View>();
        viewDrawerList = new ArrayList<View>();

        LayoutInflater fcvInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        floatingControlView = fcvInflater.inflate(R.layout.floating_control_view,null);

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


        int indexChildView = viewActionList.size()+1;

        LayoutInflater fcvInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionClickView = fcvInflater.inflate(R.layout.click_view,null);
        actionClickView.setId(indexChildView);
        TextView number_action = actionClickView.findViewById(R.id.number_action_click);
        number_action.setText(String.valueOf(indexChildView));

        _layoutParam = new WindowManager.LayoutParams();
        _layoutParam.gravity = Gravity.CENTER;

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

                Log.i(ACTIVITY_TAG,"X ========== "+event.getX());
                Log.i(ACTIVITY_TAG,"Y ========== "+event.getY());

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

        actionClickView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click click ninuninuninu.", Toast.LENGTH_LONG).show();

            }
        });

        viewActionList.add(actionClickView);
    }

    public void addActionDrawSwipe(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _layoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            _layoutParam.type = WindowManager.LayoutParams.TYPE_TOAST;
        }

        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup drawSwipeLayoutView = (ViewGroup) inflater.inflate(R.layout.draw_layout_view,null);

        _layoutParam = new WindowManager.LayoutParams();
        _layoutParam.gravity = Gravity.CENTER;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _layoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            _layoutParam.type = WindowManager.LayoutParams.TYPE_TOAST;
        }

        _layoutParam.format = PixelFormat.RGBA_8888;
        _layoutParam.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
        _layoutParam.width = LayoutParams.MATCH_PARENT;
        _layoutParam.height = LayoutParams.MATCH_PARENT;

        _windowManager.addView(drawSwipeLayoutView,_layoutParam);
        drawSwipeLayoutView.addView(new DrawingView(getContext()));
        viewDrawerList.add(drawSwipeLayoutView);


    }

    private void drawSwipePosition(float xStart,float yStart,float xEnd,float yEnd){

        Log.i(ACTIVITY_TAG,"======== After Draw ====== ");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _layoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            _layoutParam.type = WindowManager.LayoutParams.TYPE_TOAST;
        }

        if(viewDrawerList.size() > 0){
            _windowManager.removeView(viewDrawerList.get(0));
            viewDrawerList.remove(0);
        }

        int indexChildView = viewActionList.size()+1;

        RelativeLayout parentSwipeLayout;
        parentSwipeLayout = new RelativeLayout(getContext());

        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup floatSwipeStartView = (ViewGroup) inflater.inflate(R.layout.start_swipe_view, null);
        floatSwipeStartView.setId(indexChildView);
        TextView number_action_start = floatSwipeStartView.findViewById(R.id.start_action_swipe);
        number_action_start.setText(String.valueOf(indexChildView));

        WindowManager.LayoutParams floatWindowLayoutParamStart = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                _layoutParam.type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatWindowLayoutParamStart.gravity = Gravity.TOP | Gravity.LEFT; // RESET X,Y AXIS WINDOW
        floatWindowLayoutParamStart.x = (int) xStart;
        floatWindowLayoutParamStart.y = (int) yStart;


        ViewGroup floatSwipeEndView = (ViewGroup) inflater.inflate(R.layout.end_swipe_view, null);
        floatSwipeEndView.setId(indexChildView);
        TextView number_action_end = floatSwipeEndView.findViewById(R.id.end_action_swipe);
        number_action_end.setText(String.valueOf(indexChildView));
        WindowManager.LayoutParams floatWindowLayoutParamEnd = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                _layoutParam.type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );


        floatWindowLayoutParamEnd.gravity = Gravity.TOP | Gravity.LEFT; // RESET X,Y AXIS WINDOW
        floatWindowLayoutParamEnd.x = (int) xEnd;
        floatWindowLayoutParamEnd.y = (int) yEnd;

        // === PARENT SWIPE POSITION === //
        int heightParent = (int) yEnd - (int) yStart;
        int widthParent = (int) xEnd -(int) xStart;
        parentSwipeLayout.setMinimumHeight(heightParent);
        parentSwipeLayout.setMinimumWidth(widthParent);
        parentSwipeLayout.setBackgroundColor(Color.parseColor("#59673AB7"));
        parentSwipeLayout.addView(floatSwipeStartView,floatWindowLayoutParamStart);
        parentSwipeLayout.addView(floatSwipeEndView,floatWindowLayoutParamEnd);

        WindowManager.LayoutParams floatWindowParentLayoutParam = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                _layoutParam.type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatWindowParentLayoutParam.gravity = Gravity.TOP | Gravity.LEFT;
        floatWindowParentLayoutParam.x = (int) xStart;
        floatWindowParentLayoutParam.y =(int) yStart;

        _windowManager.addView(parentSwipeLayout,floatWindowParentLayoutParam);
        viewActionList.add(parentSwipeLayout);
    }

    public void removeView(){
        int indexChildView = viewActionList.size();
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

        Intent main_intent = new Intent(getContext(),MainActivity.class);
        main_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getContext().startActivity(main_intent);

    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(getContext(), ActionControlService.class);
        switch (view.getId()){
            case R.id.action_play:

                currentState = ActionControlService.PLAY;
                intent.putExtra(ActionControlService.ACTION, ActionControlService.PLAY);
                break;
            case R.id.action_pause:
                _layoutParam.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
                currentState = ActionControlService.STOP;
                intent.putExtra(ActionControlService.ACTION, ActionControlService.STOP);
                break;
            case R.id.action_close:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.CLOSE);
                break;
            case R.id.action_new_click:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.ADD_TAP);
                break;
            case R.id.action_add_swipe:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.ADD_SWIPE);
                break;
            case R.id.action_add:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.PLUS);
                break;
            case R.id.action_minus:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.MINUS);
                break;
            case R.id.action_edit:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.EDIT);
                break;
            case R.id.action_save:
                intent.putExtra(ActionControlService.ACTION, ActionControlService.SAVE);
                break;
        }

        getContext().startService(intent);

    }

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
            circlePaint.setColor(Color.parseColor("#F9C94F"));
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(12f);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(Color.parseColor("#9B5DB0"));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(75);
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
        private static final float TOUCH_TOLERANCE = 0;


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

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            Log.i(ACTIVITY_TAG,"X ========= "+x);
            Log.i(ACTIVITY_TAG,"Y ========= "+y);

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
}
