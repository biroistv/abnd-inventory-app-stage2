package com.example.biro.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.biro.inventoryapp.adapter.ProductCursorAdapter;
import com.example.biro.inventoryapp.data.DatabaseHandler;
import com.example.biro.inventoryapp.data.ProductContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingButton;
    @BindView(R.id.main_list_view)
    ListView productListView;
    @BindView(R.id.empty_view)
    View emptyView;

    private static final int PRODUCT_LOADER = 0;
    private ProductCursorAdapter mCursorAdapter;

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
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        mCursorAdapter = new ProductCursorAdapter(this, null);
        productListView.setEmptyView(emptyView);
        productListView.setAdapter(mCursorAdapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_dummy_data: {
                DatabaseHandler.insertDummyData(this);
                return true;
            }
            case R.id.product_delete: {
                return true;
            }
            case R.id.product_edit: {
                return true;
            }
            case R.id.delete_all_product: {
                DatabaseHandler.clearProductTable(MainActivity.this);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                ProductContract.ProductEntry.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
