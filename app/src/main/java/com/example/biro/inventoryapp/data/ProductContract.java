package com.example.biro.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ProductContract {

    // Building up the uri
    public static final String CONTENT_AUTHORTIY = "com.example.biro.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORTIY);
    public static final String PATH_PRODUCTS = "products";

    public static final class ProductEntry implements BaseColumns{

    // Table information like the table name and the column name stored as constants
    public static final String TABLE_NAME = "products";
    public static final String _ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
    public static final String COLUMN_SUPPLIER_PHONE = "supplier_phone";

    // Creating the content uri
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);


    }
}
