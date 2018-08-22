package com.example.biro.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ProductProvider extends ContentProvider {

    // Uri type
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

        switch (sUriMatcher.match(uri)) {
            case PRODUCTS: {
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
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
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
            default: {
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
            }
        }

        Context context = getContext();
        assert context != null;
        cursor.setNotificationUri(context.getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
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

        switch (sUriMatcher.match(uri)) {
            case PRODUCTS: {

                return insertProduct(uri, values);
            }
            default:
                throw new IllegalArgumentException(" Illegal Argument :" + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Long id = db.insert(
                ProductContract.ProductEntry.TABLE_NAME,
                null,
                values
        );

        if (id == -1)
            return null;

        Context context = getContext();
        assert context != null;
        context.getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCTS: {
                int rowsDeleted = db.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    Context context = getContext();
                    assert context != null;
                    context.getContentResolver().notifyChange(uri, null);
                }

                return rowsDeleted;
            }
            case PRODUCT_ID: {
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                int rowDeleted = db.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);

                if (rowDeleted != 0) {
                    Context context = getContext();
                    assert context != null;
                    context.getContentResolver().notifyChange(uri, null);
                }

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

                if (rowsUpdated != 0) {
                    Context context = getContext();
                    assert context != null;
                    context.getContentResolver().notifyChange(uri, null);
                }

                return rowsUpdated;
            }
            case PRODUCT_ID: {
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                assert values != null;

                int rowUpdated = UpdateData(values, selection, selectionArgs);

                if (rowUpdated != 0) {
                    Context context = getContext();
                    assert context != null;
                    context.getContentResolver().notifyChange(uri, null);
                }

                return rowUpdated;
            }
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int UpdateData(ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(ProductContract.ProductEntry.TABLE_NAME, values, selection, selectionArgs);
    }

}
