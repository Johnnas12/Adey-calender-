package com.androidcalenderproject.ethiocalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final  String CREATE_EVENTS_TABLE=" CREATE TABLE "+ DBStructure.EVENT_TABLE_NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBStructure.EVENT +" TEXT, " +DBStructure.TIME+" TEXT, "+ DBStructure.DATE+" TEXT, " +DBStructure.MONTH+" TEXT, "
            +DBStructure.YEAR+" TEXT )";
    private static  final String DROP_EVENT_TABLE ="DROP TABLE IF EXISTS " +DBStructure.EVENT_TABLE_NAME;



    public DBOpenHelper( Context context) {
        super(context, DBStructure.DB_Name, null, DBStructure.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//TO CREATE DATABASE
        db.execSQL(CREATE_EVENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//TO
        db.execSQL(DROP_EVENT_TABLE);
        onCreate(db);

    }
    public void SaveEvent(String event,String time,String date,String month,String year,SQLiteDatabase database){
        ContentValues contentValues= new ContentValues();
        contentValues.put(DBStructure.EVENT, event);
        contentValues.put(DBStructure.TIME, time);
        contentValues.put(DBStructure.DATE, date);
        contentValues.put(DBStructure.MONTH, month);
        contentValues.put(DBStructure.YEAR, year);
        database.insert(DBStructure.EVENT_TABLE_NAME,null,contentValues);




    }
    public Cursor ReadEvent(String date,SQLiteDatabase database) {
        String [] Projection ={DBStructure.EVENT,DBStructure.TIME,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR};
        String  Selection= DBStructure.DATE + "=?";
        String []SelectionArgs = {date};
        return database.query(DBStructure.EVENT_TABLE_NAME, Projection,Selection,SelectionArgs,null,null,null);

    }
    public Cursor ReadEventperMonth(String month,String year,SQLiteDatabase database) {
        String [] Projection ={DBStructure.EVENT,DBStructure.TIME, DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR};
        String  Selection= DBStructure.MONTH+ "=? and "+ DBStructure.YEAR+"=?";
        String []SelectionArgs = {month,year};
        return database.query(DBStructure.EVENT_TABLE_NAME, Projection,Selection,SelectionArgs,null,null,null);

    }
    public void deleteEvent(String event, String date, String time, SQLiteDatabase database){
        String selection = DBStructure.EVENT+ "=? and "+ DBStructure.DATE+"=? and "+ DBStructure.TIME+"=?";
        String[] selectionArg = {event, date, time};
        database.delete(DBStructure.EVENT_TABLE_NAME, selection, selectionArg);
    }
}
