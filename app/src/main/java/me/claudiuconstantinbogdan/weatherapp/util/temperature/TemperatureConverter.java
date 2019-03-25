package me.claudiuconstantinbogdan.weatherapp.util.temperature;

public interface TemperatureConverter {
    int convertTemperature(double fahrenheitTemperature);
    String getTemperatureUnits();
}
