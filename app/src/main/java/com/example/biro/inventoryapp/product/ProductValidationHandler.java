package com.example.biro.inventoryapp.product;

import android.content.Context;
import android.support.annotation.NonNull;
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

    public static Integer numberChecker(@NonNull String str,
                                     @NonNull Context context,
                                     @NonNull String regex,
                                     @NonNull String negativeMsg,
                                     @NonNull String invalidMsg) {
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
                return DEFAULT_PRICE;
            }
            default:
                throw new IllegalStateException("Illegal state error");
        }
    }
}
