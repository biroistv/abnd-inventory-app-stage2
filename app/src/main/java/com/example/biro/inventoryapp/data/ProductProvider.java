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

import com.example.biro.inventoryapp.state.State;
import com.example.biro.inventoryapp.handlers.DataValidationHandler;

import static android.content.ContentValues.TAG;

public class ProductProvider extends ContentProvider{


    private static final int PRODUCTS = 1;
    private static final int PRODUCT_ID = 10;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private ProductDbHelper mDbHelper;

    static {
        sUriMatcher.addURI(
                ProductContract.CONTENT_AUTHORITY,
                ProductContract.PATH_PRODUCTS,
                PRODUCTS);

        sUriMatcher.addURI(
                ProductContract.CONTENT_AUTHORITY,
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

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case PRODUCTS:
                return ProductContract.ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductContract.ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri + "with match" + match);
        }
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
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match){
            case PRODUCTS: {
                int rowsDeleted = db.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0)
                    getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            }
            case PRODUCT_ID: {
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                int rowDeleted = db.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);

                if (rowDeleted != 0)
                    getContext().getContentResolver().notifyChange(uri, null);

                return rowDeleted;
            }
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS: {
                assert values != null;

                int rowsUpdated = UpdateData(values, selection, selectionArgs);

                if (rowsUpdated != 0)
                    getContext().getContentResolver().notifyChange(uri, null);

                return rowsUpdated;
            }
            case PRODUCT_ID: {
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                assert values != null;

                int rowUpdated = UpdateData(values, selection, selectionArgs);

                if (rowUpdated != 0)
                    getContext().getContentResolver().notifyChange(uri, null);

                return rowUpdated;
            }
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int UpdateData(ContentValues values, String selection, String[] selectionArgs){
        State dataState;

        if (values.containsKey(ProductContract.ProductEntry.COLUMN_NAME)) {
            String productName = values.getAsString(ProductContract.ProductEntry.COLUMN_NAME);
            dataState = DataValidationHandler.checkDataValidity(productName, "[a-zA-Z0-9]+");
            switch (dataState){
                case INVALID: {
                    throw new IllegalArgumentException("Product requires a valid name");
                }
                case EMPTY: {
                    throw new IllegalArgumentException("Product requires a name");
                }
            }
        }

        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRICE)) {
            String productPrice = values.getAsString(ProductContract.ProductEntry.COLUMN_PRICE);
            dataState = DataValidationHandler.checkDataValidity(productPrice, "\\d+");
            switch (dataState){
                case VALID: {
                    Integer value = values.getAsInteger(ProductContract.ProductEntry.COLUMN_PRICE);
                    if (value < 0)
                        throw new IllegalArgumentException("Product price cannot be negative");
                }
                case INVALID: {
                    throw new IllegalArgumentException("Product requires a valid price");
                }
            }
        }

        if (values.containsKey(ProductContract.ProductEntry.COLUMN_QUANTITY)) {
            String productQuantity = values.getAsString(ProductContract.ProductEntry.COLUMN_QUANTITY);
            dataState = DataValidationHandler.checkDataValidity(productQuantity, "\\d+");
            switch (dataState){
                case VALID: {
                    Integer value = values.getAsInteger(ProductContract.ProductEntry.COLUMN_QUANTITY);
                    if (value < 0)
                        throw new IllegalArgumentException("Product quantity cannot be negative");
                }
                case INVALID: {
                    throw new IllegalArgumentException("Product requires a valid quantity");
                }
            }
        }

        if (values.containsKey(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME);
            dataState = DataValidationHandler.checkDataValidity(supplierName, "[a-zA-Z]+");
            switch (dataState){
                case INVALID: {
                    throw new IllegalArgumentException("Supplier requires a valid name");
                }
                case EMPTY: {
                    throw new IllegalArgumentException("Supplier requires a name");
                }
            }
        }

        if (values.containsKey(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE)) {
            String supplierPhone = values.getAsString(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE);

            dataState = DataValidationHandler.checkDataValidity(
                    supplierPhone,
                    "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");

            switch (dataState){
                case INVALID: {
                    throw new IllegalArgumentException("Supplier phone requires a valid number");
                }
                case EMPTY: {
                    throw new IllegalArgumentException("Supplier phone requires a number");
                }
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(ProductContract.ProductEntry.TABLE_NAME, values, selection, selectionArgs);
    }

}
