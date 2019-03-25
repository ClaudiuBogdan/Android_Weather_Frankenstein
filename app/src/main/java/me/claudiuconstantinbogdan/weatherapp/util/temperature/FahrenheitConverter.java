package me.claudiuconstantinbogdan.weatherapp.util.temperature;

public class FahrenheitConverter implements TemperatureConverter {
    @Override
    public double convertTemperature(double fahrenheitTemperature) {
        return fahrenheitTemperature;
    }

    @Override
    public String getTemperatureUnits() {
        return "°F";
    }
}
