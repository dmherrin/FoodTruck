package com.example.dmherrin.maptrial;

/**
 * Created by dmherrin on 4/26/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.dmherrin.maptrial.FoodTruckDatabaseContact.TruckColumns.COLUMN_TRUCK;
import static com.example.dmherrin.maptrial.FoodTruckDatabaseContact.TruckColumns.TABLE_NAME;

public class FoodTruckSQLiteOpenHelper extends SQLiteOpenHelper{

    private static final String TAG = "MARKER";

    public FoodTruckSQLiteOpenHelper(Context context) {
        super(context, FoodTruckDatabaseContact.DATABASE_NAME,
                null, FoodTruckDatabaseContact.DATABASE_VERSION);
    }

    // when no database exists in disk and the helper class needs to create a new one.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FoodTruckDatabaseContact.TruckColumns.CREATE_TABLE);
    }

    // when there is a database version mismatch meaning that the version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " +
                oldVersion + " to " + newVersion + ". all data destroyed.");
        db.execSQL(FoodTruckDatabaseContact.TruckColumns.DELETE_TABLE);
        Log.d(TAG, "database dropped.");
        db.execSQL(FoodTruckDatabaseContact.TruckColumns.CREATE_TABLE);
    }

    // method for inserting MyMarker objects into the database
    public void insert(SQLiteDatabase db, String truckName, String address) {
        ContentValues values = new ContentValues();

        values.put(FoodTruckDatabaseContact.TruckColumns.COLUMN_TRUCK, truckName);

        values.put(FoodTruckDatabaseContact.TruckColumns.COLUMN_LOCATION, address);

        db.insert(FoodTruckDatabaseContact.TruckColumns.TABLE_NAME, null, values);
    }

    public void remove(SQLiteDatabase db, String truckName){

        db.execSQL("delete from " + TABLE_NAME + " where " + COLUMN_TRUCK + " = '" + truckName + "'");
    }

}