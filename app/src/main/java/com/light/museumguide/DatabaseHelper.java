package com.light.museumguide;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int version = 1;
    private static final String dbName = "database.db";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context){
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE user(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "   name_expo TEXT," +
                "   add_info TEXT" +
                ")";
        db.execSQL(query);
        db.execSQL("INSERT INTO user (name_expo, add_info) VALUES('First Expo','this is add info for first expo')");
        db.execSQL("INSERT INTO user (name_expo, add_info) VALUES('Second Expo', 'this is add info for second expo')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getDataFromDB(){
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT _id, name_expo, add_info FROM user", null);
        return cursor;
    }
}
