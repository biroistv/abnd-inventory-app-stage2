package com.example.biro.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.biro.inventoryapp.data.DatabaseHandler;
import com.example.biro.inventoryapp.data.ProductContract;
import com.example.biro.inventoryapp.product.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.details_product_name)
    TextView productNameTV;
    @BindView(R.id.details_product_price)
    TextView productPriceTV;
    @BindView(R.id.details_product_quantity)
    TextView productQuantityTV;
    @BindView(R.id.details_product_supplier)
    TextView productSupplierTV;
    @BindView(R.id.details_product_phone)
    TextView productPhoneTV;

    @BindView(R.id.details_call_btn)
    ImageButton callImgBtn;
    @BindView(R.id.details_increase)
    ImageButton increaseImgBtn;
    @BindView(R.id.details_decrease)
    ImageButton decreaseImgBtn;
    @BindView(R.id.details_delete_btn)
    ImageButton deleteImgBtn;

    private static final int EXISTING_PRODUCT_LOADER = 0;
    private Uri mCurrentProductUri;
    private Product product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle(getString(R.string.details));

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri != null)
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);

        increaseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null)
                    product.increaseProductQuantity();

                boolean increaseSuccess = DatabaseHandler.updateData(
                        DetailsActivity.this,
                        mCurrentProductUri,
                        product,
                        null,
                        null
                );
            }
        });

        decreaseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (product != null)
                    product.decreaseProductQuantity();

                boolean decreaseSuccess = DatabaseHandler.updateData(
                        DetailsActivity.this,
                        mCurrentProductUri,
                        product,
                        null,
                        null
                );
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                mCurrentProductUri,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1)
            return;

        if (data.moveToFirst()) {
            String name = data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME));
            String price = data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE));
            String quantity = data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY));
            String supplier = data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME));
            String phone = data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE));

            product = new Product(
                    DetailsActivity.this,
                    name,
                    price,
                    quantity,
                    supplier,
                    phone
            );

            productNameTV.setText(": " + name);
            productPriceTV.setText(": " + price);
            productQuantityTV.setText(" " + quantity);
            productSupplierTV.setText(": " + supplier);
            productPhoneTV.setText(": " + phone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productNameTV.setText("");
        productPriceTV.setText("");
        productQuantityTV.setText("");
        productSupplierTV.setText("");
        productPhoneTV.setText("");
    }
}
