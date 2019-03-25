package me.claudiuconstantinbogdan.weatherapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class WeatherData implements Parcelable {

    private double longitude;
    private double latitude;
    private String timezone;
    private CurrentWeatherData currently;
    private HourlyWeatherData hourly;
    private DailyWeatherData daily;
    private List<AlertsWeatherData> alerts;


    private String city = "Nowhere";



    protected WeatherData(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
        timezone = in.readString();
        currently = in.readParcelable(CurrentWeatherData.class.getClassLoader());
        hourly = in.readParcelable(HourlyWeatherData.class.getClassLoader());
        daily = in.readParcelable(DailyWeatherData.class.getClassLoader());
        alerts = in.createTypedArrayList(AlertsWeatherData.CREATOR);

        city = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(timezone);
        dest.writeParcelable(currently, flags);
        dest.writeParcelable(hourly, flags);
        dest.writeParcelable(daily, flags);
        dest.writeTypedList(alerts);

        dest.writeString(city);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherData> CREATOR = new Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
