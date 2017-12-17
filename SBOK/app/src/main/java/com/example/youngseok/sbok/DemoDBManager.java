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

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396214 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396279 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396333 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396380 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396430 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396480 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396559 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396602 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396648 + ", " + 127.109694 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396699 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396740 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396781 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396826 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396865 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396905 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396955 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.396996 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397037 + ", " + 127.109694 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397084 + ", " + 127.109694 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397129 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397175 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397218 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397263 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397306 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397353 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397353 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397394 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397444 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397485 + ", " + 127.109692 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397530 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397573 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397573 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397616 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397659 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397659 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397706 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397743 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397784 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397829 + ", " + 127.109692 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397869 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397900 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397940 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.397980 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398020 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398060 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398100 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398140 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398180 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398220 + ", " + 127.109692 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398260 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398300 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398340 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398380 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398420 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398460 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398500 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398540 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398580 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398620 + ", " + 127.109692 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398660 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398700 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398740 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398780 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398820 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398860 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398900 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398940 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.398980 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399020 + ", " + 127.109692 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399060 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399100 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399140 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399180 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399220 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399260 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399300 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399340 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399380 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399420 + ", " + 127.109692 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399460 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399500 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399540 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399580 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399620 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399660 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399700 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399740 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399780 + ", " + 127.109692 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.109692 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.109740 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.109780 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.109820 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.109860 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.109900 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.109940 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.109980 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110020 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110060 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110100 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110140 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110180 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110220 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110260 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110300 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110340 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110380 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110420 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110460 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110500 + ", " + 0 + ");");

        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110540 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110580 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110620 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110660 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110700 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110740 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110780 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110820 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110860 + ", " + 0 + ");");
        db.execSQL("INSERT INTO demo VALUES (NULL, " + "'Continental'" + ", " + 37.399820 + ", " + 127.110900 + ", " + 0 + ");");

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
