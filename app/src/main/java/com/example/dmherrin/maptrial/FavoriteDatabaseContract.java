package com.example.dmherrin.maptrial;

/**
 * Created by Nick on 4/23/2016.
 */
import android.provider.BaseColumns;

public final class FavoriteDatabaseContract {

    public static final int    DATABASE_VERSION    = 2;
    public static final String DATABASE_NAME       = "favorites.db";
    public static final String TEXT_TYPE           = " TEXT";
    public static final String COMMA_SEP           = ", ";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty private constructor.
    private FavoriteDatabaseContract() {}

    /* Inner class that defines the table contents */
    public static abstract class QuakeColumns implements BaseColumns {

        public static final String TABLE_NAME   = "favoriteTable";
        public static final String COLUMN_FAVORITE   = "favorite";
        public static final String COLUMN_STAR  = "star";


        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_FAVORITE + TEXT_TYPE + "," + COLUMN_STAR + TEXT_TYPE + ")";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}