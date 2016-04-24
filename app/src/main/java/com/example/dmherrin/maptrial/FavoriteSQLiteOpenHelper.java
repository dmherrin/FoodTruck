package com.example.dmherrin.maptrial;

/**
 * Created by Nick on 4/23/2016.
 */
/**
 * Created by Nick on 4/11/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import static com.example.dmherrin.maptrial.FavoriteDatabaseContract.QuakeColumns.*;

public class FavoriteSQLiteOpenHelper extends SQLiteOpenHelper{

    private static final String TAG = "MARKER";

    public FavoriteSQLiteOpenHelper(Context context) {
        super(context, FavoriteDatabaseContract.DATABASE_NAME,
                null, FavoriteDatabaseContract.DATABASE_VERSION);
    }

    // when no database exists in disk and the helper class needs to create a new one.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavoriteDatabaseContract.QuakeColumns.CREATE_TABLE);
    }

    // when there is a database version mismatch meaning that the version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " +
                oldVersion + " to " + newVersion + ". all data destroyed.");
        db.execSQL(FavoriteDatabaseContract.QuakeColumns.DELETE_TABLE);
        Log.d(TAG, "database dropped.");
        db.execSQL(FavoriteDatabaseContract.QuakeColumns.CREATE_TABLE);
    }

    // method for inserting MyMarker objects into the database
    public void insert(SQLiteDatabase db, String favoriteTruckName) {
        ContentValues values = new ContentValues();

        values.put(FavoriteDatabaseContract.QuakeColumns.COLUMN_FAVORITE, favoriteTruckName);

        db.insert(FavoriteDatabaseContract.QuakeColumns.TABLE_NAME, null, values);
    }

    public void remove(SQLiteDatabase db, String favoriteTruckName){

        db.delete(TABLE_NAME, COLUMN_FAVORITE + "=" + favoriteTruckName, null);
    }

}