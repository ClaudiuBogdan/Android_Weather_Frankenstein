package me.claudiuconstantinbogdan.weatherapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class HourlyWeatherData implements Parcelable {
    private String summary;
    List<CurrentWeatherData> data;

    protected HourlyWeatherData(Parcel in) {
        summary = in.readString();
        data = in.createTypedArrayList(CurrentWeatherData.CREATOR);
    }

    public static final Creator<HourlyWeatherData> CREATOR = new Creator<HourlyWeatherData>() {
        @Override
        public HourlyWeatherData createFromParcel(Parcel in) {
            return new HourlyWeatherData(in);
        }

        @Override
        public HourlyWeatherData[] newArray(int size) {
            return new HourlyWeatherData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(summary);
        dest.writeTypedList(data);
    }
}
