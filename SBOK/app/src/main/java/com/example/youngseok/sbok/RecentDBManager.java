package com.example.youngseok.sbok;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class RecentDBManager extends SQLiteOpenHelper {

    public RecentDBManager (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super (context, name, factory, version);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE recent " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "name TEXT, " +
                "address TEXT);");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(double latitude, double longitude, String name, String address) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO recent VALUES (NULL, " + latitude + ", " + longitude +  ", '" + name + "', '" + address + "');");
        db.close();
    }

    public void delete (String name, String address) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM recent WHERE name = '" + name + "' AND address = '" + address + "'");
        db.close();
    }

    public void showList(ArrayList<RecentDestination> recentDestinationArrayList) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM recent", null);

        while (cursor.moveToNext()) {
            double latitude = cursor.getInt(cursor.getColumnIndex("latitude"));
            double longitude = cursor.getInt(cursor.getColumnIndex("longitude"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String address = cursor.getString(cursor.getColumnIndex("address"));

            RecentDestination recentDestination = new RecentDestination(latitude, longitude, name, address);

            recentDestinationArrayList.add(0, recentDestination);
        }
    }
}
