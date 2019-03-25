package me.claudiuconstantinbogdan.weatherapp.util.temperature;

public enum TemperatureUnits {
    CELSIUS (new CelsiusConverter()),
    FAHRENHEIT   (new FahrenheitConverter());

    private final TemperatureConverter temperatureConverter;
    TemperatureUnits(TemperatureConverter temperatureConverter) {
        this.temperatureConverter = temperatureConverter;
    }

    public String getFormattedTemperature(double fahrenheitTemperature) {
        return temperatureConverter.convertTemperature(fahrenheitTemperature) +
                temperatureConverter.getTemperatureUnits();
    }

}
