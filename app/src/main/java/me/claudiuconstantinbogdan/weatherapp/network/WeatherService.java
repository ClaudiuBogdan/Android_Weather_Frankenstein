package me.claudiuconstantinbogdan.weatherapp.network;

import java.io.IOException;

public interface WeatherService {
    String getWeatherData(double longitude, double latitude) throws IOException;
}
