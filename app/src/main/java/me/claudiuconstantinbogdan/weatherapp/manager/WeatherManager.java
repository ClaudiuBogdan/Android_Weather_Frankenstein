package me.claudiuconstantinbogdan.weatherapp.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import me.claudiuconstantinbogdan.weatherapp.data.WeatherData;
import me.claudiuconstantinbogdan.weatherapp.events.IWeatherListener;
import me.claudiuconstantinbogdan.weatherapp.storage.WeatherDataContract;
import me.claudiuconstantinbogdan.weatherapp.storage.WeatherDbHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherManager {

    private Context mContext;
    private IWeatherListener mWeatherListener;

    public WeatherManager(Context context, IWeatherListener weatherListener){
        this.mContext = context;
        this.mWeatherListener = weatherListener;
    }

    public void initLocationListener() throws SecurityException{
        String data = loadDataFromDatabase();
        postWeatherData(data);
//        LocationManager locationManager = (LocationManager)
//                mContext.getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locationListener = new MyLocationListener();
//        locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
//            textView.setText("");
            Toast.makeText(
                    mContext,
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            double longitude = loc.getLongitude();
            double latitude = loc.getLatitude();



            testOkHTTP(latitude, longitude);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    private void testOkHTTP(double longitude, double latitude) {
        try {
            run("https://api.darksky.net/forecast/2bb07c3bece89caf533ac9a5d23d8417/" + longitude +"," + latitude,
                    longitude, latitude);
        } catch (IOException ex) {
        }

    }

    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    private void run(String url, double longitude, double latitude) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OKHTTP", "Error: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String weatherJsonData = response.body().string();
                //saveDataIntoDatabase(weatherJsonData);
                postWeatherData(weatherJsonData);
            }
        });
    }

    private void postWeatherData(String weatherJsonData){
        WeatherData weatherData = gson.fromJson(weatherJsonData, WeatherData.class);

        /*------- To get city name from coordinates -------- */
        String cityName = null;
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(weatherData.getLatitude(),
                    weatherData.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        String s = weatherData.getLongitude() + "\n" + weatherData.getLatitude() + "\n\nMy Current City is: "
                + cityName + "\n" + "Temperature: " + weatherData.getCurrently().getTemperature();
        mWeatherListener.onWeatherUpdate(s);
    }

    private WeatherDbHelper dbHelper;
    private void saveDataIntoDatabase(String jsonString){
        if(dbHelper == null)
            dbHelper = new WeatherDbHelper(mContext);
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WeatherDataContract.WeatherDataEntry.COLUMN_NAME_ROW_DATA, jsonString);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(WeatherDataContract.WeatherDataEntry.TABLE_NAME, null, values);
    }

    private String  loadDataFromDatabase(){
        if(dbHelper == null)
            dbHelper = new WeatherDbHelper(mContext);

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
        return  data;
    }

    private void cancelAllRequest(){
        //When you want to cancel:
        //A) go through the queued calls and cancel if the tag matches:
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }

        //B) go through the running calls and cancel if the tag matches:
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    public void destroy(){
        mContext = null;
        mWeatherListener = null;
        dbHelper.close();
        cancelAllRequest();
    }
}
