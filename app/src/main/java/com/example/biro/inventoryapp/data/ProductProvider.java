package com.example.biro.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class ProductProvider extends ContentProvider{


    private static final int PRODUCTS = 1;
    private static final int PRODUCT_ID = 10;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private ProductDbHelper mDbHelper;

    static {
        sUriMatcher.addURI(
                ProductContract.CONTENT_AUTHORTIY,
                ProductContract.PATH_PRODUCTS,
                PRODUCTS);

        sUriMatcher.addURI(
                ProductContract.CONTENT_AUTHORTIY,
                ProductContract.PATH_PRODUCTS + "/#",
                PRODUCT_ID
        );
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case PRODUCTS:{
                cursor = db.query(
                        ProductContract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PRODUCT_ID: {
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(
                        ProductContract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                // Do something on a row
                break;
            }
            default: {
                throw new IllegalArgumentException("Cannot qiery unknown URI " + uri);
                // error handling
            }
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        switch (sUriMatcher.match(uri)){
            case PRODUCTS: {

                return insertProduct(uri, values);
            }
            default: throw new IllegalArgumentException(" Illegal Argument :" + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Long id = db.insert(
                ProductContract.ProductEntry.TABLE_NAME,
                null,
                values
        );

        if (id == -1){
            Log.d(TAG, "insertProduct: Failed -> " + uri);
            return null;
        }else
            return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
