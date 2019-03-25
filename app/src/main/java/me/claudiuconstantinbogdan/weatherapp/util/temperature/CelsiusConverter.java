package me.claudiuconstantinbogdan.weatherapp.util.temperature;

public class CelsiusConverter implements TemperatureConverter {

    @Override
    public int convertTemperature(double fahrenheitTemperature) {
        double celsius = ((fahrenheitTemperature - 32)*5)/9.0;
        return (int)Math.round(celsius);
    }

    @Override
    public String getTemperatureUnits() {
        return " °C";
    }
}
