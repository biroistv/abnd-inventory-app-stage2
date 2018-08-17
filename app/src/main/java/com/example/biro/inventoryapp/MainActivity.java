package com.example.biro.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.biro.inventoryapp.adapter.ProductCursorAdapter;
import com.example.biro.inventoryapp.data.ProductContract;
import com.example.biro.inventoryapp.data.ProductDbHelper;
import com.example.biro.inventoryapp.handlers.DatabaseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.floating_action_button) FloatingActionButton floatingButton;
    @BindView(R.id.main_list_view) ListView productListView;
    @BindView(R.id.empty_view) View emptyView;

    private ProductDbHelper mDbHelper = new ProductDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        displayProducts();
    }

    private void displayProducts(){

        // TODO: add some instruction -> how to add new item to the database

        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_NAME,
                ProductContract.ProductEntry.COLUMN_PRICE,
                ProductContract.ProductEntry.COLUMN_QUANTITY,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE
        };

        Cursor cursor = getContentResolver().query(
                ProductContract.ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        ProductCursorAdapter adapter = new ProductCursorAdapter(this, cursor);
        productListView.setEmptyView(emptyView);
        productListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.product_populate: {
                DatabaseHandler.InsertDummyData( this);
                return true;
            }
            case R.id.product_delete: {
                return true;
            }
            case R.id.product_edit: {
                Intent intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.delete_all_product: {
                DatabaseHandler.clearProductTable(MainActivity.this);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
