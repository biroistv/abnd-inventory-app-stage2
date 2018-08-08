package com.example.biro.inventoryapp.handlers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biro.inventoryapp.R;
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
            Log.d(TAG, "IsDbAccessible: " + "false");
    }

    public static void InsertDummyData(ProductDbHelper mDbHelper, Context context) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME, "Bicycle");
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, 1000);
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, 1);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, "Anon");
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, "1234567");

        Uri newRowId = context.getContentResolver().insert(
                ProductContract.ProductEntry.CONTENT_URI,
                values
        );

        if (newRowId == null)
            Toast.makeText(context, R.string.dummy_save_failed, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, R.string.dummy_save_success, Toast.LENGTH_SHORT).show();

        if (db.isOpen())
            db.close();
    }

    public static void InsertData(ProductDbHelper mDBHelper, Context context, EditText... editTexts ) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME, editTexts[0].getText().toString().trim());
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, Integer.parseInt(editTexts[1].getText().toString().trim()));
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, Integer.parseInt(editTexts[2].getText().toString().trim()));
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, editTexts[3].getText().toString().trim());
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, editTexts[4].getText().toString().trim());

        Uri newRowId = context.getContentResolver().insert(
                ProductContract.ProductEntry.CONTENT_URI,
                values
        );

        if (newRowId == null)
            Toast.makeText(context, R.string.failed_product_saving, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, R.string.successfull_product_saving, Toast.LENGTH_SHORT).show();

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
}
