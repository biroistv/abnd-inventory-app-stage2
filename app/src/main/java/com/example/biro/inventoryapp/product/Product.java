package com.example.biro.inventoryapp.product;

import android.content.ContentValues;
import android.content.Context;

import com.example.biro.inventoryapp.data.ProductContract;

public class Product {

    private String name;
    private Integer price;
    private Integer quantity;
    private String supplier;
    private String supplierPhone;

    public Product(Context context, String name, String price, String quantity, String supplier, String supplierPhone) {
        this.name = ProductValidationHandler.nameChecker(name, context);
        this.price = ProductValidationHandler.priceChecker(price, context);
        this.quantity = ProductValidationHandler.quantityChecker(quantity, context);
        this.supplier = ProductValidationHandler.supplierChecker(supplier, context);
        this.supplierPhone = ProductValidationHandler.phoneChecker(supplierPhone, context);
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

}
