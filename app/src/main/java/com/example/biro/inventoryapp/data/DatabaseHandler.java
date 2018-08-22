package com.example.biro.inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.example.biro.inventoryapp.R;
import com.example.biro.inventoryapp.product.Product;

/**
 * This class provides some database managing methods.
 */
public class DatabaseHandler {

    /**
     * Insert a dummy data to the database.
     */
    public static void insertDummyData(Context context) {
        Product product = new Product(
                context,
                "Car",
                "10000",
                "3",
                "Test",
                "1234567890"
        );

        ContentValues values = product.getProductAsCValue();

        Uri newRowId = null;

        if (values != null)
            newRowId = context.getContentResolver().insert(
                    ProductContract.ProductEntry.CONTENT_URI,
                    values
            );

        if (newRowId == null)
            Toast.makeText(context, R.string.dummy_save_failed, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, R.string.dummy_save_success, Toast.LENGTH_SHORT).show();

    }

    /**
     * Insert a row to the database with the values of the product
     *
     * @param context context
     * @param product this parameter contains the data
     * @return result of the insertion
     */
    public static boolean insertData(Context context, Product product) {
        // Extract the ContentValues from the product
        ContentValues values = product.getProductAsCValue();

        if (values == null)
            return false;

        Uri newUri = context.getContentResolver().insert(
                ProductContract.ProductEntry.CONTENT_URI,
                values
        );

        return newUri != null;
    }

    /**
     * Update a specific a row in the database with the values of the product
     *
     * @param context           context
     * @param currentProductUri The uri of that row what we want update
     * @param product           this parameter contains the data
     * @param selection         selection statements
     * @param selectionArgs     selection args
     * @return result of the update
     */
    public static boolean updateData(Context context, Uri currentProductUri, Product product, String selection, String[] selectionArgs) {
        // Extract the ContentValues from the product
        ContentValues values = product.getProductAsCValue();

        if (values == null)
            return false;

        int rowEffected = context.getContentResolver().update(
                currentProductUri,
                values,
                selection,
                selectionArgs
        );

        return rowEffected != 0;
    }

    /**
     * delete a specific a row from the database depend on the input Uri parameter
     *
     * @param context           context
     * @param currentProductUri The uri of that row what we want delete
     * @return result of the delete
     */
    public static Boolean deleteData(Context context, Uri currentProductUri) {
        int rowDeleted = 0;

        if (currentProductUri != null) {
            rowDeleted = context.getContentResolver().delete(
                    currentProductUri,
                    null,
                    null
            );
        }

        return rowDeleted != 0;
    }

    /**
     * Delete all the rows inside the product table
     *
     * @param context context
     */
    public static void clearProductTable(Context context) {
        int rowEffected = context.getContentResolver().delete(
                ProductContract.ProductEntry.CONTENT_URI,
                null,
                null
        );

        Toast.makeText(context, rowEffected + " row deleted!", Toast.LENGTH_SHORT).show();
    }
}
