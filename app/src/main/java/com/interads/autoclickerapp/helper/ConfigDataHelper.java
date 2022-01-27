package com.interads.autoclickerapp.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ConfigDataHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "autoclicker.db";
    public static final String TABLE_NAME = "config";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_APP = "app";
    public static final String COLUMN_STATUS = "status";

    public ConfigDataHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL = "CREATE TABLE "+TABLE_NAME+" ("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +COLUMN_NAME+" VARCHAR ,"
                +COLUMN_APP+" VARCHAR ,"
                +COLUMN_DATE+" DATE ,"
                +COLUMN_STATUS+" BOOLEAN"
                +")";
        sqLiteDatabase.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<HashMap<String,String>> getAllData(){
        ArrayList<HashMap<String, String>> configList;
        configList = new ArrayList<HashMap<String, String>>();

        String SQL = "SELECT * FROM "+TABLE_NAME;
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

    public void insert(String name, String app, Date date,Boolean status){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "INSERT INTO "+TABLE_NAME+" (name,app,date,status) VALUES ('"+ name +"','"+app+"','"+date+"','"+status+"')";
        db.execSQL(SQL);
        db.close();
    }

    public void update(int id,String name, String app, Date date,Boolean status){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE "+TABLE_NAME
                +" SET "+COLUMN_NAME+"='"+name+"', "
                +" SET "+COLUMN_APP+"='"+app+"', "
                +" SET "+COLUMN_DATE+"='"+date+"', "
                +" SET "+COLUMN_STATUS+"='"+status+"' "
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

    public void activate_config(int id,Boolean status){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE "+TABLE_NAME
                +" SET "+COLUMN_STATUS+"='"+status+"' "
                +" WHERE "+COLUMN_ID+"='"+id+"'";
        db.execSQL(SQL);

        String SQL_DEACTIVATE = "UPDATE "+TABLE_NAME
                +" SET "+COLUMN_STATUS+"='false'"
                +" WHERE "+COLUMN_ID+"!='"+id+"'";
        db.execSQL(SQL_DEACTIVATE);

        db.close();
    }

}
