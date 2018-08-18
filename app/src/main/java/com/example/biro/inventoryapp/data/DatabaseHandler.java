package com.example.biro.inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.example.biro.inventoryapp.R;
import com.example.biro.inventoryapp.data.ProductContract;
import com.example.biro.inventoryapp.product.Product;

/**
 * This class provides some database managing methods.
 */
public class DatabaseHandler {

    public static void insertDummyData(Context context) {
        Product product = new Product(
                context,
                "Car",
                "10000",
                "3",
                "Anonim",
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

    public static boolean insertData(Context context, Product product) {
        ContentValues values = product.getProductAsCValue();

        if (values == null)
            return false;

        Uri newUri = null;
        if (values != null)
            newUri = context.getContentResolver().insert(
                    ProductContract.ProductEntry.CONTENT_URI,
                    values
            );

        if (newUri == null)
            return false;

        return true;
    }


    public static boolean updateData(Context context, Uri currentProductUri, Product product, String selection, String[] selectionArgs) {

        ContentValues values = product.getProductAsCValue();

        if (values == null)
            return false;

        int rowEffected = context.getContentResolver().update(
                currentProductUri,
                values,
                selection,
                selectionArgs
        );

        if (rowEffected != 0)
            return true;
        else
            return false;

    }


    public static void clearProductTable(Context context) {
        int rowEffected = context.getContentResolver().delete(
                ProductContract.ProductEntry.CONTENT_URI,
                null,
                null
        );

        Toast.makeText(context, rowEffected + " row deleted!", Toast.LENGTH_SHORT).show();
    }
}
