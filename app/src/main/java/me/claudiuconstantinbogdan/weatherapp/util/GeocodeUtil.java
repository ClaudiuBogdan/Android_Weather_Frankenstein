package me.claudiuconstantinbogdan.weatherapp.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.WorkerThread;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodeUtil {

    @WorkerThread
    public static String getLocationName(Context mContext, double longitude, double latitude) throws IOException {
        /*------- To get city name from coordinates -------- */
        String cityName = null;
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        addresses = gcd.getFromLocation(latitude,
                longitude, 1);
        if (addresses.size() > 0) {
            System.out.println(addresses.get(0).getLocality());
            cityName = addresses.get(0).getLocality();
        }
        return cityName;
    }
}
