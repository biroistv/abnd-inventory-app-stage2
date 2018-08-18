package com.example.biro.inventoryapp.handlers;

import com.example.biro.inventoryapp.state.State;

/*
 *  This class provides some data validation method to the database inserting.
 * */
public class DataValidationHandler {

    /*
     *  The product name is valid if the product name contains lower or uppercase letters and numbers ONLY.
     * */
    public static State checkDataValidity(String str, String regex) {

        if (str == null)
            return State.EMPTY;
        else if (str.isEmpty())
            return State.EMPTY;
        else if (str.matches(regex))
            return State.VALID;
        else
            return State.INVALID;
    }
}
