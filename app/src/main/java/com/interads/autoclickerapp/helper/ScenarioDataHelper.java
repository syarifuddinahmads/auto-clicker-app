package com.interads.autoclickerapp.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.interads.autoclickerapp.model.Config;
import com.interads.autoclickerapp.model.Scenario;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ScenarioDataHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "autoclickers.db";
    private static final String TABLE_CONFIG = "config";
    private static final String TABLE_CONFIG_DETAIL = "config_detail";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_APP = "app";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_ID_CONFIG = "id_config";
    public static final String COLUMN_X = "x";
    public static final String COLUMN_Y = "y";
    public static final String COLUMN_XX = "xx";
    public static final String COLUMN_YY = "yy";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_ORDER = "order_config";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_TIME = "time";

    final String SQL_CONFIG = "CREATE TABLE "+TABLE_CONFIG+" ("
            +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
            +COLUMN_NAME+" VARCHAR ,"
            +COLUMN_APP+" VARCHAR ,"
            +COLUMN_DATE+" DATE ,"
            +COLUMN_STATUS+" BOOLEAN"
            +")";

    final String SQL_CONFIG_DETAIL = "CREATE TABLE "+TABLE_CONFIG_DETAIL +" ("
            +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_ID_CONFIG+" INTEGER NOT NULL ,"
            +COLUMN_ORDER+" INTEGER NOT NULL ,"
            +COLUMN_TYPE+" INTEGER NOT NULL ,"
            +COLUMN_X+" VARCHAR NOT NULL,"
            +COLUMN_Y+" VARCHAR NOT NULL,"
            +COLUMN_XX+" VARCHAR ,"
            +COLUMN_YY+" VARCHAR ,"
            +COLUMN_TIME+" INT ,"
            +COLUMN_DURATION+" INT "
            +" )";

    public ScenarioDataHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CONFIG);
        sqLiteDatabase.execSQL(SQL_CONFIG_DETAIL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_CONFIG);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_CONFIG_DETAIL);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<HashMap<String,String>> getAllDataConfig(){
        ArrayList<HashMap<String, String>> configList;
        configList = new ArrayList<HashMap<String, String>>();

        String SQL = "SELECT * FROM "+TABLE_CONFIG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if(cursor.moveToFirst()){
            do{

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_NAME, cursor.getString(1));
                map.put(COLUMN_APP, cursor.getString(2));
                map.put(COLUMN_DATE, cursor.getString(3));
                map.put(COLUMN_STATUS, cursor.getString(4));

                configList.add(map);

            }while (cursor.moveToNext());
        }

        db.close();

        return configList;
    }

    public void insertConfig(String name, String app, Date date, Boolean status){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "INSERT INTO "+TABLE_CONFIG+" (name,app,date,status) VALUES ('"+ name +"','"+app+"','"+date+"','"+status+"')";
        db.execSQL(SQL);
        db.close();
    }

    public void updateConfig(int id,String name, String app, Date date,Boolean status){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE "+TABLE_CONFIG
                +" SET "+COLUMN_NAME+"='"+name+"', "
                +" SET "+COLUMN_APP+"='"+app+"', "
                +" SET "+COLUMN_DATE+"='"+date+"', "
                +" SET "+COLUMN_STATUS+"='"+status+"' "
                +" WHERE "+COLUMN_ID+"='"+id+"'";
        db.execSQL(SQL);
        db.close();
    }

    public void deleteConfig(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "DELETE FROM "+TABLE_CONFIG+" WHERE "+COLUMN_ID+"="+"'"+id+"'";
        db.execSQL(SQL);
        db.close();
    }

    public void activateConfig(int id,Boolean status){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE "+TABLE_CONFIG
                +" SET "+COLUMN_STATUS+"='"+status+"' "
                +" WHERE "+COLUMN_ID+"='"+id+"'";
        db.execSQL(SQL);

        String SQL_DEACTIVATE = "UPDATE "+TABLE_CONFIG
                +" SET "+COLUMN_STATUS+"='false'"
                +" WHERE "+COLUMN_ID+"!='"+id+"'";
        db.execSQL(SQL_DEACTIVATE);

        db.close();
    }

    public ArrayList<HashMap<String,String>> getAllDataConfigDetail(){
        ArrayList<HashMap<String, String>> configDetailList;
        configDetailList = new ArrayList<HashMap<String, String>>();

        String SQL = "SELECT * FROM "+TABLE_CONFIG_DETAIL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if(cursor.moveToFirst()){
            do{

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_ID_CONFIG, cursor.getString(1));
                map.put(COLUMN_ORDER, cursor.getString(2));
                map.put(COLUMN_TYPE, cursor.getString(3));
                map.put(COLUMN_X, cursor.getString(4));
                map.put(COLUMN_Y, cursor.getString(5));
                map.put(COLUMN_XX, cursor.getString(6));
                map.put(COLUMN_YY, cursor.getString(7));
                map.put(COLUMN_TIME, cursor.getString(8));
                map.put(COLUMN_DURATION, cursor.getString(9));

                configDetailList.add(map);

            }while (cursor.moveToNext());
        }

        db.close();

        return configDetailList;
    }

    public void insertConfigDetail(int idConfig, int orderConfig, int type, float x, float y, float xx, float yy, int time, int duration){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "INSERT INTO "+TABLE_CONFIG_DETAIL+" (id_config,order_config,type,x,y,xx,yy,time,duration) VALUES ('"+ idConfig +"','"+orderConfig+"','"+type+"','"+x+"','"+y+"','"+xx+"','"+yy+"','"+time+"','"+duration+"')";
        db.execSQL(SQL);
        db.close();
    }

    public void updateConfigDetail(int id,int idConfig,int orderConfig,int type,String x,String y,String xx,String yy,int time,int duration){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE "+TABLE_CONFIG_DETAIL+" SET "+COLUMN_ID_CONFIG+"='"+idConfig+"', "
                +" SET "+COLUMN_ORDER+"='"+orderConfig+"', "
                +" SET "+COLUMN_TYPE+"='"+type+"', "
                +" SET "+COLUMN_X+"='"+x+"', "
                +" SET "+COLUMN_Y+"='"+y+"', "
                +" SET "+COLUMN_XX+"='"+xx+"', "
                +" SET "+COLUMN_YY+"='"+yy+"', "
                +" SET "+COLUMN_TIME+"='"+time+"', "
                +" SET "+COLUMN_DURATION+"='"+duration+"'"
                +" WHERE "+COLUMN_ID+"='"+id+"'";
        db.execSQL(SQL);
        db.close();
    }

    public void deleteConfigDetail(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "DELETE FROM "+TABLE_CONFIG_DETAIL+" WHERE "+COLUMN_ID+"="+"'"+id+"'";
        db.execSQL(SQL);
        db.close();
    }

    public void deleteConfigDetailByConfigId(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "DELETE FROM "+TABLE_CONFIG_DETAIL+" WHERE "+COLUMN_ID_CONFIG+"="+"'"+id+"'";
        db.execSQL(SQL);
        db.close();
    }
}
