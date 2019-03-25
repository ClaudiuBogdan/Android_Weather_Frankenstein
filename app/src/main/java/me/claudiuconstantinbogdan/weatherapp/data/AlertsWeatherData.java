package me.claudiuconstantinbogdan.weatherapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AlertsWeatherData implements Parcelable {
    private String title;
    private String[] regions;
    private String severity;
    private long time;
    private long expires;
    private String description;

    protected AlertsWeatherData(Parcel in) {
        title = in.readString();
        regions = in.createStringArray();
        severity = in.readString();
        time = in.readLong();
        expires = in.readLong();
        description = in.readString();
    }

    public static final Creator<AlertsWeatherData> CREATOR = new Creator<AlertsWeatherData>() {
        @Override
        public AlertsWeatherData createFromParcel(Parcel in) {
            return new AlertsWeatherData(in);
        }

        @Override
        public AlertsWeatherData[] newArray(int size) {
            return new AlertsWeatherData[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getRegions() {
        return regions;
    }

    public void setRegions(String[] regions) {
        this.regions = regions;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringArray(regions);
        dest.writeString(severity);
        dest.writeLong(time);
        dest.writeLong(expires);
        dest.writeString(description);
    }
}
