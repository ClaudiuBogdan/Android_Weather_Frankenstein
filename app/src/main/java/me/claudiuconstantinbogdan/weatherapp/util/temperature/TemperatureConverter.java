package me.claudiuconstantinbogdan.weatherapp.util.temperature;

public interface TemperatureConverter {
    double convertTemperature(double fahrenheitTemperature);
    String getTemperatureUnits();
}
