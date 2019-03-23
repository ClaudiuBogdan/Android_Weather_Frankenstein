package me.claudiuconstantinbogdan.weatherapp.manager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import me.claudiuconstantinbogdan.weatherapp.activities.MainActivity;
import me.claudiuconstantinbogdan.weatherapp.data.WeatherData;
import me.claudiuconstantinbogdan.weatherapp.events.IWeatherListener;
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


    @SuppressLint("MissingPermission")
    public void initLocationListener(){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
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

            /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }


            testOkHTTP(latitude, longitude, cityName);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    private void testOkHTTP(double longitude, double latitude, String cityName) {
        try {
            run("https://api.darksky.net/forecast/2bb07c3bece89caf533ac9a5d23d8417/" + longitude +"," + latitude,
                    longitude, latitude, cityName);
        } catch (IOException ex) {
        }

    }

    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    private void run(String url, double longitude, double latitude, String cityName) throws IOException {
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
                WeatherData weatherData = gson.fromJson(weatherJsonData, WeatherData.class);
                Log.d("OKHTTP", gson.toJson(weatherData));
                Log.d("OKHTTP", response.toString());


                    String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                            + cityName + "\n" + "Temperature: " + weatherData.getCurrently().getTemperature();
                    mWeatherListener.onWeatherUpdate(s);
            }
        });
    }
}
