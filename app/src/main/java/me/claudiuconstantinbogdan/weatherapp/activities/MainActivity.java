package me.claudiuconstantinbogdan.weatherapp.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import me.claudiuconstantinbogdan.weatherapp.R;
import me.claudiuconstantinbogdan.weatherapp.data.WeatherData;
import me.claudiuconstantinbogdan.weatherapp.events.IWeatherListener;
import me.claudiuconstantinbogdan.weatherapp.manager.WeatherManager;
import me.claudiuconstantinbogdan.weatherapp.storage.WeatherDataContract;
import me.claudiuconstantinbogdan.weatherapp.storage.WeatherDbHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements IWeatherListener {

    private TextView textView;
    private WeatherManager weatherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.helloText);
        weatherManager = new WeatherManager(this, this);
        getUserLocation();
        //startTimer(this);
    }





    @Override
    public void onWeatherUpdate(String weatherData) {
        runOnUiThread(() -> {
            textView.setText(weatherData);
        });

    }

    @Override
    public void onTimeUpdate(int newTime) {
        //textView.setText("Time: " + newTime);
    }



    private WeatherDbHelper dbHelper;
    private void saveDataIntoDatabase(String jsonString){
        if(dbHelper == null)
            dbHelper = new WeatherDbHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WeatherDataContract.WeatherDataEntry.COLUMN_NAME_ROW_DATA, jsonString);

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(WeatherDataContract.WeatherDataEntry.TABLE_NAME, null, values);
    }

    public static int counter = 0;
    public static Observable observable = new Observable();
    private void startTimer(IWeatherListener listener){
        Handler handler = new Handler();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                listener.onTimeUpdate(counter);
                counter++;
                handler.postDelayed(this, 1000);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnable);
    }

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        weatherManager.initLocationListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    weatherManager.initLocationListener();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
