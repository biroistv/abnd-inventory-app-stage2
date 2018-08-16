package com.example.biro.inventoryapp.handlers;

/*
 *  This class provides some data validation method to the database inserting.
 * */
public class DataValidationHandler {

    /*
     *  The product name is valid if the product name contains lower or uppercase letters and numbers ONLY.
     * */
    public static DataState checkDataValidity(String str, String regex) {
        if ( str.matches(regex) )
            return DataState.VALID;
        if ( str.isEmpty())
            return DataState.EMPTY;
        else
            return DataState.INVALID;
    }
}
