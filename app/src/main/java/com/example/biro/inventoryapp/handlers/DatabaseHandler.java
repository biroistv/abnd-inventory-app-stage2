package com.example.biro.inventoryapp.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.EditText;

import com.example.biro.inventoryapp.data.ProductContract;
import com.example.biro.inventoryapp.data.ProductDbHelper;


public class DatabaseHandler {

    private static final String TAG = DatabaseHandler.class.getSimpleName();

    public static void IsDbAccessible(ProductDbHelper mDbHelper) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        if (db.isOpen()) {
            Log.d(TAG, "IsDbAccessible: " + "true");
            db.close();
        }else
            Log.d(TAG, "IsDbAccesible: " + "false");
    }

    public static void InsertDummyData(ProductDbHelper mDbHelper) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME, "Bicycle");
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, 1000);
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, 1);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, "Anon");
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, "1234567");

        Long newRowId = db.insert(
                ProductContract.ProductEntry.TABLE_NAME,
                null,
                values);

        if (db.isOpen())
            db.close();
    }

    public static void InsertData(ProductDbHelper mDBHelper, EditText... editTexts) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME, editTexts[0].getText().toString().trim());
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, Integer.parseInt(editTexts[1].getText().toString().trim()));
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, Integer.parseInt(editTexts[2].getText().toString().trim()));
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, editTexts[3].getText().toString().trim());
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, editTexts[4].getText().toString().trim());

        Long newRowId = db.insert(
                ProductContract.ProductEntry.TABLE_NAME,
                null,
                values
        );

        if (db.isOpen())
            db.close();
    }

    public static void ClearDatabase(ProductDbHelper mDbHelper) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(
                ProductContract.ProductEntry.TABLE_NAME,
                null,
                null
        );
    }

    public static void printRowCount(ProductDbHelper mDbHelper) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = null;

        try{
            cursor = db.rawQuery("select * from " + ProductContract.ProductEntry.TABLE_NAME, null);
            Log.d(TAG, "printRowCount: " + cursor.getCount());
        }finally {
            if (cursor != null)
                cursor.close();
        }
    }
}
