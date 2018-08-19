package com.example.biro.inventoryapp.product;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.example.biro.inventoryapp.data.ProductContract;

public class Product {

    private static final int DEFAULT_PRICE = 99999999;
    private static final int DEFAULT_QUANTITY = 0;

    private String name;
    private Integer price;
    private Integer quantity;
    private String supplier;
    private String supplierPhone;
    private Context context;

    public Product(Context context,
                   String name,
                   String price,
                   String quantity,
                   String supplier,
                   String supplierPhone) {

        this.context = context;

        this.name = ProductValidationHandler.stringChecker(
                name,
                context,
                "[a-zA-Z0-9]+",
                "Name field cannot be empty!",
                "Not a valid name!");

        this.price = ProductValidationHandler.numberChecker(
                price,
                context,
                "\\d+",
                "Price cannot be negative!",
                "Not a valid number!",
                DEFAULT_PRICE);

        this.quantity = ProductValidationHandler.numberChecker(
                quantity,
                context,
                "\\d+",
                "Quantity cannot be negative!",
                "Not a valid quantity!",
                DEFAULT_QUANTITY);

        this.supplier = ProductValidationHandler.stringChecker(
                supplier,
                context,
                "[a-zA-Z]+",
                "Supplier must have a name!",
                "Invalid supplier name!");

        this.supplierPhone = ProductValidationHandler.stringChecker(
                supplierPhone,
                context,
                "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}",
                "Supplier must have a phone number!",
                "Invalid phone number!");
    }

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

    public void increaseProductQuantity(){ this.quantity++; }
    public void decreaseProductQuantity(){
        this.quantity--;

        if (this.quantity < 0){
            this.quantity = 0;
            Toast.makeText(this.context, "You can't decrease it more!", Toast.LENGTH_SHORT).show();
        }
    }

}
