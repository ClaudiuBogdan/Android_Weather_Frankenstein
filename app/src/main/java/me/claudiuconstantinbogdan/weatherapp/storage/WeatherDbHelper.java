package me.claudiuconstantinbogdan.weatherapp.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import me.claudiuconstantinbogdan.weatherapp.storage.WeatherDataContract.*;

public class WeatherDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WeatherData.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WeatherDataEntry.TABLE_NAME + " (" +
                    WeatherDataEntry._ID + " INTEGER PRIMARY KEY," +
                    WeatherDataEntry.COLUMN_NAME_ROW_DATA + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WeatherDataEntry.TABLE_NAME;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
