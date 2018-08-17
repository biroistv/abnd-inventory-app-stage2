package com.example.biro.inventoryapp.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biro.inventoryapp.R;
import com.example.biro.inventoryapp.data.ProductContract;
import com.example.biro.inventoryapp.data.ProductDbHelper;
import com.example.biro.inventoryapp.state.State;

/**
 * This class provides some database managing methods.
 */
public class DatabaseHandler {

    private static final String TAG = DatabaseHandler.class.getSimpleName();
    private static final int DEFAULT_PRICE = 0;
    private static final int DEFAULT_QUANTITY = 0;

    public static void InsertDummyData(Context context) {

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

    }

    public static boolean InsertData(Context context, EditText... editTexts) {
        ContentValues values = new ContentValues();

        /*
         *  Handling the name depend on the dataState.
         * */
        String productName = editTexts[0].getText().toString();
        State dataState =
                DataValidationHandler.checkDataValidity(productName, "[a-zA-Z0-9]+");

        switch (dataState) {
            case EMPTY: {
                Toast.makeText(context, "The name field cannot be empty!", Toast.LENGTH_SHORT).show();
                return false;
            }
            case VALID: {
                values.put(ProductContract.ProductEntry.COLUMN_NAME, productName);
                break;
            }
            case INVALID: {
                Toast.makeText(context, "Not a valid name!", Toast.LENGTH_SHORT).show();
                return false;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }

        /*
         *  Handling the price depend on the dataState.
         * */
        String productPrice = editTexts[1].getText().toString();
        dataState = DataValidationHandler.checkDataValidity(productPrice, "\\d+");
        switch (dataState){
            case VALID:{
                Integer priceValue = Integer.parseInt(productPrice);
                if (priceValue >= 0)
                    values.put(ProductContract.ProductEntry.COLUMN_PRICE, priceValue);
                else{
                    Toast.makeText(context, "The price cannot be negative", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            }
            case INVALID:{
                Toast.makeText(context, "Invalid number in the price filed!", Toast.LENGTH_SHORT).show();
                return false;
            }
            case EMPTY: {
                values.put(ProductContract.ProductEntry.COLUMN_PRICE, DEFAULT_PRICE);
                break;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }

        /*
         * Handling the quantity
         * */
        String productQuantity = editTexts[2].getText().toString();
        dataState = DataValidationHandler.checkDataValidity(productQuantity, "\\d+");
        switch (dataState){
            case VALID:{
                Integer quantityValue = Integer.parseInt(productQuantity);
                if (quantityValue >= 0)
                    values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, quantityValue);
                else{
                    Toast.makeText(context, "The quantity cannot be negative", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            }
            case INVALID:{
                Toast.makeText(context, "Invalid number in the quantity filed!", Toast.LENGTH_SHORT).show();
                return false;
            }
            case EMPTY: {
                values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, DEFAULT_QUANTITY);
                break;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }

        /*
         * Handling the supplier name
         * */
        String productSupplierName = editTexts[3].getText().toString();
        dataState = DataValidationHandler.checkDataValidity(productSupplierName, "[a-zA-Z]+");
        switch (dataState){
            case VALID:{
                values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, productSupplierName);
                break;
            }
            case EMPTY:{
                Toast.makeText(context, "The supplier name filed cannot be empty!", Toast.LENGTH_SHORT).show();
                return false;
            }
            case INVALID:{
                Toast.makeText(context, "Invalid text in the supplier name field!", Toast.LENGTH_SHORT).show();
                return false;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }

        /*
         * Handling the supplier phone number
         * */
        String phoneNumber = editTexts[4].getText().toString();
        dataState = DataValidationHandler.checkDataValidity(
                phoneNumber,
                "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");

        switch (dataState){
            case VALID:{
                values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, phoneNumber);
                break;
            }
            case EMPTY:{
                Toast.makeText(context, "The supplier phone field cannot be empty!", Toast.LENGTH_SHORT).show();
                return false;
            }
            case INVALID:{
                Toast.makeText(context, "Invalid phone number in the supplier phone field!", Toast.LENGTH_SHORT).show();
                return false;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }

        // Insert the data into the database
        Uri newUri = context.getContentResolver().insert(
                ProductContract.ProductEntry.CONTENT_URI,
                values
        );

        if (newUri == null)
            Toast.makeText(context, R.string.failed_product_saving, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, R.string.successfull_product_saving, Toast.LENGTH_SHORT).show();

        return true;
    }

    public static void clearProductTable(Context context) {
        int rowEffected = context.getContentResolver().delete(
                ProductContract.ProductEntry.CONTENT_URI,
                null,
                null
        );

        Toast.makeText(context, rowEffected + " row deleted!", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "clearProductTable: row deleted=" + rowEffected);
    }
}
