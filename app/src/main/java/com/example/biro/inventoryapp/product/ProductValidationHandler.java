package com.example.biro.inventoryapp.product;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.biro.inventoryapp.state.State;

class ProductValidationHandler {

    /**
     * Check the validation of the input data.
     * <p>
     * Returns back a State what representing the input string status after the check.
     *
     * @param str       string what we checking
     * @param regex     the regex
     * @return          a State
     */
    private static State checkDataValidity(String str, String regex) {
        if (str.matches(regex))
            return State.VALID;
        if (str.isEmpty())
            return State.EMPTY;
        else
            return State.INVALID;
    }

    /**
     * Check the validation of a string data which is a character sequence.
     * <p>
     * Returns back the input string if its in a VALID state, otherwise returns a null value and
     * notify the user with an error message.
     *
     * @param str           string what we checking
     * @param context       the Activity what is current active
     * @param regex         the regex
     * @param emptyMsg      EMPTY state error message
     * @param invalidMsg    INVALID state error message
     * @return              the input string if its VALID
     */
    public static String stringChecker(@NonNull String str,
                                       @NonNull Context context,
                                       @NonNull String regex,
                                       @NonNull String emptyMsg,
                                       @NonNull String invalidMsg) {
        switch (checkDataValidity(str, regex)) {
            case EMPTY: {
                Toast.makeText(context, emptyMsg, Toast.LENGTH_SHORT).show();
                return null;
            }
            case VALID: {
                return str;
            }
            case INVALID: {
                Toast.makeText(context, invalidMsg, Toast.LENGTH_SHORT).show();
                return null;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }
    }

    /**
     * Check the validation of a string data which is a number.
     * <p>
     * Returns back the input string as a number (Integer) if its in a VALID or EMPTY state,
     * otherwise returns a null value and notify the user with an error message.
     *
     * @param str           string what we checking
     * @param context       the Activity what is current active
     * @param regex         the regex
     * @param negativeMsg   INVALID (negative) state error message
     * @param invalidMsg    INVALID (NaN) state error message
     * @return              input string as Integer if its VALID or EMPTY
     */
    public static Integer numberChecker(@NonNull String str,
                                        @NonNull Context context,
                                        @NonNull String regex,
                                        @NonNull String negativeMsg,
                                        @NonNull String invalidMsg,
                                        int defaultValue) {
        switch (checkDataValidity(str, regex)) {
            case VALID: {
                Integer priceValue = Integer.parseInt(str);
                if (priceValue >= 0)
                    return Integer.parseInt(str);
                else {
                    Toast.makeText(context, negativeMsg, Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
            case INVALID: {
                Toast.makeText(context, invalidMsg, Toast.LENGTH_SHORT).show();
                return null;
            }
            case EMPTY: {
                return defaultValue;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }
    }
}
