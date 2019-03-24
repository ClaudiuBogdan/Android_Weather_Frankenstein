package me.claudiuconstantinbogdan.weatherapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import me.claudiuconstantinbogdan.weatherapp.R;
import me.claudiuconstantinbogdan.weatherapp.data.WeatherData;
import me.claudiuconstantinbogdan.weatherapp.events.IWeatherListener;
import me.claudiuconstantinbogdan.weatherapp.manager.WeatherManager;
import me.claudiuconstantinbogdan.weatherapp.receiver.NetworkChangeReceiver;

public class MainFragment extends Fragment implements IWeatherListener {

    public static final String TAG = MainFragment.class.getCanonicalName();
    private TextView tvTemperature, tvWeatherDescription, tvMaxTemperature, tvMinTemperature;
    private WeatherManager weatherManager;
    private NetworkChangeReceiver mNetworkReceiver;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherManager = new WeatherManager(getContext(), this);
        mNetworkReceiver = new NetworkChangeReceiver();
        getUserLocation();


        Toast.makeText(getContext(), "Fragment created.", Toast.LENGTH_LONG).show();
        Log.d(TAG, "MainFragment::onCreateView");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        bindViews(view);

        if(savedInstanceState != null){
            String weatherData = savedInstanceState.getString(weatherDataSaveKey);
            tvTemperature.setText(weatherData);
        }
        setRetainInstance(true);
        return view;
    }

    private void bindViews(View view) {
        tvTemperature = view.findViewById(R.id.tv_temperature);
    }

    private final String weatherDataSaveKey = "weatherDataSaveKey";
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(weatherDataSaveKey, tvTemperature.getText().toString()); //TODO: add string
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        registerNetworkBroadcastForNougat();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unregisterNetworkChanges();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        weatherManager.destroy();
    }

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(),
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
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            getActivity().unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWeatherUpdate(WeatherData weatherData) {
        getActivity().runOnUiThread(() -> {

            String s = weatherData.getLongitude() + "\n" + weatherData.getLatitude() + "\n\nMy Current City is: "
                     + "\n" + "Temperature: " + weatherData.getCurrently().getTemperature();
            tvTemperature.setText(weatherData.getCurrently().getTemperature() + "");
        });

    }
}
