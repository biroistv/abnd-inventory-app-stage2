package com.example.biro.inventoryapp.product;

import android.content.ContentValues;
import android.content.Context;

import com.example.biro.inventoryapp.data.ProductContract;
import com.example.biro.inventoryapp.handlers.DataValidationHandler;

public class Product {

    private Integer id;
    private String name;
    private Integer price;
    private Integer quantity;
    private String supplier;
    private String supplierPhone;

    public Product(Context context, Integer id, String name, String price, String quantity, String supplier, String supplierPhone) {
        this.id = id;
        this.name = ProductValidationHandler.nameChecker(name, context);
        this.price = ProductValidationHandler.priceChecker(price, context);
        this.quantity = ProductValidationHandler.checkQuantity(quantity, context);
        this.supplier = ProductValidationHandler.checkSupplier(supplier, context);
        this.supplierPhone = ProductValidationHandler.checkPshone(supplierPhone, context);
    }

    public Product(Context context, String name, String price, String quantity, String supplier, String supplierPhone) {
        this.name = ProductValidationHandler.nameChecker(name, context);
        this.price = ProductValidationHandler.priceChecker(price, context);
        this.quantity = ProductValidationHandler.checkQuantity(quantity, context);
        this.supplier = ProductValidationHandler.checkSupplier(supplier, context);
        this.supplierPhone = ProductValidationHandler.checkPshone(supplierPhone, context);
    }

    public ContentValues getProductAsCValue() {
        ContentValues value = new ContentValues();

        if (this.name == null)
            return null;
        else
            value.put(ProductContract.ProductEntry.COLUMN_NAME, this.name);

        if (this.price != null)
            value.put(ProductContract.ProductEntry.COLUMN_PRICE, this.price);

        if (this.quantity != null)
            value.put(ProductContract.ProductEntry.COLUMN_QUANTITY, this.quantity);

        if (this.supplier == null)
            return null;
        else
            value.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, this.supplier);

        if (this.supplierPhone == null)
            return null;
        else
            value.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, this.supplierPhone);

        return value;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }
}
