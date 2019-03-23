package me.claudiuconstantinbogdan.weatherapp.events;

public interface IWeatherListener {
    void onWeatherUpdate(String weatherData);

    void onTimeUpdate(int newTime);
}
