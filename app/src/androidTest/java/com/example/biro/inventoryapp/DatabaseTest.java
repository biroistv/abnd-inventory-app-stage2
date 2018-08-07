package com.example.biro.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.biro.inventoryapp.data.ProductDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private ProductDbHelper mDbHelper;
    private SQLiteDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDbHelper = new ProductDbHelper(context);
        mDb = mDbHelper.getReadableDatabase();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void database_isAccessible() {
        assertTrue(mDb.isOpen());
    }

    @Test
    public void database_isTheTableNameCorrest() { assertTrue(mDbHelper.getDatabaseName() == "shop.db");}
}

