package com.example.biro.inventoryapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biro.inventoryapp.data.DatabaseHandler;
import com.example.biro.inventoryapp.data.ProductContract;
import com.example.biro.inventoryapp.product.Product;
import com.example.biro.inventoryapp.state.Operation;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.InputType.TYPE_CLASS_NUMBER;

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

        /*
         *  This listener do the addition
         * */
        increaseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null)
                    quantityAlertDialog(product, "Increase product", Operation.INCREASE);
            }
        });

        /*
         *  This listener do the subtraction
         * */
        decreaseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    quantityAlertDialog(product, "Decrease product", Operation.DECREASE);
                }
            }
        });

        /*
         *  This listener delete a product from the database
         * */
        deleteImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            DetailsActivity.this);

                    alertDialogBuilder.setTitle(R.string.delete);

                    alertDialogBuilder
                            .setMessage(R.string.delete_question)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes_, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    boolean deleteSuccess = DatabaseHandler.deleteData(
                                                    DetailsActivity.this,
                                                    mCurrentProductUri);

                                    if (deleteSuccess) {
                                        Toast.makeText(DetailsActivity.this, R.string.product_deleted, Toast.LENGTH_SHORT).show();
                                        (DetailsActivity.this).finish();
                                    } else {
                                        dialog.cancel();
                                    }
                                }
                            })
                            .setNegativeButton(R.string.no__, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        /*
         *  This listener start a call
         * */
        callImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + product.getSupplierPhone()));
                if (ActivityCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    return;

                (DetailsActivity.this).startActivity(callIntent);
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

            productNameTV.setText(getString(R.string.colon_plus_text, name));
            productPriceTV.setText(getString(R.string.colon_price_dollar, price));
            productQuantityTV.setText(getString(R.string.colon_plus_text, quantity));
            productSupplierTV.setText(getString(R.string.colon_plus_text, supplier));
            productPhoneTV.setText(getString(R.string.colon_plus_text, phone));
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

    /**
     *  This method create a dialog window and do a operation based on the input operation parameter
     *
     *  @param prod         product with the data
     *  @param title        operation name
     *  @param operation    what is the operation
     * */
    private void quantityAlertDialog(final Product prod,
                                     @NonNull final String title,
                                     @NonNull final Operation operation){
        if (prod != null) {
            final EditText input = new EditText(DetailsActivity.this);
            input.setInputType(TYPE_CLASS_NUMBER);
            input.setText("1");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailsActivity.this);

            alertDialogBuilder
                    .setTitle(title)
                    .setCancelable(false)
                    .setView(input)
                    .setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String strValue = input.getText().toString();

                            // If the string value is empty then the value is stay the default 1
                            int value = 1;
                            if (!strValue.isEmpty())
                                value = Integer.parseInt(input.getText().toString());

                            switch (operation){
                                case DECREASE: {
                                    boolean decreaseSuccess = prod.decreaseProductQuantity(value);
                                    if (!decreaseSuccess){
                                        Toast.makeText(
                                                DetailsActivity.this,
                                                (DetailsActivity.this).getString(R.string.you_cant_delete_x_product, value),
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    break;
                                }
                                case INCREASE: {
                                    prod.increaseProductQuantity(value);
                                    break;
                                }
                                default: throw new IllegalArgumentException("Unknown operation");
                            }

                            DatabaseHandler.updateData(
                                    DetailsActivity.this,
                                    mCurrentProductUri,
                                    prod,
                                    null,
                                    null
                            );
                        }
                    })
                    .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}