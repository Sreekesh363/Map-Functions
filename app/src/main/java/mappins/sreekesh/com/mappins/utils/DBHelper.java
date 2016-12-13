package mappins.sreekesh.com.mappins.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mappins.sreekesh.com.mappins.model.Contract;

/**
 * Created by sree on 12/12/16.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "map_data.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_PINS_TABLE = "CREATE TABLE " + Contract.MapDataEntry.TABLE_NAME + " (" +
                Contract.MapDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.MapDataEntry.COLUMN_MAP_DATA_ID + " INTEGER UNIQUE, " +
                Contract.MapDataEntry.COLUMN_MAP_DATA_NAME + " TEXT NOT NULL," +
                Contract.MapDataEntry.COLUMN_MAP_DATA_LATITUDE + " TEXT NOT NULL," +
                Contract.MapDataEntry.COLUMN_MAP_DATA_LONGITUDE + " TEXT NOT NULL, " +
                Contract.MapDataEntry.COLUMN_MAP_DATA_ADDRESS + " TEXT," +
                "UNIQUE (" + Contract.MapDataEntry.COLUMN_MAP_DATA_ID +") ON CONFLICT REPLACE"+
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_PINS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.MapDataEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
