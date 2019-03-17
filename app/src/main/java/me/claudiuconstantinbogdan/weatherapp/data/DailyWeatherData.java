package me.claudiuconstantinbogdan.weatherapp.data;

import java.util.List;

public class DailyWeatherData {
    private String summary;

    public List<DailyItemWeatherData> getData() {
        return data;
    }

    public void setData(List<DailyItemWeatherData> data) {
        this.data = data;
    }

    List<DailyItemWeatherData> data;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
