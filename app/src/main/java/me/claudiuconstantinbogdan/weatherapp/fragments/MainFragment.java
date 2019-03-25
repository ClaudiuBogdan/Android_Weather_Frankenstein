package me.claudiuconstantinbogdan.weatherapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.claudiuconstantinbogdan.weatherapp.R;
import me.claudiuconstantinbogdan.weatherapp.data.CurrentWeatherData;
import me.claudiuconstantinbogdan.weatherapp.data.DailyItemWeatherData;
import me.claudiuconstantinbogdan.weatherapp.data.WeatherData;
import me.claudiuconstantinbogdan.weatherapp.events.IWeatherListener;
import me.claudiuconstantinbogdan.weatherapp.manager.WeatherManager;
import me.claudiuconstantinbogdan.weatherapp.receiver.NetworkChangeReceiver;
import me.claudiuconstantinbogdan.weatherapp.util.WindUtil;
import me.claudiuconstantinbogdan.weatherapp.util.temperature.TemperatureUnits;

public class MainFragment extends Fragment implements IWeatherListener {

    public static final String TAG = MainFragment.class.getCanonicalName();
    private TextView tvCity, tvTemperature, tvWeatherDescription, tvMaxTemperature, tvMinTemperature,
            tvWindSpeed, tvWindDirection, tvDate, tvClock;
    private RadioGroup rgTemperatureUnits;
    private WeatherManager weatherManager;
    private NetworkChangeReceiver mNetworkReceiver;
    private WeatherData mWeatherData;
    private TemperatureUnits temperaturFormatter;

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
        bindViews(view);
        setTemperatureUnitsListener();

        if(savedInstanceState != null){
            WeatherData weatherData = savedInstanceState.getParcelable(weatherDataSaveKey);
            onWeatherUpdate(weatherData);
        }
        setRetainInstance(true);
        return view;
    }

    private void bindViews(View view) {
        tvCity = view.findViewById(R.id.tv_city);
        tvTemperature = view.findViewById(R.id.tv_temperature);
        tvWeatherDescription = view.findViewById(R.id.tv_weather_description);
        tvMaxTemperature = view.findViewById(R.id.tv_temp_max);
        tvMinTemperature = view.findViewById(R.id.tv_temp_min);
        tvWindSpeed = view.findViewById(R.id.tv_wind_speed);
        tvWindDirection = view.findViewById(R.id.tv_wind_direction);
        tvDate = view.findViewById(R.id.tv_date);
        tvClock = view.findViewById(R.id.tv_clock);
        rgTemperatureUnits = view.findViewById(R.id.rg_temperature_units);
    }

    private void setTemperatureUnitsListener() {
        int initialCheckedId = rgTemperatureUnits.getCheckedRadioButtonId();
        changeTemperatureUnits(initialCheckedId);

        rgTemperatureUnits.setOnCheckedChangeListener((group, checkedId) -> {
            changeTemperatureUnits(checkedId);
        });
    }

    private void changeTemperatureUnits(int checkedId){
        switch (checkedId){
            case R.id.rb_celsius:
                temperaturFormatter = TemperatureUnits.CELSIUS;
                Log.d("TempUnits", "Group id: " + "Celsius");
                break;
            case R.id.rb_fahrenheit:
                temperaturFormatter = TemperatureUnits.FAHRENHEIT;
                Log.d("TempUnits", "Group id: " + "Fahrenheit");
                break;
            default:
                break;
        }
        updateTemperatureUnits();

    }

    private void updateTemperatureUnits() {
        if(mWeatherData == null)
            return;

        CurrentWeatherData currentWeather = mWeatherData.getCurrently();
        DailyItemWeatherData todayWeather = mWeatherData.getDaily().getData().get(0);

        String currentTemp = temperaturFormatter.getFormattedTemperature(currentWeather.getTemperature());
        String maxTemp = temperaturFormatter.getFormattedTemperature(todayWeather.getTemperatureMax());
        String minTemp = temperaturFormatter.getFormattedTemperature(todayWeather.getTemperatureMin());

        tvTemperature.setText(currentTemp);
        tvMaxTemperature.setText(maxTemp);
        tvMinTemperature.setText(minTemp);

    }

    private final String weatherDataSaveKey = "weatherDataSaveKey";
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(weatherDataSaveKey, mWeatherData);
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
    public void onWeatherUpdate(@NonNull WeatherData weatherData) {
        getActivity().runOnUiThread(() -> {
            this.mWeatherData = weatherData;
            CurrentWeatherData currentWeather = weatherData.getCurrently();
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E dd/MM/yyyy");
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("h:mm a");
            String currentDate = simpleDateFormat.format(date);
            String currentTime = simpleTimeFormat.format(date);
            tvDate.setText(currentDate);
            tvClock.setText(currentTime);

            tvCity.setText(weatherData.getCity());
            tvWeatherDescription.setText(currentWeather.getSummary());

            String windDirection = WindUtil.getWindDirection(currentWeather.getWindBearing());
            String windSpeed = WindUtil.getWindSpeedInMetricUnits(currentWeather.getWindSpeed());
            tvWindSpeed.setText(windSpeed);
            tvWindDirection.setText(windDirection);

            updateTemperatureUnits();

        });

    }
}
