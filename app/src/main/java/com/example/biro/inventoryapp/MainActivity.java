package com.example.biro.inventoryapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.biro.inventoryapp.data.ProductDbHelper;
import com.example.biro.inventoryapp.handlers.DatabaseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.log_text) TextView logText;
    @BindView(R.id.floating_action_button) FloatingActionButton floatingButton;

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

        DatabaseHandler.IsDbAccessible(mDbHelper);
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
                DatabaseHandler.InsertDummyData(mDbHelper, this);
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
                DatabaseHandler.ClearDatabase(mDbHelper);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
