package com.example.biro.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ProductDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "shop.db";

    ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCTS_TABLE =
                "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + " (" +
                        ProductContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        ProductContract.ProductEntry.COLUMN_NAME + " TEXT NOT NULL," +
                        ProductContract.ProductEntry.COLUMN_PRICE + " INTEGER DEFAULT 99999999," +
                        ProductContract.ProductEntry.COLUMN_QUANTITY + " INTEGER DEFAULT 0," +
                        ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
                        ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
