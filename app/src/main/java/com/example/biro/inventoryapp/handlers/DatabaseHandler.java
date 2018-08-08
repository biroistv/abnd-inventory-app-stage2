package com.example.biro.inventoryapp.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.EditText;

import com.example.biro.inventoryapp.data.ProductContract;
import com.example.biro.inventoryapp.data.ProductDbHelper;
import com.example.biro.inventoryapp.data.ProductProvider;


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

    public static Long InsertDummyData(ProductDbHelper mDbHelper) {
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

        return newRowId;
    }

    public static Long InsertData(ProductDbHelper mDBHelper, EditText... editTexts) {
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

        return newRowId;
    }

    public static void ClearDatabase(ProductDbHelper mDbHelper) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(
                ProductContract.ProductEntry.TABLE_NAME,
                null,
                null
        );
    }

    public static void printRowCount(ProductDbHelper mDbHelper, Context context) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] project = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_NAME,
                ProductContract.ProductEntry.COLUMN_PRICE,
                ProductContract.ProductEntry.COLUMN_QUANTITY,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE
        };

        Cursor cursor = null;

        try{
            /*cursor = db.query(
                    ProductContract.ProductEntry.TABLE_NAME,
                    project,
                    null,
                    null,
                    null,
                    null,
                    null
            );*/

            cursor = context.getContentResolver().query(
                    ProductContract.ProductEntry.CONTENT_URI,
                    project,
                    null,
                    null,
                    null
            );


            Log.d(TAG, "printRowCount: " + cursor.getCount());
        }finally {
            if (cursor != null)
                cursor.close();
        }
    }
}
