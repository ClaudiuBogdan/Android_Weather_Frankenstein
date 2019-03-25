package me.claudiuconstantinbogdan.weatherapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentWeatherData implements Parcelable {

    private long time;
    private String summary;
    private double precipIntensity;
    private double precipProbability;
    private double temperature;
    private double apparentTemperature;

    protected CurrentWeatherData(Parcel in) {
        time = in.readLong();
        summary = in.readString();
        precipIntensity = in.readDouble();
        precipProbability = in.readDouble();
        temperature = in.readDouble();
        apparentTemperature = in.readDouble();
    }

    public static final Creator<CurrentWeatherData> CREATOR = new Creator<CurrentWeatherData>() {
        @Override
        public CurrentWeatherData createFromParcel(Parcel in) {
            return new CurrentWeatherData(in);
        }

        @Override
        public CurrentWeatherData[] newArray(int size) {
            return new CurrentWeatherData[size];
        }
    };

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getPrecipIntensity() {
        return precipIntensity;
    }

    public void setPrecipIntensity(double precipIntensity) {
        this.precipIntensity = precipIntensity;
    }

    public double getPrecipProbability() {
        return precipProbability;
    }

    public void setPrecipProbability(double precipProbability) {
        this.precipProbability = precipProbability;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeDouble(precipIntensity);
        dest.writeDouble(precipProbability);
        dest.writeDouble(temperature);
        dest.writeDouble(apparentTemperature);
    }
}
