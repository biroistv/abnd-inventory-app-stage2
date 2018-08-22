package com.example.biro.inventoryapp.product;

import android.content.ContentValues;
import android.content.Context;

import com.example.biro.inventoryapp.data.ProductContract;

/**
 *  This class do the validation of a product.
 *
 *  - checking a data
 *  - getting as a ContentValue
 *  - increase/decrease the product
 * */

public class Product {

    private static final int DEFAULT_PRICE = 99999999;
    private static final int DEFAULT_QUANTITY = 0;

    private final String name;
    private final Integer price;
    private Integer quantity;
    private final String supplier;
    private final String supplierPhone;

    public String getSupplierPhone() {
        return supplierPhone;
    }

    /**
     * The Constructor is validating the input data and if its valid then
     * define the member variables with that valid data.
     * */
    public Product(Context context,
                   String name,
                   String price,
                   String quantity,
                   String supplier,
                   String supplierPhone) {

        this.name = ProductValidationHandler.stringChecker(
                name,
                context,
                "^[a-zA-Z0-9\\s]+",
                "Name field cannot be empty!",
                "Not a valid name!");

        this.price = ProductValidationHandler.numberChecker(
                price,
                context,
                "^\\d+",
                "Price cannot be negative!",
                "Not a valid number!",
                DEFAULT_PRICE);

        this.quantity = ProductValidationHandler.numberChecker(
                quantity,
                context,
                "^\\d+",
                "Quantity cannot be negative!",
                "Not a valid quantity!",
                DEFAULT_QUANTITY);

        this.supplier = ProductValidationHandler.stringChecker(
                supplier,
                context,
                "^[a-zA-Z\\s]+",
                "Supplier must have a name!",
                "Invalid supplier name!");

        this.supplierPhone = ProductValidationHandler.stringChecker(
                supplierPhone,
                context,
                "^\\d{8,12}",
                "Supplier must have a phone number!",
                "Invalid phone number!");
    }

    /**
     * Returns a ContentValue that can we use in the database handling
     * methods.
     * <p>
     * This method returns null, if any of the string data is null.
     *
     * @return      a ContentValue which contains the product information
     */
    public ContentValues getProductAsCValue() {
        ContentValues value = new ContentValues();

        if (this.name != null)
            value.put(ProductContract.ProductEntry.COLUMN_NAME, this.name);
        else return null;

        if (this.price != null)
            value.put(ProductContract.ProductEntry.COLUMN_PRICE, this.price);

        if (this.quantity != null)
            value.put(ProductContract.ProductEntry.COLUMN_QUANTITY, this.quantity);

        if (this.supplier != null)
            value.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, this.supplier);
        else return null;

        if (this.supplierPhone != null)
            value.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, this.supplierPhone);
        else return null;

        return value;
    }

    /**
     * Increase the number of quantity based on the input parameter.
     * @param value     the number of product
     */
    public void increaseProductQuantity(int value){ this.quantity += value; }

    /**
     * Decrease the number of quantity based on the input parameter.
     * <p>
     * Decrease the quantity ONLY if the subtraction of the input parameter and the current quantity is
     * greater or equal to 0. Otherwise return false, what means the decrease is not possible.
     * @param value     the number of product
     * @return          boolean value depend on the operation output
     */
    public boolean decreaseProductQuantity(int value){
        if (this.quantity - value >= 0){
            this.quantity -= value;
            return true;
        }

        return false;
    }

}
