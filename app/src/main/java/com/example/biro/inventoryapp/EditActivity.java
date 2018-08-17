package com.example.biro.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.biro.inventoryapp.handlers.DatabaseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ButterKnife.bind(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean saveSuccess = DatabaseHandler.InsertData(
                        EditActivity.this,
                        nameEditText,
                        priceEditText,
                        quantityEditText,
                        suppNameEditText,
                        suppPhoneEditText
                );

                if (saveSuccess)
                    finish();
            }
        });
    }

}