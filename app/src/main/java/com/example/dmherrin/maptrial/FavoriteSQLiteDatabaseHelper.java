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

public class FavoriteSQLiteOpenHelper extends SQLiteOpenHelper{

    private static final String TAG = "MARKER";

    public FavoriteSQLiteOpenHelper(Context context) {
        super(context, FavoriteSQLiteDatabaseContract.DATABASE_NAME,
                null, FavoriteSQLiteDatabaseContract.DATABASE_VERSION);
    }

    // when no database exists in disk and the helper class needs to create a new one.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavoriteSQLiteDatabaseContract.QuakeColumns.CREATE_TABLE);
    }

    // when there is a database version mismatch meaning that the version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " +
                oldVersion + " to " + newVersion + ". all data destroyed.");
        db.execSQL(FavoriteSQLiteDatabaseContract.QuakeColumns.DELETE_TABLE);
        Log.d(TAG, "database dropped.");
        db.execSQL(FavoriteSQLiteDatabaseContract.QuakeColumns.CREATE_TABLE);
    }

    // method for inserting MyMarker objects into the database
    public void insert(SQLiteDatabase db, String favoriteTruckName) {
        ContentValues values = new ContentValues();

        values.put(FavoriteSQLiteDatabaseContract.QuakeColumns.COLUMN_FAVORITE, favoriteTruckName);

        db.insert(FavoriteSQLiteDatabaseContract.QuakeColumns.TABLE_NAME, null, values);
    }

}