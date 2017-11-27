package com.example.youngseok.sbok;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Queue;

public class DemoDBManager extends SQLiteOpenHelper {

    public DemoDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE demo (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, latitude REAL, longitude REAL, weather INTEGER);");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void init() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Kookmin Univ'" + ", " + 37.611534 + ", " + 126.979446 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Kookmin Univ'" + ", " + 37.611552 + ", " + 126.979679 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Kookmin Univ'" + ", " + 37.611626 + ", " + 126.979943 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Kookmin Univ'" + ", " + 37.611608 + ", " + 126.980215 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Kookmin Univ'" + ", " + 37.611583 + ", " + 126.980634 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Kookmin Univ'" + ", " + 37.611595 + ", " + 126.980968 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Kookmin Univ'" + ", " + 37.611595 + ", " + 126.981286 + ", " + 0 + ");");
        Log.e("DB INSERT", "Insert all instance");
        db.close();
    }

    public void delete() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM demo");
        Log.e("DB DELETE", "Delete all instance");
        db.close();
    }

    public void demoKookmin(Queue<Demo> KookminQueue) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM demo", null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            Double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            Integer weather = cursor.getInt(cursor.getColumnIndex("weather"));

            Demo demo = new Demo(name, latitude, longitude, weather);

            KookminQueue.offer(demo);
        }
    }
}
