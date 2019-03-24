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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.claudiuconstantinbogdan.weatherapp.R;
import me.claudiuconstantinbogdan.weatherapp.events.IWeatherListener;
import me.claudiuconstantinbogdan.weatherapp.manager.WeatherManager;
import me.claudiuconstantinbogdan.weatherapp.receiver.NetworkChangeReceiver;

public class MainFragment extends Fragment implements IWeatherListener {

    private TextView textView;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        textView = view.findViewById(R.id.helloTextFragment);
        if(savedInstanceState != null){
            String weatherData = savedInstanceState.getString(weatherDataSaveKey);
            textView.setText(weatherData);
        }
        return view;
    }

    private final String weatherDataSaveKey = "weatherDataSaveKey";
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(weatherDataSaveKey, textView.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        registerNetworkBroadcastForNougat();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        weatherManager.destroy();
        unregisterNetworkChanges();
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
    public void onWeatherUpdate(String weatherData) {
        getActivity().runOnUiThread(() -> textView.setText(weatherData));

    }
}
