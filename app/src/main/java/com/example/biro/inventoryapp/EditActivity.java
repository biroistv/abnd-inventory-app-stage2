package com.example.biro.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biro.inventoryapp.data.ProductContract;
import com.example.biro.inventoryapp.data.DatabaseHandler;
import com.example.biro.inventoryapp.product.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.save_button)
    Button saveButton;
    @BindView(R.id.name_edittext)
    EditText nameEditText;
    @BindView(R.id.price_edittext)
    EditText priceEditText;
    @BindView(R.id.quantity_edittext)
    EditText quantityEditText;
    @BindView(R.id.supp_name_edittext)
    EditText suppNameEditText;
    @BindView(R.id.supp_phone_edittext)
    EditText suppPhoneEditText;

    private static final int EXISTING_PRODUCT_LOADER = 0;
    private Uri mCurrentProductUri;

    private static final String[] PROJECTION = new String[]{
            ProductContract.ProductEntry._ID,
            ProductContract.ProductEntry.COLUMN_NAME,
            ProductContract.ProductEntry.COLUMN_PRICE,
            ProductContract.ProductEntry.COLUMN_QUANTITY,
            ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME,
            ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null)
            setTitle(getString(R.string.add_product));
        else {
            setTitle(getString(R.string.edit_product));
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product = getProductFromEditTexts();

                if (mCurrentProductUri == null) {
                    boolean saveSuccess = DatabaseHandler.insertData(
                            EditActivity.this,
                            product
                    );

                    if (saveSuccess) {
                        Toast.makeText(EditActivity.this, R.string.successfull_product_saving, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    boolean updateSuccess = DatabaseHandler.updateData(
                            EditActivity.this,
                            mCurrentProductUri,
                            product,
                            null,
                            null
                    );

                    if (updateSuccess) {
                        Toast.makeText(EditActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                mCurrentProductUri,
                PROJECTION,
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

            nameEditText.setText(name);
            priceEditText.setText(price);
            quantityEditText.setText(quantity);
            suppNameEditText.setText(supplier);
            suppPhoneEditText.setText(phone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameEditText.setText("");
        priceEditText.setText("0");
        quantityEditText.setText("0");
        suppNameEditText.setText("");
        suppPhoneEditText.setText("");
    }

    private Product getProductFromEditTexts() {
        return new Product(
                EditActivity.this,
                nameEditText.getText().toString(),
                priceEditText.getText().toString(),
                quantityEditText.getText().toString(),
                suppNameEditText.getText().toString(),
                suppPhoneEditText.getText().toString());
    }
}
