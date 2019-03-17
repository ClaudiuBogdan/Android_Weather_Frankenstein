package me.claudiuconstantinbogdan.weatherapp.data;

import java.util.List;

public class WeatherData {

    private double longitude;
    private String timezone;
    private CurrentWeatherData currently;
    private HourlyWeatherData hourly;
    private DailyWeatherData daily;
    private List<AlertsWeatherData> alerts;

    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public CurrentWeatherData getCurrently() {
        return currently;
    }

    public void setCurrently(CurrentWeatherData currently) {
        this.currently = currently;
    }

    public HourlyWeatherData getHourly() {
        return hourly;
    }

    public void setHourly(HourlyWeatherData hourly) {
        this.hourly = hourly;
    }

    public DailyWeatherData getDaily() {
        return daily;
    }

    public void setDaily(DailyWeatherData daily) {
        this.daily = daily;
    }

    public List<AlertsWeatherData> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertsWeatherData> alerts) {
        this.alerts = alerts;
    }
}
