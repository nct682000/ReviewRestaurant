package com.example.foodreview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //CREATE, INSERT, UPDATE, DETELE
    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    // SELECT
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void INSERT_ACC(String userName, String pass){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Acc VALUES(null, ?, ?, null, null, null, null, null, null)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, userName);
        statement.bindString(2, pass);

        statement.executeInsert();
    }

    public void INSERT_RES(String resName, String resAddress, String resProvince, String description, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Res VALUES(null, ?, ?, ?, ?, ?, 0, 0)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, resName);
        statement.bindString(2, resAddress);
        statement.bindString(3, resProvince);
        statement.bindString(4, description);
        statement.bindBlob(5, image);

        statement.executeInsert();
    }

    public void INSERT_COMMENT(String text, int resId, String accName){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Com VALUES(null, ?, null, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, text);
        statement.bindDouble(2, resId);
        statement.bindString(3, accName);

        statement.executeInsert();
    }

    public Cursor SELECT_BY_KEYWORD(String kw){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM Res WHERE resName LIKE ? ";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, kw);

        statement.executeInsert();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

