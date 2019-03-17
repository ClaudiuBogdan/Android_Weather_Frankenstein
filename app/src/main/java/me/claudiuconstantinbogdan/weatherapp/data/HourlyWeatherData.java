package me.claudiuconstantinbogdan.weatherapp.data;

import java.util.List;

public class HourlyWeatherData {
    private String summary;
    List<CurrentWeatherData> data;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<CurrentWeatherData> getData() {
        return data;
    }

    public void setData(List<CurrentWeatherData> data) {
        this.data = data;
    }
}
