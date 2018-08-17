package com.example.biro.inventoryapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.biro.inventoryapp.R;
import com.example.biro.inventoryapp.data.ProductContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int productNameIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME);
        int productPriceIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
        int productQuantityIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);

        String productName = cursor.getString(productNameIndex);
        String productPrice = cursor.getString(productPriceIndex);
        String productQuantity = cursor.getString(productQuantityIndex);

        viewHolder.nameTextView.setText(productName);
        viewHolder.priceTextView.setText(productPrice);
        viewHolder.quantityTextView.setText(productQuantity);
    }

    static class ViewHolder{
        @BindView(R.id.product_quantity) TextView quantityTextView;
        @BindView(R.id.product_price) TextView priceTextView;
        @BindView(R.id.product_name) TextView nameTextView;
        @BindView(R.id.product_sell_btn) Button sellBtn;
        @BindView(R.id.product_card) CardView productCard;

        ViewHolder(View view){
            ButterKnife.bind(this, view);
        }

    }
}
