package com.interads.autoclickerapp.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigDetailDataHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "autoclicker.db";
    public static final String TABLE_NAME = "config_detail";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_CONFIG = "id_config";
    public static final String COLUMN_X = "x";
    public static final String COLUMN_Y = "y";
    public static final String COLUMN_XX = "xx";
    public static final String COLUMN_YY = "yy";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_ORDER = "order_config";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_TIME = "time";

    public ConfigDetailDataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL = "CREATE TABLE "+TABLE_NAME +" ("
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
        sqLiteDatabase.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<HashMap<String,String>> getAllData(){
        ArrayList<HashMap<String, String>> configDetailList;
        configDetailList = new ArrayList<HashMap<String, String>>();

        String SQL = "SELECT * FROM "+TABLE_NAME;
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

    public void insert(int idConfig,int orderConfig,int type,String x,String y,String xx,String yy,int time,int duration){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "INSERT INTO "+TABLE_NAME+" (id_config,order_config,type,x,y,xx,yy,time,duration) VALUES ('"+ idConfig +"','"+orderConfig+"','"+type+"','"+x+"','"+y+"','"+xx+"','"+yy+"','"+time+"','"+duration+"')";
        db.execSQL(SQL);
        db.close();
    }

    public void update(int id,int idConfig,int orderConfig,int type,String x,String y,String xx,String yy,int time,int duration){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE "+TABLE_NAME+" SET "+COLUMN_ID_CONFIG+"='"+idConfig+"', "
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

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "DELETE FROM "+TABLE_NAME+" WHERE "+COLUMN_ID+"="+"'"+id+"'";
        db.execSQL(SQL);
        db.close();
    }
}
