package me.claudiuconstantinbogdan.weatherapp.util.temperature;

public class FahrenheitConverter implements TemperatureConverter {
    @Override
    public int convertTemperature(double fahrenheitTemperature) {
        return (int)Math.round(fahrenheitTemperature);
    }

    @Override
    public String getTemperatureUnits() {
        return " °F";
    }
}
