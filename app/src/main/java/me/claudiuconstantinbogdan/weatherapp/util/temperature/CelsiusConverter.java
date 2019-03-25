package me.claudiuconstantinbogdan.weatherapp.util.temperature;

public class CelsiusConverter implements TemperatureConverter {

    @Override
    public double convertTemperature(double fahrenheitTemperature) {
        return ((fahrenheitTemperature - 32)*5)/9;
    }

    @Override
    public String getTemperatureUnits() {
        return "°C";
    }
}
