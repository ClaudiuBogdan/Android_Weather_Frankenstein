package me.claudiuconstantinbogdan.weatherapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class DailyItemWeatherData implements Parcelable {
    private long time;
    private String summary;
    private double precipIntensity;
    private double precipProbability;
    private double temperatureMin;
    private long temperatureMinTime;
    private double temperatureMax;
    private long temperatureMaxTime;
    private double apparentTemperatureMin;
    private double apparentTemperatureMinTime;
    private double apparentTemperatureMax;
    private double apparentTemperatureMaxTime;

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

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public long getTemperatureMinTime() {
        return temperatureMinTime;
    }

    public void setTemperatureMinTime(long temperatureMinTime) {
        this.temperatureMinTime = temperatureMinTime;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public long getTemperatureMaxTime() {
        return temperatureMaxTime;
    }

    public void setTemperatureMaxTime(long temperatureMaxTime) {
        this.temperatureMaxTime = temperatureMaxTime;
    }

    public double getApparentTemperatureMin() {
        return apparentTemperatureMin;
    }

    public void setApparentTemperatureMin(double apparentTemperatureMin) {
        this.apparentTemperatureMin = apparentTemperatureMin;
    }

    public double getApparentTemperatureMinTime() {
        return apparentTemperatureMinTime;
    }

    public void setApparentTemperatureMinTime(double apparentTemperatureMinTime) {
        this.apparentTemperatureMinTime = apparentTemperatureMinTime;
    }

    public double getApparentTemperatureMax() {
        return apparentTemperatureMax;
    }

    public void setApparentTemperatureMax(double apparentTemperatureMax) {
        this.apparentTemperatureMax = apparentTemperatureMax;
    }

    public double getApparentTemperatureMaxTime() {
        return apparentTemperatureMaxTime;
    }

    public void setApparentTemperatureMaxTime(double apparentTemperatureMaxTime) {
        this.apparentTemperatureMaxTime = apparentTemperatureMaxTime;
    }

    protected DailyItemWeatherData(Parcel in) {
        time = in.readLong();
        summary = in.readString();
        precipIntensity = in.readDouble();
        precipProbability = in.readDouble();
        temperatureMin = in.readDouble();
        temperatureMinTime = in.readLong();
        temperatureMax = in.readDouble();
        temperatureMaxTime = in.readLong();
        apparentTemperatureMin = in.readDouble();
        apparentTemperatureMinTime = in.readDouble();
        apparentTemperatureMax = in.readDouble();
        apparentTemperatureMaxTime = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeDouble(precipIntensity);
        dest.writeDouble(precipProbability);
        dest.writeDouble(temperatureMin);
        dest.writeLong(temperatureMinTime);
        dest.writeDouble(temperatureMax);
        dest.writeLong(temperatureMaxTime);
        dest.writeDouble(apparentTemperatureMin);
        dest.writeDouble(apparentTemperatureMinTime);
        dest.writeDouble(apparentTemperatureMax);
        dest.writeDouble(apparentTemperatureMaxTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DailyItemWeatherData> CREATOR = new Creator<DailyItemWeatherData>() {
        @Override
        public DailyItemWeatherData createFromParcel(Parcel in) {
            return new DailyItemWeatherData(in);
        }

        @Override
        public DailyItemWeatherData[] newArray(int size) {
            return new DailyItemWeatherData[size];
        }
    };
}
