package com.example.medicaldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "medicineDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "medicine";
    private static final String ID = "id";
    private static final String NAME = "medicine_name";
    private static final String DATE = "date";
    private static final String TIME = "time_of_the_day";
    private static final String HOUR = "hour";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT,"
                + DATE + " TEXT,"
                + TIME + " TEXT,"
                + HOUR + " INTEGER"+")";
//        "CREATE TABLE  TABLE_NAME ( ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT, DATE TEXT,TIME TEXT);
        db.execSQL(query);
    }

    public boolean addNewMedicine(String medicine_name, String date, String time, int hour) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME, medicine_name);
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(HOUR, hour);

        long result =db.insert(TABLE_NAME, null, values);

        db.close();

        if(result == -1)
            return false;
        else
            return true;

    }

    public Cursor retrieve(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }

    public Cursor getRecords(String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+ " WHERE DATE=='"+date+"'",null);
        return cursor;
    }

//    public Integer DeleteData(String id){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        return sqLiteDatabase.delete(TABLE_NAME, "ID = ?", new String[]{id});
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

