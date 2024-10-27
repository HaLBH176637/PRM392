package com.example.prm392.Slot5;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class Slot5SQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="PRM";
    public static final String SQL_CREATE_PRODUCT="create TABLE Product (\n" +
            "id text PRIMARY KEY,\n" +
            "name text,\n" +
            "price text,\n" +
            "image real\n" +
            ");";
    //tao csdl
    public Slot5SQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }
    //tao bang du lieu
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCT);
    }
    //nang cap
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Product");
    }
}
