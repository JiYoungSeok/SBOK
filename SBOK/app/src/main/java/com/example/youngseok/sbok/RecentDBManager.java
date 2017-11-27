package com.example.youngseok.sbok;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class RecentDBManager extends SQLiteOpenHelper {

    public RecentDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super (context, name, factory, version);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE recent " +
                "(name TEXT PRIMARY KEY, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "address TEXT);");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String name, double latitude, double longitude, String address) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO recent VALUES ('" + name + "', " + latitude + ", " + longitude + ", '" + address + "');");
        db.close();
    }

    public void delete() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM recent");
        db.close();
    }

    public void showList(ArrayList<RecentDestination> recentDestinationArrayList) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM recent", null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            String address = cursor.getString(cursor.getColumnIndex("address"));

            RecentDestination recentDestination = new RecentDestination(latitude, longitude, name, address);

            recentDestinationArrayList.add(0, recentDestination);
        }
    }
}