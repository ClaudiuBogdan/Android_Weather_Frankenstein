package me.claudiuconstantinbogdan.weatherapp.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import me.claudiuconstantinbogdan.weatherapp.util.NetworkStatus;
import me.claudiuconstantinbogdan.weatherapp.util.NetworkStatusUtil;
import me.claudiuconstantinbogdan.weatherapp.util.WindUtil;
import me.claudiuconstantinbogdan.weatherapp.util.temperature.TemperatureUnits;

public class MainFragment extends Fragment implements IWeatherListener {

    public static final String TAG = MainFragment.class.getCanonicalName();
    private TextView tvCity, tvTemperature, tvTemperatureUnits, tvWeatherDescription, tvMaxTemperature, tvMinTemperature,
            tvWindSpeed, tvWindDirection, tvDate, tvClock;
    private TextView tvNetworkDisconnected, tvLocationPermission;
    private RadioGroup rgTemperatureUnits;
    private WeatherManager weatherManager;
    private BroadcastReceiver mNetworkReceiver;
    private WeatherData mWeatherData;
    private TemperatureUnits temperatureFormatter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherManager = new WeatherManager(getContext(), this);
        initNetworkListener();
        initWeatherManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        bindViews(view);
        setTemperatureUnitsListener();
        setLocationPermissionListener();
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
        tvTemperatureUnits = view.findViewById(R.id.tv_temp_units);
        tvWeatherDescription = view.findViewById(R.id.tv_weather_description);
        tvMaxTemperature = view.findViewById(R.id.tv_temp_max);
        tvMinTemperature = view.findViewById(R.id.tv_temp_min);
        tvWindSpeed = view.findViewById(R.id.tv_wind_speed);
        tvWindDirection = view.findViewById(R.id.tv_wind_direction);
        tvDate = view.findViewById(R.id.tv_date);
        tvClock = view.findViewById(R.id.tv_clock);
        rgTemperatureUnits = view.findViewById(R.id.rg_temperature_units);

        tvNetworkDisconnected = view.findViewById(R.id.tv_network_disconnected);
        tvLocationPermission = view.findViewById(R.id.tv_location_permission);
    }

    private void initNetworkListener() {
        mNetworkReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                NetworkStatus status = NetworkStatusUtil.getConnectivityStatusEnum(context);
                if(status == NetworkStatus.CONNECTED){
                    tvNetworkDisconnected.setVisibility(View.GONE);
                }
                else{
                    tvNetworkDisconnected.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private void setLocationPermissionListener() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
            tvLocationPermission.setVisibility(View.VISIBLE);
        tvLocationPermission.setOnClickListener((view) -> {
            initWeatherManager();
        });
    }

    private void disableLocationPermissionListener(){
        if(tvLocationPermission != null)
            tvLocationPermission.setVisibility(View.GONE);
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
                temperatureFormatter = TemperatureUnits.CELSIUS;
                break;
            case R.id.rb_fahrenheit:
                temperatureFormatter = TemperatureUnits.FAHRENHEIT;
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

        String currentTemp = temperatureFormatter.getConvertedTemperature(currentWeather.getTemperature());
        String temperatureUnits = temperatureFormatter.getTemperatureUnits();
        String maxTemp = "Max: " + temperatureFormatter.getFormattedTemperature(todayWeather.getTemperatureMax());
        String minTemp = "Min: " + temperatureFormatter.getFormattedTemperature(todayWeather.getTemperatureMin());

        tvTemperature.setText(currentTemp);
        tvTemperatureUnits.setText(temperatureUnits);
        tvMaxTemperature.setText(maxTemp);
        tvMinTemperature.setText(minTemp);
    }

    @Override
    public void onWeatherUpdate(@NonNull WeatherData weatherData) {
        getActivity().runOnUiThread(() -> {
            this.mWeatherData = weatherData;
            CurrentWeatherData currentWeather = weatherData.getCurrently();

            tvCity.setText(weatherData.getCity());
            tvWeatherDescription.setText(currentWeather.getSummary());

            String windDirection = "Wind direction: " + WindUtil.getWindDirection(currentWeather.getWindBearing());
            String windSpeed = "Wind speed: " + WindUtil.getWindSpeedInMetricUnits(currentWeather.getWindSpeed());
            tvWindSpeed.setText(windSpeed);
            tvWindDirection.setText(windDirection);

            updateTemperatureUnits();

        });
    }

    private void displayDateAndTime(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E dd/MM/yyyy");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("h:mm a");
        String currentDate = simpleDateFormat.format(date);
        String currentTime = simpleTimeFormat.format(date);
        tvDate.setText(currentDate);
        tvClock.setText(currentTime);
    }

    private Handler dateHandler = new Handler();
    private Runnable dateRunnable;
    private void initializeDateAndTime(){
        dateRunnable = () -> {
            displayDateAndTime();

            int elapsedSeconds = Calendar.getInstance().get(Calendar.SECOND);
            int totalSecond = 60;
            long updateInterval = (totalSecond - elapsedSeconds) * 1000;
            dateHandler.postDelayed(dateRunnable, updateInterval);
        };
        dateHandler.post(dateRunnable);
    }

    private void destroyDateAndTime(){
    if(dateRunnable != null)
        dateHandler.removeCallbacks(dateRunnable);
    }

    private final String weatherDataSaveKey = "weatherDataSaveKey";
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(weatherDataSaveKey, mWeatherData);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerNetworkBroadcastForNougat();
        initializeDateAndTime();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterNetworkChanges();
        destroyDateAndTime();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        weatherManager.destroy();
    }

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    private void initWeatherManager() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        weatherManager.initLocationListener();
        disableLocationPermissionListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initWeatherManager();
                }
                return;
            }
        }
    }

    private void registerNetworkBroadcastForNougat() {
        getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void unregisterNetworkChanges() {
        try {
            getActivity().unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
