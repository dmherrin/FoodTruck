package com.example.dmherrin.maptrial;

/**
 * Created by dmherrin on 4/26/16.
 */
import android.provider.BaseColumns;

public final class FoodTruckDatabaseContact {

    public static final int    DATABASE_VERSION    = 1;
    public static final String DATABASE_NAME       = "foodtrucks.db";
    public static final String TEXT_TYPE           = " TEXT";
    public static final String COMMA_SEP           = ", ";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty private constructor.
    private FoodTruckDatabaseContact() {}

    /* Inner class that defines the table contents */
    public static abstract class TruckColumns implements BaseColumns {

        public static final String TABLE_NAME   = "truckTable";
        public static final String COLUMN_TRUCK   = "truck";
        public static final String COLUMN_LOCATION   = "location";


        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_TRUCK + TEXT_TYPE + "," +
                        COLUMN_LOCATION + TEXT_TYPE + ")";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
