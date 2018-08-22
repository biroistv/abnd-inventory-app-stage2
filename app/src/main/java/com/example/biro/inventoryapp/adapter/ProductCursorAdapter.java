package com.example.biro.inventoryapp.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.biro.inventoryapp.EditActivity;
import com.example.biro.inventoryapp.R;
import com.example.biro.inventoryapp.data.DatabaseHandler;
import com.example.biro.inventoryapp.data.ProductContract;
import com.example.biro.inventoryapp.product.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.biro.inventoryapp.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    private final Context mainActivityContext;

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.mainActivityContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.product_edit_btn.setTag(cursor.getPosition() + 1);

        int productNameIndex = cursor.getColumnIndex(ProductEntry.COLUMN_NAME);
        int productPriceIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
        int productQuantityIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
        int productSupplierIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
        int productPhoneIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE);

        final String productName = cursor.getString(productNameIndex);
        final String productPrice = cursor.getString(productPriceIndex);
        final String productQuantity = cursor.getString(productQuantityIndex);
        final String productSupplier = cursor.getString(productSupplierIndex);
        final String productPhone = cursor.getString(productPhoneIndex);

        final String productID = cursor.getString(cursor.getColumnIndex(ProductEntry._ID));

        viewHolder.sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product = new Product(
                        context,
                        productName,
                        productPrice,
                        productQuantity,
                        productSupplier,
                        productPhone
                );

                product.decreaseProductQuantity(1);

                DatabaseHandler.updateData(
                        context,
                        ProductEntry.CONTENT_URI,
                        product,
                        ProductEntry._ID + "='" + productID +"'",
                        null
                );
            }
        });

        viewHolder.product_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivityContext, EditActivity.class);

                Uri currentProductUri = ContentUris.withAppendedId(
                        ProductContract.ProductEntry.CONTENT_URI,
                        Integer.parseInt(productID));

                intent.setData(currentProductUri);
                mainActivityContext.startActivity(intent);
            }
        });

        viewHolder.nameTextView.setText(productName);
        CharSequence productPriceText = productPrice + context.getString(R.string.dollar_sign);
        viewHolder.priceTextView.setText(productPriceText);
        CharSequence productQuantityText = context.getText(R.string.colon_sign) + productQuantity;
        viewHolder.quantityTextView.setText(productQuantityText);
    }

    static class ViewHolder {
        @BindView(R.id.product_quantity)
        TextView quantityTextView;
        @BindView(R.id.product_price)
        TextView priceTextView;
        @BindView(R.id.product_name)
        TextView nameTextView;
        @BindView(R.id.product_sell_btn)
        Button sellBtn;
        @BindView(R.id.product_card)
        CardView productCard;
        @BindView(R.id.edit_img_btn)
        ImageButton product_edit_btn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
