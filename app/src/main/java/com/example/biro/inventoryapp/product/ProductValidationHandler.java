package com.example.biro.inventoryapp.product;

import android.content.Context;
import android.widget.Toast;

import com.example.biro.inventoryapp.state.State;

class ProductValidationHandler {

    private static final int DEFAULT_PRICE = 99999999;
    private static final int DEFAULT_QUANTITY = 0;

    private static State checkDataValidity(String str, String regex) {
        if (str.matches(regex))
            return State.VALID;
        if (str.isEmpty())
            return State.EMPTY;
        else
            return State.INVALID;
    }

    public static String nameChecker(String productName, Context context) {

        State dataState = checkDataValidity(productName, "[a-zA-Z0-9]+");

        switch (dataState) {
            case EMPTY: {
                Toast.makeText(context, "The name field cannot be empty!", Toast.LENGTH_SHORT).show();
                return null;
            }
            case VALID: {
                return productName;
            }
            case INVALID: {
                Toast.makeText(context, "Not a valid name!", Toast.LENGTH_SHORT).show();
                return null;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }
    }

    /*
     *  Handling the price depend on the dataState.
     * */
    public static Integer priceChecker(String price, Context context) {
        switch (checkDataValidity(price, "\\d+")) {
            case VALID: {
                Integer priceValue = Integer.parseInt(price);
                if (priceValue >= 0)
                    return Integer.parseInt(price);
                else {
                    Toast.makeText(context, "The price cannot be negative", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
            case INVALID: {
                Toast.makeText(context, "Invalid number in the price filed!", Toast.LENGTH_SHORT).show();
                return null;
            }
            case EMPTY: {
                return DEFAULT_PRICE;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }
    }

    /*
     * Handling the quantity
     * */
    public static Integer checkQuantity(String quantity, Context context) {
        switch (checkDataValidity(quantity, "\\d+")) {
            case VALID: {
                Integer quantityValue = Integer.parseInt(quantity);
                if (quantityValue >= 0)
                    return Integer.parseInt(quantity);
                else {
                    Toast.makeText(context, "The quantity cannot be negative", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
            case INVALID: {
                Toast.makeText(context, "Invalid number in the quantity filed!", Toast.LENGTH_SHORT).show();
                return null;
            }
            case EMPTY: {
                return DEFAULT_QUANTITY;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }
    }

    /*
     * Handling the supplier name
     * */
    public static String checkSupplier(String supplier, Context context) {
        switch (checkDataValidity(supplier, "[a-zA-Z]+")) {
            case VALID: {
                return supplier;
            }
            case EMPTY: {
                Toast.makeText(context, "The supplier name filed cannot be empty!", Toast.LENGTH_SHORT).show();
                return null;
            }
            case INVALID: {
                Toast.makeText(context, "Invalid text in the supplier name field!", Toast.LENGTH_SHORT).show();
                return null;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }
    }

    /*
     * Handling the supplier phone number
     * */
    public static String checkPshone(String phone, Context context) {
        switch (checkDataValidity(phone, "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")) {
            case VALID: {
                return phone;
            }
            case EMPTY: {
                Toast.makeText(context, "The supplier phone field cannot be empty!", Toast.LENGTH_SHORT).show();
                return null;
            }
            case INVALID: {
                Toast.makeText(context, "Invalid phone number in the supplier phone field!", Toast.LENGTH_SHORT).show();
                return null;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }
    }
}
