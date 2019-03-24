package me.claudiuconstantinbogdan.weatherapp.events;

import me.claudiuconstantinbogdan.weatherapp.data.WeatherData;

public interface IWeatherListener {
    void onWeatherUpdate(WeatherData weatherData);
}
