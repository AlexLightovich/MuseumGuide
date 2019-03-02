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
                "   name TEXT," +
                "   age INTEGER" +
                ")";
        db.execSQL(query);
        db.execSQL("INSERT INTO user (name, age) VALUES('Baklanchick',25)");
        db.execSQL("INSERT INTO user (name, age) VALUES('Oleg', 35)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getDataFromDB(){
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT _id, name, age FROM user", null);
        return cursor;
    }
}
