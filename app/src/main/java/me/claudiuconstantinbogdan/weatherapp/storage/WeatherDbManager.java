package me.claudiuconstantinbogdan.weatherapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.WorkerThread;

public class WeatherDbManager {

    private static WeatherDbManager mInstance;
    private Context mContext;
    private WeatherDbHelper dbHelper;

    private WeatherDbManager(Context context){
        this.mContext = context;
        this.dbHelper = new WeatherDbHelper(mContext);
    }

    public static WeatherDbManager getInstance(Context context){
        if(mInstance == null)
            mInstance = new WeatherDbManager(context);
        return mInstance;
    }

    @WorkerThread
    public void saveDataIntoDatabase(String jsonString){
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.deleteAll(db);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WeatherDataContract.WeatherDataEntry.COLUMN_NAME_ROW_DATA, jsonString);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(WeatherDataContract.WeatherDataEntry.TABLE_NAME, null, values);
    }

    @WorkerThread
    public String loadDataFromDatabase(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                WeatherDataContract.WeatherDataEntry.COLUMN_NAME_ROW_DATA
        };


        Cursor cursor = db.query(
                WeatherDataContract.WeatherDataEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        cursor.moveToNext();

        String data = cursor.getString(
                cursor.getColumnIndexOrThrow(WeatherDataContract.WeatherDataEntry.COLUMN_NAME_ROW_DATA));
        cursor.close();
        return data;
    }

    public void destroy(){
        dbHelper.close();
    }
}
