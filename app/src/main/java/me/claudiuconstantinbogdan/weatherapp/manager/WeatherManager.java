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
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.MainThread;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import me.claudiuconstantinbogdan.weatherapp.data.WeatherData;
import me.claudiuconstantinbogdan.weatherapp.events.IWeatherListener;
import me.claudiuconstantinbogdan.weatherapp.network.WeatherService;
import me.claudiuconstantinbogdan.weatherapp.storage.WeatherDataContract;
import me.claudiuconstantinbogdan.weatherapp.storage.WeatherDbHelper;
import me.claudiuconstantinbogdan.weatherapp.storage.WeatherDbManager;
import me.claudiuconstantinbogdan.weatherapp.util.GeocodeUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherManager {

    public static final String TAG = WeatherManager.class.getCanonicalName();

    private Context mContext;
    private IWeatherListener mWeatherListener;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private NetworkManager mNetworkManager;
    private WeatherService mWeatherService;
    private WeatherDbManager mWeatherDbManager;

    public WeatherManager(Context context, IWeatherListener weatherListener){
        this.mContext = context;
        this.mWeatherListener = weatherListener;
        this.mNetworkManager = NetworkManager.getInstance();
        this.mWeatherService = NetworkManager.getWeatherService();
        this.mWeatherDbManager = WeatherDbManager.getInstance(context);
    }

    public void initLocationListener() throws SecurityException{
        startNetworkHandlerThread();
        startDatabaseHandlerThread();
        loadDataFromDatabase();
        mLocationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
    }

    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        @MainThread
        @Override
        public void onLocationChanged(Location loc) {
            double longitude = loc.getLongitude();
            double latitude = loc.getLatitude();

            fetchWeatherDataFromNetwork(latitude, longitude);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }



    private Handler mNetworkHandler = null;
    private HandlerThread mNetworkHandlerThread = null;
    public void startNetworkHandlerThread(){
        mNetworkHandlerThread = new HandlerThread("NetworkHandlerThread");
        mNetworkHandlerThread.start();
        mNetworkHandler = new Handler(mNetworkHandlerThread.getLooper());
    }


    @MainThread
    private void fetchWeatherDataFromNetwork(double longitude, double latitude){
        Runnable runnable =  ()-> {
            try {
                String weatherJsonData = mWeatherService.getWeatherData(longitude, latitude);
                blockDatabaseLoadToUI();
                postWeatherData(weatherJsonData);
                mWeatherDbManager.saveDataIntoDatabase(weatherJsonData);

            } catch (IOException e) {
                //Display error message
                e.printStackTrace();
            }
        };
        // Start the initial runnable task by posting through the handler
        mNetworkHandler.post(runnable);
    }


    private Gson gson = new Gson();

    @WorkerThread
    private void postWeatherData(String weatherJsonData){
        WeatherData weatherData = gson.fromJson(weatherJsonData, WeatherData.class);
        try{
            String cityName = GeocodeUtil.getLocationName(mContext, weatherData.getLongitude(), weatherData.getLatitude());
            weatherData.setCity(cityName);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        mWeatherListener.onWeatherUpdate(weatherData);
    }



    private Handler mDatabaseHandler = null;
    private HandlerThread mDatabaseHandlerThread = null;
    public void startDatabaseHandlerThread(){
        mDatabaseHandlerThread = new HandlerThread("DatabaseHandlerThread");
        mDatabaseHandlerThread.start();
        mDatabaseHandler = new Handler(mNetworkHandlerThread.getLooper());
    }

    @WorkerThread
    private void loadDataFromDatabase(){
        Runnable runnable = () -> {
            String data = mWeatherDbManager.loadDataFromDatabase();
            if(!isUIBlocked && data != null)
                postWeatherData(data);
        };
        mDatabaseHandler.post(runnable);
    }

    private boolean isUIBlocked = false;
    private void blockDatabaseLoadToUI(){
        isUIBlocked = true;
    }



    public void destroy(){
        mNetworkManager.destroy();
        mWeatherDbManager.destroy();
        mLocationManager.removeUpdates(mLocationListener);
        mNetworkHandler.removeCallbacksAndMessages(null);
        mDatabaseHandler.removeCallbacksAndMessages(null);
        mNetworkHandlerThread.quit();
        mDatabaseHandlerThread.quit();

        mNetworkManager = null;
        mWeatherDbManager = null;
        mNetworkHandler = null;
        mDatabaseHandler = null;
        mNetworkHandlerThread = null;
        mDatabaseHandlerThread = null;
        mContext = null;
        mWeatherListener = null;
        mLocationManager = null;
        mLocationListener = null;
    }
}
