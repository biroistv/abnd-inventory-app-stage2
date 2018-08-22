package com.example.biro.inventoryapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
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
    @BindView(R.id.name_editText)
    EditText nameEditText;
    @BindView(R.id.price_editText)
    EditText priceEditText;
    @BindView(R.id.quantity_editText)
    EditText quantityEditText;
    @BindView(R.id.supp_name_editText)
    EditText suppNameEditText;
    @BindView(R.id.supp_phone_editText)
    EditText suppPhoneEditText;

    private boolean productHasChanged = false;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Binding the views with the butterKnife
        ButterKnife.bind(this);

        // Setting up the touch listener on each editTextView
        nameEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);
        quantityEditText.setOnTouchListener(touchListener);
        suppNameEditText.setOnTouchListener(touchListener);
        suppPhoneEditText.setOnTouchListener(touchListener);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        Log.d("EditOnclick", "onCreate: mCurrentProductUru = " + mCurrentProductUri);

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
                // If the uri is null then its a new item adding
                if (mCurrentProductUri == null) {
                    boolean saveSuccess = DatabaseHandler.insertData(
                            EditActivity.this,
                            product
                    );

                    if (saveSuccess) {
                        Toast.makeText(EditActivity.this, R.string.successful_product_saving, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {    // Otherwise its an editing
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

        // Setting texts to the editTexts after the load
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
        priceEditText.setText("");
        quantityEditText.setText("");
        suppNameEditText.setText("");
        suppPhoneEditText.setText("");
    }

    /**
     *  Returns back a product depends on the edit text values
     * */
    private Product getProductFromEditTexts() {
        return new Product(
                EditActivity.this,
                nameEditText.getText().toString(),
                priceEditText.getText().toString(),
                quantityEditText.getText().toString(),
                suppNameEditText.getText().toString(),
                suppPhoneEditText.getText().toString());
    }

    private final View.OnTouchListener touchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            productHasChanged = true;
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (!productHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_dialog_msg);
        builder.setPositiveButton(R.string.discard_msg, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing_msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!productHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        showUnsavedChangesDialog(discardButtonClickListener);
    }
}
