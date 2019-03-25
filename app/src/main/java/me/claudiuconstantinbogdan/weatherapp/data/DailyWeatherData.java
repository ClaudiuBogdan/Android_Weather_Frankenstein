package me.claudiuconstantinbogdan.weatherapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DailyWeatherData implements Parcelable {
    private String summary;

    protected DailyWeatherData(Parcel in) {
        summary = in.readString();
    }

    public static final Creator<DailyWeatherData> CREATOR = new Creator<DailyWeatherData>() {
        @Override
        public DailyWeatherData createFromParcel(Parcel in) {
            return new DailyWeatherData(in);
        }

        @Override
        public DailyWeatherData[] newArray(int size) {
            return new DailyWeatherData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(summary);
    }
}
